package utils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class ExcelReader {
    private Workbook workbook;

    public ExcelReader(String filePath) {
        try {
            // check file is exist
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Excel dosyası bulunamadı: " + filePath);
                return;
            } else {
                System.out.println("Excel dosyası bulundu: " + filePath);
            }

            // check the file is opened successfully
            FileInputStream fis = new FileInputStream(file);
            this.workbook = WorkbookFactory.create(fis);
            System.out.println("Excel dosyası başarıyla açıldı.");
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Excel dosyası açılırken bir hata oluştu.");
        }
    }


    public String getData(int sheetIndex, int row, int cell) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        if (sheet == null) {
            System.out.println("Sheet " + sheetIndex + " bulunamadı veya boş.");
            return "";
        }
        Row excelRow = sheet.getRow(row);
        if (excelRow == null) {
            System.out.println("Row " + row + " is empty or does not exist in sheet " + sheetIndex);
            return "";
        }
        Cell excelCell = excelRow.getCell(cell);
        if (excelCell == null) {
            System.out.println("Cell " + cell + " is empty or does not exist in row " + row);
            return "";
        }
        return excelCell.getStringCellValue();
    }



    public void close() {
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
