package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ViewGLPayExceedCashDTO {
    private String account;
    private UUID companyID;
    private Integer typeLedger;
    private BigDecimal debitAmount;
    private BigDecimal debitAmountOriginal;
    private BigDecimal creditAmount;
    private BigDecimal creditAmountOriginal;
    private UUID bankAccountDetailID;

    public ViewGLPayExceedCashDTO(String account, UUID companyID, Integer typeLedger, BigDecimal debitAmount, BigDecimal debitAmountOriginal, BigDecimal creditAmount, BigDecimal creditAmountOriginal,
                                  UUID bankAccountDetailID) {
        this.account = account;
        this.companyID = companyID;
        this.typeLedger = typeLedger;
        this.debitAmount = debitAmount;
        this.debitAmountOriginal = debitAmountOriginal;
        this.creditAmount = creditAmount;
        this.creditAmountOriginal = creditAmountOriginal;
        this.bankAccountDetailID = bankAccountDetailID;
    }


    public ViewGLPayExceedCashDTO(String account, BigDecimal debitAmount, BigDecimal debitAmountOriginal, BigDecimal creditAmount, BigDecimal creditAmountOriginal) {
        this.account = account;
        this.debitAmount = debitAmount;
        this.debitAmountOriginal = debitAmountOriginal;
        this.creditAmount = creditAmount;
        this.creditAmountOriginal = creditAmountOriginal;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getDebitAmountOriginal() {
        return debitAmountOriginal;
    }

    public void setDebitAmountOriginal(BigDecimal debitAmountOriginal) {
        this.debitAmountOriginal = debitAmountOriginal;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmountOriginal() {
        return creditAmountOriginal;
    }

    public void setCreditAmountOriginal(BigDecimal creditAmountOriginal) {
        this.creditAmountOriginal = creditAmountOriginal;
    }

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }
}
