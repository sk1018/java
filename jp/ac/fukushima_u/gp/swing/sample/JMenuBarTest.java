package jp.ac.fukushima_u.gp.swing.sample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class JMenuBarTest extends JFrame implements ActionListener {
	JMenuBarTest() {
		//使うメーニューバーやアイテムをひと通りインスタンス化
		//なお，メニューバーの階層構造は木構造であり，根に当たるものがメニューバー
		//葉に当たるものがアイテムやチェックボックスアイテムであり，
		//それ以外の中間要素がメニューになる
		//それぞれ，子にあたるインスタンスを親となるインスタンスにaddしていく
		//ただし，メニューバーは本当にバーでしか無いので，
		//「ファイル」や「表示」といった一般的な見えているメニューは，
		//バーにaddされたメニューからになる
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenuItem menuOpen = new JMenuItem("Open");
		JMenuItem menuExit = new JMenuItem("Exit");
		JMenu menuView = new JMenu("View");
		JCheckBoxMenuItem menuTool = new JCheckBoxMenuItem("Tool Bar");
		JMenu menuSize = new JMenu("Size");
		JMenuItem menuSizeLarge = new JMenuItem("Large");
		JMenuItem menuSizeSmall = new JMenuItem("Small");

		//ニーモニック設定中
		menuFile.setMnemonic('F');
		menuOpen.setMnemonic('O');
		menuExit.setMnemonic('x');
		menuView.setMnemonic('V');
		menuTool.setMnemonic('T');
		menuSize.setMnemonic('S');
		menuSizeLarge.setMnemonic('L');
		menuSizeSmall.setMnemonic('S');

		//アイテムに関して，ActionListenerを登録する
		menuOpen.addActionListener(this);
		menuExit.addActionListener(this);
		menuTool.addActionListener(this);
		menuSizeLarge.addActionListener(this);
		menuSizeSmall.addActionListener(this);

		//ウィンドウのルートペインを取得し，そこにメニューバーをセット
		//その後，メニューバーや他の要素に対し，順に要素をセットしていく
		getRootPane().setJMenuBar(menuBar);
		menuBar.add(menuFile);
		menuFile.add(menuOpen);
		menuFile.add(menuExit);
		menuBar.add(menuView);
		menuView.add(menuTool);
		menuView.add(menuSize);
		menuSize.add(menuSizeLarge);
		menuSize.add(menuSizeSmall);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("JMenuBarTest");
		setSize(200, 120);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		System.out.println("[" + e.getActionCommand() + "]");
	}
	public static void main(String[] args) {
		new JMenuBarTest();
	}
}