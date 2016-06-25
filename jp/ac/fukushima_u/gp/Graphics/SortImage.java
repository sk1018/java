package jp.ac.fukushima_u.gp.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import jp.ac.fukushima_u.gp.template.Template;

public class SortImage extends JFrame implements KeyListener, ActionListener {

	private int NODE_NUM = 40;
	private int[] NODE_SET = new int[this.NODE_NUM];
	private int RANGE = 5;
	private int CHANGE_A = -1;
	private int CHANGE_B = -1;

	private int P = 0;
	private int S = 0;
	private int S1 = 0;
	private int T = 0;
	private int T1 = 0;

	static final int BUBBLE_SORT = 0;
	static final int QUICK_SORT = 1;
	static final int MERGE_SORT = 2;
	private int SORT_NUM = 2;

	private static int SLEEP_TIME = 10;

	private JButton jb0;
	private boolean FLAG = false;

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		SortImage gt = new SortImage();

//		gt.utilityWindow();

		//gt.setUndecorated(true);
		gt.setVisible(true);
		while (true) {
			gt.setInitStatus();

			switch (gt.SORT_NUM) {
				case BUBBLE_SORT:
					gt.nodeBubbleSort();
					break;

				case QUICK_SORT:
					gt.quick(1, 0, gt.NODE_NUM - 1, gt.NODE_SET);
					break;

				case MERGE_SORT:
					//gt.mergeSort(0, gt.NODE_NUM - 1, gt.NODE_SET);
					gt.merge(gt.NODE_SET, 0, gt.NODE_NUM - 1, gt.NODE_NUM);
					break;

				default:
					break;
			}
			gt.sleep(1000);
			gt.repaint();
		}
	}

	private void utilityWindow() {
		JFrame win = new JFrame();
		Dimension d = new Dimension(300, 200);
		win.setSize(d);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		win.getContentPane().setLayout(new GridLayout(2, 1));
		Panel p1 = new Panel();
		Panel p2 = new Panel();

		win.add(p1);
		win.add(p2);

		JRadioButton jr0 = new JRadioButton("BUBBLE");
		JRadioButton jr1 = new JRadioButton("QUICK");
		JRadioButton jr2 = new JRadioButton("MERGE");

		ButtonGroup bg = new ButtonGroup();
		bg.add(jr0);
		bg.add(jr1);
		bg.add(jr2);

		p1.add(jr0);
		p1.add(jr1);
		p1.add(jr2);

		jb0 = new JButton("OK");
		p2.add(jb0);
		jb0.addActionListener(this);

		win.setVisible(true);

		while (!this.FLAG || this.SORT_NUM == -1) {
			if (jr0.isSelected())
				this.SORT_NUM = SortImage.BUBBLE_SORT;
			else if (jr1.isSelected())
				this.SORT_NUM = SortImage.QUICK_SORT;
			else if (jr2.isSelected())
				this.SORT_NUM = SortImage.MERGE_SORT;

		}
		win.setVisible(false);
	}

	//nodeのソートを一回だけ
	//もしくは，mainにあるスリープとリペイントを消して，こっちでリペイント
	public synchronized void nodeBubbleSort() {
		for (int i = 0; i < this.NODE_NUM; i++) {
			for (int l = 0; l + 1 < this.NODE_NUM; l++) {

				this.sleep(SLEEP_TIME);
				this.repaint();

				if (this.NODE_SET[l] > this.NODE_SET[l + 1]) {
					this.CHANGE_A = l;
					this.CHANGE_B = l + 1;

					int t = this.NODE_SET[l];
					this.NODE_SET[l] = this.NODE_SET[l + 1];
					this.NODE_SET[l + 1] = t;
				}
			}
		}

	}

	/**
	 *
	 * @param a 配列の先頭
	 * @param b 配列の最後（size-1みたいな）
	 * @param in 配列そのもの
	 */
	public void mergeSort(int a, int b, int[] in) {
		if (a == b)
			return;

		int p = (b - a) / 2 + a + 1;

		this.mergeSort(a, p - 1, in);
		this.mergeSort(p, b, in);

		this.sleep(100);
		this.repaint();

		int fa = 0, fb = 0, fc = 0;
		int[] c = new int[b - a + 1];

		int[] aa, bb;
		aa = new int[p - a];
		bb = new int[b - p + 1];

		int i, l;
		for (i = a, l = 0; i <= b; i++, l++) {

			if (i == p) {
				l = 0;
			}
			if (i < p) {
				aa[l] = in[i];
			}
			else {
				bb[l] = in[i];
			}
		}

		S = a;
		T = b;

		while (fc < b - a + 1) {

			if (a + fa >= p) {
				c[fc] = bb[fb];
				fc++;
				fb++;
			}
			else if (p + fb > b) {
				c[fc] = aa[fa];
				fc++;
				fa++;
			}
			else {
				if (in[a + fa] < in[p + fb]) {
					c[fc] = aa[fa];
					fc++;
					fa++;
				}
				else {
					c[fc] = bb[fb];
					fc++;
					fb++;
				}
			}
			in[a + fc - 1] = c[fc - 1];
		}
	}

	public void merge(int[] in, int a, int b, int data) {
		int i, n, ac, bc, c;
		int[] o;

		//データ数が１の時
		if (a == b)
			return;

		o = new int[data];

		n = (a + b + 1) / 2;

		merge(in, a, n - 1, data);
		merge(in, n, b, data);

		ac = a;
		bc = n;
		c = a;

		//配列inをoにコピー
		for (i = a; i <= b; i++)
			o[i] = in[i];

		while (ac <= n - 1 || bc <= b) {

			S = a;
			T = b;

			//配列Aが0の時
			if (ac > n - 1) {
				in[c++] = o[bc++];
				continue;
			}
			//配列Bが0の時
			if (bc > b) {
				in[c++] = o[ac++];
				continue;
			}

			//両方ある時
			if (o[ac] < o[bc]) {
				in[c++] = o[ac++];
				continue;
			}
			else {
				in[c++] = o[bc++];
				continue;
			}

		}

		this.sleep(800);
		this.repaint();

		return;
	}

	public void quick(int f, int a, int b, int[] in) {

		int s, t;
		int p, tmp;
		S = a;
		T = b;

		if (a >= b)
			return;

		p = (in[a] + in[b]) / 2;
		s = a;
		t = b;
		while (true) {
			while (in[s] < p * f) {
				s++;
				S1 = s;
				this.repaint();
			}
			while (in[t] > p * f) {
				t--;
				T1 = t;
				this.repaint();
			}
			if (t <= s)
				break;

			tmp = in[s];
			in[s] = in[t];
			in[t] = tmp;

			this.P = p;

			this.CHANGE_A = s;
			this.CHANGE_B = t;

			this.sleep(1000);
			this.repaint();

			s++;
			t--;
		}

		quick(f, a, s - 1, in);
		quick(f, t + 1, b, in);

		return;
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
		//windowサイズの幅-20をノード数で割ったものがノード間のスペース
		//-20はWindowの両端から少し距離を取るため
		int space = (size.width - 20) / (this.NODE_NUM - 1);

		g.setColor(Color.white);
		g.fillRect(0, 0, size.width, size.height);

		//quickの場合のみ
		if (this.SORT_NUM == SortImage.QUICK_SORT) {
			g.setColor(Color.black);

			g.drawLine(0, size.height - (P * (size.height - 20) / this.NODE_NUM + 10) - 5, size.width,
					size.height - (P * (size.height - 20) / this.NODE_NUM + 10) - 5);

			g.drawLine(space * S + 10 + this.RANGE / 2, 0, space * S + 10 + this.RANGE / 2, size.height);
			g.drawLine(space * T + 10 + this.RANGE / 2, 0, space * T + 10 + this.RANGE / 2, size.height);

			g.drawLine(space * S1 + 10 + this.RANGE / 2, 0, space * S1 + 10 + this.RANGE / 2, size.height);
			g.drawLine(space * T1 + 10 + this.RANGE / 2, 0, space * T1 + 10 + this.RANGE / 2, size.height);
		}

		//mergeの場合のみ
		else if (this.SORT_NUM == SortImage.MERGE_SORT) {
			g.setColor(Color.black);
			g.drawLine(space * S + 10 + this.RANGE / 2, 0, space * S + 10 + this.RANGE / 2, size.height);
			g.drawLine(space * T + 10 + this.RANGE / 2, 0, space * T + 10 + this.RANGE / 2, size.height);
		}

		for (int i = 0; i < this.NODE_NUM; i++) {

			if (i == this.CHANGE_A) {
				g.setColor(Color.blue);
				this.CHANGE_A = -1;
			}
			else if (i == this.CHANGE_B) {
				g.setColor(Color.red);
				this.CHANGE_B = -1;
			}
			else {
				g.setColor(Color.black);
			}

			int x = space * i + 10;
			int y = size.height - (((this.NODE_SET[i] + 1) * ((size.height - 25) / this.NODE_NUM))) - 5;

			g.fillOval(x, y, this.RANGE, this.RANGE);
			g.fillRect(x, y, this.RANGE, size.height - y);
		}

		g.dispose();

	}

	private SortImage() {

		this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.getContentPane().setBackground(Color.black);

		this.setInitStatus();
		this.addKeyListener(this);

		this.setTitle("screen saver");
		//Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\GP\\Desktop\\seiya\\Dropbox\\pro\\java\\jp\\ac\\fukushima_u\\gp\\materials\\test\\color-wheel-icon.jpg");
		Image icon = Toolkit.getDefaultToolkit().getImage(
				Template.getCLASSPATH() + "jp\\ac\\fukushima_u\\gp\\materials\\test\\color-wheel-icon.jpg");
		this.setIconImage(icon);
	}

	private void setInitStatus() {
		Random r = new Random();

		for (int i = 0; i < this.NODE_NUM; i++) {
			this.NODE_SET[i] = -1;
		}

		int t;
		for (int i = 0; i < this.NODE_NUM; i++) {
			while (this.NODE_SET[t = r.nextInt(NODE_NUM)] >= 0) {
			}
			this.NODE_SET[t] = i;
		}

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

	}

	@Override
	//キーを離した時
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		if (jb0.equals(e.getSource())) {
			this.FLAG = true;


		}

	}
}
