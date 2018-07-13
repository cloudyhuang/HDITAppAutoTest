package com.evergrande.pages;

import org.openqa.selenium.By;

import com.evergrande.config.CommonAppiumPage;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
* @author huangxiao
* @version 创建时间：2018年7月11日 下午2:45:00
* 类说明
*/
public class ImageProjectListPageObject extends CommonAppiumPage{

	public ImageProjectListPageObject(AndroidDriver<AndroidElement> driver) {
		super(driver);
	}
	public void chooseProject(String projectName) throws Exception{
		String projectXpath="//android.widget.TextView[@text='"+projectName+"']";
		By projectLocator=By.xpath(projectXpath);
		super.scrollTo(projectXpath);
		super.clickEle(projectLocator, "形象工程项目:"+projectName);
	}
}
