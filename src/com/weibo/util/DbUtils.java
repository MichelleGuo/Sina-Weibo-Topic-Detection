package com.weibo.util;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.weibo.db.WeiboDB;
import com.weibo.parser.FollowParser;

public class DbUtils {
	 
	private static final Logger logger = Logger.getLogger(FollowParser.class.getName());
	public static Connection conn = WeiboDB.getConnection();
	
	/**
	 * 将mysql里面的关注名单写出至本地txt文件
	 * 
	 * @param uid 			用户ID
	 * @param pageNumber 	读取页数
	 */
	public static void formfolloweeURL(String uid){
		
		System.out.println("生成关注用户url待抓取列表……");
		String outputFilepath = "doc" + File.separator + uid + File.separator + "followee_list.txt";		
		Statement statement = null;
		ResultSet rs = null;
		
		String followeeName = null;
		String url = "http://weibo.cn/";
		String followUrl = null;
		
		try {
			FileWriter writer = new FileWriter(new File(outputFilepath), true);
			statement = conn.createStatement();
			rs = statement.executeQuery("select followee_uid from xwb_user_follow");
			while(rs.next())
			{
				followeeName = rs.getString("followee_uid");
				followUrl = url + followeeName;
				writer.write(followUrl.toString() + "\r\n");
			}
			
			writer.flush();
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("----生成url列表完毕----");
		
		
	}

}
