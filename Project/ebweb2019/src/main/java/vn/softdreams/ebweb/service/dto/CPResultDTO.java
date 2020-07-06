package vn.softdreams.ebweb.service.dto;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.UUID;

public class CPResultDTO {
    private UUID id;
    private UUID cPPeriodID;
    private UUID costSetID;
    private String costSetCode;
    private UUID materialGoodsID;
    private String materialGoodsCode;
    private String materialGoodsName;
    private BigDecimal directMatetialAmount;
    private BigDecimal directLaborAmount;
    private BigDecimal machineMatetialAmount;
    private BigDecimal machineLaborAmount;
    private BigDecimal machineToolsAmount;
    private BigDecimal machineDepreciationAmount;
    private BigDecimal machineServiceAmount;
    private BigDecimal machineGeneralAmount;
    private BigDecimal generalMatetialAmount;
    private BigDecimal generalLaborAmount;
    private BigDecimal generalToolsAmount;
    private BigDecimal generalDepreciationAmount;
    private BigDecimal generalServiceAmount;
    private BigDecimal otherGeneralAmount;
    private BigDecimal totalCostAmount;
    private BigDecimal totalQuantity;
    private BigDecimal unitPrice;
    private BigDecimal coefficien;

    public CPResultDTO(UUID id, UUID cPPeriodID, UUID costSetID, String costSetCode, UUID materialGoodsID, String materialGoodsCode, String materialGoodsName, BigDecimal directMatetialAmount, BigDecimal directLaborAmount, BigDecimal machineMatetialAmount, BigDecimal machineLaborAmount, BigDecimal machineToolsAmount, BigDecimal machineDepreciationAmount, BigDecimal machineServiceAmount, BigDecimal machineGeneralAmount, BigDecimal generalMatetialAmount, BigDecimal generalLaborAmount, BigDecimal generalToolsAmount, BigDecimal generalDepreciationAmount, BigDecimal generalServiceAmount, BigDecimal otherGeneralAmount, BigDecimal totalCostAmount, BigDecimal totalQuantity, BigDecimal unitPrice, BigDecimal coefficien) {
        this.id = id;
        this.cPPeriodID = cPPeriodID;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.directMatetialAmount = directMatetialAmount;
        this.directLaborAmount = directLaborAmount;
        this.machineMatetialAmount = machineMatetialAmount;
        this.machineLaborAmount = machineLaborAmount;
        this.machineToolsAmount = machineToolsAmount;
        this.machineDepreciationAmount = machineDepreciationAmount;
        this.machineServiceAmount = machineServiceAmount;
        this.machineGeneralAmount = machineGeneralAmount;
        this.generalMatetialAmount = generalMatetialAmount;
        this.generalLaborAmount = generalLaborAmount;
        this.generalToolsAmount = generalToolsAmount;
        this.generalDepreciationAmount = generalDepreciationAmount;
        this.generalServiceAmount = generalServiceAmount;
        this.otherGeneralAmount = otherGeneralAmount;
        this.totalCostAmount = totalCostAmount;
        this.totalQuantity = totalQuantity;
        this.unitPrice = unitPrice;
        this.coefficien = coefficien;
    }

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

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
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

    public BigDecimal getDirectMatetialAmount() {
        return directMatetialAmount;
    }

    public void setDirectMatetialAmount(BigDecimal directMatetialAmount) {
        this.directMatetialAmount = directMatetialAmount;
    }

    public BigDecimal getDirectLaborAmount() {
        return directLaborAmount;
    }

    public void setDirectLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
    }

    public BigDecimal getMachineMatetialAmount() {
        return machineMatetialAmount;
    }

    public void setMachineMatetialAmount(BigDecimal machineMatetialAmount) {
        this.machineMatetialAmount = machineMatetialAmount;
    }

    public BigDecimal getMachineLaborAmount() {
        return machineLaborAmount;
    }

    public void setMachineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
    }

    public BigDecimal getMachineToolsAmount() {
        return machineToolsAmount;
    }

    public void setMachineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
    }

    public BigDecimal getMachineDepreciationAmount() {
        return machineDepreciationAmount;
    }

    public void setMachineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
    }

    public BigDecimal getMachineServiceAmount() {
        return machineServiceAmount;
    }

    public void setMachineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
    }

    public BigDecimal getMachineGeneralAmount() {
        return machineGeneralAmount;
    }

    public void setMachineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
    }

    public BigDecimal getGeneralMatetialAmount() {
        return generalMatetialAmount;
    }

    public void setGeneralMatetialAmount(BigDecimal generalMatetialAmount) {
        this.generalMatetialAmount = generalMatetialAmount;
    }

    public BigDecimal getGeneralLaborAmount() {
        return generalLaborAmount;
    }

    public void setGeneralLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
    }

    public BigDecimal getGeneralToolsAmount() {
        return generalToolsAmount;
    }

    public void setGeneralToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
    }

    public BigDecimal getGeneralDepreciationAmount() {
        return generalDepreciationAmount;
    }

    public void setGeneralDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
    }

    public BigDecimal getGeneralServiceAmount() {
        return generalServiceAmount;
    }

    public void setGeneralServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
    }

    public BigDecimal getOtherGeneralAmount() {
        return otherGeneralAmount;
    }

    public void setOtherGeneralAmount(BigDecimal otherGeneralAmount) {
        this.otherGeneralAmount = otherGeneralAmount;
    }

    public BigDecimal getTotalCostAmount() {
        return totalCostAmount;
    }

    public void setTotalCostAmount(BigDecimal totalCostAmount) {
        this.totalCostAmount = totalCostAmount;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getCoefficien() {
        return coefficien;
    }

    public void setCoefficien(BigDecimal coefficien) {
        this.coefficien = coefficien;
    }
}

