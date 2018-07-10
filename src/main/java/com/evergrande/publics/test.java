package com.evergrande.publics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jsoup.helper.StringUtil;
import org.openqa.selenium.By;

import com.evergrande.testbase.TestCase;
import com.evergrande.testbase.TestStep;

public class test {
	public static void main(String[] args) throws Exception {
		getElementLocator("xpath=//android.widget.TextView[@text='形象进度管理']");
	}
	public static By getElementLocator(String loc) throws Exception{
		   if(StringUtil.isBlank(loc)) 
			   throw new Exception("当前步骤未定位到任何控件元素！");
		   //如果没有索引，则加上索引0
		   if(loc.lastIndexOf("[") < 0)  loc = loc+"[0]";
		   
		   int idx1 = loc.indexOf("=") , idx2 = loc.lastIndexOf("[");
		   if(idx1 == -1 || idx2 == -1 || idx2 < idx1) 
			   throw new Exception("step元素locator属性格式有误！");
		   
		   String locatename = loc.substring(0, idx1);
		   String locatevalue = loc.substring(idx1+1,idx2);
		   String locindex = loc.substring(idx2+1,loc.length()-1);
		   
		   System.out.println(locatename+"  "+locatevalue+" "+locindex);
		   By locator=null;

		   switch(locatename){
		   		case "resource-id": 
		   			locator = By.id(locatevalue);
		   			break;
		   		case "class": 
		   			locator = By.className(locatevalue);
		   			break;
		   		case "xpath": 
		   			locator = By.xpath(locatevalue);
		   			break;
		   		default:
		   			throw new Exception("step元素locator属性配置有误，'='之前必须为resource-id、calss、xpath之一！");
		   }
		   
	       return locator==null?null:locator;
	}
	public static void CreateXMLByDOM4J(File dest) {
        // 创建Document对象
        Document document = DocumentHelper.createDocument();
        // 创建根节点
        Element testUnit=document.addElement("unit");
        testUnit.addAttribute("id", "WeChatLogin");
        testUnit.addAttribute("desc", "恒大金服登录流程的测试场景点");
        Element testCase = testUnit.addElement("case");
        //为testCase根节点添加属性
        testCase.addAttribute("id", "case1");
        testCase.addAttribute("name", "验证恒大金服登录功能");
        // 创建step子节点
        Element step = testCase.addElement("step");
        step.addAttribute("action", "wait-Forced");
        step.addAttribute("value", "5000");
        step.addAttribute("desc", "强制等待5秒");
        if(testUnit.element("case").attributeValue("id").equals("case1")){
        	// 创建step子节点
            step = testCase.addElement("step");
            step.addAttribute("action", "wait-Forced");
            step.addAttribute("value", "5000");
            step.addAttribute("desc", "强制等待5秒");
        }
        else{
        	testCase = testUnit.addElement("case");
            //为testCase根节点添加属性
            testCase.addAttribute("id", "case1");
            testCase.addAttribute("name", "验证恒大金服登录功能");
         // 创建step子节点
            step = testCase.addElement("step");
            step.addAttribute("action", "wait-Forced");
            step.addAttribute("value", "5000");
            step.addAttribute("desc", "强制等待5秒");
        }
        
        
        // 创建输出格式(OutputFormat对象)
        OutputFormat format = OutputFormat.createPrettyPrint();

        try {
            // 创建XMLWriter对象
            XMLWriter writer = new XMLWriter(new FileOutputStream(dest), format);

            //设置不自动进行转义
            writer.setEscapeText(false);

            // 生成XML文件
            writer.write(document);

            //关闭XMLWriter对象
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	/**
	 * 使用递归的二分查找 title:recursionBinarySearch
	 * 
	 * @param arr
	 *            有序数组
	 * @param key
	 *            待查找关键字
	 * @return 找到的位置
	 */
	public static int recursionBinarySearch(int[] arr, int key, int low, int high) {

		if (key < arr[low] || key > arr[high] || low > high) {
			return -1;
		}

		int middle = (low + high) / 2; // 初始中间位置
		if (arr[middle] > key) {
			// 比关键字大则关键字在左区域
			return recursionBinarySearch(arr, key, low, middle - 1);
		} else if (arr[middle] < key) {
			// 比关键字小则关键字在右区域
			return recursionBinarySearch(arr, key, middle + 1, high);
		} else {
			return middle;
		}

	}
	public static int recursionBinarySearch(int key, int low, int high) {
		int a=1;return a;
	}
}