package vn.softdreams.ebweb.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public class CPAllocationExpenseDetailDTO {
    private UUID id;
    private UUID cPPeriodID;
    private UUID cPAllocationGeneralExpenseID;
    private UUID costSetID;
    private String costSetCode;
    private String costSetName;
    private UUID contractID;
    private String accountNumber;
    private UUID expenseItemID;
    private String expenseItemCode;
    private BigDecimal allocatedRate;
    private BigDecimal allocatedAmount;
    private Integer expenseItemType;

    public CPAllocationExpenseDetailDTO(UUID id, UUID cPPeriodID, UUID cPAllocationGeneralExpenseID, UUID costSetID, String costSetCode, String costSetName, String accountNumber, UUID expenseItemID, String expenseItemCode, BigDecimal allocatedRate, BigDecimal allocatedAmount, Integer expenseItemType) {
        this.id = id;
        this.cPPeriodID = cPPeriodID;
        this.cPAllocationGeneralExpenseID = cPAllocationGeneralExpenseID;
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
        this.accountNumber = accountNumber;
        this.expenseItemID = expenseItemID;
        this.expenseItemCode = expenseItemCode;
        this.allocatedRate = allocatedRate;
        this.allocatedAmount = allocatedAmount;
        this.expenseItemType = expenseItemType;
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

    public UUID getcPAllocationGeneralExpenseID() {
        return cPAllocationGeneralExpenseID;
    }

    public void setcPAllocationGeneralExpenseID(UUID cPAllocationGeneralExpenseID) {
        this.cPAllocationGeneralExpenseID = cPAllocationGeneralExpenseID;
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

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public BigDecimal getAllocatedRate() {
        return allocatedRate;
    }

    public void setAllocatedRate(BigDecimal allocatedRate) {
        this.allocatedRate = allocatedRate;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public Integer getExpenseItemType() {
        return expenseItemType;
    }

    public void setExpenseItemType(Integer expenseItemType) {
        this.expenseItemType = expenseItemType;
    }
}

