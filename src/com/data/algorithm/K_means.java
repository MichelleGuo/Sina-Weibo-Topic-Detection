package com.data.algorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.io.data.WriteKeyword;




public class K_means {

//	static CollatorComparator comparator = new CollatorComparator();
	public static int iter = 0;
	public static int newK = 10; 
	int sampleNum = 0;
	ArrayList<String> sameType = new ArrayList<String>();
	public static ArrayList<TreeMap<String,Double>> newCenter = new ArrayList<TreeMap<String,Double>>();

	//	返回2个样本的相似度
	public static double getDistance(TreeMap<String,Double> train1, TreeMap<String,Double> train2){

		double sim = 0.0, sumvalue1 = 0.0, sumvalue2 = 0.0, sumvalue = 0.0;
		ArrayList<Double> train1Value = new ArrayList<Double>();
		ArrayList<Double> train2Value = new ArrayList<Double>();
//		第2篇文档的模的平方和
		for(Map.Entry<String, Double> p: train2.entrySet()){
			sumvalue2 += Math.pow(p.getValue(), 2.0);
		}
		
		for(Map.Entry<String, Double> p : train1.entrySet()){
			String keyword = p.getKey();
			train1Value.add(p.getValue());
			sumvalue1 +=Math.pow(p.getValue(), 2);  //train1模的平方和
			if(train2.containsKey(keyword)){
				train2Value.add(train2.get(keyword));
			}
			else {
				train2Value.add(0.0);
			}
		}
		sumvalue = Math.sqrt(sumvalue1) * Math.sqrt(sumvalue2); 
		// 计算距离,余弦
		for (int k = 0; k < train1Value.size(); k++) {

			sim += train1Value.get(k) * train2Value.get(k);
		}
			sim = sim /sumvalue;
		return  sim;
	}

//	计算新的中心的
	public static TreeMap<String, Double> addSum(TreeMap<String, Double> train1, TreeMap<String, Double> train2){
		TreeMap<String, Double> sum = new TreeMap<String, Double>();
		TreeMap<String, Double> trainLarge = train1;
		TreeMap<String, Double> trainSmall = train2;
		int size = train1.size();
//		int large = 0;
		if(size < train2.size()){
			size = train2.size();
			trainLarge = train2;
			trainSmall = train1;
		}

			for(Map.Entry<String, Double> p: trainLarge.entrySet()){
				String keyword = p.getKey();
				sum.put(keyword, 0.0);
				if(trainSmall.containsKey(keyword)){
					double value = p.getValue()+ trainSmall.get(keyword);
					sum.put(keyword, value);
				}else{
					double value = p.getValue();
					sum.put(keyword, value);
				}
			}
			for(Map.Entry<String, Double> p: trainSmall.entrySet()){
				String keyword = p.getKey();
				if(!sum.containsKey(keyword)){
					sum.put(keyword, p.getValue());
				}
			}
		return sum;
	}
	
////	计算新中心的平均
	public static TreeMap<String, Double> meanCenter(TreeMap<String, Double> center, int n){
		TreeMap<String, Double> mean = new TreeMap<String, Double>();
		for(Map.Entry<String, Double> p: center.entrySet()){
			double meanValue = p.getValue() / n;
			mean.put(p.getKey(), meanValue);			
		}
		return mean;
	}
	
//	聚类算法
	public ArrayList<Integer> KMeans(ArrayList<TreeMap<String,Double>> trainSet, int k){
	
		ArrayList<Integer> type = new ArrayList<Integer>();	
		
		ArrayList<Integer> numOfEachType = new ArrayList<Integer>();
		int numberOfSample = trainSet.size();	
		for(int i = 0; i < numberOfSample; i++){
			type.add(-1);
		}
		ArrayList<TreeMap<String,Double>> oldCenter = new ArrayList<TreeMap<String,Double>>();
		
		
		ArrayList<Integer> randNumber = new ArrayList<Integer>();
		 newK = k;
//   随机初始化k个中心  
	        Random rand = new Random();
	        boolean[] bool = new boolean[numberOfSample];	        
	        int num =0;	        
	        for (int i = 0; i<newK; i++){	           	    
	            do{
	                //如果产生的数相同继续循环
	                num = rand.nextInt(numberOfSample);    	             
	            }while(bool[num]);	            
	            bool[num] =true;	     
	            System.out.println("随机中心"+num);
	            randNumber.add(num);	        	        
	        }
	        
		//获取初始的K个中心点
		for(int i=0; i<newK; i++)
		{
				newCenter.add(trainSet.get(randNumber.get(i)));
		}
		
//		对每个样本进行聚类
		boolean isEqual = false;
		while(!isEqual){
			iter++;
			int oldK = newK;

			sameType.clear();
			oldCenter.clear();  //把就的清空，确保下一次循环的
			numOfEachType.clear();
			for(int i = 0; i < oldK; i++){
					oldCenter.add(newCenter.get(i));
					numOfEachType.add(0);
					sameType.add("");
			}
//			对每个训练集
			
			int label ;
			for(int i = 0; i < numberOfSample; i++){
				label = -1;
				double distance = 0.0;
				TreeMap<String, Double> train1 = trainSet.get(i);	
//				与每个簇中心比较距离
				for(int j = 0; j < oldK; j++){
					TreeMap<String, Double> centerTrain = oldCenter.get(j);  //中心
					double trueDistance = getDistance(train1,centerTrain);   //相似度越高就归于哪一个中心去
//					System.out.println(trueDistance);
					if(trueDistance > distance){
						distance = trueDistance;
						label = j;
					}//选取最近的那个中心
					
				}
				if(label >= 0){
					int count = numOfEachType.get(label);
					numOfEachType.set(label, ++count);
					type.set(i, label); //更改该样本的所属类型
//					计算新中心的总和
					String trainNum = sameType.get(label);
//					if(!trainNum.equals(i+",")){
						trainNum += i+",";
//					}
					sameType.set(label, trainNum);
					TreeMap<String, Double> oldCenter1 = oldCenter.get(type.get(i)); //获取该样本的所属中心
					TreeMap<String, Double> newTrain2 = trainSet.get(i);	
					if(!oldCenter1.equals(newTrain2)){

						TreeMap<String, Double> sum = addSum(oldCenter1,newTrain2);   //同一个簇的样本的总和				
						newCenter.set(label,sum); //所有同簇同样特征词的权重
					}
				}
				else{
					newK++;
					newCenter.add(trainSet.get(i));
					sameType.add(i+",");
					randNumber.add(i);
					numOfEachType.add(1);
				}

			}
//			计算新的中心
			for(int i = 0; i < oldK; i++){
				if(numOfEachType.get(i) == 0){
					TreeMap<String, Double> meancenter = meanCenter(newCenter.get(i), 1);
					newCenter.set(i, meancenter);	
				} else{
					TreeMap<String, Double> meancenter = meanCenter(newCenter.get(i), numOfEachType.get(i));
					newCenter.set(i, meancenter);
				}
								
			}

//			判断新的中心是否相等
			double dis = 0.0;
			for(int i = 0; i < oldK; i++){
				TreeMap<String, Double> nwCenter = newCenter.get(i);
				TreeMap<String, Double> odCenter = oldCenter.get(i);
				dis = getDistance(nwCenter,odCenter);	
				System.out.println(dis+"--------");
				if( dis<0.8 ){	
					System.out.println(dis+"--------");
					break;
				}else if(i == oldK-1){
					isEqual = true;
				}
			}
			
		}
		return type;
	
	}

//	对簇合并
	public void mergeCluster(double distance){
		
		boolean flag = false;
		while(!flag){
			int count = 0;
			int oldK = newK;
			for(int i = 0; i < newK-1; i++ ){
				TreeMap<String, Double> center = newCenter.get(i);
				for(int j = i+1; j < newK; j++){
					double dis = getDistance(newCenter.get(i),newCenter.get(j));
//					若2簇的余弦距离大于阈值则合并2簇，并
					if(dis > distance){
						newK--;
						TreeMap<String, Double> sumCenter = addSum(center, newCenter.get(j));
						TreeMap<String, Double> nwCenter = meanCenter(sumCenter, 2);
						newCenter.set(i, nwCenter);
						newCenter.remove(j);
						String trainNum = sameType.get(i)+ sameType.get(j);
						sameType.set(i, trainNum);
						sameType.remove(j);
//						j--;
						--j;
					}
				}
				if(newK == oldK){
					count++;
				}
			}
			if(count == oldK - 1){
				flag = true;
			}
		}
		
//		过滤离群点
		for(int i = 0; i < sameType.size(); i++){
			String s[] = sameType.get(i).trim().split(",");			
			if(s.length <= 10){
				sameType.remove(i);
				newCenter.remove(i);
				i--;
			}
		}
	}
	
