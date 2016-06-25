package jp.ac.fukushima_u.gp.Graphics.vacuumer;

public class VacuumerMain{

	static final int width = 800;
	static final int height = 600;

	static VacuumerWindow win;
	static UnitControl uc;
	static subThread st;

	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

		VacuumerMain.init();

		st = new subThread();

		//		�t���[�����[�g�̐�������
		st.start();

		//		�t���[�����[�g�̐����Ȃ�
		//		while(true){
		//			VacuumerMain.run();
		//		}
	}

	//���ۂɃ��[�v���镔��
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

//�ʃX���b�h�Ŏ��s���邽�߂̃N���X
//���̃N���X�ł͖������[�v�ɂ���āA��t���[��������̏����Ƃ���ɔ����ĕ`��(�̓���s�����\�b�h)�����s����Ă���
//���̖������[�v���ŁA�b��60�t���[���ɂȂ�悤�ɁA���s���x����������Ă���
class subThread extends Thread{
	//�t���[�����[�g�̌Œ�p
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
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
			}
			newTime = System.currentTimeMillis() << 16;
			error = newTime - oldTime - sleepTime;
		}
	}

}
