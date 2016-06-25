package jp.ac.fukushima_u.gp.Graphics.lifegame;

import java.awt.Color;

public class LifeGameMain {

	private LifeGameWin win;
	private CellControl cc;
	private int CELL_SIZE = 5;//正方形なので
	//	private int CELL_X = this.width / this.CELL_SIZE;//xellの横の数
	//	private int CELL_Y = this.height / this.CELL_SIZE;//cellの縦の数

	private boolean random_flag = true;
	private double random_mag1 = 0.001;
	private double random_mag2 = 0.01;

	public static final boolean COLOR_RANDOM = true;
	public static final Color DEFAULT_COLOR = Color.yellow;

	private int width = 1000;//paneのサイズ
	private int height = 800;//paneのサイズ

	static double init_birth = 0.3;

	static boolean flag = true;

	private static LifeGameMain main;
	static subThread st;

	public static void main(String[] args) {

		main = new LifeGameMain();

		st = new subThread(main);

		st.start();

	}

	void run() {
		cc.nextStep();
		win.repaint();
		if (this.random_flag) {
			cc.cellRandomBurst(random_mag1, random_mag2);
		}
	}

	private LifeGameMain() {
		this.init();
	}

	private void init() {
		win = new LifeGameWin(this.width, this.height);
		win.setVisible(true);

		cc = new CellControl(this.width / this.CELL_SIZE, this.height / this.CELL_SIZE, this.CELL_SIZE);
		cc.setLife(LifeGameMain.init_birth);
		cc.setColor(COLOR_RANDOM);

		win.setCellControl(cc);
		win.setLifeGameMain(this);
		win.make();
	}

}

class subThread extends Thread {
	//フレームレートの固定用
	private long error = 0;
	int fps = 60;
	private long idealSleep = (1000 << 16) / fps;
	private long oldTime;
	private long newTime = System.currentTimeMillis() << 16;

	boolean startFlag=false;

	LifeGameMain main;

	subThread() {

	}

	subThread(LifeGameMain main) {
		this.main = main;
	}

	public void run() {
		this.startFlag=true;
		while (true) {

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
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			newTime = System.currentTimeMillis() << 16;
			error = newTime - oldTime - sleepTime;
		}
	}

}
