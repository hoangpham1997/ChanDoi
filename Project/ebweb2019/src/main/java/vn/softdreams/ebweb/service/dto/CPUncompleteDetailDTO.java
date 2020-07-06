package vn.softdreams.ebweb.service.dto;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.UUID;

public class CPUncompleteDetailDTO {
    private UUID id;
    private UUID cPUncompleteID;
    private UUID cPPeriodID;
    private UUID costSetID;
    private String costSetCode;
    private String costSetName;
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

    public CPUncompleteDetailDTO(UUID id, UUID cPUncompleteID, UUID cPPeriodID, UUID costSetID, String costSetCode, String costSetName, BigDecimal directMatetialAmount, BigDecimal directLaborAmount, BigDecimal machineMatetialAmount, BigDecimal machineLaborAmount, BigDecimal machineToolsAmount, BigDecimal machineDepreciationAmount, BigDecimal machineServiceAmount, BigDecimal machineGeneralAmount, BigDecimal generalMatetialAmount, BigDecimal generalLaborAmount, BigDecimal generalToolsAmount, BigDecimal generalDepreciationAmount, BigDecimal generalServiceAmount, BigDecimal otherGeneralAmount, BigDecimal totalCostAmount) {
        this.id = id;
        this.cPUncompleteID = cPUncompleteID;
        this.cPPeriodID = cPPeriodID;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
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
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getcPUncompleteID() {
        return cPUncompleteID;
    }

    public void setcPUncompleteID(UUID cPUncompleteID) {
        this.cPUncompleteID = cPUncompleteID;
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

    public String getCostSetName() {
        return costSetName;
    }

    public void setCostSetName(String costSetName) {
        this.costSetName = costSetName;
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
}

