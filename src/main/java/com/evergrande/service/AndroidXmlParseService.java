package com.evergrande.service;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.evergrande.testbase.TestCase;
import com.evergrande.testbase.TestStep;
import com.evergrande.testbase.TestUnit;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * <br>配置Appium</br>
 * <br>解析xml到TestUnit</br>
 * @author    黄霄
 * @version 1.0
 * @since   1.0
 */
public class AndroidXmlParseService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(AndroidXmlParseService.class);

	static public AndroidDriver<AndroidElement> driver;
	
	/**
	 * <br>解析xml测试场景配置文件之前，对appium进行配置</br>
	 *
	 * @param xmlPath
	 * @return
	 */
	public static TestUnit parse(String xmlPath) {
		return parse(new File(xmlPath));
	}
	
	/**
	 * <br>解析xml测试场景配置文件，转换为一个TestUnit实例，也就是一个TestCase的集合</br>
	 *
	 * @param xmlFile
	 * @return
	 */
	public static TestUnit parse(File xmlFile) {

		TestUnit testUnit = null;

		if ( xmlFile == null || !xmlFile.exists()  )
			return testUnit;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		
		try {
			builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse(xmlFile);
			Element root = doc.getDocumentElement();
			NodeList cases = root.getElementsByTagName("case");
			//存放case的map
			LinkedHashMap<String, TestCase> caseMap = new LinkedHashMap<String, TestCase>();
			Element child;
			TestCase testCase;
			
			//逐个解析xml中的case元素
			for (int i = 0; i < cases.getLength(); i++) {
				child = (Element) cases.item(i);
				testCase = parseTestCase(child);
				
				if (testCase == null)
					continue;
				
				if (caseMap.containsKey(testCase.getId()))
					throw new RuntimeException("存在多个" + testCase.getId() + "，请检查id配置");
				else{
					if(!testCase.getId().contains("//")){
						//caseId若包含//表示这条用例被注释了
						caseMap.put(testCase.getId(), testCase);
					}
					
				}
					
			}
			
			testUnit = new TestUnit();
			testUnit.setCaseMap(caseMap);		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return testUnit;
	}

	/**
	 * <br>将case元素解析为TestCase实例，也就是一个TestStep的集合</br>
	 *
	 * @param element 一个case元素
	 * @return
	 * @throws Exception 
	 */
	private static TestCase parseTestCase(Element element) throws Exception {
		if (element == null)
			return null;
		
		NamedNodeMap attrs = element.getAttributes();
		//根据case的属性实例化TestCase，并注入相应字段值
		TestCase testCase = initByAttributes(attrs, new TestCase());
		
		NodeList stepNodes = element.getElementsByTagName("step");
		int len = stepNodes.getLength();
		List<TestStep> stepList = new ArrayList<TestStep>(len);
		
		Element stepNode;
		//逐个解析case元素的step子元素
		for (int i = 0; i < len; i++) {
			stepNode = (Element) stepNodes.item(i);
			stepList.add(parseTestStep(stepNode));
		}
		testCase.setSteps(stepList);
		
		return testCase;
	}

	/**
	 * <br>解析step元素为TestStep实例</br>
	 *
	 * @param element
	 * @return
	 */
	private static TestStep parseTestStep(Element element) {
		if (element == null)
			return null;
		
		TestStep testStep = initByAttributes(element.getAttributes(), new TestStep());
		testStep.setAndroidDriver(driver);
		
		return testStep;
	}

	/**
	 * <br>根据xml文件中的元素属性为对象的对应字段注入值</br>
	 *
	 * @param attrs
	 * @param t 需要实例化并注入字段值的对象
	 * @return
	 */
	private static <T> T initByAttributes(NamedNodeMap attrs, T t) {
		if (attrs == null || attrs.getLength() == 0)
			return t;
		
		int len = attrs.getLength();
		Node attr;
		String name, value;
		
		//通过反射逐个注入字段值
		for (int i = 0; i < len; i++) {
			attr = attrs.item(i);
			if (attr == null) continue;
			
			name = attr.getNodeName();
			value = attr.getNodeValue();
			//通过反射为对象的对应字段注入值
			initByReflect(name, value, t);
		}
		return t;
	}

	/**
	 * <br>通过反射为对象的对应字段注入值</br>
	 *
	 * @param name
	 * @param value
	 * @param t
	 * @return
	 * @throws Exception 
	 */
	private static <T> T initByReflect(String name, String value, T t) {
		if (t == null)
			throw new RuntimeException("null object");
		if (name == null || "".equals(name))
			throw new RuntimeException("empty name");
		
		Class<?> clazz = t.getClass();
		Method setter, getter;

		try {
			String methodStr = name.substring(0, 1).toUpperCase() + name.substring(1);
			
			//如果名称是cancel，则调用isCancel()方法，主要是为了语义上的直观
			getter = clazz.getMethod(("cancel".equals(name) ? "is" : "get") + methodStr, new Class<?>[] {});
			if ("selfAction".equals(name)){
				setter = clazz.getMethod("set" + methodStr, String.class);
			}
			else if("action".equals(name)){
				setter = clazz.getMethod("set" + methodStr, String.class);
			}
			else{
				setter = clazz.getMethod("set" + methodStr, getter.getReturnType());
			}
			
			
			if ("action".equals(name))
				//根据StepAction类中的map来获取名称对应的StepAction（枚举）实例
			{
				if(!value.contains("-")){
					throw new RuntimeException("action value值未包含'-'");
				}
				String valueArg[]=value.split("-");
				if(valueArg.length>2){
					throw new RuntimeException("action value值包含多个'-'");
				}
				value=valueArg[0]+valueArg[1].substring(0, 1).toUpperCase()+valueArg[1].substring(1);
				setter.invoke(t, value);
			}
			else if ("cancel".equals(name))
				setter.invoke(t, "true".equals(value) ? true : false);
			else if("details".equals(name))
				setter.invoke(t,parseDetail(value));
			else if("selfMethodParameters".equals(name)){
				String selfParametersArr[]=parseSelfParameters(value);
				Object[] o=new Object[]{selfParametersArr};
				System.out.println(o[0].toString());
				setter.invoke(t,o);
			}
				
			else
				setter.invoke(t, value);
		} catch (Exception e) {
			System.out.println("对象反射初始");
			e.printStackTrace();
		}
		return t;
	}
	/**
	 * <br>解析行为的细节描述为map</br>
	 *
	 * @author    黄霄
	 * @date      2018年6月12日 上午11:01:14
	 * @param detail
	 * @return
	 */
	public static String[] parseSelfParameters(String value){
		String[] strarr = value.split(";");
		return strarr;
	}
	/**
	 * <br>解析行为的细节描述为map</br>
	 *
	 * @author    黄霄
	 * @date      2018年6月12日 上午11:01:14
	 * @param detail
	 * @return
	 */
	public static Map<String,String> parseDetail(String detail){
		HashMap<String,String> map = new HashMap<>();
		String[] strarr = detail.split(";");
		
		for(String str : strarr){
			map.put(str.split(":")[0], str.split(":")[1]);
		}
		return map;
	}
	

}
