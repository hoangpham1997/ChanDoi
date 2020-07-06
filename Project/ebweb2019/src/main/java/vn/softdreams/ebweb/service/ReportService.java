package vn.softdreams.ebweb.service;

import net.sf.jasperreports.engine.JRException;
import vn.softdreams.ebweb.web.rest.dto.RequestReport;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public interface ReportService {

    /**
     * @return trả ra mảng byte của báo cáo.
     * @Author hieugie
     * <p>
     * Hàm tạo báo cáo demo.
     */
    byte[] getReport(UUID id, int typeID, int typeReport) throws JRException, InvocationTargetException, IllegalAccessException;

    /**
     * @return trả ra mảng byte của báo cáo.
     * @Author hieugie
     * <p>
     * Hàm tạo báo cáo excel demo.
     */
    byte[] getReportExcel();

}
