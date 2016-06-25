package jp.ac.fukushima_u.gp.template.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.ac.fukushima_u.gp.template.Template;

public class WebDateGetTest {
	private HttpURLConnection connect = null;
	private InputStream in = null;
	private OutputStream out = null;

	/**
	 * HTTPへのアクセスを確立して、inputstreamを取得する<br>
	 * webからのデータ取得待機中の状態にする<BR>
	 * なお、標準ではGETで通信を行う？
	 * @param adress データを取得したいURL
	 * @throws Exception 何らかのエラーが発生した場合
	 */
	public void setOpenHttpConnect(String adress) throws Exception {
		//URLの準備とHTTPへのアクセス
		//引数のadressに取得したいデータのあるURLを入力
		//"http://files.myopera.com/kaoris/blog/Opera_512x512.png";
		URL url;
		url = new URL(adress);
		connect = (HttpURLConnection) url.openConnection();
		connect.setRequestMethod("GET");
		in = connect.getInputStream();
	}

	/**
	 * 前提として、setOpenHttpConnectを行なってデータ取得待機中にしておくこと
	 * <BR>setOpenHttpConnectで待機中にしたURLからデータを取得する
	 * <BR>取得したデータは「クラスパス＋outdir」という絶対パスに格納される
	 * @param outdir パス名とファイル名を入力
	 * @throws Exception 何らかのエラー発生時
	 */
	public void getDatatoHttp(String outdir) throws Exception {
		//HTTPからデータを取得
		byte[] buf = new byte[1024];
		int len;
		char[] temp = outdir.toCharArray();
		if (temp[1] == new String(":").charAt(0)) {
			out = new FileOutputStream(outdir);
		}
		else {
			out = new FileOutputStream(Template.getCLASSPATH() + outdir);
		}
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.flush();
		out.close();
	}

	/**
	 * setで開いたHttpコネクションとInputStreamの二つを閉じる
	 * <BR>setでコネクションを開いたら実行しておくこと
	 * <BR>setでエラー吐いてもはかなくてもやっておくと安全
	 * <BR>setで一つも開いていなくても，開いていなければスルー，開いていれば閉じるから
	 */
	public void setCloseHttpConnect() {
		try {
			in.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}//InputStreamを閉じる
		connect.disconnect();//サイトの接続を切断

	}

	public static void main(String[] argv) {
		WebDateGetTest wd = new WebDateGetTest();
		try {
			String adress = "http://files.myopera.com/kaoris/blog/Opera_512x512.png";
			wd.setOpenHttpConnect(adress);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		try {
			String outdir = "jp/ac/fukushima_u/gp/materials/test/sample.png";
			wd.getDatatoHttp(outdir);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		wd.setCloseHttpConnect();

	}

	public WebDateGetTest() {
	}

}
