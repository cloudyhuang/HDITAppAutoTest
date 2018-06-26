package com.evergrande.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
* @author huangxiao
* @version 创建时间：2017年11月16日 下午4:53:05
* 类说明
*/
public class AnyproxyServer extends Thread{
	public void run(){
		this.startServer();
	 }
	
	public void startServer() {
		try {
			Process process = Runtime.getRuntime().exec("anyproxy --intercept --rule /usr/local/lib/node_modules/anyproxy/rule_sample/rule_log.js"); // adb
																		// shell
			final BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			final BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
			// 这里一定要注意错误流的读取，不然很容易阻塞，得不到你想要的结果，
			final BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			new Thread(new Runnable() {
				String line;

				public void run() {
					System.out.println("listener started");
					try {
						while ((line = inputStream.readLine()) != null) {
							System.out.println(line);
						}
					} catch (IOException e) {
						// e.printStackTrace();
					}
				}
			}).start();
			new Thread(new Runnable() {
				final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

				public void run() {
					System.out.println("writer started");
					String line;
					try {
						while ((line = br.readLine()) != null) {
							outputStream.write(line + "\r\n");
							outputStream.flush();
						}
					} catch (IOException e) {
						// e.printStackTrace();
					}
				}
			}).start();
			int i = process.waitFor();
			System.out.println("i=" + i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
