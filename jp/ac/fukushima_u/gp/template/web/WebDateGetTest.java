package jp.ac.fukushima_u.gp.template.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.ac.fukushima_u.gp.template.Template;

public class WebDateGetTest {
	private HttpURLConnection connect = null;
	private InputStream in = null;
	private OutputStream out = null;

	/**
	 * HTTP�ւ̃A�N�Z�X���m�����āAinputstream���擾����<br>
	 * web����̃f�[�^�擾�ҋ@���̏�Ԃɂ���<BR>
	 * �Ȃ��A�W���ł�GET�ŒʐM���s���H
	 * @param adress �f�[�^���擾������URL
	 * @throws Exception ���炩�̃G���[�����������ꍇ
	 */
	public void setOpenHttpConnect(String adress) throws Exception {
		//URL�̏�����HTTP�ւ̃A�N�Z�X
		//������adress�Ɏ擾�������f�[�^�̂���URL�����
		//"http://files.myopera.com/kaoris/blog/Opera_512x512.png";
		URL url;
		url = new URL(adress);
		connect = (HttpURLConnection) url.openConnection();
		connect.setRequestMethod("GET");
		in = connect.getInputStream();
	}

	/**
	 * �O��Ƃ��āAsetOpenHttpConnect���s�Ȃ��ăf�[�^�擾�ҋ@���ɂ��Ă�������
	 * <BR>setOpenHttpConnect�őҋ@���ɂ���URL����f�[�^���擾����
	 * <BR>�擾�����f�[�^�́u�N���X�p�X�{outdir�v�Ƃ�����΃p�X�Ɋi�[�����
	 * @param outdir �p�X���ƃt�@�C���������
	 * @throws Exception ���炩�̃G���[������
	 */
	public void getDatatoHttp(String outdir) throws Exception {
		//HTTP����f�[�^���擾
		byte[] buf = new byte[1024];
		int len;
		char[] temp = outdir.toCharArray();
		if (temp[1] == new String(":").charAt(0)) {
			out = new FileOutputStream(outdir);
		}
		else {
			out = new FileOutputStream(Template.getCLASSPATH() + outdir);
		}
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.flush();
		out.close();
	}

	/**
	 * set�ŊJ����Http�R�l�N�V������InputStream�̓�����
	 * <BR>set�ŃR�l�N�V�������J��������s���Ă�������
	 * <BR>set�ŃG���[�f���Ă��͂��Ȃ��Ă�����Ă����ƈ��S
	 * <BR>set�ň���J���Ă��Ȃ��Ă��C�J���Ă��Ȃ���΃X���[�C�J���Ă���Ε��邩��
	 */
	public void setCloseHttpConnect() {
		try {
			in.close();
		} catch (IOException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}//InputStream�����
		connect.disconnect();//�T�C�g�̐ڑ���ؒf

	}

	public static void main(String[] argv) {
		WebDateGetTest wd = new WebDateGetTest();
		try {
			String adress = "http://files.myopera.com/kaoris/blog/Opera_512x512.png";
			wd.setOpenHttpConnect(adress);
		} catch (Exception e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
		try {
			String outdir = "jp/ac/fukushima_u/gp/materials/test/sample.png";
			wd.getDatatoHttp(outdir);
		} catch (Exception e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}

		wd.setCloseHttpConnect();

	}

	public WebDateGetTest() {
	}

}
