package com.data.extraction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;


public class Feature {

	// 日志对象
	public static Logger logger = Logger.getLogger(Feature.class);
		
	public LinkedList featureWord;// 存储特征词
	public ArrayList<TreeMap<String, Double>> allBlogFeature = new ArrayList<TreeMap<String, Double>>();// 存储所有blog的特征向量
	public double blogNum;// 博文数

	public Feature() {
		featureWord = new LinkedList();
	}

	// 判断词是否加入特征库
	public boolean isWordExist(String word) throws IOException {
		for (Object item : featureWord) {
			if (item.toString().equals(word)) {
				// System.out.println(word+"已经成为特征词,不再加入");
				// cn.edu.ices.main.Parameter.log.writeLog(word+"已经成为特征词,不再加入");
				return true;
			}

		}
		return false;
	}

	// 将词加入特征库
	public void addWord(String word) throws IOException {
		featureWord.add(word);
		// System.out.println(word+"成功加入特征词集合");
		// cn.edu.ices.main.Parameter.log.writeLog(word+"成功加入特征词集合");
	}

	public void scanWord(String filePath) throws Exception {
		BufferedReader buffer = new BufferedReader(new FileReader(filePath));
		String line = buffer.readLine();// 一行即一条微博
		String[] s;
		// System.out.println(!line.equals("\r\n\r\n")+"********");
		while (line != null) {
			blogNum++;
			if (line == "\n") {
				System.out.println("空行");
				continue;
			}
			s = line.split(" ");
			for (int i = 1; i < s.length; i++) {
				if (!isWordExist(s[i]))// 如果该词没有被当作特征词
				{
					addWord(s[i]);// 添加新关键词
				}
			}
			line = buffer.readLine();
		}
		buffer.close();
	}

