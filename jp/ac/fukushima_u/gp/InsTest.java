package jp.ac.fukushima_u.gp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class InsTest{
	public static void main(String[] args) throws IOException {
		System.out.println("");
		
		BufferedReader input =
				new BufferedReader(new InputStreamReader(System.in));
		String str = input.readLine();
		
		System.out.println("test" + str + "ttt");
	}
}
