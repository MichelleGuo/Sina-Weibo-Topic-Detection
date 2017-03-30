package com.data.distance;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestSort {
	
	public static final String QBchange(String QJstr) {
        String outStr = "";
        String Tstr = "";
        byte[] b = null;

        for (int i = 0; i < QJstr.length(); i++) {
                try {
                        Tstr = QJstr.substring(i, i + 1);
                        b = Tstr.getBytes("unicode");
                } catch (java.io.UnsupportedEncodingException e) {
                        e.printStackTrace();
                }
                if (b[3] == -1) {
                        b[2] = (byte) (b[2] + 32);
                        b[3] = 0;
                        try {
                                outStr = outStr + new String(b, "unicode");
                        } catch (java.io.UnsupportedEncodingException e) {
                                e.printStackTrace();
                        }
                } else
                        outStr = outStr + Tstr;
        }
        return outStr;
}
	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		CollatorComparator comparator = new CollatorComparator();
////		comparator.compare(element1, element2)
//	    TreeMap<String, String> map = new TreeMap<String, String>(comparator);
////		TreeMap map = new TreeMap();
//		for (int i = 0; i < 10; i++) {
//			String s = "" + (int) (Math.random() * 1000);
//			map.put(s, s);
//		}
//		map.put("abcd", "abcd");
//		map.put("Abc", "Abc");
//		map.put("bbb", "bbb");
//		map.put("BBBB", "BBBB");
//		map.put("北京", "北京");
//		map.put("中国", "中国");
//		map.put("上海", "上海");
//		map.put("厦门", "厦门");
//		map.put("香港", "香港");
//		map.put("碑海", "碑海");
//		Collection col = map.values();
//		Iterator it = col.iterator();
//		while (it.hasNext()) {
//			System.out.println(it.next());
//		}
		
		 String res = "/|@|⌒|△|●|_|\\w|\\s";
		 Pattern p=Pattern.compile(res);
		String str ="玛丽外宿中:你好吗？！！！！!!!我只能期待下一部戏，我表示对这部戏很无力     //@322wuli_Suk:⌒△⌒ //@azuresky_";
//		String s 
		str = QBchange(str);
		Matcher m = p.matcher(str);
		if(m.find()){
			str = m.replaceAll("");
		}
		
		System.out.println(str);
		
//		String str="For my money, the important thing "+"about the meeting was bridge-building";
//		String regEx="a|f"; //表示a或f 
//		Pattern p=Pattern.compile(regEx);
//		Matcher m=p.matcher(str);
//		boolean result=m.find();
//		if(result){
//			str = m.replaceAll("");
//		}
//		System.out.println(str);
	}
}