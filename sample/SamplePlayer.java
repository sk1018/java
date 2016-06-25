package sample;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import othello.Game;
import othello.Player;
import othello.Server;

/**
 * オセロプレイヤのサンプル
 *
 * ランダムに手を選択して打っていく． デフォルトでは試合主催者となり対戦相手を待つ． 石の色指定オプションを選択すると非主催者として参加する．この時，
 * 試合リストの中から最初の試合が選択される．
 *
 * @author isp
 */
public class SamplePlayer implements Player {

	/**
	 * Player自身の名前を表すString
	 */
	private String name;
	/**
	 * Random打ちをする際に、乱数を生成するインスタンスを保持するフィールド
	 */
	private Random random;

	/* デフォルトのサーバ接続情報 */
	/**
	 * オセロサーバのIPアドレスを保持するフィールド。このIPにサーバが有るとしてアクセスを行う<BR>
	 * プログラム実行時に「-h IP」というオプションで上書き可能(IP部分は任意のIPアドレス)
	 */
	private static String host = "192.168.1.27";
	/**
	 * オセロサーバにアクセスするポート番号。ポート番号が同じ主催者と参加者をマッチングする<BR>
	 * プログラム実行時に「-p PORT」というオプションで上書き可能(PORT部分は任意のポート番号)
	 */
	private static int port = 8002;

	/**
	 * プレイヤーの名前を保持するフィールド。これをプレイヤーの名前として表示する<BR>
	 * プログラム実行時「-n 名前」で上書き可能。名前は任意の文字列
	 */
	private static String playerName = "sample player";
	/**
	 * 自身の色を表すフィールド。<BR>
	 * プログラム実行時に「-c b」で上書き可能。bの部分は、bならば黒、それ以外なら白が与えられる<BR>
	 * このオプショがなかった場合、ゲームを主催者として開催し、Optionがあった場合、開催中のゲームに参加者として参加する
	 */
	static Game.COLOR color = null;

	// インスタンス保持用クラスフィールド
	/**
	 * マップクラスのインスタンスを保持するためのクラスフィールド<BR>
	 * どこからでもアクセスを可能にするためにクラスフィールドにしている
	 */
	static OthelloMap mapIns;
	/**
	 * windowクラスのインスタンスを保持するためのクラスフィールド<BR>
	 * どこからでもアクセスを可能にするためにクラスフィールドにしている
	 */
	static OthelloWin winIns;
	/**
	 * 現在何手目かを表す整数値のフィールド<BR>
	 * 初期値はまだ打っていないので0<BR>
	 * 自分が打って１増加、相手が打って１増加する。ただし、これを使って表示を更新するメソッドを実行しなければ表示は変わらないため、<BR>
	 * これの値が変わったら、随時表示を更新すること
	 */
	static int count = 0;
	/**
	 * 棋譜を保持するためのリスト。これに座標情報とともに色を表す値を入れて、棋譜として保持している<BR>
	 * なお、このままではこのフィールドにはリスト型インスタンスが入っておらず使えないため、最初にnewでインスタンスを格納する必要がある<BR>
	 * 格納する情報は、要素数が３のint型配列で、各要素は｛x座標、y座標、色｝が入っていることを想定している
	 */
	static ArrayList<int[]> record;

	/**
	 * プレイヤオブジェクトを生成する
	 *
	 * @param name
	 *            プレイヤの名前
	 */
	public SamplePlayer(String name) {
		this.random = new Random();
		this.name = name;
	}

	/**
	 * プレイヤの名前を返す
	 *
	 * @return プレイヤの名前
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 試合開始直後に呼ばれる
	 *
	 * @param myColor
	 *            割り当てられた石の色
	 */
	public void start(Game.COLOR myColor) {
		SamplePlayer.color = myColor;

		System.err.println(name + " started with Color: " + color.toString());

		SamplePlayer.mapIns = new OthelloMap();
		SamplePlayer.winIns = new OthelloWin();

		record = new ArrayList<int[]>();
		record.clear();

		winIns.record = record;
		winIns.setPlayerName(playerName);
		winIns.setPlayerColor(myColor);
		winIns.setTitle("othello:" + SamplePlayer.color.toString());
		winIns.setMap(mapIns.getMap());
		winIns.setVisible(true);
	}

