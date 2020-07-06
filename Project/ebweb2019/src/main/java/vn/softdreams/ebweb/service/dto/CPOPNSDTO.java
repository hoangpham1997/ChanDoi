package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CPOPNSDTO {
    private UUID id;
    private Integer objectType;
    private UUID costSetID;
    private String costSetCode;
    private String costSetName;
    private UUID contractID;
    private String contractCode;
    private LocalDate signedDate;
    private UUID accountingObjectID;
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
    private BigDecimal acceptedAmount;
    private BigDecimal notAcceptedAmount;
    private String uncompletedAccount ;
    private UUID objectID;

    public CPOPNSDTO(UUID id, Integer objectType, UUID costSetID, String costSetCode, String costSetName, UUID contractID, String contractCode, LocalDate signedDate, UUID accountingObjectID, BigDecimal directMaterialAmount,
                     BigDecimal directLaborAmount, BigDecimal machineMaterialAmount, BigDecimal machineLaborAmount,
                     BigDecimal machineToolsAmount, BigDecimal machineDepreciationAmount, BigDecimal machineServiceAmount, BigDecimal machineGeneralAmount,
                     BigDecimal generalMaterialAmount, BigDecimal generalLaborAmount, BigDecimal generalToolsAmount,
                     BigDecimal generalDepreciationAmount, BigDecimal generalServiceAmount, BigDecimal otherGeneralAmount,
                     BigDecimal totalCostAmount, BigDecimal acceptedAmount, BigDecimal notAcceptedAmount, String uncompletedAccount,
                     UUID objectID) {
        this.id = id;
        this.objectType = objectType;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
        this.contractID = contractID;
        this.contractCode = contractCode;
        this.signedDate = signedDate;
        this.accountingObjectID = accountingObjectID;
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
        this.acceptedAmount = acceptedAmount;
        this.notAcceptedAmount = notAcceptedAmount;
        this.uncompletedAccount = uncompletedAccount;
        this.objectID = objectID;
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

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public LocalDate getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(LocalDate signedDate) {
        this.signedDate = signedDate;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
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

    public BigDecimal getMachineServiceAmount() {
        return machineServiceAmount;
    }

    public void setMachineServiceAmount(BigDecimal machineServiceAmount) {
        this.machineServiceAmount = machineServiceAmount;
    }

    public UUID getObjectID() {
        return objectID;
    }

    public void setObjectID(UUID objectID) {
        this.objectID = objectID;
    }

    public BigDecimal getNotAcceptedAmount() {
        return notAcceptedAmount;
    }

    public void setNotAcceptedAmount(BigDecimal notAcceptedAmount) {
        this.notAcceptedAmount = notAcceptedAmount;
    }

    public BigDecimal getAcceptedAmount() {
        return acceptedAmount;
    }

    public void setAcceptedAmount(BigDecimal acceptedAmount) {
        this.acceptedAmount = acceptedAmount;
    }

    public String getUncompletedAccount() {
        return uncompletedAccount;
    }

    public void setUncompletedAccount(String uncompletedAccount) {
        this.uncompletedAccount = uncompletedAccount;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }
}


