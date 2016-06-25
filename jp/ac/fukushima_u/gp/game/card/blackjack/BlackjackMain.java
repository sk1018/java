package jp.ac.fukushima_u.gp.game.card.blackjack;

import java.util.ArrayList;

import jp.ac.fukushima_u.gp.game.card.Card;

public class BlackjackMain {
	private Card card;
	private BJWindowImage bjw;
	private ArrayList<Integer> uc, dc;
	
	/*
	 * ������
	 * hit��stand�̎���wait�����Ĕ����Ɏ��Ԃ�x�点�鎖
	 * ���ʕ\���̎��Ƀ��[�U�ƃf�B�[���[�����̐���\��������Ƃ�
	 * �P���ȏ��s�����łȂ��A���[�U��f�B�[���[�̃o�[�X�g���ɂ̓o�[�X�g�ƕ\��������
	 * �Q�[���̐��т�\��������(��{�͏������Ə��s�B��̓o�[�X�g���Ƃ����σq�b�g���Ƃ����̕ӂ̐��l�I�Ȃ���)
	 * ��L�̂��̂��o�͂���ۂɁA���C��window�ɃX�y�[�X����邩�A�T�u�E�B���h�E����邩�i���₷���̊֌W�ŁA���C���ɕt���������ȁj
	 * 
	 * 1�̃N���X��1�̐Ӗ��A�Ƃ�����������A�f�B�[���[�ƃ��[�U�̃N���X�����̂��L�肩��
	 * �������͂ǂ�����v���C���[�ł���Ƃ����_���狤�ʂ̃v���C���[�N���X���쐬����Ƃ�
	 * �v���C���[�̃C���^�[�t�F�[�X���A�u�X�g���N�g�����C����������E�p�������f�B�[���[�ƃ��[�U���쐬�Ƃ�
	 */
	
	/**
	 * �u���b�N�W���b�N���s���ۂ�main�ƂȂ�֐�
	 * ��{�����ŏ������s���Awindow�N���X��ʂ��ă��[�U�Ƃ��Ƃ������
	 *
	 * main���\�b�h�̓��e����ӂ݂�ɁA�����炭�O������C���X�^���X�����邾���Ŏ��s�\
	 * �R���X�g���N�^��private����public�ɂ���K�v�͂��邯��
	 * @param args main���\�b�h�p�̕s�v�Ȉ���
	 */
	public static void main(String[] args) {
		BlackjackMain bjm = new BlackjackMain();
	}
	
	private void setIns(BlackjackMain bjm) {
		bjw.setIns(bjm);
	}
	
	private BlackjackMain() {
		//�J�[�h�̎R�Ɗ�{window���Z�b�g
		card = new Card();
		card.setJokerNum(0);
		card.initCard();
		bjw = new BJWindowImage();
		this.setIns(this);
		
		//��D�p�̃��X�g�̊m��
		uc = new ArrayList<Integer>();
		dc = new ArrayList<Integer>();
		
		//�ŏ��̃J�[�h�̃h���[
		this.initCardDraw();
		
		//window�̉���
		bjw.setVisible(true);
		
	}
	
	private void initCardDraw() {
		int t;
		t = card.drawCard();
		dc.add(t);
		bjw.setDealerCard(t);
		t = card.drawCard();
		uc.add(t);
		bjw.setUserCard(t);
		t = card.drawCard();
		uc.add(t);
		bjw.setUserCard(t);
		bjw.sumRenew(sumCheck(uc), sumCheck(dc));
	}
	
	void hitAction() {
		int t = card.drawCard();
		uc.add(t);
		bjw.setUserCard(t);
		bjw.sumRenew(sumCheck(uc), sumCheck(dc));
		bjw.setVisible(true);
		if (this.sumCheck(uc) > 21) {
			bjw.subWin("YOU LOSE!");
		}
	}
	
	public synchronized void sleep(long msec)
	{ //�w��~���b���s���~�߂郁�\�b�h
		try
		{
			wait(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	void standAction() {
		while (this.sumCheck(dc) < 18) {
			int t;
			t = card.drawCard();
			dc.add(t);
			bjw.setDealerCard(t);
			bjw.sumRenew(sumCheck(uc), sumCheck(dc));
			//bjw.setVisible(true);
			//������ւ��wait�����āA�������߂���̂��L�肩��
			
			this.sleep(1000);
		}
		
		if (this.sumCheck(dc) > 21 || this.sumCheck(dc) < this.sumCheck(uc)) {
			bjw.subWin("YOU WIN!");
		}
		else if (this.sumCheck(dc) >= this.sumCheck(uc)) {
			bjw.subWin("YOU LOSE!");
		}
	}
	
	/**
	 * �^����ꂽ��D�p�̃��X�g�̍��v�l��Ԃ�
	 * @param l ������s��������D�p�̃��X�g
	 * @return ���v�l��Ԃ�
	 */
	private int sumCheck(ArrayList<Integer> l) {
		int sum = 0;
		int f = 0;
		for (int i = 0; i < l.size(); i++) {
			sum += l.get(i) % 13 + 1;
			
			//�G�D���o�����̏���
			if (l.get(i) % 13 + 1 > 10) {
				sum -= l.get(i) % 13 + 1 - 10;
			}
			
			//A���o�����̏���
			if (l.get(i) % 13 == 0) {
				f++;
				sum += 10;
			}
			while (sum > 21 && f > 0) {
				sum -= 10;
				f--;
			}
		}
		return sum;
	}
	
}
