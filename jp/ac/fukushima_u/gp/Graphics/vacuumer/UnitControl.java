package jp.ac.fukushima_u.gp.Graphics.vacuumer;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

public class UnitControl {
	
	//	static UnitControl suc;
	
	int PLAYER_UNIT_NUM = 3;
	//ENEMY������
	//b/a�̊m��
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
	
	//�G�l�~�[�������̕␳�l
	//�G�l�~�[�������̐����������邽�тɂP�t���[��������̑�������������
	static double BASE_ENEMY_NUM = 80;
	
	int DEFAULT_SIZE = 10;
	
	//window�T�C�Y�ɂ��G�l�~�[�������̔{���␳�l
	//�T�C�Y���傫���Ȃ�΂Ȃ�قǍ����Ȃ�
	double WIN_MAGN = 1.0;
	
	//life�ɂ�鑬�x�␳�l�B����life��DEFAULT_LIFE�̉��{��������
	//��{���x��1.0+(LIFE_MAGN * life�{��)��������
	//���Ȃ�ΐH���قǑ����A�Q����قǒx���Ȃ�
	//+-1.0�ȏ�ɂ��Ă��܂��Ƒ��x���}�C�i�X�ɂȂ�ꍇ������̂Œ���
	static double LIFE_MAGN = 0.0;
	
	ArrayList<PlayerUnit> PLAYER_UNIT_SET = new ArrayList<PlayerUnit>();
	ArrayList<EnemyUnit> ENEMY_UNIT_SET = new ArrayList<EnemyUnit>();
	Boolean[] PLAYER_UNIT_COLORS;
	
	private Random r = new Random();
	ArrayList<Color> color_set = new ArrayList<Color>();
	
	public UnitControl() {
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		this.init();
	}
	
