package jp.ac.fukushima_u.gp.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class DataFlowImage extends JFrame {
	
	/*
	 * �f�[�^�̓]�����Ɋւ��ẮC�ɗ͗e�ʃI�[�o�[�������ׂ��C
	 * �������݁C�v�Z�C�ǂݍ��݂̏��Ɏ��s����
	 * �������݂Ɋւ��ẮC��ԉ��̃p�[�c���珇�ɁC
	 * ���݂̃p�[�c�������̗ʂ����Z���C���̕�������̃p�[�c�ɉ��Z����
	 * 
	 * �v�Z�Ɋւ��ẮC�ŏ�ʂ̃p�[�c�̂ݎ��s����
	 * ���̍ۂɂ͓���̑��x�ōď�ʃp�[�c���̓ǂݍ��݃f�[�^���������݃f�[�^�ɕϊ�����
	 * 
	 * �ǂݍ��݂ɂ��Ă͍ŏ�ʂ��珇�ɍs��
	 * ����̃p�[�c���猸�Z���s���C���̕��̃f�[�^�ʂ����g�ɉ��Z����
	 */
	class Parts {
		double MAX_CAP;
		double READ_DATA;
		double WRITE_DATA;
		
		double MAX_SPEED;
		double READ_SPEED;
		double WRITE_SPEED;
		
		Parts UPPER_PARTS = null;
		Parts LOWER_PARTS = null;
		
		boolean TOP = false;
		
		void setUPPER_PARTS(Parts parts) {
			this.UPPER_PARTS = parts;
		}
		
		void setLOWER_PARTS(Parts parts) {
			this.LOWER_PARTS = parts;
		}
		
		synchronized void dataWrite() {
			//���ۂɓ]�����s����
			//��{�͏������ݑ��x
			double data = this.WRITE_SPEED;
			
			//���Ƀp�[�c������ꍇ�C�󂫗e�ʂ𒴂��Ȃ��͈͂ɐݒ�
			if (this.LOWER_PARTS != null) {
				if (data > LOWER_PARTS.FreeCap()) {
					data = LOWER_PARTS.FreeCap();
				}
			}
			
			//�]���ʂɑ΂��āC���݂̃f�[�^�ʂ����Ȃ���΁C�c��̃f�[�^���ׂĂɐݒ�
			if (data > this.WRITE_DATA) {
				data = this.WRITE_DATA;
			}
			
			//����������ۂɓ]��
			//���̃p�[�c������΁C�����ɉ��Z
			if (this.LOWER_PARTS != null) {
				this.LOWER_PARTS.WRITE_DATA += data;
			}
			//���݂̕ێ��f�[�^���猸�Z
			this.WRITE_DATA -= data;
		}
		
		
		
		synchronized double FreeCap() {
			return this.MAX_CAP - (this.WRITE_DATA + this.READ_DATA);
		}
	}
	
	public static void main(String[] args) {
		DataFlowImage df = new DataFlowImage();
		
		df.setVisible(true);
	}
	
	private DataFlowImage() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension winsize = new Dimension(800, 500);
		this.setBounds((d.width - winsize.width) / 2, (d.height - winsize.height) / 2, winsize.width, winsize.height);
	}
	
	public void paint(Graphics g) {
		Dimension size = this.getSize();
		int centerline = size.width / 2;
		g.setColor(Color.white);
		
		g.fillRect(0, 0, size.width, size.height);
		
		g.setColor(Color.black);
		
		int linewidth = 1;
		g.fillRect(centerline - linewidth / 2, 0, linewidth, size.height);
		
		g.dispose();
	}
}
