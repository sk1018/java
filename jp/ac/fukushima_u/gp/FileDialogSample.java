package jp.ac.fukushima_u.gp;

import java.awt.FileDialog;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;


/*
 * ウィンドウズでよく見る「ファイルを開く・保存する」のダイアログを表示する
 * 
 * ダイアログなので、親ウィンドウがあって、そこからのポップアップ
 * モーダルダイアログ？なので、これが出てる間は親ウィンドウは操作不可
 * 
 */
public class FileDialogSample extends WindowAdapter{
	public static void main(String args[]) {
		new FileDialogSample().start();
	}

	private void start() {
		//親となるウィンドウを設定
		//一応親はなしでも開くことは可能
        JFrame frame = new JFrame();
        frame.setSize(400 , 150);
        frame.setVisible(true);
        frame.addWindowListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //ダイアログのインスタンスを作成して、setVisibleで可視化
        //引数はそれぞれ親ウィンドウ、タイトル、読み込みか保存か
        //最後の引数は、ボタンが「開く」「保存」のどちらにするかの指定
        FileDialog f_dialog = new FileDialog(frame, "title test", FileDialog.LOAD);
        f_dialog.setVisible(true);


		String dir = f_dialog.getDirectory();//ディレクトリーの取得
		String fileName = f_dialog.getFile();//File名の取得


//取得した情報を表示する
		frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));
		frame.add(new Label("dir="+dir));
		frame.add(new Label("FileName="+fileName));
		frame.setVisible(true);

		System.out.println("dir="+dir);
		System.out.println("FileName="+fileName);

		//ここからはテキストファイルを開いて中身を読み出す部分
		//FileDialogSampleとしては蛇足
		FileReader fr = null;
		try {
			fr = new FileReader(new File(dir+fileName));
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		int c=0;
		while(true){
			try {
				c=fr.read();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			if(c==-1)break;

		System.out.print((char)c);
    }}
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}