package jp.ac.fukushima_u.gp.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Template {

	public static void main(String[] args) {

	}

	void inputKeybord() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			String str = br.readLine();
			System.out.println("" + str);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception :" + e);
		}

	}

	void inputKeybordInt() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String s = null;
		Integer t = null;
		int a = 0;
		try {
			for (int i = 0; i < 3; i++) {
				s = br.readLine();
				t = new Integer(s);
				a += t.intValue();
			}
			System.out.println(a);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	static byte 	parseByte(String)
	static short   parseShort(String)
	static int     parseInt(String)
	static long    parseLong(String)
	static float   parseFloat(String)
	static double  parseDouble(String)
	 */

	//javaのクラスパスを取得するメソッド
	//ただし、実行場所がDropbox以下の階層でないと取得失敗になる
	//eclipseの場合要注意
	public static String getCLASSPATH() {
		String key = "java.class.path";
		String s = System.getProperty(key);

		//		if (s.equals("C:\\hoge\\Dropbox\\pro\\workspace\\test\\bin")) {
		//			return "C:\\hoge\\Dropbox\\pro\\java";
		//		}

		String[] ss = s.split("\\\\");
		String t = "";
		for (int i = 0; i < ss.length; i++) {
			t += ss[i] + "\\";
			if (ss[i].equals("Dropbox")) {
				t += "pro\\java\\";
				return t;
			}
		}

		return null;
	}

}
