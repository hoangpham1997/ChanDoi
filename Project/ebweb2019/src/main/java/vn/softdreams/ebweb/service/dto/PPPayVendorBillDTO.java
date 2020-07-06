package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PPPayVendorBillDTO {

    private UUID referenceID;

    private String date;

    private String noFBook;

    private String noMBook;

    private String invoiceNo;

    private String dueDate;

    private BigDecimal totalDebitOriginal;

    private BigDecimal totalDebit;

    private BigDecimal debitAmountOriginal;

    private BigDecimal debitAmount;

    private String account;

    private String currencyID;

    private Integer typeID;

    private String employeeName;

    private String paymentClause;

    private BigDecimal refVoucherExchangeRate;

    private BigDecimal lastExchangeRate;

    private BigDecimal differAmount;

    public PPPayVendorBillDTO(UUID referenceID, String date, String noFBook, String noMBook, String invoiceNo, String dueDate, BigDecimal totalDebitOriginal, BigDecimal totalDebit, BigDecimal debitAmountOriginal, BigDecimal debitAmount, String account, String currencyID, Integer typeID, String employeeName, String paymentClause, BigDecimal refVoucherExchangeRate, BigDecimal lastExchangeRate, BigDecimal differAmount) {
        this.referenceID = referenceID;
        this.date = date;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.invoiceNo = invoiceNo;
        this.dueDate = dueDate;
        this.totalDebitOriginal = totalDebitOriginal;
        this.totalDebit = totalDebit;
        this.debitAmountOriginal = debitAmountOriginal;
        this.debitAmount = debitAmount;
        this.account = account;
        this.currencyID = currencyID;
        this.typeID = typeID;
        this.employeeName = employeeName;
        this.paymentClause = paymentClause;
        this.refVoucherExchangeRate = refVoucherExchangeRate;
        this.lastExchangeRate = lastExchangeRate;
        this.differAmount = differAmount;
    }

    public UUID getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getTotalDebitOriginal() {
        return totalDebitOriginal;
    }

    public void setTotalDebitOriginal(BigDecimal totalDebitOriginal) {
        this.totalDebitOriginal = totalDebitOriginal;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getDebitAmountOriginal() {
        return debitAmountOriginal;
    }

    public void setDebitAmountOriginal(BigDecimal debitAmountOriginal) {
        this.debitAmountOriginal = debitAmountOriginal;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPaymentClause() {
        return paymentClause;
    }

    public void setPaymentClause(String paymentClause) {
        this.paymentClause = paymentClause;
    }

    public BigDecimal getRefVoucherExchangeRate() {
        return refVoucherExchangeRate;
    }

    public void setRefVoucherExchangeRate(BigDecimal refVoucherExchangeRate) {
        this.refVoucherExchangeRate = refVoucherExchangeRate;
    }

    public BigDecimal getLastExchangeRate() {
        return lastExchangeRate;
    }

    public void setLastExchangeRate(BigDecimal lastExchangeRate) {
        this.lastExchangeRate = lastExchangeRate;
    }

    public BigDecimal getDifferAmount() {
        return differAmount;
    }

    public void setDifferAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
    }
}
