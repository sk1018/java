package jp.ac.fukushima_u.gp.nampre;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * �i���v����ǃv���O�����̃��C���N���X�B * ���window�n��S������N���X
 * <BR>���[�U�͂��̃N���X�̃C���X�^���X�ɑ΂�������s���A�C���X�^���X�͓K�؂ȃ��\�b�h�֓��͂𓊂��A���̌��ʂ�\������
 *
 * @author GP
 *
 */
public class Window extends JFrame implements ActionListener
{
	/**
	 * window�ɕ\������A9*9�̃i���v���}�b�v��͂����z�u�̃e�L�X�g�t�B�[���h
	 */
	private JTextField[][] tf = new JTextField[9][9];
	/**
	 * ����window�ɕ\�������{�I�ȃ{�^��
	 * �e�e�L�X�g�t�B�[���h�ɓ��͂��ꂽ�e�L�X�g��ǂݎ��A��������ɉ�ǂ����s����
	 */
	private JButton ok;
	/**
	 * �e�L�X�g�t�B�[���h�ɓ��͂��ꂽ������S�ċ�ɂ���
	 */
	private JButton clear;
	/**
	 * ���ݕ\�����̃e�L�X�g�t�B�[���h�̕������A�R�s�[���₷���悤�ɑS�Ă̕�����1�̃e�L�X�g�G���A�ɓ��͂���
	 */
	private JButton copy;
	/**
	 * �e�L�X�g�t�B�[���h�̕\�����镶�����A�O��ok�{�^�������������̏�Ԃɖ߂�
	 * <BR>�����񉟂����ꍇ�ɂ͂��̉񐔕��O�̏�Ԃɖ߂�A��ԍŏ��̏�Ԃŉ������ꍇ�ɂ̓G���[�\���p��window���N������
	 */
	private JButton back;
	/**
	 * �T�uwindow�p�̃{�^��
	 * ���ݕ\�����̃T�uwindow�̃C���X�^���X��setVisible(false)�����s����
	 */
	private JButton Attention, copyWinOk;
	/**
	 * �T�uwindow�̃C���X�^���X
	 */
	private JDialog atten, copyWin;
	/**
	 * �{�^��back�Ŗ߂邽�߂̏�����Ԃ�ۑ����邽�߂̃X�^�b�N
	 */
	private Stack<MapForList> stack = new Stack<MapForList>();

	/**
	 * �v���O�������s�p��main���\�b�h
	 * @param args null��ok
	 */
	public static void main(String[] args) {

		Window inwin = new Window();
		inwin.mapWindowCreate();

	}

	/**
	 * window�N���G�C�g�p�̃��\�b�h
	 * @param str window�^�C�g��
	 * @param width window����
	 * @param height window����
	 */
	void jframeCreate(String str, int width, int height) {

		Container CP = getContentPane();
		CP.setLayout(new FlowLayout());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(str);
		setSize(width, height);

	}

	/**
	 * JButton�쐬�p�̃��\�b�h
	 * <br>�Ȃ��AActionListener�͂��̃C���X�^���X�ɓo�^
	 * @param str �{�^���ɕ\������e�L�X�g
	 * @return �쐬���ꂽ�{�^���̃C���X�^���X
	 */
	public JButton buttonCreate(String str) {
		JButton b1 = new JButton(str);
		b1.addActionListener(this);
		getContentPane().add(b1);
		return b1;
	}

	/**
	 * ���̃C���X�^���X���̊e��{�^�������������Ɏ��s�����A�N�V�������L���������\�b�h
	 */
	public void actionPerformed(ActionEvent e) {
		System.out.println("OK");

		if (e.getSource() == ok) {
			int[][] map = new int[9][9];
			map = getMap();
			stack.push(new MapForList(map));
			map = jp.ac.fukushima_u.gp.nampre.solve.Solve.solve(map);
			if (map == null) {
				attentionWindowCreate("INPUT IS ERROE!");
			}
			else {
				outMap(map);
				First.outputCons(map);
			}
		}
		if (e.getSource() == clear) {
			clearMap();
		}
		if (e.getSource() == Attention) {
			atten.setVisible(false);
		}
		if (e.getSource() == copy) {
			copyWinCreate();
		}
		if (e.getSource() == back) {
			if (stack.empty())
				attentionWindowCreate("NO DATE!");
			else
				setMap(stack.pop().getMap());
		}
		if (e.getSource() == copyWinOk) {
			copyWin.setVisible(false);
		}
	}

	/**
	 * �R�s�[�p�̃T�uwindow���쐬���C�����Ƀ}�b�v��\�����郁�\�b�h
	 * <BR>�E�B���h�E���쐬���C�e�L�X�g�G���A�𒣂�t����
	 * <BR>���̌�C�e�L�X�g�G���A�Ƀ��C��window�����ݕ\�����̕������擾������t����
	 */
	private void copyWinCreate() {
		int w = 100;
		int h = 210;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		copyWin = new JDialog(this, "copy window", true);
		copyWin.setBounds(d.width / 2 - w / 2, d.height / 2 - h / 2, w, h);
		copyWin.setLayout(new BorderLayout());

		String str = new String("");

		int[][] map = getMap();

		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				str += map[i][l] + " ";
			}
			str += "\n";
		}

		JTextArea ta = new JTextArea(str, 9, 9);
		copyWin.getContentPane().add("Center", ta);

		copyWinOk = new JButton("ok");
		copyWinOk.addActionListener(this);
		copyWin.getContentPane().add("South", copyWinOk);

		copyWin.setVisible(true);
	}

	/**
	 * �T�uwindow<BR>
	 * ��肪���������ۂɁC�x����\������ׂ�window
	 * <BR>�^�C�g���́uAttention Window�v�ŌŒ�
	 * @param s window�ɕ\��������������
	 * @return ���������T�uwindow�̃C���X�^���X
	 */
	private void attentionWindowCreate(String s) {
		int w = 200;
		int h = 100;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		atten = new JDialog(this, "ATTENTION WINDOW", true);
		atten.setBounds(d.width / 2 - w / 2, d.height / 2 - h / 2, w, h);
		atten.getContentPane().setLayout(new GridLayout(2, 1));
		atten.getContentPane().add(new JLabel(s));
		Attention = new JButton("OK");
		Attention.addActionListener(this);
		atten.getContentPane().add(Attention);
		atten.setVisible(true);

	}

	/**
	 * ���C��window�쐬�p�̃��\�b�h
	 * <BR>main�֐�����C�Ƃ肠�������ꂪ���s�����
	 * <BR>�ꉞ���\�b�h�Ƃ��ēƗ����Ă��邯�ǁC���Ԃ�R���X�g���N�^�ɂ��Ă������C������
	 *
	 */
	public void mapWindowCreate() {
		jframeCreate("input", 300, 300);
		Container CP = getContentPane();
		CP.setLayout(new GridLayout(11, 9));

		for (int i = 0; i < 9; i++) {
			if (i != 4)
				CP.add(new JLabel(" "));
			else
				CP.add(new JLabel("input"));
		}

		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				tf[i][l] = new JTextField("", 3);
				CP.add(tf[i][l]);
			}
		}
		for (int i = 0; i < 9; i++) {
			if (i == 2)
				ok = buttonCreate("ok");
			else if (i == 3)
				copy = buttonCreate("copy");
			else if (i == 5)
				back = buttonCreate("back");
			else if (i == 6)
				clear = buttonCreate("clear");
			else
				CP.add(new JLabel(" "));
		}

		setVisible(true);

	}

	/**
	 * ���C��window��9*9�̃e�L�X�g�t�B�[���h���當������擾���C1-9�Ȃ炻�̂܂܁C�����łȂ����0�Ƃ��āC
	 * int�^�񎟌��z��ɕϊ����ĕԂ����\�b�h
	 * @return �e���W�̃e�L�X�g�t�B�[���h�̓��͕������0-9�ɕϊ����āC�Ή��������W�ɑ������int�^�񎟌��z��
	 */
	private int[][] getMap() {
		int[][] map = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				map[i][l] = -1;
				String s = tf[i][l].getText();
				try {
					map[i][l] = Integer.parseInt(s);
					if (map[i][l] < 1 || map[i][l] > 9)
						map[i][l] = 0;
				} catch (NumberFormatException e) {
					map[i][l] = 0;
				}
			}
		}

		return map;
	}

	/**
	 * �����Ƃ��ē��͂��ꂽint[9][9]�̒l���C�Ή��������W�̃e�L�X�g�t�B�[���h�ɏo�͂���
	 * @param map �e�L�X�g�t�B�[���h�ɏo�͂�����int[9][9]
	 */
	private void outMap(int[][] map) {
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				tf[i][l].setText(String.valueOf(map[i][l]));
			}
		}
	}

	/**
	 * ���C��window�̑S�e�L�X�g�t�B�[���h����ɂ��郁�\�b�h
	 */
	private void clearMap() {
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				tf[i][l].setText("");
			}
		}
	}

	/**
	 * ���C��window��9*9�̃e�L�X�g�t�B�[���h�ɁA�����Ƃ��ė^����ꂽmap�̒l���o�͂���
	 * <BR>�e�L�X�g�t�B�[���h�̍��W(x,y)��map[x][y]�̒l���o�͂���
	 * @param map ���C��window�ɏo�͂�����int[][]
	 */
	private void setMap(int[][] map) {
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				String s = new String("");
				if (map[i][l] != 0)
					s += map[i][l];
				tf[i][l].setText(s);
			}
		}
	}

	/**
	 * ���b�p�[�N���X{@literal ?}���ŁAint�^�񎟌��z����X�^�b�N�Ƃ��Ďg�����߂ɃI�u�W�F�N�g�����邽�߂̃N���X
	 * <BR>���͗p�̃R���X�g���N�^�Əo�͗p�̃��\�b�h�݂̂�����
	 * @author GP
	 *
	 */
	private class MapForList {
		/**
		 * ���̓f�[�^��ێ�����t�B�[���h
		 */
		int[][] map = new int[9][9];

		/**
		 * �f�[�^�̃R�s�[���s���R���X�g���N�^
		 * <BR>�P����map=ope�łȂ��̂́Aint[][]�^�̕ϐ��ł��邽�߁A�Q�Ɛ�̃R�s�[�ɂȂ�\�������邽��
		 * <BR>���ׁ̈A�z��̊e�ꏊ���Ƃɒl�̃R�s�[���s��
		 * @param ope �ۑ����s������int[][]�^�ϐ�
		 */
		MapForList(int[][] ope) {
			for (int i = 0; i < 9; i++) {
				for (int l = 0; l < 9; l++) {
					map[i][l] = ope[i][l];
				}
			}

		}

		/**
		 * �o�͗p�̃��\�b�h
		 * <BR>�����ɕێ����Ă���int[][]�����̂܂ܕԂ�
		 *
		 * @return �ێ����Ă���int[][]�^�f�[�^
		 */
		public int[][] getMap() {
			return map;
		}

	}

}
