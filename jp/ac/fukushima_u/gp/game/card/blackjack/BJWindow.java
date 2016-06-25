package jp.ac.fukushima_u.gp.game.card.blackjack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;

import jp.ac.fukushima_u.gp.game.card.Card;

class BJWindow extends JFrame implements ActionListener, ComponentListener {

	private JButton stand, hit, subOk;
	protected Panel pd, pu, pb;
	//	private JFrame subWin;
	private BlackjackMain bjm;
	private JLabel usum, dsum;
	private JSplitPane jsp;

	private JDialog subWin;

	/**
	 * 最初に表示されるウィンドウを生成するコンストラクタ
	 */
	BJWindow() {
		//wu\indowのサイズを指定
		int h = 300;
		int w = 400;

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

		this.setTitle("Blackjack");
		//this.setSize(w, h);
		this.setBounds(d.width / 2 - w / 2, d.height / 2 - h / 2, w, h);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//レイアウトの設定と、必要なボタン（check,draw）の配置
		this.setLayout(new BorderLayout());
		jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.getContentPane().add("Center", jsp);
		this.getContentPane().add("North", new java.awt.Label("Window"));

		pd = new Panel();
		pu = new Panel();
		pb = new Panel();
		this.getContentPane().add("South", pb);

		pb.setLayout(new GridLayout(1, 2));
		hit = new JButton("hit");
		stand = new JButton("stand");
		hit.addActionListener(this);
		stand.addActionListener(this);
		pb.add(stand);
		pb.add(hit);
		jsp.setLeftComponent(pd);
		jsp.setRightComponent(pu);
		jsp.setDividerLocation(0.5);

		pd.setLayout(new FlowLayout());
		pu.setLayout(new FlowLayout());
		dsum = new JLabel("合計 ");
		usum = new JLabel("合計 ");
		pd.add(dsum);
		pu.add(usum);

		this.setVisible(true);
		this.addComponentListener(this);
		//		ThreadTest tt = new ThreadTest();
		//		tt.setIns(this);
		//		tt.start();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == subOk) {
			subWin.setVisible(false);
			this.setVisible(false);
			BlackjackMain.main(null);
		}

		if (e.getSource() == hit) {
			bjm.hitAction();
		}
		if (e.getSource() == stand) {
			//ここで直接standActionを呼ぶと、
			//描画用のスレッドでstandActionが実行されてしまい、
			//standActionメソッドの処理が完了するまで描画が行われなくなる
			//それを回避するために、別のスレッドを作成し、そちらの方でstandActionメソッドを実行させる
			//可能ならメインスレッドを「waitさせて、ここで起動」や
			//「ハンドラ？とかのメッセージで起動」させる形のほうがいいかも
			//			bjm.standAction();
			subThread st = new subThread(bjm);
			st.start();
		}

	}

	//ActionListenerから発生したイベントの処理と、描画系の処理を同時に行うために、
	//ActionEventを別スレッドで実行するための別スレッドクラス
	private class subThread extends Thread {
		private BlackjackMain bjm;

		private subThread(BlackjackMain bjm) {
			this.bjm = bjm;
		}

		@Override
		public void run() {
			this.bjm.standAction();
		}
	}

	void setIns(BlackjackMain bjm) {
		this.bjm = bjm;
	}

	void setDealerCard(int c) {
		//shdc-j
		char s;

		s = Card.getSuit(c);
		int card = c % 13 + 1;
		pd.add(new Label(s + ":" + card + " "));
	}

	void setUserCard(int c) {
		char s = Card.getSuit(c);
		int card = c % 13 + 1;
		pu.add(new Label(s + ":" + card + " "));
	}

	void subWin(String t) {
		int w = 300;
		int h = 100;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		subWin = new JDialog(this, "result", true);
		subWin.setAlwaysOnTop(true);
		subWin.setBounds(d.width / 2 - w / 2, d.height / 2 - h / 2, w, h);
		subWin.getContentPane().setLayout(new BorderLayout());
		subWin.add("Center", new JLabel(t));
		subOk = new JButton("ok");
		subOk.addActionListener(this);
		subWin.add("South", subOk);
		subWin.setVisible(true);
	}

	void sumRenew(int uc, int dc) {
		int t;
		String s;

		t = uc;
		if (t < 10) {
			s = " User :合計 0" + t;
		}
		else {
			s = " User :合計 " + t;
		}
		usum.setText(s);

		t = dc;
		if (t < 10) {
			s = "Dealer:合計 0" + t;
		}
		else {
			s = "Dealer:合計 " + t;
		}
		dsum.setText(s);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		jsp.setDividerLocation(0.5);
		this.setVisible(true);

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
