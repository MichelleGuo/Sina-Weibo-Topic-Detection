package com.data.algorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import com.data.distance.Sort;
import com.io.data.WriteKeyword;

public class HRCluster {

	private double[][] sim; // 相似度矩阵
	int n; // 样本个数
	public static List<List> clusterList; // 簇
	static ArrayList<TreeMap<String, Double>> center = new ArrayList<TreeMap<String, Double>>();

	public HRCluster(double[][] sim) {
		this.sim = sim;
		n = sim[0].length;

		clusterList = new ArrayList<List>();

		for (int i = 0; i < n; i++) { // 初始化，每个点作为一个簇
			List<Integer> temp = new ArrayList<Integer>();
			temp.add(i);
			clusterList.add(temp);
		}
	}

	public void display() {
		int clusterListSize = clusterList.size();
		for (int i = 0; i < clusterListSize; i++) {
			List<Integer> temp = new ArrayList<Integer>();
			temp = clusterList.get(i);
			for (int j = 0; j < temp.size(); j++) {
				System.out.println(i + ":" + temp.get(j));
			}
		}
	}

	// 获取每一簇的中心
	public static ArrayList<TreeMap<String, Double>> getCenter(
			ArrayList<TreeMap<String, Double>> trainset) {
		System.out.println(clusterList.size() + "*********");
		for (int i = 0; i < clusterList.size(); i++) {
			int size = clusterList.get(i).size();
			TreeMap<String, Double> sumCenter = new TreeMap<String, Double>();
			center.add(sumCenter);
			for (int j = 0; j < size; j++) {
				String num1 = clusterList.get(i).get(j).toString();
				int count1 = Integer.parseInt(num1);
				TreeMap<String, Double> train1 = trainset.get(count1);
				sumCenter = K_means.addSum(train1, sumCenter);
			}

			TreeMap<String, Double> mean = K_means.meanCenter(sumCenter, size);
			center.set(i, mean);
		}
		return center;
	}

	public void printResult(List<String> blog,
			ArrayList<TreeMap<String, Double>> trainSet, String filePath,
			int featureNum) throws Exception {
		Sort sortFeature = new Sort();
		String file = "D:\\HR_keyword.txt";

		center = getCenter(trainSet);
		WriteKeyword writeKeyword = new WriteKeyword();
		writeKeyword.printKeyWord(center, file, featureNum);
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
		// bw.append("输出聚类结果.....\r\n");
		bw.append("该数据集共聚成" + center.size() + "簇\r\n\r\n");
		for (int i = 0; i < center.size(); i++) {
			TreeMap<String, Double> trainCenter = center.get(i);
			int sampleNum = clusterList.get(i).size();
			int label = 0;
			bw.append("第" + i + "个簇共" + sampleNum + "篇博文：\r\n");
			// String content = null;
			bw.flush();
			double max = 0.0;
			// String keyword = "";
			String content = "";
			// TreeMap<String, Double> item = center.get(i);//提取某个blog的特征向量映射map
			// if(featureNum > item.size()){
			// featureNum = item.size();
			// }
			// item = sortFeature.featureSort(item, featureNum);
			// for(Map.Entry<String, Double> p : item.entrySet() ){
			//
			// keyword += p.getKey()+" ";
			// }
			// bw.append(keyword+"\r\n");
			// 获取核心博文
			for (int j = 0; j < sampleNum; j++) {
				String num1 = clusterList.get(i).get(j).toString();
				int count1 = Integer.parseInt(num1);
				TreeMap<String, Double> train = trainSet.get(count1);
				double dis = K_means.getDistance(trainCenter, train);
				if (dis > max) {
					max = dis;
					label = count1;
				}
				content += blog.get(count1).trim() + "\r\n";
			}
			bw.append("该簇核心博文为" + blog.get(label).trim() + "\r\n\r\n");
			bw.append("该簇具体内容为：" + content);

			System.out.println("正在算" + i + "簇博文的关键词");
			bw.append("\r\n\r\n");
		}
		bw.flush();
		bw.close();
	}

	public void cluster(double thd) {
		// 进行比较的两个簇
		List<Integer> listTempI = new ArrayList<Integer>();
		List<Integer> listTempJ = new ArrayList<Integer>();

		for (int iter = 0; iter < n - 1; iter++) { // 迭代次数
			double max = 0;
			for (int i = 0; i < clusterList.size() - 1; i++) {
				for (int j = i + 1; j < clusterList.size(); j++) {
					double avg = 0; //
					int sizeI = clusterList.get(i).size();
					int sizeJ = clusterList.get(j).size();

					for (int ii = 0; ii < sizeI; ii++) { // 计算两个簇之间的平均值
						for (int jj = 0; jj < sizeJ; jj++) {
							avg += this.sim[(Integer) clusterList.get(i)
									.get(ii)][(Integer) clusterList.get(j).get(
									jj)];
						}
					}

					avg = avg / (sizeI * sizeJ);
					if (avg > max) {
						max = avg;
						listTempI = clusterList.get(i);
						listTempJ = clusterList.get(j);
					}
				}
			}

			// 合并相似度最大的两个簇
			if (max < thd) {
				break;
			} else {
				for (int i = 0; i < listTempJ.size(); i++) {
					listTempI.add(listTempJ.get(i));
				}
				clusterList.remove(listTempJ);
			}
		}

		// 去除小簇
		int CLSize = clusterList.size();
		for (int i = 0; i < CLSize; i++) {
			if (clusterList.get(i).size() < 10) {
				clusterList.remove(i);
				i--;
				CLSize--;
			}
		}
		System.out.println(clusterList.size() + "------------");
	}

}
