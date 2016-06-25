package jp.ac.fukushima_u.gp.poi.sample;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Sampled{
  public static void main(String[] args){
    HSSFWorkbook workbook = new HSSFWorkbook();

    workbook.createSheet("test");

    FileOutputStream out = null;
    

	String s = new String("C:\\Users\\GP\\Desktop\\seiya\\Dropbox\\pro\\java\\");
    
    try{
      out = new FileOutputStream(s + "sample.xls");
      workbook.write(out);
    }catch(IOException e){
      System.out.println(e.toString());
    }finally{
      try {
        out.close();
      }catch(IOException e){
        System.out.println(e.toString());
      }
    }
  }
}