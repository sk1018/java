package jp.ac.fukushima_u.gp.code_iq;

import java.io.FileInputStream;

public class Primenumber {

	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		try {
			FileInputStream fis = new FileInputStream("materials/primenumber.txt");

			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
