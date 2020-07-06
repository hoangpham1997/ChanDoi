package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PPDiscountReturnSearchDTO {
    private UUID id;
    private LocalDate date;
    private LocalDate postedDate;
    private String NoBook;
    private String accountingObjectName;
    private String accountingObjectAddress;
    private String reason;
    private BigDecimal totalAmount;
    private BigDecimal totalVATAmount;
    private Boolean recorded;

    public PPDiscountReturnSearchDTO() {
    }

    public PPDiscountReturnSearchDTO(UUID id, LocalDate date, LocalDate postedDate, String noBook, String accountingObjectName, String accountingObjectAddress, String reason, BigDecimal totalAmount, BigDecimal totalVATAmount, Boolean recorded) {
        this.id = id;
        this.date = date;
        this.postedDate = postedDate;
        NoBook = noBook;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.totalVATAmount = totalVATAmount;
        this.recorded = recorded;
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

    public String getNoBook() {
        return NoBook;
    }

    public void setNoBook(String noBook) {
        NoBook = noBook;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
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

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }
}
