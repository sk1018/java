import java.applet.Applet;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;

public class AppletTest extends Applet{
  public void paint(Graphics g){
    g.drawString("�t�@�C���ǂݍ��݃e�X�g1",20,10); 
  	fileRead();
  }
	
	private void fileRead() {
		try{
            FileReader f = new FileReader("C:\\hoge\\test.txt");
            BufferedReader b = new BufferedReader(f);
            String s;
            while((s = b.readLine())!=null){
                System.out.println(s);
            }
            b.close();
        }catch(Exception e){
        	e.printStackTrace();
        }
		
	}
}
