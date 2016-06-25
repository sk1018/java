package jp.ac.fukushima_u.gp.template.swing;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class JLabelTest extends JFrame {
	JLabelTest() {
		getContentPane().setLayout(new FlowLayout());

		JLabel label = new JLabel("Hello World!!");
		getContentPane().add(label);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JLabelTest");
		setSize(200, 100);
		setVisible(true);
	}
	public static void main(String[] args) {
		new JLabelTest();
	}
}