package jp.ac.fukushima_u.gp.template.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;

public class JComboBoxTest extends JFrame implements ActionListener {
	JComboBoxTest() {
		getContentPane().setLayout(new FlowLayout());

		JComboBox<String> cb = new JComboBox<String>();
		cb.addItem("ComboA");
		cb.addItem("ComboB");
		cb.addItem("ComboC");
		cb.addActionListener(this);
		getContentPane().add(cb);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JComboBoxTest");
		setSize(200, 120);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		JComboBox<?> cb = (JComboBox<?>)e.getSource();
		System.out.println(cb.getSelectedItem());
	}
	public static void main(String[] args) {
		new JComboBoxTest();
	}
}