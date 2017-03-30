package com.data.process;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import ICTCLAS.I3S.AC.ICTCLAS50;


public class WeiboSplitter {

	/**
	 * 将微博文本进行初步分词 写出到 wbBreaker.txt
	 * 
	 */
	
	//日志对象
	public static final Logger logger = Logger.getLogger(WeiboSplitter.class.getName());
	ICTCLAS50 ictclas50 = new ICTCLAS50();
	String path = "./";
	
	// 用户字典路径
	String usrdir = "./userdict.txt"; 
	
	public WeiboSplitter() {
		super();
		// 初始化
		try {
			if (ictclas50.ICTCLAS_Init(path.getBytes("GB2312")) == false) {
				logger.debug("Init Failed!");
			}
		} catch (UnsupportedEncodingException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		// 设置词性标注集(0 计算所二级标注集，1 计算所一级标注集，2 北大二级标注集，3 北大一级标注集)
		ictclas50.ICTCLAS_SetPOSmap(2);
		
		// 导入用户字典
		byte[] usrdirb = usrdir.getBytes();// 将string转化为byte类型
		// 导入用户字典,返回导入用户词语个数第一个参数为用户字典路径，第二个参数为用户字典的编码类型
		int userDictsCounter = ictclas50.ICTCLAS_ImportUserDictFile(usrdirb, 0);
		logger.info("导入用户词个数: " + userDictsCounter);
	}

	public String weiboTrim(String input) {
		String weiboTextTrimmed = input.replaceAll("\\w+", "");
		return weiboTextTrimmed;
		
	}

	public String weiboSplitProcessing(String input) {
		try {
			byte nativeBytes[] = ictclas50.ICTCLAS_ParagraphProcess(
					input.getBytes("GB2312"), 2, 1);
			String nativeStr = new String(nativeBytes, 0,
					nativeBytes.length, "GB2312");
			return nativeStr;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			return null;
		}
	}
	
	public void finalize() {
		// 保存用户字典
		ictclas50.ICTCLAS_SaveTheUsrDic();
		// 释放分词组件资源
		ictclas50.ICTCLAS_Exit();
	}
}
