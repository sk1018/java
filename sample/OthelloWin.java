package sample;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import othello.Game;

/**
 * オセロの盤面及び各種の情報を表示するクラス.
 * @author seiya
 *
 */
public class OthelloWin extends JFrame implements ComponentListener,
		MouseListener, ActionListener {

	/**
	 * windowに表示する盤面の石の状況を保持する２次元配列<BR>
	 * この二次元配列の状況を参照して、window上の白黒の石の配置を行う<BR>
	 * オセロの盤面なので、各次元とも要素数が8として宣言している
	 */
	private int[][] map = new int[8][8];

	/**
	 * 表示する情報をひと通り載せたパネル<BR>
	 * このパネルに各種情報を表すラベルやテキストエリアを張り付けていく<BR>
	 * window系は、基本的にラベル（文字列や画像の表示）やボタン、テキストエリアやテキストフィールドといった、<BR>
	 * コンポーネントをwindowに相当するFrameやJFrameに貼り付けて表示していく<BR>
	 * Panelはラベルやボタンなどのコンポーネントをまとめるためのもので、パネルにボタンなどを貼り付けた後<BR>
	 * そのパネルをwindowに貼り付けると、パネルに貼り付けたコンポーネントがまとめて表示可能<BR>
	 * パネルとコンポーネント、ウィンドウの詳細な関係は自身で調べてください
	 */
	Panel infoP;
	/**
	 * 棋譜を表示するテキストエリア
	 */
	TextArea ta;
	/**
	 * プレイヤー名を表示するラベル
	 */
	Label myName;
	/**
	 * 自身の色を表示するラベル
	 */
	Label myColor;
	/**
	 * 現在何手目かを表示するラベル
	 */
	Label turn;
	/**
	 * 現在の白と黒の石の数を表示するラベル
	 */
	Label stoneNum;
	/**
	 * 試合開始から現在までの経過時間を表示するラベル
	 */
	Label time;
	/**
	 * 現在の手番の開始時からの経過時間を表すラベル
	 */
	Label turnTime;
	/**
	 * 現在の盤面状況の、一手前や一手後、最新の状況を表示する際に操作するボタン
	 */
	JButton back, forward, now;
	/**
	 * 各種の情報を表示するためのwindow
	 */
	JFrame infoWin;

	/**
	 * 現在の盤面状況からn手前を表示する際の、nに相当するフィールド<BR>
	 * ユーザの操作により、現在の手数*-1から0まで増減し、「step前の手数の時の盤面状況を表示する」際の判断に用いる
	 */
	int step = 0;

	/**
	 * ダブルバッファリング用のイメージ領域<BR>
	 * ダブルバッファリングとは、描画作業中は非表示領域で行い、作業完了後に表示領域を新しく描画したイメージで一気に塗り替えるもの
	 * <BR>これを行わないと、描画作業中の状態も出力されてしまうため、画面がちらつく場合がある
	 */
	Image buf;
	/**
	 * ダブルバッファリング用のイメージ領域に描画するための変数<BR>
	 * ダブルバッファリング用のイメージ領域に描画する際には、この変数の各種メソッドを用いての描画を行う
	 */
	Graphics bufG;

	/**
	 * OthelloMap型のインスタンスを保持するクラスフィールド<BR>
	 * どこからでもアクセスできるようにクラスフィールドに格納する<BR>
	 * このwinクラスから実行した場合の、「人 VS CPU戦」に用いるためのフィールド<BR>
	 * 「人 VS CPU戦」における、盤面状況（どこに石が打たれているか？など）の保持や更新に利用<BR>
	 * 逆に言えば、サンプルプレイヤークラスから実行した場合の通常起動時には一切意味が無い
	 */
	static OthelloMap mapIns;
	/**
	 * OthelloWin型のインスタンスを保持するクラスフィールド<BR>
	 * どこからでもアクセスできるようにクラスフィールドに格納する<BR>
	 * このwinクラスから実行した場合の、「人 VS CPU戦」に用いるためのフィールド<BR>
	 * 「人 VS CPU戦」中の、盤面状況や各種の情報の表示に用いるインスタンス<BR>
	 * 逆に言えば、サンプルプレイヤークラスから実行した場合の通常起動時には一切意味が無い
	 */
	static OthelloWin winIns;
	/**
	 * 「人 VS CPU戦」専用の、現在の打ち手の色を表すフラグ<BR>
	 *  サンプルプレイヤークラスからの通常起動時には全く関係ない<BR>
	 *  「人 VS CPU戦」時には、このboolean値がtrueの時は黒の手番を表し、falseの時は白の手番を表す
	 */
	boolean black = true;
	/**
	 * 「人 VS CPU戦」専用の、現在の手数を表すint型変数<BR>
	 *  サンプルプレイヤークラスからの通常起動時には全く関係ない<BR>
	 *  「人 VS CPU戦」時には、このint型の整数値が現在何手目かを表している
	 */
	int count = 0;
	/**
	 * 「人 VS CPU戦」専用の、現在の状況までの棋譜を格納するための標準的なリスト型のインスタンス<BR>
	 *  サンプルプレイヤークラスからの通常起動時には全く関係ない<BR>
	 *  「人 VS CPU戦」時には、このリスト中に、n手目に打った座標とその時の色を格納している
	 */
	ArrayList<int[]> record = null;

	/**
	 * 「人 VS CPU戦」専用の、ある座標に置いた時に反転する石の座標を格納するリスト<BR>
	 *  サンプルプレイヤークラスからの通常起動時には全く関係ない<BR>
	 *  「人 VS CPU戦」時には、このリスト中に、ある座標に石をおいた時に反転する石の座標を格納している
	 */
	ArrayList<int[]> reversePos = null;

	int[] reverseCheckPos={-1,-1};

	/**
	 * 「人 VS CPU戦」を行うときのみ実行されるmainメソッド<BR>
	 * 通常のサーバを介しての試合を行う際には不要<BR>
	 * 「人 VS CPU戦」を行うために必要な各種のインスタンスの生成や、フィールドの初期化などの処理を行っている
	 * @param args 意味なし
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		//「人 VS CPU戦」に使うmapとwinのインスタンスを生成し、クラスフィールドに格納
		mapIns = new OthelloMap();
		winIns = new OthelloWin();

		//winのインスタンス中のフィールドの初期化や、必要なインスタンスの生成及び保持
		//windowで盤面を表示するために、初期化済みのmapの盤面をwinのフィールドにセット
		winIns.setMap(mapIns.getMap());
		//プレイヤー名をセット。今回は「人 VS CPU戦」なので、「VS CPU」と命名
		winIns.setPlayerName("VS CPU");
		//プレイヤーの色をセット。「人 VS CPU戦」時はプレイヤーの色は黒固定なので、黒をセット
		winIns.setPlayerColor(Game.COLOR.BLACK);
		//棋譜を保存するためのArrayList型のインスタンスを生成し、クラスフィールドに格納
		//初期化用のinitメソッドでSamplePlayerクラスのrecordを与えられているが、「人 VS CPU戦」の場合、
		//SamplePlayerクラスのrecordにインスタンスを格納しておらずエラーを吐くため、
		//ここでインスタンスを生成し、それを格納することでエラーを回避している
		//以後、このインスタンスに棋譜を格納していく
		winIns.record = new ArrayList<int[]>();
		//念のため、棋譜を格納するArrayList型インスタンスを空にするメソッドを実行
		winIns.record.clear();
		//ある座標に置いた時に反転する石の座標を格納するためのリストのインスタンスを生成し、クラスフィールドに格納
		winIns.reversePos = new ArrayList<int[]>();
		//クラスフィールドに格納したリストを念のため初期化
		winIns.reversePos.clear();
		;
		//「現在n手目」と表示される部分を更新。引数として与えられるフィールドは0で初期化したままなので、
		//「現在0手目」と表示される
		winIns.setTurn(winIns.count);
		//棋譜を表示する部分を更新する
		//引数として与える棋譜を表すインスタンスは、初期化したままなので、棋譜の欄は空欄として表示
		winIns.setRecord(winIns.record);
		//初期状態の盤面の石の数を元に、現在の双方の色の石の数を更新する
		winIns.setStoneNum(mapIns.checkStoneNum()[0],
				mapIns.checkStoneNum()[1]);

		//window系のクラスは初期状態では非表示状態なので、このメソッドで表示状態に変更
		//引数のtrueは表示状態を表す。なお、ここでfalseを引数として与えた場合、そのwindowは非表示状態になる
		winIns.setVisible(true);

	}

	/**
	 * 引数として与えられたmapを描画用に保持<BR>
	 * ここで受け取った盤面を現在の状況として描画する<BR>
	 * そのため、mapインスタンスをいくら更新しても、windowに描画される内容は変わらない<BR>
	 * なので、mapインスタンスを更新した際には、その盤面状況を元にこのメソッドを実行し、描画を行う盤面状況を更新すること<BR>
	 * 逆に言えば、mapインスタンス中の状況を問わず、ここでセットした盤面状況のみが表示されるということでも有る
	 *
	 * @param map
	 *            描画を行いたい盤面が格納された整数型2次元配列。この二次元配列について白黒空欄の描画が行われる
	 */
	public void setMap(int[][] map) {
		//オセロの盤面状況なので、引数のmapも各次元の要素数が8であると仮定し、
		//二重ループで引数の二次元配列の値をインスタンスフィールドの盤面を表す2次元配列にコピーしていく
		//単純に「this.map=map」と行わないのは、引数は参照型のため、そのようにした場合アドレスをコピーしてしまい、
		//引数として与えられた配列に対する操作に影響されてしまうのを避けるため
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				this.map[i][j] = map[i][j];
			}
		}
	}

	/**
	 * コンストラクタ
	 */
	public OthelloWin() {
		//初期化メソッドを実行する
		this.init();
	}

	/**
	 * 初期化用メソッド<BR>
	 * 盤面を表示するwindowや、情報を表示するwindowのサイズやタイトル、ボタンやラベルなどを設定したり貼り付けたりする
	 */
	private void init() {
		//windowのサイズ
		//ここで設定したサイズを引数に盤面を表示するwindowのサイズ指定を行う
		int width = 400, height = 400;

		//SamplePlayer中の棋譜を表すクラスフィールドのアドレスをこのインスタンスのフィールドに格納
		//以後、SamplePlayerのrecordとこのインスタンスのrecordは同じものを指しているとして操作が可能
		this.record = SamplePlayer.record;

		//setBoudsメソッドは、このwindowを描画する際のwindowの左上の座標と、そのwindowの縦横の幅を同時に指定するメソッド
		//第一第二引数で描画するwindowの左上の座標を指定し、第三第四引数でそのwindowの縦横のサイズを指定する
		//なお、第一第二引数中にある「Toolkit.getDefaultToolkit().getScreenSize()」とは、左上を基準にした、
		//モニタのサイズを持ったDimension型のインスタンスを返すメソッドである
		//ここで取得したモニタのサイズと、事前に定義したwindowのサイズを用いて、windowを画面中央に表示している
		this.setBounds((Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2, (Toolkit.getDefaultToolkit()
				.getScreenSize().height - height) / 2, width, height);
		//windowを一旦表示状態にする
		this.setVisible(true);
		//ダブルバッファリングに使うための非表示状態での描画領域を確保
		this.make();
		//このwindowに対するコンポーネント系のイベントを取得し、何かしらの処理を実行するためのメソッド
		//コンポーネント系のイベントとは、windowの移動やリサイズなどの操作を行われた時に発生する
		//今回はリサイズ時にダブルバッファリング用の領域を更新することにより、
		//windowのりサイズに合わせて表示領域も変更するために行う
		//「this.addComponentListener」で、このwindowに対するコンポーネント系のイベントを取得すると宣言し、
		//それらのイベント発生時にどのインスタンスのメソッドを実行するかを引数として与えるメソッドで定義している
		//なお、ここで引数として与えるメソッドには、ComponentListennerインターフェースを実装する必要があり、
		//オーバーライドしたインターフェース中のメソッドがイベントに応じて実行される
		//今回の場合、引数にthisを与えているため、このインスタンス中のオーバーライドしたメソッドが実行される
		this.addComponentListener(this);
		//上記のコンポーネントリスナーとほぼ同じ
		//違うのは、ComponentListennerをMouseListennerと読み替えることと、
		//盤面をクリックした時を検知するのが目的であるということの二点のみ
		this.addMouseListener(this);
		//windowの右上のバツボタンをおした時の動作を指定するメソッド
		//これを指定しないと、windowは消えてもプログラムは実行状態のままになったりするので注意
		//今回は、バツボタンを押した場合、プログラム自体が終了するように設定した
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//サブの情報パネル
		//各種の情報をまとめるためのパネルのインスタンスを生成し、保持する
		this.infoP = new Panel();
		//パネルのレイアウトを指定
		//レイアウトとは、パネルやwindowにラベルやボタンなどの各種のコンポーネントを貼り付けた際に、
		//どのようなデザインで貼り付けていくかの指定
		//今回はBorderLayoutを採用。これは、windowやパネルを上下左右と中央に分け、それぞれに貼り付けるレイアウト
		this.infoP.setLayout(new BorderLayout());
		//棋譜を表示するためのテキストエリアを生成し保持
		//テキストエリアとは、複数行を表示可能なテキストボックス
		this.ta = new TextArea();
		//プレイヤー名を表示するためのラベルを生成し保持
		this.myName = new Label();
		//現在までの経過時間を表示するためのラベルを生成して保持
		this.time = new Label();
		//現在の手番が開始してからの経過時間を表示するためのラベルを生成して保持
		this.turnTime = new Label();

		Game.COLOR color = SamplePlayer.color;
		if (color == null) {
			color = Game.COLOR.BLACK;
		}
		this.myColor = new Label("my color: " + color.toString());
		this.turn = new Label();
		this.stoneNum = new Label();

		Panel northPanel = new Panel();
		northPanel.setLayout(new GridLayout(0, 1));
		northPanel.add(myName);
		northPanel.add(myColor);
		northPanel.add(turn);
		northPanel.add(stoneNum);
		northPanel.add(time);
		northPanel.add(turnTime);

		this.back = new JButton("<<");
		this.back.addActionListener(this);
		this.forward = new JButton(">>");
		this.forward.addActionListener(this);
		this.now = new JButton("now");
		this.now.addActionListener(this);

		Panel southPanel = new Panel();
		southPanel.setLayout(new GridLayout(1, 0));
		southPanel.add(this.back);
		southPanel.add(this.now);
		southPanel.add(this.forward);

		this.infoP.add(ta, BorderLayout.CENTER);
		this.infoP.add(northPanel, BorderLayout.NORTH);
		this.infoP.add(southPanel, BorderLayout.SOUTH);

		//暫定表示用のwindow
		infoWin = new JFrame();
		infoWin.getContentPane().setLayout(new CardLayout());
		infoWin.getContentPane().add(infoP);

		//		infoWin.setSize(200, 400);
		infoWin.setBounds((Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2 + width, (Toolkit
				.getDefaultToolkit().getScreenSize().height - height) / 2, 250, height);

		infoWin.setTitle("情報ウィンドウ");
		infoWin.setVisible(true);

		subThread st = new subThread(this);
		st.start();

	}

	/**
	 * 色について表示する部分を更新する
	 * @param color 自分の色
	 */
	void setPlayerColor(Game.COLOR color) {
		String str = new String("");
		str += "my color : ";
		str += (color == Game.COLOR.BLACK) ? "黒" : "白";
		this.myColor.setText(str);
	}

	/**
	 * 現在の白と黒の石の数を更新するメソッド<BR>
	 * 引数の数でラベルを更新
	 * @param black　黒の石の数
	 * @param white　白の石の数
	 */
	void setStoneNum(int black, int white) {
		this.stoneNum.setText("黒  ： " + black + " VS 白 ： " + white);
	}

	/**
	 * 現在の手数を引数の数で更新する
	 * @param count　現在何手目か
	 */
	void setTurn(int count) {
		String str = "";
		if (count < 10) {
			str += " ";
		}
		str += String.valueOf(count);

		this.turn.setText("現在" + str + "手目");
	}

	/**
	 * 表示する名前を定義
	 * @param name 表示したい名前
	 */

	void setPlayerName(String name) {
		this.myName.setText("Player name : \n" + name);
	}

	/**
	 * 棋譜を設定しテキストエリアに反映する<BR>
	 * 配列中の要素は、先頭からx,y座標と色(黒：１、白：－１)
	 * @param record 棋譜に相当する打ち手のリスト
	 */
	void setRecord(ArrayList<int[]> record) {
		String str = new String("");
		for (int i = record.size() - 1; i >= 0; i--) {
			String turn = new String("");
			if (i + 1 < 10) {
				turn += " ";
			}
			turn += String.valueOf(i + 1);
			String t = (record.get(i)[2] == 1) ? "黒" : "白";
			str += turn + "手目 ： " + t + " : " + record.get(i)[0] + ", " + record.get(i)[1] + "\n";
		}

		ta.setText(str);
	}

	/**
	 * 勝敗の結果を引数として受け取り、それを視覚化してダイアログとして出力するメソッド
	 * @param result 自身の勝敗。勝ち：１、負け：-1、引き分け：０
	 */
	public void createResultWin(int result) {
		Dialog resultWin = new Dialog(this, true);

		resultWin.setLayout(new FlowLayout());

		String str = new String("");

		if (result == 1) {
			str += "勝利";
		} else if (result == -1) {
			str += "敗北";
		} else {
			str += "引き分け";
		}

		resultWin.add(new Label(str));

		resultWin.setVisible(true);

	}

	// ここからダブルバッファリング
	/**
	 * ダブルバッファリング用の領域を作成するメソッド
	 */
	public void make() {
		buf = createImage(this.getSize().width, this.getSize().height);
		bufG = buf.getGraphics();
	}

	// 再描画
	/**
	 * 再描画用メソッド
	 */
	public void update(Graphics g) {
		paint(g);
	}

	/**
	 * 実際に描画を行うメソッド
	 */
	public synchronized void paint(Graphics g) {
		this.make();

		if (buf == null && bufG == null) {
			this.make();
		}

		// windowの現在のサイズを取得
		Dimension size = getSize();

		// 上のタイトルバーと、それ以外の縁の部分のサイズを決定
		// このサイズ分だけ、windowの縁からスペースを取って盤面を描画する
		int upArea = 35;
		int otherArea = 15;

		// 画面サイズの内、狭い方に合わせて常時正方形を保つ

		// lengthはmap中の緑の盤面部分の長さ
		// length/8は1マス当たりの長さ
		// 縦と横の短い方を、盤面を意味する正方形の一辺とする
		int length = 0;
		if (size.height - upArea - otherArea < size.width - otherArea * 2) {
			length = size.height - upArea - otherArea;
		} else {
			length = size.width - otherArea * 2;
		}

		// 色を緑に変更し、盤面を意味する緑の正方形を描画
		bufG.setColor(Color.green);
		bufG.fillRect(otherArea, upArea, length, length);

		Game.COLOR color;
		if (this.black) {
			color = Game.COLOR.BLACK;
		} else {
			color = Game.COLOR.WHITE;
		}

		//現在の手番の色が打てる盤面上の座標を赤く塗ることで可視化するメソッドを実行
		this.ablePosVisualize(color);
		//各マス目に対し、評価値を表示するメソッド
		this.mapEvalValueVisualize();
		//あるマス目に置いた時に反転する石の座標を表示するメソッド
		this.reversePosVisualize();

		// 線を引く前に色を黒に変更
		bufG.setColor(Color.black);

		// 縦横にマス目の区切りの線を描画する
		// 正方形の一辺の長さlengthをマス目の数8で割った間隔ごとに線を引く
		for (int i = 0; i < 9; i++) {
			// 横線
			bufG.drawLine(otherArea, upArea + i * (length) / 8, otherArea
					+ length, upArea + i * (length) / 8);

			// 縦線
			bufG.drawLine(otherArea + i * (length) / 8, upArea, otherArea + i
					* (length) / 8, upArea + length);
		}

		// 盤面上の石を反映
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (map[i][j] == 1 || map[i][j] == -1) {
					// 石が置かれている時
					if (map[i][j] == 1) {// 1なら黒
						bufG.setColor(Color.black);
					} else if (map[i][j] == -1) {// -1なら白
						bufG.setColor(Color.white);
					} else {
						continue;
					}

					// 現在着目中のマス目の左上の座標を求める
					int posX = otherArea + i * (length) / 8;
					int posY = upArea + j * (length) / 8;
					// マス目の上下左右に開けるスペース
					int space = (int) ((double) length / 8.0 * (5.0 / 100.0));

					// マス目の左上の座標からスペース分だけ内側の部分から円を描画
					bufG.fillOval(posX + space, posY + space, length / 8
							- (space * 2), length / 8 - (space * 2));

				}
			}
		}

		// g.dispose();
		g.drawImage(buf, 0, 0, this);

	}

	// α-β法を再現するメソッド
	int[] getAlphaBeta(OthelloMap mapIns, Game.COLOR myColor) {

		// 現在の盤面で打てる手がない場合、パスを表す手を返す
		if (!mapIns.isAble(myColor)) {
			int[] pos = { -1, -1 };
			return pos;
		}

		// 現在の盤面で、自分が打てる手をリストに保持
		ArrayList<int[]> apos = mapIns.getAblePosList(myColor);
		// リストのサイズと同じサイズのint型配列を確保
		// score[i]にapos中のi番目の座標に打った時の自身の評価値を格納する
		int[] score = new int[apos.size()];

		// 自身の色と異なる色を次の色とする
		Game.COLOR nextColor;
		if (myColor == Game.COLOR.BLACK) {
			nextColor = Game.COLOR.WHITE;
		} else {
			nextColor = Game.COLOR.BLACK;
		}

		// 現在打てる手のうち、各座標に打った場合の数手先の評価値を求める
		for (int i = 0; i < apos.size(); i++) {

			// 現在のマップインスタンスのコピーを用意
			OthelloMap copyIns = mapIns.clone();
			// コピーしたインスタンスにaposのi番目の座標を打った状態にする
			copyIns.update(apos.get(i), myColor);
			// 現在の盤面の数手先の想定される評価値を取得し、score配列のi番目に格納する
			score[i] = getAlphaBetaRe(copyIns, myColor, nextColor, 4,
					Integer.MIN_VALUE, Integer.MAX_VALUE);

		}

		// 着手可能リストの何番目が良いかを指す値
		// 初期値はとりあえずリストの先頭を指しておく
		int n = 0;

		// リスト中のどれが最も評価値が高いかを調べる
		// nの初期値は0なので、初期の最大値は0を指している。そこで、1番から比較を行う
		for (int i = 1; i < score.length; i++) {
			// 現在の最大値n番と、i番目のうち、i番目の方が大きい場合、最大値を指すnをiで更新する
			if (score[n] < score[i]) {
				n = i;
			}

		}

		return apos.get(n);

	}

	// α-β法を実装するメソッドにおける再帰を担当する部分
	// 引数には、評価値を求めたい盤面状況のインスタンス、自分自身の色、現在の手番の色、あと何回再帰を行うか
	int getAlphaBetaRe(OthelloMap mapIns, Game.COLOR myColor,
			Game.COLOR nowColor, int count, int a, int b) {
		// 引数のcountが0の時、現在の盤面状況における自身の色の評価値を返す
		if (count == 0 || mapIns.isEnd())
			return mapIns.getEvalValue(mapIns.getMap(), myColor, this.count);

		// 自身の色と異なる色を次の手番の色とする
		Game.COLOR nextColor;
		if (nowColor == Game.COLOR.BLACK) {
			nextColor = Game.COLOR.WHITE;
		} else {
			nextColor = Game.COLOR.BLACK;
		}

		// 現在の盤面と色から着手可能な座標をリストとして受け取る
		ArrayList<int[]> apos = mapIns.getAblePosList(nowColor);

		if (apos.size() == 0) {
			return getAlphaBetaRe(mapIns.clone(), myColor, nextColor,
					count - 1, a, b);
		}

		// 打てる座標と同じ要素数のint型のscore配列を確保する
		int[] score = new int[apos.size()];

		// 着手可能な座標全てについて走査を行う
		for (int i = 0; i < apos.size(); i++) {
			// 現在の盤面のコピーインスタンスを生成する
			OthelloMap copyIns = mapIns.clone();
			// コピーインスタンスに対し、apos中のi番目の座標に石を打ったとして盤面を更新する
			copyIns.update(apos.get(i), nowColor);
			// 更新したコピーインスタンスを引数に、メソッドを再帰実行
			// その際の引数には、更新したコピーインスタンスと反転させたNextColor、count-1を入れる
			// myColorには引数そのままを入れる
			//ここで与えられるaとbは、
			//今が自分の手番ならばaには現在までの暫定最大評価値が含まれている
			//今が相手の手番ならばbには現在までの暫定最低評価値が含まれている
			score[i] = getAlphaBetaRe(copyIns, myColor, nextColor, count - 1,
					a, b);

			//今回の再帰が自分の手番かつ、今調査を行った評価値が、一つ前の再帰における暫定最低値を上回った場合に、
			//一つ前の再帰における暫定最低値を返す
			//これは、今の再帰では自分の手番なので、一つでも前の再帰における暫定最低値を上回った場合、必ず前の再帰の暫定最低値以上の値を返すことになる
			//その場合、必ず前の再帰ではこの値は使われず、暫定最低値以下の値が採用されるため、ここの評価値は前の再帰の暫定最低値より、
			//大きいということのみがわかれば良い
			if (score[i] > b && myColor == nowColor) {
				return b;
			}
			if (score[i] < a && myColor != nowColor) {
				return a;
			}

			//もし現在が自分の手番ならば、ここから想定される最大の評価値を返すので、
			//少なくとも現在の暫定最大値以上の値を返すことになるので、少なくともこの再帰はa以上の値を返すことになる
			//逆に、現在が相手の手番ならば、相手は想定される(こちらにとって)最低の評価値を返すので、
			//少なくとも暫定最低値以下の値を返すことになるので、少なくともこの再帰はb以下の値を返すことになる

			//現在が自分の手番かつ、今回の評価値が暫定最大値を上回ったら
			if (score[i] > a && myColor == nowColor) {
				a = score[i];
			}
			//現在が相手の手番かつ、今回の評価値が暫定最低値を下回ったら
			if (score[i] < b && myColor != nowColor) {
				b = score[i];
			}

		}

		// myColorとnowColorに応じてcの値を1か-1にする
		int c = 0;
		if (myColor == nowColor) {
			// myColorとnowColorが同じならばc=1
			c = 1;
		} else {
			// myColorとnowColorが異なればc=-1
			c = -1;
		}

		// score配列中の最小・最大の評価値を保持している番号を表す値
		// 初期値は先頭の要素として0を格納
		int n = 0;

		// score配列中の1ばんから最後の要素までを比較
		// 初期状態では配列の0番目を暫定最小・最大の要素としているので、次の要素である1からそれ以降を比較している
		for (int i = 1; i < score.length; i++) {
			// score配列のn番目よりi番目が大きい、もしくは小さければ、nをiに更新
			// myColorとnowColorが同色ならば、cは1なので、nには最大値を指す番号が保持され、
			// myColorとnowColorが異色ならば、cは-1なので、nには最小値を指す番号が保持される

			if (score[n] * c < score[i] * c) {
				n = i;
			}
		}

		// 直前で求めた最小、もしくは最大の評価値を現在の盤面から想定される評価値として返す
		return score[n];

	}

	/**
	 * 画面のリサイズ時に実行されるメソッド<BR>
	 * ダブルバッファリング用の変数を開放し、リサイズ後の画面サイズに合わせて再設定<BR>
	 * 最後にそれらをつかって再描画を行う
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		buf.flush();
		bufG.dispose();
		this.make();
		this.repaint();

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * window上でマウスボタンを離した場合に実行されるメソッド<BR>
	 * このクラスから始めた場合、これをつかって遊べる
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

		if (e.getButton() == MouseEvent.BUTTON1) {
			// マウスボタンが離された時の座標
			Point p = e.getPoint();
			int posX = p.x;
			int posY = p.y;

			// windowのサイズ
			Dimension size = getSize();

			// 上のタイトルバーと、それ以外の縁の部分
			int upArea = 35;
			int otherArea = 15;

			// lengthはmap中の緑の部分の長さ
			// length/8は1マス当たりの長さ
			int length = 0;
			if (size.height - upArea - otherArea < size.width - otherArea * 2) {
				length = size.height - upArea - otherArea;
			} else {
				length = size.width - otherArea * 2;
			}

			int i, l;

			// ボタンが離された時のx,y座標が何行何列に位置するかを探す
			// i番目とi+1番目のラインの間にいるならそのx座標はi番であるとする。y座標も同様
			for (i = 0; i < 8; i++) {
				if (otherArea + (i) * (length / 8) < posX
						&& posX <= otherArea + (i + 1) * (length / 8)) {
					break;
				}
				if (i >= 7) {
					i = -1;
					break;
				}
			}

			for (l = 0; l < 8; l++) {
				if (upArea + (l) * (length / 8) < posY
						&& posY <= upArea + (l + 1) * (length / 8)) {
					break;
				}
				if (l >= 7) {
					l = -1;
					break;
				}
			}

			//算出した座標をposに格納
			int[] pos = { i, l };

			Game.COLOR myColor;

			//現在のblackフラグから色を求める
			int c = 0;
			if (this.black) {
				myColor = Game.COLOR.BLACK;
				c = 1;
			} else {
				myColor = Game.COLOR.WHITE;
				c = -1;
			}

			//座標と色から、打てるかを確認。打てない場合終了し、再度の入力を待つ
			if (!OthelloWin.mapIns.isAblePos(pos, myColor)) {
				return;
			}

			//打てる場合、盤面状況を更新し、表示系も更新し新しい盤面を再描画
			OthelloWin.mapIns.update(pos, myColor);
			count++;
			this.setTurn(count);
			int[] re = { pos[0], pos[1], c };
			this.record.add(re);
			this.setRecord(record);
			this.setStoneNum(mapIns.checkStoneNum()[0],
					mapIns.checkStoneNum()[1]);

			OthelloWin.winIns.setMap(OthelloWin.mapIns.getMap());
			OthelloWin.winIns.repaint();

			//終了状態の場合、その旨を出力し、出力ダイアログを閉じるとともにシステム終了
			if (OthelloWin.mapIns.isEnd()) {
				int result = mapIns.checkResult();
				if (result == 1) {
					JOptionPane.showMessageDialog(winIns, "黒の勝利");
				} else if (result == -1) {
					JOptionPane.showMessageDialog(winIns, "白の勝利");
				} else if (result == 0) {
					JOptionPane.showMessageDialog(winIns, "引き分け");
				}

				System.exit(0);
			}

			//次回用に色フラグを反転
			this.black = !this.black;

			//確認用に、色フラグから色を変数に格納
			Game.COLOR eneColor;
			if (this.black) {
				eneColor = Game.COLOR.BLACK;
				c = 1;
			} else {
				eneColor = Game.COLOR.WHITE;
				c = -1;
			}

			//次の手番で打てる手が無いとき
			if (!OthelloWin.mapIns.isAble(eneColor)) {

				if (myColor == Game.COLOR.BLACK) {
					JOptionPane.showMessageDialog(winIns, "黒はパスです");
				} else {
					JOptionPane.showMessageDialog(winIns, "白はパスです");
				}

				//色のフラグを反転
				this.black = !this.black;
			} else {//else以下は白の自動打ちなので、コメントアウトすれば、黒白自分打ちになる

				//			//random打ち
				//			//posが打てる座標になるまでループ
				//			Random r = new Random();
				//			pos[0] = r.nextInt(8);
				//			pos[1] = r.nextInt(8);
				//			while (!OthelloWin.mapIns.isAblePos(pos, color)) {
				//				pos[0] = r.nextInt(8);
				//				pos[1] = r.nextInt(8);
				//			}
				//			int[] reEne = { pos[0], pos[1], c };

				do {
					int[] enePos = this.getAlphaBeta(mapIns, eneColor);
					int[] reEne = { enePos[0], enePos[1], c };
					count++;
					this.record.add(reEne);
					this.setRecord(record);
					this.setStoneNum(mapIns.checkStoneNum()[0],
							mapIns.checkStoneNum()[1]);
					//posの座標に石を打ったと盤面状況を更新し、再描画
					//その後、色フラグを反転し終了
					OthelloWin.mapIns.update(enePos, eneColor);
					this.setTurn(count);
					this.setMap(OthelloWin.mapIns.getMap());
					this.black = !this.black;
					this.repaint();

					//終了状態の場合、その旨を出力し、出力ダイアログを閉じるとともにシステム終了
					if (OthelloWin.mapIns.isEnd()) {
						int result = mapIns.checkResult();
						if (result == 1) {

							JOptionPane.showMessageDialog(winIns, "黒の勝利");
						} else if (result == -1) {
							JOptionPane.showMessageDialog(winIns, "白の勝利");
						} else if (result == 0) {
							JOptionPane.showMessageDialog(winIns, "引き分け");
						}

						System.exit(0);
					}

					if (!OthelloWin.mapIns.isAble(myColor)) {

						if (myColor == Game.COLOR.BLACK) {
							JOptionPane.showMessageDialog(winIns, "黒はパスです");
						} else {
							JOptionPane.showMessageDialog(winIns, "白はパスです");
						}
						continue;
					}
				} while (!OthelloWin.mapIns.isAble(myColor));
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3) {
			System.out.println("test");

			// マウスボタンが離された時の座標
			Point p = e.getPoint();
			this.reverseCheckPos[0] = p.x;
			this.reverseCheckPos[1] = p.y;

			OthelloWin.winIns.repaint();
		}
	}

	public void stepRepaint(int step) {
		OthelloMap mapIns = new OthelloMap();
		Game.COLOR color;

		for (int i = 0; i < record.size() + step; i++) {
			int[] pos = { record.get(i)[0], record.get(i)[1] };

			if (record.get(i)[2] == 1) {
				color = Game.COLOR.BLACK;
			} else {
				color = Game.COLOR.WHITE;
			}

			mapIns.update(pos, color);
		}

		this.setMap(mapIns.getMap());
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ

		if (e.getSource() == back) {
			step--;
			if (step * -1 > record.size()) {
				step = -record.size();
			}
			this.stepRepaint(step);
		} else if (e.getSource() == forward) {
			step++;
			if (step >= 0) {
				step = 0;
			}
			this.stepRepaint(step);
		} else if (e.getSource() == now) {
			step = 0;
			this.stepRepaint(step);
		}

	}

	/**
	 * 引数として与えられた色が、現在の盤面状況でどの座標に打てるかということを可視化するメソッド<BR>
	 * 描画する盤面上のマス目の内、引数の色が打てるマス目を赤く染めることで可視化する
	 * @param color どちらの色にとって打てる座標を可視化するかを指定する
	 */
	public void ablePosVisualize(Game.COLOR color) {
		//		int c=0;
		//		if(color==Game.COLOR.BLACK){
		//			c=1;
		//		}else{
		//			c=-1;
		//		}

		Dimension size = this.getSize();

		OthelloMap mapIns = new OthelloMap();
		mapIns.setMap(this.map);

		// 上のタイトルバーと、それ以外の縁の部分のサイズを決定
		// このサイズ分だけ、windowの縁からスペースを取って盤面を描画する
		int upArea = 35;
		int otherArea = 15;

		// 画面サイズの内、狭い方に合わせて常時正方形を保つ

		// lengthはmap中の緑の盤面部分の長さ
		// length/8は1マス当たりの長さ
		// 縦と横の短い方を、盤面を意味する正方形の一辺とする
		int length = 0;
		if (size.height - upArea - otherArea < size.width - otherArea * 2) {
			length = size.height - upArea - otherArea;
		} else {
			length = size.width - otherArea * 2;
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				int[] pos = { i, j };
				if (mapIns.isAblePos(pos, color)) {
					this.bufG.setColor(Color.RED);
					this.bufG.fillRect(otherArea + (i * length) / 8, upArea + (j * length) / 8,
							(((i + 1) * length) / 8) - ((i * length) / 8), (((j + 1) * length) / 8)
									- ((j * length) / 8));
				}
			}
		}
		//		this.repaint();

	}

	/**
	 * 各マス目毎の評価値を盤面上に描画するメソッド
	 */
	public void mapEvalValueVisualize() {
		//		int c=0;
		//		if(color==Game.COLOR.BLACK){
		//			c=1;
		//		}else{
		//			c=-1;
		//		}

		Dimension size = this.getSize();

		OthelloMap mapIns = new OthelloMap();
		mapIns.setMap(this.map);

		// 上のタイトルバーと、それ以外の縁の部分のサイズを決定
		// このサイズ分だけ、windowの縁からスペースを取って盤面を描画する
		int upArea = 35;
		int otherArea = 15;

		// 画面サイズの内、狭い方に合わせて常時正方形を保つ

		// lengthはmap中の緑の盤面部分の長さ
		// length/8は1マス当たりの長さ
		// 縦と横の短い方を、盤面を意味する正方形の一辺とする
		int length = 0;
		if (size.height - upArea - otherArea < size.width - otherArea * 2) {
			length = size.height - upArea - otherArea;
		} else {
			length = size.width - otherArea * 2;
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				this.bufG.setColor(Color.BLACK);
				String str = Integer.toString(OthelloWin.mapIns.EvalValue[i][j]);
				this.bufG.drawString(str, otherArea + (i * length) / 8, upArea + ((j + 1) * length) / 8);

			}
		}
		//		this.repaint();

	}

	/**
	 * あるマス目に石を置いた時に反転するマス目を可視化するメソッド
	 */
	public void reversePosVisualize() {
		if(OthelloWin.winIns.reversePos==null){
			return;
		}

		OthelloMap om = new OthelloMap();
		om.setMap(this.map);
		//現在のblackフラグから色を求める
		Game.COLOR color;
		if (this.black) {
			color = Game.COLOR.BLACK;
		} else {
			color = Game.COLOR.WHITE;
		}

		om.checkReverse(this.reverseCheckPos, color, false);


		Dimension size = this.getSize();

		// 上のタイトルバーと、それ以外の縁の部分のサイズを決定
		// このサイズ分だけ、windowの縁からスペースを取って盤面を描画する
		int upArea = 35;
		int otherArea = 15;

		// 画面サイズの内、狭い方に合わせて常時正方形を保つ

		// lengthはmap中の緑の盤面部分の長さ
		// length/8は1マス当たりの長さ
		// 縦と横の短い方を、盤面を意味する正方形の一辺とする
		int length = 0;
		if (size.height - upArea - otherArea < size.width - otherArea * 2) {
			length = size.height - upArea - otherArea;
		} else {
			length = size.width - otherArea * 2;
		}

		this.bufG.setColor(Color.BLUE);
		for (int i = 0; i < OthelloWin.winIns.reversePos.size(); i++) {
			int[] pos = OthelloWin.winIns.reversePos.get(i);
			this.bufG.fillRect(otherArea + (pos[0] * length) / 8,
					upArea + (pos[1] * length) / 8,
					(((pos[0] + 1) * length) / 8) - ((pos[0] * length) / 8),
					(((pos[1] + 1) * length) / 8) - ((pos[1] * length) / 8));

		}
		//		this.repaint();

	}

}

class subThread extends Thread {

	OthelloWin owin;
	long turnStartTime = System.currentTimeMillis();

	public subThread() {

	}

	public subThread(OthelloWin owin) {
		this.owin = owin;
	}

	public void run() {

		long startTime = System.currentTimeMillis();

		long oldTime = 0;

		while (true) {
			if (oldTime != (System.currentTimeMillis() - startTime) / 1000) {
				oldTime = (System.currentTimeMillis() - startTime) / 1000;
				owin.time.setText("現在までの経過時間 : " + (oldTime - oldTime % 60) / 60 + ":" + oldTime % 60);
			}

		}

	}

	public long getTurnStartTime() {
		return System.currentTimeMillis() / 1000;
	}

	public void setTurnStartTime() {
		this.turnStartTime = System.currentTimeMillis() / 1000;
	}
}