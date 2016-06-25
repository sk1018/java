package jp.ac.fukushima_u.gp;

//
//FileDialogのデモ例
//
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileDialogDemo{
	public static void main(String args[]) {
		//アプリはここから始まります。
		new WindowTest();
	}
}

//
//WindowTest クラスは, Windowの表示を行います。
//
//Windowのイベント応答をの例
//
class WindowTest extends Frame implements WindowListener{
	//コンストラクタ
	WindowTest() {
		//FileDialogによるファイルの取得
		FileDialog fileDialog = new FileDialog(this);//FileDialogの作成
		fileDialog.setVisible(true);//表示する
		String dir = fileDialog.getDirectory();//ディレクトリーの取得
		String fileName = fileDialog.getFile();//File名の取得
		if (fileName == null)
			System.exit(0);//ファイル名の設定が無ければ処理中止
			
		//指定したファイルからデータを読み取り表示
		try {//ファイルをか使うには、例外処理が必要
		
			String s;//読み込んだデータを保持する文字列
			FileReader rd = new FileReader(dir + fileName);//読み取り用として、ファイルとアプリを繋ぐ
			BufferedReader br = new BufferedReader(rd);//BufferdReaderの作成
			
			s = br.readLine();//最初の1行を読み込む
			while (s != null) {
				add(new Label(s));//読み込んだ文字列をラベルで表示
				s = br.readLine();//次の1行を読む
			}
			
			br.close();//閉じる
			rd.close();
			
		} catch (IOException e) {
			//エラーが発生したら エラーを表示
			System.out.println("Err=" + e);
		}
		
		//Windowを設定し表示
		setSize(240, 240);//サイズを指定す
		setLayout(new GridLayout(20, 1));//1列×20行のレイアウトを設定
		addWindowListener(this);//WindowListenerを設定
		setVisible(true);//可視化する
		
	}
	
	//アクティブになった特の処理
	public void windowActivated(java.awt.event.WindowEvent e) {
		System.out.println("Activated");
		
	}
	
	//閉じられた時の処理
	public void windowClosed(java.awt.event.WindowEvent e) {
		System.out.println("Closed");
		
	}
	
	//閉じられている時の処理
	public void windowClosing(java.awt.event.WindowEvent e) {
		System.out.println("Closing");
		System.exit(0);
	}
	
	//アクティブでなくなったときの処理
	public void windowDeactivated(java.awt.event.WindowEvent e) {
		System.out.println("Deactivaed");
		
	}
	
	//アイコンから戻ったときの処理
	public void windowDeiconified(java.awt.event.WindowEvent e) {
		System.out.println("Deicnified");
		
	}
	
	//アイコン化された時の処理
	public void windowIconified(java.awt.event.WindowEvent e) {
		System.out.println("Icnified");
		
	}
	
	// 開かれた時の処理
	public void windowOpened(java.awt.event.WindowEvent e) {
		System.out.println("Opend");
		
	}
}