package com.weibo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.weibo.bean.Weibo;

public class WeiboDB {
	static String CONN_URL = "jdbc:mysql://127.0.0.1:3306/"
			+ "tweibo?useUnicode=true&characterEncoding=utf8";
	static String USERNAME = "root";
	static String PASSWORD = "";
	
	// 日志对象
	public static Logger logger = Logger.getLogger(WeiboDB.class);

	public static Connection getConnection(){
		
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(CONN_URL, USERNAME, PASSWORD);
			if (!conn.isClosed())
				logger.debug("连接成功！");
			else
				logger.debug("连接失败！");
		} 
		catch (Exception e) {
			logger.error(e);
		}
		
		return conn;
	}
	

	public static int insertWeiboToDB(ArrayList<Weibo> msgList,
			Connection conn) {
		
		String insertSQL = "INSERT INTO 'xwb_weibo' ('uid','mid','content','isforward','username','forward',"
				+ "'favorite','comment','heart','addtime','comeFrom', 'href') VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
		logger.debug(msgList);
		if (msgList != null && msgList.size() > 0) {
			try {
				logger.debug("insert START");
				int parameterIndex = 1;
				PreparedStatement pst = conn.prepareStatement(insertSQL);
				for (int i = 0; i < msgList.size(); i++) {
					parameterIndex = 0;
					Weibo msg = msgList.get(i);
					pst.setLong(parameterIndex++, stringToLong(msg.getUserId()));
					pst.setLong(parameterIndex++, stringToLong(msg.getMid()));
					pst.setString(parameterIndex++, msg.getContent());
					logger.debug(msg.getContent());
					/**
//					pst.setByte(parameterIndex++,
//							stringToByte(msg.getIsforward()));
//					pst.setString(parameterIndex++, msg.getAuthorName());
 * */
					pst.setInt(parameterIndex++,
							stringToInteger(msg.getForward()));
					pst.setInt(parameterIndex++,
							stringToInteger(msg.getFavorite()));
					pst.setInt(parameterIndex++,
							stringToInteger(msg.getComment()));
					pst.setInt(parameterIndex++,
							stringToInteger(msg.getHeart()));
					pst.setLong(parameterIndex++,
							stringToLong(msg.getDateString()));
					pst.setString(parameterIndex++, msg.getComeFrom());
					pst.setString(parameterIndex++, msg.getHref());
					// logger.debug(pst.toString());
					logger.debug("插入完毕");
					pst.execute();
				}
			} catch (SQLException e) {
				logger.debug("NULL");
				e.printStackTrace();
			}
		}
		return 0;
	}

	public static long stringToLong(String longString) {
		long id = 0;
		try {
			longString = FindNumber(longString);
			id = Long.parseLong(longString);
		} catch (Exception ex) {
			logger.debug("===================" + longString);
			ex.printStackTrace();
		}
		return id;
	}

	public static int stringToInteger(String integerString) {
		int id = 0;
		try {
			integerString = FindNumber(integerString);
			id = Integer.parseInt(integerString);
		} catch (Exception ex) {
			logger.debug("===================" + integerString);
			ex.printStackTrace();
		}
		return id;
	}

	public static byte stringToByte(String byteString) {
		byte id = 0;
		try {
			byteString = FindNumber(byteString);
			id = Byte.parseByte(byteString);
		} catch (Exception ex) {
			logger.debug("===================" + byteString);
			ex.printStackTrace();
		}
		return id;
	}

	/**
	 * 查找字符串中的数字
	 * 
	 * @param input
	 * @return
	 */
	public static String FindNumber(String input) {
		String value = "";
		String regex = "(\\d){0,}";
		Pattern p = Pattern.compile(regex);
		if (input != null && !"".equals(input)) {
			Matcher m = p.matcher(input);
			while (m.find()) {
				value = m.group();
				if (value == null || "".equals(value)) {
					continue;
				} else {
					logger.debug(value);
					break;
				}
			}
		}
		if (value == null || "".equals(value)) {
			value = "0";
		}
		return value;
	}

//	/**
//	 * @param args
//	 * @throws SQLException
//	 * @throws ClassNotFoundException
//	 */
//	public static void main(String[] args) throws ClassNotFoundException,
//			SQLException {
//		WeiboDB weiboDB = new WeiboDB();
//		// 连续数据库
//		//Connection conn = DriverManager.getConnection(url, user, password);
//		Connection conn = weiboDB.getConnection();
//
//	}
	
	
}
