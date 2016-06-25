package jp.ac.fukushima_u.gp.Graphics.vacuumer;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

public class UnitControl {
	
	//	static UnitControl suc;
	
	int PLAYER_UNIT_NUM = 3;
	//ENEMY発生率
	//b/aの確率
	int ENEMY_UNIT_PROB_A = 60;
	int ENEMY_UNIT_PROB_B = 20;
	int ENEMY_UNIT_PROB_C = 10;
	
	int ENEMY_POP_MIN = 1;
	int ENEMY_POP_MAX = 1;
	
	static double DEFAULT_LIFE = 180;
	static double DEFAULT_ENEMY_LIFE = 3.0;
	static double FRAME_DAMAGE = 2.0;
	static double DEFAULT_VELOCITY = 5.0;
	static double ENEMY_GROW = 0.00;
	static double ENEMY_SPLIT = 10.0;
	
	//エネミー発生数の補正値
	//エネミー数がこの数だけ増えるたびに１フレームあたりの増加数が増える
	static double BASE_ENEMY_NUM = 80;
	
	int DEFAULT_SIZE = 10;
	
	//windowサイズによるエネミー発生率の倍率補正値
	//サイズが大きくなればなるほど高くなる
	double WIN_MAGN = 1.0;
	
	//lifeによる速度補正値。現在lifeがDEFAULT_LIFEの何倍かを求め
	//基本速度に1.0+(LIFE_MAGN * life倍率)をかける
	//正ならば食うほど早く、飢えるほど遅くなる
	//+-1.0以上にしてしまうと速度がマイナスになる場合があるので注意
	static double LIFE_MAGN = 0.0;
	
	ArrayList<PlayerUnit> PLAYER_UNIT_SET = new ArrayList<PlayerUnit>();
	ArrayList<EnemyUnit> ENEMY_UNIT_SET = new ArrayList<EnemyUnit>();
	Boolean[] PLAYER_UNIT_COLORS;
	
	private Random r = new Random();
	ArrayList<Color> color_set = new ArrayList<Color>();
	
	public UnitControl() {
		// TODO 自動生成されたコンストラクター・スタブ
		this.init();
	}
	
