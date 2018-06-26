package com.evergrande.publics;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Reporter;

/**
 * @author huangxiao
 * @version 创建时间：2017年11月9日 下午2:58:58 类说明
 */
public class LogReader implements Runnable {
	private File logFile = null;
	private long lastTimeFileSize = 0; // 上次文件大小
	private long beforeLastTimeFileSize=0;	//上上次文件大小
	private int readLogID=0;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public LogReader(File logFile) {
		this.logFile = logFile;
		lastTimeFileSize = logFile.length();
	}
	public long getBeforeLastTimeFileSize(){
		return beforeLastTimeFileSize;
	}
	public void readLog(){
		try {
			long len = logFile.length();
			if (len < lastTimeFileSize) {
				System.out.println("Log file was reset. Restarting logging from start of file.");
				beforeLastTimeFileSize=lastTimeFileSize;
				lastTimeFileSize = len;
			} else if (len > lastTimeFileSize) {
				RandomAccessFile randomFile = new RandomAccessFile(logFile, "r");
				randomFile.seek(lastTimeFileSize);
				String tmp = null;
				while ((tmp = randomFile.readLine()) != null) {
					tmp = new String(tmp.getBytes("iso8859-1"), "utf-8");
					//System.out.println(dateFormat.format(new Date()) + "\t" + tmp);
				}
				beforeLastTimeFileSize=lastTimeFileSize;
				lastTimeFileSize = randomFile.length();
				randomFile.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readLog(long fileSize){
		try {
			long len = logFile.length();
			if (len < fileSize) {
				System.out.println("Log file was reset. Restarting logging from start of file.");
				lastTimeFileSize = len;
			} else if (len > fileSize) {
				readLogID=readLogID+1;
				RandomAccessFile randomFile = new RandomAccessFile(logFile, "r");
				randomFile.seek(fileSize);
				String tmp = null;
				Reporter.log("<self><a href=\"javascript:toggleElement('exceptionLog-"+readLogID+"', 'block')\" title=\"Click to expand/collapse\"><b>失败附近调用接口信息(点击展开详细)↓：</b></a><br /><div class=\"stackTrace\" id=\"exceptionLog-"+readLogID+"\"></self>");
				while ((tmp = randomFile.readLine()) != null) {
					tmp = new String(tmp.getBytes("iso8859-1"), "utf-8");
					Reporter.log("<self>"+dateFormat.format(new Date()) + "\t" + tmp+"</self>");
				}
				Reporter.log("<self></div></self>");
				lastTimeFileSize = randomFile.length();
				beforeLastTimeFileSize=fileSize;
				randomFile.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 实时输出日志信息
	 */
	public void run() {
		while (true) {
			try {
				long len = logFile.length();
				if (len < lastTimeFileSize) {
					System.out.println("Log file was reset. Restarting logging from start of file.");
					lastTimeFileSize = len;
				} else if (len > lastTimeFileSize) {
					RandomAccessFile randomFile = new RandomAccessFile(logFile, "r");
					randomFile.seek(lastTimeFileSize);
					String tmp = null;
					while ((tmp = randomFile.readLine()) != null) {
						tmp = new String(tmp.getBytes("iso8859-1"), "utf-8");
						System.out.println(dateFormat.format(new Date()) + "\t" + tmp);
					}
					lastTimeFileSize = randomFile.length();
					randomFile.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
