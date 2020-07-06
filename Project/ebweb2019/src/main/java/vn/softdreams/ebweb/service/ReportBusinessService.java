package vn.softdreams.ebweb.service;

import net.sf.jasperreports.engine.JRException;
import vn.softdreams.ebweb.web.rest.dto.RequestReport;
import java.io.IOException;

public interface ReportBusinessService {
    byte[] getReportBusiness(RequestReport requestReport) throws JRException;

    byte[] getInstructionPDF() throws JRException, IOException;

    byte[] getExcelReportBusiness(RequestReport requestReport, String path) throws JRException;

}
