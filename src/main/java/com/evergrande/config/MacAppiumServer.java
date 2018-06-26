package com.evergrande.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;

import com.evergrande.publics.ShellUtils;
import com.evergrande.publics.ShellUtils.CommandResult;

/**
 * @author huangxiao
 * @version 创建时间：2017年8月17日 下午2:24:12 mac os启动appium服务
 */
public class MacAppiumServer extends AppiumServer {
	@Override
	public void startServer(String udid,boolean isDebug) {
		if(!isDebug){
			String[] commands1 = new String[] { "pkill Nox", "pkill VBox" };	//结束模拟器进程
//			String vmName="74720963-42df-4250-abb9-f3c11f84809f";
//			String[] commands1 = new String[] { "vboxmanage controlvm "+vmName+" poweroff || true" };	//虚拟机关机
			CommandResult result = ShellUtils.execCommand(commands1, false);
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			commands1 = new String[] { "cd /Applications", "open Nox\\ App\\ Player.app" };
			//commands1 = new String[] { "ps x | grep \"Genymotion\\.app/Contents/MacOS/.*player\" | awk '{print $1}' | xargs kill" };
			ShellUtils.execCommand(commands1, false);
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			commands1 = new String[] { "cd /Applications", "open Nox\\ App\\ Player.app" };
			//commands1 = new String[] { "open -a /Applications/Genymotion.app/Contents/MacOS/player.app --args --vm-name '"+vmName+"'" };
			ShellUtils.execCommand(commands1, false);
			try {
				Thread.currentThread().sleep(60000);	//等待模拟器60s启动
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
    	String cmd1="adb kill-server";
    	String cmd2="adb start-server";
    	String cmd3="adb connect "+udid;
		runCommand(cmd1);
		runCommand(cmd2);
		runCommand(cmd3);
		try {
			Thread.currentThread().sleep(5000);	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(!isDebug){
			String[] commands = new String[]{"exec adb -s "+udid+" shell","input text \"HD888888\"","input keyevent 66"};	//输入开机密码
			CommandResult result = ShellUtils.execCommand(commands, false);
			System.out.println("输入开机密码："+result.successMsg);
		}
		String[] commands2 = new String[]{"exec adb -s "+udid+" shell","cd data/local/tmp","rm *.apk"};	//删除缓存apk
		CommandResult result = ShellUtils.execCommand(commands2,false);
		System.out.println("删除缓存apk"+result.successMsg);
		System.out.println("删除缓存apk"+result.errorMsg);
		if(isDebug){}
		else {
			String cmd4 = "adb -s " + udid + " uninstall com.evergrande.eif.android.hengjiaosuo"; // 卸载app
			System.out.print("卸载app:");
			runCommand(cmd4);
		}
		CommandLine command = new CommandLine("appium");
		command.addArgument("--address");
		command.addArgument("127.0.0.1");
		command.addArgument("--port");
		command.addArgument("4723");
		command.addArgument("--device-name");
		command.addArgument(udid);// 新加
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
	public void startIOSServer() {
		// TODO Auto-generated method stub
		
	}
}
