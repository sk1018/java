//���g���ړ���\��ɗ\�߃R�s�[
copy compileB.bat %1\compileB.bat

//�������̃f�B���N�g���Ɉړ�
cd %1

//�J�����g�f�B���N�g�����̃t�@�C�����R���p�C��
for %%s (*.java) do javac %%s

//�J�����g�f�B���N�g�����̃T�u�t�@�C����������Ƃ���
//�������g�̍ċA�Ăяo�����s��
for /d %%s (*) do cd compileB.bat %%s

//�Ђƒʂ��Ƃ��I��������AcompileB.bat�̍폜���s���A��ʃf�B���N�g����
del compileB.bat
cd ..

