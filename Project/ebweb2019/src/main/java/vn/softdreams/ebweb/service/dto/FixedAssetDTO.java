package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class FixedAssetDTO {
    private UUID id;
    private String fixedAssetCode;
    private String fixedAssetName;
    private UUID departmentID;
    private BigDecimal originalPrice;
    private BigDecimal usedTime;
    private BigDecimal depreciationRate;
    private BigDecimal purchasePrice;
    private String depreciationAccount;
    private String originalPriceAccount;
    private BigDecimal monthDepreciationRate;
    private BigDecimal monthPeriodDepreciationAmount;

    public FixedAssetDTO() {
    }

    public FixedAssetDTO(UUID id, String fixedAssetCode, String fixedAssetName, UUID departmentID, BigDecimal originalPrice) {
        this.id = id;
        this.fixedAssetCode = fixedAssetCode;
        this.fixedAssetName = fixedAssetName;
        this.departmentID = departmentID;
        this.originalPrice = originalPrice;
    }

    public FixedAssetDTO(UUID id, String fixedAssetCode, String fixedAssetName, UUID departmentID, BigDecimal originalPrice, BigDecimal usedTime, BigDecimal depreciationRate, BigDecimal purchasePrice, String depreciationAccount, String originalPriceAccount, BigDecimal monthDepreciationRate, BigDecimal monthPeriodDepreciationAmount) {
        this.id = id;
        this.fixedAssetCode = fixedAssetCode;
        this.fixedAssetName = fixedAssetName;
        this.departmentID = departmentID;
        this.originalPrice = originalPrice;
        this.usedTime = usedTime;
        this.depreciationRate = depreciationRate;
        this.purchasePrice = purchasePrice;
        this.depreciationAccount = depreciationAccount;
        this.originalPriceAccount = originalPriceAccount;
        this.monthDepreciationRate = monthDepreciationRate;
        this.monthPeriodDepreciationAmount = monthPeriodDepreciationAmount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFixedAssetCode() {
        return fixedAssetCode;
    }

    public void setFixedAssetCode(String fixedAssetCode) {
        this.fixedAssetCode = fixedAssetCode;
    }

    public String getFixedAssetName() {
        return fixedAssetName;
    }

    public void setFixedAssetName(String fixedAssetName) {
        this.fixedAssetName = fixedAssetName;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(BigDecimal usedTime) {
        this.usedTime = usedTime;
    }

    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getDepreciationAccount() {
        return depreciationAccount;
    }

    public void setDepreciationAccount(String depreciationAccount) {
        this.depreciationAccount = depreciationAccount;
    }

    public String getOriginalPriceAccount() {
        return originalPriceAccount;
    }

    public void setOriginalPriceAccount(String originalPriceAccount) {
        this.originalPriceAccount = originalPriceAccount;
    }

    public BigDecimal getMonthDepreciationRate() {
        return monthDepreciationRate;
    }

    public void setMonthDepreciationRate(BigDecimal monthDepreciationRate) {
        this.monthDepreciationRate = monthDepreciationRate;
    }

    public BigDecimal getMonthPeriodDepreciationAmount() {
        return monthPeriodDepreciationAmount;
    }

    public void setMonthPeriodDepreciationAmount(BigDecimal monthPeriodDepreciationAmount) {
        this.monthPeriodDepreciationAmount = monthPeriodDepreciationAmount;
    }
}
