package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;

public class TongHopCongNoPhaiThuDTO {
    private String accountingObjectID;
    private String accountingObjectCode;
    private String accountingObjectName;
    private String accountObjectAddress;
    private String accountObjectTaxCode;
    private String accountNumber;
    private BigDecimal openningDebitAmountOC;
    private BigDecimal openningDebitAmount;
    private BigDecimal openningCreditAmountOC;
    private BigDecimal openningCreditAmount;
    private BigDecimal debitAmountOC;
    private BigDecimal debitAmount;
    private BigDecimal creditAmountOC;
    private BigDecimal creditAmount;
    private BigDecimal accumDebitAmountOC;
    private BigDecimal accumDebitAmount;
    private BigDecimal accumCreditAmountOC;
    private BigDecimal accumCreditAmount;
    private BigDecimal closeDebitAmountOC;
    private BigDecimal closeCreditAmountOC;
    private BigDecimal closeDebitAmount;
    private BigDecimal closeCreditAmount;
    private String accountObjectGroupListCode;
    private String accountObjectCategoryName;

    private String openningDebitAmountOCToString;
    private String openningDebitAmountToString;
    private String openningCreditAmountOCToString;
    private String openningCreditAmountToString;
    private String debitAmountOCToString;
    private String debitAmountToString;
    private String creditAmountOCToString;
    private String creditAmountToString;
    private String accumDebitAmountOCToString;
    private String accumDebitAmountToString;
    private String accumCreditAmountOCToString;
    private String accumCreditAmountToString;
    private String closeDebitAmountOCToString;
    private String closeCreditAmountOCToString;
    private String closeDebitAmountToString;
    private String closeCreditAmountToString;

    public TongHopCongNoPhaiThuDTO() {
    }

    public TongHopCongNoPhaiThuDTO(String accountingObjectID, String accountingObjectCode, String accountingObjectName, String accountObjectAddress, String accountObjectTaxCode, String accountNumber, BigDecimal openningDebitAmountOC, BigDecimal openningDebitAmount, BigDecimal openningCreditAmountOC, BigDecimal openningCreditAmount, BigDecimal debitAmountOC, BigDecimal debitAmount, BigDecimal creditAmountOC, BigDecimal creditAmount, BigDecimal accumDebitAmountOC, BigDecimal accumDebitAmount, BigDecimal accumCreditAmountOC, BigDecimal accumCreditAmount, BigDecimal closeDebitAmountOC, BigDecimal closeCreditAmountOC, BigDecimal closeDebitAmount, BigDecimal closeCreditAmount, String accountObjectGroupListCode, String accountObjectCategoryName) {
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectCode = accountingObjectCode;
        this.accountingObjectName = accountingObjectName;
        this.accountObjectAddress = accountObjectAddress;
        this.accountObjectTaxCode = accountObjectTaxCode;
        this.accountNumber = accountNumber;
        this.openningDebitAmountOC = openningDebitAmountOC;
        this.openningDebitAmount = openningDebitAmount;
        this.openningCreditAmountOC = openningCreditAmountOC;
        this.openningCreditAmount = openningCreditAmount;
        this.debitAmountOC = debitAmountOC;
        this.debitAmount = debitAmount;
        this.creditAmountOC = creditAmountOC;
        this.creditAmount = creditAmount;
        this.accumDebitAmountOC = accumDebitAmountOC;
        this.accumDebitAmount = accumDebitAmount;
        this.accumCreditAmountOC = accumCreditAmountOC;
        this.accumCreditAmount = accumCreditAmount;
        this.closeDebitAmountOC = closeDebitAmountOC;
        this.closeCreditAmountOC = closeCreditAmountOC;
        this.closeDebitAmount = closeDebitAmount;
        this.closeCreditAmount = closeCreditAmount;
        this.accountObjectGroupListCode = accountObjectGroupListCode;
        this.accountObjectCategoryName = accountObjectCategoryName;
    }

    public String getOpenningDebitAmountOCToString() {
        return openningDebitAmountOCToString;
    }

