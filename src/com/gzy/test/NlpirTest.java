package com.gzy.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import utils.SystemParas;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class NlpirTest {

	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		// CLibrary Instance = (CLibrary) Native.loadLibrary(
		// "D:\\NLPIR\\bin\\ICTCLAS2013\\x64\\NLPIR", CLibrary.class);

		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"D:\\ICTCLAS2015\\lib\\win64\\NLPIR", CLibrary.class);

		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);

		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);

		public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);

		public int NLPIR_AddUserWord(String sWord);// add by qp 2008.11.10

		public int NLPIR_DelUsrWord(String sWord);// add by qp 2008.11.10

		public String NLPIR_GetLastErrorMsg();

		public void NLPIR_Exit();
	}

	public static String transString(String aidString, String ori_encoding,
			String new_encoding) {
		try {
			return new String(aidString.getBytes(ori_encoding), new_encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		// String argu = "D:\\NLPIR";
		String argu = "D:\\ICTCLAS2015";

		/**
		 * test-utf8
		 */
		//String filePath = "D:\\ICTCLAS\\test.txt";
		String filePath = "F:\\DM\\blogContent.txt";
		// String system_charset = "GBK";//GBK----0
//		InputStreamReader read = new InputStreamReader(
//				new FileInputStream(filePath), "GBK");// 考虑到编码格式
//		BufferedReader bufferedReader = new BufferedReader(read);
//		String sInput = bufferedReader.readLine();
//		while ((bufferedReader.readLine()) != null) {
//			input = bufferedReader.readLine();
//			//input =
//		}
		
		String system_charset = "UTF-8";
		int charset_type = 1;

		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;

		if (0 == init_flag) {
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is " + nativeBytes);
			return;
		}

		//String sInput = "据悉，质检总局已将最新有关情况再次通报美方，要求美方加强对输华玉米的产地来源、运输及仓储等环节的管控措施，有效避免输华玉米被未经我国农业部安全评估并批准的转基因品系污染。";

		String sInput = "年终大盘点，兜 ，你今年真腐败了，麻麻自己不怎么淘宝的！晚上平安夜我们去教堂，叫主赐我们现金，金条也可以！";
		// String nativeBytes = null;
		try {
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);

			System.out.println("分词结果为： " + nativeBytes);

			CLibrary.Instance.NLPIR_AddUserWord("要求美方加强对输 n");
			CLibrary.Instance.NLPIR_AddUserWord("华玉米的产地来源 n");
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
			System.out.println("增加用户词典后分词结果为： " + nativeBytes);

			CLibrary.Instance.NLPIR_DelUsrWord("要求美方加强对输");
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
			System.out.println("删除用户词典后分词结果为： " + nativeBytes);
			
			//nativeBytes = CLibrary.Instance.ICTCLAS_FileProcess("");

			int nCountKey = 0;
			String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(sInput, 10,
					false);

			System.out.println("关键词提取结果是：" + nativeByte);

			nativeByte = CLibrary.Instance.NLPIR_GetFileKeyWords(
					"D:\\blogContent.txt", 10, false);

			System.out.print("关键词提取结果是：" + nativeByte);

			CLibrary.Instance.NLPIR_Exit();

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

	}

}
