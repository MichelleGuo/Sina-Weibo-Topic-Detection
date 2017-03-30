package com.data.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MiningDriver {
	
	public static void wordBreaker(String Filepath,String outputFilepath){

		WeiboSplitter weiboSplitter = new WeiboSplitter();
		StopWords stopWords = new StopWords();
		int id=0;

		File f = new File(Filepath);
		try {
			BufferedReader in  = new BufferedReader(new FileReader(f));
			FileWriter writer = new FileWriter(new File(outputFilepath), true);
			String wbtext = in.readLine();
			
			//System.out.println("读取文件……");
			while(wbtext!=null){
								
				/**
				 * 分词处理
				 */
				String weiboTextTrimmed = weiboSplitter.weiboTrim(wbtext);
				 // 分词后微博文本
				String weiboTextSplitted = weiboSplitter.weiboSplitProcessing(weiboTextTrimmed);
				 // 除去停用词后的微博文本
				String weiboTextStopWordsRemoved = stopWords.remove(weiboTextSplitted);
				 // 除去后缀标记
				 //weiboTextStopWordsRemoved = weiboTextStopWordsRemoved.replaceAll("/[a-z0-9]+","");
				 // 同义词扩展后的微博文本
				 // synonymWords.extendSynonymWords(stopWords.remove(weiboTextSplitted)));
				
				String[] terms = weiboTextStopWordsRemoved.split("\\s+");
				 //微博分词后长度小于 3 的微博去除掉。
				 if(terms.length <= 9) 
					 wbtext = in.readLine();
				 else {
					 //System.out.println("weiboTextStopWordsRemoved:"+weiboTextStopWordsRemoved);
					 writer.write(id + " " + weiboTextStopWordsRemoved.toString()+"\r\n");
					 wbtext = in.readLine();
					 id++;
				 }
			}
			writer.flush();
			writer.close();
			in.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
