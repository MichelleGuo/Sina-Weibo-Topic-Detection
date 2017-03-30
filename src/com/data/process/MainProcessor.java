package com.data.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.data.algorithm.DBSCAN;
import com.data.algorithm.HRCluster;
import com.data.algorithm.K_means;
import com.data.distance.CountDistance;
import com.data.extraction.Feature;
import com.io.data.FileOperation;

public class MainProcessor {

	public static String uid = "1267454277";
	public static String ResultPath = "doc" + File.separator + uid + File.separator
			+ "wb_mining" + File.separator;
	
	public static void main(String[] args) throws Exception {

		//存储博文内容的文档
		String Contentpath = ResultPath + "wbContent.txt";
		//初步分词的文档
		String Breakerpath = ResultPath + "wbBreaker.txt";
		//分词整理的文档	
		String Rearrangepath = ResultPath + "wbRearrange.txt";
		//特征词的文档
		String Featurepath = ResultPath + "wbFeature.txt";
		//特征向量的文档
		String Vectorpath = ResultPath + "wbVector.txt";

		//K-means聚类结果
		String KClusterpath = ResultPath + "wbKCluster.txt";
		//层次聚类结果
		String HClusterpath = ResultPath + "wbHCluster.txt";
		//DBScan聚类结果
		String DBscanpath = ResultPath + "wbDBscan.txt";
		
		List<String> content = WeiboReader.WeiboToTxt(uid);
//		/**
//		 * step 1
//		 */
//		MiningDriver.wordBreaker(Contentpath,Breakerpath);
//		int blogNum=0;//博文总数
//
//		/**
//		 * step 2
//		 */
//		FileOperation fileTool = new FileOperation();
//		System.out.println("对分词文档进行整合:....");
//		try {
//			List<String> brokenWord = (List<String>) fileTool.getList(Breakerpath);//存储初步分词结果
//			WordBreaker.combineWord(Rearrangepath, brokenWord);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//		System.out.println("整合完成，生成整合文档: ");
		
		
//		/**
//		 * step 3
//		 */
		Feature feature = new Feature();
//		System.out.println("抽取特征词:....");
//		try {
//			feature.scanWord(Rearrangepath);
//			feature .saveWord(Featurepath);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("特征词抽取完成，生成特征词文档文档: ...");
		
		
		
		/**
		 * step 4
		 */
		FileOperation fp = new FileOperation();
		ArrayList<TreeMap<String,Double>> featureVector = new ArrayList<TreeMap<String,Double>>();
		//存储特征向量的链表	
		System.out.println("抽取特征向量: ....");
		List<String> newContent = feature.getAllFeature(1,"tf", fp.getList(Rearrangepath));
		feature.printFeature(true,Vectorpath);
		
		feature.clear();
		featureVector = feature.allBlogFeature;
		System.out.println("特征向量的维数是"+feature.featureWord.size());
		System.out.println("特征向量抽取完成，生成特征向量" + featureVector.size() + "个");

		/**
		 * step 5
		 */
		double[][] distance;//距离矩阵
		CountDistance countDistance = new CountDistance();
		System.out.println("计算距离矩阵: ....");
		distance = countDistance.getDistanceWithother(featureVector);
		System.out.println("进行层次聚类: ....");
	    HRCluster HRClusterIn = new HRCluster(distance);
	    HRClusterIn.cluster(0.5);   //
//	    HRClusterIn.display();
	    System.out.println("计算中心……");
//	    HRClusterIn.getCenter(featureVector);	   
	    HRClusterIn.printResult(content,featureVector,HClusterpath,10);
		
		System.out.println("层次聚类完毕");
		
		
		
		/**
		 * step 6
		 */
		K_means kmeans = new K_means();
		System.out.println("------------------------");
		System.out.println("进行k_means聚类: ....");
		kmeans.KMeans(featureVector,10);
		kmeans.mergeCluster(0.8);	
		kmeans.printResult(content,featureVector,KClusterpath,10);
		System.out.println("聚类完毕");
		System.out.println("------------------------");
	
		/**
		 * step 7
		 */
		
		System.out.println("计算距离矩阵: ....");
//		distance = countDistance.getDistanceWithother(featureVector);
		System.out.println("进行DBSCAN聚类: ....");
		DBSCAN DBSCANIn = new DBSCAN(distance, 0.2, 6);
		DBSCANIn.cluster();
//		DBSCANIn.display();
		DBSCANIn.printResult(newContent,featureVector, DBscanpath, 10);

		System.out.println("DBSCAN聚类完毕");
		
	}
	
}
