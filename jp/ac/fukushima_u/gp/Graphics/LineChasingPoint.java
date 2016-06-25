package jp.ac.fukushima_u.gp.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;

/*
 * Random�ɎO���x�̃m�[�h���������A���̒����玊�߂̃m�[�h�ɑ΂��Đ����L�тĂ䂭
 * �����m�[�h�ɓ��B������ēxRandom�Ńm�[�h���ЂƂ������A�܂����߂̃m�[�h�܂Ő����L��
 * �ŏ��͐������̂܂ܕ`�悳��邪�A�����m�[�h����萔������ΌÂ��m�[�h�Ƃ�������Ȃ���͏�����
 * �Ȃ��A������m�[�h�͐��Ɍq����Ă���m�[�h�̓��A�ł��Â����̂���ɂȂ�
 */


public class LineChasingPoint extends JFrame {

	private class Node {
		int X;
		int Y;
		boolean CHASING_STATE = false;

		private Node() {

		}

		private Node(int X, int Y) {
			this.X = X;
			this.Y = Y;
		}

		private double getDistance(int x, int y) {
			double d;
			d = Math.pow(this.X - x, 2) + Math.pow(this.Y - y, 2);
			return Math.sqrt(d);
		}

		private boolean getChasingState() {
			return this.CHASING_STATE;
		}

		private void setChasingState(boolean b) {
			this.CHASING_STATE = b;
		}
	}

	private int NODE_NUM = 40;
	private int R = 5;
	private int CHASING_NODE_NUM;
//	private double LINE_START_X;
//	private double LINE_START_Y;
//	private double LINE_END_X;
//	private double LINE_END_Y;

	private Node p = new Node();

	private ArrayList<Node> CHASING_HISTORY = new ArrayList<Node>();

	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		LineChasingPoint lcp = new LineChasingPoint();
		while (true) {
			lcp.repaint();
		}

	}

	public LineChasingPoint() {
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2);

//		this.LINE_START_X = this.getSize().width / 2;
//		this.LINE_START_Y = this.getSize().height / 2;
//		this.LINE_END_X = this.getSize().width / 2;
//		this.LINE_END_Y = this.getSize().height / 2;




		this.setVisible(true);
	}

	public void paint(Graphics g) {
		Dimension size = getSize();

		g.setColor(Color.white);
		g.fillRect(0, 0, size.width, size.height);

		g.dispose();

	}

}
