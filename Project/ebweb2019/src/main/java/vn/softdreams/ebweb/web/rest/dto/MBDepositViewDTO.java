package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MBDeposit;
import vn.softdreams.ebweb.domain.RefVoucher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class MBDepositViewDTO {
    private UUID id;
    private Integer typeID;
    private LocalDate date;
    private LocalDate postedDate;
    private Integer typeLedger;
    private String noFBook;
    private String noMBook;
    private UUID accountingObjectID;
    private String accountingObjectName;
    private String accountingObjectAddress;
    private UUID bankAccountDetailID;
    private String bankName;
    private String reason;
    private String currencyID;
    private BigDecimal exchangeRate;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountOriginal;
    private BigDecimal totalVATAmount;
    private BigDecimal totalVATAmountOriginal;
    private Boolean recorded;
    private Number total;

    public MBDepositViewDTO() {
    }

    public MBDepositViewDTO(UUID id, Integer typeID, LocalDate date, LocalDate postedDate, Integer typeLedger,
                            String noFBook, String noMBook, UUID accountingObjectID, String accountingObjectName, String accountingObjectAddress,
                            UUID bankAccountDetailID, String bankName, String reason, String currencyID, BigDecimal exchangeRate,
                            BigDecimal totalAmount, BigDecimal totalAmountOriginal, BigDecimal totalVATAmount, BigDecimal totalVATAmountOriginal,
                            Boolean recorded) {
        this.id = id;
        this.typeID = typeID;
        this.date = date;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.bankAccountDetailID = bankAccountDetailID;
        this.bankName = bankName;
        this.reason = reason;
        this.currencyID = currencyID;
        this.exchangeRate = exchangeRate;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.totalVATAmount = totalVATAmount;
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        this.recorded = recorded;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
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

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
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

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
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

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
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

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }
}
