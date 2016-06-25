package jp.ac.fukushima_u.gp.study;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ListSimCalc extends JFrame implements ActionListener{
	TextField la, lb;
	Panel p;
	JButton ok;

	String sa, sb;
	
	Dialog d;
	JButton dok;

	public static void main(String[] args) {
		ListSimCalc win = new ListSimCalc();
		win.winInit();

	}

	void winInit() {
		this.p = new Panel();
		this.ok = new JButton("ok");
		this.ok.addActionListener(this);
		this.la = new TextField();
		this.lb = new TextField();

		this.setLayout(new BorderLayout());
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(la);
		p.add(lb);

		this.add(p, BorderLayout.CENTER);
		this.add(ok, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 300);
		this.setVisible(true);

	}

	public double simCalc(String sa, String sb) {
		ArrayList<String> lista = new ArrayList<String>();
		ArrayList<String> listb = new ArrayList<String>();

		String[] ssa = sa.split(",");
		String[] ssb = sb.split(",");

		lista.clear();
		for (int i = 0; i < ssa.length; i++) {
			lista.add(ssa[i]);
		}

		listb.clear();
		for (int i = 0; i < ssb.length; i++) {
			listb.add(ssb[i]);
		}

		ArrayList<ArrayList<String>> pa = this.splitList(lista);
		ArrayList<ArrayList<String>> pb = this.splitList(listb);

		return this.calc(pa, pb);

	}

	public double calc(ArrayList<ArrayList<String>> pa, ArrayList<ArrayList<String>> pb) {
		double ans = 0.0;

		for (int i = 0; i < pa.size(); i++) {
			for (int j = 0; j < pb.size(); j++) {
				if (this.listCon(pa.get(i), pb.get(j))) {
					ans = ans + 1.0;
				}
			}
		}

		ans = (ans * 2) / (pa.size() + pb.size());

		return ans;
	}

	public boolean listCon(ArrayList<String> a, ArrayList<String> b) {
		if (a.size() != b.size()) {
			return false;
		}

		for (int i = 0; i < a.size(); i++) {
			if (!(a.get(i).equals(b.get(i)))) {
				return false;
			}
		}

		return true;
	}

	public ArrayList<ArrayList<String>> splitList(ArrayList<String> list) {
		ArrayList<ArrayList<String>> p = new ArrayList<ArrayList<String>>();

		ArrayList<String> temp;
		for (int i = 2; i < list.size(); i++) {
			for (int l = 0; l + i - 1 < list.size(); l++) {
				temp = new ArrayList<String>();
				for (int j = 0; j < i; j++) {
					temp.add(list.get(l + j));
				}
				p.add(temp);
			}

		}

		return p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ

		if (e.getSource() == this.ok) {
			sa = la.getText();
			sb = lb.getText();

			double sim = this.simCalc(sa, sb);

			d = new Dialog(this);
			d.setLayout(new BorderLayout());
			dok=new JButton("ok");
			dok.addActionListener(this);
			d.add(dok,BorderLayout.SOUTH);
			d.setSize(100, 100);
			String str= new String();
			str=str+sim;
			d.add(new Label(str),BorderLayout.CENTER);
			d.setVisible(true);
		}
		
		if(e.getSource()==this.dok){
			d.setVisible(false);
		}

	}

}

class node{
	String e;
	ArrayList<node> next;

	node() {
		next = new ArrayList<node>();
	}

	node(String str) {
		this();
		this.e = new String(str);
	}

}
