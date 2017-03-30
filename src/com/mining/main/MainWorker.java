package com.mining.main;

import java.io.File;

import org.apache.log4j.Logger;

import com.data.process.WeiboReader;
import com.weibo.bean.WebPage;
import com.weibo.fetch.FollowFetcher;
import com.weibo.fetch.WeiboFetcher;
import com.weibo.parser.FollowParser;
import com.weibo.parser.WeiboContentParser;
import com.weibo.parser.WeiboParser;
import com.weibo.util.DbUtils;
import com.weibo.util.FileUtil;
import com.weibo.work.SimpleWebClient;

public class MainWorker {

	// 日志对象
	public static Logger logger = Logger.getLogger(WeiboContentParser.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// 微博用户ID
		String uid = "1267454277";
		// 读取微博的分页总数
		int weibopageNumber = 10;
		// 读取关注列表的页数
		int followeepageNumber = 10;			 
		try {
			//获取入口用户的微博
			WeiboFetcher.weibofetcher(uid,weibopageNumber);
			//获取入口用户的关注列表
			FollowFetcher.followfetcher(uid,followeepageNumber);			
			//对入口用户的微博页面进行解析
			FileUtil.readweiboFile2parser(uid, weibopageNumber);
			System.out.println("----readweiboFile2parser End!----");			
			//将关注列表页面的url解析至txt
			FileUtil.readFollowFile2parser(uid,followeepageNumber);
			System.out.println("----readFollowFile2parser End!----");			
			//生成关注用户的url列表--待抓取
			DbUtils.formfolloweeURL(uid);
			System.out.println("----formfolloweeURL End!----");						
			//抓取关注用户列表的微博
			WeiboFetcher.followeeweibofetcher(uid,weibopageNumber);
			System.out.println("----followeeweibofetcher End!----");			
			//对关注用户的微博页面进行解析
			FileUtil.readweiboFile2parser(uid, weibopageNumber);
			System.out.println("----readweiboFile2parser End!----");
			//最后将数据库中的微博写入txt文件，进行下一步文本处理
			WeiboReader.WeiboToTxt(uid);

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("----All is End!----");
		
		
				
		
//		// 启动爬虫worker线程
//				for(int i = 0; i < WORKER_NUM; i++){
//					new Thread(new UrlWeiboWorker()).start();
//				}
		
		/** 用户登录后的cookie */
//		String cookie = "";
//		cookie = "	_T_WM=; _T_WM=9af2a39d240aa82dfb4875b2cbe4884a; SUHB=0N2lo1mLV8eN7r; SUB=_2A254D8ceDeTxGeNL7VMV8ynJyzyIHXVb8-lWrDV6PUJbvNAKLUnfkW0eR_IAl5pgqL4gXTWLktwRW-LLtw..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWfjDqC2KUVrVVzarKwM8v95JpX5K-t; _T_WL=1; _WEIBO_UID=55614";

		
//		 微博的分页总数
//		int pageNumber = 12;
		// 抓取微博并解析存入MySQL数据库
		//WeiboFetcher.weibofetcher(cookie, uid, pageNumber);
		//logger.debug("fetch end. . . . . .");
		//WeiboParser.createFile(weiboItems,uid);
		//FileUtil.readFollowFile2parser(uid,pageNumber);


	}

	/**
	 * 抓取某一个的所有微博列表
	 * 
	 * @param cookie
	 *            登录后的Cookie
	 * @param uid
	 *            某一个的uid
	 * @param pageNumber
	 *            微博总页数
	 */
	public static void weibo(String cookie, String uid, int pageNumber) {
		// 数据库保存记录
		// WeiboDB weiboDB = new WeiboDB();
		// Connection conn = WeiboDB.getConnection();

		// http://weibo.com/137010645?page=3&pre_page=2&end_id=3575033743847089&end_msign=-1
		String pageURL = "http://weibo.com/" + uid + "?page=";
		WebPage webPage = null;

		SimpleWebClient simpleClient = new SimpleWebClient();

		for (int indexPage = 1; indexPage <= pageNumber; indexPage++) {
			try {
				webPage = simpleClient.getPageContent(pageURL + indexPage, cookie);
				if (webPage != null) {
					logger.debug("读取HTML");
					// 打印HTML的内容
					logger.debug(webPage.getHtml());
					FileUtil.writerFile("data" + File.separator + uid,
							indexPage + "_1" + ".htm", "UTF-8",
							webPage.getHtml());

					/** 替换为weibo.cn的网页解析 */
				}
				// logger.debug("插入微博数据到数据库");
				// logger.debug("msgList2:"+msgList);
				// logger.debug("msgList:"+onePageMsgList);
				// WeiboDB.insertWeiboToDB(onePageMsgList, conn);
			} catch (Exception ex) {
				ex.printStackTrace();
				continue;
			}
		}

	}

}
