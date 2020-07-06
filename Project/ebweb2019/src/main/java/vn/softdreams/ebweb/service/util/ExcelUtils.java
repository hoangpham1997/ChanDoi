package vn.softdreams.ebweb.service.util;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.stereotype.Service;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.UserDTO;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ExcelUtils {

    public static void createHeader(Row row, List<String> headers, HSSFWorkbook workbook, HSSFSheet sheet) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) (12 * 20));
        font.setFontName("Times New Roman");
        font.setColor(IndexedColors.WHITE.index);
        cellStyle.setFillForegroundColor(IndexedColors.ORANGE.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(font);

        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(headers.get(i));
            CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
            sheet.autoSizeColumn(i + 1);
        }
    }

    public static void genBodyDynamicReportExcel(HSSFWorkbook wb, Sheet ws, HashMap<String, HSSFCellStyle> styleLists,
                                                 List<?> resultList, List<String> headerList) {
        genBodyDynamicReportExcel(wb, ws, styleLists, resultList, headerList, null);
    }

    public static void genBodyDynamicReportExcel(HSSFWorkbook wb, Sheet ws, HashMap<String, HSSFCellStyle> styleLists,
                                                 List<?> resultList, List<String> headerList, UserDTO userDTO) {
        int lastRow = ws.getLastRowNum() == 0 ? 0 : ws.getLastRowNum() + 1;
        int col = 0;
        int sheet = 1;
        Object content = null;
        int rowHeader = ws.getRow(0).getLastCellNum();
        for (int colNum = 0; colNum < rowHeader; colNum++) {
            ws.setColumnWidth(colNum, 5000);
        }
        for (Object item : resultList) {
            col = 0;
            // Next Sheet when reach limit
            if (wb.getSheetAt(wb.getNumberOfSheets() - 1).getLastRowNum() > 60000) {
                sheet++;
                ws = wb.createSheet("Sheet" + sheet);
                lastRow = ws.getLastRowNum();
                for (int colNum = 0; colNum < rowHeader; colNum++) {
                    ws.setColumnWidth(colNum, 5000);
                }
            }
            // Fill data
            Row row = ws.createRow(lastRow + 1);

            for (String field : headerList) {
                HorizontalAlignment align = HorizontalAlignment.LEFT;
                // Trường hợp field này là tổng của các field khác
                // edit by anmt : thêm trường hợp tính toán +, -
                if (field.contains("+") || field.contains("-")) {
                    String[] fields = field.split("\\+|-");
                    BigDecimal value = BigDecimal.ZERO;
                    for (String subField : fields) {
                        Object data = ReflectionUtils.getFieldValue(item, subField);
                        if (field.contains("-" + subField)) {
                            data = "-" + data.toString();
                        }
                        value = value.add(new BigDecimal(data.toString()));
                    }
                    content = value;
                    align = HorizontalAlignment.RIGHT;
                } else if (field.equalsIgnoreCase("noMBook")) {
                    align = HorizontalAlignment.CENTER;
                    content = ReflectionUtils.getFieldValue(item, field);
                    if (Strings.isNullOrEmpty((String) content)) {
                        content = ReflectionUtils.getFieldValue(item, "noFBook");
                    }

                } else {
                    content = ReflectionUtils.getFieldValue(item, field);
                    if (content instanceof LocalDate) {
                        align = HorizontalAlignment.CENTER;
                        content = ((LocalDate) content).format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY));
                    } else if (content instanceof LocalDateTime) {
                        align = HorizontalAlignment.CENTER;
                        content = ((LocalDateTime) content).format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY_HHMM));
                    }
                }
                // Date thì center
                // amount thì align phải
                // còn lại trái
                align = content instanceof BigDecimal ? HorizontalAlignment.RIGHT : align;
                CellStyle cellStyle = wb.createCellStyle();
                if (userDTO != null) {
                    /*Hautv Edit fromat tiền*/
                    if (field.toLowerCase().equals("totalincompleteopenning") || field.toLowerCase().equals("totalamountinperiod") || field.toLowerCase().equals("totalincompleteclosing") || field.toLowerCase().equals("totalcost")) {
                        if (content != null && !StringUtils.isEmpty(content.toString())) {
                            BigDecimal amount = new BigDecimal(content.toString());
                            content = Utils.formatTien(amount, Constants.SystemOption.DDSo_TienVND, userDTO);
                        }
                    }  else if (field.toLowerCase().contains("amount") && !field.toLowerCase().contains("original")) {
                        if (content != null && !StringUtils.isEmpty(content.toString())) {
                            BigDecimal amount = new BigDecimal(content.toString());
//                            content = Utils.formatTien(amount, Constants.SystemOption.DDSo_TienVND, userDTO);

                            DataFormat format = wb.createDataFormat();
                            cellStyle.setDataFormat(format.getFormat("###,###,###,###.###")); // custom number format

                        }
                    } else if (field.toLowerCase().contains("quantity")) {
                        if (content != null && !StringUtils.isEmpty(content.toString())) {
                            BigDecimal amount = new BigDecimal(content.toString());
                            content = Utils.formatTien(amount, Constants.SystemOption.DDSo_SoLuong, userDTO);
                        }
                    } else if (field.toLowerCase().contains("amount") && field.toLowerCase().contains("original")) {
                        if (content != null && !StringUtils.isEmpty(content.toString())) {
                            BigDecimal amount = new BigDecimal(content.toString());
                            content = Utils.formatTien(amount, Constants.SystemOption.DDSo_NgoaiTe, userDTO);
                        }
                    }
                    /*Hautv Edit fromat tiền*/
                }
                if (Objects.nonNull(content)) {
                    ExcelUtils.createCell(row, col, content, cellStyle, align);
                } else {
                    ExcelUtils.createCell(row, col, "", cellStyle, HorizontalAlignment.LEFT);
                }
                col++;
            }
            lastRow++;
        }
    }

    private static Cell createCell(Row row, int indexCell, Object data, CellStyle style, HorizontalAlignment align) {
        Cell cell = row.createCell(indexCell);
        try {
            if (data instanceof BigDecimal) {
                if (((BigDecimal) data).doubleValue() != 0) {
                    cell.setCellValue(((BigDecimal) data).doubleValue());
                }
            } else if (data instanceof String) {
                cell.setCellValue((String) data);
            } else if (data instanceof Boolean) {
                cell.setCellValue((Boolean) data);
            } else if (data instanceof Double) {
                if ((Double) data != 0) {
                    cell.setCellValue((Double) data);
                }
            } else if (data instanceof Integer) {
                if ((Integer) data != 0) {
                    cell.setCellValue((Integer) data);
                }
            } else if (data instanceof Long) {
                if ((Long) data != 0) {
                    cell.setCellValue((Long) data);
                }
            }
            CellUtil.setAlignment(cell, align);
            if (style != null) {
                cell.setCellStyle(style);
            }
        } catch (Exception e) {
            throw e;
        }
        return cell;
    }

    public static byte[] writeToFile(List units, String sheetName, List<String> headers, List<String> fieldNames) {
        return writeToFile(units, sheetName, headers, fieldNames, null);
    }

    /**
     * @param units      danh sách muốn kết xuất
     * @param sheetName  tên sheet
     * @param headers    cột tiêu đề đầu tiên
     * @param fieldNames tên trường
     * @return
     * @Author hieugie
     */
    public static byte[] writeToFile(List units, String sheetName, List<String> headers, List<String> fieldNames, UserDTO userDTO) {

        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(sheetName);

            // tạo header
            ExcelUtils.createHeader(sheet.createRow(0), headers, workbook, sheet);

            // fill dữ liệu vào file
//            writeExcell(units, sheet);
            if (userDTO != null) {
                ExcelUtils.genBodyDynamicReportExcel(workbook, sheet, null, units, fieldNames, userDTO);
            } else {
                ExcelUtils.genBodyDynamicReportExcel(workbook, sheet, null, units, fieldNames);
            }


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();
//            pdf(workbook, );
            return bos.toByteArray();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case FORMULA:
                return cell.getStringCellValue();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    public static BigDecimal getCellValueBigDecimal(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case BOOLEAN:
                return null;
            case STRING:
                if (Strings.isNullOrEmpty(cell.getStringCellValue())){
                    return null;
                } else {
                    return new BigDecimal(cell.getRichStringCellValue().toString());
                }
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    return null;
                } else {
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                }
            case FORMULA:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case BLANK:
                return null;
            default:
                return null;
        }
    }
    public static LocalDate getCellValueLocalDate(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType().name().equals("NUMERIC")) {
            return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            String date = cell.getStringCellValue();
            if (date.matches("[0-9]{1}/[0-9]{2}/[0-9]{4}")) {
                return DateUtil.getLocalDateFromString(date, DateUtil.C_D_MM_YYYY);
            } else if (date.matches("[0-9]{1}-[0-9]{2}-[0-9]{4}")) {
                return DateUtil.getLocalDateFromString(date, DateUtil.C_DaMMaYYYY);
            } else if (date.matches("[0-9]{2}/[0-9]{1}/[0-9]{4}")) {
                return DateUtil.getLocalDateFromString(date, DateUtil.C_DD_M_YYYY);
            } else if (date.matches("[0-9]{2}-[0-9]{1}-[0-9]{4}")) {
                return DateUtil.getLocalDateFromString(date, DateUtil.C_DDaMaYYYY);
            } else if (date.matches("[0-9]{1}/[0-9]{1}/[0-9]{4}")) {
                return DateUtil.getLocalDateFromString(date, DateUtil.C_D_M_YYYY);
            } else if (date.matches("[0-9]{1}-[0-9]{1}-[0-9]{4}")) {
                return DateUtil.getLocalDateFromString(date, DateUtil.C_DaMaYYYY);
            } else if (date.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
                return DateUtil.getLocalDateFromString(date, DateUtil.C_DD_MM_YYYY);
            } else if (date.matches("[0-9]{2}-[0-9]{2}-[0-9]{4}")) {
                return DateUtil.getLocalDateFromString(date, DateUtil.C_DDaMMaYYYY);
            }
        }
        return null;
    }
    public static String getCellValueStringTypeString(Cell cell) {
        if (cell == null) {
            return "";
        }
        String value = "";
        if (cell.getCellType().equals(CellType.STRING)) {
            value = cell.getStringCellValue();
        } else {
            value = ((XSSFCell) cell).getRawValue();
        }
        if (!Strings.isNullOrEmpty(value)) {
            value = value.trim();
        }
        return value;
    }
    public static String getCellValueString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case STRING:
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime().format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY));
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case NUMERIC:
                        return String.valueOf(cell.getNumericCellValue());
                    case STRING:
                        return String.valueOf(cell.getRichStringCellValue());
                }
                return String.valueOf(cell.getCellFormula());
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    public static void setComment(Cell cell, String message, Row row, int index) {
        if (cell == null) {
            cell = row.createCell(index);
        }
        try {
            cell.removeCellComment();
            Drawing drawing = cell.getSheet().createDrawingPatriarch();
            CreationHelper factory = cell.getSheet().getWorkbook().getCreationHelper();

            // When the comment box is visible, have it show in a 1x3 space
            ClientAnchor anchor = factory.createClientAnchor();
            // Create the comment and set the text+author
            anchor.setCol1(cell.getColumnIndex());
            anchor.setCol2(cell.getColumnIndex() + 3);
            anchor.setRow1(cell.getRowIndex());
            anchor.setRow2(cell.getRowIndex() + 3);
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
            Comment comment = drawing.createCellComment(anchor);
            RichTextString str = factory.createRichTextString(message);
            comment.setString(str);
            // Assign the comment to the cell
            cell.setCellComment(comment);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeComment(Cell cell) {
        if (cell == null) {
            return;
        }
        cell.removeCellComment();
    }
}
