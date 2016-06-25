package jp.ac.fukushima_u.gp;

//�R���X�g���N�^�̃I�[�o�[���[�h�e�X�g

class OverLoad2{

	//�����o�ϐ���錾����
	public String name;

	//��̂Ȃ��R���X�g���N�^
	public OverLoad2(){
		System.out.println("��Ȃ��̃R���X�g���N�^");
		name = "�w��Ȃ�";		
	}

	//��1����R���X�g���N�^
	public OverLoad2(String arg){
		System.out.println("��t���̃R���X�g���N�^");
		//�����o�ϐ��Ɉ�Ŏw�肳�ꂽ�l�����
		name = arg;		
	}

	//�����o�ϐ���\������
	public void printName(){
		System.out.println(name);
	}
}


class OverLoadCall2{
	public static void main(String[] args){
		//OverLoad2�N���X����Ȃ��ŃC���X�^���X������
		OverLoad2 overLoad2 = new OverLoad2();

		//�����o�ϐ���\������
		overLoad2.printName();

		//OverLoad2�N���X����t���ŃC���X�^���X������
		OverLoad2 overLoadArg = new OverLoad2("��w��");

		//�����o�ϐ���\������
		overLoadArg.printName();
		
	}
}