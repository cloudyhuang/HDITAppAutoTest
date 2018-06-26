package com.evergrande.pages;

import org.openqa.selenium.By;

import com.evergrande.config.CommonAppiumPage;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class SafeKeyBoard extends CommonAppiumPage{
	private AndroidDriver<AndroidElement> driver; 
	@AndroidFindBy(id="btn0")
	private AndroidElement num0;		//数字1
	@AndroidFindBy(id="btn1")
	private AndroidElement num1;		//数字1
	@AndroidFindBy(id="btn2")
	private AndroidElement num2;		//数字2
	@AndroidFindBy(id="btn3")
	private AndroidElement num3;		//数字3
	@AndroidFindBy(id="btn4")
	private AndroidElement num4;		//数字4
	@AndroidFindBy(id="btn5")
	private AndroidElement num5;		//数字5
	@AndroidFindBy(id="btn6")
	private AndroidElement num6;		//数字6
	@AndroidFindBy(id="btn7")
	private AndroidElement num7;		//数字7
	@AndroidFindBy(id="btn8")
	private AndroidElement num8;		//数字8
	@AndroidFindBy(id="btn9")
	private AndroidElement num9;		//数字9
	@AndroidFindBy(id="btnpoint")
	private AndroidElement pointBtn;		//数字9
	@AndroidFindBy(id="linear_lineardel")
	private AndroidElement backSpace;		//退格
	@AndroidFindBy(id="digitbtnsure1")
	private AndroidElement finishBtn;		//完成按钮
	
	private By finishBtnLocator=By.id("digitbtnsure1");	//完成按钮Locator
	public SafeKeyBoard(AndroidDriver<AndroidElement> driver) {
		super(driver);
	}
	
	public void pressBtn(int num) throws Exception{
		switch(num){
		case 0:
			clickNativeElePoint(num0,"安全键盘数字0");
			break;
		case 1:
			clickNativeElePoint(num1,"安全键盘数字1");
			break;
		case 2:
			clickNativeElePoint(num2,"安全键盘数字2");
			break;
		case 3:
			clickNativeElePoint(num3,"安全键盘数字3");
			break;
		case 4:
			clickNativeElePoint(num4,"安全键盘数字4");
			break;
		case 5:
			clickNativeElePoint(num5,"安全键盘数字5");
			break;
		case 6:
			clickNativeElePoint(num6,"安全键盘数字6");
			break;
		case 7:
			clickNativeElePoint(num7,"安全键盘数字7");
			break;
		case 8:
			clickNativeElePoint(num8,"安全键盘数字8");
			break;
		case 9:
			clickNativeElePoint(num9,"安全键盘数字9");
			break;
		default:
			throw new Exception("输入数字非0-9，为："+num);
		}
	}
	public void sendNum(String nums) throws Exception{
		char charNum[] = nums.toCharArray();
        for(int i=0;i<charNum.length;i++)
        {
            String c = charNum[i]+"";
            int d = Integer.parseInt(c);
            this.pressBtn(d);
        }
	}
	
	public void pressFinishBtn(){
		clickEle(finishBtn,"安全键盘完成按钮");
	}

	public void pressBackSpace(int times) {
		for (int i = 0; i < times; i++) {
			clickNativeElePoint(backSpace, "退格键");
		}

	}
	
	public boolean verifySafeKeyBoardLocated(){
		return isElementExsit(15,finishBtnLocator);
	}
}
