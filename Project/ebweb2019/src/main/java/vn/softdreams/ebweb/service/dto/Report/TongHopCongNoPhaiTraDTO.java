package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TongHopCongNoPhaiTraDTO {
    private String accountObjectCode;
    private String accountObjectName;
    private String accountNumber;
    private BigDecimal openingDebitAmount;
    private BigDecimal openingCreditAmount;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal closeDebitAmount;
    private BigDecimal closeCreditAmount;
    private BigDecimal openingDebitAmountOC;
    private BigDecimal openingCreditAmountOC;
    private BigDecimal debitAmountOC;
    private BigDecimal creditAmountOC;
    private BigDecimal closeDebitAmountOC;
    private BigDecimal closeCreditAmountOC;
    private BigDecimal accumDebitAmountOC;
    private BigDecimal accumDebitAmount;
    private BigDecimal accumCreditAmountOC;
    private BigDecimal accumCreditAmount;


    private String openingDebitAmountToString;
    private String openingCreditAmountToString;
    private String debitAmountToString;
    private String creditAmountToString;
        private String closeDebitAmountToString;
    private String closeCreditAmountToString;

    public TongHopCongNoPhaiTraDTO() {
    }

    public TongHopCongNoPhaiTraDTO(String accountObjectCode, String accountObjectName, String accountNumber, BigDecimal openingDebitAmount, BigDecimal openingCreditAmount, BigDecimal debitAmount, BigDecimal creditAmount, BigDecimal closeDebitAmount, BigDecimal closeCreditAmount, BigDecimal openingDebitAmountOC, BigDecimal openingCreditAmountOC, BigDecimal debitAmountOC, BigDecimal creditAmountOC, BigDecimal closeDebitAmountOC, BigDecimal closeCreditAmountOC, BigDecimal accumDebitAmountOC, BigDecimal accumDebitAmount, BigDecimal accumCreditAmountOC) {
        this.accountObjectCode = accountObjectCode;
        this.accountObjectName = accountObjectName;
        this.accountNumber = accountNumber;
        this.openingDebitAmount = openingDebitAmount;
        this.openingCreditAmount = openingCreditAmount;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.closeDebitAmount = closeDebitAmount;
        this.closeCreditAmount = closeCreditAmount;
        this.openingDebitAmountOC = openingDebitAmountOC;
        this.openingCreditAmountOC = openingCreditAmountOC;
        this.debitAmountOC = debitAmountOC;
        this.creditAmountOC = creditAmountOC;
        this.closeDebitAmountOC = closeDebitAmountOC;
        this.closeCreditAmountOC = closeCreditAmountOC;
        this.accumDebitAmountOC = accumDebitAmountOC;
        this.accumDebitAmount = accumDebitAmount;
        this.accumCreditAmountOC = accumCreditAmountOC;
    }

    public String getAccountObjectCode() {
        return accountObjectCode;
    }

    public void setAccountObjectCode(String accountObjectCode) {
        this.accountObjectCode = accountObjectCode;
    }

    public String getAccountObjectName() {
        return accountObjectName;
    }

    public void setAccountObjectName(String accountObjectName) {
        this.accountObjectName = accountObjectName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getOpeningDebitAmount() {
        return openingDebitAmount;
    }

    public void setOpeningDebitAmount(BigDecimal openingDebitAmount) {
        this.openingDebitAmount = openingDebitAmount;
    }

    public BigDecimal getOpeningCreditAmount() {
        return openingCreditAmount;
    }

    public void setOpeningCreditAmount(BigDecimal openingCreditAmount) {
        this.openingCreditAmount = openingCreditAmount;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
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

    public BigDecimal getOpeningDebitAmountOC() {
        return openingDebitAmountOC;
    }

    public void setOpeningDebitAmountOC(BigDecimal openingDebitAmountOC) {
        this.openingDebitAmountOC = openingDebitAmountOC;
    }

    public BigDecimal getOpeningCreditAmountOC() {
        return openingCreditAmountOC;
    }

    public void setOpeningCreditAmountOC(BigDecimal openingCreditAmountOC) {
        this.openingCreditAmountOC = openingCreditAmountOC;
    }

    public BigDecimal getDebitAmountOC() {
        return debitAmountOC;
    }

    public void setDebitAmountOC(BigDecimal debitAmountOC) {
        this.debitAmountOC = debitAmountOC;
    }

    public BigDecimal getCreditAmountOC() {
        return creditAmountOC;
    }

    public void setCreditAmountOC(BigDecimal creditAmountOC) {
        this.creditAmountOC = creditAmountOC;
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

    public String getOpeningDebitAmountToString() {
        return openingDebitAmountToString;
    }

    public void setOpeningDebitAmountToString(String openingDebitAmountToString) {
        this.openingDebitAmountToString = openingDebitAmountToString;
    }

    public String getOpeningCreditAmountToString() {
        return openingCreditAmountToString;
    }

    public void setOpeningCreditAmountToString(String openingCreditAmountToString) {
        this.openingCreditAmountToString = openingCreditAmountToString;
    }

    public String getDebitAmountToString() {
        return debitAmountToString;
    }

    public void setDebitAmountToString(String debitAmountToString) {
        this.debitAmountToString = debitAmountToString;
    }

    public String getCreditAmountToString() {
        return creditAmountToString;
    }

    public void setCreditAmountToString(String creditAmountToString) {
        this.creditAmountToString = creditAmountToString;
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
}

