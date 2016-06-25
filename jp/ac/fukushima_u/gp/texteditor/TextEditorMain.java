package jp.ac.fukushima_u.gp.texteditor;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextEditorMain implements ActionListener{

	private TextEditorWin win;

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		TextEditorMain main = new TextEditorMain();
		main.setAction();

	}

	public TextEditorMain() {
		win = new TextEditorWin();
		win.setVisible(true);
	}

	public void setAction() {
		win.miOpen.addActionListener(this);
		win.miSaveBufAs.addActionListener(this);
		win.miExit.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//上書き保存の案
		/*
		 * ファイルまでの絶対パスを表すフィールドを用意しておき、初期値ではnullを入れておく
		 * で、ファイル参照ダイアログを実行後、そこから絶対パスを取得し、
		 * それらがnull（ファイルを選ばなかった時）以外の場合、それを現在操作中のファイルの絶対パスとして、保存しておく
		 * 「上書き保存」を選択した場合、絶対パスを保存したフィールドを参照し、null以外の場合、ファイル参照ダイアログを省略し、
		 * フィールドに格納されている絶対パスを元に上書き保存を行う
		 * フィールドがnullのまま上書き保存を実行しようとしたら、「名前をつけて保存」を実行
		 */

		//終了時の保存確認の案
		/*
		 * 前提として、ウィンドウの閉じるボタン（右上のバツボタン）を押した時にイベントを挟めること
		 *
		 * 現在の内容と編集内容と、ファイルから取得した過去の内容を比較し、
		 * 変更がなければそのまま終了し、変更があれば「ファイルを保存する・しない・キャンセル」の三択ウィンドウを表示
		 * 変更済みか否かの判断が難しい場合、判断なしにとりあえずウィンドウを提示することもありうる
		 *
		 * 編集の確認に関しては、Boolean型のフィールドを用意
		 * 次に実際に編集を行うテキストエリアはKeyListenerに登録しておく
		 * Boolean型のフィールドは初期値はtrueに設定する
		 * このフィールドがtrueならば変更なし、falseならば変更有りとする
		 *
		 * KeyListenerから、テキストエリア内でなんらかのキー操作が感知された場合フィールドをfalseに
		 * 保存処理を行った場合にはフィールドをtrueに変更する
		 * ファイルの変更の有無を確認する際にはこのフィールドを参照するようにする
		 *
		 * 注意として、Booleanを扱う場合には、どっちの状態が何を指しているかを明確にする
		 * 今回の場合、新規ファイル・ファイル展開後・保存処理後のファイルに対し、
		 * true  : 変更なし
		 * false : 変更あり
		 * これをそれぞれ指している
		 *
		 * これを明確にしておかない場合、どちらがどちらかわからなくなり、間違って逆に設定してしまうなどのバグのもととなる
		 *
		 */

		//新規ファイルの案
		/*
		 * ファイルメニュー内にアイテムとして「新規作成」とでも用意
		 *
		 * 実行時の処理としては、終了確認時と同様に、変更してから保存されたかどうかを確認し、保存済みなら処理続行、
		 * 未保存ならば確認ウィンドウを提示し、その処理を行ってから処理を続行
		 *
		 * 次にテキストエリアに対し、エリア内をクリアするメソッドを実行する
		 * もしない場合には適当に空文字列（""であってnullではない）を用意し、これをsetTextとしてかける
		 *
		 * ウィンドウタイトルを編集中のファイル名として設定している場合には、タイトルも「新規」とかに更新すること
		 *
		 */

		// TODO 自動生成されたメソッド・スタブ
		if (e.getSource() == win.miOpen) {
			//「ファイルを開く」の実装部分
			//ファイル参照ダイアログをロードモードで表示
			FileDialog fd = new FileDialog(win, "開くファイルを選択", FileDialog.LOAD);
			fd.setVisible(true);

			//ファイル参照ダイアログからファイル名を取得し、それをウィンドウのタイトルとして設定する
			//ファイル名がnullの場合はファイルを選択肢なかったとして処理を終了
			String fileName = fd.getFile();
			if(fileName==null){
				return;
			}
			else{
				win.setTitle(fileName);
			}

			//ここからは入力情報を元にした実際に開く処理
			//ファイルを開いたり、中身を取り出したりするのは例外の発生が起こりうるので、try-catchブロックでくくる
			try {
				//新規インスタンスを引数に新規インスタンスを生成し、更にそれを引数に…と繰り返してるからわかりにくいが、
				//ユーザが入力した情報を連結して絶対パスに変換し、絶対パスを元にファイルのインスタンスを作成する
				//作成したファイルのインスタンスを元にファイルの読み込みを行うファイルリーダークラスのインスタンスを生成
				//ファイルリーダークラスでも読み込みはできるが、このままでは効率が悪いため、ファイルリーダークラスの
				//ラッパークラスであるバッファードリーダークラスでラッピングしている

				//結論だけ言うと、ユーザが入力した絶対パスのファイルをターゲットとして
				//読み込みを行うBufferedReaderクラスのインスタンス用意した
				BufferedReader br = new BufferedReader(new FileReader(new File(fd.getDirectory() + fd.getFile())));
				//空のStringインスタンスを用意する
				//この文字列にどんどん連結していき、最終的にファイル内の全文字列を連結し終わったらこの文字列を出力する
				String str = new String("");
				//無限ループの開始
				//多分ここはdo-while文のほうがいいかもしれない
				while (true) {
					//新しいString変数に、ファイルから一行単位で読み取った文字列を格納
					String s = br.readLine();
					//変数sがnullの場合、すべての文字列を読み込んだということなのでbreak
					//そうでなければ続行
					if (s == null)
						break;
					//文字列strに対し、strの後ろに今回読み取った文字列sを連結し、一行分なので最後に改行を挟む
					str = str + s + "\n";
				}
				//strにはファイル内の全文字列が連結して格納されているので、それを出力する
				win.ta.setText(str);

				//プログラムの外部のファイル等への接続やコネクションは、使ったら必ず閉じる
				//これを怠るとファイルが破損したり、ずっと使用中になってしまったりする
				//昔ほど厳密に行わなくても何とかなるが、あくまで忘れた時の保険とし、基本は手動で閉じる
				br.close();
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック

				//ここにはエラーメッセージとして、ダイアログを出して
				//「エラー発生。開けませんでした」と表示を行いたい。
				//その際、可能ならばエラーコードとかも表示できるとわかりやすい
				e1.printStackTrace();
			}
		}

		if (e.getSource() == win.miSaveBufAs) {
			//いわゆる「名前をつけて保存」

			//最初はユーザによる情報入力部
			//ファイル参照ダイアログをセーブモードで表示
			FileDialog fd = new FileDialog(win, "名前をつけて保存する", FileDialog.SAVE);
			fd.setVisible(true);

			//ファイル参照ダイアログから、入力された「ディレクトリまでのパス」と「ファイル名」を取得
			//なお、ディレクトリ名に関しては、最後が「￥」で終わってるので、パスとファイル名を連結するだけで絶対パスになる
			//「C:\hoge\temp\」+「test.txt」=「C:\hoge\temp\test.txt」

			//ここでは確認のための出力
			System.out.println("dir=" + fd.getDirectory());
			System.out.println("FileName=" + fd.getFile());

			//ファイル参照ダイアログからファイル名を取得し、それをウィンドウのタイトルとして設定する
			//ファイル名がnullの場合はファイルを選択肢なかったとして処理を終了
			String fileName = fd.getFile();
			if(fileName==null){
				return;
			}
			else{
				win.setTitle(fileName);
			}

			//ここからは入力情報を元にした実際の保存処理
			//ファイルを開いたり、それに書き込んだりするのは例外の発生が起こりうるので、try-catchブロックでくくる
			try {
				//新規インスタンスを引数に新規インスタンスを生成し、更にそれを引数に…と繰り返してるからわかりにくいが、
				//ユーザが入力した情報を連結して絶対パスに変換し、絶対パスを元にファイルのインスタンスを作成する
				//作成したファイルのインスタンスを元にファイルの書き込みを行うファイルライタークラスのインスタンスを生成
				//ファイルライタークラスでも書き込みはできるが、このままでは効率が悪いため、ファイルライタークラスの
				//ラッパークラスであるバッファードライタークラスでラッピングしている

				//結論だけ言うと、ユーザが入力した絶対パスのファイルをターゲットとして
				//書き込みを行うBufferedWriterクラスのインスタンス用意した
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fd.getDirectory() + fd.getFile())));

				//テキストエリアに書かれているテキストを取得し、それを書き込む
				//なお、これはファイルがなければ生成しそこに書き込みを行い、ファイルが有る場合には上書きを行う
				bw.write(win.ta.getText());
				//確認用
				//				System.out.print(ta.getText());
				//プログラムの外部のファイル等への接続やコネクションは、使ったら閉じる
				//これを怠るとファイルが破損したり、ずっと使用中になってしまったりする
				//昔ほど厳密に行わなくても何とかなるが、あくまで忘れた時の保険とし、基本は手動で閉じる
				bw.close();
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック

				//ここにはエラーメッセージとして、ダイアログを出して
				//「エラー発生。保存されませんでした」と表示を行いたい。
				//その際、可能ならばエラーコードとかも表示できるとわかりやすい

				e1.printStackTrace();
			}
		}

		if (e.getSource() == win.miExit) {
			//システム終了宣言
			System.exit(0);
		}
	}

}
