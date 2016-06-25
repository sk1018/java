package jp.ac.fukushima_u.gp.Graphics.encampment_game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class CellControl {
	private Cell[][] CELL_GROUP;//一次元がy(縦)二次元がx(横)
	private int CELL_X;
	private int CELL_Y;
	private int CELL_SIZE;

	//	//チェック用
	//	int cellcount=-1;
	//	int framecount=0;

	public void init() {

	}

	public CellControl() {
		this.init();
	}

	public CellControl(int x, int y) {
		this.init();
		this.setCellNum(x, y);

	}

	public synchronized boolean[][] getCurrentLife() {
		boolean[][] current_life = new boolean[this.CELL_Y][this.CELL_X];

		for (int i = 0; i < this.CELL_Y; i++) {
			for (int l = 0; l < this.CELL_X; l++) {
				current_life[i][l] = this.CELL_GROUP[i][l].getLife();
			}
		}
		return current_life;
	}

	public int getCellSize() {
		return this.CELL_SIZE;
	}

	public int[] getCellNum() {
		int[] n = new int[2];
		n[0] = this.CELL_X;
		n[1] = this.CELL_Y;

		return n;
	}

	public CellControl(int x, int y, int size) {
		this.init();
		this.setCellNum(x, y);

		this.CELL_SIZE = size;
	}

	public void setLife(double p) {
		Random r = new Random();

		for (int i = 0; i < this.CELL_Y; i++) {
			for (int l = 0; l < this.CELL_X; l++) {
				double n = r.nextDouble();
				boolean life;
				if (n <= p) {
					life = true;
				}
				else {
					life = false;
				}
				this.CELL_GROUP[i][l].setLife(life);
			}
		}

	}

	public void setColor(boolean f) {
		Random r = new Random();

		ArrayList<Color> color_set = new ArrayList<Color>();

		color_set.add(Color.blue);
		color_set.add(Color.cyan);
		color_set.add(Color.green);
		color_set.add(Color.magenta);
		color_set.add(Color.orange);
		color_set.add(Color.pink);
		color_set.add(Color.red);
		color_set.add(Color.white);
		color_set.add(Color.yellow);

		for (int i = 0; i < this.CELL_Y; i++) {
			for (int l = 0; l < this.CELL_X; l++) {
				if (f) {
					this.CELL_GROUP[i][l].setColor(color_set.get(r.nextInt(color_set.size())));
				}
				else {
					this.CELL_GROUP[i][l].setColor(Color.yellow);
				}

			}
		}

	}

	public void setCellNum(int x, int y) {
		this.CELL_X = x;
		this.CELL_Y = y;
		this.CELL_GROUP = new Cell[y][x];

		for (int i = 0; i < y; i++) {
			for (int l = 0; l < x; l++) {
				this.CELL_GROUP[i][l] = new Cell();
			}
		}
		this.setCellAround();
	}

	public Color getColor(int y, int x) {
		return this.CELL_GROUP[y][x].getColor();
	}

	public void setCellAround() {
		for (int i = 0; i < this.CELL_Y; i++) {
			for (int l = 0; l < this.CELL_X; l++) {
				for (int j = 0; j < 8; j++) {
					int x = 0, y = 0;
					if (j < 3) {
						y = i - 1;
						x = l + j - 1;
					}
					else if (j < 5) {
						y = i;
						if (j == 3) {
							x = l - 1;
						}
						else {
							x = l + 1;
						}
					}
					else if (j < 8) {
						y = i + 1;
						x = l + j - 6;
					}

					if (y < 0) {
						y = this.CELL_Y - 1;
					}
					else if (y >= this.CELL_Y) {
						y = 0;
					}
					if (x < 0) {
						x = this.CELL_X - 1;
					}
					else if (x >= this.CELL_X) {
						x = 0;
					}

					this.CELL_GROUP[i][l].c[j] = this.CELL_GROUP[y][x];
				}
			}
		}
	}

	public synchronized void cellBurst() {
		int cellNum = this.CELL_X * this.CELL_Y;

		int burstNum = cellNum / 10;
		Random r = new Random();
		for (int i = 0; i < burstNum; i++) {
			int x = r.nextInt(CELL_X);
			int y = r.nextInt(CELL_Y);
			this.CELL_GROUP[y][x].setLife(true);
		}

	}

	public synchronized void nextStep() {
		boolean[][] nextLife = new boolean[this.CELL_Y][this.CELL_X];
		for (int i = 0; i < this.CELL_Y; i++) {
			for (int l = 0; l < this.CELL_X; l++) {
				nextLife[i][l] = this.CELL_GROUP[i][l].nextLife();
			}
		}

		for (int i = 0; i < this.CELL_Y; i++) {
			for (int l = 0; l < this.CELL_X; l++) {
				this.CELL_GROUP[i][l].setLife(nextLife[i][l]);
			}
		}
	}

}

class Cell {
	boolean SIMPLE_ATTACK = true;
	boolean life = false;
	int X;
	int Y;
	Cell[] c = new Cell[8];
	Color CELL_COLOR;
	ArrayList<Color> AROUND_COLOR = new ArrayList<Color>();

	boolean getLife() {
		return this.life;
	}

	void setLife(boolean life) {
		this.life = life;
	}

	void setColor(Color c) {
		this.CELL_COLOR = c;
	}

	Color getColor() {
		return this.CELL_COLOR;
	}

	boolean nextLife() {
//		double p = 0.3;
		this.AROUND_COLOR.clear();
		for (int i = 0; i < 8; i++) {
			if (!(i == 1 || i == 3 || i == 4 || i == 6))
				continue;
			if (c[i].getLife()) {
				this.AROUND_COLOR.add(c[i].getColor());
			}
		}
		Random r = new Random();
		//			if (c[1].getLife() || c[3].getLife() || c[4].getLife() || c[6].getLife()) {
		//				if (r.nextDouble() < 0.3) {
		//					return true;
		//				}
		//				else {
		//					return false;
		//				}
		//			}
		//			else {
		//				return false;
		//			}

		//		for (int i = 0; i < 8; i++) {
		//			if (c[i].getLife() && (c[i].getColor() != this.CELL_COLOR || this.life == false)) {
		//				if(c[i].getColor()==Color.yellow)p=0.5;
		//				else p=0.3;
		//				if (r.nextDouble() < p) {
		//					this.setColor(c[i].getColor());
		//					return true;
		//				}
		//			}
		//		}

		if (!this.AROUND_COLOR.isEmpty()) {
			double f = r.nextDouble();
			if ((f < 0.8 &&this.life)|| (f < 0.5 && !this.life)) {
				this.setColor(this.AROUND_COLOR.get(r.nextInt(this.AROUND_COLOR.size())));
				return true;
			}
		}

		return this.life;

	}
}
