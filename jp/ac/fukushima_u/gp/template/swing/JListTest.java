package jp.ac.fukushima_u.gp.template.swing;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JListTest extends JFrame implements ListSelectionListener {
	JListTest() {
		getContentPane().setLayout(new FlowLayout());

		String[] data = { "ListA", "ListB", "ListC", "ListD" };
		JList<?> lst = new JList<Object>(data);
		lst.addListSelectionListener(this);
		getContentPane().add(lst);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JListTest");
		setSize(200, 120);
		setVisible(true);
	}
	public void valueChanged(ListSelectionEvent e) {
		JList<?> lst = (JList<?>)e.getSource();
		System.out.println(lst.getSelectedValue());
	}
	public static void main(String[] args) {
		new JListTest();
	}
}