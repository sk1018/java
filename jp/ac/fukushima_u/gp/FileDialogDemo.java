package jp.ac.fukushima_u.gp;

//
//FileDialog�̃f����
//
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileDialogDemo{
	public static void main(String args[]) {
		//�A�v���͂�������n�܂�܂��B
		new WindowTest();
	}
}

//
//WindowTest �N���X��, Window�̕\�����s���܂��B
//
//Window�̃C�x���g�������̗�
//
class WindowTest extends Frame implements WindowListener{
	//�R���X�g���N�^
	WindowTest() {
		//FileDialog�ɂ��t�@�C���̎擾
		FileDialog fileDialog = new FileDialog(this);//FileDialog�̍쐬
		fileDialog.setVisible(true);//�\������
		String dir = fileDialog.getDirectory();//�f�B���N�g���[�̎擾
		String fileName = fileDialog.getFile();//File���̎擾
		if (fileName == null)
			System.exit(0);//�t�@�C�����̐ݒ肪������Ώ������~
			
		//�w�肵���t�@�C������f�[�^��ǂݎ��\��
		try {//�t�@�C�������g���ɂ́A��O�������K�v
		
			String s;//�ǂݍ��񂾃f�[�^��ێ����镶����
			FileReader rd = new FileReader(dir + fileName);//�ǂݎ��p�Ƃ��āA�t�@�C���ƃA�v�����q��
			BufferedReader br = new BufferedReader(rd);//BufferdReader�̍쐬
			
			s = br.readLine();//�ŏ���1�s��ǂݍ���
			while (s != null) {
				add(new Label(s));//�ǂݍ��񂾕���������x���ŕ\��
				s = br.readLine();//����1�s��ǂ�
			}
			
			br.close();//����
			rd.close();
			
		} catch (IOException e) {
			//�G���[������������ �G���[��\��
			System.out.println("Err=" + e);
		}
		
		//Window��ݒ肵�\��
		setSize(240, 240);//�T�C�Y���w�肷
		setLayout(new GridLayout(20, 1));//1��~20�s�̃��C�A�E�g��ݒ�
		addWindowListener(this);//WindowListener��ݒ�
		setVisible(true);//��������
		
	}
	
	//�A�N�e�B�u�ɂȂ������̏���
	public void windowActivated(java.awt.event.WindowEvent e) {
		System.out.println("Activated");
		
	}
	
	//����ꂽ���̏���
	public void windowClosed(java.awt.event.WindowEvent e) {
		System.out.println("Closed");
		
	}
	
	//�����Ă��鎞�̏���
	public void windowClosing(java.awt.event.WindowEvent e) {
		System.out.println("Closing");
		System.exit(0);
	}
	
	//�A�N�e�B�u�łȂ��Ȃ����Ƃ��̏���
	public void windowDeactivated(java.awt.event.WindowEvent e) {
		System.out.println("Deactivaed");
		
	}
	
	//�A�C�R������߂����Ƃ��̏���
	public void windowDeiconified(java.awt.event.WindowEvent e) {
		System.out.println("Deicnified");
		
	}
	
	//�A�C�R�������ꂽ���̏���
	public void windowIconified(java.awt.event.WindowEvent e) {
		System.out.println("Icnified");
		
	}
	
	// �J���ꂽ���̏���
	public void windowOpened(java.awt.event.WindowEvent e) {
		System.out.println("Opend");
		
	}
}