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
 * �u2d6�v�݂����ȃ_�C�X���[��������ꍇ�ɁA���Ғl��e�l�̏o��m�����Z�o������A���ۂɓK���ȉ񐔗����𔭐������A
 * ���ۂ�n�������炱���Ȃ����A�݂����Ȃ̂��o�͂�����
 * @author seiya
 *
 */
public class TRPG_DiceCalc extends JFrame implements ActionListener {
	
	int wid = 300;
	int hei = 200;
	
	JButton B_calc, B_random;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}
	
	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
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
	
	//�v�Z����������
	public HashMap<Integer, Integer> calc(int diceNum, int size) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.clear();
		
		this.calcRecursive(map, diceNum, size, 0);
		
		return map;
		
	}
	
	private void calcRecursive(HashMap<Integer, Integer> map, int diceNum, int size, int sum) {
		//�_�C�X�̐���0�̏ꍇ�A�_�C�X���w��񐔕��U�����Ƃ��āA���̎��̍��v�l�L�[�Ƃ��āA
		//�l�̕����̐������C���N�������g���Ēu��������
		if (diceNum == 0) {
			//���̒l���o���񐔂̏����l(����Ȃ̂�1)
			int n = 1;
			//���̒l���o���񐔂�ێ����Ă���C���X�^���X���擾
			Integer count = map.get(new Integer(sum));
			
			//���߂Ă��̒l���o���Ȃ�΁Anull���A���Ă���̂ŏ����l��1�̂܂�
			//���łɏo�Ă���Ȃ�΁A���܂ŏo���񐔂����C���X�^���X���A�邽�߁A������C���N�������g�����l��ݒ�
			if (count != null) {
				n += count.intValue();
				
			}
			map.put(new Integer(sum), new Integer(n));
			
			return;
		} else {
			//�_�C�X�̐���0�ȊO�̏ꍇ�܂��U���ĂȂ��_�C�X������Ƃ������ƂȂ̂ŁA�_�C�X��U��A���̒l�����Z���čċA����
			for (int i = 1; i <= size; i++) {
				//���݂܂ł̍��v�l���R�s�[�����ꎞ�ϐ���p��
				//���̂܂܉��Z�����Ⴄ�ƍ��v�l���ǂ�ǂ�ς���Ă��܂�����
				int n = sum;
				//���݂܂ł̍��v�l�ɁA���񕪂̃_�C�X��U�����l�����Z
				n += i;
				//����_�C�X��U�������A�_�C�X�̐����f�N�������g���A���v�l�����Z������ԂōċA����
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
