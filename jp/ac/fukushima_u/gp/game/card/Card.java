package jp.ac.fukushima_u.gp.game.card;

import java.util.Random;

/**
 * トランプの山札を管理するためのクラス<BR>
 * トランプの基本52枚+ジョーカー(0～2)の内、各カードが山札にあるか否かを管理するクラス
 * @author seiya
 *
 */
public class Card {

	/**
	 * 各カードが山札にあるかどうかを表す配列<BR>
	 * 13で割った時の商がスート、余り+1がカードの数字を表す。<BR>
	 * 53,54はジョーカーを表す
	 */
	private boolean[] card = new boolean[54];

	/**
	 * 山札に含まれるジョーカーの枚数を表す
	 */
	private int jokerNum;

	public static void main(String[] args) {

	}

	/**
	 * コンストラクタ<BR>
	 * 標準ではジョーカー2枚を含む54枚のフルセットとして考える
	 */
	public Card() {
		this.initCard();
		this.setJokerNum(2);
	}

	/**
	 * 初期化メソッド<BR>
	 * 54枚すべてが山札に存在する、というように初期化する
	 */
	public void initCard() {
		for (int i = 0; i < 54; i++) {
			card[i] = false;
		}
		this.setJokerNum(jokerNum);
	}

	/**
	 * ジョーカーの枚数を設定するメソッド<BR>
	 * 引数として与えられた数をジョーカーの枚数として保持する
	 * @param n ジョーカーの枚数
	 */
	public void setJokerNum(int n) {
		if (n < 0 || n > 2) {
			return;
		}
		this.jokerNum = n;
		for (int i = 52 + this.jokerNum; i < 54; i++) {
			card[i] = true;
		}
	}

	/**
	 * 山札から一枚カードを引くメソッド<BR>
	 * 現在の山札に含まれるカードの内、どれか一枚をランダムに引いたとして、引いたカードを山札から取り除き、
	 * そのカードの番号を返す
	 * @return 山札から引いたカード
	 */
	public int drawCard() {
		Random rand = new Random();
		int n;
		do {
			n = rand.nextInt(52 + this.jokerNum);
		} while (card[n]);
		card[n] = true;
		return n;
	}

	public int[] drawCard(int num) {
		Random rand = new Random();
		int n;
		int[] tmp = new int[num];

		for (int i = 0; i < num; i++) {
			do {
				n = rand.nextInt(52 + this.jokerNum);
			} while (card[n]);
			card[n] = true;
		}
		return tmp;
	}

	public int drawCard(String in) {
		Random rand = new Random();
		int n;
		boolean[] suit = new boolean[5];

		for (int i = 0; i < 5; i++) {
			suit[i] = false;
		}
		for (int l = 0; l < in.length(); l++) {
			switch (in.charAt(l)) {
				case 's':
					suit[0] = true;
					break;
				case 'h':
					suit[1] = true;
					break;
				case 'd':
					suit[2] = true;
					break;
				case 'c':
					suit[3] = true;
					break;
				case 'j':
					suit[4] = true;
					break;
				default:
					break;
			}
		}
		boolean f = true;
		for (int i = 0; i < 5; i++) {
			if (suit[i])
				f = false;
		}
		if (f)
			return -1;

		do {
			n = rand.nextInt(52 + this.jokerNum);
			if (!suit[n % 13])
				continue;
		} while (card[n]);
		card[n] = true;
		return n;
	}

	public int[] drawCard(String in, int num) {
		Random rand = new Random();
		int n;
		int[] tmp = new int[num];
		boolean[] suit = new boolean[5];

		for (int i = 0; i < 5; i++) {
			suit[i] = false;
		}
		for (int l = 0; l < in.length(); l++) {
			switch (in.charAt(l)) {
				case 's':
					suit[0] = true;
					break;
				case 'h':
					suit[1] = true;
					break;
				case 'd':
					suit[2] = true;
					break;
				case 'c':
					suit[3] = true;
					break;
				case 'j':
					suit[4] = true;
					break;
				default:
					break;
			}
		}
		boolean f = true;
		for (int i = 0; i < 5; i++) {
			if (suit[i])
				f = false;
		}
		if (f) {
			tmp[0] = -1;
			return tmp;
		}

		for (int i = 0; i < num; i++) {
			do {
				n = rand.nextInt(52 + this.jokerNum);
				if (!suit[n % 13])
					continue;
			} while (card[n]);
			card[n] = true;
			tmp[i] = n;
		}
		return tmp;
	}

	public int drawCardSelect(int n) {
		card[n] = true;
		return n;
	}

	public void backCardSelect(int n) {
		card[n] = false;
	}

	public int getRestCardNum() {
		int num = 0;
		for (int i = 0; i < 52 + this.jokerNum; i++) {
			if (!card[i])
				num++;
		}
		return num;
	}

	public int[] getRestCardNum(String in) {
		int[] num = new int[5];
		for (int i = 0; i < 5; i++) {
			num[i] = 0;
		}

		for (int i = 0; i < 52 + this.jokerNum; i++) {
			if (!card[i]) {
				num[i % 5]++;
			}
		}

		return num;
	}

	public static char getSuit(int num) {
		int n = num / 13;
		switch (n) {
			case 0:
				return 's';
			case 1:
				return 'h';
			case 2:
				return 'd';
			case 3:
				return 'c';
			default:
				return 'j';
		}

	}

}
