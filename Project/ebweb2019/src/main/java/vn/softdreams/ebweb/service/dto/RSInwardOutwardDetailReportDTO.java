package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RSInwardOutwardDetailReportDTO {
    private String materialGoodsCode;
    private String materialGoodsName;
    private String unitName;
    private BigDecimal quantity;
    private BigDecimal amountOriginal;
    private BigDecimal amount;
    private String creditAccount;
    private String debitAccount;
    private String repositoryCode;
    private BigDecimal unitPriceOriginal;
    private String repositoryName;
    private String description;
    private String unitPriceOriginalToString;
    private String amountOriginalToString;
    private String amountToString;
    private String quantityToString;
    private String totalToString;
    private BigDecimal owPrice;
    private BigDecimal owAmount;

    public RSInwardOutwardDetailReportDTO(String materialGoodsCode, String materialGoodsName, String unitName, BigDecimal quantity, BigDecimal amountOriginal, BigDecimal amount, String creditAccount, String debitAccount, String repositoryCode, BigDecimal unitPriceOriginal, String repositoryName, String description, BigDecimal owPrice, BigDecimal owAmount) {
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.unitName = unitName;
        this.quantity = quantity;
        this.amountOriginal = amountOriginal;
        this.amount = amount;
        this.creditAccount = creditAccount;
        this.debitAccount = debitAccount;
        this.repositoryCode = repositoryCode;
        this.unitPriceOriginal = unitPriceOriginal;
        this.repositoryName = repositoryName;
        this.description = description;
        this.owPrice = owPrice;
        this.owAmount = owAmount;
    }

    public RSInwardOutwardDetailReportDTO(String materialGoodsCode, String materialGoodsName, String unitName, BigDecimal quantity, BigDecimal amountOriginal, BigDecimal amount, String creditAccount, String debitAccount, String repositoryCode, BigDecimal unitPriceOriginal, String repositoryName, String description) {
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.unitName = unitName;
        this.quantity = quantity;
        this.amountOriginal = amountOriginal;
        this.amount = amount;
        this.creditAccount = creditAccount;
        this.debitAccount = debitAccount;
        this.repositoryCode = repositoryCode;
        this.unitPriceOriginal = unitPriceOriginal;
        this.repositoryName = repositoryName;
        this.description = description;
    }

    public String getQuantityToString() {
        return quantityToString;
    }

    public void setQuantityToString(String quantityToString) {
        this.quantityToString = quantityToString;
    }

    public String getUnitPriceOriginalToString() {
        return unitPriceOriginalToString;
    }

    public void setUnitPriceOriginalToString(String unitPriceOriginalToString) {
        this.unitPriceOriginalToString = unitPriceOriginalToString;
    }

    public String getAmountOriginalToString() {
        return amountOriginalToString;
    }

    public void setAmountOriginalToString(String amountOriginalToString) {
        this.amountOriginalToString = amountOriginalToString;
    }

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalToString() {
        return totalToString;
    }

    public void setTotalToString(String totalToString) {
        this.totalToString = totalToString;
    }

    public BigDecimal getOwPrice() {
        return owPrice;
    }

    public void setOwPrice(BigDecimal owPrice) {
        this.owPrice = owPrice;
    }

    public BigDecimal getOwAmount() {
        return owAmount;
    }

    public void setOwAmount(BigDecimal owAmount) {
        this.owAmount = owAmount;
    }
}
