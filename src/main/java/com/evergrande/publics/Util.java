package com.evergrande.publics;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.evergrande.interfaces.GetOrderPushStatus;

public class Util {
	public static GetOrderPushStatus orderpushstatus = new GetOrderPushStatus();
	/**
	   * 判断日期格式是否正确
	   *
	   * @param time 时间戳
	   * @param addDays 增加天数
	   * @return time+addDays
	   */
	public static boolean isValidDate(String str) {
	      boolean convertSuccess=true;
	// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	       try {
	// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
	          format.setLenient(false);
	          format.parse(str);
	       } catch (ParseException e) {
	          // e.printStackTrace();
	// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
	           convertSuccess=false;
	       } 
	       return convertSuccess;
	}
	public static String getcurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");// 自定义日期格式
		return df.format(new Date());
	}
	/**
	   * 传入时间戳和需要增加的天数，返回n天后的时间戳。
	   *
	   * @param time 时间戳
	   * @param addDays 增加天数
	   * @return time+addDays
	   */
	public  static long addDate(long time,int addDays){
		Date date = new Date(time);
	     Calendar   calendar   =   new   GregorianCalendar(); 
	     calendar.setTime(date); 
	     calendar.add(calendar.DATE,addDays);//把日期往后增加一天.整数往后推,负数往前移动 
	     date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
	     return date.getTime();
	}
	/**
	   * 传入Date和需要增加的天数，返回n天后的Date。
	   *
	   * @param time 时间戳
	   * @param addDays 增加天数
	   * @return time+addDays
	   */
	public  static Date addDate(Date date,int addDays){
	     Calendar   calendar   =   new   GregorianCalendar(); 
	     calendar.setTime(date); 
	     calendar.add(calendar.DATE,addDays);//把日期往后增加一天.整数往后推,负数往前移动 
	     date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
	     return date;
	}
	 /**
     * 计算工作日
     * 具体节日包含哪些,可以在HolidayMap中修改
     * @param time 时间戳
     * @param adddays 要加的天数
	 * @throws ParseException 
     */
    public static long addDateByWorkDay(long time,int adddays) throws ParseException
    {
        boolean holidayFlag = false;
        Date date = new Date(time);
	    Calendar   src   =   new   GregorianCalendar(); 
	    src.setTime(date); 
        for (int i = 0; i < adddays; i++)
        {
            //把源日期加一天
            src.add(Calendar.DAY_OF_MONTH, 1);
            holidayFlag =checkHoliday(src);
            if(holidayFlag)
            {
               i--;
            }
            System.out.println(src.getTime());
        }
        System.out.println("Final Result:"+src.getTime());
        return src.getTimeInMillis();
    }
	/**
	   * 判断一个时间是否为节假日
	   *
	   * @param time 时间
	   * @return 是否为节假日
	   * @throws ParseException 
	   */
	public  static boolean checkHoliday(Calendar cal) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString =  formatter.format(cal.getTime());
		String url = ("http://api.goseek.cn/Tools/holiday?date="+dateString);
		String httpResult = orderpushstatus.sendGet(url);	//响应结果
		int httpStatusCode=orderpushstatus.getStatusCode();  //响应码
		System.out.println(httpResult+"status:"+httpStatusCode);
		JSONObject postJsonobj = new JSONObject(httpResult);
		String data=postJsonobj.get("data").toString();
		if(data.equals("0")){
			return false;
		}
		else if(data.equals("1")||data.equals("2")){
			return true;
		}
		else return false;
	}
	/**
	   * 传入时间戳和需要减少的天数，返回n天后的时间戳。
	   *
	   * @param time 时间戳
	   * @param minusDays 减少天数
	   * @return time-minusDays
	   */
	public  static long minusDate(long time,int minusDays){
		Date date = new Date(time);
	     Calendar   calendar   =   new   GregorianCalendar(); 
	     calendar.setTime(date); 
	     calendar.add(calendar.DATE,(0-minusDays));//把日期往后增加一天.整数往后推,负数往前移动 
	     date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
	     return date.getTime();
	}
	/**
	   * 传入Date和需要减少的天数，返回n天后的Date。
	   *
	   * @param date Date
	   * @param minusDays 减少天数
	   * @return date-minusDays
	   */
	public  static Date minusDate(Date date,int minusDays){
	     Calendar   calendar   =   new   GregorianCalendar(); 
	     calendar.setTime(date); 
	     calendar.add(calendar.DATE,(0-minusDays));//把日期往后增加一天.整数往后推,负数往前移动 
	     date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
	     return date;
	}
	/**
	   * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
	   *
	   * @param sformat yyyyMMddhhmmss
	   * @return
	   */
	  public static String getUserDate(String sformat) {
	    Date currentTime = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat(sformat);
	    String dateString = formatter.format(currentTime);
	    return dateString;
	  }
	  /**
	   * 根据用户传入的时间,和对应时间格式，转换为相应时间戳
	   *
	   * @param time 时间
	   * @param sformat yyyyMMddhhmmss
	   * @return
	   */
	public static long dateToLong(String time,String sformat) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(sformat);
		// String time="1970-01-06 11:45:55";
		Date date = format.parse(time);
		return date.getTime();
	}
	/**
	   * 计算两个日期之间间距
	   *
	   * @param str1，str2 2个日期
	   * @return long 间距秒数
	   */
	public static long getDistanceTime(String str1,String str2) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date one;  
        Date two;    
        long sec = 0;  
        try {  
            one = df.parse(str1);  
            two = df.parse(str2);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }    
            sec = diff/1000;  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }
		return sec;  
	}
	/**
	   * 传入日期秒置零
	   *
	   * @param time 时间
	   * @return 置零秒的时间
	   */
	public static String setSecondsToZero(String time) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse(time);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.SECOND, 0);
		date=calendar.getTime();
		time=format.format(date);
		return time;
	}
	/*
	 * System.out.println(dateToLong("2017-06-18 20:50:35"));
	 * System.out.println(longToDate(1496904635362L));
	 */
	public static String longToDate(long times) {
		Date date = new Date(times);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}
	public static String longToDate(long times,String sformat) {
		Date date = new Date(times);
		SimpleDateFormat formatter = new SimpleDateFormat(sformat);
		String dateString = formatter.format(date);
		return dateString;
	}
	 /**
	   * 从字符串中取出数字
	   *
	   * @param string 字符串
	   * @return 字符串中的数字
	   */
	public static String getNumInString(String string) {
		//Pattern p = Pattern.compile("\\d+(\\.\\d+)?");
		Pattern p=Pattern.compile("[^0-9]");
		Matcher m = p.matcher(string);
//		if (m.find()) {
//			return m.group();
//		} else
			return m.replaceAll("").trim();
	}
	/**
	   * 从字符串中取出数字包括小数
	   *
	   * @param string 字符串
	   * @return 字符串中的数字
	   */
	public static String getDecimalNumInString(String string) {
		Pattern p=Pattern.compile("[^0-9.]");
		Matcher m = p.matcher(string);
		return m.replaceAll("").trim();
	}
	/**
	   * 从字符串中取出数字并保留2位小数
	   *
	   * @param string 字符串
	   * @return 2位小数
	   */
	public static String get2DecimalPointsNumInString(String a){
		String b=Util.getDecimalNumInString(a);
		double c=Util.stringToDouble(b);
		//c=c/100.0;
		DecimalFormat df = new DecimalFormat("#0.00");
		String d=df.format(c);
		return d;
	}
	 /**
	   * double转String，若double为小数点为x.0返回整数String
	   *
	   * @param double num
	   * @return 字符串
	   */
	public static String doubleTransToString(double num) {
		if (num % 1.0 == 0) {
			return String.valueOf((long) num);
		}
		return String.valueOf(num);
	}
	 /**
	   * double转String
	   *
	   * @param double num
	   * @return 字符串
	   */
	public static String doubleToString(double num) {
		return String.valueOf(num);
	}
	public static double stringToDouble(String string) {
		return Double.parseDouble(string);
	}
	 /**
	   * 比较两个集合是否相等
	   *
	   * @param List a  List b
	   * @return boolean
	   */
	public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
		if (a.size() != b.size())
			return false;
		Collections.sort(a);
		Collections.sort(b);
		for (int i = 0; i < a.size(); i++) {
			if (!a.get(i).equals(b.get(i)))
				return false;
		}
		return true;
	}
}
