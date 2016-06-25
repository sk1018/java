package sample;

import java.util.ArrayList;

import othello.Game;

/**
 * オセロにおける現在の盤面状況を保持し、それに対する操作を行うクラス<BR>
 * 盤面状況の保持だけではなく、ある座標に石を打った時の更新や、現在の白と黒の石の数の取得なども行う
 * @author seiya
 *
 */
public class OthelloMap implements Cloneable {

	/**
	 * オセロの盤面に相当する2次元配列<br>
	 * オセロの盤面なので、一次元二次元共に要素数が8として宣言している<BR>
	 * 1次元が左からの横列、2次元が上からの縦列を指す 各座標の値は、0:空,1;黒,-1:白
	 */
	private int[][] map = new int[8][8];

	/**
	 * 盤面中の各マス目の評価値の重み付け<BR>
	 * 自身の石があるマスの評価値の総和が、その盤面における自身の評価値になる
	 */
	int[][] EvalValue = { { 30, -12, 0, -1, -1, 0, -12, 30 },
			{ -12, -15, -3, -3, -3, -3, -15, -12 },
			{ 0, -3, 0, -1, -1, 0, -3, 0 }, { -1, -3, -1, -1, -1, -1, -3, -1 },
			{ -1, -3, -1, -1, -1, -1, -3, -1 }, { 0, -3, 0, -1, -1, 0, -3, 0 },
			{ -12, -15, -3, -3, -3, -3, -15, -12 },
			{ 30, -12, 0, -1, -1, 0, -12, 30 } };

