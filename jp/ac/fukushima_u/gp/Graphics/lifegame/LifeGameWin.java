package jp.ac.fukushima_u.gp.Graphics.lifegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class LifeGameWin extends JFrame implements ComponentListener,KeyListener{

	private int width=300;//paneのサイズ
	private int height=200;//paneのサイズ
	private CellControl cc;
	private LifeGameMain main;
	//ダブルバッファリング用
	Image buf;
	Graphics bufG;


	public void setCellControl(CellControl cc){
		this.cc=cc;
	}

	public void setLifeGameMain(LifeGameMain main){
		this.main=main;
	}

	public LifeGameWin(){
		this(300, 200);
	}
	public LifeGameWin(int width, int height){
		this.width=width;
		this.height=height;
		this.init();
	}

	private void init(){
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(d.width/2-(this.width/2+8),d.height/2-(this.height/2+18),this.width+16, this.height+37);
		this.setTitle("Life Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.addKeyListener(this);
		this.addComponentListener(this);


	}



	//ここからダブルバッファリング
	public void make(){
		buf=createImage(this.getSize().width, this.getSize().height);
		bufG=buf.getGraphics();
	}

	// 再描画
	public void update(Graphics g) {
	paint(g);
	}

	public synchronized void paint(Graphics g) {
		Dimension size = this.getSize();

		bufG.setColor(Color.black);
		bufG.fillRect(0, 0, size.width, size.height);

		int[] n=cc.getCellNum();

		int cellSize=cc.getCellSize();
		boolean[][] current_life=cc.getCurrentLife();

//		bufG.setColor(Color.yellow);
		for(int i=0;i<n[1];i++){
			for(int l=0;l<n[0];l++){
				if(current_life[i][l]){
					bufG.setColor(cc.getColor(i, l));
					bufG.fillRect(8+(cellSize*l), 29+(cellSize*i), cellSize, cellSize);
				}
			}
		}


//		g.dispose();
		g.drawImage(buf, 0, 0, this);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		char n=e.getKeyChar();
		switch(n){
			case 'z':
				if(LifeGameMain.st.fps<=1){
					try {
						LifeGameMain.st.wait();
					} catch (InterruptedException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
				}
				else{
					LifeGameMain.st.fps--;
				}
				break;
			case 'x':
				if(LifeGameMain.st.fps<=1)LifeGameMain.st.notify();
				else LifeGameMain.st.fps++;
				LifeGameMain.flag=true;
				break;
			case 's':
				main.run();
				break;
			case 'a':
				cc.cellBurst();
				break;
		}

//		if(e.getKeyChar()=='z')LifeGameMain.fps--;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		try {
			LifeGameMain.st.wait();
		} catch (InterruptedException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		LifeGameMain.st.notify();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

		//this.setSize(this.width+16, this.height+37);

		//cc.setCellNum(this.getContentPane().getSize().width/cc.CELL_SIZE, this.getContentPane().getSize().height/cc.CELL_SIZE);
		if(LifeGameMain.st.startFlag){
		Dimension d= this.getSize();
		this.width=d.width;
		this.height=d.height;
		cc.changeCellNum(this.width, this.height);
		}

		bufG.dispose();
		buf.flush();
		this.make();

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}





}
