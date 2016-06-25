package jp.ac.fukushima_u.gp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainClass extends JFrame implements ChangeListener, ActionListener{
	
	final int pn = 1;
	
	int f = 0;
	
	JButton ok;
	JTextArea ta;
	
	JRadioButton nampreSolve;
	
	public static void main(String[] args) {
		
		MainClass win = new MainClass();
		win.setVisible(true);
		
	}
	
	public MainClass() {
		setTitle("Main Window");
		setSize(400, pn * 10 + 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout());
		ok = new JButton();
		ok.addActionListener(this);
		getContentPane().add("South", ok);
		
		getContentPane().add("North", new JLabel("test"));
		
		JPanel left = new JPanel();
		JPanel right = new JPanel();
		
		JPanel center = new JPanel();
		add("Center", center);
		center.setLayout(new GridLayout(1, 2));
		JScrollPane lsp = new JScrollPane(left);
		center.add(lsp);
		center.add(right);
		
		left.setLayout(new GridLayout(1, pn));
		right.setLayout(new CardLayout());
		
		ta = new JTextArea();
		JScrollPane scrollpane = new JScrollPane(ta);
		right.add(scrollpane);
		
		ButtonGroup bg = new ButtonGroup();
		
		nampreSolve = new JRadioButton("ナンプレ解読");
		nampreSolve.addChangeListener(this);
		left.add(nampreSolve);
		bg.add(nampreSolve);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			if (nampreSolve.isSelected()) {
				setVisible(false);
				jp.ac.fukushima_u.gp.nampre.First.main(null);
			}
		}
		
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		
		//JRadioButton rb = (JRadioButton)e
		
		if (nampreSolve.isSelected()) {
			ta.setText(new String("標準的な9*9タイプのナンプレを解読するプログラム"));
			
		}
		
	}
	
}
