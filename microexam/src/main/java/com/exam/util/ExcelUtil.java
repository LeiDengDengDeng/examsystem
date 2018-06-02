package com.exam.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chentiange on 2017/11/24.
 */
public class ExcelUtil {
    /**
     * 创建表头
     * @param workbook
     * @param sheet
     * @param titles 表头，各个属性用逗号分隔
     */
    public static void createTitle(HSSFWorkbook workbook, HSSFSheet sheet, String titles){
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell;
        String[] title_split = titles.split(",");
        int varnumber = title_split.length;
        for (int i=0; i<varnumber; ++i){
            cell = row.createCell(i);
            cell.setCellValue(title_split[i]);
        }

    }

    public static void writeToExcel(HSSFWorkbook workbook,String filename){
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(filename);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println("获取不到位置");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
