package com.weibo.fetch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.weibo.util.Constants;

import cn.edu.hfut.dmic.webcollector.weiboapi.WeiboCN;

public class PageFetcher {
	// 设置头部信息
	protected static void setHeader(HttpRequestBase request) {
		request.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		request.setHeader("Accept-Language", "en-us,en;q=0.5");
		request.setHeader("Connection", "keep-alive");
		request.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0");
	}

	// 微博爬取函数
	public void crawler(String cookie, String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		setHeader(get);
		get.setHeader("Cookie", cookie);
		HttpResponse response;
		try {

			response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			parse(entity);
			EntityUtils.consume(entity);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}
	}

	// 微博爬取函数
	public String followcrawler(String cookie, String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		setHeader(get);
		get.setHeader("Cookie", cookie);
		HttpResponse response;

		String followhtml = null;

		try {

			response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			followhtml = follow(entity);
			parse(entity);
			EntityUtils.consume(entity);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}
		return followhtml;
	}

	private void parse(HttpEntity entity) {
		try {
			String outputFilepath = "output\\followee-weibo.txt";
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

	private String follow(HttpEntity entity) {
		String html = null;
		try {
			String outputFilepath = "output\\follow.txt";
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

	public static void main(String[] args) throws Exception {
		
		
		String filePath = "res\\follow.txt";
		File f = new File(filePath);
		BufferedReader in = new BufferedReader(new FileReader(f));
		String url = in.readLine();
		// String url = "http://weibo.cn/1662579485/follow";
		String followcont = null;

		while (url != null) {
			// String url = "http://weibo.cn/gaoyuanyuan";
			System.out.println("开始抓取" + url);
			String cookie = WeiboCN.getSinaCookie(Constants.EntranceUsername,
					Constants.EntranceUserpwd);
			
			int sumPage = getSumPage(url, cookie);
			System.out.println("微博总页数为" + sumPage);
			// i<sumPage + 1

			for (int i = 1; i < 4; i++) {
				try {
					System.out.println("开始抓取第" + i + "页");
					String pageUrl = url + "?page=" + i;
					PageFetcher mwc = new PageFetcher();
					mwc.crawler(cookie, pageUrl);

					// new here
					// followcont = mwc.followcrawler(cookie, pageUrl);
					// FileUtil.writerFile("output", i + "_follow3" + ".htm",
					// "UTF-8", followcont);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			url = in.readLine();
		}
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
