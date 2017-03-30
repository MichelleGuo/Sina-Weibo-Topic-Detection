package com.weibo.fetch;

import java.io.File;
import java.io.FileWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.weiboapi.WeiboCN;

import com.weibo.util.Constants;
import com.weibo.util.FileUtil;

public class FollowFetcher {

	// private static final Logger logger = Logger.getLogger(FollowFetcher.class
	// .getName());

	public static Document getPageDocument(String content) {
		return Jsoup.parse(content);
	}

	/**
	 * 抓取某一个的所有关注列表
	 * 
	 * @param uid
	 *            要抓取的用户uid
	 */

	public static void followfetcher(String uid,int readPage) throws Exception {
		// 读取关注列表
		// 格式：String url = "http://weibo.cn/1662579485/follow"
		// String filePath = "doc" + File.separator + uid + File.separator +
		// "followee_list.txt";
		// File f = new File(filePath);
		// BufferedReader in = new BufferedReader(new FileReader(f));
		// String url = in.readLine();

		// String url = "http://weibo.cn/"+ uid + "/follow";
		String url = "http://weibo.cn/" + uid + "/follow";
		String followcont = null;

		System.out.println("开始抓取关注列表：" + url);
		String cookie = WeiboCN.getSinaCookie(Constants.EntranceUsername,
				Constants.EntranceUserpwd);

		int sumPage = getSumPage(url, cookie);
		System.out.println("微博总页数为" + sumPage);

		// i<sumPage + 1
		// int sumPage = 5;
		for (int i = 1; i < readPage + 1 ; i++) {
			try {
				System.out.println("----开始抓取第" + i + "页----");
				String pageUrl = url + "?page=" + i;
				FollowFetcher follow = new FollowFetcher();

				followcont = follow.followcrawler(cookie, pageUrl, uid);
				// 保存页面
				FileUtil.writerFile("doc" + File.separator + uid
						+ File.separator + "followee", "followeeList_" + i + ".htm",
						"UTF-8", followcont);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("----关注列表抓取结束----");
	}

	// 设置头部信息
	protected static void setHeader(HttpRequestBase request) {
		request.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		request.setHeader("Accept-Language", "en-us,en;q=0.5");
		request.setHeader("Connection", "keep-alive");
		request.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0");
	}

	// 关注列表爬取函数
	public String followcrawler(String cookie, String url, String uid) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		setHeader(get);
		get.setHeader("Cookie", cookie);
		HttpResponse response;

		String followhtml = null;

		try {

			response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			followhtml = EntityUtils.toString(entity);
			// followhtml = follow(entity, uid);
			// parse(entity, uid);
			EntityUtils.consume(entity);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}
		return followhtml;
	}

	// 解析网页 获取关注者微博内容
	private void parse(HttpEntity entity, String uid) {
		try {
			String outputFilepath = "doc" + File.separator + uid
					+ File.separator + "followee-weibo.txt";
			FileWriter writer = new FileWriter(new File(outputFilepath), true);
			String html = null;
			html = EntityUtils.toString(entity);
			Document doc = Jsoup.parse(html);
			Elements weibos = doc.select("div.c");
			for (Element weibo : weibos) {
				// System.out.println(weibo.text());
				writer.write(weibo.text() + "\r\n");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String follow(HttpEntity entity, String uid) {
		String html = null;
		try {
			String outputFilepath = "doc" + File.separator + uid
					+ File.separator + "followee.txt";
			FileWriter writer = new FileWriter(new File(outputFilepath), true);

			html = EntityUtils.toString(entity);
			Document doc = Jsoup.parse(html);
			Elements weibos = doc.select("div.c");
			for (Element weibo : weibos) {
				// System.out.println(weibo.text());
				writer.write(weibo.text() + "\r\n");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}

	private static int getSumPage(String url, String cookie) {
		int sumPage = 0;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		setHeader(get);
		get.setHeader("Cookie", cookie);
		HttpResponse response;
		try {
			response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			String html = null;
			html = EntityUtils.toString(entity);
			Document doc = Jsoup.parse(html);
			Elements page = doc.select("div#pagelist input");
			String text = page.get(0).toString();
			int target = text.indexOf("value");
			int begin = text.indexOf("\"", target);
			int end = text.indexOf("\"", begin + 1);
			String sumPageString = text.substring(begin + 1, end);
			sumPage = Integer.parseInt(sumPageString);
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}
		return sumPage;
	}

}
