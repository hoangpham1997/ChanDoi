package vn.softdreams.ebweb.web.rest.dto;

import io.swagger.models.auth.In;
import vn.softdreams.ebweb.domain.Supplier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class GOtherVoucherDTO {
    private UUID ID;
    private UUID companyID;
    private UUID branchID;
    private String noFBook;
    private String noMBook;
    private Integer typeID;
    private String currencyID;
    private BigDecimal exchangeRate;
    private LocalDate postedDate;
    private LocalDate date;
    private Integer typeLedger;
    private String noBook;
    private String reason;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountOriginal;
    private Boolean recorded;
    private UUID templateID;
    private BigDecimal sumAmount;

    public GOtherVoucherDTO() {
    }

    public GOtherVoucherDTO(UUID ID, UUID companyID, UUID branchID, String noFBook, String noMBook, Integer typeID, String currencyID, BigDecimal exchangeRate, LocalDate postedDate, LocalDate date, Integer typeLedger, String noBook, String reason, BigDecimal totalAmount, BigDecimal totalAmountOriginal, Boolean recorded, UUID templateID) {
        this.ID = ID;
        this.companyID = companyID;
        this.branchID = branchID;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.typeID = typeID;
        this.currencyID = currencyID;
        this.exchangeRate = exchangeRate;
        this.postedDate = postedDate;
        this.date = date;
        this.typeLedger = typeLedger;
        this.noBook = noBook;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.recorded = recorded;
        this.templateID = templateID;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoBook() {
        return noBook;
    }

    public void setNoBook(String noBook) {
        this.noBook = noBook;
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

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }
}
