package jp.ac.fukushima_u.gp;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Sampled{
  public static void main(String[] args){
    HSSFWorkbook workbook = new HSSFWorkbook();

    workbook.createSheet("test");

    FileOutputStream out = null;
    try{
      out = new FileOutputStream("sample.xls");
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