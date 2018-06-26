package com.evergrande.publics;

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.evergrande.config.AppiumServer;
import com.evergrande.config.MacAppiumServer;
import com.evergrande.config.WinAppiumServer;

public class iOS_CloudTest1 {
	
private IOSDriver<MobileElement> driver;
AppiumServer appiumServer;
	@BeforeClass
	public void beforeclass(){
//		appiumServer=new MacAppiumServer();
//    	appiumServer.stopServer();	//先结束残留进程
//    	System.out.println("---- Starting appium server ----");
//    	appiumServer.startIOSServer();
//		System.out.println("---- Appium server started Successfully ! ----");
//		//处理应用启动
		File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "app");
        File app = new File(appDir, "CloudAlbum_iPhone_V3.0.app");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
	    capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.3");
	    capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 5s");
	    //如果是模拟器的话，udid的配置不需要，app选择使用应用的路径，替换“com.netease.cloudphotos”
	    //capabilities.setCapability(MobileCapabilityType.UDID, "b1182137eed56ab4d615e85bec5cba5d0ac0e21e");
	    capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
	    capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
	    try {
			driver = new IOSDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public void afterclass(){
		//处理资源回收
		driver.quit();
	}
	
	@Test
	public void testDemo1(){
		//登陆部分
		//寻找通行证账号的输入框，输入账号
		//driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATextField[1]")).sendKeys("hiappium@163.com");
		driver.findElement(By.xpath("/XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeTextField")).sendKeys("hiappium@163.com");
		
		//在联想列表中，点击第一行
		driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATableView[1]")).click();
		//寻找密码输入框框，输入密码
		driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIASecureTextField[1]")).sendKeys("a123456");
		//点击登录
		driver.findElementByAccessibilityId("立即登录").click();
		//等待一下
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//点击右上角的上传云端按钮
		driver.findElementByAccessibilityId("nav cloud btn").click();
		//随机点击一张照片
		driver.findElementByAccessibilityId("unchecked").click();
		//点击备份
		driver.findElementByAccessibilityId("备份(1)").click();
		//等待一下
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//判断备份报告是否出现
		Assert.assertTrue(driver.findElementByAccessibilityId("备份报告").isDisplayed());
		
	}

}
