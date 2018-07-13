package com.evergrande.TestCases;

import java.io.File;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.evergrande.config.CommonAppiumTest;
import com.evergrande.publics.ExcelUtil;
import com.evergrande.service.AndroidXmlParseService;
import com.evergrande.service.RunUnitService;
import com.evergrande.testbase.TestCase;
import com.evergrande.testbase.TestUnit;


public class WeChatLogin extends CommonAppiumTest{
	
	private static RunUnitService runService;
		
	@BeforeTest
	private void stup() throws Exception{
		ExcelUtil excelUtil =new ExcelUtil();
		excelUtil.setFilePath("src/test/java/TestCaseXml/测试用例.xlsx");
		String xmlPath="src/test/java/TestCaseXml/HDLogin.xml";
		Workbook workbook = excelUtil.initWorkBook();
        if (workbook != null) {
        	excelUtil.parseWorkbook(workbook,xmlPath);
        }
		TestUnit testunit = AndroidXmlParseService.parse(xmlPath);
		testunit.setAndroidDriver(driver);
		runService = new RunUnitService(testunit);
		System.out.println("-----------------------------------【恒大地产信息化登录流程的测试场景点】-----------------------------------");
	}
	
	@Test
	public void allTest() throws Throwable{
		for(Map.Entry<String,TestCase> e : runService.getTestunit().getCaseMap().entrySet()){
			String caseId=e.getValue().getId();
			String caseName=e.getValue().getName();
			runService.runCase(caseId); 
			runService.TestReportRemarks(caseName);
			System.out.println(caseId+caseName+"---执行完毕");
        }
	}



}