	// 保存抽取出的关键词到文件
	public void saveWord(String filePath) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));

		for (Object item : featureWord) {
			bw.append(item.toString() + " ");
		}
		bw.flush();
		bw.close();
	}

	// 计算某个特征词的文档词频
	public int countWord(String word,List<String> content) throws Exception {
		int n = 0;
		String s[];
		
		for(int i=0;i<content.size();i++)
		{
			s = content.get(i).split(" ");
			for(int j=0;j<s.length;j++)
			{
			if (s[j].equals(word)) {
				n++;
			   }
			}

		}
		return n;

	}

	// 计算词的权重，采用tf*idf算法,抽取一篇微博的特征向量
	public void getBlogFeature(String blog, List<String> content, int wordthold)
			throws Exception {
		TreeMap newMap = new TreeMap();
		String word;// 某词
		double fi = 0;// 某词的博文词频
		double ni = 0;// 某词的文档频数
		double weight = 0;// 权重

		String s[] = blog.split(" ");
		// 遍历每个词，统计一篇博文的词频
		logger.info("博文的内容是" + blog + "\r\n");
		for (int i = 0; i < s.length; i++) {
			fi = 0;
			if (s[i] != null && !s[i].equals(" ") && !s[i].equals("\r")
					&& !s[i].equals("\r\n") && !s[i].equals("\n")) {
				word = s[i];
			} else {
				continue;
			}
			ni = countWord(s[i], content);// 如果词频小于阈值，不作为特征词
			if (ni < wordthold) {
				break;
			}
			for (int j = 0; j < s.length; j++) {
				if (s[j].equals(word)) {

					fi++;
				}
			}

			weight = fi * Math.log(blogNum / ni);// 权重计算完毕
			if (word.length() != 1 && word.length() != 0) {
				newMap.put(word, weight);// 放入map
			}
			logger.info("博文总数" + blogNum + "\r\n");
			logger.info(word + "的博文词频是" + fi
					+ "\r\n");
			logger.info(word + "的文档词频是" + ni
					+ "\r\n");
			logger.info(word + "的权重是" + weight
					+ "\r\n");
		}
		newMap = normalize(newMap);// 归一化
		allBlogFeature.add(newMap);
	}

	// 计算词的权重，采用tf算法,抽取一篇微博的特征向量
	public void getBlogTFFeature(String blog, List<String> content, int wordThold)
			throws Exception {
		TreeMap newMap = new TreeMap();
		String word;// 某词
		double fi = 0;// 某词的博文词频
		double ni = 0;// 某词的文档词频
		String s[] = blog.split(" ");
		// 遍历每个词，统计一篇博文的词频
		logger.info("博文的内容是" + blog + "\r\n");
		
		//遍历博文中的每个词
		for (int i = 1; i < s.length; i++)
		{
			fi = 0;
			ni = countWord(s[i], content);// 如果词频小于yu，不作为特征词
			word = s[i];
			
			if (ni < wordThold) {
				logger.info(s[i]+"共出现"+ni+"次，不能作为特征词");
				continue;
			   }
			
			for (int j = 1; j < s.length; j++) {
				if (s[j].equals(word)) {
					fi++;
				}
			}
			if (word.length() != 1 && word.length() != 0) {
				newMap.put(word, fi);// 放入map
				logger.info(word+"放入map中\r\n");
			}
			logger.info("博文总数" + blogNum + "\r\n");
			logger.info(word + "的博文词频是" + fi
					+ "\r\n");
		}
		newMap = normalize(newMap);
		allBlogFeature.add(newMap);
	}

	public TreeMap<String, Double> normalize(TreeMap<String, Double> map) {
		double sum = 0;
		TreeMap<String, Double> newMap = new TreeMap<String, Double>();
		Set<Entry<String, Double>> set1 = map.entrySet();
		Iterator<Entry<String, Double>> itor1 = set1.iterator();
		while (itor1.hasNext()) {
			Entry<String, Double> entry = itor1.next();
			sum = sum + entry.getValue() * entry.getValue();
		}
		sum = Math.pow(sum, 0.5);// 模值

		Set<Entry<String, Double>> set = map.entrySet();
		Iterator<Entry<String, Double>> itor = set.iterator();
		while (itor.hasNext()) {
			Entry<String, Double> entry = itor.next();
			newMap.put(entry.getKey(), entry.getValue() / sum);

		}

		return newMap;
	}

	//获取所有博文的特征向量
	public List<String> getAllFeature(int wordthold,
			String type, List<String> content) throws Exception {
		int id = 0;
		int size = content.size();
		String str ;
		
		//遍历所有博文
		for(int i=0;i<size;i++)
		{
			str = content.get(i);
			logger.info("抽取第" + i + "篇博文\r\n");
			logger.info("博文内容" + str);
			
			if (type.equals("tf"))
			  {
				getBlogTFFeature(str, content, wordthold);// 获取TF值
			   }else{
				getBlogFeature(str, content, wordthold);// 获取所有博客的特征向量
			  }
		}
		
		System.out.println("抽取博文结束");
		return content;
	}

	// 如果true，那么保存文件
	public void printFeature(boolean flag, String filePath) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));

		logger.info("共有特征向量"
				+ allBlogFeature.size() + "个\r\n");
		for (int i = 0; i < allBlogFeature.size(); i++) {
			logger.info("第" + i + "个的特征向量如下：\r\n");
			TreeMap<String, Double> item = allBlogFeature.get(i);// 提取某个blog的特征向量映射map
			Set<Entry<String, Double>> set = item.entrySet();
			Iterator<Entry<String, Double>> itor = set.iterator();
			while (itor.hasNext()) {
				Entry<String, Double> entry = itor.next();
				if (flag) {
					bw.append(entry.getKey() + ":" + entry.getValue() + " ");
					// System.out.println("成功写入");

				} else {
					System.out.println(entry.getKey() + "     "
							+ entry.getValue());
				}
			}
			
			bw.append("\r\n");
		}
	}
	
	public void clear()
	{ 
		System.out.println("****");
		for(int i=0;i<allBlogFeature.size();i++)
		{
			TreeMap tm = allBlogFeature.get(i);
			if(tm.size()==0)
			{
				allBlogFeature.remove(i);
				System.out.println("移除空特征值");
			}
		}
	}

}
