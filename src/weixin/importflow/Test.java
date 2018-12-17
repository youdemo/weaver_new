package weixin.importflow;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wdnnccey on 17/2/27.
 * <p>
 * <p>
 * 2017年02月27日21:27:26，
 *
 */
public class TestXlsx2 {

    public static void main(String[] args) {

        File excelFile = new File("/Users/wdnnccey/Desktop/test/test.xlsx"); //替换你文档地址
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(new FileInputStream(excelFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int numberOfSheets = wb.getNumberOfSheets();
        String str = "";
        for (int x = 0; x < numberOfSheets; x++) {
            XSSFSheet sheet = wb.getSheetAt(x);
            int columnNum = 0;
            if (sheet.getRow(0) != null) {
                columnNum = sheet.getRow(0).getLastCellNum()
                        - sheet.getRow(0).getFirstCellNum();
            }
            if (columnNum > 0) {
                for (Row row : sheet) {
                    String[] singleRow = new String[columnNum];
                    int n = 0;
                    for (int i = 0; i < columnNum; i++) {
                        Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_BLANK:
                                singleRow[n] = "";
                                if (cell == null || cell.equals("") || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                                    System.out.print("<Null>|");
                                } else {
                                    System.out.print(singleRow[n] + "|");
                                }
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                singleRow[n] = Boolean.toString(cell
                                        .getBooleanCellValue());
                                System.out.print(singleRow[n] + "|");
                                break;
                            // 数值
                            case Cell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    SimpleDateFormat sdf = null;
                                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                                            .getBuiltinFormat("h:mm")) {
                                        sdf = new SimpleDateFormat("HH:mm");
                                    } else {// 日期
                                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    }
                                    Date date = cell.getDateCellValue();
                                    System.out.print(sdf.format(date) + "|");
                                } else {
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    String temp = cell.getStringCellValue();
                                    // 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
                                    if (temp.indexOf(".") > -1) {
                                        singleRow[n] = String.valueOf(new Double(temp))
                                                .trim();
                                        System.out.print(singleRow[n] + "|");
                                    } else {
                                        singleRow[n] = temp.trim();
                                        System.out.print(singleRow[n] + "|");
                                    }
                                }
                                break;
                            case Cell.CELL_TYPE_STRING:
                                singleRow[n] = cell.getStringCellValue().trim();
                                System.out.print(singleRow[n] + "|");
                                break;
                            case Cell.CELL_TYPE_ERROR:
                                singleRow[n] = "";
                                System.out.print(singleRow[n] + "|");
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                String temp = cell.getStringCellValue();
                                // 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
                                if (temp.indexOf(".") > -1) {
                                    temp = String.valueOf(new Double(temp))
                                            .trim();
                                    Double cny = Double.parseDouble(temp);//6.2041
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    String CNY = df.format(cny);
                                    System.out.print(CNY + "|");
                                } else {
                                    singleRow[n] = temp.trim();
                                    System.out.print(singleRow[n] + "|");

                                }
                            default:
                                singleRow[n] = "";
                                break;
                        }
                        n++;
                    }
                    System.out.println();
                }
                System.out.println("===========================================================Sheet分割线===========================================================");
            }
        }
    }
}