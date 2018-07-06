package com.evergrande.interfaces;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bohan on 10/21/16.
 */
public class HttpSlimUtils {
    private CloseableHttpClient httpclient;
    private BasicCookieStore cookieStore;

    public HttpSlimUtils() {
        httpclient = HttpClients.createDefault();
        cookieStore = new BasicCookieStore();

        try {
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, new TrustStrategy() {
                        //信任所有
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType) throws CertificateException {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslSelf = new SSLConnectionSocketFactory(
                    sslContext);

            httpclient = HttpClients.custom()
                    .setDefaultCookieStore(cookieStore).setSSLSocketFactory(sslSelf)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public String doGet(String url) throws Throwable {
        if (url.startsWith("<a")) {
            url = getRealURIFromTag(url);
        }
        url = url.trim();
        String result = null;
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;

        System.out.println("HTTP GET " + url);

        try {
            response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY) ||
                    (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) ||
                    (statusCode == HttpStatus.SC_SEE_OTHER) ||
                    (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                String redirectUrl = response.getLastHeader("Location").getValue();
                System.out.println("|HTTP GET " + url + "| " + "Redirect HTTP URL: " + redirectUrl);
                return redirectUrl;
            } else if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new HttpException("status code is wrong, the value is " +
                        response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(response.getEntity(), "utf-8");
            EntityUtils.consume(entity);
        } finally {
            if (response != null) {
                response.close();
            }
        }

        System.out.println("|HTTP GET " + url + "| " + "HTTP Response: " + result);
        return result;
    }

    public String doPost(String url, String bodyJson) throws Throwable {
        if (url.startsWith("<a")) {
            url = getRealURIFromTag(url);
        }
        url = url.trim();
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        System.out.println("|HTTP POST " + url + "| HTTP Request: " + bodyJson);

        try {
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setEntity(new StringEntity(bodyJson, Charset.forName("UTF-8")));
            response = httpclient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 400) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
                throw new HttpException("status code is wrong, the value is " +
                        response.getStatusLine().getStatusCode() + "; the response body is " + result);
            }

            result = EntityUtils.toString(response.getEntity(), "utf-8");
            EntityUtils.consume(response.getEntity());

        } finally {
            if (response != null) {
                response.close();
            }
        }

        System.out.println("|HTTP POST " + url + "| " + "HTTP Response: " + result);

        return result;
    }


    public String doPostForm(String url, Map<String, String> nameValueParis) throws Throwable {
        if (url.startsWith("<a")) {
            url = getRealURIFromTag(url);
        }
        url = url.trim();
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        System.out.println("|HTTP POST FORM " + url + "| HTTP Request: " + nameValueParis.toString());

        try {
            List<NameValuePair> param = new ArrayList<>();
            Iterator it = nameValueParis.entrySet().iterator();
            for (Map.Entry<String, String> entry : nameValueParis.entrySet()) {
                param.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(param, "UTF-8");
            httpPost.setEntity(postEntity);

            response = httpclient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 400) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
                throw new HttpException("status code is wrong, the value is " +
                        response.getStatusLine().getStatusCode() + "; the response body is " + result);
            }

            result = EntityUtils.toString(response.getEntity(), "utf-8");
            EntityUtils.consume(response.getEntity());

        } finally {
            if (response != null) {
                response.close();
            }
        }

        System.out.println("|HTTP POST FORM" + url + "| " + "HTTP Response: " + result);

        return result;
    }

    public void closeClient() throws Throwable {

        httpclient.close();
    }

    public void finalize() {
        try {
            closeClient();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private String getRealURIFromTag(String url) {
        Pattern p = Pattern.compile("<a href=\"(.*)\">(.*)</a>");
        Matcher m = p.matcher(url);
        if (m.find()) {
            url = m.group(1);
            System.out.println(url);
        }
        url = url.trim();

        return url;
    }
}

