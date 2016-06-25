package jp.ac.fukushima_u.gp.Graphics.vacuumer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class VacuumerWindow extends JFrame implements ComponentListener, KeyListener{

	private static int wid = 500;
	private static int hei = 300;
	private UnitControl uc;

	//ダブルバッファリング用
	Image buf;
	Graphics bufG;

	public VacuumerWindow() {
		this(VacuumerWindow.wid, VacuumerWindow.hei);
	}

	public VacuumerWindow(int width, int height) {
		this.setSize(width, height);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(this);
		this.addComponentListener(this);
	}

	public void setUnitControl(UnitControl uc) {
		this.uc = uc;
	}

	public void winInit() {

	}

	public void fillTriangle(int x, int y, int h, int t, double theta, Graphics g) {
		//		int sx = (int) (x - h * Math.cos(theta));
		//		int sy = (int) (y + h * Math.sin(theta));

	}

	//ここからダブルバッファリング
	public void make() {
		buf = createImage(this.getSize().width, this.getSize().height);
		bufG = buf.getGraphics();
	}

	// 再描画
	public void update(Graphics g) {
		paint(g);
	}

	public synchronized void paint(Graphics g) {
		//ダブルバッファリング用の領域が確保されていない場合
		if(this.buf==null || this.bufG==null){
			this.make();
		}
		
		
		Dimension size = this.getSize();

		//ウィンドウを黒で塗りつぶし
		bufG.setColor(Color.black);
		bufG.fillRect(0, 0, size.width, size.height);
		
		if(uc.PLAYER_UNIT_SET==null){
			return;
		}
		
		for (int i = 0; i < uc.PLAYER_UNIT_SET.size(); i++) {
			Unit u = uc.PLAYER_UNIT_SET.get(i);
			bufG.setColor(u.getColor());
			bufG.fillOval((int) u.getX(), (int) u.getY(), (int) (u.getSize() * (1 + 0.5
					* (u.life - UnitControl.DEFAULT_LIFE) / (UnitControl.DEFAULT_LIFE))),
					(int) (u.getSize() * (1 + 0.5 * (u.life - UnitControl.DEFAULT_LIFE) / (UnitControl.DEFAULT_LIFE))));
		}

		for (int i = 0; i < uc.ENEMY_UNIT_SET.size(); i++) {
			Unit u = uc.ENEMY_UNIT_SET.get(i);
			bufG.setColor(u.getColor());
			//enemy lifeによる補正なし
			bufG.fillRect((int) u.getX(), (int) u.getY(), (int) (u.getSize()), (int) (u.getSize()));

			//enemy lifeによる補正あり
//			bufG.fillRect((int) u.getX(), (int) u.getY(), (int) (u.getSize()
//					* (1.0 + ((u.life - UnitControl.DEFAULT_ENEMY_LIFE) / UnitControl.DEFAULT_ENEMY_LIFE))),
//					(int) (u.getSize()
//					* (1.0 + ((u.life - UnitControl.DEFAULT_ENEMY_LIFE) / UnitControl.DEFAULT_ENEMY_LIFE))));
		}

		g.drawImage(buf, 0, 0, this);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		//リサイズされた時に実行されるメソッド

		bufG.dispose();
		buf.flush();
		this.make();

		//元のサイズに対する今のサイズの倍率を求める
		uc.WIN_MAGN=(this.getSize().width*this.getSize().height)/(VacuumerMain.width*VacuumerMain.height);
		
		//リサイズ時、ウィンドウ外になったエネミーの消去
		uc.sizeOutEnemy(getSize());

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
