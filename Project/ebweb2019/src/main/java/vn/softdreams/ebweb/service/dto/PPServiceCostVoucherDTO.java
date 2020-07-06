package vn.softdreams.ebweb.service.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PPServiceCostVoucherDTO {
    private UUID id;
    private String noFBook;
    private String noMBook;
    private LocalDate postedDate;
    private LocalDate date;
    private UUID accountingObjectID;
    private String accountingObjectName;
    private String reason;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountOriginal;
    private BigDecimal exchangeRate;
    private BigDecimal totalDiscountAmount;
    private BigDecimal totalDiscountAmountOriginal;
    private BigDecimal totalVATAmount;
    private BigDecimal totalVATAmountOriginal;
    private BigDecimal sumAmount;

    public PPServiceCostVoucherDTO() {
    }

    public PPServiceCostVoucherDTO(UUID id, String noFBook, String noMBook, LocalDate postedDate, LocalDate date, UUID accountingObjectID, String accountingObjectName, String reason, BigDecimal totalAmount, BigDecimal totalAmountOriginal, BigDecimal exchangeRate, BigDecimal totalDiscountAmount, BigDecimal totalDiscountAmountOriginal, BigDecimal totalVATAmount, BigDecimal totalVATAmountOriginal, BigDecimal sumAmount) {
        this.id = id;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.postedDate = postedDate;
        this.date = date;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.exchangeRate = exchangeRate;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
        this.totalVATAmount = totalVATAmount;
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        this.sumAmount = sumAmount;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
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
}
