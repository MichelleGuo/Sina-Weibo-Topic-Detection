package com.weibo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.weibo.parser.FollowParser;
import com.weibo.parser.WeiboParser;

public class FileUtil {
	// 日志对象
	public static Logger logger = Logger.getLogger(FileUtil.class);

	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 *            文件名称
	 * @param charsetName
	 *            文件编码
	 * @return
	 * @throws IOException
	 */
	public static String readText(String fileName, String charsetName)
			throws IOException {
		StringBuffer buffer = new StringBuffer();
		BufferedReader breader = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName), charsetName));
		String line = null;
		while ((line = breader.readLine()) != null) {
			buffer.append(line + "\r\n");
		}
		breader.close();
		return buffer.toString();
	}

	/**
	 * 保存HTML页面
	 * 
	 * @param path
	 *            本地路径
	 * @param fileName
	 *            文件名称
	 * @param charset
	 *            文件编码
	 * @param content
	 *            页面内容
	 */
	public static void writerFile(String path, String fileName, String charset,
			String content) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(new File(path
					+ File.separator + fileName));
			//logger.debug("文件地址：" + path + File.separator + fileName);
			OutputStreamWriter out = new OutputStreamWriter(fos, charset);
			out.write(content);
			out.flush();
			out.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取HTML页面
	 * 
	 * @param uid
	 *            用户ID
	 * @param pageNumber
	 *            读取页数
	 */
	public static void readweiboFile2parser(String uid, int pageNumber) {

		String content = null;
		Document contentDoc = null;
		// "data" + File.separator + uid, indexPage + "_1" + ".htm",
		// "UTF-8",webPage.getHtml());
		
		for (int indexPage = 1; indexPage <= pageNumber; indexPage++) {

			File input = new File("doc" + File.separator + uid
					+ File.separator + "weibo", "weiboList_" + indexPage + ".htm");

			content = input.toString();
			//System.out.println("content:" + content);

			try {
				contentDoc = Jsoup.parse(input, "UTF8", "");
				List<Element> weiboItems = WeiboParser
						.getGoalContent(contentDoc);
				// 微博数量超过限制，过滤掉，使其拿不到后续链接自动结束
				if (weiboItems == null) {
					logger.debug("weiboItems == null");
					contentDoc = new Document("");

				}

				if (weiboItems != null && weiboItems.size() > 0) {
					logger.debug("weiboItems != null");
					WeiboParser.createFile(weiboItems, uid);

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("contentDoc:" + contentDoc);
		}

	}
	
	
	/**
	 * 读取HTML页面
	 * 
	 * @param uid
	 *            用户ID
	 * @param pageNumber
	 *            读取页数
	 */
	public static void readfollowweiboFile2parser(String uid, int pageNumber) {

		String content = null;
		Document contentDoc = null;
		// "data" + File.separator + uid, indexPage + "_1" + ".htm",
		// "UTF-8",webPage.getHtml());
		
		for (int indexPage = 1; indexPage <= pageNumber; indexPage++) {

			File input = new File("doc" + File.separator + uid
					+ File.separator + "followee_weibo"+ File.separator + uid, "weiboList_" + indexPage + ".htm");

			content = input.toString();
			//System.out.println("content:" + content);

			try {
				contentDoc = Jsoup.parse(input, "UTF8", "");
				List<Element> weiboItems = WeiboParser
						.getGoalContent(contentDoc);
				// 微博数量超过限制，过滤掉，使其拿不到后续链接自动结束
				if (weiboItems == null) {
					logger.debug("weiboItems == null");
					contentDoc = new Document("");

				}

				if (weiboItems != null && weiboItems.size() > 0) {
					logger.debug("weiboItems != null");
					WeiboParser.createFile(weiboItems, uid);

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("contentDoc:" + contentDoc);
		}

	}
	
	
	
	
	/**
	 * 读取HTML页面
	 * 
	 * @param uid
	 *            用户ID
	 * @param pageNumber
	 *            读取页数
	 */
	public static void readFollowFile2parser(String uid, int pageNumber) {

		//String content = null;
		Document contentDoc = null;
		
		for (int indexPage = 1; indexPage <= pageNumber; indexPage++) {

			File input = new File("doc" + File.separator + uid
					+ File.separator + "followee", "followeeList_" + indexPage + ".htm");

			//content = input.toString();
			//System.out.println("content:" + content);

			try {		
				//contentDoc = FollowParser.getPageDocument(content);	

				// 将content字符串转换成Document对象
				contentDoc = Jsoup.parse(input, "UTF8", "");
				//取回这个页面所有的followee	
				List<Element> followeeItems = FollowParser.getGoalContent(contentDoc);
				
				// 微博数量超过限制，过滤掉，使其拿不到后续链接自动结束
				if (followeeItems == null) {
					logger.debug("followeeItems == null");
					contentDoc = new Document("");
				}

				if (followeeItems != null && followeeItems.size() > 0) {
					logger.debug("followeeItems != null");
					FollowParser.createFile(followeeItems, uid);

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("----readFollowFile2parser End!----");
		}

	}
	
}
