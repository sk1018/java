package jp.ac.fukushima_u.gp.sort;

public class QuickSort{
	
	public static int[] sort(int n, int f, int... in) {
		;
		
		if (f == 0)
			quick(1, 0, n - 1, in);
		else if (f == 1)
			quick(-1, 0, n - 1, in);
		
		return in;
	}
	
	
	/**
	 * 
	 * @param f +1 or -1で，昇順か降順を指定
	 * @param a 配列の先頭番号．呼び出し時は0
	 * @param b 配列の最後尾の番号．呼び出し時は配列のsize-1
	 * @param in 配列そのもの
	 */
	public static void quick(int f, int a, int b, int[] in) {
		int s, t;
		int p, tmp;
		
		if (a >= b)
			return;
		
		p = (in[a] + in[b]) / 2;
		s = a;
		t = b;
		while (true) {
			while (in[s] < p * f)
				s++;
			while (in[t] > p * f)
				t--;
			if (t <= s)
				break;
			
			tmp = in[s];
			in[s] = in[t];
			in[t] = tmp;
			
			s++;
			t--;
		}
		
		quick(f, a, s - 1, in);
		quick(f, t + 1, b, in);
		
		return;
	}
	
	//float
	public static float[] sort(int n, int f, float... in) {
		;
		
		if (f == 0)
			quick(1, 0, n - 1, in);
		else if (f == 1)
			quick(-1, 0, n - 1, in);
		
		return in;
	}
	
	private static void quick(int f, int a, int b, float[] in) {
		int s, t;
		float p, tmp;
		
		if (a >= b)
			return;
		
		p = (in[a] + in[b]) / 2;
		s = a;
		t = b;
		while (true) {
			while (in[s] < p * f)
				s++;
			while (in[t] > p * f)
				t--;
			if (t <= s)
				break;
			
			tmp = in[s];
			in[s] = in[t];
			in[t] = tmp;
			
			s++;
			t--;
		}
		
		quick(f, a, s - 1, in);
		quick(f, t + 1, b, in);
		
		return;
	}
	
	//double
	public static double[] sort(int n, int f, double... in) {
		;
		
		if (f == 0)
			quick(1, 0, n - 1, in);
		else if (f == 1)
			quick(-1, 0, n - 1, in);
		
		return in;
	}
	
	private static void quick(int f, int a, int b, double[] in) {
		int s, t;
		double p, tmp;
		
		if (a >= b)
			return;
		
		p = (in[a] + in[b]) / 2;
		s = a;
		t = b;
		while (true) {
			while (in[s] < p * f)
				s++;
			while (in[t] > p * f)
				t--;
			if (t <= s)
				break;
			
			tmp = in[s];
			in[s] = in[t];
			in[t] = tmp;
			
			s++;
			t--;
		}
		
		quick(f, a, s - 1, in);
		quick(f, t + 1, b, in);
		
		return;
	}
	
}