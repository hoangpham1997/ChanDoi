package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class SAReceiptDebitBillDTO {

    private UUID referenceID;

    private String date;

    private String noFBook;

    private String noMBook;

    private String invoiceNo;

    private String dueDate;

    private BigDecimal totalCreditOriginal;

    private BigDecimal totalCredit;

    private BigDecimal creditAmountOriginal;

    private BigDecimal creditAmount;

    private String account;

    private String currencyID;

    private Integer typeID;

    private String employeeName;

    private String paymentClause;

    private BigDecimal refVoucherExchangeRate;

    private BigDecimal lastExchangeRate;

    private BigDecimal differAmount;

    public SAReceiptDebitBillDTO(UUID referenceID, String date, String noFBook, String noMBook, String invoiceNo, String dueDate, BigDecimal totalCreditOriginal, BigDecimal totalCredit, BigDecimal creditAmountOriginal, BigDecimal creditAmount, String account, String currencyID, Integer typeID, String employeeName, String paymentClause, BigDecimal refVoucherExchangeRate, BigDecimal lastExchangeRate, BigDecimal differAmount) {
        this.referenceID = referenceID;
        this.date = date;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.invoiceNo = invoiceNo;
        this.dueDate = dueDate;
        this.totalCreditOriginal = totalCreditOriginal;
        this.totalCredit = totalCredit;
        this.creditAmountOriginal = creditAmountOriginal;
        this.creditAmount = creditAmount;
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

    public BigDecimal getTotalCreditOriginal() {
        return totalCreditOriginal;
    }

    public void setTotalCreditOriginal(BigDecimal totalCreditOriginal) {
        this.totalCreditOriginal = totalCreditOriginal;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public BigDecimal getCreditAmountOriginal() {
        return creditAmountOriginal;
    }

    public void setCreditAmountOriginal(BigDecimal creditAmountOriginal) {
        this.creditAmountOriginal = creditAmountOriginal;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
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
