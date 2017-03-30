package com.gzy.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

//public class TestNLPIR {
//	
//	public static void main(String[] args) throws Exception{
//		String filePath = "./test/test-utf8.TXT";
//		String tt=new String();
//		try{
//           BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));
//		    String str;
//		    while ((str = in.readLine()) != null) {
//		    	tt+=str;
//		    }
//		    test(tt);
//		}
//		catch (Exception ex){
//		} 
//	}
//	public static void test(String sInput){
//		try{
//			System.out.println(sInput);
//			NLPIR testNLPIR = new NLPIR();
//			String argu = "./file/";
//			System.out.println("NLPIR_Init");
//			if (testNLPIR.NLPIR_Init(argu.getBytes("GB2312"),0) == false){
//				System.out.println("Init Fail!");
//				return;
//			}
//			byte nativeBytes[]=testNLPIR.NLPIR_GetKeyWords(sInput.getBytes("GB2312"), 20, true);
//			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
//			String outline[] = new String[50];
//			outline = nativeStr.split("  ", -1);
//			System.out.println("关键词识别结果为:");
//			for(int i=0;i<outline.length;i++){
//				String newoutline[] = new String[3];
//				newoutline=outline[i].split("/",-1);
//				System.out.println(newoutline[0]+","+newoutline[2]);
//			}				
//			testNLPIR.NLPIR_Exit();
//		}
//		catch (Exception ex){
//		} 
//	}


