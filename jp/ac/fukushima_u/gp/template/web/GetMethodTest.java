package jp.ac.fukushima_u.gp.template.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.ac.fukushima_u.gp.template.text_read.TextRead;

public class GetMethodTest {

	public static void main(String[] argv) {
		WebDateGetTest wd = new WebDateGetTest();

		/*
		 * �Ȃ낤API�̃e�X�g�p�ɍ��������
		 * �Ώ�URL�Ƃ̐ڑ����m�����AURL����f�[�^���_�E�����[�h���ĔC�ӂ̖��O�ŕۑ����ăN���[�Y
		 * ���̌�A�ۑ������t�@�C�����G���R�[�h�w�肵�ēǂݍ���
		 */
		try {
			wd.setOpenHttpConnect("http://api.syosetu.com/novelapi/api/");
			wd.getDatatoHttp("C:\\test.YAML");
			wd.setCloseHttpConnect();
			TextRead.encodeRead("C:\\test.YAML","UTF-8");
		} catch (Exception e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}

	}

	public GetMethodTest() {
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u

	}

	public void getMethodTest(){
		try {
			URL url = new URL("http://www.hogehoge.com/hoge.html?parameter1=value1&parameter2=value2");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
					"JISAutoDetect"));
			String httpSource = new String();
			String str;
			while (null != (str = bufferReader.readLine())) {
				httpSource = httpSource + str;
			}
			bufferReader.close();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
