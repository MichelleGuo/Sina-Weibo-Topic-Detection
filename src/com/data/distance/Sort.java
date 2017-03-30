package com.data.distance;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class Sort {

	private static final Logger logger = Logger.getLogger(Sort.class.getName());
	public TreeMap<String, Double> featureSort(TreeMap<String, Double> feature, int k) throws Exception{
		TreeMap<String, Double> result = new TreeMap<String, Double>();
		
		String keyword=null;
		int count = 0;
		while( count < k){
			double max = 0.0; 
			for(Map.Entry<String, Double> p: feature.entrySet()){
				if(p.getValue() > max){
					max = p.getValue();
					keyword = p.getKey();
				}
			}
			logger.info("当前最大的值"+keyword+"  "+ max+"\r\n");
			result.put(keyword, max);
			feature.remove(keyword);			
			count++;
		}	
		return result;
	}
	
	public static void main(String args[]){
//		TreeMap<String, Double> t1 = new TreeMap<String, Double>();
//		t1.put("圣诞", 4.0);
//		t1.put("礼物", 2.3);
//		t1.put("开心", 1.5);
//		t1.put("新年", 7.0);
//		
//		new Sort().featureSort(t1, 2);
	}
}
