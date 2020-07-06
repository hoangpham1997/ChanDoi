package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PhanBoChiPhiTraTruocDTO {
    private String id;
    private String companyID;
    private Integer typeLedger;
    private Boolean typeExpense;
    private String prepaidExpenseCode;
    private String prepaidExpenseName;
    private String date;
    private BigDecimal amount;
    private BigDecimal allocationTime;
    private BigDecimal allocatedPeriod;
    private BigDecimal allocatedAmount;
    private String allocationAccount;
    private Boolean isActive;
    private Boolean isAllocation;
    private BigDecimal allocationAmountAfter;
    private BigDecimal allocatedPeriodAfter;
    private BigDecimal allocatedPeriodRest;
    private BigDecimal accumulated;
    private BigDecimal amountRest;

    private String amountToString;
    private String allocationTimeToString;
    private String allocatedPeriodToString;
    private String allocatedAmountToString;
    private String allocationAccountToString;
    private String allocationAmountAfterToString;
    private String allocatedPeriodAfterToString;
    private String allocatedPeriodRestToString;
    private String accumulatedToString;
    private String amountRestToString;

    public PhanBoChiPhiTraTruocDTO() {
    }

    public PhanBoChiPhiTraTruocDTO(String id, String companyID, Integer typeLedger, Boolean typeExpense, String prepaidExpenseCode, String prepaidExpenseName, String date, BigDecimal amount, BigDecimal allocationTime, BigDecimal allocatedPeriod, BigDecimal allocatedAmount, String allocationAccount, Boolean isActive, Boolean isAllocation, BigDecimal allocationAmountAfter, BigDecimal allocatedPeriodAfter, BigDecimal allocatedPeriodRest, BigDecimal accumulated, BigDecimal amountRest) {
        this.id = id;
        this.companyID = companyID;
        this.typeLedger = typeLedger;
        this.typeExpense = typeExpense;
        this.prepaidExpenseCode = prepaidExpenseCode;
        this.prepaidExpenseName = prepaidExpenseName;
        this.date = date;
        this.amount = amount;
        this.allocationTime = allocationTime;
        this.allocatedPeriod = allocatedPeriod;
        this.allocatedAmount = allocatedAmount;
        this.allocationAccount = allocationAccount;
        this.isActive = isActive;
        this.isAllocation = isAllocation;
        this.allocationAmountAfter = allocationAmountAfter;
        this.allocatedPeriodAfter = allocatedPeriodAfter;
        this.allocatedPeriodRest = allocatedPeriodRest;
        this.accumulated = accumulated;
        this.amountRest = amountRest;
    }

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
    }

    public String getAllocationTimeToString() {
        return allocationTimeToString;
    }

    public void setAllocationTimeToString(String allocationTimeToString) {
        this.allocationTimeToString = allocationTimeToString;
    }

    public String getAllocatedPeriodToString() {
        return allocatedPeriodToString;
    }

    public void setAllocatedPeriodToString(String allocatedPeriodToString) {
        this.allocatedPeriodToString = allocatedPeriodToString;
    }

    public String getAllocatedAmountToString() {
        return allocatedAmountToString;
    }

    public void setAllocatedAmountToString(String allocatedAmountToString) {
        this.allocatedAmountToString = allocatedAmountToString;
    }

    public String getAllocationAccountToString() {
        return allocationAccountToString;
    }

    public void setAllocationAccountToString(String allocationAccountToString) {
        this.allocationAccountToString = allocationAccountToString;
    }

    public String getAllocationAmountAfterToString() {
        return allocationAmountAfterToString;
    }

    public void setAllocationAmountAfterToString(String allocationAmountAfterToString) {
        this.allocationAmountAfterToString = allocationAmountAfterToString;
    }

    public String getAllocatedPeriodAfterToString() {
        return allocatedPeriodAfterToString;
    }

    public void setAllocatedPeriodAfterToString(String allocatedPeriodAfterToString) {
        this.allocatedPeriodAfterToString = allocatedPeriodAfterToString;
    }

    public String getAllocatedPeriodRestToString() {
        return allocatedPeriodRestToString;
    }

    public void setAllocatedPeriodRestToString(String allocatedPeriodRestToString) {
        this.allocatedPeriodRestToString = allocatedPeriodRestToString;
    }

    public String getAccumulatedToString() {
        return accumulatedToString;
    }

    public void setAccumulatedToString(String accumulatedToString) {
        this.accumulatedToString = accumulatedToString;
    }

    public String getAmountRestToString() {
        return amountRestToString;
    }

    public void setAmountRestToString(String amountRestToString) {
        this.amountRestToString = amountRestToString;
    }

    public BigDecimal getAmountRest() {
        return amountRest;
    }

    public void setAmountRest(BigDecimal amountRest) {
        this.amountRest = amountRest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public Boolean getTypeExpenseco() {
        return typeExpense;
    }

    public void setTypeExpense(Boolean typeExpense) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAllocationTime() {
        return allocationTime;
    }

    public void setAllocationTime(BigDecimal allocationTime) {
        this.allocationTime = allocationTime;
    }

    public BigDecimal getAllocatedPeriod() {
        return allocatedPeriod;
    }

    public void setAllocatedPeriod(BigDecimal allocatedPeriod) {
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsAllocation() {
        return isAllocation;
    }

    public void setIsAllocation(Boolean isAllocation) {
        this.isAllocation = isAllocation;
    }

    public BigDecimal getAllocationAmountAfter() {
        return allocationAmountAfter;
    }

    public void setAllocationAmountAfter(BigDecimal allocationAmountAfter) {
        this.allocationAmountAfter = allocationAmountAfter;
    }

    public BigDecimal getAllocatedPeriodAfter() {
        return allocatedPeriodAfter;
    }

    public void setAllocatedPeriodAfter(BigDecimal allocatedPeriodAfter) {
        this.allocatedPeriodAfter = allocatedPeriodAfter;
    }

    public BigDecimal getAllocatedPeriodRest() {
        return allocatedPeriodRest;
    }

    public void setAllocatedPeriodRest(BigDecimal allocatedPeriodRest) {
        this.allocatedPeriodRest = allocatedPeriodRest;
    }

    public BigDecimal getAccumulated() {
        return accumulated;
    }

    public void setAccumulated(BigDecimal accumulated) {
        this.accumulated = accumulated;
    }

}
