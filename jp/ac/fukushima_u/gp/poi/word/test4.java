package jp.ac.fukushima_u.gp.poi.word;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class test4{
	public static void main(String[] args) {
		
		
		try {
			String s = new String("C:\\Users\\GP\\Desktop\\seiya\\Dropbox\\pro\\java\\jp\\ac\\fukushima_u\\gp\\poi\\word\\");
			FileInputStream fis = new FileInputStream(s + "testx.docx");
			FileOutputStream out = new FileOutputStream(s + "testx2.docx");
			
			XWPFDocument docx = new XWPFDocument(fis);
			
			java.util.List<XWPFParagraph> list = docx.getParagraphs();
			
			for (int y = 0; y < list.size(); y++) {
				XWPFParagraph para = list.get(y);
				
				//int t = y + list.size();
				
				//docx.setParagraph(para, t);
				
				//java.lang.String ss = new java.lang.String("test");
				//para.insertAfter(ss);
				//para.replaceText(ss,"[2] Microsoft Windows XP");
				
				String line = para.getText();
				//String line=sec.text();
				
				System.out.println("\ntest" + " y=" + y);
				System.out.println(line);
				
			}
			
			docx.write(out);
			
			fis.close();
			
		} catch (java.io.FileNotFoundException e) {
			System.out.println("file is not found!\nplease check for key");
		} catch (IOException e) {
			System.out.println("file is not open!\nplease check for key");
		}
	}
}
