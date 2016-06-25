package jp.ac.fukushima_u.gp.template;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class WindowTest extends JFrame implements ActionListener{

	JButton A, B;

	public static void main(String[] args) {
		WindowTest fr = jp.ac.fukushima_u.gp.template.WindowTest.jframe(null, 100, 300);
		WindowTest.textLabel(fr);
		for (int i = 0; i < 9; i++)
			WindowTest.JTextFieldTest(fr);
		fr.jbutton(fr);

	}

	public static Frame frame(String str, int width, int height) {
		if (str == null)
			str = "win test";

		Frame fr = new Frame(str);

		fr.setSize(width, height);

		fr.setVisible(true);

		return fr;
	}

	public static WindowTest jframe(String str, int width, int height) {
		if (str == null)
			str = "win test";

		WindowTest fr = new WindowTest();
		fr.setTitle(str);

		fr.setSize(width, height);

		fr.setVisible(true);

		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		fr.getContentPane().setLayout(new FlowLayout());

		return fr;
	}

	public static JFrame textLabel(JFrame fr) {
		fr.add(new JLabel("test"));
		return fr;
	}

	public static JFrame textLabel(JFrame fr, String s) {
		fr.add(new JLabel(s));
		return fr;
	}

	public static JFrame JTextFieldTest(JFrame fr) {
		JTextField tf = new JTextField(15);
		fr.getContentPane().add(tf);

		return fr;
	}

	public JFrame jbutton(JFrame fr) {
		A = new JButton("A");
		B = new JButton("B");

		A.addActionListener(this);
		B.addActionListener(this);

		fr.getContentPane().add(A);
		fr.getContentPane().add(B);

		return fr;
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == A) {
			System.out.println("A");
		}
		if (event.getSource() == B) {
			System.out.println("B");

		}
	}

}
