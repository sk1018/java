package jp.ac.fukushima_u.gp.texteditor;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextEditorMain implements ActionListener{

	private TextEditorWin win;

	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		TextEditorMain main = new TextEditorMain();
		main.setAction();

	}

	public TextEditorMain() {
		win = new TextEditorWin();
		win.setVisible(true);
	}

	public void setAction() {
		win.miOpen.addActionListener(this);
		win.miSaveBufAs.addActionListener(this);
		win.miExit.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//�㏑���ۑ��̈�
		/*
		 * �t�@�C���܂ł̐�΃p�X��\���t�B�[���h��p�ӂ��Ă����A�����l�ł�null�����Ă���
		 * �ŁA�t�@�C���Q�ƃ_�C�A���O�����s��A���������΃p�X���擾���A
		 * ����炪null�i�t�@�C����I�΂Ȃ��������j�ȊO�̏ꍇ�A��������ݑ��쒆�̃t�@�C���̐�΃p�X�Ƃ��āA�ۑ����Ă���
		 * �u�㏑���ۑ��v��I�������ꍇ�A��΃p�X��ۑ������t�B�[���h���Q�Ƃ��Anull�ȊO�̏ꍇ�A�t�@�C���Q�ƃ_�C�A���O���ȗ����A
		 * �t�B�[���h�Ɋi�[����Ă����΃p�X�����ɏ㏑���ۑ����s��
		 * �t�B�[���h��null�̂܂܏㏑���ۑ������s���悤�Ƃ�����A�u���O�����ĕۑ��v�����s
		 */

		//�I�����̕ۑ��m�F�̈�
		/*
		 * �O��Ƃ��āA�E�B���h�E�̕���{�^���i�E��̃o�c�{�^���j�����������ɃC�x���g�����߂邱��
		 *
		 * ���݂̓��e�ƕҏW���e�ƁA�t�@�C������擾�����ߋ��̓��e���r���A
		 * �ύX���Ȃ���΂��̂܂܏I�����A�ύX������΁u�t�@�C����ۑ�����E���Ȃ��E�L�����Z���v�̎O���E�B���h�E��\��
		 * �ύX�ς݂��ۂ��̔��f������ꍇ�A���f�Ȃ��ɂƂ肠�����E�B���h�E��񎦂��邱�Ƃ����肤��
		 *
		 * �ҏW�̊m�F�Ɋւ��ẮABoolean�^�̃t�B�[���h��p��
		 * ���Ɏ��ۂɕҏW���s���e�L�X�g�G���A��KeyListener�ɓo�^���Ă���
		 * Boolean�^�̃t�B�[���h�͏����l��true�ɐݒ肷��
		 * ���̃t�B�[���h��true�Ȃ�ΕύX�Ȃ��Afalse�Ȃ�ΕύX�L��Ƃ���
		 *
		 * KeyListener����A�e�L�X�g�G���A���łȂ�炩�̃L�[���삪���m���ꂽ�ꍇ�t�B�[���h��false��
		 * �ۑ��������s�����ꍇ�ɂ̓t�B�[���h��true�ɕύX����
		 * �t�@�C���̕ύX�̗L�����m�F����ۂɂ͂��̃t�B�[���h���Q�Ƃ���悤�ɂ���
		 *
		 * ���ӂƂ��āABoolean�������ꍇ�ɂ́A�ǂ����̏�Ԃ������w���Ă��邩�𖾊m�ɂ���
		 * ����̏ꍇ�A�V�K�t�@�C���E�t�@�C���W�J��E�ۑ�������̃t�@�C���ɑ΂��A
		 * true  : �ύX�Ȃ�
		 * false : �ύX����
		 * ��������ꂼ��w���Ă���
		 *
		 * ����𖾊m�ɂ��Ă����Ȃ��ꍇ�A�ǂ��炪�ǂ��炩�킩��Ȃ��Ȃ�A�Ԉ���ċt�ɐݒ肵�Ă��܂��Ȃǂ̃o�O�̂��ƂƂȂ�
		 *
		 */

		//�V�K�t�@�C���̈�
		/*
		 * �t�@�C�����j���[���ɃA�C�e���Ƃ��āu�V�K�쐬�v�Ƃł��p��
		 *
		 * ���s���̏����Ƃ��ẮA�I���m�F���Ɠ��l�ɁA�ύX���Ă���ۑ����ꂽ���ǂ������m�F���A�ۑ��ς݂Ȃ珈�����s�A
		 * ���ۑ��Ȃ�Ίm�F�E�B���h�E��񎦂��A���̏������s���Ă��珈���𑱍s
		 *
		 * ���Ƀe�L�X�g�G���A�ɑ΂��A�G���A�����N���A���郁�\�b�h�����s����
		 * �����Ȃ��ꍇ�ɂ͓K���ɋ󕶎���i""�ł�����null�ł͂Ȃ��j��p�ӂ��A�����setText�Ƃ��Ă�����
		 *
		 * �E�B���h�E�^�C�g����ҏW���̃t�@�C�����Ƃ��Đݒ肵�Ă���ꍇ�ɂ́A�^�C�g�����u�V�K�v�Ƃ��ɍX�V���邱��
		 *
		 */

		// TODO �����������ꂽ���\�b�h�E�X�^�u
		if (e.getSource() == win.miOpen) {
			//�u�t�@�C�����J���v�̎�������
			//�t�@�C���Q�ƃ_�C�A���O�����[�h���[�h�ŕ\��
			FileDialog fd = new FileDialog(win, "�J���t�@�C����I��", FileDialog.LOAD);
			fd.setVisible(true);

			//�t�@�C���Q�ƃ_�C�A���O����t�@�C�������擾���A������E�B���h�E�̃^�C�g���Ƃ��Đݒ肷��
			//�t�@�C������null�̏ꍇ�̓t�@�C����I�����Ȃ������Ƃ��ď������I��
			String fileName = fd.getFile();
			if(fileName==null){
				return;
			}
			else{
				win.setTitle(fileName);
			}

			//��������͓��͏������ɂ������ۂɊJ������
			//�t�@�C�����J������A���g�����o�����肷��̂͗�O�̔������N���肤��̂ŁAtry-catch�u���b�N�ł�����
			try {
				//�V�K�C���X�^���X�������ɐV�K�C���X�^���X�𐶐����A�X�ɂ���������Ɂc�ƌJ��Ԃ��Ă邩��킩��ɂ������A
				//���[�U�����͂�������A�����Đ�΃p�X�ɕϊ����A��΃p�X�����Ƀt�@�C���̃C���X�^���X���쐬����
				//�쐬�����t�@�C���̃C���X�^���X�����Ƀt�@�C���̓ǂݍ��݂��s���t�@�C�����[�_�[�N���X�̃C���X�^���X�𐶐�
				//�t�@�C�����[�_�[�N���X�ł��ǂݍ��݂͂ł��邪�A���̂܂܂ł͌������������߁A�t�@�C�����[�_�[�N���X��
				//���b�p�[�N���X�ł���o�b�t�@�[�h���[�_�[�N���X�Ń��b�s���O���Ă���

				//���_���������ƁA���[�U�����͂�����΃p�X�̃t�@�C�����^�[�Q�b�g�Ƃ���
				//�ǂݍ��݂��s��BufferedReader�N���X�̃C���X�^���X�p�ӂ���
				BufferedReader br = new BufferedReader(new FileReader(new File(fd.getDirectory() + fd.getFile())));
				//���String�C���X�^���X��p�ӂ���
				//���̕�����ɂǂ�ǂ�A�����Ă����A�ŏI�I�Ƀt�@�C�����̑S�������A�����I������炱�̕�������o�͂���
				String str = new String("");
				//�������[�v�̊J�n
				//����������do-while���̂ق���������������Ȃ�
				while (true) {
					//�V����String�ϐ��ɁA�t�@�C�������s�P�ʂœǂݎ������������i�[
					String s = br.readLine();
					//�ϐ�s��null�̏ꍇ�A���ׂĂ̕������ǂݍ��񂾂Ƃ������ƂȂ̂�break
					//�����łȂ���Α��s
					if (s == null)
						break;
					//������str�ɑ΂��Astr�̌��ɍ���ǂݎ����������s��A�����A��s���Ȃ̂ōŌ�ɉ��s������
					str = str + s + "\n";
				}
				//str�ɂ̓t�@�C�����̑S�����񂪘A�����Ċi�[����Ă���̂ŁA������o�͂���
				win.ta.setText(str);

				//�v���O�����̊O���̃t�@�C�����ւ̐ڑ���R�l�N�V�����́A�g������K������
				//�����ӂ�ƃt�@�C�����j��������A�����Ǝg�p���ɂȂ��Ă��܂����肷��
				//�̂قǌ����ɍs��Ȃ��Ă����Ƃ��Ȃ邪�A�����܂ŖY�ꂽ���̕ی��Ƃ��A��{�͎蓮�ŕ���
				br.close();
			} catch (IOException e1) {
				// TODO �����������ꂽ catch �u���b�N

				//�����ɂ̓G���[���b�Z�[�W�Ƃ��āA�_�C�A���O���o����
				//�u�G���[�����B�J���܂���ł����v�ƕ\�����s�������B
				//���̍ہA�\�Ȃ�΃G���[�R�[�h�Ƃ����\���ł���Ƃ킩��₷��
				e1.printStackTrace();
			}
		}

		if (e.getSource() == win.miSaveBufAs) {
			//������u���O�����ĕۑ��v

			//�ŏ��̓��[�U�ɂ������͕�
			//�t�@�C���Q�ƃ_�C�A���O���Z�[�u���[�h�ŕ\��
			FileDialog fd = new FileDialog(win, "���O�����ĕۑ�����", FileDialog.SAVE);
			fd.setVisible(true);

			//�t�@�C���Q�ƃ_�C�A���O����A���͂��ꂽ�u�f�B���N�g���܂ł̃p�X�v�Ɓu�t�@�C�����v���擾
			//�Ȃ��A�f�B���N�g�����Ɋւ��ẮA�Ōオ�u���v�ŏI����Ă�̂ŁA�p�X�ƃt�@�C������A�����邾���Ő�΃p�X�ɂȂ�
			//�uC:\hoge\temp\�v+�utest.txt�v=�uC:\hoge\temp\test.txt�v

			//�����ł͊m�F�̂��߂̏o��
			System.out.println("dir=" + fd.getDirectory());
			System.out.println("FileName=" + fd.getFile());

			//�t�@�C���Q�ƃ_�C�A���O����t�@�C�������擾���A������E�B���h�E�̃^�C�g���Ƃ��Đݒ肷��
			//�t�@�C������null�̏ꍇ�̓t�@�C����I�����Ȃ������Ƃ��ď������I��
			String fileName = fd.getFile();
			if(fileName==null){
				return;
			}
			else{
				win.setTitle(fileName);
			}

			//��������͓��͏������ɂ������ۂ̕ۑ�����
			//�t�@�C�����J������A����ɏ������񂾂肷��̂͗�O�̔������N���肤��̂ŁAtry-catch�u���b�N�ł�����
			try {
				//�V�K�C���X�^���X�������ɐV�K�C���X�^���X�𐶐����A�X�ɂ���������Ɂc�ƌJ��Ԃ��Ă邩��킩��ɂ������A
				//���[�U�����͂�������A�����Đ�΃p�X�ɕϊ����A��΃p�X�����Ƀt�@�C���̃C���X�^���X���쐬����
				//�쐬�����t�@�C���̃C���X�^���X�����Ƀt�@�C���̏������݂��s���t�@�C�����C�^�[�N���X�̃C���X�^���X�𐶐�
				//�t�@�C�����C�^�[�N���X�ł��������݂͂ł��邪�A���̂܂܂ł͌������������߁A�t�@�C�����C�^�[�N���X��
				//���b�p�[�N���X�ł���o�b�t�@�[�h���C�^�[�N���X�Ń��b�s���O���Ă���

				//���_���������ƁA���[�U�����͂�����΃p�X�̃t�@�C�����^�[�Q�b�g�Ƃ���
				//�������݂��s��BufferedWriter�N���X�̃C���X�^���X�p�ӂ���
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fd.getDirectory() + fd.getFile())));

				//�e�L�X�g�G���A�ɏ�����Ă���e�L�X�g���擾���A�������������
				//�Ȃ��A����̓t�@�C�����Ȃ���ΐ����������ɏ������݂��s���A�t�@�C�����L��ꍇ�ɂ͏㏑�����s��
				bw.write(win.ta.getText());
				//�m�F�p
				//				System.out.print(ta.getText());
				//�v���O�����̊O���̃t�@�C�����ւ̐ڑ���R�l�N�V�����́A�g���������
				//�����ӂ�ƃt�@�C�����j��������A�����Ǝg�p���ɂȂ��Ă��܂����肷��
				//�̂قǌ����ɍs��Ȃ��Ă����Ƃ��Ȃ邪�A�����܂ŖY�ꂽ���̕ی��Ƃ��A��{�͎蓮�ŕ���
				bw.close();
			} catch (IOException e1) {
				// TODO �����������ꂽ catch �u���b�N

				//�����ɂ̓G���[���b�Z�[�W�Ƃ��āA�_�C�A���O���o����
				//�u�G���[�����B�ۑ�����܂���ł����v�ƕ\�����s�������B
				//���̍ہA�\�Ȃ�΃G���[�R�[�h�Ƃ����\���ł���Ƃ킩��₷��

				e1.printStackTrace();
			}
		}

		if (e.getSource() == win.miExit) {
			//�V�X�e���I���錾
			System.exit(0);
		}
	}

}
