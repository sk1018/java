package jp.ac.fukushima_u.gp.nampre;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * ナンプレ解読プログラムのメインクラス。 * 主にwindow系を担当するクラス
 * <BR>ユーザはこのクラスのインスタンスに対し操作を行い、インスタンスは適切なメソッドへ入力を投げ、その結果を表示する
 *
 * @author GP
 *
 */
public class Window extends JFrame implements ActionListener
{
	/**
	 * windowに表示する、9*9のナンプレマップを模した配置のテキストフィールド
	 */
	private JTextField[][] tf = new JTextField[9][9];
	/**
	 * 初期windowに表示する基本的なボタン
	 * 各テキストフィールドに入力されたテキストを読み取り、それを元に解読を実行する
	 */
	private JButton ok;
	/**
	 * テキストフィールドに入力された文字を全て空にする
	 */
	private JButton clear;
	/**
	 * 現在表示中のテキストフィールドの文字を、コピーしやすいように全ての文字を1つのテキストエリアに入力する
	 */
	private JButton copy;
	/**
	 * テキストフィールドの表示する文字を、前回okボタンをおした時の状態に戻す
	 * <BR>複数回押した場合にはその回数分前の状態に戻り、一番最初の状態で押した場合にはエラー表示用のwindowが起動する
	 */
	private JButton back;
	/**
	 * サブwindow用のボタン
	 * 現在表示中のサブwindowのインスタンスでsetVisible(false)を実行する
	 */
	private JButton Attention, copyWinOk;
	/**
	 * サブwindowのインスタンス
	 */
	private JDialog atten, copyWin;
	/**
	 * ボタンbackで戻るための初期状態を保存するためのスタック
	 */
	private Stack<MapForList> stack = new Stack<MapForList>();

	/**
	 * プログラム実行用のmainメソッド
	 * @param args nullでok
	 */
	public static void main(String[] args) {

		Window inwin = new Window();
		inwin.mapWindowCreate();

	}

	/**
	 * windowクリエイト用のメソッド
	 * @param str windowタイトル
	 * @param width window横幅
	 * @param height window高さ
	 */
	void jframeCreate(String str, int width, int height) {

		Container CP = getContentPane();
		CP.setLayout(new FlowLayout());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(str);
		setSize(width, height);

	}

	/**
	 * JButton作成用のメソッド
	 * <br>なお、ActionListenerはこのインスタンスに登録
	 * @param str ボタンに表示するテキスト
	 * @return 作成されたボタンのインスタンス
	 */
	public JButton buttonCreate(String str) {
		JButton b1 = new JButton(str);
		b1.addActionListener(this);
		getContentPane().add(b1);
		return b1;
	}

	/**
	 * このインスタンス内の各種ボタンをおした時に実行されるアクションを記入したメソッド
	 */
	public void actionPerformed(ActionEvent e) {
		System.out.println("OK");

		if (e.getSource() == ok) {
			int[][] map = new int[9][9];
			map = getMap();
			stack.push(new MapForList(map));
			map = jp.ac.fukushima_u.gp.nampre.solve.Solve.solve(map);
			if (map == null) {
				attentionWindowCreate("INPUT IS ERROE!");
			}
			else {
				outMap(map);
				First.outputCons(map);
			}
		}
		if (e.getSource() == clear) {
			clearMap();
		}
		if (e.getSource() == Attention) {
			atten.setVisible(false);
		}
		if (e.getSource() == copy) {
			copyWinCreate();
		}
		if (e.getSource() == back) {
			if (stack.empty())
				attentionWindowCreate("NO DATE!");
			else
				setMap(stack.pop().getMap());
		}
		if (e.getSource() == copyWinOk) {
			copyWin.setVisible(false);
		}
	}

