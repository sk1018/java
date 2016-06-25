package jp.ac.fukushima_u.gp.poi.word;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;

public class test1i{
	public static void main(String[] args) {
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("test.doc");
			HWPFDocument doc = new HWPFDocument(fis);
			FileOutputStream fo33 = new FileOutputStream("test1i.txt");
			PrintStream ps33 = new PrintStream(fo33);
			PrintStream oldps = System.out;
			System.setOut(ps33);
			
			Range r = doc.getRange();
			
			for (int x = 0; x < r.numSections(); x++) {
				Section sec = r.getSection(x);
				
				for (int y = 0; y < sec.numParagraphs(); y++) {
					Paragraph para = sec.getParagraph(y);
					for (int z = 0; z < para.numCharacterRuns(); z++) {
						CharacterRun run = para.getCharacterRun(z);
						String line = run.text();
						System.out.println("" + line);
					}
				}
			}
			
			System.setOut(oldps);
			System.out.println("");
			
			ps33.close();
			fo33.close();
			
			fis.close();
		} catch (IOException e) {
			System.out.println("");
		}
	}
}
