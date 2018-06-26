package com.evergrande.service;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.testng.Reporter;

import com.evergrande.TestBase.StepAction;
import com.evergrande.TestBase.TestCase;
import com.evergrande.TestBase.TestStep;
import com.evergrande.TestBase.TestUnit;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;



/**
 * <br>TODO(描述该类的作用)</br>
 * @author    黄霄
 * @date    2018年5月27日 下午5:50:09
 * @version 1.0
 * @since   1.0
 */
public class RunUnitService {

	private TestUnit testunit;
	
	public RunUnitService(TestUnit testunit){
		this.testunit = testunit;
	}
	
    /**
     * <br>根据索引从TestUnit中获取测试用例</br>
     *
     * @param index
     * @return
     */
    public TestCase getCase(int index){
        int n = 0;
        if(testunit.getCaseMap() == null)
            return null;
        
        int size = testunit.getCaseMap().size();
        if(index < 0 || (index > 0 && index >= size))
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
        
        //遍历map内部Entry集合
        for(Map.Entry<String,TestCase> e : testunit.getCaseMap().entrySet()){
            if(n++ == index)
                return e.getValue();
        }
        
        return null;
    }

    /**
     * <br>根据用例名，即case元素中的id元素值，从TestUnit中获取测试用例</br>
     *
     * @param id
     * @return
     */
    public TestCase getCase(String id){
        return testunit.getCaseMap() == null ? null : testunit.getCaseMap().get(id);
    }
    
    /**
     * <br>执行测试用例</br>
     *
     * @param id
     * @throws Throwable 
     */
    public void runCase(String id) throws Throwable{
    	TestCase testCase = getCase(id);
    	List<TestStep> steps = testCase.getSteps();
    	
    	for(TestStep step : steps){
//    		if(step.isCancel())
//    			continue;
//    		System.out.println("开始执行: "+step.toString());
    		StepAction action = step.getAction();
    		Class<?> clazz = null;
    		if(action!=null){
    			clazz = action.getClass();
    		}
    		Class<?> selfActionClazz=step.getSelfAction();
    		
    		if(clazz != null){
    			String ationName=action.getActionName();
    			Class<?>[] parameterTypes=getMethodParameterTypes(clazz,ationName);
    			Method m = clazz.getDeclaredMethod(getMethodName(ationName),parameterTypes);
    			Constructor<?> con = clazz.getDeclaredConstructor(new Class<?>[]{AndroidDriver.class});
    			AndroidDriver<AndroidElement> driver=testunit.getAndroidDriver();
    			Object o=con.newInstance(driver);
    			try {
    			m.invoke(o, step);
    			}catch (IllegalAccessException | IllegalArgumentException
    	                | InvocationTargetException e) {
    	            Throwable cause = e.getCause();
    	            throw cause;
    			}
    		}
    		else if(selfActionClazz != null){
				String methodName=step.getSelfMethodName();
				Object[] selfMethodParameters=step.getSelfMethodParameters();
				Class<?>[] parameterTypes=getMethodParameterTypes(selfActionClazz,methodName);
				if(selfMethodParameters!=null&&selfMethodParameters.length!=parameterTypes.length){
					throw new Exception("自定义selfAction xml传入参数与定义参数个数不同!");
				}
				Method m=null;
				if(parameterTypes.length>0){
					m = selfActionClazz.getDeclaredMethod(getMethodName(methodName),parameterTypes);
				}
				else{
					m = selfActionClazz.getDeclaredMethod(getMethodName(methodName));
				}
    			Constructor<?> con = selfActionClazz.getDeclaredConstructor(new Class<?>[]{AndroidDriver.class});
    			AndroidDriver<AndroidElement> driver=testunit.getAndroidDriver();
    			Object o=con.newInstance(driver);
    			try {
    				if(parameterTypes.length==0){
    					m.invoke(o);
    				}
    				else{
    					m.invoke(o, selfMethodParameters);
    				}
        			
        			}catch (IllegalAccessException | IllegalArgumentException
        	                | InvocationTargetException e) {
        	            Throwable cause = e.getCause();
        	            throw cause;
        			}
			}
    		else{
    			action.run(step);
    		}
    	}
    }
    /**
     * 获取方法形参
     *
     * @param class
     * @return 形参class数组
     */
    public Class[] getMethodParameterTypes(Class c,String methodName){
    	Class parameterTypes[] = null;
    	Method m[]=c.getDeclaredMethods();
    	for(Method method:m){
    		if(method.getName().equals(methodName)){
    			parameterTypes=method.getParameterTypes();
    		}
    	}
    	return parameterTypes;
    }
    /**
     * 获取方法形参
     *
     * @param class
     * @return 形参class数组
     */
    public Object[] getStepParameterTypes(TestStep step){
    	Class parameterTypes[] = null;
    	Class stepClass=step.getClass();
    	Field f[]=stepClass.getDeclaredFields();
    	return f;
    }
    /**
     * <br>根据step元素的值解析出对应的方法名</br>
     * @param actionKey
     * @return
     */
    private String getMethodName(String actionKey){
    	if(actionKey == null || "".equals(actionKey))
    		throw new RuntimeException("empty action key");  	
    	return actionKey;
    }
//    
//    /**
//	 * <br>执行完毕，退出App程序</br>
//	 */
//	public void closeApp() {
//		AndroidXmlParseService.QuitApp();
//	}
//    
//	/**
//	 * <br>执行完毕，关闭浏览器</br>
//	 */
//	public void closeBrowser() {
//		WebXmlParseService.QuitBrowser();
//	}
	
    /**
     * <br>标记备注，一般展示在测试报告中</br>
     *
     * @param RemarksName
     */
    public void TestReportRemarks(String RemarksName){
 	   Reporter.log(RemarksName);
    }
    
    /**
     * <br>标记备注，一般展示在测试报告中</br>
     *
     * @param command
     */
    public void AndroidAdb(String command) throws IOException{
    	Runtime.getRuntime().exec(command);
    }
}    
