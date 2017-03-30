package com.data.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.weibo.db.WeiboDB;
import com.weibo.parser.FollowParser;
import com.weibo.util.HtmlRegexpUtil;

public class WeiboReader {
	
	/**
	 * 将数据库的微博写出到 wbContent.txt
	 * 
	 */
	
	private static final Logger logger = Logger.getLogger(FollowParser.class.getName());
	public static Connection conn = WeiboDB.getConnection();
	
	//public List<String> WeiboToTxt(String uid) throws Exception{
	public static List<String> WeiboToTxt(String uid){
		
		List<String> strs = new LinkedList<String>();
		String outputFilepath = "doc" + File.separator + uid + File.separator + "wb_mining"+ File.separator + "wbContent.txt";
		
		int id=0;
		Statement statement = null;
		ResultSet rs = null;
		String wbContent = null;
		String str = "转发微博";

		int length = 0;
		
		//&nbsp的处理
		try {
			FileOutputStream writer = new FileOutputStream(new File(outputFilepath));
					//new FileWriter(new File(outputFilepath), true);
			OutputStreamWriter out = new OutputStreamWriter(writer, "UTF-8");
			statement = conn.createStatement();
			rs = statement.executeQuery("select content from content_weibo");
			while(rs.next())
			{
				wbContent = rs.getString("content");

				if(!wbContent.equals("") && !wbContent.equals(str)){
					    wbContent = textPrehandler(wbContent);
						strs.add(id+wbContent);
						id++;	

				}
			}
			
			for(int i = 0; i < strs.size();i++){
				out.write(strs.get(i).trim()+"\r\n");
			}
			out.flush();
			out.close();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return strs;
		//System.out.println("----生成txt完毕----");
	}
	
	
	
//	public int writeFile(String destFile) throws Exception{
//
//		FileWriter writer = new FileWriter(new File(destFile));
//		for(int i = 0; i < strs.size();i++){
//			writer.write(strs.get(i).trim()+"\r\n");
//		}
//		writer.flush();
//		writer.close();
//		return strs.size();
//	}
	
	public static String textPrehandler(String str){
		
		String taskstr = null;
		// 过滤文章内容中的html
		taskstr = HtmlRegexpUtil.filterHtml(str);
		// 去除空格
		taskstr = taskstr.replace("&nbsp;","");	
		// 过滤文章内容中的http链接
		taskstr = taskstr.replaceAll("http.*","");
		// 去除微博特定标签
		taskstr = taskstr.split("赞\\[")[0];
		
		//length = wbContent.length();
		//转发微博赞[0]
		//wbContent = wbContent.replaceAll("赞[[^(0-9)]]","");
		//&& wbContent.substring(length-4, length)!= "转发微博"
		
		return taskstr;
	}
	

}
