package jp.ac.fukushima_u.gp.Graphics.golden_ratio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;

public class GoldenRatioWin extends JFrame {

	//ダブルバッファリング用
	Image buf;
	Graphics bufG;

	private int width = 800;
	private int height = 600;

	int step = 20;

	ArrayList<Point> ps = new ArrayList<Point>();

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		GoldenRatioWin gr = new GoldenRatioWin();

		gr.make();
		gr.run();
	}

	private void run() {
		while(true){
			this.repaint();
		}
	}

	private GoldenRatioWin() {
		this.init();
	}

	private void init() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(d.width / 2 - (this.width / 2 + 8), d.height / 2 - (this.height / 2 + 18), this.width + 16,
				this.height + 38);
		this.setTitle("Life Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		this.ratioCalc();
	}

	private void ratioCalc(){
		double short_edge = this.height - 20;
		double long_edge = this.width - 20;

		if ((short_edge * ((1 + Math.sqrt(5.0)) / 2)) < long_edge) {
			long_edge = short_edge * ((1 + Math.sqrt(5.0)) / 2);
		}
		else {
			short_edge = long_edge / ((1 + Math.sqrt(5.0)) / 2);
		}

		int x=8+10;
		int y=this.height-38-10;
		int f=1;
		for (int i = 0; i < this.step; i++) {
			ps.add(new Point(x,y));
			if(f==1){
				x+=short_edge;
				y-=short_edge;

				double t=long_edge-short_edge;
				long_edge=short_edge;
				short_edge=t;

				f=4;
			}else if(f==2){
				x-=short_edge;
				y-=short_edge;

				double t=long_edge-short_edge;
				long_edge=short_edge;
				short_edge=t;

				f=1;
			}else if(f==3){
				x-=short_edge;
				y+=short_edge;

				double t=long_edge-short_edge;
				long_edge=short_edge;
				short_edge=t;

				f=2;
			}else if(f==4){
				x+=short_edge;
				y+=short_edge;

				double t=long_edge-short_edge;
				long_edge=short_edge;
				short_edge=t;

				f=3;
			}else{
				break;
			}
		}
	}

	public void make(){
		buf=createImage(this.getSize().width, this.getSize().height);
		bufG=buf.getGraphics();
	}
	
	public synchronized void paint(Graphics g) {
		Dimension size = this.getSize();

		bufG.setColor(Color.white);
		bufG.fillRect(0, 0, size.width, size.height);

		for(int i=1;i<this.step;i++){
			if(i==0){
				bufG.setColor(Color.blue);
			}
			else{
				bufG.setColor(Color.black);
			}
			bufG.drawLine(ps.get(i-1).x, ps.get(i-1).y, ps.get(i).x, ps.get(i).y);
		}

		g.drawImage(buf, 0, 0, this);

	}

}

class Point {
	int x;
	int y;

	Point() {
	}

	Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
}