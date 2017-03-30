package com.data.distance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.data.algorithm.K_means;

public class CountDistance {

	/**
	 * @param args
	 */

	int TextSize = 1000;
	static CollatorComparator comparator = new CollatorComparator();

//	// 将文件转换为映射
//
//	public ArrayList<TreeMap<String, Double>> convertToMap(String fileName) {
//
//		ArrayList<TreeMap<String, Double>> list = new ArrayList<TreeMap<String, Double>>();
//		BufferedReader in = null;
//		try {
//			in = new BufferedReader(new FileReader(fileName));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		String s = null;
//
//		try {
//			// one line
//			while ((s = in.readLine()) != null) {
//				TreeMap<String, Double> ret = new TreeMap<String, Double>(
//						comparator);
//				String[] wordWeigh = s.trim().split(",");
//
//				for (int i = 0; i < wordWeigh.length; i++) {
//					String pi = wordWeigh[i];
//					String[] p = pi.trim().split(":");
//					ret.put(p[0], Double.parseDouble(p[1]));
//
//				}
//				list.add(ret);
//			}
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return list;
//	}

	// 计算每一个博文与其他博文的距离

	public double[][] getDistanceWithother(
			ArrayList<TreeMap<String, Double>> trainList) {

		int size = trainList.size();
		double[][] distanceS = new double[size][size];
		ArrayList<Double> valueArray = new ArrayList<Double>();// 存放当前博文的词的权重
		ArrayList<Double> index = new ArrayList<Double>();// 存储其他博文的词的权重
		// ArrayList<Double> distance = new ArrayList<Double>();
		double sumvalue = 0.0, sumindex = 0.0;
		double sim = 0.0;
		for (int i = 0; i < trainList.size(); i++) {
			TreeMap<String, Double> dataMap = trainList.get(i); // 当前博文之一的词：权重映射
			for (int j = i; j < trainList.size(); j++) {
				TreeMap<String, Double> map = trainList.get(j);
				sim = K_means.getDistance(dataMap, map);
				distanceS[i][j] = sim;
				// System.out.println(sim);
				sim = 0.0;
				index.clear();
			}

			valueArray.clear();
		}
		// 填充下三角矩阵 ，自己和自己的相似度
		for (int i = 0; i < distanceS.length; i++) {
			for (int j = 0; j < i; j++) {
				distanceS[i][j] = distanceS[j][i];
			}
		}
		// 返回一个距离矩阵
		return distanceS;
	}

	public static void main(String[] args) {
		// // TODO Auto-generated method stub
		// // 测试
		// CountDistance dis = new CountDistance();
		// ArrayList<TreeMap<String, Double>> trainList = dis
		// .convertToMap("./train.txt");
		//
		// // 存放返回的距离
		// double[][] list = dis.getDistanceWithother(trainList);
	}

}
