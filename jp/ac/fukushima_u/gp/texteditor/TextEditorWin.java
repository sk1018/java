package jp.ac.fukushima_u.gp.texteditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class TextEditorWin extends JFrame{

	static int wid = 400;
	static int hei = 300;

	TextArea ta = null;

	JMenuItem miOpen;
	JMenuItem miSaveBufAs;
	JMenuItem miExit;

	public TextEditorWin() {
		this.setSize(wid, hei);
		this.setCenter();
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("word pad");

		ta = new TextArea();
		ta.setSize(wid, hei);
		this.add(ta, BorderLayout.CENTER);

		//メニューバー生成
		JMenuBar jmb = new JMenuBar();
		//バーに表示される「ファイル」とかのメニューを生成
		JMenu jmFile = new JMenu("File");
		jmb.add(jmFile);
		//「ファイル」とかのメニューにいろいろ追加
		miOpen = new JMenuItem("Open");
		jmFile.add(miOpen);

		miSaveBufAs = new JMenuItem("Save Buffer As...");
		jmFile.add(miSaveBufAs);

		miExit = new JMenuItem("Exit");
		jmFile.add(miExit);

		this.add(jmb, BorderLayout.NORTH);

		this.setTitle("new file");

		this.setVisible(true);
	}

	public void setCenter() {
		Dimension winSize = this.getSize();
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setBounds(scrSize.width / 2 - winSize.width / 2, scrSize.height / 2 - winSize.height / 2, winSize.width,
				winSize.height);
	}



}
