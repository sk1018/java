package jp.ac.fukushima_u.gp.template.text_read;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

public class TextRead {

	public TextRead() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		String str="C:\\hoge\\Dropbox\\pro\\java\\jp\\ac\\fukushima_u\\gp\\test.xml";
		TextRead.simpleRead(str);
		TextRead.encodeRead(str, "UTF-8");
	}

	/**
	 * 対象テキストファイルのエンコードを変更することなく読み込むシンプルな読み込みメソッド
	 * <BR>シンプルな反面、エンコードの指定が変更できないのでエンコードが異なるファイルが正しく読み込まれない
	 * @param str 対象ファイルまでの絶対パス
	 */
	public static void simpleRead(String str) {
		try {
			FileReader fr = new FileReader(str);
			char[] buf = new char[1024];
			while (fr.read(buf) > 0) {
				System.out.println(buf);
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 対象のテキストファイルを指定したエンコードで読み込むメソッド
	 * @param str 対象ファイルまでの絶対パス
	 * @param enc 対象ファイルの文字コード
	 */
	public static void encodeRead(String str, String enc) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(str),enc));
			String temp=new String("");
			while((temp=br.readLine())!=null){
				System.out.println(temp);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
