package jp.ac.fukushima_u.gp;

public class SynchroTest {

	private int X = 0;

	synchronized void setAdd() {
		this.X++;
		System.out.print("\n\n");
		this.notifyAll();
		try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	synchronized void getX()  {
		for (int i = 0; i < 20; i++) {
			System.out.print(this.X + " ");
		}
		System.out.print("\n");
		this.notifyAll();
		try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public SynchroTest() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		SynchroTest st = new SynchroTest();
		SynchroTestThread01 t01 = new SynchroTestThread01(st);
		SynchroTestThread02 t02 = new SynchroTestThread02(st);
		t01.start();
		t02.start();
	}

}

class SynchroTestThread01 extends Thread {
	SynchroTest st;

	SynchroTestThread01(SynchroTest st) {
		this.st = st;
	}

	public void run() {
		while (true) {
			st.setAdd();
		}
	}

}

class SynchroTestThread02 extends Thread {
	SynchroTest st;

	SynchroTestThread02(SynchroTest st) {
		this.st = st;
	}

	public void run() {
		while (true) {
			st.getX();
		}
	}
}