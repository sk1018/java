package jp.ac.fukushima_u.gp.nampre.solve;

/**
 * 9*9�̕W���^�̃i���v����Ǘp�̃N���X�B
 * <br>�O������i���v���̏����l��int[][]�œ��͂��A���̉񓚂�int[][]�Ƃ��ĕԂ�
 * <br>�O�������solve�������Ăяo���A���̃��\�b�h�͑S��solve����̓����Ăяo���Ŏ��s�����
 * 
 * @author GP
 *
 */

public class Solve{
	/**
	 * ���݉�ǒ��̃i���v�����񓚂܂ŒH�蒅�������ǂ�����\���t�B�[���h
	 * <br>�����l��0�Ȃ�΂܂��A1�Ȃ�Ή񓚍ς݂�����
	 * <BR>boolean�ł͂Ȃ��̂́A�����񓚂�����ꍇ�ɁA���̐��Ƃ��������悤�Ƃ��Ă������c
	 */
	static int flag;
	
	/**
	 * ���̃N���X�̒��ŁA�O������Ăяo����B��̃��\�b�h
	 * <BR>���̃��\�b�h���Ăяo�����ƂŁA�i���v���̉�ǂ��J�n����
	 * <BR>�Ȃ��A�����l�̎��_�Ńi���v�����s�����̏ꍇ�Anull��߂�l�Ƃ��ĕԂ�
	 * @param map ��ǂ������i���v���̏����z�u
	 * @return �����Ƃ��ē��͂���map�����ǂ����񓚂�Ԃ��B�Ȃ��A���͂����������ꍇnull��Ԃ��ꍇ������
	 */
	public static int[][] solve(int[][] map) {
		
		if (!initCheck(map))
			return null;
		
		flag = 0;
		
		return solveVer1(map);
	}
	
	/**
	 * solve���\�b�h����ŏ��ɌĂяo����郁�\�b�h
	 * <BR>�����z�u���̂��Ԉ���ĂȂ����𔻒肷��
	 * @param map �i���v���̏����z�u
	 * @return �����ɃG���[���Ȃ����true�A�����false��Ԃ�
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
	 * �i���v����ǂ̏����i�K
	 * <BR>��Ǎ�Ɨp�̕ϐ�ope��p�ӂ��A�m�F�p�̏����z�umap�̒l���R�s�[����
	 * <BR>�R�s�[������A��ǂ̎��s�������Ăяo��
	 * @param map ��ǂ��s�������i���v���̏����l
	 * @return ���������ɉ�ǂ����񓚂�Ԃ�
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
	 * �i���v����ǂ̎��s�����B����̃}�X����A���ɐ��������蓖�Ċm�F���Ă����A����������ŉ�ǂ��s��
	 * <BR>�����l������Ώ����l�����Ď��̃}�X�ɍs���B�Ȃ����1-9�����Ԃɓ��Ă͂߂āA��Âm�F���s��
	 * <BR>��肪�Ȃ���Ύ��̃}�X�ɐ����𓖂ĂĂ����A��肪����΍��̃}�X�̐��������̐����ɂ���
	 * <BR>9�𓖂ĂĂ���肪�������ꍇ�A���݂̃}�X���󗓂ɂ��Ă���A�O�̃}�X�̐�����1���₷
	 * <BR>��L�̗�����}�X�P�ʂōċA�Ăяo���Ŏ��s����
	 * <BR>�Ō�̃}�X�ɓ��Ă͂߂������Ŗ�肪�Ȃ��ꍇ�Aflag��1�ƂȂ�A��Ǎς݂Ƃ݂Ȃ��ă��\�b�h���I������
	 * 
	 * @param map ��ǂ��s�������i���v���̏����z�u
	 * @param ope ��Ǎ�ƒ��̌��݂̃}�b�v
	 * @param x ���݊m�F���̃}�X�̏c���W
	 * @param y ���݊m�F���̃}�X�̉����W
	 * @return ��Ǎ�ƒ��̃}�b�v�iflag=1�̏ꍇ�͐����}�b�v�j
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
	 * ope[x][y]�ɓ��Ă�ꂽ�������c���ƃu���b�N���ŏd�����Ȃ����ǂ����𔻒肷�郁�\�b�h
	 * <BR>�ǂꂩ1�ł��d�����������ꍇ�A���L��Ƃ���false���A�d�����Ȃ����true��Ԃ�
	 * @param ope �m�F�Ɏg���}�b�v
	 * @param x ���݊m�F���̃}�X�̏c���W
	 * @param y ���݊m�F���̃}�X�̉����W
	 * @return �d���̗L���i�L:false�A��:true�j
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
