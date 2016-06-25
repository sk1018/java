package jp.ac.fukushima_u.gp.swing.sample;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SwingAppMain implements ActionListener{
	private JFrame mainFrame;
	private Container contentPane;
	private JTextField textField;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JPanel buttonPane;
	private JButton addButton;
	private JButton clearButton;
	
	public SwingAppMain() {
		mainFrame = new JFrame("");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(320, 200);
		mainFrame.setLocationRelativeTo(null);
		contentPane = mainFrame.getContentPane();
		textField = new JTextField();
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		addButton = new JButton("add");
		clearButton = new JButton("clear");
		addButton.addActionListener(this);
		clearButton.addActionListener(this);
		buttonPane = new JPanel();
		buttonPane.add(addButton);
		buttonPane.add(clearButton);
		contentPane.add(textField, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		mainFrame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == addButton) {
			textArea.append(textField.getText() + "\n");
		}
		if (event.getSource() == clearButton) {
			textArea.setText(null);
		}
	}
	
	public static void main(String[] args) {
		new SwingAppMain();
	}
}