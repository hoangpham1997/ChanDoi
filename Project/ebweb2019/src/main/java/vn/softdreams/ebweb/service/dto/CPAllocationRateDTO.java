package vn.softdreams.ebweb.service.dto;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.UUID;

public class CPAllocationRateDTO {
    private UUID id;
    private UUID cPPeriodID;
    private Integer allocationMethod;
    private UUID costSetID;
    private String costSetCode;
    private UUID materialGoodsID;
    private String materialGoodsCode;
    private String materialGoodsName;
    private Boolean isStandardItem;
    private BigDecimal quantity;
    private BigDecimal priceQuantum;
    private BigDecimal coefficient;
    private BigDecimal quantityStandard;
    private BigDecimal allocationStandard;
    private BigDecimal allocatedRate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public Integer getAllocationMethod() {
        return allocationMethod;
    }

    public void setAllocationMethod(Integer allocationMethod) {
        this.allocationMethod = allocationMethod;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public Boolean isIsStandardItem() {
        return isStandardItem;
    }

    public void setIsStandardItem(Boolean isStandardItem) {
        isStandardItem = isStandardItem;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceQuantum() {
        return priceQuantum;
    }

    public void setPriceQuantum(BigDecimal priceQuantum) {
        this.priceQuantum = priceQuantum;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public BigDecimal getQuantityStandard() {
        return quantityStandard;
    }

    public void setQuantityStandard(BigDecimal quantityStandard) {
        this.quantityStandard = quantityStandard;
    }

    public BigDecimal getAllocationStandard() {
        return allocationStandard;
    }

    public void setAllocationStandard(BigDecimal allocationStandard) {
        this.allocationStandard = allocationStandard;
    }

    public BigDecimal getAllocatedRate() {
        return allocatedRate;
    }

    public void setAllocatedRate(BigDecimal allocatedRate) {
        this.allocatedRate = allocatedRate;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
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

    public CPAllocationRateDTO() {
    }

    public CPAllocationRateDTO(Integer allocationMethod, UUID costSetID, UUID materialGoodsID, Boolean isStandardItem, BigDecimal quantity, BigDecimal priceQuantum, BigDecimal coefficient, BigDecimal quantityStandard, BigDecimal allocationStandard, BigDecimal allocatedRate) {
        this.allocationMethod = allocationMethod;
        this.costSetID = costSetID;
        this.materialGoodsID = materialGoodsID;
        this.isStandardItem = isStandardItem;
        this.quantity = quantity;
        this.priceQuantum = priceQuantum;
        this.coefficient = coefficient;
        this.quantityStandard = quantityStandard;
        this.allocationStandard = allocationStandard;
        this.allocatedRate = allocatedRate;
    }

    public CPAllocationRateDTO(UUID id, UUID cPPeriodID, Integer allocationMethod, UUID costSetID, String costSetCode, UUID materialGoodsID, String materialGoodsCode, String materialGoodsName, Boolean isStandardItem, BigDecimal quantity, BigDecimal priceQuantum, BigDecimal coefficient, BigDecimal quantityStandard, BigDecimal allocationStandard, BigDecimal allocatedRate) {
        this.id = id;
        this.cPPeriodID = cPPeriodID;
        this.allocationMethod = allocationMethod;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.isStandardItem = isStandardItem;
        this.quantity = quantity;
        this.priceQuantum = priceQuantum;
        this.coefficient = coefficient;
        this.quantityStandard = quantityStandard;
        this.allocationStandard = allocationStandard;
        this.allocatedRate = allocatedRate;
    }
}

