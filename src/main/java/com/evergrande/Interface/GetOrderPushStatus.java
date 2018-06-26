package com.evergrande.Interface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

public class GetOrderPushStatus {
	private String url = null;
	private CloseableHttpClient httpclient;
	private int statusCode;
	private BasicCookieStore cookieStore;
	public String geturl() {
		return url;
	}
	public GetOrderPushStatus() {
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
            
            HttpClientBuilder builder = HttpClients.custom()
    				.setDefaultCookieStore(cookieStore).setSSLSocketFactory(sslSelf).disableAutomaticRetries() // 关闭自动处理重定向
    				.setRedirectStrategy(new LaxRedirectStrategy());// 利用LaxRedirectStrategy处理POST重定向问题
    		httpclient = builder.build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
		
    }
	public String sendJsonPost(String url, String param) {
		String httpResults = null;
		this.url = url;
		String params = param;
		HttpClientBuilder builder = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).disableAutomaticRetries() // 关闭自动处理重定向
				.setRedirectStrategy(new LaxRedirectStrategy());// 利用LaxRedirectStrategy处理POST重定向问题

		httpclient = builder.build();
		// CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			httppost.addHeader("Content-type",
					"application/json; charset=utf-8");
			httppost.setHeader("Accept", "application/json");
			StringEntity requestEntity;
			requestEntity = new StringEntity(params, "utf-8");

			httppost.setEntity(requestEntity);
			// System.out.println("executing request " + httppost.getURI());
			response = httpclient.execute(httppost);
			this.setStatusCode(response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				httpResults = EntityUtils.toString(entity, "UTF-8");
				// System.out.println("--------------------------------------");
				// System.out.println("Response content: " + httpResults);
				// System.out.println("--------------------------------------");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {

				httpclient.close();
				if (response != null) {
					response.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return httpResults;
	}

	public String sendx_www_form_urlencodedPost(String url, String param) {
		String httpResults = null;
		this.url = url;
		cookieStore = new BasicCookieStore();
		HttpClientBuilder builder = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).disableAutomaticRetries() // 关闭自动处理重定向
				.setRedirectStrategy(new LaxRedirectStrategy());// 利用LaxRedirectStrategy处理POST重定向问题

		httpclient = builder.build();
		// CloseableHttpClient httpclient = HttpClients.createDefault();
		// HttpClientBuilder build = HttpClients.custom();
		// HttpHost proxy = new HttpHost("127.0.0.1", 8888);
		// CloseableHttpClient httpclient = build.setProxy(proxy).build(); ///
		// 代理
		HttpPost httppost = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			httppost.setHeader("Content-type",
					"application/x-www-form-urlencoded");
			StringEntity reqEntity = new StringEntity(param);
			httppost.setEntity(reqEntity);
			// System.out.println("executing request " + httppost.getURI());
			response = httpclient.execute(httppost);
			String JSESSIONID = null;
            String cookie_user = null;
            List<Cookie> cookies = cookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                if (cookies.get(i).getName().equals("JSESSIONID")) {
                    JSESSIONID = cookies.get(i).getValue();
                }
                if (cookies.get(i).getName().equals("cookie_user")) {
                    cookie_user = cookies.get(i).getValue();
                }
            }
//            if (cookie_user != null) {
//                result = JSESSIONID;
//            }
			System.out.println(cookieStore.getCookies());
			this.setStatusCode(response.getStatusLine().getStatusCode());
			
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				httpResults = EntityUtils.toString(entity, "UTF-8");
				// System.out.println("--------------------------------------");
				// System.out.println("Response content: " + httpResults);
				// System.out.println("--------------------------------------");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {

				httpclient.close();
				if (response != null) {
					response.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return httpResults;
	}
	public String sendx_www_form_urlencodedPut(String url, String param) {
		String httpResults = null;
		this.url = url;
		HttpClientBuilder builder = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).disableAutomaticRetries() // 关闭自动处理重定向
				.setRedirectStrategy(new LaxRedirectStrategy());// 利用LaxRedirectStrategy处理POST重定向问题

		httpclient = builder.build();
		// CloseableHttpClient httpclient = HttpClients.createDefault();
		// HttpClientBuilder build = HttpClients.custom();
		// HttpHost proxy = new HttpHost("127.0.0.1", 8888);
		// CloseableHttpClient httpclient = build.setProxy(proxy).build(); ///
		// 代理
		HttpPut httpPut = new HttpPut(url);
		CloseableHttpResponse response = null;
		try {
			httpPut.setHeader("Content-type",
					"application/x-www-form-urlencoded");
			StringEntity reqEntity = new StringEntity(param);
			httpPut.setEntity(reqEntity);
			// System.out.println("executing request " + httppost.getURI());
			response = httpclient.execute(httpPut);
			String JSESSIONID = null;
            String cookie_user = null;
            List<Cookie> cookies = cookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                if (cookies.get(i).getName().equals("JSESSIONID")) {
                    JSESSIONID = cookies.get(i).getValue();
                }
                if (cookies.get(i).getName().equals("cookie_user")) {
                    cookie_user = cookies.get(i).getValue();
                }
            }
//            if (cookie_user != null) {
//                result = JSESSIONID;
//            }
			System.out.println(cookieStore.getCookies());
			this.setStatusCode(response.getStatusLine().getStatusCode());
			
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				httpResults = EntityUtils.toString(entity, "UTF-8");
				// System.out.println("--------------------------------------");
				// System.out.println("Response content: " + httpResults);
				// System.out.println("--------------------------------------");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {

				httpclient.close();
				if (response != null) {
					response.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return httpResults;
	}
	public String sendGet(String url) {
		//CloseableHttpClient httpClient = null;
		HttpGet httpGet = null;
		String httpResults = null;
//		 //声明
//        ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
//        //加入相关的https请求方式
//        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
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
            
            HttpClientBuilder builder = HttpClients.custom()
    				.setDefaultCookieStore(cookieStore).setSSLSocketFactory(sslSelf).disableAutomaticRetries() // 关闭自动处理重定向
    				.setRedirectStrategy(new LaxRedirectStrategy());// 利用LaxRedirectStrategy处理POST重定向问题

			httpclient = builder.build();
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(20000).setConnectTimeout(20000).build();
			httpGet = new HttpGet(url);
			httpGet.setConfig(requestConfig);
			CloseableHttpResponse response = httpclient.execute(httpGet);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				httpResults = EntityUtils.toString(httpEntity, "UTF-8");
				// System.out.println("--------------------------------------");
				// System.out.println("Response content: " + httpResults);
				// System.out.println("--------------------------------------");
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpGet != null) {
					httpGet.releaseConnection();
				}
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return httpResults;
	}


	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
