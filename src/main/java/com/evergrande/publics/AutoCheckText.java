package com.evergrande.publics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
* @author huangxiao
* @version 创建时间：2017年11月6日 下午4:02:12
* 类说明
*/
public class AutoCheckText {
	private static final String FILE_PATH = "/Users/master/Documents/workspace/Test-UI-AndroidAuto/log/urlLog.log";

    private static final String COMPARED_FILE_PATH = "/Users/master/Documents/workspace/Test-UI-AndroidAuto/log/urlLog2.log";

    private static final String RESULT_FILE_PATH = "/Users/master/Documents/workspace/Test-UI-AndroidAuto/log/urlLog3.log";


    public static void markFile(){
    	System.out.println("======Start Search!=======");
        long startTime = System.currentTimeMillis();
        // Read first file
        File file = new File(FILE_PATH);
        BufferedReader br = null;
        BufferedWriter rbw = null;
        try {
            br = new BufferedReader(new FileReader(file));
            rbw = new BufferedWriter(new FileWriter(RESULT_FILE_PATH));
            String lineText = null;
			String cpLineText = null;
			boolean cpReadFlag = false;
			boolean lineReadFlag = false;
			String cpSearchText = null;
			String searchText = null;
            long endTime = System.currentTimeMillis();
            System.out.println("======Process Over!=======");
            System.out.println("Time Spending:" + ((endTime - startTime) / 1000D) + "s");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if ( rbw != null) {
                        try {
                            rbw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    public static void compareFile(){
    	System.out.println("======Start Search!=======");
        long startTime = System.currentTimeMillis();
        // Read first file
        File file = new File(FILE_PATH);
        File comparedFile = new File(COMPARED_FILE_PATH);
        BufferedReader br = null;
        BufferedReader cbr = null;
        BufferedWriter rbw = null;
        try {
            br = new BufferedReader(new FileReader(file));
            cbr = new BufferedReader(new FileReader(comparedFile));
            cbr.mark(90000000);
            rbw = new BufferedWriter(new FileWriter(RESULT_FILE_PATH));
            String lineText = null;
			String cpLineText = null;
			boolean cpReadFlag = false;
			boolean lineReadFlag = false;
			String cpSearchText = null;
			String searchText = null;
			while (true) {
				if ((cpLineText = cbr.readLine()) != null) {
					cpSearchText = cpLineText.trim();
					while (true) {
						if ((lineText = br.readLine()) != null) {
							searchText = lineText.trim();
							if (searchText.equals(cpSearchText)) {
								break;
							} else
								rbw.write(cpSearchText+"\n");
						} else {
							lineReadFlag = true;
							break;
						}
					}
				} else {
					cpReadFlag = true;
					break;
				}
				if (lineReadFlag == true && cpReadFlag == false) {
					rbw.write(cpSearchText+"\n");
				}
			}
            long endTime = System.currentTimeMillis();
            System.out.println("======Process Over!=======");
            System.out.println("Time Spending:" + ((endTime - startTime) / 1000D) + "s");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (cbr != null && rbw != null) {
                        try {
                            cbr.close();
                            rbw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        
    }



    public static void searchAndSignProcess(String searchText, BufferedReader comparedReader, BufferedWriter rbw)
            throws IOException {
        String lineStr = "-\n";
        if (searchText == null) {
            return;
        }
        if ("".equals(searchText)) {
            rbw.write(lineStr);
            return;
        }
        String lineText = null;
        int lineNum = 1;
        while ((lineText = comparedReader.readLine()) != null) {
            String comparedLine = lineText.trim();
            if (searchText.equals(comparedLine)) {
                lineStr = "###=Equal:" + lineNum + "=###\n";
                break;
            }
            else{
            	
            }
            lineNum++;
        }
        rbw.write(lineStr);
        comparedReader.reset();
    }
}
