package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.PPOrder;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PPOrderExportDTO {
    private LocalDate date;
    private String no;
    private String accountingObjectName;
    private String reason;
    private LocalDate deliverDate;
    private BigDecimal total;
    private String status;

    public PPOrderExportDTO() {
    }

    public PPOrderExportDTO(PPOrder ppOrder) {
        date = ppOrder.getDate();
        no = ppOrder.getNo();
        accountingObjectName = ppOrder.getAccountingObjectName();
        reason = ppOrder.getReason();
        deliverDate = ppOrder.getDeliverDate();
        total = ppOrder.getTotalAmount().subtract(ppOrder.getTotalDiscountAmount()).add(ppOrder.getTotalVATAmount());
        switch (ppOrder.getStatus()) {
            case 1:
                status = "Đang thực hiện";
            case 2:
                status = "Đã hoàn thành";
            case 3:
                status = "Đã hủy bỏ";
            default:
                status = "Chưa thực hiện";
        }
    }

    public PPOrderExportDTO(LocalDate date, String no, String accountingObjectName, String reason, LocalDate deliverDate, BigDecimal total, String status) {
        this.date = date;
        this.no = no;
        this.accountingObjectName = accountingObjectName;
        this.reason = reason;
        this.deliverDate = deliverDate;
        this.total = total;
        this.status = status;
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

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(LocalDate deliverDate) {
        this.deliverDate = deliverDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
