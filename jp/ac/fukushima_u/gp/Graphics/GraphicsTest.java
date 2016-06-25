package jp.ac.fukushima_u.gp.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;

import jp.ac.fukushima_u.gp.template.Template;

public class GraphicsTest extends JFrame implements KeyListener {

	//	private class MovementThread extends Thread{
	//		private GraphicsTest gt;
	//
	//		public void run() {
	//			this.gt.nodeMove();
	//			this.gt.repaint(100);
	//		}
	//
	//		private void setIns(GraphicsTest gt) {
	//			this.gt = gt;
	//		}
	//	}

	private int NODE_NUM = 4;
	private int NODE_SET_NUM = 3;
	private int COLOR_NUM = 8;
	private boolean[] COLOR_USED = new boolean[this.COLOR_NUM];
	private Color[] COLOR_SET = new Color[this.COLOR_NUM];
	private Color[] COLOR_SET_LIST = new Color[this.COLOR_NUM];
	private double[][] NODE_POSITION_X = new double[this.NODE_SET_NUM][this.NODE_NUM];
	private double[][] NODE_POSITION_Y = new double[this.NODE_SET_NUM][this.NODE_NUM];
	private double[][] NODE_VELOCITY = new double[this.NODE_SET_NUM][this.NODE_NUM];
	private double[][] NODE_ANGLE = new double[this.NODE_SET_NUM][this.NODE_NUM];//角度。3時を基準に反時計方向にカウント。三角関数をイメージ
	private int NODE_VELOCITY_MAX = 15;
	private int NODE_VELOCITY_MIN = 5;
	private Random r;

	//private MovementThread mt;

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		GraphicsTest gt = new GraphicsTest();

		//gt.setUndecorated(true);
		gt.setVisible(true);
		while (true) {
			gt.sleep(10);
			gt.nodeMove();
			gt.repaint();
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

	public void paint(Graphics g) {
		Dimension size = getSize();

		g.setColor(Color.black);
		g.fillRect(0, 0, size.width, size.height);

		for (int set = 0; set < this.NODE_SET_NUM; set++) {
			if (COLOR_SET.length >= set)
				g.setColor(COLOR_SET[set]);
			else
				g.setColor(Color.cyan);
			for (int i = 0; i < this.NODE_NUM; i++) {
				int t = i + 1;
				if (t >= this.NODE_NUM)
					t = 0;
				g.drawLine((int) this.NODE_POSITION_X[set][i], (int) this.NODE_POSITION_Y[set][i],
						(int) this.NODE_POSITION_X[set][t],
						(int) this.NODE_POSITION_Y[set][t]);
			}
		}
		g.dispose();

	}

	private void nodeMove() {

		for (int set = 0; set < this.NODE_SET_NUM; set++) {
			for (int i = 0; i < this.NODE_NUM; i++) {
				this.NODE_POSITION_X[set][i] += Math.cos(Math.toRadians(this.NODE_ANGLE[set][i]))
						* this.NODE_VELOCITY[set][i];
				this.NODE_POSITION_Y[set][i] -= Math.sin(Math.toRadians(this.NODE_ANGLE[set][i]))
						* this.NODE_VELOCITY[set][i];

				Dimension size = getSize();
				if (this.NODE_POSITION_X[set][i] < 0) {
					this.NODE_POSITION_X[set][i] = 0;
					this.NODE_ANGLE[set][i] = 180.0 - this.NODE_ANGLE[set][i];
				}
				else if (this.NODE_POSITION_X[set][i] > size.width) {
					this.NODE_POSITION_X[set][i] = size.width;
					this.NODE_ANGLE[set][i] = 180.0 - this.NODE_ANGLE[set][i];
				}
				int n = 15;
				if (this.NODE_POSITION_Y[set][i] < n) {
					this.NODE_POSITION_Y[set][i] = n;
					this.NODE_ANGLE[set][i] = -this.NODE_ANGLE[set][i];
				}
				else if (this.NODE_POSITION_Y[set][i] > size.height) {
					this.NODE_POSITION_Y[set][i] = size.height;
					this.NODE_ANGLE[set][i] = -this.NODE_ANGLE[set][i];
				}
			}
		}
	}

	private GraphicsTest() {
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.getContentPane().setBackground(Color.black);

		this.setInitStatus();
		this.setInitColors();
		this.setColors();
		this.addKeyListener(this);

		this.setTitle("screen saver");
		//Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\GP\\Desktop\\seiya\\Dropbox\\pro\\java\\jp\\ac\\fukushima_u\\gp\\materials\\test\\color-wheel-icon.jpg");
		Image icon = Toolkit.getDefaultToolkit().getImage(
				Template.getCLASSPATH() + "jp\\ac\\fukushima_u\\gp\\materials\\test\\color-wheel-icon.jpg");
		this.setIconImage(icon);
	}

	private void setInitStatus() {
		r = new Random();
		for (int set = 0; set < this.NODE_SET_NUM; set++) {
			for (int i = 0; i < this.NODE_NUM; i++) {
				this.NODE_POSITION_X[set][i] = r.nextInt(this.getSize().width + 1);
				this.NODE_POSITION_Y[set][i] = r.nextInt(this.getSize().height + 1);
				this.NODE_VELOCITY[set][i] = r.nextInt(this.NODE_VELOCITY_MAX - this.NODE_VELOCITY_MIN)
						+ this.NODE_VELOCITY_MIN;
				this.NODE_ANGLE[set][i] = r.nextDouble() * 360;
				//				this.NODE_VELOCITY[set][i] = 10;

			}
		}
	}

	private void setColors() {
		for (int i = 0; i < this.COLOR_NUM; i++) {
			this.COLOR_USED[i] = false;
		}
		Random r = new Random();
		int t;
		for (int i = 0; i < this.NODE_SET_NUM; i++) {
			if (i >= this.COLOR_NUM) {
				for (int l = 0; l < this.COLOR_NUM; l++) {
					this.COLOR_USED[l] = false;
				}
			}
			do {
				t = r.nextInt(COLOR_NUM);
			} while (this.COLOR_USED[t]);
			COLOR_SET[i] = COLOR_SET_LIST[t];
			this.COLOR_USED[t] = true;

		}
	}

	private void setInitColors() {
		for (int i = 0; i < this.COLOR_NUM; i++)
			this.COLOR_USED[i] = false;
		COLOR_SET_LIST[0] = Color.cyan;
		COLOR_SET_LIST[1] = Color.green;
		COLOR_SET_LIST[2] = Color.magenta;
		COLOR_SET_LIST[3] = Color.red;
		COLOR_SET_LIST[4] = Color.yellow;
		COLOR_SET_LIST[5] = Color.orange;
		COLOR_SET_LIST[6] = Color.PINK;
		COLOR_SET_LIST[7] = Color.blue;
		;

	}

	@Override
	//タイプされた時
	//文字や数字以外のキーには反応しなかったりする
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	//キーをおした時
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		if (e.getKeyChar() == 'a' && !this.isUndecorated()) {
			this.dispose();
			this.setUndecorated(true);
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
			this.setVisible(true);
		}
		if (e.getKeyChar() == 's' && this.isUndecorated()) {
			this.dispose();
			this.setUndecorated(false);
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
			this.setVisible(true);
		}
		if (e.getKeyChar() == 'd') {
			this.setColors();
		}
		if (e.getKeyChar() == 'f') {
			this.setInitStatus();
		}
	}

	@Override
	//キーを離した時
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}
}
