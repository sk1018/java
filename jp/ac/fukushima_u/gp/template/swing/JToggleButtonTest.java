package jp.ac.fukushima_u.gp.template.swing;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JToggleButtonTest extends JFrame implements ChangeListener {
	JToggleButtonTest() {
		getContentPane().setLayout(new FlowLayout());

		JToggleButton b1 = new JToggleButton("OFF");
		b1.addChangeListener(this);
		getContentPane().add(b1);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JToggleButtonTest");
		setSize(200, 100);
		setVisible(true);
	}
	public void stateChanged(ChangeEvent e) {
		JToggleButton btn = (JToggleButton)e.getSource();
		if (btn.isSelected()) {
			btn.setText("ON");
		} else {
			btn.setText("OFF");
		}
	}
	public static void main(String[] args) {
		new JToggleButtonTest();
	}
}