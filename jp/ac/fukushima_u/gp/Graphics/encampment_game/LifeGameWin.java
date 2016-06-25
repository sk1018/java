package jp.ac.fukushima_u.gp.Graphics.encampment_game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class LifeGameWin extends JFrame implements ComponentListener,KeyListener, MouseListener{

	private int width=300;//paneのサイズ
	private int height=200;//paneのサイズ
	private CellControl cc;
	private LifeGameMain main;
	//ダブルバッファリング用
	Image buf;
	Graphics bufG;

	private boolean subWinFlag=true;
	JFrame subWin;
	JTextArea ta;


	
	
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

		if(this.subWinFlag){
			this.addMouseListener(this);
//			this.subWin.addMouseListener(this);
			this.subWin=new JFrame();
			this.subWin.setSize(200,150);
			this.subWin.setLayout(new FlowLayout());
			ta=new JTextArea();
			this.subWin.add(ta);
			this.subWin.setVisible(true);
		}

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


		g.drawImage(buf, 0, 0, this);
		g.dispose();

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		char n=e.getKeyChar();
		switch(n){
			case 'z':
				if(LifeGameMain.fps<=1){
					LifeGameMain.fps=0;
					LifeGameMain.flag=false;
				}
				else{
					LifeGameMain.fps--;
				}
				break;
			case 'x':
				if(LifeGameMain.fps<=0)LifeGameMain.fps=1;
				else LifeGameMain.fps++;
				LifeGameMain.flag=true;
				LifeGameMain.exec();
				break;
			case 's':
				main.run();
				break;
			case 'a':
				cc.cellBurst();break;
		}

//		if(e.getKeyChar()=='z')LifeGameMain.fps--;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		bufG.dispose();
		buf.flush();
		this.make();

		this.setSize(this.width+16, this.height+37);
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

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		ta.setText("x:"+e.getX()+" y:"+e.getY()+"\nX="+e.getXOnScreen()+" Y="+e.getYOnScreen());
		this.subWin.repaint();
		this.subWin.setVisible(true);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}





}
