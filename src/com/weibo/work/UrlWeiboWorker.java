package com.weibo.work;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.weibo.fetch.WeiboFetcher;
import com.weibo.parser.WeiboParser;
import com.weibo.util.StrUtils;

/**
 * 从UrlQueue中取出url，下载页面，分析url，保存已访问rul
 * @author yuki
 *
 */
public class UrlWeiboWorker extends BasicWorker implements Runnable {
	private static final Logger Log = Logger.getLogger(UrlWeiboWorker.class.getName());
	/**
	 * 下载对应页面并分析出页面对应URL，放置在未访问队列中
	 * @param url
	 * 
	 * 返回值：被封账号/系统繁忙/OK
	 * 
	 */
//	protected String dataHandler(String url){
//		return NextUrlHandler.addNextWeiboUrl(WeiboFetcher.getContentFromUrl(url));
//	}
	
	@Override
	public void run() {
		// 首先获取账号并登录
//		Account account = AccountQueue.outElement();
//		AccountQueue.addElement(account);
//		this.username = account.getUsername();
//		this.password = account.getPassword();
		
		String cookie = "";
		cookie = "	_T_WM=; _T_WM=9af2a39d240aa82dfb4875b2cbe4884a; SUHB=0N2lo1mLV8eN7r; SUB=_2A254D8ceDeTxGeNL7VMV8ynJyzyIHXVb8-lWrDV6PUJbvNAKLUnfkW0eR_IAl5pgqL4gXTWLktwRW-LLtw..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWfjDqC2KUVrVVzarKwM8v95JpX5K-t; _T_WL=1; _WEIBO_UID=55614";

		// 微博用户ID
		String uid = "cwweekly";
		// 微博的分页总数
		int pageNumber = 5;
		
		// 使用账号登录
		String gsid = login(username, password);
		String result = null;
		try {
			// 若登录失败，则执行一轮切换账户的操作，如果还失败，则退出
			if(gsid == null){
				//gsid = switchAccount();
			}
		
			// 登录成功
			if(gsid != null) {
				// 当URL队列不为空时，从未访问队列中取出url进行分析
				while(!WeiboUrlQueue.isEmpty()) {
					// 从队列中获取URL并处理
					result = dataHandler(WeiboUrlQueue.outElement() + "&" + gsid);
					
					// 针对处理结果进行处理：OK, SYSTEM_BUSY, ACCOUNT_FORBIDDEN
					gsid = process(result, gsid);

					// 没有新的URL了，从数据库中继续拿一个
					if(WeiboUrlQueue.isEmpty()){
						// 仍为空，从数据库中取
						if(WeiboUrlQueue.isEmpty()){
							Log.info(">> Add new weibo Url...");
							StrUtils.initializeWeiboUrl();
							
							// 拿完还是空，退出爬虫
							if(WeiboUrlQueue.isEmpty()){
								Log.info(">> All users have been fetched...");
								break;
							}
						}
					}
				}
			}
			else{
				Log.info(">> " + username + " login failed!");
			}

		} 
		catch (InterruptedException e) {
			Log.error(e);
		}
		catch (IOException e) {
			Log.error(e);
		}
		
		// 关闭数据库连接
		try {
			WeiboParser.conn.close();
			//Utils.conn.close();
		} 
		catch (SQLException e) {
			Log.error(e);
		}

		//Log.info("Spider stop...");
	}
	@Override
	protected String dataHandler(String url) {
		// TODO Auto-generated method stub
		return null;
	}

}