	/**
	 * 次の一手を返す
	 *
	 * @param enemyPos
	 *            敵が置いた位置
	 * @return 次の一手の位置
	 */
	public int[] next(int[] enemyPos) {

		long sleepTime = 0;

		// sleep
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		int c = 0;
		// 相手の色をcolorに格納
		Game.COLOR color;
		if (SamplePlayer.color == Game.COLOR.BLACK) {
			color = Game.COLOR.WHITE;
			c = -1;
		} else {
			color = Game.COLOR.BLACK;
			c = 1;
		}

		// 正常な手として打てた時
		if (enemyPos != null && mapIns.isAblePos(enemyPos, color)) {
			SamplePlayer.mapIns.update(enemyPos, color);
			SamplePlayer.winIns.setMap(SamplePlayer.mapIns.getMap());

			// 情報の保持とその更新系
			count++;
			winIns.setTurn(count);
			int[] r = { enemyPos[0], enemyPos[1], c };
			record.add(r);
			winIns.setRecord(record);
			winIns.setStoneNum(mapIns.checkStoneNum()[0],
					mapIns.checkStoneNum()[1]);

			SamplePlayer.winIns.repaint();
		}

		// sleep
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		// ここで打ち手をmyPosに格納している
		int[] myPos = this.getAlphaBeta(SamplePlayer.mapIns, SamplePlayer.color);

		// 格納された今回の打ち手がパスならば、下の処理は行わず、ここで処理を行って返す
		if (myPos[0] == -1 && myPos[1] == -1) {
			count++;
			winIns.setTurn(count);
			int[] r = { myPos[0], myPos[1], c * -1 };
			record.add(r);
			winIns.setRecord(record);
			return myPos;
		}

		SamplePlayer.mapIns.update(myPos, SamplePlayer.color);
		SamplePlayer.winIns.setMap(SamplePlayer.mapIns.getMap());

		// 情報の保持とその更新系
		count++;
		winIns.setTurn(count);
		int[] r = { myPos[0], myPos[1], c * -1 };
		record.add(r);
		winIns.setRecord(record);
		winIns.setStoneNum(mapIns.checkStoneNum()[0], mapIns.checkStoneNum()[1]);

		// 再描画
		SamplePlayer.winIns.repaint();

		return myPos;
	}

	/**
	 * エントリポイント
	 *
	 * @param args
	 *            コマンドライン引数
	 */
	public static void main(String[] args) {

		Options opts = new Options();
		opts.addOption("h", "host", true, "接続先ホストアドレス");
		opts.addOption("p", "port", true, "接続先ポート番号");
		opts.addOption("c", "color", true, "石の色を選択する(black or white)");
		opts.addOption("n", "name", true, "プレイヤ名");

		CommandLineParser parser = new BasicParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(opts, args);
			/* オプション解析結果の取得 */
			if (cmd.hasOption("h")) {
				host = cmd.getOptionValue("h");
			}
			if (cmd.hasOption("p")) {
				port = Integer.valueOf(cmd.getOptionValue("p"));
			}
			if (cmd.hasOption("c")) {
				String colorName = cmd.getOptionValue("c");
				if (colorName.toLowerCase().startsWith("b")) {
					color = Game.COLOR.BLACK;
				} else {
					// bで始まらなければwhiteとみなす
					color = Game.COLOR.WHITE;
				}
			}
			if (cmd.hasOption("n")) {
				playerName = cmd.getOptionValue("n");
			}
		} catch (ParseException e) {
			HelpFormatter help = new HelpFormatter();
			help.printHelp("SamplePlayer", opts, true);
			return;
		}

		Server server = new Server();
		SamplePlayer player = new SamplePlayer(playerName);

		/* まずサーバ接続しにいく */
		if (!server.connect(host, port)) {
			System.out.println("オセロサーバに接続できません");
			return;
		}
		/* 次にログインする */
		if (!server.login(player)) {
			System.out.println("オセロサーバにログインできません");
			return;
		}

