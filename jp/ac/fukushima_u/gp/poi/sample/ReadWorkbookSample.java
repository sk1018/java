package jp.ac.fukushima_u.gp.poi.sample;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ReadWorkbookSample {

    public static void main(String[] args) throws Exception {

        System.out.println("");
        String path = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            path = br.readLine();
            br.close();
        }
        catch (IOException err) {
            System.out.println(err);
            return;
        }

        FileInputStream is = new FileInputStream(path);
        Workbook wb = new HSSFWorkbook(is);
        Sheet sh = wb.getSheetAt(0);

        System.out.println(" : " + wb.getNumberOfSheets());
        System.out.println(" : " + wb.getSheetName(0));

        Row row = null;
        Cell cell = null;
        for (int r=0; r < 50; r++) {
            for (int c=0; c < 26; c++) {
                row = sh.getRow(r);
                cell = row.getCell(c);

                String value = "";
                int cellType = cell.getCellType();

                switch (cellType) {
                case HSSFCell.CELL_TYPE_NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell) == true) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                        value = df.format(date);
                    } else {
                        value = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    value = String.valueOf(cell.getStringCellValue());
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    value = String.valueOf(cell.getCellFormula());
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    continue;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    value = String.valueOf(cell.getErrorCellValue());
                    break;
                }
                System.out.println("Row=" + r + ", Column=" + c + ", Type=" + cellType + ", value=" + value);
            }
        }
    }
}