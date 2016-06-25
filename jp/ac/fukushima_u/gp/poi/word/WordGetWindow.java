package jp.ac.fukushima_u.gp.poi.word;

import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;

public class WordGetWindow extends JFrame{
	
	public static void main(String[] args) {
		WordGetWindow wgw = new WordGetWindow();
		wgw.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,
				Toolkit.getDefaultToolkit().getScreenSize().height);
		wgw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		wgw.getContentPane().setLayout(new BoxLayout(wgw.getContentPane(), BoxLayout.Y_AXIS));
		//wgw.setVisible(true);
		
		try {
			String s = new String(
					"C:\\Users\\GP\\Desktop\\seiya\\Dropbox\\pro\\java\\jp\\ac\\fukushima_u\\gp\\poi\\word\\");
			
			FileInputStream fis = new FileInputStream(s + "test.doc");
			FileOutputStream out = new FileOutputStream(s + "test2.doc");
			
			HWPFDocument doc = new HWPFDocument(fis);
			
			Range r = doc.getRange();
			
			for (int x = 0; x < r.numSections(); x++) {
				Section sec = r.getSection(x);
				
				//for (int y = 0; y < sec.numParagraphs(); y++) {
				//Paragraph para = sec.getParagraph(y);
				
				//java.lang.String ss = new java.lang.String("test");
				//para.insertAfter(ss);
				//String line = para.text();
				String line = sec.text();
				
				//System.out.println("\ntest x=" + x + " y=" + y);
				System.out.print(line);
				
				//}
			}
			
			doc.write(out);
			
			fis.close();
			
		} catch (java.io.FileNotFoundException e) {
			System.out.println("");
		} catch (IOException e) {
			System.out.println("");
		}
		
	}
	
}
