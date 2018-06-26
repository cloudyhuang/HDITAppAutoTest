package com.evergrande.config;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
* @author huangxiao
* @version 创建时间：2017年8月31日 上午10:13:32
* 类说明
*/
public class JenkinsJob {
	//jenkins登录账号
		public static String username = "huangxiao";
		//jenkins登录密码
		public static String password = "Hdhxnearcj228";
		//jenkins登录url
		public static String jenkinsUrl = "http://172.16.59.251:8080";
		public static void main(String [] args) throws ClientProtocolException, IOException{
			buildAndroidApp();
		}
		
	public static boolean buildAndroidApp() throws ClientProtocolException, IOException {
		String lastBuildCmd = "curl -X GET http://172.16.59.251:8080/job/eif-android-app/job/Compile/lastSuccessfulBuild/api/json --user "+username+":"+password;
		String lastBuildResult = runCommand(lastBuildCmd);
		JSONObject lastBuildJsonObj =null;
		try{
			lastBuildJsonObj = new JSONObject(lastBuildResult);
		}
		catch(JSONException e){
			System.out.print("-----构建出现问题，提示信息："+lastBuildResult+"-----");
			return false;
		}
		String lastJobDebugValue=null;
		String lastJobEnvValue=null;
		try{
			//String lastJobDebugValue2 = JsonPath.read(lastBuildJsonObj, "$..actions[3]").toString();
			lastJobDebugValue=lastBuildJsonObj.getJSONArray("actions").getJSONObject(0).getJSONArray("parameters").getJSONObject(2).getString("value");
			lastJobEnvValue=lastBuildJsonObj.getJSONArray("actions").getJSONObject(0).getJSONArray("parameters").getJSONObject(0).getString("value");
			
		}
		catch(JSONException e){
			lastJobDebugValue=lastBuildJsonObj.getJSONArray("actions").getJSONObject(2).getJSONArray("parameters").getJSONObject(2).getString("value");
			lastJobEnvValue=lastBuildJsonObj.getJSONArray("actions").getJSONObject(2).getJSONArray("parameters").getJSONObject(0).getString("value");
		}
		long lastJobTime=lastBuildJsonObj.getLong("timestamp");	//上次构建时间
		long currentTime=System.currentTimeMillis();	//当前系统时间
		long lastJobTomorrowTime=lastJobTime+86400000;
		if(lastJobDebugValue.equals("Debug")&&lastJobEnvValue.equals("TEST")&&(currentTime<lastJobTomorrowTime)){	//当上次构建debug版本TEST环境且未超过一天
			System.out.println("-----上个包为Debug，TEST环境版本，未超过一天，无需触发-----");
			return true;
		}
		else{
		while (true) {
			String cmd = "curl -X GET http://172.16.59.251:8080/job/eif-android-app/job/Compile/api/json --user "+username+":"+password;
			String buildResult = runCommand(cmd);
			JSONObject buildJsonObj = new JSONObject(buildResult);
			int lastBuild = buildJsonObj.getJSONObject("lastBuild").getInt("number");
			int lastCompletedBuild = buildJsonObj.getJSONObject("lastCompletedBuild").getInt("number");
			int nextBuildNumber = buildJsonObj.getInt("nextBuildNumber");
			if (lastBuild == lastCompletedBuild) {
				JenkinsJob job = new JenkinsJob();
				job.buildJob("eif-android-app", true , "PackageEnvironment=TEST&MTP_URL=https://dqm.hdfax.com/eif-mtp-web/mobile/&Configuration=Debug&channels=c000001");
				System.out.println("------开始构建------");
				try {Thread.sleep(10000);} catch (InterruptedException e) {e.printStackTrace();}	//触发后等待一下
				while (true) {
					String buildResult2 = runCommand(cmd);
					JSONObject buildJsonObj2 = new JSONObject(buildResult2);
					int lastBuild2 = buildJsonObj2.getJSONObject("lastBuild").getInt("number");
					int lastCompletedBuild2 = buildJsonObj2.getJSONObject("lastCompletedBuild").getInt("number");
					int lastSuccessfulBuild = buildJsonObj2.getJSONObject("lastSuccessfulBuild").getInt("number");
					if ((lastBuild2 == nextBuildNumber) && (lastCompletedBuild == lastCompletedBuild2)) {
						System.out.println("------构建中...------");
					}
					if (lastCompletedBuild2 == lastCompletedBuild + 1) {
						System.out.println("------构建结束------");
						if (lastSuccessfulBuild == lastCompletedBuild2) {
							System.out.println("------构建成功------");
							return true;
						} else {
							System.out.println("------构建失败------");
							return false;
						}

					}
					if(lastBuild2==lastBuild){
						System.out.println("------未触发构建-----");
						return false;
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			else{
				System.out.println("----上个构建正在进行，等待5s-----");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		}
	}
		/*
		 * 调用jenkins接口，构建job
		 * 1、jobName:需要构建的jenkins工程的名字
		 * 2、buildJobParams：构建jenkins工程时需要的参数
		 * 3、isNeedParams：jenkins工程的类型，如果jenkins工程带参数，isNeedParams=ture(isNeedParams=true时，buildJobParams生效，参数格式：key1=value1&key2=value2...，不填写参数时jenkins使用默认参数)
		 *                        如果jenkins工程不带参数，isNeedParams=false(isNeedParams=false时，buildJobParams不生效，可以随便填写)
		 */
		public void buildJob(String jobName ,Boolean isNeedParams ,String buildJobParams) throws ClientProtocolException, IOException{
			//jenkins构建job的url
			String jenkinsBuildUrl = jenkinsUrl + "/job/" + jobName +"/job/Compile"+ "/build";
			if(isNeedParams == true){
				jenkinsBuildUrl = jenkinsUrl + "/job/" + jobName +"/job/Compile"+ "/buildWithParameters" + "?" + buildJobParams;
			}
			URI uri = URI.create(jenkinsBuildUrl);
			HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(username, password));
			// Create AuthCache instance
			AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local auth cache
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(host, basicAuth);
			CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
			HttpPost httpPost = new HttpPost(uri);
			// Add AuthCache to the execution context
			HttpClientContext localContext = HttpClientContext.create();
			localContext.setAuthCache(authCache);
			HttpResponse response = httpClient.execute(host, httpPost, localContext);
			EntityUtils.toString(response.getEntity());
		}
		public static String runCommand(String cmd){
			String result=null;
	        try {
	        	Process p =Runtime.getRuntime().exec(cmd);
	        	//取得命令结果的输出流    
	            InputStream fis=p.getInputStream();    
	            result = IOUtils.toString(fis, "UTF-8");
	           //用一个读输出流类去读    
	            InputStreamReader isr=new InputStreamReader(fis);    
	           //用缓冲器读行    
	            BufferedReader br=new BufferedReader(isr);    
	            String line=null;    
	           //直到读完为止    
	           while((line=br.readLine())!=null)    
	            {    
	                System.out.println(line);    
	            }   
	           p.waitFor();//导致当前线程等待，如果必要，一直要等到由该 Process 对象表示的进程已经终止。如果已终止该子进程，此方法立即返回。如果没有终止该子进程，调用的线程将被阻塞，直到退出子进程
	           p.exitValue() ;
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return result;
	    }
}
