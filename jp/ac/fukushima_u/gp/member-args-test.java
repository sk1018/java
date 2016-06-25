package jp.ac.fukushima_u.gp;

class Account {
	// �����o�[�ϐ�
	int balance;
	
	void setBalance(int aBalance) {
		// �����̃N���X�̃����o�[�ϐ��ւ̃A�N�Z�X
		balance = aBalance;
	}
	int getBalance() {
		// �����̃N���X�̃����o�[�ϐ��ւ̃A�N�Z�X
		return balance;
	}
}
class AccountManager {
	void transfer(Account account, int ammount) {
		// �I�u�W�F�N�g�̃����o�[�ϐ��ւ̃A�N�Z�X
		account.balance += ammount;
	}
}
class AccountDemo {
	public static void main(String[] args) {
		// �I�u�W�F�N�g�̐���
		Account obj = new Account();
		// �I�u�W�F�N�g�̃��\�b�h�̗��p
		obj.setBalance(1000);
		System.out.println(obj.getBalance());
		// �I�u�W�F�N�g�̐���
		AccountManager obj2 = new AccountManager();
		// �I�u�W�F�N�g�̃��\�b�h�̗��p
		obj2.transfer(obj, 100);
		System.out.println(obj.getBalance());
		
		
		
		// �I�u�W�F�N�g�̐���
		AccountManager obj3 = new AccountManager();
		// �I�u�W�F�N�g�̃��\�b�h�̗��p
		obj3.transfer(obj, 100);
		System.out.println(obj.getBalance());
	}
}