	/**
	 * コピー用のサブwindowを作成し，そこにマップを表示するメソッド
	 * <BR>ウィンドウを作成し，テキストエリアを張り付ける
	 * <BR>その後，テキストエリアにメインwindowが現在表示中の文字を取得し張り付ける
	 */
	private void copyWinCreate() {
		int w = 100;
		int h = 210;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		copyWin = new JDialog(this, "copy window", true);
		copyWin.setBounds(d.width / 2 - w / 2, d.height / 2 - h / 2, w, h);
		copyWin.setLayout(new BorderLayout());

		String str = new String("");

		int[][] map = getMap();

		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				str += map[i][l] + " ";
			}
			str += "\n";
		}

		JTextArea ta = new JTextArea(str, 9, 9);
		copyWin.getContentPane().add("Center", ta);

		copyWinOk = new JButton("ok");
		copyWinOk.addActionListener(this);
		copyWin.getContentPane().add("South", copyWinOk);

		copyWin.setVisible(true);
	}

	/**
	 * サブwindow<BR>
	 * 問題が発生した際に，警告を表示する為のwindow
	 * <BR>タイトルは「Attention Window」で固定
	 * @param s windowに表示したい文字列
	 * @return 生成したサブwindowのインスタンス
	 */
	private void attentionWindowCreate(String s) {
		int w = 200;
		int h = 100;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		atten = new JDialog(this, "ATTENTION WINDOW", true);
		atten.setBounds(d.width / 2 - w / 2, d.height / 2 - h / 2, w, h);
		atten.getContentPane().setLayout(new GridLayout(2, 1));
		atten.getContentPane().add(new JLabel(s));
		Attention = new JButton("OK");
		Attention.addActionListener(this);
		atten.getContentPane().add(Attention);
		atten.setVisible(true);

	}

	/**
	 * メインwindow作成用のメソッド
	 * <BR>main関数から，とりあえずこれが実行される
	 * <BR>一応メソッドとして独立しているけど，たぶんコンストラクタにしてもいい気がする
	 *
	 */
	public void mapWindowCreate() {
		jframeCreate("input", 300, 300);
		Container CP = getContentPane();
		CP.setLayout(new GridLayout(11, 9));

		for (int i = 0; i < 9; i++) {
			if (i != 4)
				CP.add(new JLabel(" "));
			else
				CP.add(new JLabel("input"));
		}

		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				tf[i][l] = new JTextField("", 3);
				CP.add(tf[i][l]);
			}
		}
		for (int i = 0; i < 9; i++) {
			if (i == 2)
				ok = buttonCreate("ok");
			else if (i == 3)
				copy = buttonCreate("copy");
			else if (i == 5)
				back = buttonCreate("back");
			else if (i == 6)
				clear = buttonCreate("clear");
			else
				CP.add(new JLabel(" "));
		}

		setVisible(true);

	}

	/**
	 * メインwindowの9*9のテキストフィールドから文字列を取得し，1-9ならそのまま，そうでなければ0として，
	 * int型二次元配列に変換して返すメソッド
	 * @return 各座標のテキストフィールドの入力文字列を0-9に変換して，対応した座標に代入したint型二次元配列
	 */
	private int[][] getMap() {
		int[][] map = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				map[i][l] = -1;
				String s = tf[i][l].getText();
				try {
					map[i][l] = Integer.parseInt(s);
					if (map[i][l] < 1 || map[i][l] > 9)
						map[i][l] = 0;
				} catch (NumberFormatException e) {
					map[i][l] = 0;
				}
			}
		}

		return map;
	}

	/**
	 * 引数として入力されたint[9][9]の値を，対応した座標のテキストフィールドに出力する
	 * @param map テキストフィールドに出力したいint[9][9]
	 */
	private void outMap(int[][] map) {
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				tf[i][l].setText(String.valueOf(map[i][l]));
			}
		}
	}

	/**
	 * メインwindowの全テキストフィールドを空にするメソッド
	 */
	private void clearMap() {
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				tf[i][l].setText("");
			}
		}
	}

	/**
	 * メインwindowの9*9のテキストフィールドに、引数として与えられたmapの値を出力する
	 * <BR>テキストフィールドの座標(x,y)にmap[x][y]の値を出力する
	 * @param map メインwindowに出力したいint[][]
	 */
	private void setMap(int[][] map) {
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				String s = new String("");
				if (map[i][l] != 0)
					s += map[i][l];
				tf[i][l].setText(s);
			}
		}
	}

	/**
	 * ラッパークラス{@literal ?}内で、int型二次元配列をスタックとして使うためにオブジェクト化するためのクラス
	 * <BR>入力用のコンストラクタと出力用のメソッドのみを持つ
	 * @author GP
	 *
	 */
	private class MapForList {
		/**
		 * 入力データを保持するフィールド
		 */
		int[][] map = new int[9][9];

		/**
		 * データのコピーを行うコンストラクタ
		 * <BR>単純にmap=opeでないのは、int[][]型の変数であるため、参照先のコピーになる可能性があるため
		 * <BR>その為、配列の各場所ごとに値のコピーを行う
		 * @param ope 保存を行いたいint[][]型変数
		 */
		MapForList(int[][] ope) {
			for (int i = 0; i < 9; i++) {
				for (int l = 0; l < 9; l++) {
					map[i][l] = ope[i][l];
				}
			}

		}

		/**
		 * 出力用のメソッド
		 * <BR>内部に保持していたint[][]をそのまま返す
		 *
		 * @return 保持していたint[][]型データ
		 */
		public int[][] getMap() {
			return map;
		}

	}

}
