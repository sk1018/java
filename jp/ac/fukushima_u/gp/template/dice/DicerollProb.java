package jp.ac.fukushima_u.gp.template.dice;


public class DicerollProb{

	int DICE_NUM = 3;
	int[] DICE_SUM;

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		DicerollProb pb = new DicerollProb();
		pb.init();
		pb.probCalc(0, 0);
		pb.output();
	}

	public void init() {
		this.DICE_SUM = new int[this.DICE_NUM * 6];
		for (int i = 0; i < this.DICE_NUM * 6; i++) {
			this.DICE_SUM[i] = 0;
		}

	}

	public void probCalc(int dice, int sum) {
		if (dice >= this.DICE_NUM) {
			this.DICE_SUM[sum - 1]++;
			return;
		}
		for (int i = 1; i <= 6; i++) {
			this.probCalc(dice + 1, sum + i);
		}
	}

	public void output() {
		System.out.println("Diceroll pattern = " +String.format("%.0f" ,Math.pow(6, (double) this.DICE_NUM)));

		for (int i = this.DICE_NUM-1; i < this.DICE_NUM * 6; i++) {
			System.out.println("sum " + String.format("%2d", i + 1) + " = " + this.DICE_SUM[i]);
		}
	}

}
