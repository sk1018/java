package jp.ac.fukushima_u.gp.template.swing;

import java.awt.FlowLayout;

import javax.swing.JFrame;

public class JFrameTest extends JFrame {
	JFrameTest() {
		getContentPane().setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JFrameTest");
		setSize(200, 100);
		setVisible(true);
	}
	public static void main(String [] args) {
		new JFrameTest();
	}
}