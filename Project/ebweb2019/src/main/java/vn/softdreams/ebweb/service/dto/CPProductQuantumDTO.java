package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CPProductQuantumDTO {
    private UUID id;
    private String materialGoodsCode;
    private String materialGoodsName;
    private UUID unitID;
    private BigDecimal directMaterialAmount;
    private BigDecimal directLaborAmount;
    private BigDecimal machineMaterialAmount;
    private BigDecimal machineLaborAmount;
    private BigDecimal machineToolsAmount;
    private BigDecimal machineDepreciationAmount;
    private BigDecimal machineServiceAmount;
    private BigDecimal machineGeneralAmount;
    private BigDecimal generalMaterialAmount;
    private BigDecimal generalLaborAmount;
    private BigDecimal generalToolsAmount;
    private BigDecimal generalDepreciationAmount;
    private BigDecimal generalServiceAmount;
    private BigDecimal otherGeneralAmount;
    private BigDecimal totalCostAmount;
    private UUID materialGoodsID;
    public CPProductQuantumDTO(UUID id, String materialGoodsCode, String materialGoodsName, UUID unitID, BigDecimal directMaterialAmount,
                               BigDecimal directLaborAmount, BigDecimal machineMaterialAmount, BigDecimal machineLaborAmount,
                               BigDecimal machineToolsAmount, BigDecimal machineDepreciationAmount, BigDecimal machineServiceAmount, BigDecimal machineGeneralAmount,
                               BigDecimal generalMaterialAmount, BigDecimal generalLaborAmount, BigDecimal generalToolsAmount,
                               BigDecimal generalDepreciationAmount, BigDecimal generalServiceAmount, BigDecimal otherGeneralAmount,
                               BigDecimal totalCostAmount, UUID materialGoodsID) {
        this.id = id;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.unitID = unitID;
        this.directMaterialAmount = directMaterialAmount;
        this.directLaborAmount = directLaborAmount;
        this.machineMaterialAmount = machineMaterialAmount;
        this.machineLaborAmount = machineLaborAmount;
        this.machineToolsAmount = machineToolsAmount;
        this.machineDepreciationAmount = machineDepreciationAmount;
        this.machineServiceAmount = machineServiceAmount;
        this.machineGeneralAmount = machineGeneralAmount;
        this.generalMaterialAmount = generalMaterialAmount;
        this.generalLaborAmount = generalLaborAmount;
        this.generalToolsAmount = generalToolsAmount;
        this.generalDepreciationAmount = generalDepreciationAmount;
        this.generalServiceAmount = generalServiceAmount;
        this.otherGeneralAmount = otherGeneralAmount;
        this.totalCostAmount = totalCostAmount;
        this.materialGoodsID = materialGoodsID;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public BigDecimal getGeneralMaterialAmount() {
        return generalMaterialAmount;
    }

    public void setGeneralMaterialAmount(BigDecimal generalMaterialAmount) {
        this.generalMaterialAmount = generalMaterialAmount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getDirectLaborAmount() {
        return directLaborAmount;
    }

    public void setDirectLaborAmount(BigDecimal directLaborAmount) {
        this.directLaborAmount = directLaborAmount;
    }

    public BigDecimal getDirectMaterialAmount() {
        return directMaterialAmount;
    }

    public void setDirectMaterialAmount(BigDecimal directMaterialAmount) {
        this.directMaterialAmount = directMaterialAmount;
    }

    public BigDecimal getGeneralDepreciationAmount() {
        return generalDepreciationAmount;
    }

    public void setGeneralDepreciationAmount(BigDecimal generalDepreciationAmount) {
        this.generalDepreciationAmount = generalDepreciationAmount;
    }

    public BigDecimal getGeneralLaborAmount() {
        return generalLaborAmount;
    }

    public void setGeneralLaborAmount(BigDecimal generalLaborAmount) {
        this.generalLaborAmount = generalLaborAmount;
    }

    public BigDecimal getGeneralServiceAmount() {
        return generalServiceAmount;
    }

    public void setGeneralServiceAmount(BigDecimal generalServiceAmount) {
        this.generalServiceAmount = generalServiceAmount;
    }

    public BigDecimal getGeneralToolsAmount() {
        return generalToolsAmount;
    }

    public void setGeneralToolsAmount(BigDecimal generalToolsAmount) {
        this.generalToolsAmount = generalToolsAmount;
    }

    public BigDecimal getMachineDepreciationAmount() {
        return machineDepreciationAmount;
    }

    public void setMachineDepreciationAmount(BigDecimal machineDepreciationAmount) {
        this.machineDepreciationAmount = machineDepreciationAmount;
    }

    public BigDecimal getMachineGeneralAmount() {
        return machineGeneralAmount;
    }

    public void setMachineGeneralAmount(BigDecimal machineGeneralAmount) {
        this.machineGeneralAmount = machineGeneralAmount;
    }

    public BigDecimal getMachineLaborAmount() {
        return machineLaborAmount;
    }

    public void setMachineLaborAmount(BigDecimal machineLaborAmount) {
        this.machineLaborAmount = machineLaborAmount;
    }

    public BigDecimal getMachineMaterialAmount() {
        return machineMaterialAmount;
    }

    public void setMachineMaterialAmount(BigDecimal machineMaterialAmount) {
        this.machineMaterialAmount = machineMaterialAmount;
    }

    public BigDecimal getMachineToolsAmount() {
        return machineToolsAmount;
    }

    public void setMachineToolsAmount(BigDecimal machineToolsAmount) {
        this.machineToolsAmount = machineToolsAmount;
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

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public BigDecimal getMachineServiceAmount() {
        return machineServiceAmount;
    }

    public void setMachineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
    }
}