	//1フレームあたりに行うこと
	//エネミーの出現
	//ユニットごとに
	//時間経過に依るダメージと、それに伴う消滅
	//至近のターゲットの選択(接触済みの場合、そのまま捕食)
	//ターゲットへ向けての移動(接触した場合、捕食)
	public synchronized void step() {
		//新規エネミーを出現
		this.enemyPopCheck();
		
		//現在のプレイヤーユニット数の平方根の算出
		double damage = this.PLAYER_UNIT_SET.size();
		//		double damage =1.0;
		
		//enemyの成長
		if (UnitControl.ENEMY_GROW != 0.0) {
			for (int i = 0; i < this.ENEMY_UNIT_SET.size(); i++) {
				this.ENEMY_UNIT_SET.get(i).life += UnitControl.ENEMY_GROW;
				if (this.ENEMY_UNIT_SET.get(i).life >= UnitControl.DEFAULT_ENEMY_LIFE) {
					this.ENEMY_UNIT_SET.get(i).life -= UnitControl.DEFAULT_ENEMY_LIFE;
					this.ENEMY_UNIT_SET.add(this.CreateEnemyUnit());
				}
			}
		}
		
		//すべてのプレイヤーユニットに対して
		for (int i = 0; i < this.PLAYER_UNIT_SET.size(); i++) {
			
			//ライフ減少とそれに伴う死亡
			//			this.PLAYER_UNIT_SET.get(i).life = this.PLAYER_UNIT_SET.get(i).life - UnitControl.FRAME_DAMAGE;
			
			//unit数に応じてダメージを増やす
			this.PLAYER_UNIT_SET.get(i).life = this.PLAYER_UNIT_SET.get(i).life - damage * 0.1;
			//Player数が一定より多い、かつ、ライフが0以下なら死亡、それ以外なら生存
			if (this.PLAYER_UNIT_SET.get(i).life <= 0.0) {
				if (this.PLAYER_UNIT_SET.size() <= 3) {
					this.PLAYER_UNIT_SET.get(i).life = 1.0;
				}
				else {
					this.PLAYER_UNIT_SET.remove(i);
					//ユニットが死亡した場合、以降の処理(移動と捕食)は行われないので次へ
					//その際、以降のユニットの番号が１つ手前にずれるので(次がi+1からiに)
					//正しく次のユニット(i番目)を指すように、ここでiを-1しておく(次のループで+1されるため)
					i--;
					continue;
				}
			}
			
			//至近のエネミーユニットを選択し、それに向かって移動
			this.selectTarget(this.PLAYER_UNIT_SET.get(i));
			this.PLAYER_UNIT_SET.get(i).moveUnit();
			
			//ターゲットが存在し、かつプレイヤーとエネミーが接触状態の場合
			if (this.PLAYER_UNIT_SET.get(i).target != null
					&& (Math.pow(this.PLAYER_UNIT_SET.get(i).getSize() + this.PLAYER_UNIT_SET.get(i).target.getSize(),
							2) > this.getDistance(this.PLAYER_UNIT_SET.get(i), this.PLAYER_UNIT_SET.get(i).target))) {
				
				EnemyUnit eu = (EnemyUnit) this.PLAYER_UNIT_SET.get(i).target;
				this.ENEMY_UNIT_SET.remove(eu);
				
				//enemyを食ったらlifeが回復
				this.PLAYER_UNIT_SET.get(i).life = this.PLAYER_UNIT_SET.get(i).life + eu.life;
				//life量が初期値の二倍以上になったら、初期値分マイナスして新規PlayerUnitを生成
				while (this.PLAYER_UNIT_SET.get(i).life > UnitControl.DEFAULT_LIFE * 2) {
					this.PLAYER_UNIT_SET.get(i).life -= UnitControl.DEFAULT_LIFE;
					this.CreatePlayerUnit();
					
					//新規ユニットの座標指定
					this.PLAYER_UNIT_SET.get(this.PLAYER_UNIT_SET.size() - 1).x = this.PLAYER_UNIT_SET.get(i).x;
					this.PLAYER_UNIT_SET.get(this.PLAYER_UNIT_SET.size() - 1).y = this.PLAYER_UNIT_SET.get(i).y;
				}
				
				//全ユニットに対し、今回捕食したユニットをターゲットとしているものが無いかを確認
				//もし捕食済みのエネミーをターゲットしていた場合、そのユニットのターゲットをnullにする
//				for (int l = 0; l < this.PLAYER_UNIT_SET.size(); l++) {
//					if (eu.equals(this.PLAYER_UNIT_SET.get(l).target)) {
//						this.PLAYER_UNIT_SET.get(l).target = null;
//					}
//				}
			}
		}
		
	}
	
	private void init() {
		
		this.color_set.clear();
		
		color_set.add(Color.blue);
		color_set.add(Color.cyan);
		color_set.add(Color.green);
		color_set.add(Color.magenta);
		color_set.add(Color.orange);
		color_set.add(Color.pink);
		color_set.add(Color.red);
		color_set.add(Color.white);
		color_set.add(Color.yellow);
		
		this.PLAYER_UNIT_COLORS = new Boolean[this.color_set.size()];
		for (int i = 0; i < this.PLAYER_UNIT_COLORS.length; i++) {
			this.PLAYER_UNIT_COLORS[i] = true;//trueは空き、falseは既出
		}
		
		for (int i = 0; i < this.PLAYER_UNIT_NUM; i++) {
			this.CreatePlayerUnit();
		}
		
		//		UnitControl.suc=this;
		
	}
	
	void enemyPopCheck() {
		//		int a = this.ENEMY_UNIT_PROB_A;
		//		int b = this.ENEMY_UNIT_PROB_B;
		//		while (b > a) {
		//			this.ENEMY_UNIT_SET.add(this.CreateEnemyUnit());
		//			b = b - a;
		//		}
		//		for (int i = 0; i < this.ENEMY_UNIT_PROB_C; i++) {
		//			int n = r.nextInt(a);
		//			if (n < b) {
		//				this.ENEMY_UNIT_SET.add(this.CreateEnemyUnit());
		//			}
		//		}
		
		int a = (int) ((r.nextInt(this.ENEMY_POP_MAX - this.ENEMY_POP_MIN + 1) + this.ENEMY_POP_MIN) * this.WIN_MAGN);
		for (int i = 0; i < a; i++) {
			this.ENEMY_UNIT_SET.add(this.CreateEnemyUnit());
		}
		
		for (int i = 0; i < this.ENEMY_UNIT_SET.size() / (UnitControl.BASE_ENEMY_NUM * this.WIN_MAGN); i++) {
			this.ENEMY_UNIT_SET.add(this.CreateEnemyUnit());
		}
		
	}
	
