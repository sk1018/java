package jp.ac.fukushima_u.gp.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import jp.ac.fukushima_u.gp.template.Template;

public class GraphicsBallTest extends JFrame implements KeyListener, Cloneable, ComponentListener {

	boolean test = false;

	private int COLOR_NUM = 8;
	private boolean[] COLOR_USED = new boolean[this.COLOR_NUM];
	private Color[] COLOR_SET_LIST = new Color[this.COLOR_NUM];
	private int NODE_VELOCITY_MAX = 10;
	private int NODE_VELOCITY_MIN = 5;
	private double NODE_VEL_MAG = 0.5;

	private int BALL_NUM = 40;
	private ArrayList<Ball> BALL_SET;
	private int diameter = 10;

	private boolean GRAVITY = false;

	//ダブルバッファリング用
	Image buf;
	Graphics bufG;

	//フレームレートの固定用
	private static long error = 0;
	private static int fps = 60;
	private static long idealSleep = (1000 << 16) / fps;
	private static long oldTime;
	private static long newTime = System.currentTimeMillis() << 16;

	//	private Map<String,ArrayList<boolean>> BALL_CONTIGUOUS_STATE;

	//ボールを表すクラス
	//基本的にこのクラス1つがボール1つに対応し、
	//内部の各フィールドがそのボールの状態を表す
	private class Ball implements Cloneable {
		private double X;
		private double Y;
		private double ANGLE;
		private double VEL;
		private Color COLOR;
		private int D;

		private Ball() {

		}

		public Object clone() throws CloneNotSupportedException {
			return super.clone();
		}

	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		GraphicsBallTest gt = new GraphicsBallTest();

		gt.setVisible(true);
		gt.make();
		while (true) {
			oldTime = newTime;


			gt.ballContiguous();
			gt.ballMove();
			gt.repaint();


			newTime = System.currentTimeMillis() << 16;
			long sleepTime = idealSleep - (newTime - oldTime) - error;
			if (sleepTime < 0x20000)
				sleepTime = 0x20000;
			oldTime = newTime;
			try {
				Thread.sleep(sleepTime >> 16);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			newTime = System.currentTimeMillis() << 16;
			error = newTime - oldTime - sleepTime;
		}
	}

	/**
	 * ボールの接触を判定後、接触していたボールの方向ベクトルや速度を変更する
	 * <BR>ただし、複数同時接触なんかは考慮していないので、その場合は変な挙動を見せる
	 * <BR>具体的にはボール１と２の接触後のベクトルと、同時に接触している３との計算が始まるため
	 *
	 * <br><br>ここでの計算方法は、接触した２つのボールの速度を大きさ、方向を向きとしたベクトルとみなし、
	 * <BR>それぞれx,yの成分に分解後、合成ベクトルを算出する
	 * <BR>
	 * <BR>現時点の判定では，接触時の状態によっては
	 * <BR>「接触と判定→速度ベクトルの変更→同じボールの組が接触と判定→再度速度ベクトルの変更→……」
	 * <BR>これが繰り返されて，変な方向への跳ね返りや，接触し続ける場合がある
	 *
	 * <BR><BR>
	 */

	/*
	 * 速度ベクトルがだいたい向かい合ってる時は問題無いと思うが，ベクトルが同じ方向を向いている場合の事を考慮していなかった
	 * （例，右に10のボールに右に30のボールが接触とか）こういった場合の動作を確認する
	 *
	 * ボール同士が変な方向への跳ね返りとか，しばらくくっついたままの状態の解消法
	 *
	 * 辺挙動の原因候補その２
	 * 多重計算の疑い？
	 * ボールAとBが接触してたとして，ベクトルA，Bから接触後のベクトルS,Tを計算するわけだが，ABからSTではなく，
	 * ABからSを出し，SBからTが出ている可能性はないか？
	 *
	 * 接触判定後，速度ベクトルを更新したら
	 * 1.お互いの中心座標を非接触の位置までずらす
	 * 2.一度接触したら，全てのボールと非接触状態になるまで接触後のベクトルの更新をしない
	 * 3.ボールの組合せ毎に個別に接触状態を保持しておき，一度接触しベクトルを更新した組合せは，一度離れるまで更新を行わない
	 *
	 */
	private synchronized void ballContiguous() {
		for (int i = 0; i < this.BALL_SET.size() - 1; i++) {
			for (int l = i + 1; l < this.BALL_SET.size(); l++) {
				//二点間の距離を求める、その距離が二点の半径の和未満なら接触していると判定
				double distance = Math.pow((this.BALL_SET.get(i).X - this.BALL_SET.get(l).X), 2)
						+ Math.pow((this.BALL_SET.get(i).Y - this.BALL_SET.get(l).Y), 2);
				if (distance <= Math.pow((this.BALL_SET.get(i).D + this.BALL_SET.get(l).D),2)) {
					this.ballReflect(this.BALL_SET.get(i), this.BALL_SET.get(l));
				}

			}
		}
	}