	//1�t���[��������ɍs������
	//�G�l�~�[�̏o��
	//���j�b�g���Ƃ�
	//���Ԍo�߂Ɉ˂�_���[�W�ƁA����ɔ�������
	//���߂̃^�[�Q�b�g�̑I��(�ڐG�ς݂̏ꍇ�A���̂܂ܕߐH)
	//�^�[�Q�b�g�֌����Ă̈ړ�(�ڐG�����ꍇ�A�ߐH)
	public synchronized void step() {
		//�V�K�G�l�~�[���o��
		this.enemyPopCheck();
		
		//���݂̃v���C���[���j�b�g���̕������̎Z�o
		double damage = this.PLAYER_UNIT_SET.size();
		//		double damage =1.0;
		
		//enemy�̐���
		if (UnitControl.ENEMY_GROW != 0.0) {
			for (int i = 0; i < this.ENEMY_UNIT_SET.size(); i++) {
				this.ENEMY_UNIT_SET.get(i).life += UnitControl.ENEMY_GROW;
				if (this.ENEMY_UNIT_SET.get(i).life >= UnitControl.DEFAULT_ENEMY_LIFE) {
					this.ENEMY_UNIT_SET.get(i).life -= UnitControl.DEFAULT_ENEMY_LIFE;
					this.ENEMY_UNIT_SET.add(this.CreateEnemyUnit());
				}
			}
		}
		
		//���ׂẴv���C���[���j�b�g�ɑ΂���
		for (int i = 0; i < this.PLAYER_UNIT_SET.size(); i++) {
			
			//���C�t�����Ƃ���ɔ������S
			//			this.PLAYER_UNIT_SET.get(i).life = this.PLAYER_UNIT_SET.get(i).life - UnitControl.FRAME_DAMAGE;
			
			//unit���ɉ����ă_���[�W�𑝂₷
			this.PLAYER_UNIT_SET.get(i).life = this.PLAYER_UNIT_SET.get(i).life - damage * 0.1;
			//Player��������葽���A���A���C�t��0�ȉ��Ȃ玀�S�A����ȊO�Ȃ琶��
			if (this.PLAYER_UNIT_SET.get(i).life <= 0.0) {
				if (this.PLAYER_UNIT_SET.size() <= 3) {
					this.PLAYER_UNIT_SET.get(i).life = 1.0;
				}
				else {
					this.PLAYER_UNIT_SET.remove(i);
					//���j�b�g�����S�����ꍇ�A�ȍ~�̏���(�ړ��ƕߐH)�͍s���Ȃ��̂Ŏ���
					//���̍ہA�ȍ~�̃��j�b�g�̔ԍ����P��O�ɂ����̂�(����i+1����i��)
					//���������̃��j�b�g(i�Ԗ�)���w���悤�ɁA������i��-1���Ă���(���̃��[�v��+1����邽��)
					i--;
					continue;
				}
			}
			
			//���߂̃G�l�~�[���j�b�g��I�����A����Ɍ������Ĉړ�
			this.selectTarget(this.PLAYER_UNIT_SET.get(i));
			this.PLAYER_UNIT_SET.get(i).moveUnit();
			
			//�^�[�Q�b�g�����݂��A���v���C���[�ƃG�l�~�[���ڐG��Ԃ̏ꍇ
			if (this.PLAYER_UNIT_SET.get(i).target != null
					&& (Math.pow(this.PLAYER_UNIT_SET.get(i).getSize() + this.PLAYER_UNIT_SET.get(i).target.getSize(),
							2) > this.getDistance(this.PLAYER_UNIT_SET.get(i), this.PLAYER_UNIT_SET.get(i).target))) {
				
				EnemyUnit eu = (EnemyUnit) this.PLAYER_UNIT_SET.get(i).target;
				this.ENEMY_UNIT_SET.remove(eu);
				
				//enemy��H������life����
				this.PLAYER_UNIT_SET.get(i).life = this.PLAYER_UNIT_SET.get(i).life + eu.life;
				//life�ʂ������l�̓�{�ȏ�ɂȂ�����A�����l���}�C�i�X���ĐV�KPlayerUnit�𐶐�
				while (this.PLAYER_UNIT_SET.get(i).life > UnitControl.DEFAULT_LIFE * 2) {
					this.PLAYER_UNIT_SET.get(i).life -= UnitControl.DEFAULT_LIFE;
					this.CreatePlayerUnit();
					
					//�V�K���j�b�g�̍��W�w��
					this.PLAYER_UNIT_SET.get(this.PLAYER_UNIT_SET.size() - 1).x = this.PLAYER_UNIT_SET.get(i).x;
					this.PLAYER_UNIT_SET.get(this.PLAYER_UNIT_SET.size() - 1).y = this.PLAYER_UNIT_SET.get(i).y;
				}
				
				//�S���j�b�g�ɑ΂��A����ߐH�������j�b�g���^�[�Q�b�g�Ƃ��Ă�����̂����������m�F
				//�����ߐH�ς݂̃G�l�~�[���^�[�Q�b�g���Ă����ꍇ�A���̃��j�b�g�̃^�[�Q�b�g��null�ɂ���
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
			this.PLAYER_UNIT_COLORS[i] = true;//true�͋󂫁Afalse�͊��o
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
		
		//�F�����ׂĖ��܂��Ă��珉����(��ł�true�Ȃ�break)
		//����ȊO�Ȃ�X���[
		for (int i = 0; i < this.PLAYER_UNIT_COLORS.length; i++) {
			if (this.PLAYER_UNIT_COLORS[i])
				break;
			if (i == this.PLAYER_UNIT_COLORS.length - 1) {
				for (int l = 0; l < this.PLAYER_UNIT_COLORS.length; l++) {
					this.PLAYER_UNIT_COLORS[l] = true;//true�͋󂫁Afalse�͊��o
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
		
		//�v���C���[���j�b�gu���^�[�Q�b�g��null�o�Ȃ��ꍇ�X�L�b�v
		//		if(u.target!=null)return;
		
		for (int i = 0; i < this.ENEMY_UNIT_SET.size(); i++) {
			//���j�b�g�ƃG�l�~�[�����F�̏ꍇ(�ߐH�s�̏ꍇ)�A���̃G�l�~�[�̊m�F��
			if( u.getColor() == ENEMY_UNIT_SET.get(i).getColor()){
				continue;
			}
			
			//u(���ڒ��̃��j�b�g)����i�Ԗڂ̃G�l�~�[�܂ł̋���
			ed[i] = this.getDistance(u, this.ENEMY_UNIT_SET.get(i));
			
			//�^�[�Q�b�g�����݂��A���v���C���[�ƃG�l�~�[���ڐG��Ԃ̏ꍇ
			//���ߐH�\(�ِF)�̏ꍇ�A�ߐH���������s
			if (Math.pow(u.getSize() + this.ENEMY_UNIT_SET.get(i).getSize(), 2) > ed[i]
					&& u.getColor() != ENEMY_UNIT_SET.get(i).getColor()) {
				
				EnemyUnit eu = this.ENEMY_UNIT_SET.get(i);
				this.ENEMY_UNIT_SET.remove(eu);
				
				//enemy��H������life����
				u.life = u.life + eu.life;
				//life�ʂ������l�̓�{�ȏ�ɂȂ�����A�����l���}�C�i�X���ĐV�KPlayerUnit�𐶐�
				while (u.life > UnitControl.DEFAULT_LIFE * 2) {
					u.life -= UnitControl.DEFAULT_LIFE;
					this.CreatePlayerUnit();
					
					//�V�K���j�b�g�̍��W�w��
					this.PLAYER_UNIT_SET.get(this.PLAYER_UNIT_SET.size() - 1).x = u.x;
					this.PLAYER_UNIT_SET.get(this.PLAYER_UNIT_SET.size() - 1).y = u.y;
				}
				
				
//				for (int l = 0; l < this.PLAYER_UNIT_SET.size(); l++) {
//					if (eu.equals(this.PLAYER_UNIT_SET.get(l).target)) {
//						this.PLAYER_UNIT_SET.get(l).target = null;
//					}
//				}
				
				//�ߐH�ς݂Ȃ̂ŁA���̑Ώۂ܂ł̋�����-1��
				ed[i] = -1.0;
				//�ȍ~�̃G�l�~�[�̔ԍ��J��オ��̂ŁA�ēx�����ԍ�(���ݒ��ڒ��̎��̃G�l�~�[)���m�F�ł���悤�ɂ���
				i--;
			}
		}
		
		double a = -1.0;
		int b = 0;
		for (int i = 0; i < this.ENEMY_UNIT_SET.size(); i++) {
			//��ԍŏ����A�����_�ł̍ŒZ���������Z����΍X�V
			if ((a < 0.0 || a > ed[i]) && u.getColor() != ENEMY_UNIT_SET.get(i).getColor() && ed[i] >= 0.0) {
				
				//�ΏۃG�l�~�[�����łɐڐG��Ԃ̏ꍇ�A���̂܂܋z������
				//				if (Math.pow(u.getSize() + this.ENEMY_UNIT_SET.get(i).getSize(),
				//						2) > this.getDistance(u, this.ENEMY_UNIT_SET.get(i))){
				//					u.life+=this.ENEMY_UNIT_SET.get(i).life;
				//					this.ENEMY_UNIT_SET.remove(i);
				//					continue;
				//				}
				
				//�N���Ƀ^�[�Q�b�g����Ă����ꍇ�X�L�b�v����
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
		
		//�G�l�~�[�̐���0���ŒZ�����������̏ꍇ�G���[�Ƃ��ă^�[�Q�b�g��null
		if (this.ENEMY_UNIT_SET.size() == 0 || a < 0.0) {
			u.setTarget(null);
			return;
		}
		else {
			//�^�[�Q�b�g�I���ɖ�肪�Ȃ��ꍇ
			//�ŒZ������b�Ԗڂ̃G�l�~�[���^�[�Q�b�g�ɃZ�b�g
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
		
		//life�␳�Ȃ�
		//		double dx = (this.vel) * Math.cos(theta);
		//		double dy = (this.vel) * Math.sin(theta);
		
		//life�␳�L��
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