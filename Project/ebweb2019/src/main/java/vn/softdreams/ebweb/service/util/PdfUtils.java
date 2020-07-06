package vn.softdreams.ebweb.service.util;

import com.google.common.base.Strings;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.UserDTO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static vn.softdreams.ebweb.service.util.Constants.MBTellerPaper.*;

public class PdfUtils {

    public static byte[] writeToFile(List<?> resultList, List<String> headerList, List<String> headerName) {
        return writeToFile(resultList, headerList, headerName, null);
    }

    public static byte[] writeToFile(List<?> resultList, List<String> headerList, List<String> headerName, UserDTO userDTO) {
        try {
            Document document = new Document(new Rectangle(headerList.size() * 120, resultList.size() * 18 + 50), 0, 0, 20, 20);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            PdfPTable table = new PdfPTable(headerName.size());

            createHeader(table, headerList);

            if (userDTO != null) {
                genBodyDynamicReportPdf(table, resultList, headerName, userDTO);
            } else {
                genBodyDynamicReportPdf(table, resultList, headerName);
            }
            document.add(table);
            document.close();
            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void createHeader(PdfPTable table, List<String> headers) throws IOException, DocumentException {

        for (String header : headers) {
            PdfPCell c1 = new PdfPCell(new Phrase(header, cellFont(BaseColor.WHITE)));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            BaseColor color = new BaseColor(242, 111, 33);
            c1.setBackgroundColor(color);
            table.addCell(c1);
        }
    }

    private static void genBodyDynamicReportPdf(PdfPTable table, List<?> resultList,
                                                List<String> headerList) {
        genBodyDynamicReportPdf(table, resultList, headerList, null);
    }

    private static void genBodyDynamicReportPdf(PdfPTable table, List<?> resultList,
                                                List<String> headerList, UserDTO userDTO) {
        Object content;
        for (Object item : resultList) {
            // Fill data
            for (String field : headerList) {
                int align = Element.ALIGN_LEFT;
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
                    align = Element.ALIGN_RIGHT;
                } else if (field.equalsIgnoreCase("noMBook")) {
                    align = Element.ALIGN_CENTER;
                    content = ReflectionUtils.getFieldValue(item, field);
                    if (Strings.isNullOrEmpty((String) content)) {
                        content = ReflectionUtils.getFieldValue(item, "noFBook");
                    }
                } else {
                    content = ReflectionUtils.getFieldValue(item, field);
                    if (content instanceof LocalDate) {
                        align = Element.ALIGN_CENTER;
                        content = ((LocalDate) content).format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY));
                    } else if (content instanceof LocalDateTime) {
                        align = Element.ALIGN_CENTER;
                        content = ((LocalDateTime) content).format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY_HHMM));
                    }
                }
                // Date thì center
                // amount thì align phải
                // còn lại trái
                align = content instanceof BigDecimal ? Element.ALIGN_RIGHT : align;
                if (userDTO != null) {
                    /*Hautv Edit fromat tiền*/

                    if (field.toLowerCase().contains("totalincompleteopenning") || field.toLowerCase().contains("totalamountinperiod")
                    || field.toLowerCase().contains("totalincompleteclosing") || field.toLowerCase().contains("totalcost")) {
                        if (content != null && !StringUtils.isEmpty(content.toString())) {
                            BigDecimal amount = new BigDecimal(content.toString());
                            content = Utils.formatTien(amount, Constants.SystemOption.DDSo_TienVND, userDTO);
                        }
                    } else if (field.toLowerCase().contains("amount") && !field.toLowerCase().contains("original")) {
                        if (content != null && !StringUtils.isEmpty(content.toString())) {
                            BigDecimal amount = new BigDecimal(content.toString());
                            content = Utils.formatTien(amount, Constants.SystemOption.DDSo_TienVND, userDTO);
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
                PdfPCell c1 = new PdfPCell(new Phrase(content != null ? content.toString() : "", cellFont(BaseColor.BLACK)));
                c1.setHorizontalAlignment(align);
                table.addCell(c1);
            }
        }
    }

    static Font cellFont(BaseColor color) {
        File currentDirectory = new File(new File("").getAbsolutePath());
        BaseFont courier = null;
        try {
            courier = BaseFont.createFont(currentDirectory.getAbsolutePath() + "/report/timesnewroman.ttf", BaseFont.IDENTITY_H, true);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return new Font(courier, 8, Font.NORMAL, color);
    }
}
