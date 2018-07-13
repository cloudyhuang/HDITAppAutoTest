package com.evergrande.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;

import com.evergrande.config.CommonAppiumPage;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
* @author huangxiao
* @version 创建时间：2018年7月12日 上午9:57:44
* 类说明
*/
public class ImageProgressPageObject extends CommonAppiumPage{

	public ImageProgressPageObject(AndroidDriver<AndroidElement> driver) {
		super(driver);
	}
	/**
	 * 选择楼栋
	 * @param buildingName
	 * @param buildingDetailName
	 */
	public void chooseBuilding(String buildingName,String buildingDetailName){
		By buildingNameLocator=By.xpath("//android.widget.TextView[@resource-id='com.evergrande.hdproject:id/tv_unit' and @text='"+buildingName+"']");
		if(super.isElementExsit(5, buildingNameLocator)){
			clickEle(buildingNameLocator,"楼栋名称");
		}
		By buildingDetailNameLocator=By.xpath("//android.widget.TextView[@resource-id='com.evergrande.hdproject:id/tv_unit' and @text='"+buildingDetailName+"']");
		clickEle(buildingDetailNameLocator,"楼栋详细名称");
	}
	/**
	 * 选择工序
	 * @param procedureName
	 */
	public void chooseProcedure(String procedureName){
		By procedureNameLocator=By.xpath("//android.widget.TextView[@resource-id='com.evergrande.hdproject:id/tv_block' and contains(@text,'"+procedureName+"')]");
		clickEle(procedureNameLocator,procedureName+"工序名称");
	}
	/**
	 * 填入工序
	 * @throws Exception 
	 */
	public void sendConstructionStages() throws Exception{
		super.swipeToUp(500, 1);
		List<AndroidElement> stagesCheckBoxEleList=driver.findElements(By.id("ckbMustInput"));
		List<AndroidElement> startTimeTextList=driver.findElements(By.id("start_timetext"));
		List<AndroidElement> endTimeTextList=driver.findElements(By.id("end_timetext"));
		for(int i=0;i<stagesCheckBoxEleList.size();i++){
			String checkedFlag=stagesCheckBoxEleList.get(i).getAttribute("checked");
			if(checkedFlag.equals("true")){
				startTimeTextList.get(i).click();
				this.dateSend("2018", "7", "12");
				endTimeTextList.get(i).click();
				this.dateSend("2018", "7", "13");
			}
		}
	}
	/**
	 * 输入时间控件
	 * @param year
	 * @param month
	 * @param day
	 */
	public void dateSend(String year, String month, String day) {
		List<AndroidElement> sendTimeEleList = driver.findElements(By.id("numberpicker_input"));
		super.sendKeys(sendTimeEleList.get(0), year);
		super.sendKeys(sendTimeEleList.get(1), month);
		super.sendKeys(sendTimeEleList.get(2), day);
		super.clickEle(findBy(By.id("button1")),"时间选框确定按钮");
	}
	
}
