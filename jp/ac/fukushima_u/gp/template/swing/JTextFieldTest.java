package jp.ac.fukushima_u.gp.template.swing;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class JTextFieldTest extends JFrame {
	JTextFieldTest() {
		getContentPane().setLayout(new FlowLayout());

		JTextField tf = new JTextField("Hello World!!", 15);
		getContentPane().add(tf);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JTextFieldTest");
		setSize(200, 100);
		setVisible(true);
	}
	public static void main(String[] args) {
		new JTextFieldTest();
	}
}