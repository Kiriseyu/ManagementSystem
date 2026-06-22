package com.hr.util;

import com.hr.entity.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel导入导出工具类
 * 使用Apache POI处理Excel文件，实现员工信息的批量导入导出
 */
public class ExcelUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 导出员工列表到Excel
     * @param employees 员工列表
     * @param outputStream 输出流
     */
    public static void exportEmployees(List<Employee> employees, OutputStream outputStream) throws IOException {
        // 创建工作簿(使用XSSF支持.xlsx格式)
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("员工信息");

            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"工号", "姓名", "性别", "出生日期", "电话", "邮箱", "地址", "部门ID", "入职日期", "职位"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                // 设置列宽
                sheet.setColumnWidth(i, 4000);
            }

            // 填充数据
            int rowNum = 1;
            for (Employee emp : employees) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(emp.getEmpId() != null ? emp.getEmpId() : 0);
                row.createCell(1).setCellValue(emp.getEmpName() != null ? emp.getEmpName() : "");
                row.createCell(2).setCellValue(emp.getEmpGender() != null ? emp.getEmpGender() : "");
                row.createCell(3).setCellValue(emp.getEmpBirthdate() != null ? emp.getEmpBirthdate().toString() : "");
                row.createCell(4).setCellValue(emp.getEmpPhone() != null ? emp.getEmpPhone() : "");
                row.createCell(5).setCellValue(emp.getEmpEmail() != null ? emp.getEmpEmail() : "");
                row.createCell(6).setCellValue(emp.getEmpAddress() != null ? emp.getEmpAddress() : "");
                row.createCell(7).setCellValue(emp.getDeptId() != null ? emp.getDeptId() : 0);
                row.createCell(8).setCellValue(emp.getHireDate() != null ? emp.getHireDate().toString() : "");
                row.createCell(9).setCellValue(emp.getJobTitle() != null ? emp.getJobTitle() : "");
            }

            // 写入输出流
            workbook.write(outputStream);
        }
    }

    /**
     * 从Excel导入员工列表
     * @param inputStream 输入流
     * @return 员工列表
     */
    public static List<Employee> importEmployees(InputStream inputStream) throws IOException {
        List<Employee> employees = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            // 跳过表头，从第二行开始读取
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Employee emp = new Employee();

                // 读取各列数据
                emp.setEmpId(getIntCellValue(row.getCell(0)));
                emp.setEmpName(getStringCellValue(row.getCell(1)));
                emp.setEmpGender(getStringCellValue(row.getCell(2)));
                emp.setEmpBirthdate(getDateCellValue(row.getCell(3)));
                emp.setEmpPhone(getStringCellValue(row.getCell(4)));
                emp.setEmpEmail(getStringCellValue(row.getCell(5)));
                emp.setEmpAddress(getStringCellValue(row.getCell(6)));
                emp.setDeptId(getIntCellValue(row.getCell(7)));
                emp.setHireDate(getDateCellValue(row.getCell(8)));
                emp.setJobTitle(getStringCellValue(row.getCell(9)));

                // 只有姓名必填
                if (emp.getEmpName() != null && !emp.getEmpName().trim().isEmpty()) {
                    employees.add(emp);
                }
            }
        }

        return employees;
    }

    /**
     * 获取字符串类型的单元格值
     */
    private static String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * 获取整数类型的单元格值
     */
    private static Integer getIntCellValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    /**
     * 获取日期类型的单元格值
     */
    private static Date getDateCellValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case NUMERIC:
                // 日期类型直接转为java.sql.Date
                return new Date(cell.getDateCellValue().getTime());
            case STRING:
                String dateStr = cell.getStringCellValue().trim();
                if (!dateStr.isEmpty()) {
                    try {
                        return new Date(DATE_FORMAT.parse(dateStr).getTime());
                    } catch (ParseException e) {
                        return null;
                    }
                }
            default:
                return null;
        }
    }
}