	/* b1からb2およびその逆方向の接触ベクトルb12,b21を求める
	 * b1とb2を結ぶ線分をベクトルとみなし、それとb1,b2の速度ベクトルとの内積からコサインを求める
	 * このコサインにb1の速度ベクトルをかけたものがb1からb2に与える力になる
	 *
	 * b1ベクトルに対し，ベクトルb12を引き，b21を足す
	 * b2ベクトルに関してもベクトルが逆になるだけで同様
	 */
	private synchronized void ballReflect(Ball b1, Ball b2) {
		Ball a = null, b = null;

		try {
			a = (GraphicsBallTest.Ball) b1.clone();
			b = (GraphicsBallTest.Ball) b2.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		//b1からみたb2の相対座標
		double xd = b.X - a.X;
		double yd = b.Y - a.Y;

		//ボールの速度ベクトルと接触方向のコサインを、内積を利用して求める
		//このコサインは接触ベクトルの大きさの算出にのみ利用
		double cosa = (Math.cos(Math.toRadians(a.ANGLE))
				* a.VEL * xd + Math.sin(Math.toRadians(a.ANGLE))
				* a.VEL * yd) / (a.VEL * Math.sqrt(xd * xd + yd * yd));

		double cosb = (Math.cos(Math.toRadians(b.ANGLE))
				* b.VEL * (-xd) + Math.sin(Math.toRadians(b.ANGLE))
				* b.VEL * (-yd)) / (b.VEL * Math.sqrt(xd * xd + yd * yd));

		//コサインに速度ベクトルの大きさをかけて、接触方向のベクトルの大きさを算出
		double vectorSizea = a.VEL * cosa;
		double vectorSizeb = b.VEL * cosb;

		//三時方向の単位ベクトルとのコサインを求める
		//これに大きさをかけたものがX座標で、そのサインをかけたものがY座標。ただしYは正負が不明
		double cosa0 = (xd * 1) / Math.sqrt(xd * xd + yd * yd) * 1;
		double cosb0 = ((-xd) * 1) / Math.sqrt(xd * xd + yd * yd) * 1;

		//ベクトルb12,b21の座標系を算出
		double b12X = cosa0 * vectorSizea;
		double b12Y = Math.sqrt(1 - Math.pow(cosa0, 2)) * vectorSizea * (yd / Math.abs(yd));

		double b21X = cosb0 * vectorSizeb;
		double b21Y = Math.sqrt(1 - Math.pow(cosb0, 2)) * vectorSizeb * (-(yd / Math.abs(yd)));

		//元ベクトルを座標に変換し、それに接触ベクトルの座標を加算し、合成ベクトルの座標を求める
		double newb1X = a.VEL * Math.cos(Math.toRadians(a.ANGLE)) - b12X + b21X;
		double newb1Y = a.VEL * Math.sin(Math.toRadians(a.ANGLE)) - b12Y + b21Y;

		double newb2X = b.VEL * Math.cos(Math.toRadians(b.ANGLE)) - b21X + b12X;
		double newb2Y = b.VEL * Math.sin(Math.toRadians(b.ANGLE)) - b21Y + b12Y;

		//接触ベクトルの座標から角度を算出
		//ただし、-PI/2からPI/2までなので、0から2PIの形に調整が必要
		//また，これはラジアンで，アングルの方は度数なので，実際に更新する際には変換が必要
		double newb1Angle = this.tangentSignCheck(Math.atan(newb1Y / newb1X), newb1X, newb1Y);
		double newb2Angle = this.tangentSignCheck(Math.atan(newb2Y / newb2X), newb2X, newb2Y);

		newb1Angle = Math.toDegrees(newb1Angle);
		newb2Angle = Math.toDegrees(newb2Angle);

		//求めたアングルを新しいものとして更新
		//速度に関しては新しいX,Yの両座標から三平方の定理により算出
		b1.ANGLE = newb1Angle;
		b1.VEL = Math.sqrt(Math.pow(newb1X, 2) + Math.pow(newb1Y, 2));

		b2.ANGLE = newb2Angle;
		b2.VEL = Math.sqrt(Math.pow(newb2X, 2) + Math.pow(newb2Y, 2));

		//新しい速度から，更に１フレーム分移動した座標に変更する
//		b1.X += b1.VEL * Math.cos(b1.ANGLE);
//		b1.Y -= b1.VEL * Math.sin(b1.ANGLE);
//
//		b2.X += b2.VEL * Math.cos(b1.ANGLE);
//		b2.Y -= b2.VEL * Math.sin(b1.ANGLE);


		b1.X += b1.VEL * Math.cos(b1.ANGLE);
		b1.Y -= b1.VEL * Math.sin(b1.ANGLE);

		b2.X += b2.VEL * Math.cos(b1.ANGLE);
		b2.Y -= b2.VEL * Math.sin(b1.ANGLE);
	}

	//-PI/2からPI/2までのラジアンを，アークタンジェントの元にした座標から，適切な0から2PIに変換する
	public double tangentSignCheck(double rad, double x, double y) {
		if (rad >= 0) {
			if (x >= 0 && y >= 0) {
				return rad;
			}
			else {
				return rad + Math.PI;
			}
		}
		else {
			if (x >= 0) {
				return rad + Math.PI;
			}
			else {
				return rad + Math.PI * 2;
			}
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

	//ここからダブルバッファリング
	public void make() {
		buf = createImage(this.getSize().width, this.getSize().height);
		bufG = buf.getGraphics();
	}

	// 再描画
	public void update(Graphics g) {
		paint(g);
	}

	public synchronized void paint(Graphics g) {
		Dimension size = getSize();

		bufG.setColor(Color.black);
		bufG.fillRect(0, 0, size.width, size.height);

		for (int i = 0; i < this.BALL_SET.size(); i++) {
			Ball ball = this.BALL_SET.get(i);
			bufG.setColor(ball.COLOR);
			bufG.fillOval((int) ball.X, (int) ball.Y, ball.D, ball.D);
		}

		//g.dispose();
		g.drawImage(buf, 0, 0, this);

	}

	/**
	 * Javaの座標は左上の原点として、右方向にX,下方向にYが増える
	 * <BR>ここでは、三時方向を0度として、反時計回りに角度を設定し、その角度の三角関数で移動量を求めている
	 * <BR>Y軸に関しては、三角関数と軸の正負の向きが逆なので、加算ではなく減算を行っている
	 */
	private synchronized void ballMove() {
		for (int i = 0; i < this.BALL_SET.size(); i++) {
			Ball ball = this.BALL_SET.get(i);
			ball.X += Math.cos(Math.toRadians(ball.ANGLE))
					* ball.VEL;
			ball.Y -= Math.sin(Math.toRadians(ball.ANGLE))
					* ball.VEL;

			if (this.GRAVITY) {
				double x = Math.cos(Math.toRadians(ball.ANGLE)) * ball.VEL;
				double y = Math.sin(Math.toRadians(ball.ANGLE)) * ball.VEL - 0.1;

				ball.ANGLE = Math.toDegrees(Math.atan2(y, x));
				ball.VEL = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

			}

			if (test) {
				Ball b;
				b = this.BALL_SET.get(0);
				b.X = this.getSize().width / 2;
				b.Y = this.getSize().height / 2;
			}

			Dimension size = getSize();
			if (ball.X < 0) {
				ball.X = 0;
				ball.ANGLE = 180.0 - ball.ANGLE;
			}
			else if (ball.X > size.width) {
				ball.X = size.width;
				ball.ANGLE = 180.0 - ball.ANGLE;
			}
			int n = 15;
			if (ball.Y < n) {
				ball.Y = n;
				ball.ANGLE = -ball.ANGLE;
			}
			else if (ball.Y > size.height) {
				ball.Y = size.height;
				ball.ANGLE = -ball.ANGLE;

			}
		}

	}

	private GraphicsBallTest() {
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.getContentPane().setBackground(Color.black);

		this.BALL_SET = new ArrayList<Ball>();

		this.setInitColors();
		this.setInitStatus();
		this.addKeyListener(this);
		this.addComponentListener(this);

		this.setTitle("screen saver");
		//Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\GP\\Desktop\\seiya\\Dropbox\\pro\\java\\jp\\ac\\fukushima_u\\gp\\materials\\test\\color-wheel-icon.jpg");
		Image icon = Toolkit.getDefaultToolkit().getImage(
				Template.getCLASSPATH() + "jp\\ac\\fukushima_u\\gp\\materials\\test\\color-wheel-icon.jpg");
		this.setIconImage(icon);

	}

	private void setInitStatus() {
		for (int i = 0; i < this.BALL_NUM; i++) {
			this.BALL_SET.add(this.getNewBall());
		}

	}

	private Ball getNewBall() {
		Random r = new Random();
		Ball ball;
		ball = new Ball();
		ball.X = r.nextInt(this.getSize().width);
		ball.Y = r.nextInt(this.getSize().height);
		ball.ANGLE = r.nextDouble() * 360;
		ball.VEL = (r.nextInt(this.NODE_VELOCITY_MAX - this.NODE_VELOCITY_MIN)
				+ this.NODE_VELOCITY_MIN) * this.NODE_VEL_MAG;
		ball.COLOR = this.setColor();
		ball.D = this.diameter;

		return ball;

	}

	private Color setColor() {
		for (int i = 0; i < this.COLOR_NUM; i++) {
			if (COLOR_USED[i])
				break;
			if (i == this.COLOR_NUM - 1) {
				for (int l = 0; l < this.COLOR_NUM; l++) {
					this.COLOR_USED[i] = false;
				}
			}
		}

		boolean f = true;
		for (int i = 0; i < this.COLOR_NUM; i++) {
			if (!this.COLOR_USED[i])
				f = false;
		}
		if (f) {
			for (int i = 0; i < this.COLOR_NUM; i++) {
				this.COLOR_USED[i] = false;
			}
		}

		Random r = new Random();
		int t;
		do {
			t = r.nextInt(COLOR_NUM);
		} while (this.COLOR_USED[t]);
		this.COLOR_USED[t] = true;

		return this.COLOR_SET_LIST[t];
	}

	private void setInitColors() {
		for (int i = 0; i < this.COLOR_NUM; i++) {
			this.COLOR_USED[i] = false;
		}
		COLOR_SET_LIST[0] = Color.cyan;
		COLOR_SET_LIST[1] = Color.green;
		COLOR_SET_LIST[2] = Color.magenta;
		COLOR_SET_LIST[3] = Color.red;
		COLOR_SET_LIST[4] = Color.yellow;
		COLOR_SET_LIST[5] = Color.orange;
		COLOR_SET_LIST[6] = Color.PINK;
		COLOR_SET_LIST[7] = Color.blue;

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
		}
		if (e.getKeyChar() == 'f') {
			this.setInitStatus();
		}
		if (e.getKeyChar() == 'z') {
			this.BALL_SET.add(getNewBall());
		}
		if (e.getKeyChar() == 'x') {
			if (!this.BALL_SET.isEmpty())
				this.BALL_SET.remove(this.BALL_SET.size() - 1);
		}
	}

	@Override
	//キーを離した時
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		buf.flush();
		bufG.dispose();
		this.make();

		System.out.println("test");

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
