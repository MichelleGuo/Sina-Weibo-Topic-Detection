package com.data.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import ICTCLAS.I3S.AC.ICTCLAS50;

public class WordBreaker {

	// 日志对象
	public static Logger logger = Logger.getLogger(WordBreaker.class);

	// 对博文的初步分词
	static HashMap<String, Integer> stopWord = new HashMap<String, Integer>();// 放置停用词的链表

	ICTCLAS50 testICTCLAS50 = new ICTCLAS50();

	public void breakWord(String srcFile, String destFile)
			throws UnsupportedEncodingException {

		ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
		String argu = ".";
		// String argu = "F:\\DM\\blogContent.txt";
		if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false) {
			System.out.println("Init Fail!");
			return;
		}
		testICTCLAS50.ICTCLAS_FileProcess(srcFile.getBytes("GB2312"), 0, 1,
				destFile.getBytes("GB2312"));
	}

	// 获取停用词
	public void getStopWord(String filePath) throws Exception {
		String line;
		BufferedReader bw = new BufferedReader(new FileReader(filePath));
		line = bw.readLine();

		while (line != null) {
			stopWord.put(line, 0);
			line = bw.readLine();
		}
		logger.info("停用词词典大小是" + stopWord.size());

	}

	// 提取博文关键词，输入分完词的文档路径，写一个整理后的分词文件
	public static void combineWord(String destFile, List<String> content)
			throws Exception {
		String Str;
		String[] s;
		BufferedWriter bw = new BufferedWriter(new FileWriter(destFile));
		System.out.println(content.size());
		for (int i = 0; i < content.size(); i++) {
			Str = content.get(i);

			s = Str.split(" ");
			bw.append(s[0] + " ");
			// 处理一篇博文
			for (int j = 1; j < s.length; j++) {

				// System.out.println(s.length+" "+s[j]+" "+j);
				if (s[j] == null || s[j].length() == 0) {
					logger.info("s[j]为空");
					s[j] = null;
					continue;
				}
				logger.info(s[j] + " 长度是" + s[j].length() + "\r\n");

				// 去标点符号、特殊符号,去副词,去代词,
				if (s[j].indexOf("/p") > -1 || s[j].indexOf("/w") > -1
						|| s[j].indexOf("/rr") > -1
						|| s[j].indexOf("/ude1") > -1
						|| s[j].indexOf("/e") > -1 || s[j].indexOf("/y") > -1
						|| s[j].indexOf("/r") > -1 || s[j].indexOf("vshi") > -1
						|| s[j].indexOf("/x") > -1 || s[j].indexOf("/o") > -1
						|| s[j].indexOf("/c") > -1 || s[j].indexOf("//n") > -1
						|| s[j].indexOf("/d") > -1 || s[j].indexOf("/f") > -1
						|| s[j].indexOf("/m") > -1 || s[j].indexOf("#/n") > -1
						|| s[j].indexOf("~/n") > -1 || s[j].indexOf("*/n") > -1
						|| s[j].indexOf("&/n") > -1 || s[j].indexOf("/a") > -1
						|| s[j].indexOf("/vf") > -1 || s[j].indexOf("/Mg") > -1
						|| s[j].indexOf("时候/n") > -1
						|| s[j].indexOf("还有/v") > -1 || s[j].indexOf("/s") > -1
						|| s[j].indexOf("周边/n") > -1 || s[j].indexOf("/a") > -1
						|| s[j].indexOf("/z") > -1 || s[j].indexOf("可以/v") > -1
						|| s[j].indexOf("显得/v") > -1
						|| s[j].indexOf("以为/v") > -1
						|| s[j].indexOf("不知/v") > -1 || s[j].indexOf("/b") > -1) {
					logger.info("  去标点符号、特殊符号,去副词,去代词" + s[j] + "\r\n");
					s[j] = null;
					continue;
				}
				// 去除后缀标记
				if (s[j] != null) {
					s[j] = s[j].replaceAll("/[a-z0-9]+", "");
					// 去停用词
					logger.info("检测" + s[j] + "是否是停用词\r\b");
					// System.out.println("stopWord.size="+stopWord.size()+" "+j);
					// System.out.println(stopWord.containsKey(s[i]));
					if (stopWord.containsKey(s[j])) {
						// System.out.println("停用"+s[i]);
						logger.info("停用了" + s[j] + "\r\b");
						s[j] = null;
						continue;
					}
				}
				logger.info("s[j]=" + s[j] + "\r\n");
				if (s[j] != null && s[j].length() <= 1) {
					logger.info("删掉s[j]=" + s[j] + "\r\n");
					s[j] = null;
					continue;
				}
				if (s[j] != null) {
					bw.append(s[j] + " ");
				}
				// System.out.print(s[i]);
			}
			bw.append("\r\n");
			// 至此，一个微博处理完毕
		}
		bw.flush();
		bw.close();
	}

}
