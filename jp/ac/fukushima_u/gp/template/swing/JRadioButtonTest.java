package jp.ac.fukushima_u.gp.template.swing;

import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JRadioButtonTest extends JFrame implements ChangeListener {
	JRadioButtonTest() {
		getContentPane().setLayout(new FlowLayout());

		JRadioButton rb1 = new JRadioButton("On", true);
		JRadioButton rb2 = new JRadioButton("Off");
		JRadioButton rb3 = new JRadioButton("Off");

		rb1.addChangeListener(this);
		rb2.addChangeListener(this);
		rb3.addChangeListener(this);

		getContentPane().add(rb1);
		getContentPane().add(rb2);
		getContentPane().add(rb3);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rb1);
		bg.add(rb2);
		bg.add(rb3);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JRadioButtonTest");
		setSize(200, 100);
		setVisible(true);
	}
	public void stateChanged(ChangeEvent e) {
		JRadioButton cb = (JRadioButton)e.getSource();
		if (cb.isSelected()) {
			cb.setText("On");
		} else {
			cb.setText("Off");
		}
	}
	public static void main(String[] args) {
		new JRadioButtonTest();
	}
}