	public void printResult(List<String> blog,ArrayList<TreeMap<String,Double>> trainSet,String filePath, int n) throws Exception{
//		ArrayList<String> content = getContent("F:\\DM\\blogContent.txt");
		String file = "D:\\K-means_keyword.txt";
		WriteKeyword writeKeyword = new WriteKeyword();
		writeKeyword.printKeyWord(newCenter, file, n);
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
//		Sort sortFeature = new Sort();
		bw.append("该数据集共聚为"+ sameType.size()+"簇\r\n\r\n");
		for(int i = 0; i < sameType.size(); i++){
			
			double max = 0.0;
			int label = 0;
			String content = "";
			String s[] = sameType.get(i).trim().split(",");
			bw.append("第"+ i+"簇共"+s.length+"篇博文：\r\n");
//				
//					String keyword = "";
//					TreeMap<String, Double> item = newCenter.get(i);//提取某个blog的特征向量映射map
//					if(n > item.size()){
//						n = item.size();
//					}
//					item = sortFeature.featureSort(item, n);
//					  Set<Entry<String, Double>> set = item.entrySet();
//					  Iterator<Entry<String, Double>> itor = set.iterator();
//					  while(itor.hasNext())
//					  {
//					    Entry<String, Double> entry = itor.next();
//					    keyword += entry.getKey()+" " ;
//					    
//					  }
//					  
//					  bw.append(keyword+"\r\n\r\n");    
//					  System.out.println("正在算"+ i+"簇博文的关键词");
////					  bw.append("该簇核心博文为：");
//				}
			for(int j = 0; j < s.length; j++){
				int num = Integer.parseInt(s[j]);
//				获取核心博文
				TreeMap<String, Double> train = trainSet.get(num);
				double dis = getDistance(train, newCenter.get(i));
				if(dis > max){
					max = dis;
					label = num;
				}
				content += blog.get(num).trim()+"\r\n\r\n";
				
			}
			bw.append("其核心博文为："+blog.get(label).trim()+"\r\n");
			
			bw.append(content);
			bw.append("\r\n\r\n\r\n");
			bw.flush();
		}
	
		bw.close();
//		Sort sortFeature = new Sort();
//	    BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
//	    bw.append("输出聚类结果.....\r\n");
//		System.out.println("输出聚类结果.....");
//		int count[] = new int[sameType.size()];
//		
//		for(int i = 0; i < sameType.size();i++){
//			String s[] = sameType.get(i).trim().split(",");
//			count[i] = s.length;
//			System.out.println("属于第"+ i + "簇博文的数量是"+ count[i]);
//			bw.append("属于第"+ i+ "簇的博文\r\n");
//			for(int j = 0; j < s.length; j++){
//				bw.append(s[j]+"     ");
//			}
//			bw.append("\r\n");
//		}
//		bw.append("\r\n");
//		for(int i=0;i<newCenter.size();i++)
//		{
//			bw.append("第"+i+"个簇包含"+count[i]+"篇博文，该簇主题词:");
//			double max = 0.0;
//			String keyword = "";
//			 String content="";
//			TreeMap<String, Double> item = newCenter.get(i);//提取某个blog的特征向量映射map
//			if(featureNum > item.size()){
//				featureNum = item.size();
//			}
//				item = sortFeature.featureSort(item, featureNum);
//			  Set<Entry<String, Double>> set = item.entrySet();
//			  Iterator<Entry<String, Double>> itor = set.iterator();
//			  while(itor.hasNext())
//			  {
//			      Entry<String, Double> entry = itor.next();
//			      if(entry.getValue() > max){
//			    	  max = entry.getValue();
//			    	  keyword = entry.getKey();
//			      }
////			     content += entry.getKey()+" " + entry.getValue()+" ";
//			     content += entry.getKey()+" " ;
//			    
//			  }
//			  bw.append(keyword+",该簇关键词如下：\r\n");
//			  bw.append(content);    
//			  System.out.println("正在算"+ i+"簇博文的关键词");
//			  bw.append("\r\n\r\n");
//		}
//		System.out.println("聚类结果输出完毕"+filePath);
//		bw.flush();
//		bw.close();
	}

}
