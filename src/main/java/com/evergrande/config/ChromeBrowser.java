package com.evergrande.config;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author huangxiao
 * @version 创建时间：2018年3月5日 上午11:35:44 类说明
 */
public class ChromeBrowser implements Runnable {
	public void run() {
		this.chromeGetUrl();
	}

	public void chromeGetUrl() {
		DesiredCapabilities cap=DesiredCapabilities.chrome();
		// 设置变量ACCEPT_SSL_CERTS的值为True  处理不安全链接
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);	
		// 此处PATH替换为你的chromedriver所在路径
		System.setProperty("webdriver.chrome.driver", "/Users/master/Documents/chromedriver"); 
		WebDriver webDriver = new ChromeDriver();
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		webDriver.get("https://www.tripadvisor.cn");
		try {
			Thread.currentThread().sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(webDriver.getCurrentUrl().contains("1.1.1.3")){
			WebElement userNameInputEle=webDriver.findElement(By.id("password_name"));
			userNameInputEle.click();
			userNameInputEle.sendKeys("huangxiao");
			WebElement pwdInputEle=webDriver.findElement(By.id("password_pwd"));
			pwdInputEle.click();
			pwdInputEle.sendKeys("Hdhxnearcj228");
			WebElement rememberPwdCheckBoxEle=webDriver.findElement(By.id("rememberPwd"));
			rememberPwdCheckBoxEle.click();
			WebElement submitBtnEle=webDriver.findElement(By.id("password_submitBtn"));
			submitBtnEle.click();
			//div[@class='login_box_msg shiftKey']//dd
			try {Thread.currentThread().sleep(10000);} catch (InterruptedException e) {e.printStackTrace();}
		}
		webDriver.quit();
	}
}
