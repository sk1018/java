package jp.ac.fukushima_u.gp.template.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class POSTMethodTest {

	public static void main(String[] args) {
		String urlString = "http://jlp.yahooapis.jp/FuriganaService/V1/furigana";//�ڑ�����URL
		String YahooAppID = "dj0zaiZpPUZGYjNBNHMxcERJZyZzPWNvbnN1bWVyc2VjcmV0Jng9NDY-";
		String postStr = "sentence=%E6%98%8E%E9%8F%A1%E6%AD%A2%E6%B0%B4";//POST����f�[�^
		postStr+="&"+YahooAppID;
		try {
//			URL url = new URL(urlString);
			URL url = new URL("http://localhost:123");
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoOutput(true);//POST�\�ɂ���

			uc.setRequestMethod("POST");

			uc.setRequestProperty("HOST", "jlp.yahooapis.jp");// �w�b�_��ݒ�
			uc.setRequestProperty("User-Agent", "");// �w�b�_��ݒ�
			uc.setRequestProperty("Accept-Language", "ja");// �w�b�_��ݒ�
			uc.setRequestProperty("Yahoo AppID", YahooAppID);// �w�b�_��ݒ�
			uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// �w�b�_��ݒ�
			uc.setRequestProperty("Content-Length", Integer.toString(postStr.length()));// �w�b�_��ݒ�
			OutputStream os = uc.getOutputStream();//POST�p��OutputStream���擾

			PrintStream ps = new PrintStream(os);
			ps.print(postStr);//�f�[�^��POST����
			ps.close();

			System.out.println("test");

			InputStream is = uc.getInputStream();//POST�������ʂ��擾
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String s;
			while ((s = reader.readLine()) != null) {
				System.out.println(s);
			}
			reader.close();
		} catch (MalformedURLException e) {
			System.err.println("Invalid URL format: " + urlString);
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Can't connect to " + urlString);
			System.exit(-1);
		}
	}
}