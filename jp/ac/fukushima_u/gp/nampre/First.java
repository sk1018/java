package jp.ac.fukushima_u.gp.nampre;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class First{
	
	static Boolean f = true;
	
	public static void main(String[] args) {
		
		Window inwin = new Window();
		inwin.mapWindowCreate();
		
	}
	
	static void outputCons(int[][] map) {
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				System.out.print(map[i][l] + " ");
			}
			System.out.print("\n");
		}
	}
	
	static int[][] sampleData() {
		
		int[][] map = new int[9][9];
		for (int i = 0; i < 9; i++) {
			map[0][i] = i + 1;
		}
		for (int i = 0; i < 9; i++) {
			for (int l = 0; l < 9; l++) {
				map[i][l] = 0;
			}
		}
		return map;
		
	}
	
	public static int[][] inputCons() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int[][] map = new int[9][9];
		String[] str = new String[9];
		try {
			for (int i = 0; i < 9; i++) {
				str[i] = br.readLine();
			}
			for (int i = 0; i < 9; i++) {
				for (int l = 0; l < 9; l++) {
					String s = null;
					s += str[i].charAt(l * 2);
					map[i][l] = Integer.parseInt(s);
				}
			}
			
		} catch (IOException e) {
			System.out.println("Exception :" + e);
		}
		
		return map;
	}
	
}
