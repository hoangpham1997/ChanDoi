package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SAQuoteDTO {
    private UUID id;
    private LocalDate date;
    private String no;
    private LocalDate finalDate;
    private String accountingObjectName;
    private String reason;
    private BigDecimal totalAmount;
    private BigDecimal totalDiscountAmount;
    private BigDecimal totalVATAmount;
    private Number totalMoney;
    private String currencyID;

    public SAQuoteDTO() {
    }

    public SAQuoteDTO(UUID id, LocalDate date, String no, LocalDate finalDate, String accountingObjectName,
                      String reason, BigDecimal totalAmount, BigDecimal totalDiscountAmount, BigDecimal totalVATAmount,
                      String currencyID
    ) {
        this.id = id;
        this.date = date;
        this.no = no;
        this.finalDate = finalDate;
        this.accountingObjectName = accountingObjectName;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalVATAmount = totalVATAmount;
        this.currencyID = currencyID;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public Number getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Number totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }
}
