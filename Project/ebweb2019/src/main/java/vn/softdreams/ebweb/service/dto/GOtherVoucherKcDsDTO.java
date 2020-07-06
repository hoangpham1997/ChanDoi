package vn.softdreams.ebweb.service.dto;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class GOtherVoucherKcDsDTO {
    private UUID id;
    private LocalDate date;
    private LocalDate postedDate;
    private String no;
    private Boolean recorded;
    private String reason;
    private String currencyID;
    private BigDecimal totalAmount;
    private BigDecimal sumTotalAmount;

    public GOtherVoucherKcDsDTO() {
    }

    public GOtherVoucherKcDsDTO(UUID id, LocalDate date, LocalDate postedDate, String no, Boolean recorded, String reason, String currencyID, BigDecimal totalAmount) {
        this.id = id;
        this.date = date;
        this.postedDate = postedDate;
        this.no = no;
        this.recorded = recorded;
        this.reason = reason;
        this.currencyID = currencyID;
        this.totalAmount = totalAmount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getSumTotalAmount() {
        return sumTotalAmount;
    }

    public void setSumTotalAmount(BigDecimal sumTotalAmount) {
        this.sumTotalAmount = sumTotalAmount;
    }
}