	double getDistance(Unit a, Unit b) {
		double dx = a.getX() - b.getX();
		double dy = a.getY() - b.getY();
		
		return Math.pow(dx, 2) + Math.pow(dy, 2);
	}
	
	private Unit UnitCreate(Unit u) {
		Dimension d = VacuumerMain.win.getSize();
		
		u.setX(r.nextInt(d.width));
		u.setY(r.nextInt(d.height));
		u.setSize(this.DEFAULT_SIZE);
		u.setColor(this.color_set.get(r.nextInt(this.color_set.size())));
		u.life = 300;
		
		return u;
	}
	
	void CreatePlayerUnit() {
		PlayerUnit pu = new PlayerUnit();
		pu = (PlayerUnit) this.UnitCreate((Unit) pu);
		
		//色がすべて埋まってたら初期化(一つでもtrueならbreak)
		//それ以外ならスルー
		for (int i = 0; i < this.PLAYER_UNIT_COLORS.length; i++) {
			if (this.PLAYER_UNIT_COLORS[i])
				break;
			if (i == this.PLAYER_UNIT_COLORS.length - 1) {
				for (int l = 0; l < this.PLAYER_UNIT_COLORS.length; l++) {
					this.PLAYER_UNIT_COLORS[l] = true;//trueは空き、falseは既出
				}
			}
		}
		
		int n;
		do {
			n = r.nextInt(this.color_set.size());
		} while (!this.PLAYER_UNIT_COLORS[n]);
		
		this.PLAYER_UNIT_COLORS[n] = false;
		pu.setColor(this.color_set.get(n));
		
		pu.setVelocity(UnitControl.DEFAULT_VELOCITY);
		pu.life = UnitControl.DEFAULT_LIFE;
		
		this.PLAYER_UNIT_SET.add(pu);
		
		return;
	}
	
	EnemyUnit CreateEnemyUnit() {
		EnemyUnit eu = new EnemyUnit();
		eu = (EnemyUnit) this.UnitCreate((Unit) eu);
		eu.life = UnitControl.DEFAULT_ENEMY_LIFE;
		return eu;
	}
	
