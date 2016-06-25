package jp.ac.fukushima_u.gp.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class DataFlowImage extends JFrame {
	
	/*
	 * データの転送順に関しては，極力容量オーバーを避けるべく，
	 * 書き込み，計算，読み込みの順に実行する
	 * 書き込みに関しては，一番下のパーツから順に，
	 * 現在のパーツから特定の量を減算し，その分を一つ下のパーツに加算する
	 * 
	 * 計算に関しては，最上位のパーツのみ実行する
	 * その際には特定の速度で再上位パーツ内の読み込みデータを書き込みデータに変換する
	 * 
	 * 読み込みについては最上位から順に行う
	 * 一つ下のパーツから減算を行い，その分のデータ量を自身に加算する
	 */
	class Parts {
		double MAX_CAP;
		double READ_DATA;
		double WRITE_DATA;
		
		double MAX_SPEED;
		double READ_SPEED;
		double WRITE_SPEED;
		
		Parts UPPER_PARTS = null;
		Parts LOWER_PARTS = null;
		
		boolean TOP = false;
		
		void setUPPER_PARTS(Parts parts) {
			this.UPPER_PARTS = parts;
		}
		
		void setLOWER_PARTS(Parts parts) {
			this.LOWER_PARTS = parts;
		}
		
		synchronized void dataWrite() {
			//実際に転送を行う量
			//基本は書き込み速度
			double data = this.WRITE_SPEED;
			
			//下にパーツがある場合，空き容量を超えない範囲に設定
			if (this.LOWER_PARTS != null) {
				if (data > LOWER_PARTS.FreeCap()) {
					data = LOWER_PARTS.FreeCap();
				}
			}
			
			//転送量に対して，現在のデータ量が少なければ，残りのデータすべてに設定
			if (data > this.WRITE_DATA) {
				data = this.WRITE_DATA;
			}
			
			//ここから実際に転送
			//下のパーツがあれば，そこに加算
			if (this.LOWER_PARTS != null) {
				this.LOWER_PARTS.WRITE_DATA += data;
			}
			//現在の保持データから減算
			this.WRITE_DATA -= data;
		}
		
		
		
		synchronized double FreeCap() {
			return this.MAX_CAP - (this.WRITE_DATA + this.READ_DATA);
		}
	}
	
	public static void main(String[] args) {
		DataFlowImage df = new DataFlowImage();
		
		df.setVisible(true);
	}
	
	private DataFlowImage() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension winsize = new Dimension(800, 500);
		this.setBounds((d.width - winsize.width) / 2, (d.height - winsize.height) / 2, winsize.width, winsize.height);
	}
	
	public void paint(Graphics g) {
		Dimension size = this.getSize();
		int centerline = size.width / 2;
		g.setColor(Color.white);
		
		g.fillRect(0, 0, size.width, size.height);
		
		g.setColor(Color.black);
		
		int linewidth = 1;
		g.fillRect(centerline - linewidth / 2, 0, linewidth, size.height);
		
		g.dispose();
	}
}
