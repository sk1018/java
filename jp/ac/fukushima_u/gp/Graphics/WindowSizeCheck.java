package jp.ac.fukushima_u.gp.Graphics;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

public class WindowSizeCheck extends JFrame implements ComponentListener{
	
//	private BufferdReader br = new BufferdReader(System.in);
	
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		Dimension win_d=this.getSize();
		System.out.println("win width="+win_d.width+" win height="+win_d.height);

		Dimension con_d=this.getContentPane().getSize();
		System.out.println("con width="+con_d.width+" con height="+con_d.height);

		Dimension frm_d=new Dimension();
		frm_d.width=win_d.width-con_d.width;
		frm_d.height=win_d.height-con_d.height;
		System.out.println("frm width="+frm_d.width+" frm height="+frm_d.height);
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
	
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		WindowSizeCheck win = new WindowSizeCheck();
		win.setSize(300,200);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.addComponentListener(win);
		win.setVisible(true);
		System.out.println("test start");
	}
	
}
