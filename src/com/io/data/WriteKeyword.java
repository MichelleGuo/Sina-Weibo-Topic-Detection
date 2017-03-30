package com.io.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;
import java.util.Map.Entry;

import com.data.distance.Sort;


public class WriteKeyword {

	public void printKeyWord(ArrayList<TreeMap<String, Double>> center,String filePath, int n) throws Exception{
		Sort sortFeature = new Sort();
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
		for(int i = 0; i < center.size(); i++){
			String keyword="";
			bw.append("第"+ i+"簇的关键词如下：\r\n");
			TreeMap<String, Double> item = center.get(i);//提取某个blog的特征向量映射map
			if(n > item.size()){
				n = item.size();
			}
			item = sortFeature.featureSort(item, n);
			  Set<Entry<String, Double>> set = item.entrySet();
			  Iterator<Entry<String, Double>> itor = set.iterator();
			  while(itor.hasNext())
			  {
			    Entry<String, Double> entry = itor.next();
			    keyword += entry.getKey()+" " ;
			    
			  }
			  bw.append(keyword+"\r\n\r\n");
			  bw.flush();
		}
		bw.close();
	}
}
