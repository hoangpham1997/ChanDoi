package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SaBillCreatedDTO {
    private UUID id;

    private UUID CompanyID;

    private Integer typeGroupID;

    private UUID refID2;

    private String invoiceTemplate;

    private String invoiceSeries;

    private String invoiceNo;

    private LocalDate invoiceDate;

    private String AccountingObjectName;

    private BigDecimal totalSabill;

    public SaBillCreatedDTO(UUID id, UUID companyID, Integer typeGroupID, UUID refID2, String invoiceTemplate, String invoiceSeries, String invoiceNo, LocalDate invoiceDate, String accountingObjectName, BigDecimal totalSabill) {
        this.id = id;
        CompanyID = companyID;
        this.typeGroupID = typeGroupID;
        this.refID2 = refID2;
        this.invoiceTemplate = invoiceTemplate;
        this.invoiceSeries = invoiceSeries;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        AccountingObjectName = accountingObjectName;
        this.totalSabill = totalSabill;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }

    public UUID getRefID2() {
        return refID2;
    }

    public void setRefID2(UUID refID2) {
        this.refID2 = refID2;
    }

    public SaBillCreatedDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(UUID companyID) {
        CompanyID = companyID;
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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getAccountingObjectName() {
        return AccountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        AccountingObjectName = accountingObjectName;
    }

    public BigDecimal getTotalSabill() {
        return totalSabill;
    }

    public void setTotalSabill(BigDecimal totalSabill) {
        this.totalSabill = totalSabill;
    }
}
