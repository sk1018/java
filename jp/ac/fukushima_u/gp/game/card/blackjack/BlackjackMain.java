package jp.ac.fukushima_u.gp.game.card.blackjack;

import java.util.ArrayList;

import jp.ac.fukushima_u.gp.game.card.Card;

public class BlackjackMain {
	private Card card;
	private BJWindowImage bjw;
	private ArrayList<Integer> uc, dc;
	
	/*
	 * 改造案
	 * hitとstandの時にwaitを入れて微妙に時間を遅らせる事
	 * 結果表示の時にユーザとディーラー両方の数を表示させるとか
	 * 単純な勝敗だけでなく、ユーザやディーラーのバースト時にはバーストと表示させる
	 * ゲームの成績を表示させる(基本は勝負数と勝敗。後はバースト数とか平均ヒット数とかその辺の数値的なもの)
	 * 上記のものを出力する際に、メインwindowにスペースを作るか、サブウィンドウを作るか（見やすさの関係で、メインに付け足しかな）
	 * 
	 * 1つのクラスに1つの責務、という原則から、ディーラーとユーザのクラスを作るのも有りかも
	 * もしくはどちらもプレイヤーであるという点から共通のプレイヤークラスを作成するとか
	 * プレイヤーのインターフェースかアブストラクトを作り，それを実装・継承したディーラーとユーザを作成とか
	 */
	
	/**
	 * ブラックジャックを行う際のmainとなる関数
	 * 基本ここで処理を行いつつ、windowクラスを通してユーザとやりとりをする
	 *
	 * mainメソッドの内容から鑑みるに、おそらく外部からインスタンス化するだけで実行可能
	 * コンストラクタをprivateからpublicにする必要はあるけど
	 * @param args mainメソッド用の不要な引数
	 */
	public static void main(String[] args) {
		BlackjackMain bjm = new BlackjackMain();
	}
	
	private void setIns(BlackjackMain bjm) {
		bjw.setIns(bjm);
	}
	
	private BlackjackMain() {
		//カードの山と基本windowをセット
		card = new Card();
		card.setJokerNum(0);
		card.initCard();
		bjw = new BJWindowImage();
		this.setIns(this);
		
		//手札用のリストの確保
		uc = new ArrayList<Integer>();
		dc = new ArrayList<Integer>();
		
		//最初のカードのドロー
		this.initCardDraw();
		
		//windowの可視化
		bjw.setVisible(true);
		
	}
	
	private void initCardDraw() {
		int t;
		t = card.drawCard();
		dc.add(t);
		bjw.setDealerCard(t);
		t = card.drawCard();
		uc.add(t);
		bjw.setUserCard(t);
		t = card.drawCard();
		uc.add(t);
		bjw.setUserCard(t);
		bjw.sumRenew(sumCheck(uc), sumCheck(dc));
	}
	
	void hitAction() {
		int t = card.drawCard();
		uc.add(t);
		bjw.setUserCard(t);
		bjw.sumRenew(sumCheck(uc), sumCheck(dc));
		bjw.setVisible(true);
		if (this.sumCheck(uc) > 21) {
			bjw.subWin("YOU LOSE!");
		}
	}
	
	public synchronized void sleep(long msec)
	{ //指定ミリ秒実行を止めるメソッド
		try
		{
			wait(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void standAction() {
		while (this.sumCheck(dc) < 18) {
			int t;
			t = card.drawCard();
			dc.add(t);
			bjw.setDealerCard(t);
			bjw.sumRenew(sumCheck(uc), sumCheck(dc));
			//bjw.setVisible(true);
			//ここらへんでwaitを入れて、ゆっくりめくるのも有りかも
			
			this.sleep(1000);
		}
		
		if (this.sumCheck(dc) > 21 || this.sumCheck(dc) < this.sumCheck(uc)) {
			bjw.subWin("YOU WIN!");
		}
		else if (this.sumCheck(dc) >= this.sumCheck(uc)) {
			bjw.subWin("YOU LOSE!");
		}
	}
	
	/**
	 * 与えられた手札用のリストの合計値を返す
	 * @param l 判定を行いたい手札用のリスト
	 * @return 合計値を返す
	 */
	private int sumCheck(ArrayList<Integer> l) {
		int sum = 0;
		int f = 0;
		for (int i = 0; i < l.size(); i++) {
			sum += l.get(i) % 13 + 1;
			
			//絵札が出た時の処理
			if (l.get(i) % 13 + 1 > 10) {
				sum -= l.get(i) % 13 + 1 - 10;
			}
			
			//Aが出た時の処理
			if (l.get(i) % 13 == 0) {
				f++;
				sum += 10;
			}
			while (sum > 21 && f > 0) {
				sum -= 10;
				f--;
			}
		}
		return sum;
	}
	
}
