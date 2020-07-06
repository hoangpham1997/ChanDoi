package vn.softdreams.ebweb.service.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportUtils {
	private final static Logger LOG = LoggerFactory.getLogger(ReportUtils.class);

	public static byte[] generateReportPDF(List<?> dataSource, Map parameters, JasperReport jasperReport)
			throws JRException {
		byte[] bytes = null;
		JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dataSource);
        bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, beanDataSource);

		return bytes;
	}

	public static byte[] generateReportPDF(Map parameters, JasperReport jasperReport)
			throws JRException {
		return JasperRunManager.runReportToPdf(jasperReport, parameters);
	}

	public static byte[] generateReportExcel(Map parameters, JasperReport jasperReport, List<?> dataSource)
			throws JRException {
		byte[] excel;
		JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dataSource);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanDataSource);
		JRXlsExporter exporter = new JRXlsExporter();
		ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		configuration.setOnePagePerSheet(false);
		configuration.setDetectCellType(true);
		configuration.setCollapseRowSpan(false);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
		excel = xlsReport.toByteArray();

		return excel;
	}

	public static JasperReport getCompiledFile(String jasperPath, String jrxmlPath) throws JRException {

		File reportFile = null;
        try {
            File currentDirectory = new File(new File("").getAbsolutePath());
            System.out.println(currentDirectory.getAbsolutePath());
            reportFile = ResourceUtils.getFile(currentDirectory.getAbsolutePath() + "/report/" + jasperPath);
        } catch (FileNotFoundException e) {
            for(StackTraceElement ex: e.getStackTrace()) {
                LOG.error(ex.toString());
            }
            e.printStackTrace();
        }
        // If compiled file is not found, then compile XML template
//		if (reportFile == null || !reportFile.exists()) {
//			JasperCompileManager.compileReportToFile(RESOURSE + jrxmlPath, RESOURSE + jasperPath);
//		}
		BufferedInputStream bufferedInputStream = null;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(reportFile.getPath()));
		} catch (FileNotFoundException e) {
			LOG.error("FileNotFoundException " + e.getMessage());
		}

        return (JasperReport) JRLoader.loadObject(bufferedInputStream);
	}

	public static DecimalFormat getDecimalFormat() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter;
    }

    public static byte[] generateEmptyReportPDF(String templatePath) throws JRException {
        Map<String, Object> parameter = new HashMap<>();
        JasperReport jasperReport = null;
        if (templatePath != null) {
            jasperReport = ReportUtils.getCompiledFile(templatePath + ".jasper", templatePath + ".jrxml");
            jasperReport.setProperty("net.sf.jasperreports.default.pdf.encoding", "Cp1250");
        }
        return JasperRunManager.runReportToPdf(jasperReport, parameter, new JREmptyDataSource());
    }
}
