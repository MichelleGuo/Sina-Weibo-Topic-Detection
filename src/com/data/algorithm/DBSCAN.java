package com.data.algorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

import com.io.data.WriteKeyword;

public class DBSCAN {
	private double[][] sim;
	static List<List> clusterList;
	int n;
	double simThd;
	int MinPts;
	static ArrayList<TreeMap<String, Double>> center = new ArrayList<TreeMap<String, Double>>();

	public DBSCAN(double[][] sim, double simThd, int MinPts) {
		this.sim = sim;
		this.n = sim.length;
		this.simThd = simThd;
		this.MinPts = MinPts;
		clusterList = new ArrayList<List>();

		for (int i = 0; i < n; i++) { // 初始化，每个样本作为一类保存在一个箱子中
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
		// Sort sortFeature = new Sort();
		ArrayList<TreeMap<String, Double>> center = getCenter(trainSet);
		String file = "D:\\DB_keyword.txt";
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

	public void cluster() {
		for (int i = 0; i < n; i++) {

			List<Integer> clusterI = new ArrayList<Integer>();
			clusterI = clusterList.get(i);

			if ((Integer) clusterI.get(0) != -1) { // 如果箱子不空

				Set<Integer> CISet = new HashSet<Integer>();
				for (int CISize = 0; CISize < clusterI.size(); CISize++) { // 箱子中的集合
					CISet.add(clusterI.get(CISize));
				}

				for (int j = 0; j < clusterI.size(); j++) { // 箱子中的第j个样本
					int row = (Integer) clusterI.get(j);
					List<Integer> clusterTemp = new ArrayList<Integer>();
					for (int column = 0; column < n; column++) {
						if (sim[row][column] >= simThd && row != column) {
							clusterTemp.add(column);
						}
					}
					int clusterTempSize = clusterTemp.size();
					if (clusterTempSize >= MinPts) { // 如果满足密度条件，合并样本作为一类
						for (int ii = 0; ii < clusterTempSize; ii++) {
							if (!CISet.contains(clusterTemp.get(ii))) {
								CISet.add(clusterTemp.get(ii));
								clusterI.add(clusterTemp.get(ii));
								clusterList.get(clusterTemp.get(ii)).set(0, -1); // -1标记该点被移
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < clusterList.size(); i++) {
			if ((Integer) clusterList.get(i).get(0) == -1) {
				clusterList.remove(i);
				i--;
			}
		}

	}

}
