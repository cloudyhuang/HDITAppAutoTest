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
 * @version 创建时间：2017年8月18日 上午9:00:31 类说明
 */
public class WinAppiumServer extends AppiumServer {
	@Override
	public void startServer(String udid,boolean isDebug) {
		String cmd = "cmd /c adb kill-server && adb start-server && adb connect " + udid; // 重置adb
		runCommand(cmd);
		if(isDebug){}
		else {
			cmd = "adb -s " + udid + " uninstall com.evergrande.eif.android.hengjiaosuo"; // 卸载app
			System.out.print("卸载app:");
			runCommand(cmd);
		}
		CommandLine command = new CommandLine("cmd");
		command.addArgument("/c");
		command.addArgument("appium");
		command.addArgument("--address");
		command.addArgument("127.0.0.1");
		command.addArgument("--port");
		command.addArgument("4723");
		command.addArgument("--device-name");
		command.addArgument("127.0.0.1:52001");// 新加
		command.addArgument("--no-reset");
		command.addArgument("--command-timeout");
		command.addArgument("600");
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

		CommandLine command = new CommandLine("cmd");
		command.addArgument("/c");
		command.addArgument("taskkill");
		command.addArgument("/F");
		command.addArgument("/IM");
		command.addArgument("node.exe");

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
	public void startIOSServer() {
		// TODO Auto-generated method stub
		
	}

}
