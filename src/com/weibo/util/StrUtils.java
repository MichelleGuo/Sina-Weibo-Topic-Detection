package com.weibo.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.weibo.db.WeiboDB;
import com.weibo.work.WeiboUrlQueue;

public class StrUtils {
	private static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Logger Log = Logger.getLogger(StrUtils.class.getName());
	public static Connection conn = WeiboDB.getConnection();
	
	/**
	 * 数据库中读取用户账号，并生成第一页微博的url，放入WeiboUrlQueue
	 */
	public static synchronized void initializeWeiboUrl(){
		String querySql = "SELECT accountID FROM USER WHERE isFetched = 0 ORDER BY id LIMIT 1";
//		Connection conn = DBConn.getConnection();
		PreparedStatement ps = null;
		Statement st = null;
		ResultSet rs = null;
		String accountID = null;
		
		try {
			conn.setAutoCommit(false); 
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			st = conn.createStatement();
			rs = st.executeQuery(querySql);		
			if(rs.next()){
				accountID = rs.getString("accountID");
				ps = conn.prepareStatement("UPDATE USER SET isFetched = 1 WHERE accountID = ?");
				ps.setString(1, accountID);
				ps.execute();
				ps.close();
			}
			rs.close();
			st.close();
			
			conn.commit();
			if(accountID != null){
				// 提交成功后，再放入队列
				WeiboUrlQueue.addElement(Constants.WEIBO_BASE_STR + accountID + "?page=1");
			}
		} 
		catch (SQLException e) {
			Log.error(e);
			// 提交失败 roll back，并将放入队列的URL拿出来
			try {
				conn.rollback();
			} 
			catch (SQLException e1) {
				Log.error(e1);
			}
		}
		finally{
//			try {
//				conn.close();
//			} 
//			catch (SQLException e) {
//				Log.error(e);
//			}
		}
	}
	
	/**
	 * 检测字符串是否为null，或空字符串
	 * @param str
	 * @return
	 */
	public static boolean isEmptyStr(String str){
		if(str == null || str.trim().length() == 0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 将微博时间字符串转换成yyyyMMddHHmmSS
	 * 微博时间字符串有：
	 * 		xx分钟前
	 * 		今天 11:53 
	 * 		07月09日 13:36
	 * 		2010-09-23 19:55:38
	 * 		
	 * @param weiboTimeStr
	 * @return
	 */
	public static String parseDate(String weiboTimeStr){
		
		Calendar currentTime = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。 
		
		if(weiboTimeStr.contains("分钟前")){
			int minutes = Integer.parseInt(weiboTimeStr.split("分钟前")[0]);
			
			currentTime.add(Calendar.MINUTE, -minutes);//取当前日期的前一天. 
			
			return simpleDateTimeFormat.format(currentTime.getTime());
		}
		else if(weiboTimeStr.startsWith("今天")){
			String[] time = weiboTimeStr.split("天")[1].split(":");
			int hour = Integer.parseInt(time[0].substring(1));
			int minute = Integer.parseInt(time[1].substring(0, 2));
			
			currentTime.set(Calendar.HOUR_OF_DAY, hour);
			currentTime.set(Calendar.MINUTE, minute);
			
			return simpleDateTimeFormat.format(currentTime.getTime());
		}
		else if(weiboTimeStr.contains("月")){
			String[] time = weiboTimeStr.split("日")[1].split(":");
			int dayIndex = weiboTimeStr.indexOf("日") - 2;
			int month = Integer.parseInt(weiboTimeStr.substring(0, 2));
			int day = Integer.parseInt(weiboTimeStr.substring(dayIndex, dayIndex + 2));
			int hour = Integer.parseInt(time[0].substring(1));
			int minute = Integer.parseInt(time[1].substring(0, 2));
			
			currentTime.set(Calendar.MONTH, month - 1);
			currentTime.set(Calendar.DAY_OF_MONTH, day);
			currentTime.set(Calendar.HOUR_OF_DAY, hour);
			currentTime.set(Calendar.MINUTE, minute);
			
			return simpleDateTimeFormat.format(currentTime.getTime());
		}
		else if(weiboTimeStr.contains("-")){
			return weiboTimeStr.replace("-", "").replace(":", "").replace(" ", "").substring(0, 14);
		}
		else{
			Log.info(">> Error: Unknown time format - " + weiboTimeStr);
		}
		
		return null;
	}
	
	// 从url中解析出当前用户的ID
	public static String getUserIdFromUrl(String url) {
		int startIndex = url.lastIndexOf("/");
		int endIndex = url.indexOf("?");
		
		if(endIndex == -1){
			return url.substring(startIndex + 1); 
		}
		return url.substring(startIndex + 1,  endIndex);
	}
	
	public static String getUserIdFromImgUrl(String url) {
		int startIndex = url.indexOf("sinaimg.cn/") + "sinaimg.cn/".length();
		String subStr = url.substring(startIndex);

		return subStr.substring(0, subStr.indexOf("/"));
	}
	// 从follow url中解析出当前用户的ID
		public static String getUserIdFromFollowUrl(String url) {
			int startIndex = 16;
			int endIndex = url.indexOf("/follow");

			return url.substring(startIndex,  endIndex);
		}

}
