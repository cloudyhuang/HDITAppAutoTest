package com.evergrande.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;

/**
 * @author huangxiao
 * @version 创建时间：2017年8月17日 下午2:24:12 mac os启动appium服务
 */
public class MacIosAppiumServer extends AppiumServer {
	@Override
	public void startIOSServer() {
		CommandLine command = new CommandLine("appium");
		command.addArgument("--address");
		command.addArgument("127.0.0.1");
		command.addArgument("--pre-launch");
		command.addArgument("--port");
		command.addArgument("4723");
		command.addArgument("--platform-version");
		command.addArgument("9.3");
		command.addArgument("--platform-name");
		command.addArgument("iOS");
		command.addArgument("--device-name");
		command.addArgument("iPhone 6");
		command.addArgument("--log");
		command.addArgument(System.getProperty("user.dir") + File.separator + "appiumLogs.txt");
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		try {
			executor.execute(command, resultHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stopServer() {

		CommandLine command = new CommandLine("killall");
		command.addArgument("node");

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);

		try {
			executor.execute(command, resultHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startServer(String udid,boolean isDebug) {
		// TODO Auto-generated method stub
		
	}
}
