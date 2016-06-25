package jp.ac.fukushima_u.gp.poi.word;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;

public class test2{
	public static void main(String[] args) {
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("test.doc");
			HWPFDocument doc = new HWPFDocument(fis);
			
			Range r = doc.getRange();
			
			for (int x = 0; x < r.numSections(); x++) {
				Section sec = r.getSection(x);
				
				for (int y = 0; y < sec.numParagraphs(); y++) {
					Paragraph para = sec.getParagraph(y);
					String line = para.text();
					
					System.out.println("\ntest");
					System.out.println(line);
					
				}
			}
			fis.close();
			
		} catch (java.io.FileNotFoundException e) {
			
			System.out.println("file not found");
		} catch (IOException e) {
			
			System.out.println("samthing error");
		}
	}
}
