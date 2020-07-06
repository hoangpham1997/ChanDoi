package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.IARegisterInvoice;

import java.time.LocalDate;

/**
 * DTO để kết xuất màn đăng ký sử dụng hóa đơn
 */
public class IARegisterInvoiceExportDTO {
    private LocalDate date;
    private String no;
    private String description;
    private String signer;
    private String status;

    public IARegisterInvoiceExportDTO() {
    }

    public IARegisterInvoiceExportDTO(LocalDate date, String no, String description, String signer, String status) {
        this.date = date;
        this.no = no;
        this.description = description;
        this.signer = signer;
        this.status = status;
    }

    public IARegisterInvoiceExportDTO(IARegisterInvoice iaRegisterInvoice) {
        this.date = iaRegisterInvoice.getDate();
        this.no = iaRegisterInvoice.getNo();
        this.description = iaRegisterInvoice.getDescription();
        this.signer = iaRegisterInvoice.getSigner();
        this.status = iaRegisterInvoice.getStatus() == 0 ? "Chưa có hiệu lực" : "Đã có hiệu lực";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
