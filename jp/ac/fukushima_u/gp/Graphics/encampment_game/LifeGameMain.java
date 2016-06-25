package jp.ac.fukushima_u.gp.Graphics.encampment_game;

import java.awt.Color;

public class LifeGameMain {

	private LifeGameWin win;
	private CellControl cc;
	private int CELL_SIZE = 1;//�����`�Ȃ̂�
//	private int CELL_X = this.width / this.CELL_SIZE;//xell�̉��̐�
//	private int CELL_Y = this.height / this.CELL_SIZE;//cell�̏c�̐�
	
	public static final boolean COLOR_RANDOM=true;
	public static final Color DEFAULT_COLOR=Color.yellow;

	private int width = 1000;//pane�̃T�C�Y
	private int height = 800;//pane�̃T�C�Y

	private double init_birth = 0.0001;

	static boolean flag = true;

	//�t���[�����[�g�̌Œ�p
	private static long error = 0;
	static int fps = 60;
	private static long idealSleep = (1000 << 16) / fps;
	private static long oldTime;
	private static long newTime = System.currentTimeMillis() << 16;
	private static LifeGameMain main;

	public static void main(String[] args) {

		main = new LifeGameMain();

		LifeGameMain.exec();
	}

	public static void exec() {
		while (true) {
			if (!flag)
				break;

			oldTime = newTime;

			main.run();

			newTime = System.currentTimeMillis() << 16;
			idealSleep = (1000 << 16) / fps;
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

	void run() {
		cc.nextStep();
		win.repaint();
	}

	private LifeGameMain() {
		this.init();
	}

	private void init() {
		win = new LifeGameWin(this.width, this.height);
		win.setVisible(true);

		cc = new CellControl(this.width / this.CELL_SIZE, this.height / this.CELL_SIZE, this.CELL_SIZE);
		cc.setLife(this.init_birth);
		cc.setColor(COLOR_RANDOM);

		win.setCellControl(cc);
		win.setLifeGameMain(this);
		win.make();
	}

}
