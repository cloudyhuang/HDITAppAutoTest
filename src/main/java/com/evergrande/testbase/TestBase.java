package com.evergrande.testbase;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
* @author huangxiao
* @version 创建时间：2018年5月24日 下午4:30:05
* 类说明
*/
public class TestBase {
	private String id;
	private String desc;
	private AndroidDriver<AndroidElement> driver;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public AndroidDriver<AndroidElement> getAndroidDriver() {
		return driver;
	}
	public void setAndroidDriver(AndroidDriver<AndroidElement> driver) {
		this.driver = driver;
	}
	
	
}
