package vn.softdreams.ebweb.web.rest.dto;

import java.time.LocalDate;
import java.util.UUID;

public class IAPublishInvoiceDetailDTO {
    private Integer invoiceForm;
    private String invoiceTemplate;
    private UUID invoiceTypeID;
    private String invoiceSeries;
    private String invoiceTypeCode;
    private LocalDate startUsing;

    public IAPublishInvoiceDetailDTO(Integer invoiceForm, String invoiceTemplate, UUID invoiceTypeID, String invoiceSeries) {
        this.invoiceForm = invoiceForm;
        this.invoiceTemplate = invoiceTemplate;
        this.invoiceTypeID = invoiceTypeID;
        this.invoiceSeries = invoiceSeries;
    }

    public IAPublishInvoiceDetailDTO(UUID invoiceTypeID, Integer invoiceForm, String invoiceSeries, String invoiceTemplate, LocalDate startUsing) {
        this.invoiceForm = invoiceForm;
        this.invoiceTypeID = invoiceTypeID;
        this.invoiceSeries = invoiceSeries;
        this.invoiceTemplate = invoiceTemplate;
        this.startUsing = startUsing;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public UUID getInvoiceTypeID() {
        return invoiceTypeID;
    }

    public void setInvoiceTypeID(UUID invoiceTypeID) {
        this.invoiceTypeID = invoiceTypeID;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getInvoiceTypeCode() {
        return invoiceTypeCode;
    }

    public void setInvoiceTypeCode(String invoiceTypeCode) {
        this.invoiceTypeCode = invoiceTypeCode;
    }

    public LocalDate getStartUsing() {
        return startUsing;
    }

    public void setStartUsing(LocalDate startUsing) {
        this.startUsing = startUsing;
    }
}
