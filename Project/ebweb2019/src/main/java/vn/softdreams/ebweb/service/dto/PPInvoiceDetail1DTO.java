package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PPInvoiceDetail1DTO {
    private UUID ppInvoiceID;
    private String description;
    private BigDecimal amount;
    private BigDecimal amountOriginal;
    private String amountString;
    private String amountOriginalString;
    private String creditAccount;
    private String debitAccount;
    private String materialGoodsCode;
    private Integer orderPriority;
    private Boolean isImportPurchase;

    private String vatDescription;
    private BigDecimal vatAmount;
    private BigDecimal vatAmountOriginal;
    private String vatAccount;

    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getVatAmountOriginal() {
        return vatAmountOriginal;
    }

    public void setVatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
    }

    public String getVatAccount() {
        return vatAccount;
    }

    public void setVatAccount(String vatAccount) {
        this.vatAccount = vatAccount;
    }

    public PPInvoiceDetail1DTO(String description, String creditAccount, String debitAccount, BigDecimal amount, BigDecimal amountOriginal, String materialGoodsCode, Integer orderPriority) {
        this.description = description;
        this.creditAccount = creditAccount;
        this.debitAccount = debitAccount;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.materialGoodsCode = materialGoodsCode;
        this.orderPriority = orderPriority;
    }

    public UUID getPpInvoiceID() {
        return ppInvoiceID;
    }

    public void setPpInvoiceID(UUID ppInvoiceID) {
        this.ppInvoiceID = ppInvoiceID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public Boolean getImportPurchase() {
        return isImportPurchase;
    }

    public void setImportPurchase(Boolean importPurchase) {
        isImportPurchase = importPurchase;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getAmountOriginalString() {
        return amountOriginalString;
    }

    public void setAmountOriginalString(String amountOriginalString) {
        this.amountOriginalString = amountOriginalString;
    }
}