    public void setOpenningDebitAmountOCToString(String openningDebitAmountOCToString) {
        this.openningDebitAmountOCToString = openningDebitAmountOCToString;
    }

    public String getOpenningDebitAmountToString() {
        return openningDebitAmountToString;
    }

    public void setOpenningDebitAmountToString(String openningDebitAmountToString) {
        this.openningDebitAmountToString = openningDebitAmountToString;
    }

    public String getOpenningCreditAmountOCToString() {
        return openningCreditAmountOCToString;
    }

    public void setOpenningCreditAmountOCToString(String openningCreditAmountOCToString) {
        this.openningCreditAmountOCToString = openningCreditAmountOCToString;
    }

    public String getOpenningCreditAmountToString() {
        return openningCreditAmountToString;
    }

    public void setOpenningCreditAmountToString(String openningCreditAmountToString) {
        this.openningCreditAmountToString = openningCreditAmountToString;
    }

    public String getDebitAmountOCToString() {
        return debitAmountOCToString;
    }

    public void setDebitAmountOCToString(String debitAmountOCToString) {
        this.debitAmountOCToString = debitAmountOCToString;
    }

    public String getDebitAmountToString() {
        return debitAmountToString;
    }

    public void setDebitAmountToString(String debitAmountToString) {
        this.debitAmountToString = debitAmountToString;
    }

    public String getCreditAmountOCToString() {
        return creditAmountOCToString;
    }

    public void setCreditAmountOCToString(String creditAmountOCToString) {
        this.creditAmountOCToString = creditAmountOCToString;
    }

    public String getCreditAmountToString() {
        return creditAmountToString;
    }

    public void setCreditAmountToString(String creditAmountToString) {
        this.creditAmountToString = creditAmountToString;
    }

    public String getAccumDebitAmountOCToString() {
        return accumDebitAmountOCToString;
    }

    public void setAccumDebitAmountOCToString(String accumDebitAmountOCToString) {
        this.accumDebitAmountOCToString = accumDebitAmountOCToString;
    }

    public String getAccumDebitAmountToString() {
        return accumDebitAmountToString;
    }

    public void setAccumDebitAmountToString(String accumDebitAmountToString) {
        this.accumDebitAmountToString = accumDebitAmountToString;
    }

    public String getAccumCreditAmountOCToString() {
        return accumCreditAmountOCToString;
    }

    public void setAccumCreditAmountOCToString(String accumCreditAmountOCToString) {
        this.accumCreditAmountOCToString = accumCreditAmountOCToString;
    }

    public String getAccumCreditAmountToString() {
        return accumCreditAmountToString;
    }

    public void setAccumCreditAmountToString(String accumCreditAmountToString) {
        this.accumCreditAmountToString = accumCreditAmountToString;
    }

    public String getCloseDebitAmountOCToString() {
        return closeDebitAmountOCToString;
    }

    public void setCloseDebitAmountOCToString(String closeDebitAmountOCToString) {
        this.closeDebitAmountOCToString = closeDebitAmountOCToString;
    }

    public String getCloseCreditAmountOCToString() {
        return closeCreditAmountOCToString;
    }

    public void setCloseCreditAmountOCToString(String closeCreditAmountOCToString) {
        this.closeCreditAmountOCToString = closeCreditAmountOCToString;
    }

    public String getCloseDebitAmountToString() {
        return closeDebitAmountToString;
    }

    public void setCloseDebitAmountToString(String closeDebitAmountToString) {
        this.closeDebitAmountToString = closeDebitAmountToString;
    }

    public String getCloseCreditAmountToString() {
        return closeCreditAmountToString;
    }

    public void setCloseCreditAmountToString(String closeCreditAmountToString) {
        this.closeCreditAmountToString = closeCreditAmountToString;
    }