	/**
	 * 盤面状況を表すフィールドの初期化
	 */
	private void init() {
		//mapはオセロの盤面なので、各要素数が8
		//そのため、i,jともに0～8までの二重ループで初期化を行う
		//基本的に、空欄を示す0で初期化を行うが、中央の四マスに限り、初期配置の白と黒を表す値を格納する
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if ((i == 4 && j == 3) || (i == 3 && j == 4)) {
					map[i][j] = 1;
				} else if ((i == 4 && j == 4) || (i == 3 && j == 3)) {
					map[i][j] = -1;
				} else {
					map[i][j] = 0;
				}

			}
		}
	}

	/**
	 * ある座標へ石を打った時、反転できるものがあるかを返すメソッド<BR>
	 * 引数次第では実際に反転処理も行う
	 *
	 * @param pos
	 *            石を打つ座標
	 * @param color
	 *            打つ石の色
	 * @param doReverse
	 *            実際に反転させるか否か。trueなら反転させて更新
	 * @return 反転可能ならtrue、反転できなければfalse
	 */
	public boolean checkReverse(int[] pos, Game.COLOR color, boolean doReverse) {
		//次の処理はOthelloWinから起動時の、「人 VS CPU戦」にのみ使用
		//これから確認していくので、確認前にリストを初期化
		if(OthelloWin.winIns.reversePos!=null){
			OthelloWin.winIns.reversePos.clear();
		}

		//確認を行う座標が定義に沿った要素数が2の配列ではない場合、それは打てないとしてfalseを返す
		if (pos.length != 2) {
			return false;
		}

		//引数として与えられたcolorをint型に変換
		//colorが黒だった場合は1，違った場合-1を代入する
		int c = 0;
		if (color == Game.COLOR.BLACK) {
			c = 1;
		} else {
			c = -1;
		}

		// 各方向に1マス動いた時の変化量
		//dir[i][j]の、iは上下左右斜めの方向を表し、jはx座標y座標を表す
		//入力された座標pos[0]にdir[i][0]*nを加算すると、posを基準にiの方向にnマスずらしたx座標が取得できる
		//同様にpos[1]にdir[i][1]*nを加算すると、posを基準にiの方向にnマスずらしたy座標が取得できる
		//これを用いて、入力座標からi方向にnマスずれたマス目の座標を求めていく
		int dir[][] = { { -1, -1 }, { 0, -1 }, { 1, -1 }, { -1, 0 }, { 1, 0 },
				{ -1, 1 }, { 0, 1 }, { 1, 1 } };

		//反転したかどうかのフラグ
		//初期状態ではまだ反転していないのでfalse
		//後ほど反転する場所があった場合、これをtrueにする
		boolean reversed = false;

		//入力座標を基準に上下左右斜めの各方向について確認していく
		for (int i = 0; i < 8; i++) {
			// 隣のマスの座標
			//入力座標からi方向に1マスずれたマス目の座標を格納
			int x0 = pos[0] + dir[i][0];
			int y0 = pos[1] + dir[i][1];
			// map外をさしてたら次へ
			//1マスずれた座標がmap外を指していた場合、この方向には反転できる石がないとして次の方向へ
			if (isOut(x0, y0) == true) {
				continue;
			}
			// 隣のマスの状況（黒か白か空欄か）
			int nextState = map[x0][y0];
			// 同色か、空欄ならば、反転できる石がないとして、次の方向へcontinue
			//(異色ならば飯店の可能性有りとして次の処理へ)
			if (nextState == c) {
				continue;
			} else if (nextState == 0) {
				continue;
			}

			// 隣の隣から端まで順に走査して、その途中で自分の色があればリバースして次の方向へ
			//一マス隣は確認済みなので、二マス隣から確認する
			//二マス隣を表すのでj=2から実行
			int j = 2;
			while (true) {
				//入力座標からi方向にjマスずれたマス目の座標を格納
				int x1 = pos[0] + (dir[i][0] * j);
				int y1 = pos[1] + (dir[i][1] * j);
				// map外まで来たらbreak
				if (isOut(x1, y1) == true) {
					break;
				}

				// 自分の駒があったら、その間のマスをリバース
				if (map[x1][y1] == c) {

					// 反転フラグがtrueなら反転処理
					if (doReverse) {
						//入力座標の1つ隣のマスから、同色のマスが出てくるj番目の手前までを反転
						for (int k = 1; k < j; k++) {
							//反転する座標を設定
							int x2 = pos[0] + (dir[i][0] * k);
							int y2 = pos[1] + (dir[i][1] * k);
							//白と黒は-1と1なので、-1をかけることで反転可能
							map[x2][y2] *= -1;
						}
					}

					//次の処理はOthelloWinから起動時の、「人 VS CPU戦」にのみ使用
					//最初に格納するリストがnullかどうかを確認し、nullでなければ「人 VS CPU戦」であるとして処理を続行
					//入力座標の1つ隣のマスから、同色のマスが出てくるj番目の手前までを反転するOthelloWinの座標リストに格納
					if(OthelloWin.winIns.reversePos!=null){
					for (int k = 1; k < j; k++) {
						//反転する座標を設定
						int x2 = pos[0] + (dir[i][0] * k);
						int y2 = pos[1] + (dir[i][1] * k);
						//この座標が反転するとして、反転可能リストに格納
						int[] rPos={x2,y2};
						OthelloWin.winIns.reversePos.add(rPos);
					}
					}

					// 反転出来たので、あるいは反転できるマスがあるので、反転済みフラグはtrue
					reversed = true;
					//i方向の反転処理が終わったので反転用無限ループから脱出
					break;
				}

				// 空白があったら、終了
				if (map[x1][y1] == 0) {
					break;
				}

				//jを加算し、次のマスの確認を行う
				j++;

			}

		}

		// 反転可能箇所があればtrue、無ければfalseを返す
		return reversed;
	}

	/**
	 * 引数として与えられた色のプレイヤーが、現在の盤面状況で打つことが可能な座標をリストとして返す
	 * @param color　どちらの色について調べるか
	 * @return　着手可能な座標(要素数2のint型配列)のリストを返す
	 */
	public ArrayList<int[]> getAblePosList(Game.COLOR color) {
		//着手可能な座標を返すためのリスト型インスタンスを生成し、一応要素数が0になるように初期化
		ArrayList<int[]> apos = new ArrayList<int[]>();
		apos.clear();

		//盤面の左上から全マス目に対し、各マス目に引数の色が打てるかどうかを確認するメソッドを実行
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				//現在の座標情報をposに格納
				int[] pos = { i, j };
				//posに格納した座標に、colorの色の石が打てるかどうか
				if (this.isAblePos(pos, color)) {//その座標に石がうてた時
					//着手可能点の座標posをリストに追加
					apos.add(pos);
				}
			}
		}

		//着手可能点をまとめたリストを返す
		return apos;
	}

	@Override
	/**
	 * 自身のインスタンスのコピーを返すメソッド<BR>
	 * mapフィールドもコピー済み
	 *
	 */
	public OthelloMap clone() {
		//OthelloMap型の新しいインスタンスを生成
		OthelloMap t = new OthelloMap();
		//新しく生成したMap型インスタンス
		t.setMap(this.map);
		//新しく生成し、内部のフィールドもコピーしたものを返す
		return t;

	}

	/**
	 * 入力座標が盤面外を指していないかを確認するメソッド
	 *
	 * @param x
	 *            確認したいx座標
	 * @param y
	 *            確認したいy座標
	 * @return 入力座標が盤面の内か外かtrue:外、false:内
	 */
	public boolean isOut(int x, int y) {
		//入力値の
		if (x < 0 || y < 0 || x >= 8 || y >= 8) {
			return true;
		}
		return false;
	}

	/**
	 * 入力された座標に入力された色の石を打ったとし、その石の追加と反転処理を実行するメソッド<BR>
	 * 実際のゲームでの、石を打つことと、それに合わせた反転作業を表現
	 *
	 * @param pos
	 *            石を打つ座標
	 * @param color
	 *            打つ石の色
	 */
	public synchronized void update(int[] pos, Game.COLOR color) {

		// 石を打つ場所が開いていない、もしくはmap外ならば何もせず終了
		if (map[pos[0]][pos[1]] != 0 || isOut(pos[0], pos[1])) {
			return;
		}

		// 石の色を整数値に変換
		int c = 0;

		if (color == Game.COLOR.BLACK) {
			c = 1;
		} else {
			c = -1;
		}

		// map上の一点をピンポイントで更新
		this.setMapPos(pos, c);

		// 反転可能場所を反転
		this.checkReverse(pos, color, true);
	}

	/**
	 * 現在の盤面状況に対し、引数の色の石が打てる場所があるか
	 *
	 * @param color
	 *            打とうとしている石の色
	 * @return 打てるかどうか。true:打てる、false:打てない
	 */
	public boolean isAble(Game.COLOR color) {
		// 初期値はfalse
		boolean check = false;
		// map上のすべてのマス目に対し、そこに入力の色の石が打てるかを確認
		for (int i = 0; i < 8; i++) {
			for (int l = 0; l < 8; l++) {
				int[] pos = { i, l };
				// 打てる場所があった時true
				check = isAblePos(pos, color);
				if (check) {
					// 一箇所でも打てる場所があるならtrueを返す
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 引数として与えられたmap中のposの場所に、colorで与えられた色の石を置けるかを返す<BR>
	 * 戻り値がtrueなら置ける、falseなら置けない
	 *
	 * @param pos
	 *            着目中の座標
	 * @param color
	 *            確認する色
	 * @return 置けるか否か(true:置ける, false:置けない)
	 */
	public boolean isAblePos(int[] pos, Game.COLOR color) {

		// 座標がmap外だった場合には置けない
		if (isOut(pos[0], pos[1])) {
			return false;
		}

		// すでに置かれている場合には置けない
		if (map[pos[0]][pos[1]] != 0) {
			return false;
		}

		// 反転するものがあればtrue、なければfalseを返す
		return this.checkReverse(pos, color, false);

	}

	public int getEvalValue(int[][] map, Game.COLOR color, int count) {
		// 石の色を整数値に変換
		int c = 0;
		if (color == Game.COLOR.BLACK) {
			c = 1;
		} else {
			c = -1;
		}
		// 評価値の総和を表す
		int sum = 0;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (map[i][j] == c) {
					if (SamplePlayer.count < 37) {//38,7あたりが適切
						sum += EvalValue[i][j];
					}
					//					else if(SamplePlayer.count < 55){
					//						sum += EvalValue2[i][j];
					//					}
					else {
						int n;
						if (c == 1) {
							n = 0;
						} else {
							n = 1;
						}

						sum = this.checkStoneNum()[n];
					}
				}
			}
		}

		return sum;

	}

	/**
	 * 現在の盤面に打たれているそれぞれの石の色を返すメソッド
	 *
	 * @return 配列の0番に黒の数、1番に白の数を入れた整数型の配列を返す
	 */
	public int[] checkStoneNum() {
		int[] stone = { 0, 0 };

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (map[i][j] == 1) {
					stone[0] += 1;
				} else if (map[i][j] == -1) {
					stone[1] += 1;
				}
			}
		}
		return stone;
	}

	/**
	 * 現在の盤面から、ゲームが終わりかどうかを判断するメソッド<BR>
	 * 石の数がマス目と同じ数(64個)か、白と黒の両方の色で置けない場合、ゲーム終了と判断する
	 *
	 * @return ゲームが終了か否かを返す。true:ゲーム終了、false:ゲーム継続可能
	 */
	public boolean isEnd() {
		int[] snum = this.checkStoneNum();
		if (snum[0] + snum[1] >= 64) {
			return true;
		}

		if (!this.isAble(Game.COLOR.BLACK) && !this.isAble(Game.COLOR.WHITE)) {
			return true;
		}

		return false;
	}

	/**
	 * サーバからゲーム終了が帰ってきた時の処理<BR>
	 * 最後の手まで打ち切っていた、もしくは双方ともに打てなければそのまま終了<BR>
	 * まだ打てる状況だった場合、相手の手で終わった場合なので、開いてる場所に相手の色の石を打つ<BR>
	 * 相手の色では打てる手がない場合には自身の色の石を打つ
	 *
	 * @param color
	 *            自分の色
	 */
	public void finalHand(Game.COLOR color) {
		if (this.isEnd()) {
			return;
		}

		Game.COLOR enemyColor;
		int c = 0;
		if (color == Game.COLOR.BLACK) {
			enemyColor = Game.COLOR.WHITE;
			c = -1;
		} else {
			enemyColor = Game.COLOR.BLACK;
			c = 1;
		}

		int i, j;
		for (i = 0; i < 8; i++) {
			for (j = 0; j < 8; j++) {
				if (map[i][j] == 0) {
					int[] pos = { i, j };
					if (this.isAblePos(pos, enemyColor)) {
						this.update(pos, enemyColor);
						int[] r = { pos[0], pos[1], c };
						SamplePlayer.record.add(r);
						SamplePlayer.winIns.setRecord(SamplePlayer.record);
					} else {
						this.update(pos, color);
						int[] r = { pos[0], pos[1], c * -1 };
						SamplePlayer.record.add(r);
						SamplePlayer.winIns.setRecord(SamplePlayer.record);
					}
					SamplePlayer.winIns.setStoneNum(this.checkStoneNum()[0],
							this.checkStoneNum()[1]);
					SamplePlayer.count++;
					SamplePlayer.winIns.setTurn(SamplePlayer.count);
					return;
				}
			}
		}

	}

	/**
	 * 現在の盤面状況から、白と黒のどちらが勝ちかを判定するメソッド
	 *
	 * @return 勝利したのがどちらかを返す。黒が勝ち：１、白が勝ち：-1、引き分け：0
	 */
	public int checkResult() {
		int[] stone = this.checkStoneNum();

		if (stone[0] > stone[1]) {
			return 1;
		} else if (stone[0] < stone[1]) {
			return -1;
		} else {
			return 0;
		}
	}

	public OthelloMap() {
		this.init();
	}

	public int[][] getMap() {
		return this.map;
	}

	public void setMap(int[][] map) {
		// this.map = map;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				this.map[i][j] = map[i][j];
			}
		}
	}

	public void setMapPos(int[] pos, int c) {
		if (pos.length != 2)
			return;
		this.map[pos[0]][pos[1]] = c;
	}

	public int getMapPos(int[] pos) {
		return this.map[pos[0]][pos[1]];
	}

}
