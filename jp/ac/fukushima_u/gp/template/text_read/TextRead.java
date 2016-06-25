package jp.ac.fukushima_u.gp.template.text_read;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

public class TextRead {

	public TextRead() {
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		String str="C:\\hoge\\Dropbox\\pro\\java\\jp\\ac\\fukushima_u\\gp\\test.xml";
		TextRead.simpleRead(str);
		TextRead.encodeRead(str, "UTF-8");
	}

	/**
	 * �Ώۃe�L�X�g�t�@�C���̃G���R�[�h��ύX���邱�ƂȂ��ǂݍ��ރV���v���ȓǂݍ��݃��\�b�h
	 * <BR>�V���v���Ȕ��ʁA�G���R�[�h�̎w�肪�ύX�ł��Ȃ��̂ŃG���R�[�h���قȂ�t�@�C�����������ǂݍ��܂�Ȃ�
	 * @param str �Ώۃt�@�C���܂ł̐�΃p�X
	 */
	public static void simpleRead(String str) {
		try {
			FileReader fr = new FileReader(str);
			char[] buf = new char[1024];
			while (fr.read(buf) > 0) {
				System.out.println(buf);
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �Ώۂ̃e�L�X�g�t�@�C�����w�肵���G���R�[�h�œǂݍ��ރ��\�b�h
	 * @param str �Ώۃt�@�C���܂ł̐�΃p�X
	 * @param enc �Ώۃt�@�C���̕����R�[�h
	 */
	public static void encodeRead(String str, String enc) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(str),enc));
			String temp=new String("");
			while((temp=br.readLine())!=null){
				System.out.println(temp);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