    public String getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(String accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountObjectAddress() {
        return accountObjectAddress;
    }

    public void setAccountObjectAddress(String accountObjectAddress) {
        this.accountObjectAddress = accountObjectAddress;
    }

    public String getAccountObjectTaxCode() {
        return accountObjectTaxCode;
    }

    public void setAccountObjectTaxCode(String accountObjectTaxCode) {
        this.accountObjectTaxCode = accountObjectTaxCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getOpenningDebitAmountOC() {
        return openningDebitAmountOC;
    }

    public void setOpenningDebitAmountOC(BigDecimal openningDebitAmountOC) {
        this.openningDebitAmountOC = openningDebitAmountOC;
    }

    public BigDecimal getOpenningDebitAmount() {
        return openningDebitAmount;
    }

    public void setOpenningDebitAmount(BigDecimal openningDebitAmount) {
        this.openningDebitAmount = openningDebitAmount;
    }

    public BigDecimal getOpenningCreditAmountOC() {
        return openningCreditAmountOC;
    }

    public void setOpenningCreditAmountOC(BigDecimal openningCreditAmountOC) {
        this.openningCreditAmountOC = openningCreditAmountOC;
    }

    public BigDecimal getOpenningCreditAmount() {
        return openningCreditAmount;
    }

    public void setOpenningCreditAmount(BigDecimal openningCreditAmount) {
        this.openningCreditAmount = openningCreditAmount;
    }

    public BigDecimal getDebitAmountOC() {
        return debitAmountOC;
    }

    public void setDebitAmountOC(BigDecimal debitAmountOC) {
        this.debitAmountOC = debitAmountOC;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmountOC() {
        return creditAmountOC;
    }

    public void setCreditAmountOC(BigDecimal creditAmountOC) {
        this.creditAmountOC = creditAmountOC;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getAccumDebitAmountOC() {
        return accumDebitAmountOC;
    }

    public void setAccumDebitAmountOC(BigDecimal accumDebitAmountOC) {
        this.accumDebitAmountOC = accumDebitAmountOC;
    }

    public BigDecimal getAccumDebitAmount() {
        return accumDebitAmount;
    }

    public void setAccumDebitAmount(BigDecimal accumDebitAmount) {
        this.accumDebitAmount = accumDebitAmount;
    }

    public BigDecimal getAccumCreditAmountOC() {
        return accumCreditAmountOC;
    }

    public void setAccumCreditAmountOC(BigDecimal accumCreditAmountOC) {
        this.accumCreditAmountOC = accumCreditAmountOC;
    }

    public BigDecimal getAccumCreditAmount() {
        return accumCreditAmount;
    }

    public void setAccumCreditAmount(BigDecimal accumCreditAmount) {
        this.accumCreditAmount = accumCreditAmount;
    }

    public BigDecimal getCloseDebitAmountOC() {
        return closeDebitAmountOC;
    }

    public void setCloseDebitAmountOC(BigDecimal closeDebitAmountOC) {
        this.closeDebitAmountOC = closeDebitAmountOC;
    }

    public BigDecimal getCloseCreditAmountOC() {
        return closeCreditAmountOC;
    }

    public void setCloseCreditAmountOC(BigDecimal closeCreditAmountOC) {
        this.closeCreditAmountOC = closeCreditAmountOC;
    }

    public BigDecimal getCloseDebitAmount() {
        return closeDebitAmount;
    }

    public void setCloseDebitAmount(BigDecimal closeDebitAmount) {
        this.closeDebitAmount = closeDebitAmount;
    }

    public BigDecimal getCloseCreditAmount() {
        return closeCreditAmount;
    }

    public void setCloseCreditAmount(BigDecimal closeCreditAmount) {
        this.closeCreditAmount = closeCreditAmount;
    }

    public String getAccountObjectGroupListCode() {
        return accountObjectGroupListCode;
    }

    public void setAccountObjectGroupListCode(String accountObjectGroupListCode) {
        this.accountObjectGroupListCode = accountObjectGroupListCode;
    }

    public String getAccountObjectCategoryName() {
        return accountObjectCategoryName;
    }

    public void setAccountObjectCategoryName(String accountObjectCategoryName) {
        this.accountObjectCategoryName = accountObjectCategoryName;
    }
}

