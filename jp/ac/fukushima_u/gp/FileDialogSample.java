package jp.ac.fukushima_u.gp;

import java.awt.FileDialog;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;


/*
 * �E�B���h�E�Y�ł悭����u�t�@�C�����J���E�ۑ�����v�̃_�C�A���O��\������
 * 
 * �_�C�A���O�Ȃ̂ŁA�e�E�B���h�E�������āA��������̃|�b�v�A�b�v
 * ���[�_���_�C�A���O�H�Ȃ̂ŁA���ꂪ�o�Ă�Ԃ͐e�E�B���h�E�͑���s��
 * 
 */
public class FileDialogSample extends WindowAdapter{
	public static void main(String args[]) {
		new FileDialogSample().start();
	}

	private void start() {
		//�e�ƂȂ�E�B���h�E��ݒ�
		//�ꉞ�e�͂Ȃ��ł��J�����Ƃ͉\
        JFrame frame = new JFrame();
        frame.setSize(400 , 150);
        frame.setVisible(true);
        frame.addWindowListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //�_�C�A���O�̃C���X�^���X���쐬���āAsetVisible�ŉ���
        //�����͂��ꂼ��e�E�B���h�E�A�^�C�g���A�ǂݍ��݂��ۑ���
        //�Ō�̈����́A�{�^�����u�J���v�u�ۑ��v�̂ǂ���ɂ��邩�̎w��
        FileDialog f_dialog = new FileDialog(frame, "title test", FileDialog.LOAD);
        f_dialog.setVisible(true);


		String dir = f_dialog.getDirectory();//�f�B���N�g���[�̎擾
		String fileName = f_dialog.getFile();//File���̎擾


//�擾��������\������
		frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));
		frame.add(new Label("dir="+dir));
		frame.add(new Label("FileName="+fileName));
		frame.setVisible(true);

		System.out.println("dir="+dir);
		System.out.println("FileName="+fileName);

		//��������̓e�L�X�g�t�@�C�����J���Ē��g��ǂݏo������
		//FileDialogSample�Ƃ��Ă͎֑�
		FileReader fr = null;
		try {
			fr = new FileReader(new File(dir+fileName));
		} catch (FileNotFoundException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}

		int c=0;
		while(true){
			try {
				c=fr.read();
			} catch (IOException e) {
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
			}
			if(c==-1)break;

		System.out.print((char)c);
    }}
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}