package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoChiTietTaiKhoanDTO {
    private String accountNumber;
    private String accountNameWithAccountNumber;
    private LocalDate postedDate;
    private LocalDate date;
    private String no;
    private UUID referenceID;
    private Integer typeID;
    private String journalMemo;
    private String accountCorresponding;
    private BigDecimal debitAmountOriginal;
    private BigDecimal debitAmount;
    private BigDecimal creditAmountOriginal;
    private BigDecimal creditAmount;
    private BigDecimal closingDebitAmount;          // Dư Nợ
    private BigDecimal closingDebitAmountOriginal;  // Dư Nợ quy đổi
    private BigDecimal closingCreditAmount;         // Dư Có
    private BigDecimal closingCreditAmountOriginal;
    private Boolean isBold;
    private String unResonableCost;
    private Integer orderNumber;

    private String postedDateToString;
    private String dateToString;
    private String debitAmountOriginalToString;
    private String debitAmountToString;
    private String creditAmountOriginalToString;
    private String creditAmountToString;
    private String closingDebitAmountToString;          // Dư Nợ
    private String closingDebitAmountOriginalToString;  // Dư Nợ quy đổi
    private String closingCreditAmountToString;         // Dư Có
    private String closingCreditAmountOriginalToString;
    private String linkRef;

    public SoChiTietTaiKhoanDTO() {
    }

    public SoChiTietTaiKhoanDTO(String accountNumber, String accountNameWithAccountNumber, LocalDate postedDate, LocalDate date, String no, UUID referenceID, Integer typeID, String journalMemo, String accountCorresponding, BigDecimal debitAmountOriginal, BigDecimal debitAmount, BigDecimal creditAmountOriginal, BigDecimal creditAmount, BigDecimal closingDebitAmount, BigDecimal closingDebitAmountOriginal, BigDecimal closingCreditAmount, BigDecimal closingCreditAmountOriginal, Boolean isBold, String unResonableCost, Integer orderNumber) {
        this.accountNumber = accountNumber;
        this.accountNameWithAccountNumber = accountNameWithAccountNumber;
        this.postedDate = postedDate;
        this.date = date;
        this.no = no;
        this.referenceID = referenceID;
        this.typeID = typeID;
        this.journalMemo = journalMemo;
        this.accountCorresponding = accountCorresponding;
        this.debitAmountOriginal = debitAmountOriginal;
        this.debitAmount = debitAmount;
        this.creditAmountOriginal = creditAmountOriginal;
        this.creditAmount = creditAmount;
        this.closingDebitAmount = closingDebitAmount;
        this.closingDebitAmountOriginal = closingDebitAmountOriginal;
        this.closingCreditAmount = closingCreditAmount;
        this.closingCreditAmountOriginal = closingCreditAmountOriginal;
        this.isBold = isBold;
        this.unResonableCost = unResonableCost;
        this.orderNumber = orderNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNameWithAccountNumber() {
        return accountNameWithAccountNumber;
    }

    public void setAccountNameWithAccountNumber(String accountNameWithAccountNumber) {
        this.accountNameWithAccountNumber = accountNameWithAccountNumber;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public UUID getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getJournalMemo() {
        return journalMemo;
    }

    public void setJournalMemo(String journalMemo) {
        this.journalMemo = journalMemo;
    }

    public String getAccountCorresponding() {
        return accountCorresponding;
    }

    public void setAccountCorresponding(String accountCorresponding) {
        this.accountCorresponding = accountCorresponding;
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

    public BigDecimal getClosingDebitAmount() {
        return closingDebitAmount;
    }

    public void setClosingDebitAmount(BigDecimal closingDebitAmount) {
        this.closingDebitAmount = closingDebitAmount;
    }

    public BigDecimal getClosingDebitAmountOriginal() {
        return closingDebitAmountOriginal;
    }

    public void setClosingDebitAmountOriginal(BigDecimal closingDebitAmountOriginal) {
        this.closingDebitAmountOriginal = closingDebitAmountOriginal;
    }

    public BigDecimal getClosingCreditAmount() {
        return closingCreditAmount;
    }

    public void setClosingCreditAmount(BigDecimal closingCreditAmount) {
        this.closingCreditAmount = closingCreditAmount;
    }

    public BigDecimal getClosingCreditAmountOriginal() {
        return closingCreditAmountOriginal;
    }

    public void setClosingCreditAmountOriginal(BigDecimal closingCreditAmountOriginal) {
        this.closingCreditAmountOriginal = closingCreditAmountOriginal;
    }

    public Boolean getBold() {
        return isBold;
    }

    public void setBold(Boolean bold) {
        isBold = bold;
    }

    public String getUnResonableCost() {
        return unResonableCost;
    }

    public void setUnResonableCost(String unResonableCost) {
        this.unResonableCost = unResonableCost;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPostedDateToString() {
        return postedDateToString;
    }

    public void setPostedDateToString(String postedDateToString) {
        this.postedDateToString = postedDateToString;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public String getDebitAmountOriginalToString() {
        return debitAmountOriginalToString;
    }

    public void setDebitAmountOriginalToString(String debitAmountOriginalToString) {
        this.debitAmountOriginalToString = debitAmountOriginalToString;
    }

    public String getDebitAmountToString() {
        return debitAmountToString;
    }

    public void setDebitAmountToString(String debitAmountToString) {
        this.debitAmountToString = debitAmountToString;
    }

    public String getCreditAmountOriginalToString() {
        return creditAmountOriginalToString;
    }

    public void setCreditAmountOriginalToString(String creditAmountOriginalToString) {
        this.creditAmountOriginalToString = creditAmountOriginalToString;
    }

    public String getCreditAmountToString() {
        return creditAmountToString;
    }

    public void setCreditAmountToString(String creditAmountToString) {
        this.creditAmountToString = creditAmountToString;
    }

    public String getClosingDebitAmountToString() {
        return closingDebitAmountToString;
    }

    public void setClosingDebitAmountToString(String closingDebitAmountToString) {
        this.closingDebitAmountToString = closingDebitAmountToString;
    }

    public String getClosingDebitAmountOriginalToString() {
        return closingDebitAmountOriginalToString;
    }

    public void setClosingDebitAmountOriginalToString(String closingDebitAmountOriginalToString) {
        this.closingDebitAmountOriginalToString = closingDebitAmountOriginalToString;
    }

    public String getClosingCreditAmountToString() {
        return closingCreditAmountToString;
    }

    public void setClosingCreditAmountToString(String closingCreditAmountToString) {
        this.closingCreditAmountToString = closingCreditAmountToString;
    }

    public String getClosingCreditAmountOriginalToString() {
        return closingCreditAmountOriginalToString;
    }

    public void setClosingCreditAmountOriginalToString(String closingCreditAmountOriginalToString) {
        this.closingCreditAmountOriginalToString = closingCreditAmountOriginalToString;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }
}