		Game game = null;
		/* 主催者モード */
		if (color == null) {
			/* 試合を開催する */
			if ((game = server.makeGame()) == null) {
				System.out.println("試合開催に失敗しました");
				return;
			}
			System.out.println("試合を開催しました");
			System.out.println("対戦相手を待っています");
			/* 対戦相手を待つ */
			if ((color = game.waitJoin()) == null) {
				System.out.println("対戦相手待ちに失敗しました");
				return;
			}
		}
		/* 非主催者モード */
		else {
			/* 試合リストを取得 */
			Game[] games = server.getGames();
			if (games == null) {
				System.out.println("試合リストの取得に失敗しました");
				return;
			}
			if (games.length == 0) {
				System.out.println("試合が開催されていません");
				return;
			}
			game = games[0];
			if (!game.join(color)) {
				System.out.println("試合に参加できませんでした");
				return;
			}
		}

		/* 試合を開始する */
		System.out.println("試合を開始します");
		if (!game.start()) {
			System.out.println("試合実行に失敗しました");
			return;
		}

		SamplePlayer.endProcess();

		System.out.println("試合が終了しました");
	}

	/**
	 * ゲーム終了後、プログラムが終了するまでの処理
	 */
	public static void endProcess() {

		mapIns.finalHand(SamplePlayer.color);
		SamplePlayer.winIns.setMap(SamplePlayer.mapIns.getMap());
		winIns.repaint();

		int result = mapIns.checkResult();
		if (result == 1) {

			JOptionPane.showMessageDialog(winIns, "黒の勝利");
		} else if (result == -1) {
			JOptionPane.showMessageDialog(winIns, "白の勝利");
		} else if (result == 0) {
			JOptionPane.showMessageDialog(winIns, "引き分け");
		}
	}

	// 最善手を求めるメソッド
	public int[] getBestPos(int[][] map, Game.COLOR color) {
		int[] pos = { -1, -1 };

		return pos;

	}

	// ランダムな打ち場所を返す
	public int[] getRandomPos() {
		int[] myPos = { random.nextInt(8), random.nextInt(8) };

		if (!mapIns.isAble(SamplePlayer.color)) {
			myPos[0] = -1;
			myPos[1] = -1;
			System.out.println(SamplePlayer.color.toString() + " is pass");
			return myPos;
		}

		while (!SamplePlayer.mapIns.isAblePos(myPos, SamplePlayer.color)) {
			myPos[0] = random.nextInt(8);
			myPos[1] = random.nextInt(8);
		}

		return myPos;

	}

	// min-max法を用いて最善手を求めるアルゴリズム
	int[] getMinMax(OthelloMap mapIns, Game.COLOR myColor) {

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
			score[i] = getMinMaxRe(copyIns, myColor, nextColor, 3);

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

	// min-max法を再現するメソッドにおける再帰を担当する部分
	// 引数には、評価値を求めたい盤面状況のインスタンス、自分自身の色、現在の手番の色、あと何回再帰を行うか
	int getMinMaxRe(OthelloMap mapIns, Game.COLOR myColor, Game.COLOR nowColor,
			int count) {
		// 引数のcountが0の時、現在の盤面状況における自身の色の評価値を返す
		if (count == 0 || mapIns.isEnd())
			return mapIns.getEvalValue(mapIns.getMap(), myColor,SamplePlayer.count);

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
			return getMinMaxRe(mapIns.clone(), myColor, nextColor, count - 1);
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
			score[i] = getMinMaxRe(copyIns, myColor, nextColor, count - 1);

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
			score[i] = getAlphaBetaRe(copyIns, myColor, nextColor, 5,
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
			return mapIns.getEvalValue(mapIns.getMap(), myColor,SamplePlayer.count);

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
			if(score[i]>b && myColor == nowColor){
				return b;
			}
			if(score[i]<a && myColor != nowColor){
				return a;
			}

			//もし現在が自分の手番ならば、ここから想定される最大の評価値を返すので、
			//少なくとも現在の暫定最大値以上の値を返すことになるので、少なくともこの再帰はa以上の値を返すことになる
			//逆に、現在が相手の手番ならば、相手は想定される(こちらにとって)最低の評価値を返すので、
			//少なくとも暫定最低値以下の値を返すことになるので、少なくともこの再帰はb以下の値を返すことになる
			
			//現在が自分の手番かつ、今回の評価値が暫定最大値を上回ったら
			if(score[i]>a && myColor == nowColor){
				a=score[i];
			}
			//現在が相手の手番かつ、今回の評価値が暫定最低値を下回ったら
			if(score[i]<b && myColor != nowColor){
				b=score[i];
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

}
