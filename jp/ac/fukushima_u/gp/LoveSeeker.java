package jp.ac.fukushima_u.gp;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

public class LoveSeeker{
	public static void main(String[] args) {
		FileReader in = null;
		LineNumberReader lnr = null;
		FileWriter out = null;
		BufferedWriter bw = null;
		try {
			
			//C:\Users\GP\Desktop\seiya\Dropbox\pro\java\
			
			//in = new FileReader("C:\\java\\test.txt");
			in = new FileReader("C:\\Users\\GP\\Desktop\\seiya\\Dropbox\\pro\\java\\test.txt");
			lnr = new LineNumberReader(in);
			
			//out = new FileWriter("C:\\java\\out.txt");
			out = new FileWriter("C:\\Users\\GP\\Desktop\\seiya\\Dropbox\\pro\\java\\test.txt");
			bw = new BufferedWriter(out);
			
			String key = "test";
			String line;
			while ((line = lnr.readLine()) != null) {
				if (line.contains(key)) {
					bw.write(lnr.getLineNumber() + "ÅF");
					bw.newLine();
					bw.write(line);
					bw.newLine();
				}
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				lnr.close();
			} catch (Exception e) {
			}
			try {
				bw.close();
			} catch (Exception e) {
			}
		}
	}
}
