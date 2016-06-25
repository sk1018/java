package jp.ac.fukushima_u.gp.Graphics.vacuumer;

public class VacuumerMain{

	static final int width = 800;
	static final int height = 600;

	static VacuumerWindow win;
	static UnitControl uc;
	static subThread st;

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		VacuumerMain.init();

		st = new subThread();

		//		フレームレートの制限あり
		st.start();

		//		フレームレートの制限なし
		//		while(true){
		//			VacuumerMain.run();
		//		}
	}

	//実際にループする部分
	public static void run() {
		uc.step();
		win.repaint();
	}

	public static void init() {
		win = new VacuumerWindow(VacuumerMain.width, VacuumerMain.height);
		win.setVisible(true);
		win.make();

		uc = new UnitControl();

		win.setUnitControl(uc);
	}

}

//別スレッドで実行するためのクラス
//このクラスでは無限ループによって、一フレームあたりの処理とそれに伴う再描画(の二つを行うメソッド)が実行されている
//その無限ループ中で、秒間60フレームになるように、実行速度が制限されている
class subThread extends Thread{
	//フレームレートの固定用
	private long error = 0;
	int fps = 60;
	private long idealSleep = (1000 << 16) / fps;
	private long oldTime;
	private long newTime = System.currentTimeMillis() << 16;

	boolean startFlag = false;

	subThread() {

	}

	public void run() {
		this.startFlag = true;
		while (true) {

			oldTime = newTime;

			VacuumerMain.run();

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
