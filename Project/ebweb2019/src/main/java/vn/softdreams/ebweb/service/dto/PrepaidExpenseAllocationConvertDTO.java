package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.PrepaidExpenseAllocation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PrepaidExpenseAllocationConvertDTO {
    private UUID branchID;
    private Integer typeLedger;
    private Integer typeExpense;
    private UUID prepaidExpenseID;
    private String prepaidExpenseCode;
    private String prepaidExpenseName;
    private LocalDate date;
    private BigDecimal amount;
    private BigDecimal allocationAmount;
    private Integer allocationTime;
    private Integer allocatedPeriod;
    private BigDecimal allocatedAmount;
    private String allocationAccount;
    private Boolean isActive;
    private UUID allocationObjectID;
    private Integer allocationObjectType;
    private String allocationObjectName;
    private BigDecimal allocationRate;
    private String costAccount;
    private UUID expenseItemID;
    private BigDecimal remainingAmount;
    private BigDecimal allocationAmountGo;


    public PrepaidExpenseAllocationConvertDTO() {
    }

    public PrepaidExpenseAllocationConvertDTO(UUID branchID, Integer typeLedger, Integer typeExpense, UUID prepaidExpenseID, String prepaidExpenseCode, String prepaidExpenseName, LocalDate date, BigDecimal amount, BigDecimal allocationAmount, Integer allocationTime, Integer allocatedPeriod, BigDecimal allocatedAmount, String allocationAccount, Boolean isActive, UUID allocationObjectID, Integer allocationObjectType, String allocationObjectName, BigDecimal allocationRate, String costAccount, UUID expenseItemID, BigDecimal remainingAmount, BigDecimal allocationAmountGo) {
        this.branchID = branchID;
        this.typeLedger = typeLedger;
        this.typeExpense = typeExpense;
        this.prepaidExpenseID = prepaidExpenseID;
        this.prepaidExpenseCode = prepaidExpenseCode;
        this.prepaidExpenseName = prepaidExpenseName;
        this.date = date;
        this.amount = amount;
        this.allocationAmount = allocationAmount;
        this.allocationTime = allocationTime;
        this.allocatedPeriod = allocatedPeriod;
        this.allocatedAmount = allocatedAmount;
        this.allocationAccount = allocationAccount;
        this.isActive = isActive;
        this.allocationObjectID = allocationObjectID;
        this.allocationObjectType = allocationObjectType;
        this.allocationObjectName = allocationObjectName;
        this.allocationRate = allocationRate;
        this.costAccount = costAccount;
        this.expenseItemID = expenseItemID;
        this.remainingAmount = remainingAmount;
        this.allocationAmountGo = allocationAmountGo;
    }

    public UUID getPrepaidExpenseID() {
        return prepaidExpenseID;
    }

    public void setPrepaidExpenseID(UUID prepaidExpenseID) {
        this.prepaidExpenseID = prepaidExpenseID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public Integer getTypeExpense() {
        return typeExpense;
    }

    public void setTypeExpense(Integer typeExpense) {
        this.typeExpense = typeExpense;
    }

    public String getPrepaidExpenseCode() {
        return prepaidExpenseCode;
    }

    public void setPrepaidExpenseCode(String prepaidExpenseCode) {
        this.prepaidExpenseCode = prepaidExpenseCode;
    }

    public String getPrepaidExpenseName() {
        return prepaidExpenseName;
    }

    public void setPrepaidExpenseName(String prepaidExpenseName) {
        this.prepaidExpenseName = prepaidExpenseName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public Integer getAllocationTime() {
        return allocationTime;
    }

    public void setAllocationTime(Integer allocationTime) {
        this.allocationTime = allocationTime;
    }

    public Integer getAllocatedPeriod() {
        return allocatedPeriod;
    }

    public void setAllocatedPeriod(Integer allocatedPeriod) {
        this.allocatedPeriod = allocatedPeriod;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public String getAllocationAccount() {
        return allocationAccount;
    }

    public void setAllocationAccount(String allocationAccount) {
        this.allocationAccount = allocationAccount;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getAllocationAmountGo() {
        return allocationAmountGo;
    }

    public void setAllocationAmountGo(BigDecimal allocationAmountGo) {
        this.allocationAmountGo = allocationAmountGo;
    }


    public UUID getAllocationObjectID() {
        return allocationObjectID;
    }

    public void setAllocationObjectID(UUID allocationObjectID) {
        this.allocationObjectID = allocationObjectID;
    }

    public Integer getAllocationObjectType() {
        return allocationObjectType;
    }

    public void setAllocationObjectType(Integer allocationObjectType) {
        this.allocationObjectType = allocationObjectType;
    }

    public String getAllocationObjectName() {
        return allocationObjectName;
    }

    public void setAllocationObjectName(String allocationObjectName) {
        this.allocationObjectName = allocationObjectName;
    }

    public BigDecimal getAllocationRate() {
        return allocationRate;
    }

    public void setAllocationRate(BigDecimal allocationRate) {
        this.allocationRate = allocationRate;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }
}
