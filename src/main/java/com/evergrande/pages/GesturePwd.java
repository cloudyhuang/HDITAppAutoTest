package com.evergrande.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.testng.Assert;

import com.evergrande.config.CommonAppiumPage;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class GesturePwd extends CommonAppiumPage{
	
	@AndroidFindBy(id="layout_title")
	private AndroidElement title;		//手势密码title
	@AndroidFindBy(id="gesture_login")
	private AndroidElement gestureLogin;		//手势密码区域
	@AndroidFindBy(id="login_gesture_tip")
	private AndroidElement gestureTip;		//手势密码提示
	
	@AndroidFindBy(id="textView_gesture_forget_pwd")
	private AndroidElement forgetPwdTV;		//忘记密码
	@AndroidFindBy(id="textView_gesture_switch_user")
	private AndroidElement switchUserTV;		//其他账号登录
	@AndroidFindBy(id="textView_gesture_setting_skip")
	private AndroidElement skipGesturePwd;		//跳过手势密码设置
	@AndroidFindBy(xpath="//android.widget.Button[@resource-id='com.evergrande.eif.android.hengjiaosuo:id/dlg_msg_3pbtn_type2_2' and @text='重新登录']")
	private AndroidElement reLoginBtnOnTips;		//提示弹框中重新登录按钮
	
	@AndroidFindBy(id="dlg_msg_msg")
	private AndroidElement dlgMsgMsg;		//弹出框信息内容
	@AndroidFindBy(xpath="//android.widget.Button[@resource-id='com.evergrande.eif.android.hengjiaosuo:id/dlg_msg_singlebtn' and @text='重新登录']")
	private AndroidElement tipsConfirmBtn;		//手密验证失败弹出框重新登录按钮
	
	private By gestureTipLocator=By.id("login_gesture_tip");
	private By gestureLoginLocator=By.id("gesture_login");
	
	Point point1;
    Point point2;
    Point point3;
    Point point4;
    Point point5;
    Point point6;
    Point point7;
    Point point8;
    Point point9;
	public GesturePwd(AndroidDriver<AndroidElement> driver) {
		super(driver);
	}
	public void skipGesturePwd(){
		clickEle(skipGesturePwd,"跳过手势密码设置");
	}
	/*手势密码区
	 * p1 p2 p3
	 * p4 p5 p6
	 * p7 p8 p9
	 * */
	public String setFirstGesturePwd(int...pn){
		String result="";
        List<Point> pointList=new ArrayList<Point>();
        pointList=getPointList(pn);
        gestureTouchAction(pointList);//输入密码
        result=verifyGestureTips();
        return result;
	}
	
	public List<Point>getPointList(int...pn){
		if (pn.length<4){
			throw new IllegalArgumentException("手势密码没有超过4个点");
		}
		int devWidth = driver.manage().window().getSize().width;	//屏幕像素宽
		int devHeight=driver.manage().window().getSize().height;	//屏幕像素高
		int startX = gestureLogin.getLocation().getX();
        int startY = gestureLogin.getLocation().getY();
        int height = gestureLogin.getSize().getHeight();
        int width = gestureLogin.getSize().getWidth();
        int X1=(int) (0.25*(startX+width));
        int X2=(int) (0.5*(startX+width));
        int X3=(int) (0.75*(startX+width));
        int Y1=(int)(0.079*height+startY);
        int Y2=(int)(0.32*height+startY);
        int Y3=(int)(0.55*height+startY);
        point1=new Point(X1,Y1);
        point2=new Point(X2,Y1);
        point3=new Point(X3,Y1);
        point4=new Point(X1,Y2);
        point5=new Point(X2,Y2);
        point6=new Point(X3,Y2);
        point7=new Point(X1,Y3);
        point8=new Point(X2,Y3);
        point9=new Point(X3,Y3);
        List<Point> pointList=new ArrayList<Point>();
        for(int i=0;i<pn.length;i++){
        	switch(pn[i]){
        	case 1:
        		pointList.add(point1);
        		break;
        	case 2:
        		pointList.add(point2);
        		break;
        	case 3:
        		pointList.add(point3);
        		break;
        	case 4:
        		pointList.add(point4);
        		break;
        	case 5:
        		pointList.add(point5);
        		break;
        	case 6:
        		pointList.add(point6);
        		break;
        	case 7:
        		pointList.add(point7);
        		break;
        	case 8:
        		pointList.add(point8);
        		break;
        	case 9:
        		pointList.add(point9);
        		break;
        	default:
        		throw new IllegalArgumentException("输入的手势密码参数非法");
        	}
        	
        }
        return pointList;
	}
	/*
	 * 登录和设置密码的手势密码坐标不相同*/
	public List<Point>getLoginPointList(int...pn){
		if (pn.length<4){
			throw new IllegalArgumentException("手势密码没有超过4个点");
		}
		int startX = gestureLogin.getLocation().getX();
        int startY = gestureLogin.getLocation().getY();
        int height = gestureLogin.getSize().getHeight();
        int width = gestureLogin.getSize().getWidth();
        int X1=(int) (0.25*(startX+width));
        int X2=(int) (0.5*(startX+width));
        int X3=(int) (0.75*(startX+width));
        int Y1=(int)(0.093*height+startY);
        int Y2=(int)(0.38*height+startY);
        int Y3=(int)(0.67*height+startY);
        point1=new Point(X1,Y1);
        point2=new Point(X2,Y1);
        point3=new Point(X3,Y1);
        point4=new Point(X1,Y2);
        point5=new Point(X2,Y2);
        point6=new Point(X3,Y2);
        point7=new Point(X1,Y3);
        point8=new Point(X2,Y3);
        point9=new Point(X3,Y3);
        List<Point> pointList=new ArrayList<Point>();
        for(int i=0;i<pn.length;i++){
        	switch(pn[i]){
        	case 1:
        		pointList.add(point1);
        		break;
        	case 2:
        		pointList.add(point2);
        		break;
        	case 3:
        		pointList.add(point3);
        		break;
        	case 4:
        		pointList.add(point4);
        		break;
        	case 5:
        		pointList.add(point5);
        		break;
        	case 6:
        		pointList.add(point6);
        		break;
        	case 7:
        		pointList.add(point7);
        		break;
        	case 8:
        		pointList.add(point8);
        		break;
        	case 9:
        		pointList.add(point9);
        		break;
        	default:
        		throw new IllegalArgumentException("输入的手势密码参数非法");
        	}
        	
        }
        return pointList;
	}
	public void setNextGesturePwd(int...pn){
		String result="";
		List<Point> pointList=new ArrayList<Point>();
        pointList=getPointList(pn);
        gestureTouchAction(pointList);//输入密码
		
	}
	public void  inputGesturePwd(int...pn){
		List<Point> pointList=new ArrayList<Point>();
        pointList=getLoginPointList(pn);
        gestureTouchAction(pointList);//输入密码
	}
	public void  inputWrongGesturePwd(int...wrongPn){
		List<Point> pointList=new ArrayList<Point>();
        pointList=getLoginPointList(wrongPn);
        for(int i=4;i>=1;i--){
        	gestureTouchAction(pointList);//输入密码
        	if(super.isElementExsit(super.getWaitTime(), gestureTip)){
        		Assert.assertEquals(super.getEleText(gestureTip, "手密提示"), "手势密码错误,你还有"+i+"次机会");
        	}
        }
        gestureTouchAction(pointList);//输入最后一次手密错误
        String dlgMsgMsgText=super.getEleText(dlgMsgMsg, "手密错误弹框提示");
        Assert.assertEquals(dlgMsgMsgText, "手势密码已失效,请重新登录,登录成功之后,重设手势密码");
        clickEle(tipsConfirmBtn,"手密失效重新登录按钮");
	}
	public void gestureTouchAction(List<Point> gesturePoints){
		Point prePoint = gesturePoints.get(0);
		TouchAction touchAction = new TouchAction(driver);
		TouchAction cur = touchAction.press(prePoint.getX(),prePoint.getY()).waitAction(Duration.ofMillis(1000));
		for(int i = 1;i<gesturePoints.size();i++){
		   Point nextPoint = gesturePoints.get(i);
		   cur = cur.moveTo(nextPoint.getX()-prePoint.getX(),nextPoint.getY()-prePoint.getY()).waitAction(Duration.ofMillis(1000));
		   prePoint = nextPoint;
		  }
		cur.release().waitAction(Duration.ofMillis(1000)).perform();
	}
	public void switchUser(){
		clickEle(switchUserTV,"其他账号登录");
	}
	public void forgotPwd(){
		clickEle(forgetPwdTV,"忘记密码");
		super.waitForClickble(reLoginBtnOnTips, super.getWaitTime());
		clickEle(reLoginBtnOnTips,"重新登录");
	}
	public String verifyGestureTips(){
        return gestureTip.getText();
    }
	public boolean verifyInthisPage(){
        return isElementExsit(10,gestureLoginLocator);
    }
}
