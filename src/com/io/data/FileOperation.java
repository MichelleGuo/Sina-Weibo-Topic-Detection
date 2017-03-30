package com.io.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FileOperation {
	
public List<String> list = new ArrayList<String>();
	
	public List<String> getList(String filePath) throws Exception
	{
		BufferedReader bw = new BufferedReader(new FileReader(filePath));
		String line = bw.readLine();
		while(line!=null)
		{
		list.add(line);	
		line = bw.readLine();
		}
		//bw.close();
		return list;
	}
	
	public void writeList(List<String> list,String filePath) throws Exception
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
		for(int i=0;i<list.size();i++)
		{
			bw.write(list.get(i));
		}
		bw.flush();
		bw.close();
		System.out.println("list写入成功");
		
	}

}
