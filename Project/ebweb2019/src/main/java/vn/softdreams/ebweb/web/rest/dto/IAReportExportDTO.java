package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.IAReport;

/**
 * DTO kết xuất của khởi tạo mẫu hóa đơn
 */
public class IAReportExportDTO {
    private String reportName;
    private String invoiceForm;
    private String invoiceType;
    private String invoiceTemplate;
    private String invoiceSeries;

    public IAReportExportDTO(String reportName, String invoiceForm, String invoiceType, String invoiceTemplate, String invoiceSeries) {
        this.reportName = reportName;
        this.invoiceForm = invoiceForm;
        this.invoiceType = invoiceType;
        this.invoiceTemplate = invoiceTemplate;
        this.invoiceSeries = invoiceSeries;
    }

    public IAReportExportDTO(IAReport iaReport) {
        if (iaReport != null) {
            this.reportName = iaReport.getReportName();
            switch (iaReport.getInvoiceForm()) {
                case 0:
                    this.invoiceForm = "Hóa đơn điện tử";
                    break;
                case 1:
                    this.invoiceForm = "Hóa đơn đặt in";
                    break;
                case 2:
                    this.invoiceForm = "Hóa đơn tự in";
                    break;
            }
            if (iaReport.getInvoiceType() != null) {
                this.invoiceType = iaReport.getInvoiceType().getInvoiceTypeName();
            }
            this.invoiceTemplate = iaReport.getInvoiceTemplate();
            this.invoiceSeries = iaReport.getInvoiceSeries();
        }
    }


    public IAReportExportDTO() {
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(String invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }
}
