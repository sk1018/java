package jp.ac.fukushima_u.gp.template.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class JButtonTest extends JFrame implements ActionListener {
	JButtonTest() {
		getContentPane().setLayout(new FlowLayout());

		JButton b1 = new JButton("OK");
		b1.addActionListener(this);
		getContentPane().add(b1);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JButtonTest");
		setSize(200, 100);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		System.out.println("OK");
	}
	public static void main(String[] args) {
		new JButtonTest();
	}
}