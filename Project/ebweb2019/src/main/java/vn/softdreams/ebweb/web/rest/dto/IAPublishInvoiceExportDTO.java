package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.IaPublishInvoice;

import java.time.LocalDate;

public class IAPublishInvoiceExportDTO {
    private LocalDate date;
    private String no;
    private String receiptedTaxOffical;
    private String representationInLaw;
    private String status;

    public IAPublishInvoiceExportDTO(IaPublishInvoice iaPublishInvoice) {
        this.date = iaPublishInvoice.getDate();
        this.no = iaPublishInvoice.getNo();
        this.receiptedTaxOffical = iaPublishInvoice.getReceiptedTaxOffical();
        this.representationInLaw = iaPublishInvoice.getRepresentationInLaw();
        this.status = iaPublishInvoice.getStatus() == 0 ? "Chưa có hiệu lực" : "Đã có hiệu lực";
    }

    public IAPublishInvoiceExportDTO(LocalDate date, String no, String receiptedTaxOffical, String representationInLaw, String status) {
        this.date = date;
        this.no = no;
        this.receiptedTaxOffical = receiptedTaxOffical;
        this.representationInLaw = representationInLaw;
        this.status = status;
    }

    public IAPublishInvoiceExportDTO() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getReceiptedTaxOffical() {
        return receiptedTaxOffical;
    }

    public void setReceiptedTaxOffical(String receiptedTaxOffical) {
        this.receiptedTaxOffical = receiptedTaxOffical;
    }

    public String getRepresentationInLaw() {
        return representationInLaw;
    }

    public void setRepresentationInLaw(String representationInLaw) {
        this.representationInLaw = representationInLaw;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
