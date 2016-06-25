package jp.ac.fukushima_u.gp;

public class Sample01{
	
	private int w;
	
	private int h;
	
	Sample01() {
		w = 0;
		h = 0;
	}
	
	public void setSize(int width, int height) {
		w = width;
		h = height;
	}
	
	public int getWidth() {
		return w;
	}
	
	public int getHeight() {
		return h;
	}
}
