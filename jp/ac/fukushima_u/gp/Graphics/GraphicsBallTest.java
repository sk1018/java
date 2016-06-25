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

	//�_�u���o�b�t�@�����O�p
	Image buf;
	Graphics bufG;

	//�t���[�����[�g�̌Œ�p
	private static long error = 0;
	private static int fps = 60;
	private static long idealSleep = (1000 << 16) / fps;
	private static long oldTime;
	private static long newTime = System.currentTimeMillis() << 16;

	//	private Map<String,ArrayList<boolean>> BALL_CONTIGUOUS_STATE;

	//�{�[����\���N���X
	//��{�I�ɂ��̃N���X1���{�[��1�ɑΉ����A
	//�����̊e�t�B�[���h�����̃{�[���̏�Ԃ�\��
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
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
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
			}
			newTime = System.currentTimeMillis() << 16;
			error = newTime - oldTime - sleepTime;
		}
	}

	/**
	 * �{�[���̐ڐG�𔻒��A�ڐG���Ă����{�[���̕����x�N�g���⑬�x��ύX����
	 * <BR>�������A���������ڐG�Ȃ񂩂͍l�����Ă��Ȃ��̂ŁA���̏ꍇ�͕ςȋ�����������
	 * <BR>��̓I�ɂ̓{�[���P�ƂQ�̐ڐG��̃x�N�g���ƁA�����ɐڐG���Ă���R�Ƃ̌v�Z���n�܂邽��
	 *
	 * <br><br>�����ł̌v�Z���@�́A�ڐG�����Q�̃{�[���̑��x��傫���A�����������Ƃ����x�N�g���Ƃ݂Ȃ��A
	 * <BR>���ꂼ��x,y�̐����ɕ�����A�����x�N�g�����Z�o����
	 * <BR>
	 * <BR>�����_�̔���ł́C�ڐG���̏�Ԃɂ���Ă�
	 * <BR>�u�ڐG�Ɣ��聨���x�x�N�g���̕ύX�������{�[���̑g���ڐG�Ɣ��聨�ēx���x�x�N�g���̕ύX���c�c�v
	 * <BR>���ꂪ�J��Ԃ���āC�ςȕ����ւ̒��˕Ԃ��C�ڐG��������ꍇ������
	 *
	 * <BR><BR>
	 */

	/*
	 * ���x�x�N�g�����������������������Ă鎞�͖�薳���Ǝv�����C�x�N�g�������������������Ă���ꍇ�̎����l�����Ă��Ȃ�����
	 * �i��C�E��10�̃{�[���ɉE��30�̃{�[�����ڐG�Ƃ��j�����������ꍇ�̓�����m�F����
	 *
	 * �{�[�����m���ςȕ����ւ̒��˕Ԃ�Ƃ��C���΂炭���������܂܂̏�Ԃ̉����@
	 *
	 * �Ӌ����̌�����₻�̂Q
	 * ���d�v�Z�̋^���H
	 * �{�[��A��B���ڐG���Ă��Ƃ��āC�x�N�g��A�CB����ڐG��̃x�N�g��S,T���v�Z����킯�����CAB����ST�ł͂Ȃ��C
	 * AB����S���o���CSB����T���o�Ă���\���͂Ȃ����H
	 *
	 * �ڐG�����C���x�x�N�g�����X�V������
	 * 1.���݂��̒��S���W���ڐG�̈ʒu�܂ł��炷
	 * 2.��x�ڐG������C�S�Ẵ{�[���Ɣ�ڐG��ԂɂȂ�܂ŐڐG��̃x�N�g���̍X�V�����Ȃ�
	 * 3.�{�[���̑g�������ɌʂɐڐG��Ԃ�ێ����Ă����C��x�ڐG���x�N�g�����X�V�����g�����́C��x�����܂ōX�V���s��Ȃ�
	 *
	 */
	private synchronized void ballContiguous() {
		for (int i = 0; i < this.BALL_SET.size() - 1; i++) {
			for (int l = i + 1; l < this.BALL_SET.size(); l++) {
				//��_�Ԃ̋��������߂�A���̋�������_�̔��a�̘a�����Ȃ�ڐG���Ă���Ɣ���
				double distance = Math.pow((this.BALL_SET.get(i).X - this.BALL_SET.get(l).X), 2)
						+ Math.pow((this.BALL_SET.get(i).Y - this.BALL_SET.get(l).Y), 2);
				if (distance <= Math.pow((this.BALL_SET.get(i).D + this.BALL_SET.get(l).D),2)) {
					this.ballReflect(this.BALL_SET.get(i), this.BALL_SET.get(l));
				}

			}
		}
	}

	/* b1����b2����т��̋t�����̐ڐG�x�N�g��b12,b21�����߂�
	 * b1��b2�����Ԑ������x�N�g���Ƃ݂Ȃ��A�����b1,b2�̑��x�x�N�g���Ƃ̓��ς���R�T�C�������߂�
	 * ���̃R�T�C����b1�̑��x�x�N�g�������������̂�b1����b2�ɗ^����͂ɂȂ�
	 *
	 * b1�x�N�g���ɑ΂��C�x�N�g��b12�������Cb21�𑫂�
	 * b2�x�N�g���Ɋւ��Ă��x�N�g�����t�ɂȂ邾���œ��l
	 */
	private synchronized void ballReflect(Ball b1, Ball b2) {
		Ball a = null, b = null;

		try {
			a = (GraphicsBallTest.Ball) b1.clone();
			b = (GraphicsBallTest.Ball) b2.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		//b1����݂�b2�̑��΍��W
		double xd = b.X - a.X;
		double yd = b.Y - a.Y;

		//�{�[���̑��x�x�N�g���ƐڐG�����̃R�T�C�����A���ς𗘗p���ċ��߂�
		//���̃R�T�C���͐ڐG�x�N�g���̑傫���̎Z�o�ɂ̂ݗ��p
		double cosa = (Math.cos(Math.toRadians(a.ANGLE))
				* a.VEL * xd + Math.sin(Math.toRadians(a.ANGLE))
				* a.VEL * yd) / (a.VEL * Math.sqrt(xd * xd + yd * yd));

		double cosb = (Math.cos(Math.toRadians(b.ANGLE))
				* b.VEL * (-xd) + Math.sin(Math.toRadians(b.ANGLE))
				* b.VEL * (-yd)) / (b.VEL * Math.sqrt(xd * xd + yd * yd));

		//�R�T�C���ɑ��x�x�N�g���̑傫���������āA�ڐG�����̃x�N�g���̑傫�����Z�o
		double vectorSizea = a.VEL * cosa;
		double vectorSizeb = b.VEL * cosb;

		//�O�������̒P�ʃx�N�g���Ƃ̃R�T�C�������߂�
		//����ɑ傫�������������̂�X���W�ŁA���̃T�C�������������̂�Y���W�B������Y�͐������s��
		double cosa0 = (xd * 1) / Math.sqrt(xd * xd + yd * yd) * 1;
		double cosb0 = ((-xd) * 1) / Math.sqrt(xd * xd + yd * yd) * 1;

		//�x�N�g��b12,b21�̍��W�n���Z�o
		double b12X = cosa0 * vectorSizea;
		double b12Y = Math.sqrt(1 - Math.pow(cosa0, 2)) * vectorSizea * (yd / Math.abs(yd));

		double b21X = cosb0 * vectorSizeb;
		double b21Y = Math.sqrt(1 - Math.pow(cosb0, 2)) * vectorSizeb * (-(yd / Math.abs(yd)));

		//���x�N�g�������W�ɕϊ����A����ɐڐG�x�N�g���̍��W�����Z���A�����x�N�g���̍��W�����߂�
		double newb1X = a.VEL * Math.cos(Math.toRadians(a.ANGLE)) - b12X + b21X;
		double newb1Y = a.VEL * Math.sin(Math.toRadians(a.ANGLE)) - b12Y + b21Y;

		double newb2X = b.VEL * Math.cos(Math.toRadians(b.ANGLE)) - b21X + b12X;
		double newb2Y = b.VEL * Math.sin(Math.toRadians(b.ANGLE)) - b21Y + b12Y;

		//�ڐG�x�N�g���̍��W����p�x���Z�o
		//�������A-PI/2����PI/2�܂łȂ̂ŁA0����2PI�̌`�ɒ������K�v
		//�܂��C����̓��W�A���ŁC�A���O���̕��͓x���Ȃ̂ŁC���ۂɍX�V����ۂɂ͕ϊ����K�v
		double newb1Angle = this.tangentSignCheck(Math.atan(newb1Y / newb1X), newb1X, newb1Y);
		double newb2Angle = this.tangentSignCheck(Math.atan(newb2Y / newb2X), newb2X, newb2Y);

		newb1Angle = Math.toDegrees(newb1Angle);
		newb2Angle = Math.toDegrees(newb2Angle);

		//���߂��A���O����V�������̂Ƃ��čX�V
		//���x�Ɋւ��Ă͐V����X,Y�̗����W����O�����̒藝�ɂ��Z�o
		b1.ANGLE = newb1Angle;
		b1.VEL = Math.sqrt(Math.pow(newb1X, 2) + Math.pow(newb1Y, 2));

		b2.ANGLE = newb2Angle;
		b2.VEL = Math.sqrt(Math.pow(newb2X, 2) + Math.pow(newb2Y, 2));

		//�V�������x����C�X�ɂP�t���[�����ړ��������W�ɕύX����
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

	//-PI/2����PI/2�܂ł̃��W�A�����C�A�[�N�^���W�F���g�̌��ɂ������W����C�K�؂�0����2PI�ɕϊ�����
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
	{ //�w��~���b���s���~�߂郁�\�b�h
		try
		{
			wait(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//��������_�u���o�b�t�@�����O
	public void make() {
		buf = createImage(this.getSize().width, this.getSize().height);
		bufG = buf.getGraphics();
	}

	// �ĕ`��
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
	 * Java�̍��W�͍���̌��_�Ƃ��āA�E������X,��������Y��������
	 * <BR>�����ł́A�O��������0�x�Ƃ��āA�����v���Ɋp�x��ݒ肵�A���̊p�x�̎O�p�֐��ňړ��ʂ����߂Ă���
	 * <BR>Y���Ɋւ��ẮA�O�p�֐��Ǝ��̐����̌������t�Ȃ̂ŁA���Z�ł͂Ȃ����Z���s���Ă���
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
	//�^�C�v���ꂽ��
	//�����␔���ȊO�̃L�[�ɂ͔������Ȃ������肷��
	public void keyTyped(KeyEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

	@Override
	//�L�[����������
	public void keyPressed(KeyEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
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
	//�L�[�𗣂�����
	public void keyReleased(KeyEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		buf.flush();
		bufG.dispose();
		this.make();

		System.out.println("test");

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

}
