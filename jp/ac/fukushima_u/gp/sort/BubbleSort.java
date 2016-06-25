package jp.ac.fukushima_u.gp.sort;

public class BubbleSort{
	
	public static int[] sort(int n, int f, int... in) {
		int i, l;
		int t;
		
		for (i = 0; i < n - 1; i++) {
			for (l = 0; l < n - 1 - i; l++) {
				if (f == 0) {
					if (in[l] > in[l + 1]) {
						t = in[l];
						in[l] = in[l + 1];
						in[l + 1] = t;
					}
				}
				else if (f == 1) {
					if (in[l] < in[l + 1]) {
						t = in[l];
						in[l] = in[l + 1];
						in[l + 1] = t;
					}
				}
			}
		}
		
		return in;
	}
	
	public static float[] sort(int n, int f, float... in) {
		int i, l;
		float t;
		
		for (i = 0; i < n - 1; i++) {
			for (l = 0; l < n - 1 - i; l++) {
				if (f == 0) {
					if (in[l] > in[l + 1]) {
						t = in[l];
						in[l] = in[l + 1];
						in[l + 1] = t;
					}
				}
				else if (f == 1) {
					if (in[l] < in[l + 1]) {
						t = in[l];
						in[l] = in[l + 1];
						in[l + 1] = t;
					}
				}
			}
		}
		
		return in;
	}
	
	public static double[] sort(int n, int f, double... in) {
		int i, l;
		double t;
		
		for (i = 0; i < n - 1; i++) {
			for (l = 0; l < n - 1 - i; l++) {
				if (f == 0) {
					if (in[l] > in[l + 1]) {
						t = in[l];
						in[l] = in[l + 1];
						in[l + 1] = t;
					}
				}
				else if (f == 1) {
					if (in[l] < in[l + 1]) {
						t = in[l];
						in[l] = in[l + 1];
						in[l + 1] = t;
					}
				}
			}
		}
		
		return in;
	}
	
	public static void sorttest(int n, int f, int... in) {
		for (int i = 0; i < 5; i++) {
			System.out.println(in[i]);
		}
	}
	
}
