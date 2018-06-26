package com.evergrande.publics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* @author huangxiao
* @version 创建时间：2017年11月9日 下午3:45:00
* 类说明
*/
public class LogWrite {
	private static final Logger logger = LoggerFactory.getLogger(LogWrite.class);

	private SimpleDateFormat  dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
	private static ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
	
	public void logMsg(File logFile,String msgInfo) throws IOException{
		
		if(!logFile.exists()) {
			logFile.createNewFile();
		}
		
		Writer txtWriter = new FileWriter(logFile,true);
		txtWriter.write(dateFormat.format(new Date()) + "\t" + msgInfo + "\n");
		txtWriter.flush();
		txtWriter.close();
	}
	
	public void stop(){
		if(exec != null){
			exec.shutdown();
			logger.info("file write stop ！");
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		//final LogWrite logSvr = new LogWrite();
		final File tmpLogFile = new File("/Users/master/Documents/workspace/Test-UI-AndroidAuto/log/urlLog.log");
//		final String msgInfo = "test !";
//		//启动一个线程每5秒向日志文件写一次数据
//		exec.scheduleWithFixedDelay(new Runnable(){
//
//			@Override
//			public void run() {
//				try {
//					logSvr.logMsg(tmpLogFile, msgInfo);
//					//Thread.sleep(1000);
//				} catch (Exception e) {
//					logger.error("file write error ！");
//				}
//			}
//			
//		}, 0, 5, TimeUnit.SECONDS);
		Thread rthread = new Thread(new LogReader(tmpLogFile));
        rthread.start();
	}
}
