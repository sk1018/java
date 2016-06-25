package jp.ac.fukushima_u.gp.nampre.solve;

/**
 * 9*9の標準型のナンプレ解読用のクラス。
 * <br>外部からナンプレの初期値をint[][]で入力し、その回答をint[][]として返す
 * <br>外部からはsolveだけを呼び出し、他のメソッドは全てsolveからの内部呼び出しで実行される
 * 
 * @author GP
 *
 */

public class Solve{
	/**
	 * 現在解読中のナンプレが回答まで辿り着いたかどうかを表すフィールド
	 * <br>初期値の0ならばまだ、1ならば回答済みを示す
	 * <BR>booleanではないのは、複数回答がある場合に、その数とかも数えようとしていた名残
	 */
	static int flag;
	
	/**
	 * このクラスの中で、外部から呼び出せる唯一のメソッド
	 * <BR>このメソッドを呼び出すことで、ナンプレの解読を開始する
	 * <BR>なお、初期値の時点でナンプレが不成立の場合、nullを戻り値として返す
	 * @param map 解読したいナンプレの初期配置
	 * @return 引数として入力したmapから解読した回答を返す。なお、入力がおかしい場合nullを返す場合もある
	 */
	public static int[][] solve(int[][] map) {
		
		if (!initCheck(map))
			return null;
		
		flag = 0;
		
		return solveVer1(map);
	}
	
	/**
	 * solveメソッドから最初に呼び出されるメソッド
	 * <BR>初期配置自体が間違ってないかを判定する
	 * @param map ナンプレの初期配置
	 * @return 引数にエラーがなければtrue、あればfalseを返す
	 */
	private static Boolean initCheck(int[][] map) {
		
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				if (map[i][l] == 0)
					continue;
				if (!Solve.solveVer1Check(map, i, l))
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * ナンプレ解読の準備段階
	 * <BR>解読作業用の変数opeを用意し、確認用の初期配置mapの値をコピーする
	 * <BR>コピー完了後、解読の実行部分を呼び出す
	 * @param map 解読を行いたいナンプレの初期値
	 * @return 引数を元に解読した回答を返す
	 */
	private static int[][] solveVer1(final int[][] map) {
		
		int[][] ope = new int[9][9];
		
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				ope[i][l] = map[i][l];
			}
		}
		
		ope = solveVer1Sub(map, ope, 0, 0);
		
		return ope;
	}
	/**
	 * ナンプレ解読の実行部分。左上のマスから、順に数字を割り当て確認していく、総当り方式で解読を行う
	 * <BR>初期値があれば初期値を入れて次のマスに行く。なければ1-9を順番に当てはめて、一つづつ確認を行う
	 * <BR>問題がなければ次のマスに数字を当てていき、問題があれば今のマスの数字を次の数字にする
	 * <BR>9を当てても問題が生じた場合、現在のマスを空欄にしてから、前のマスの数字を1つ増やす
	 * <BR>上記の流れをマス単位で再帰呼び出しで実行する
	 * <BR>最後のマスに当てはめた数字で問題がない場合、flagが1となり、解読済みとみなしてメソッドが終了する
	 * 
	 * @param map 解読を行いたいナンプレの初期配置
	 * @param ope 解読作業中の現在のマップ
	 * @param x 現在確認中のマスの縦座標
	 * @param y 現在確認中のマスの横座標
	 * @return 解読作業中のマップ（flag=1の場合は正解マップ）
	 */
	private static int[][] solveVer1Sub(final int[][] map, int[][] ope, int x, int y) {
		if (y >= 9) {
			x++;
			y = 0;
		}
		if (x >= 9) {
			flag = 1;
			return ope;
		}
		
		if (map[x][y] == 0) {
			for (int i = 1; i <= 9; i++) {
				ope[x][y] = i;
				if (solveVer1Check(ope, x, y)) {
					solveVer1Sub(map, ope, x, y + 1);
					if (flag == 1)
						return ope;
				}
			}
		}
		else {
			ope[x][y] = map[x][y];
			if (solveVer1Check(ope, x, y)) {
				solveVer1Sub(map, ope, x, y + 1);
				if (flag == 1)
					return ope;
			}
		}
		
		ope[x][y] = 0;
		
		return ope;
	}
	/**
	 * ope[x][y]に当てられた数字が縦横とブロック内で重複がないかどうかを判定するメソッド
	 * <BR>どれか1つでも重複があった場合、問題有りとしてfalseを、重複がなければtrueを返す
	 * @param ope 確認に使うマップ
	 * @param x 現在確認中のマスの縦座標
	 * @param y 現在確認中のマスの横座標
	 * @return 重複の有無（有:false、無:true）
	 */
	private static boolean solveVer1Check(int[][] ope, int x, int y) {
		for (int i = 0; i < 9; i++) {
			if (i != x && ope[i][y] == ope[x][y])
				return false;
			if (i != y && ope[x][i] == ope[x][y])
				return false;
		}
		
		int s = x - x % 3;
		int t = y - y % 3;
		for (int i = 0; i < 3; i++) {
			for (int l = 0; l < 3; l++) {
				if (x == i + s || y == l + t)
					continue;
				
				if (ope[s + i][t + l] == ope[x][y])
					return false;
			}
		}
		
		return true;
	}
	
}
