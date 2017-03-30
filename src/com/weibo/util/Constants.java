package com.weibo.util;

public class Constants
{
	
	public static String FORBIDDEN_PAGE = "http://weibo.cn/pub";
	public static String FORBIDDEN_PAGE_TITILE = "<title>微博广场</title>";
	
	public static String ACCOUNT_FORBIDDEN = "forbidden";
	public static String SYSTEM_BUSY = "busy";
	public static String SYSTEM_EMPTY = "empty";
	public static String OK = "ok";
	
	public static String EntranceUsername = "guozying@qq.com";
	public static String EntranceUserpwd = "1234567890*";
	
	// 抓取数据的本地存储路径，根目录
	public static String ROOT_DISK;
	public static String REPOST_LOG_PATH;
	public static String COMMENT_LOG_PATH;
	public static String SWITCH_ACCOUNT_LOG_PATH;
	public static String ACCOUNT_PATH;
	public static String ACCOUNT_RESULT_PATH;
	public static String LOGIN_ACCOUNT_PATH;
	public static String ABNORMAL_ACCOUNT_PATH;
	public static String ABNORMAL_WEIBO_PATH;
	public static String ABNORMAL_WEIBO_CLEANED_PATH;

	public static String REPOST_BASE_STR = "http://weibo.cn/repost/";
	public static String COMMENT_BASE_STR = "http://weibo.cn/comment/";	
	public static String WEIBO_BASE_STR = "http://weibo.cn/u/";
	
	// used for follow
	public static int LEVEL = 3;
	public static int FANS_NO_MORE_THAN = Integer.MAX_VALUE;
	
	// 是否检查微博数
	public static boolean CHECK_WEIBO_NUM = false;
	public static int WEIBO_NO_MORE_THAN = Integer.MAX_VALUE;
	
	/** 数据库IP */
	public static String HOST = "127.0.0.1";
	/** 数据库端口号 */
	public static String PORT = "3306";
	/** 数据库名称 */
	public static String DB_NAME = "tweibo";
	/** 数据库用户名 */
	public static String USER = "root";
	/** 数据库密码 */
	public static String PASSWORD = "";
	/** 当前线程暂停一段时间 */
	public static long THREAD_SLEEP = 10 * 1000;
	/**  */
	
	/** 是否检查微博数 */
	
	
}
