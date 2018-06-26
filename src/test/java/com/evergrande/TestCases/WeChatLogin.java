package com.evergrande.TestCases;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.evergrande.TestBase.TestUnit;
import com.evergrande.config.CommonAppiumTest;
import com.evergrande.service.AndroidXmlParseService;
import com.evergrande.service.RunUnitService;


public class WeChatLogin extends CommonAppiumTest{
	
	private static RunUnitService runService;
		
	@BeforeTest
	private void stup() throws Exception{
		TestUnit testunit = AndroidXmlParseService.parse("src/test/java/TestCaseXml/WeChatLogin.xml");
		testunit.setAndroidDriver(driver);
		runService = new RunUnitService(testunit);
		System.out.println("-----------------------------------【恒大金服登录流程的测试场景点】-----------------------------------");
	}
	
	@Test
	public void case1() throws Throwable{
		runService.runCase("case1");
		runService.TestReportRemarks("验证在Android系统中，首次启动恒大金服APP，点击登录按钮后，可以正常进入登录界面");
	}



}
