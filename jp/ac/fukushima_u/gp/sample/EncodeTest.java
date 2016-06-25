package jp.ac.fukushima_u.gp.sample;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class EncodeTest {

	private static String str;

	public static void main(String[] args) {

		String encodeStr = new String("");

		try {
			str = URLDecoder.decode(encodeStr, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO é©ìÆê∂ê¨Ç≥ÇÍÇΩ catch ÉuÉçÉbÉN
			e.printStackTrace();
		}

		System.out.println("test" + str);
	}
}
