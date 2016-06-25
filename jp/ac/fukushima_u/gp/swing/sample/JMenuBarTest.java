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
		//�g�����[�j���[�o�[��A�C�e�����Ђƒʂ�C���X�^���X��
		//�Ȃ��C���j���[�o�[�̊K�w�\���͖؍\���ł���C���ɓ�������̂����j���[�o�[
		//�t�ɓ�������̂��A�C�e����`�F�b�N�{�b�N�X�A�C�e���ł���C
		//����ȊO�̒��ԗv�f�����j���[�ɂȂ�
		//���ꂼ��C�q�ɂ�����C���X�^���X��e�ƂȂ�C���X�^���X��add���Ă���
		//�������C���j���[�o�[�͖{���Ƀo�[�ł��������̂ŁC
		//�u�t�@�C���v��u�\���v�Ƃ�������ʓI�Ȍ����Ă��郁�j���[�́C
		//�o�[��add���ꂽ���j���[����ɂȂ�
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("File");
		JMenuItem menuOpen = new JMenuItem("Open");
		JMenuItem menuExit = new JMenuItem("Exit");
		JMenu menuView = new JMenu("View");
		JCheckBoxMenuItem menuTool = new JCheckBoxMenuItem("Tool Bar");
		JMenu menuSize = new JMenu("Size");
		JMenuItem menuSizeLarge = new JMenuItem("Large");
		JMenuItem menuSizeSmall = new JMenuItem("Small");

		//�j�[���j�b�N�ݒ蒆
		menuFile.setMnemonic('F');
		menuOpen.setMnemonic('O');
		menuExit.setMnemonic('x');
		menuView.setMnemonic('V');
		menuTool.setMnemonic('T');
		menuSize.setMnemonic('S');
		menuSizeLarge.setMnemonic('L');
		menuSizeSmall.setMnemonic('S');

		//�A�C�e���Ɋւ��āCActionListener��o�^����
		menuOpen.addActionListener(this);
		menuExit.addActionListener(this);
		menuTool.addActionListener(this);
		menuSizeLarge.addActionListener(this);
		menuSizeSmall.addActionListener(this);

		//�E�B���h�E�̃��[�g�y�C�����擾���C�����Ƀ��j���[�o�[���Z�b�g
		//���̌�C���j���[�o�[�⑼�̗v�f�ɑ΂��C���ɗv�f���Z�b�g���Ă���
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