package com.exam.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by deng on 2017/11/18.
 */
public class ExcelUtil {
    private ExcelUtil() {
    }

    /**
     * 获得指定路径的excel工作簿文件
     *
     * @param fileCompletePath
     * @return
     */
    public static Workbook getWorkbook(String fileCompletePath) {
        Workbook result = null;
        InputStream is = null;
        String suffixName = fileCompletePath.substring(fileCompletePath.lastIndexOf(".") + 1);
        try {
            is = new FileInputStream(fileCompletePath);
            if (suffixName.equals("xls")) {
                result = new HSSFWorkbook(is);
            } else if (suffixName.equals("xlsx")) {
                result = new XSSFWorkbook(is);
            }

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    /**
     * 以字符串形式获得单元格内的值
     *
     * @param cell
     * @return
     */
    public static String getCellVal(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return String.valueOf(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue().trim();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
        }
        return "";
    }
}
