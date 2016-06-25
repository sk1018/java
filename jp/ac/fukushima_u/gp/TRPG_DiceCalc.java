package jp.ac.fukushima_u.gp;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * 「2d6」みたいなダイスロールをする場合に、期待値や各値の出る確率を算出したり、実際に適当な回数乱数を発生させ、
 * 実際にn回やったらこうなった、みたいなのを出力させる
 * @author seiya
 *
 */
public class TRPG_DiceCalc extends JFrame implements ActionListener {
	
	int wid = 300;
	int hei = 200;
	
	JButton B_calc, B_random;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		//		TRPG_DiceCalc tdc = new TRPG_DiceCalc();
		//		tdc.setVisible(true);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			String in = br.readLine();
			String[] in2 = in.split("d");
			
			int a = Integer.valueOf(in2[0]);
			int b = Integer.valueOf(in2[1]);
			
			TRPG_DiceCalc dc = new TRPG_DiceCalc();
			
			HashMap<Integer, Integer> map = dc.calc(a, b);
			
			Set<Integer> s = map.keySet();
			Iterator<Integer> ite = s.iterator();
			
			while (ite.hasNext()) {
				//for(int i=a;i<=a*b;i++){
				Integer e = ite.next();
				
//				System.out.println(e.intValue() + "=" + map.get(e));
				System.out.printf("%4d=%3d\n", e.intValue(),map.get(e).intValue());
			}
			
		} catch (Exception e) {
			e.getStackTrace();
		}
		
	}
	
	//計算をする入り口
	public HashMap<Integer, Integer> calc(int diceNum, int size) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.clear();
		
		this.calcRecursive(map, diceNum, size, 0);
		
		return map;
		
	}
	
	private void calcRecursive(HashMap<Integer, Integer> map, int diceNum, int size, int sum) {
		//ダイスの数が0の場合、ダイスを指定回数分振ったとして、その時の合計値キーとして、
		//値の部分の数字をインクリメントして置き換える
		if (diceNum == 0) {
			//この値が出た回数の初期値(初回なので1)
			int n = 1;
			//この値が出た回数を保持しているインスタンスを取得
			Integer count = map.get(new Integer(sum));
			
			//初めてこの値が出たならば、nullが帰ってくるので初期値の1のまま
			//すでに出ているならば、今まで出た回数を持つインスタンスが帰るため、それをインクリメントした値を設定
			if (count != null) {
				n += count.intValue();
				
			}
			map.put(new Integer(sum), new Integer(n));
			
			return;
		} else {
			//ダイスの数が0以外の場合まだ振ってないダイスがあるということなので、ダイスを振り、その値を加算して再帰する
			for (int i = 1; i <= size; i++) {
				//現在までの合計値をコピーした一時変数を用意
				//そのまま加算しちゃうと合計値がどんどん変わってしまうため
				int n = sum;
				//現在までの合計値に、今回分のダイスを振った値を加算
				n += i;
				//今回ダイスを振った分、ダイスの数をデクリメントし、合計値を加算した状態で再帰する
				this.calcRecursive(map, diceNum - 1, size, n);
			}
			return;
		}
	}
	
	public TRPG_DiceCalc() {
		this.setBounds((Toolkit.getDefaultToolkit().getScreenSize().width * this.wid) / 2, (Toolkit.getDefaultToolkit()
				.getScreenSize().height * this.hei) / 2, this.wid, this.hei);
		this.setLayout(new BorderLayout());
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.B_calc = new JButton("calc");
		this.B_random = new JButton("random");
		
		this.add(this.B_calc, BorderLayout.SOUTH);
		this.add(this.B_random, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
}
