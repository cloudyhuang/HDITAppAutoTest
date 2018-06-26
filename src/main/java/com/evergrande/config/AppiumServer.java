package com.evergrande.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;


public abstract class AppiumServer {
	public abstract void startServer(String udid,boolean isDebug);
	public abstract void startIOSServer();
	public abstract void stopServer();
	public void runCommand(String cmd){
        try {
        	Process p =Runtime.getRuntime().exec(cmd);
        	//取得命令结果的输出流    
            InputStream fis=p.getInputStream();    
           //用一个读输出流类去读    
            InputStreamReader isr=new InputStreamReader(fis);    
           //用缓冲器读行    
            BufferedReader br=new BufferedReader(isr);    
            String line=null;    
           //直到读完为止    
           while((line=br.readLine())!=null)    
            {    
                System.out.println(line);    
            }   
           p.waitFor();//导致当前线程等待，如果必要，一直要等到由该 Process 对象表示的进程已经终止。如果已终止该子进程，此方法立即返回。如果没有终止该子进程，调用的线程将被阻塞，直到退出子进程
           p.exitValue() ;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
