package jp.ac.fukushima_u.gp.poi.word;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;

public class test3{
	public static void main(String[] args) {
		
		try {
			String s = new String("C:\\Users\\GP\\Desktop\\seiya\\Dropbox\\pro\\java\\");
			
			FileInputStream fis = new FileInputStream(s + "test.doc");
			FileOutputStream out = new FileOutputStream(s + "test2.doc");
			
			HWPFDocument doc = new HWPFDocument(fis);
			
			Range r = doc.getRange();
			
			for (int x = 0; x < r.numSections(); x++) {
				Section sec = r.getSection(x);
				
				for (int y = 0; y < sec.numParagraphs(); y++) {
					Paragraph para = sec.getParagraph(y);
					
					java.lang.String ss = new java.lang.String("test");
					para.insertAfter(ss);
					//para.replaceText(ss,"[2] Microsoft Windows XP");
					
					String line = para.text();
					//String line=sec.text();
					
					System.out.println("\ntest x=" + x + " y=" + y);
					System.out.println(line);
					
				}
			}
			
			doc.write(out);
			
			fis.close();
			
		} catch (java.io.FileNotFoundException e) {
			System.out.println("file not found");
		} catch (IOException e) {
			System.out.println("file is not open");
		}
	}
}
