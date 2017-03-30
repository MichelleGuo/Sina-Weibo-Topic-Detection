package com.data.algorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class hierarchyCluster {
	
	// 日志对象
		public static Logger logger = Logger.getLogger(hierarchyCluster.class);

//	public static void main(String args[]) {
//		double[][] test = { { 0 }, { 1, 0 }, { 10, 2, 0 }, { 9, 10, 10, 0 },
//				{ 10, 10, 10, 10, 0 }, { 10, 10, 10, 10, 5, 0 },
//				{ 10, 10, 10, 10, 6, 5, 0 }, { 10, 7, 10, 10, 8, 9, 10, 0 },
//				{ 10, 10, 10, 10, 10, 10, 10, 10, 0 }, };
//
//		hierarchyCluster h = new hierarchyCluster();
//
//		int[] type = h.herarchy(test, 29);
//
//		for (int i = 0; i < test.length; i++) {
//			System.out.println(type[i]);
//		}
//	}

	public hierarchyCluster() {

	}
	int[] label;
	int[] type;

	public int[] herarchy(double[][] test, int iter) {
		int row = test.length;
		// int column = test[0].length;

		double min = 10000; // 设置极大值
		int minRow = 0;
		int minColumn = 0;

		// 实现层次聚类所用到辅助数组和结果数组
		 label = new int[row];
		 type = new int[row];

		int it = 0;

		for (int i = 0; i < row; i++) {
			label[i] = -1;
		}

		while (it < iter) // 控制迭代次数
		{
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < i; j++) {
					if (test[i][j] < min && test[i][j] != -1) // 以-1标记寻找到的最小值
					{
						min = test[i][j];
						minRow = i;
						minColumn = j;
					}
				}
			}

			int labelMax = minRow;
			int labelMin = minColumn;
			while (label[labelMin] != -1) {
				labelMin = label[labelMin];
			}
			while (label[labelMax] != -1) {
				labelMax = label[labelMax];
			}
			if (labelMax > labelMin) {
				label[labelMax] = labelMin;
			} else if (labelMin > labelMax) {
				label[labelMin] = labelMax;
			}

			min = 10000;
			test[minRow][minColumn] = -1; // 以-1标记寻找到的最小值

			it++;
		}

		int typeTemp = 1;
		for (int i = 0; i < row; i++) {
			if (label[i] == -1) {
				type[i] = typeTemp;
				typeTemp++;
			}
		}

		for (int i = 0; i < row; i++) {
			if (label[i] != -1) {
				type[i] = type[label[i]];
			}
		}

		return type;
	}
	
	public int[] herarchy_layer(double[][] test, int cluster)
	{
		int row = test.length;
		//int column = test[0].length;
		int layer = row-cluster;
			
		double min = 10000;  //设置极大值
		int minRow = 0;
		int minColumn = 0;
	
		//实现层次聚类所用到辅助数组和结果数组
		int[] label = new int[row];
		int[] type = new int[row];
		
		int it = 0;
		
		for(int i=0; i<row; i++)
		{
			label[i] = -1;
		}
		
		while(it<layer)    //控制迭代次数
		{
			for(int i=0; i<row; i++)
			{
				for(int j=0; j<i; j++)
				{
					if(test[i][j] < min && test[i][j] != -1 )  //以-1标记寻找到的最小值
					{
						min = test[i][j];
						minRow = i;
						minColumn = j;				
					}
				}
			}
			
			int labelMax = minRow;
			int labelMin = minColumn;
			
			while(label[labelMin]!=-1)
			{
				labelMin = label[labelMin];
			}
			while(label[labelMax]!=-1)
			{
				labelMax = label[labelMax];
			}
			if(labelMax > labelMin )
			{
				label[labelMax] =labelMin;
				it++;
			}
			else if(labelMin > labelMax)
			{
				label[labelMin] =labelMax;
				it++;
			}
			
		
			min = 10000; 
			test[minRow][minColumn] = -1;   //以-1标记寻找到的最小值

		}
		
		int typeTemp=1;
		for(int i=0; i<row; i++)
		{
			if(label[i] == -1)
			{
				type[i] = typeTemp;
				typeTemp++;
			}
		}
		
		for(int i=0; i<row; i++)
		{
			if(label[i]!=-1)
			{
				type[i]=type[label[i]];
			}
		}
		return type;
	}
	
	public int[] deleteRepeat(int a[]) throws IOException
	{
		HashMap<Integer,Integer> hm = new HashMap<Integer,Integer>();
		
		for(int i=0;i<a.length;i++)
		{
			logger.info("a[i]="+a[i]+"\r\n");
			hm.put(a[i], 0);
		}
		Object[] b =hm.keySet().toArray();
		int c[] = new int[b.length];
		for(int i=0;i<b.length;i++)
		{
			c[i] = ((Integer)b[i]).intValue();
			logger.info("c[i]="+c[i]+"\r\n");
		}
		return c;
	}
	
	
	public void printResult(String filePath,ArrayList<TreeMap<String,Double>> featureVector) throws Exception{
		HashMap<Integer,ArrayList> cluster = new HashMap<Integer,ArrayList>();//
	    BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
	    bw.append("输出层次聚类结果.....\r\n");
		System.out.println("输出层次聚类结果.....");
		
		int[] newType = deleteRepeat(type);//对类标进行去重
		for(int i=0;i<newType.length;i++)
		{
			int c = newType[i];
			ArrayList arry = new ArrayList();
			for(int j=0;j<type.length;j++)
			{
				if(type[j]==c)
				{
					arry.add(j);
				}
			}
			cluster.put(i, arry);//博文i属于类型type[i]
		}
		
		for(int i = 0; i < cluster.size();i++){
//			System.out.println("属于第"+ i+ "簇的博文");
			bw.append("属于第"+ i+ "簇的博文\r\n");
			for(int j = 0; j < cluster.get(i).size(); j++){
				bw.append(cluster.get(i).get(j)+"     ");
//				System.out.print(cluster.get(i).get(j)+"     ");
			}
			bw.append("\r\n");
		}
		
//		for(int i=0;i<newCenter.size();i++)
//		{
//			bw.append("第"+i+"个簇的特征词如下:\r\n");
//			TreeMap<String, Double> item = newCenter.get(i);//提取某个blog的特征向量映射map
//			  Set<Entry<String, Double>> set = item.entrySet();
//			  Iterator<Entry<String, Double>> itor = set.iterator();
//			  while(itor.hasNext())
//			  {
//			      Entry<String, Double> entry = itor.next();
//			      bw.append(entry.getKey()+" ");    
//			  }	  
//			  bw.append("\r\n");
//		}
		System.out.println("层次聚类结果输出完毕"+filePath);
		bw.flush();
		bw.close();
	}
	
}