	void selectTarget(PlayerUnit u) {
		double[] ed = new double[this.ENEMY_UNIT_SET.size()];
		
		//プレイヤーユニットuがターゲットがnull出ない場合スキップ
		//		if(u.target!=null)return;
		
		for (int i = 0; i < this.ENEMY_UNIT_SET.size(); i++) {
			//ユニットとエネミーが同色の場合(捕食不可の場合)、次のエネミーの確認へ
			if( u.getColor() == ENEMY_UNIT_SET.get(i).getColor()){
				continue;
			}
			
			//u(着目中のユニット)からi番目のエネミーまでの距離
			ed[i] = this.getDistance(u, this.ENEMY_UNIT_SET.get(i));
			
			//ターゲットが存在し、かつプレイヤーとエネミーが接触状態の場合
			//かつ捕食可能(異色)の場合、捕食処理を実行
			if (Math.pow(u.getSize() + this.ENEMY_UNIT_SET.get(i).getSize(), 2) > ed[i]
					&& u.getColor() != ENEMY_UNIT_SET.get(i).getColor()) {
				
				EnemyUnit eu = this.ENEMY_UNIT_SET.get(i);
				this.ENEMY_UNIT_SET.remove(eu);
				
				//enemyを食ったらlifeが回復
				u.life = u.life + eu.life;
				//life量が初期値の二倍以上になったら、初期値分マイナスして新規PlayerUnitを生成
				while (u.life > UnitControl.DEFAULT_LIFE * 2) {
					u.life -= UnitControl.DEFAULT_LIFE;
					this.CreatePlayerUnit();
					
					//新規ユニットの座標指定
					this.PLAYER_UNIT_SET.get(this.PLAYER_UNIT_SET.size() - 1).x = u.x;
					this.PLAYER_UNIT_SET.get(this.PLAYER_UNIT_SET.size() - 1).y = u.y;
				}
				
				
//				for (int l = 0; l < this.PLAYER_UNIT_SET.size(); l++) {
//					if (eu.equals(this.PLAYER_UNIT_SET.get(l).target)) {
//						this.PLAYER_UNIT_SET.get(l).target = null;
//					}
//				}
				
				//捕食済みなので、その対象までの距離は-1に
				ed[i] = -1.0;
				//以降のエネミーの番号繰り上がるので、再度同じ番号(現在着目中の次のエネミー)を確認できるようにする
				i--;
			}
		}
		
		double a = -1.0;
		int b = 0;
		for (int i = 0; i < this.ENEMY_UNIT_SET.size(); i++) {
			//一番最初か、現時点での最短距離よりも短ければ更新
			if ((a < 0.0 || a > ed[i]) && u.getColor() != ENEMY_UNIT_SET.get(i).getColor() && ed[i] >= 0.0) {
				
				//対象エネミーがすでに接触状態の場合、そのまま吸収する
				//				if (Math.pow(u.getSize() + this.ENEMY_UNIT_SET.get(i).getSize(),
				//						2) > this.getDistance(u, this.ENEMY_UNIT_SET.get(i))){
				//					u.life+=this.ENEMY_UNIT_SET.get(i).life;
				//					this.ENEMY_UNIT_SET.remove(i);
				//					continue;
				//				}
				
				//誰かにターゲットされていた場合スキップする
				//				boolean s = false;
				//				for (int l = 0; l < this.PLAYER_UNIT_SET.size(); l++) {
				//					s = this.ENEMY_UNIT_SET.get(i).equals(this.PLAYER_UNIT_SET.get(l).target);
				//					if (s)
				//						break;
				//				}
				//				if (s)
				//					continue;
				
				a = ed[i];
				b = i;
			}
		}
		
		//エネミーの数が0か最短距離が負数の場合エラーとしてターゲットはnull
		if (this.ENEMY_UNIT_SET.size() == 0 || a < 0.0) {
			u.setTarget(null);
			return;
		}
		else {
			//ターゲット選択に問題がない場合
			//最短距離のb番目のエネミーをターゲットにセット
			u.setTarget(this.ENEMY_UNIT_SET.get(b));
		}
		
	}
	
	synchronized void sizeOutEnemy(Dimension d) {
		for (int i = 0; i < this.ENEMY_UNIT_SET.size(); i++) {
			EnemyUnit eu = this.ENEMY_UNIT_SET.get(i);
			if (eu.x >= d.width || eu.y >= d.height) {
				this.ENEMY_UNIT_SET.remove(i);
				i--;
			}
		}
		
	}
	
}

class Unit {
	
	double x, y;
	double size;
	Color co;
	double life;
	
	Unit() {
		
	}
	
	void setX(double x) {
		this.x = x;
	}
	
	void setY(double y) {
		this.y = y;
	}
	
	void setSize(double r) {
		this.size = r;
	}
	
	void setColor(Color co) {
		this.co = co;
	}
	
	double getX() {
		return this.x;
	}
	
	double getY() {
		return this.y;
	}
	
	double getSize() {
		return this.size;
	}
	
	Color getColor() {
		return co;
	}
}

class PlayerUnit extends Unit {
	double vel;
	Unit target = null;
	
	void setTarget(Unit t) {
		this.target = t;
	}
	
	void setVelocity(double v) {
		this.vel = v;
	}
	
	void moveUnit() {
		
		if (this.target == null) {
			return;
		}
		double tx = target.getX() - this.x;
		double ty = target.getY() - this.y;
		double theta = Math.atan2(ty, tx);
		
		//life補正なし
		//		double dx = (this.vel) * Math.cos(theta);
		//		double dy = (this.vel) * Math.sin(theta);
		
		//life補正有り
		double dx = (this.vel * (1.0 + UnitControl.LIFE_MAGN * (this.life - UnitControl.DEFAULT_LIFE)
				/ UnitControl.DEFAULT_LIFE))
				* Math.cos(theta);
		double dy = (this.vel * (1.0 + UnitControl.LIFE_MAGN * (this.life - UnitControl.DEFAULT_LIFE)
				/ UnitControl.DEFAULT_LIFE))
				* Math.sin(theta);
		
		this.setX(dx + this.getX());
		this.setY(dy + this.getY());
	}
}

class EnemyUnit extends Unit {
	
}