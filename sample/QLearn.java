package sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import othello.Game;

/**
 * オセロにおけるQ-学習を行うクラス<BR>
 * 
 *
 * @author GP
 *
 */
public class QLearn {

	OthelloMap mapIns;
	private int count;
	private BufferedWriter bw = null;
	static String[] outStr = new String[64 - 4];

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		// String配列を「""」で初期化
		for (int i = 0; i < outStr.length; i++) {
			outStr[i] = new String("");
		}

		QLearn ql = new QLearn();
		// ql.autoBattleStarter();
		// try {
		// ql.research();
		// } catch (IOException e) {
		// // TODO 自動生成された catch ブロック
		// e.printStackTrace();
		// }

		subThread st = new subThread();
		st.start();

	}

	public void research() throws IOException {

		bw = new BufferedWriter(new FileWriter(new File(
				"C:\\hoge\\date\\record.txt")));

		this.getRecordRecursive(new OthelloMap(), Game.COLOR.BLACK,
				new ArrayList<int[]>());

		bw.close();
	}

	public void researchRecord() {
		OthelloMap mapIns = new OthelloMap();
	}

	/**
	 * 現在の盤面状況を表すマップのインスタンスと、現在の打ち手の色を受け取り、そこから想定される白と黒の勝利パターン数を評価値として返す<BR>
	 * 現在の盤面状況から、着手可能な手を打った時それを基準に再帰実行し、最終的に勝敗がわかるまで繰り返すメソッド<BR>
	 * このメソッド単品で見た場合、引数の盤面が終了状態ならば勝敗に応じた評価値を返し、そうでなければ、着手可能座標をリスト化し、
	 * 各座標に打った場合ごとの評価値を求め<BR>
	 * その総和をこの盤面における評価値として返している
	 * 
	 * @param mapIns
	 *            　現在の盤面状況を表すインスタンス
	 * @param color
	 *            　現在は白と黒のどちらの色を示す値
	 * @return　引数として与えられた盤面状況の、黒と白の評価値を格納した要素数２の配列<BR>
	 *         配列の0番には黒の評価値が、1番には白の評価値が格納されている
	 */
	public int[] getScoreRecursive(OthelloMap mapIns, Game.COLOR color) {
		int[] score = { 0, 0 };

		// 現在の盤面が終了状態か否か
		if (mapIns.isEnd()) {
			// ゲーム終了ならば、勝敗に応じたscoreを返す
			// 黒が勝ちなら1,0、白が勝ちなら0,1、引き分けなら0,0を返す
			if (mapIns.checkResult() == 1) {
				int[] s = { 1, 0 };
				return s;
			} else if (mapIns.checkResult() == -1) {
				int[] s = { 0, -1 };
				return s;
			} else {
				int[] s = { 0, 0 };
				return s;
			}
		}

		// 次の手番の色を保持する変数
		Game.COLOR nextColor;
		// int c = 0;
		// 引数として与えられた現在の手番の色が黒ならば、次の手番の色は白として保持し、
		// 現在の手番の色が黒でないならば、次の手番の色は黒として保持する
		if (color == Game.COLOR.BLACK) {
			nextColor = Game.COLOR.WHITE;
			// c = -1;
		} else {
			nextColor = Game.COLOR.BLACK;
			// c = 1;
		}

		// 終了状態ではない場合、現在の盤面から着手可能な座標をリストとして保持
		ArrayList<int[]> apos = mapIns.getAblePosList(color);

		// 現在の盤面に現在の色のプレイヤーが打てる座標がなかった場合、パスをしたとして、現在のマップのコピーと、反転させた色をいれて再帰実行
		// 着手可能な座標があった場合、各座標に打った場合の評価値の合計を求める
		if (apos.size() == 0) {
			// パスした場合、盤面に変化はないので、単純に盤面のコピーインスタンスを引数として渡す
			// 色を表す引数は、事前に定義済みの次の色を与える
			int[] t = this.getScoreRecursive(mapIns.clone(), nextColor);
			score[0] += t[0];
			score[1] += t[1];
		} else {
			// 着手可能な場合、着手可能座標のリストの各座標ごとに評価値を算出
			for (int i = 0; i < apos.size(); i++) {
				// 現在のマップインスタンスのコピーを生成
				OthelloMap nextMapIns = mapIns.clone();
				// コピーインスタンスに着手可能座標のi番目を打ったとしてアップデート
				nextMapIns.update(apos.get(i), color);
				// 更新したコピーインスタンスと次の手の色を引数として再帰実行し、
				// 更新した盤面の評価値を求める
				int[] t = this.getScoreRecursive(nextMapIns, nextColor);
				// 取得したi番目の手を打った時の評価値を現在の評価値に加算する
				score[0] += t[0];
				score[1] += t[1];

			}

		}

		// 現在の盤面状況とその時の評価値を「,」で区切る形式にして、String配列の[石の数-4]番目に連結する
		// 評価値を求めた現在の盤面状況をインデックス形式に変換する
		int[] index = this.getIndex(mapIns.getMap());
		// 新しいString型変数を「""」で初期化し、その後ろにindexの0から7と、黒・白の評価値の順で「,」区切りで連結
		// なお、これでひとつのデータだと示すために、最後に改行を入れる
		String str = new String("");
		for (int i = 0; i < 8; i++) {
			str += String.valueOf(index[i]) + ",";
		}
		str += String.valueOf(score[0]) + "," + String.valueOf(score[1]) + "\n";

		// 現在の状況をStringに変換後、このStringをクラスフィールド中のString配列の[石の数-4]番目のStringに連結する
		QLearn.outStr[mapIns.checkStoneNum()[0] + mapIns.checkStoneNum()[1] - 4] += str;

		// 最終的なscoreの値を、現在の盤面状況における評価値として返す
		return score;

	}

	// 自動試合をさせるためのメソッド
	public void autoBattleStarter() {
		int num = 100;
		int b = 0, w = 0, n = 0;
		for (int i = 0; i < 100; i++) {
			int r = this.autoBattle();

			if (r == 1) {
				System.out.println("黒が勝ち");
				b++;
			} else if (r == -1) {
				System.out.println("白が勝ち");
				w++;
			} else {
				System.out.println("引き分け");
				n++;
			}
		}

		System.out.println("試合数:" + num);
		System.out.println("黒：白：引き分け");
		System.out.println(b + " : " + w + " : " + n);

	}

	// 自動で試合をするメソッド
	// 戻り値として、黒が勝てば1、白が勝てば-1、引き分けなら0を返す
	public int autoBattle() {

		count = 0;

		OthelloMap mapIns = new OthelloMap();

		// Game.COLOR myColor = Game.COLOR.BLACK;
		// Game.COLOR enemyColor = Game.COLOR.WHITE;

		Game.COLOR nowColor = Game.COLOR.BLACK;

		// ランダムを使うかどうか
		// ランダムに打たせる色を入れる
		// Game.COLOR randomColor = Game.COLOR.BLACK;
		Game.COLOR randomColor = Game.COLOR.WHITE;
		// Game.COLOR randomColor=null;

		while (!mapIns.isEnd()) {
			int[] pos;

			if (randomColor == nowColor) {
				pos = this.getRandomPos(mapIns, nowColor);
			} else {
				pos = this.getAlphaBeta(mapIns, nowColor);
			}
			if (!mapIns.isOut(pos[0], pos[1])) {
				count++;
				mapIns.update(pos, nowColor);
			}

			if (nowColor == Game.COLOR.BLACK) {
				nowColor = Game.COLOR.WHITE;
			} else {
				nowColor = Game.COLOR.BLACK;
			}
		}

		return mapIns.checkResult();
	}

	// ランダムな打ち場所を返す
	public int[] getRandomPos(OthelloMap mapIns, Game.COLOR nowColor) {
		Random random = new Random();

		int[] myPos = { random.nextInt(8), random.nextInt(8) };

		if (!mapIns.isAble(nowColor)) {
			myPos[0] = -1;
			myPos[1] = -1;
			return myPos;
		}

		// while (!mapIns.isAblePos(myPos, nowColor)) {
		// myPos[0] = random.nextInt(8);
		// myPos[1] = random.nextInt(8);
		// }

		myPos = mapIns.getAblePosList(nowColor).get(
				random.nextInt(mapIns.getAblePosList(nowColor).size()));

		return myPos;

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
			score[i] = getAlphaBetaRe(copyIns, myColor, nextColor, 3,
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
			// ここで与えられるaとbは、
			// 今が自分の手番ならばaには現在までの暫定最大評価値が含まれている
			// 今が相手の手番ならばbには現在までの暫定最低評価値が含まれている
			score[i] = getAlphaBetaRe(copyIns, myColor, nextColor, count - 1,
					a, b);

			// 今回の再帰が自分の手番かつ、今調査を行った評価値が、一つ前の再帰における暫定最低値を上回った場合に、
			// 一つ前の再帰における暫定最低値を返す
			// これは、今の再帰では自分の手番なので、一つでも前の再帰における暫定最低値を上回った場合、必ず前の再帰の暫定最低値以上の値を返すことになる
			// その場合、必ず前の再帰ではこの値は使われず、暫定最低値以下の値が採用されるため、ここの評価値は前の再帰の暫定最低値より、
			// 大きいということのみがわかれば良い
			if (score[i] > b && myColor == nowColor) {
				return b;
			}
			if (score[i] < a && myColor != nowColor) {
				return a;
			}

			// もし現在が自分の手番ならば、ここから想定される最大の評価値を返すので、
			// 少なくとも現在の暫定最大値以上の値を返すことになるので、少なくともこの再帰はa以上の値を返すことになる
			// 逆に、現在が相手の手番ならば、相手は想定される(こちらにとって)最低の評価値を返すので、
			// 少なくとも暫定最低値以下の値を返すことになるので、少なくともこの再帰はb以下の値を返すことになる

			// 現在が自分の手番かつ、今回の評価値が暫定最大値を上回ったら
			if (score[i] > a && myColor == nowColor) {
				a = score[i];
			}
			// 現在が相手の手番かつ、今回の評価値が暫定最低値を下回ったら
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

	public void outputStringToFile() {

		for (int i = 0; i < QLearn.outStr.length; i++) {
			String fileFullPass = new String("");
			fileFullPass += "C:\\hoge\\date\\map" + i + ".txt";

			// ここからは入力情報を元にした実際の保存処理
			// ファイルを開いたり、それに書き込んだりするのは例外の発生が起こりうるので、try-catchブロックでくくる
			try {
				// 新規インスタンスを引数に新規インスタンスを生成し、更にそれを引数に…と繰り返してるからわかりにくいが、
				// ユーザが入力した情報を連結して絶対パスに変換し、絶対パスを元にファイルのインスタンスを作成する
				// 作成したファイルのインスタンスを元にファイルの書き込みを行うファイルライタークラスのインスタンスを生成
				// ファイルライタークラスでも書き込みはできるが、このままでは効率が悪いため、ファイルライタークラスの
				// ラッパークラスであるバッファードライタークラスでラッピングしている

				// 結論だけ言うと、ユーザが入力した絶対パスのファイルをターゲットとして
				// 書き込みを行うBufferedWriterクラスのインスタンス用意した
				PrintWriter pw = new PrintWriter(new BufferedWriter(
						new FileWriter(new File(fileFullPass))));

				// テキストエリアに書かれているテキストを取得し、それを書き込む
				// なお、これはファイルがなければ生成しそこに書き込みを行い、ファイルが有る場合には上書きを行う
				pw.write(QLearn.outStr[i]);
				// 確認用
				// System.out.print(QLearn.outStr[i]);
				// プログラムの外部のファイル等への接続やコネクションは、使ったら閉じる
				// これを怠るとファイルが破損したり、ずっと使用中になってしまったりする
				// 昔ほど厳密に行わなくても何とかなるが、あくまで忘れた時の保険とし、基本は手動で閉じる
				pw.close();
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック

				// ここにはエラーメッセージとして、ダイアログを出して
				// 「エラー発生。保存されませんでした」と表示を行いたい。
				// その際、可能ならばエラーコードとかも表示できるとわかりやすい

				e1.printStackTrace();
			}
		}
	}

	/**
	 * 現在の盤面状況を表すマップのインスタンスと、現在の打ち手の色を受け取り、そこから想定される白と黒の勝利パターン数を評価値として返す<BR>
	 * 現在の盤面状況から、着手可能な手を打った時それを基準に再帰実行し、最終的に勝敗がわかるまで繰り返すメソッド<BR>
	 * このメソッド単品で見た場合、引数の盤面が終了状態ならば勝敗に応じた評価値を返し、そうでなければ、着手可能座標をリスト化し、
	 * 各座標に打った場合ごとの評価値を求め<BR>
	 * その総和をこの盤面における評価値として返している
	 * 
	 * @param mapIns
	 *            　現在の盤面状況を表すインスタンス
	 * @param color
	 *            　現在は白と黒のどちらの色を示す値
	 * @return　引数として与えられた盤面状況の、黒と白の評価値を格納した要素数２の配列<BR>
	 *         配列の0番には黒の評価値が、1番には白の評価値が格納されている
	 */
	public void getRecordRecursive(OthelloMap mapIns, Game.COLOR color,
			ArrayList<int[]> record) {
		int[] score = { 0, 0 };

		// 現在の盤面が終了状態か否か
		if (mapIns.isEnd()) {
			// ゲーム終了ならば、勝敗に応じたscoreを返す
			// 黒が勝ちなら1,0、白が勝ちなら0,1、引き分けなら0,0を返す
			try {
				for (int i = 0; i < record.size(); i++) {

					bw.write(String.valueOf(record.get(i)[0] + record.get(i)[1]
							* 8)
							+ ",");

				}
				bw.write(String.valueOf(mapIns.checkResult()) + "\n");

			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		// 現在までの棋譜をコピー

		// 次の手番の色を保持する変数
		Game.COLOR nextColor;
		int c = 0;
		// 引数として与えられた現在の手番の色が黒ならば、次の手番の色は白として保持し、
		// 現在の手番の色が黒でないならば、次の手番の色は黒として保持する
		if (color == Game.COLOR.BLACK) {
			nextColor = Game.COLOR.WHITE;
			c = -1;
		} else {
			nextColor = Game.COLOR.BLACK;
			c = 1;
		}

		// 終了状態ではない場合、現在の盤面から着手可能な座標をリストとして保持
		ArrayList<int[]> apos = mapIns.getAblePosList(color);

		// 現在の盤面に現在の色のプレイヤーが打てる座標がなかった場合、パスをしたとして、現在のマップのコピーと、反転させた色をいれて再帰実行
		// 着手可能な座標があった場合、各座標に打った場合の評価値の合計を求める
		if (apos.size() == 0) {
			// パスした場合、盤面に変化はないので、単純に盤面のコピーインスタンスを引数として渡す
			// 色を表す引数は、事前に定義済みの次の色を与える
			this.getRecordRecursive(mapIns.clone(), nextColor, record);
		} else {
			// 着手可能な場合、着手可能座標のリストの各座標ごとに評価値を算出
			for (int i = 0; i < apos.size(); i++) {
				// 現在のマップインスタンスのコピーを生成
				OthelloMap nextMapIns = mapIns.clone();
				// コピーインスタンスに着手可能座標のi番目を打ったとしてアップデート
				nextMapIns.update(apos.get(i), color);
				int[] pos = { apos.get(i)[0], apos.get(i)[1], c };
				record.add(pos);
				// 更新したコピーインスタンスと次の手の色を引数として再帰実行し、
				// 更新した盤面の評価値を求める
				this.getRecordRecursive(nextMapIns, nextColor, record);
				record.remove(record.size() - 1);

			}

		}

		// 現在の盤面状況とその時の評価値を「,」で区切る形式にして、String配列の[石の数-4]番目に連結する
		// 評価値を求めた現在の盤面状況をインデックス形式に変換する
		int[] index = this.getIndex(mapIns.getMap());
		// 新しいString型変数を「""」で初期化し、その後ろにindexの0から7と、黒・白の評価値の順で「,」区切りで連結
		// なお、これでひとつのデータだと示すために、最後に改行を入れる
		String str = new String("");
		for (int i = 0; i < 8; i++) {
			str += String.valueOf(index[i]) + ",";
		}
		str += String.valueOf(score[0]) + "," + String.valueOf(score[1]) + "\n";

		// 現在の状況をStringに変換後、このStringをクラスフィールド中のString配列の[石の数-4]番目のStringに連結する
		QLearn.outStr[mapIns.checkStoneNum()[0] + mapIns.checkStoneNum()[1] - 4] += str;

		// 最終的なscoreの値を、現在の盤面状況における評価値として返す
		return;

	}

	/**
	 * コンストラクタ。初期化メソッドの呼び出しのみ
	 */
	public QLearn() {
		this.init();
	}

	/**
	 * 初期化メソッド
	 */
	void init() {
		mapIns = new OthelloMap();
	}

	/**
	 * 引数として与えられたmap[8][8]を、index[8]として変換して返すメソッド<BR>
	 * map[i]番目の各値に1を加算し、0-2の範囲にした上で8桁の三進数とみなし、それを10進数に変換したものをindex[i]に格納し、返す
	 * 
	 * @param map
	 *            オセロの現在の盤面状況
	 * @return 引数の盤面状況を二次元配列から1次元配列に変換したもの
	 */
	int[] getIndex(int[][] map) {

		int[] index = new int[8];

		for (int i = 0; i < index.length; i++) {
			index[i] = 0;
		}

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				index[i] += (map[i][j] + 1) * Math.pow(3, j);
			}
		}

		return index;
	}

	/**
	 * 引数として与えられたindexの一次元配列を、mapという二次元配列に変換して返すメソッド<BR>
	 * もともとは-1～1の範囲を、1加算して0～2の範囲にしているので、その分途中で１減算している
	 * 
	 * @param index
	 *            変換を行いたいindex配列
	 * @return index配列から復元したmapの２次元配列
	 */
	int[][] getMapFromIndex(int[] index) {
		int[][] map = new int[8][8];

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				map[i][j] += index[i] % 3 - 1;
				index[i] = (index[i] - index[i] % 3) / 3;
			}
		}

		return map;
	}

}

