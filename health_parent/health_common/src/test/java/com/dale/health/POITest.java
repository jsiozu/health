package com.dale.health;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class POITest {

    @Test
    public void test01() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\gaozc\\Desktop\\test.xlsx");
        XSSFWorkbook excel = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = excel.getSheetAt(0);
        for (Row row : sheet) {
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }
        }
        excel.close();
        fileInputStream.close();
    }

    @Test
    public void test02() throws IOException {
        XSSFWorkbook excel = new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet("传智播客");
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("地址");
        row.createCell(2).setCellValue("年龄");
        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("小明");
        row1.createCell(1).setCellValue("北京");
        row1.createCell(2).setCellValue("20");
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\gaozc\\Desktop\\test02.xlsx");
        excel.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        excel.close();
    }
}
