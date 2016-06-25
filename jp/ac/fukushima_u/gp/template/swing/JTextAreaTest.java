package jp.ac.fukushima_u.gp.template.swing;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class JTextAreaTest extends JFrame {
	JTextAreaTest() {
		getContentPane().setLayout(new FlowLayout());

		JTextArea ta = new JTextArea("Hello \nWorld!!", 9, 17);
		getContentPane().add(ta);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JTextAreaTest");
		setSize(200, 200);
		setVisible(true);
	}
	public static void main(String[] args) {
		new JTextAreaTest();
	}
}