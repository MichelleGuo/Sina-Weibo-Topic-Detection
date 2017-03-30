package com.weibo.work;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import com.weibo.bean.WebPage;
import com.weibo.util.Constants;

public class SimpleWebClient {
	public static final String PROFILE_URL = "http://weibo.cn/";

	/** 日志对象 */
	private static Logger logger = Logger.getLogger(SimpleWebClient.class);

	String content = null;

	/**
	 * 根据URL,下载网站的页面.
	 * 
	 * @param site 网站的链接
	 * 
	 */
	public WebPage getPageContent(String pageURL, String cookie) {

		WebPage webPage = null;
		try {
			// 休眠10秒钟
			Thread.currentThread().sleep(Constants.THREAD_SLEEP);
			// 页面的编码
			String contentEncoding = "";
			// 页面类型
			String contentType = "";
			// 页面的大小
			int contentLength = -1;
			// 数据字节流
			byte[] data = null;
			// 数据读取的位置
			int bytesRead = 0, offset = 0;
			URL url = new URL(pageURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			HttpURLConnection.setFollowRedirects(true);

			// 设置请求的参数
			conn.setRequestMethod("GET");

			// 必须设置false，否则会自动redirect到Location的地址
			conn.setInstanceFollowRedirects(false);
			


			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0");
			
			conn.setRequestProperty(
					"Accept",
					"text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
			conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
			// 在HTTP压缩传输中启用启用Gzip的方法：发送HTTP请求的时候在HTTP头中增加：
			// Accept-Encoding:gzip,deflate
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
			conn.setRequestProperty("Accept-Charset",
					"gb2312,utf-8;q=0.7,*;q=0.7");
			// weibo cookie
			conn.setRequestProperty(
					"Cookie",
					"	_T_WM=; _T_WM=9af2a39d240aa82dfb4875b2cbe4884a; SUHB=0N2lo1mLV8eN7r; SUB=_2A254D8ceDeTxGeNL7VMV8ynJyzyIHXVb8-lWrDV6PUJbvNAKLUnfkW0eR_IAl5pgqL4gXTWLktwRW-LLtw..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWfjDqC2KUVrVVzarKwM8v95JpX5K-t; _T_WL=1; _WEIBO_UID=55614");

			if (cookie != null && !"".equals(cookie)) {
				conn.setRequestProperty("Cookie", " \"" + cookie + "\"");
			}

			// 设置链接超时
			conn.setConnectTimeout(5 * 60 * 1000);
			conn.setReadTimeout(5 * 60 * 1000);

			//conn.connect();
			String gsid = "gsid=4uxZab601m3bPc5IE6GYmnkMB84";

			 if (conn.getResponseCode() == 302) {
			 String redirectUrl=url+"?" + gsid;
			 getPageContent(redirectUrl,cookie);//跳转到重定向的url
			 }
			
			// 显示返回的请求的Header信息.
			for (int i = 1;; i++) {
				String header = conn.getHeaderField(i);
				if (header == null)
					break;
				logger.debug("[Header]:" + conn.getHeaderFieldKey(i) + " : "
						+ header);

			}

			// 从Header头部查找字符编码
			contentType = conn.getContentType();

			System.out.println("conn.getContentType(): "
					+ conn.getContentType());
			System.out.println("conn.getContentType(): "
					+ conn.getHeaderField("Contetnt-Type"));

			if (contentType != null
					&& (contentType.toLowerCase().indexOf("charset") != -1)) {
				contentType = contentType.toLowerCase();
				try {
					contentEncoding = contentType.substring(contentType
							.indexOf("charset=") + "charset=".length());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			contentLength = conn.getContentLength();
			int responseCode = conn.getResponseCode();

			logger.info(pageURL + "\t" + responseCode);

			System.out.println("contentLength = " + contentLength);

			// 如果有Conent-Length字段,就按指定的大小读取数据；但是如果用“gzip”压缩传送，就不能Conent-Length长度以为准。
			if (contentLength != -1) {
				InputStream in = null;
				// 判断数据是否压缩?
				if (conn.getContentEncoding() != null
						&& conn.getContentEncoding().indexOf("gzip") > -1) {
					in = new GZIPInputStream(conn.getInputStream());
					// 把数据暂时保存在内存中
					ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
					data = new byte[4096];
					int read = 0;
					while ((read = in.read(data)) != -1) {
						arrayOutputStream.write(data, 0, read);
					}
					data = arrayOutputStream.toByteArray();
					arrayOutputStream.close();
				} else {
					in = new BufferedInputStream(conn.getInputStream());
					data = new byte[contentLength];
					bytesRead = 0;
					offset = 0;
					while (offset < contentLength) {
						bytesRead = in.read(data, offset, data.length - offset);
						if (bytesRead == -1)
							break;
						offset += bytesRead;
					}
				}
				in.close();

				// 取得页面字符集
				if (contentEncoding == null || contentEncoding.equals("")) {
					contentEncoding = getCharset(new String(data, 0,
							data.length));
					if (contentEncoding.equalsIgnoreCase("gb2312"))
						contentEncoding = "gbk";
				}
				// 关闭链接
				conn.disconnect();
			} // 如果不知道数据大小,循环读取,直到没有数据.
			else {

				InputStream in = null;
				if (conn.getContentEncoding() != null
						&& conn.getContentEncoding().indexOf("gzip") > -1) {
					in = new GZIPInputStream(conn.getInputStream());
				} else {
					in = new BufferedInputStream(conn.getInputStream());
				}
				// 把数据暂时保存在内存中
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				data = new byte[4096];
				int read = 0;
				while ((read = in.read(data)) != -1) {
					arrayOutputStream.write(data, 0, read);
				}
				arrayOutputStream.close();
				in.close();

				// 关闭链接
				conn.disconnect();
				data = arrayOutputStream.toByteArray();
				// 取得页面字符集
				if (contentEncoding.equals("") || contentEncoding == null) {
					contentEncoding = getCharset(new String(data, 0, 1000));
					if (contentEncoding.equalsIgnoreCase("gb2312"))
						contentEncoding = "gbk";
				}
			}
			webPage = new WebPage();
			webPage.setHref(pageURL);
			webPage.setContentEncoding(contentEncoding);
			webPage.setContentLength(data.length);
			webPage.setHtml(new String(data, 0, data.length, contentEncoding));

			return webPage;

		} catch (Exception e) {
			e.printStackTrace();
			return webPage;
		}
	}

	/**
	 * 取得页面的字符集
	 * 
	 * @param content 页面开头的一段字符串
	 */
	public String getCharset(String content) {
		try {
			String contentEncoding = "";
			content = content.toLowerCase();
			if (content != null && content.indexOf("content-type") > -1) {
				contentEncoding = content
						.substring(content.indexOf("charset="));
				contentEncoding = contentEncoding.substring(contentEncoding
						.indexOf("charset=") + "charset=".length());
				contentEncoding = contentEncoding.substring(0,
						contentEncoding.indexOf("\""));
			} else {
				contentEncoding = "gbk";
			}
			return contentEncoding;
		} catch (Exception e) {
			e.printStackTrace();
			return "gbk";
		}
	}

	@SuppressWarnings("deprecation")
	public void getRedirect() throws Exception {

		// 创建一个客户端
		DefaultHttpClient client = new DefaultHttpClient();

		client.setRedirectStrategy(new DefaultRedirectStrategy() {
			public boolean isRedirected(HttpRequest request,
					HttpResponse response, HttpContext context) {
				boolean isRedirect = false;
				try {
					isRedirect = super.isRedirected(request, response, context);
				} catch (ProtocolException e) {
					e.printStackTrace();
				}
				if (!isRedirect) {
					int responseCode = response.getStatusLine().getStatusCode();
					if (responseCode == 301 || responseCode == 302) {
						return true;
					}
				}
				return isRedirect;
			}
		});

	}
}
