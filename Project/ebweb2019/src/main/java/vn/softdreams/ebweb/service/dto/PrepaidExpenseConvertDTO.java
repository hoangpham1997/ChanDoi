package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.PrepaidExpense;
import vn.softdreams.ebweb.domain.PrepaidExpenseAllocation;
import vn.softdreams.ebweb.domain.PrepaidExpenseVoucher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PrepaidExpenseConvertDTO {
    private UUID prepaidExpenseID;
    private UUID companyID;
    private UUID branchID;
    private Integer typeLedger;
    private Integer typeExpense;
    private String prepaidExpenseCode;
    private String prepaidExpenseName;
    private LocalDate date;
    private BigDecimal amount;
    private Integer allocationTime;
    private Integer allocatedPeriod;
    private BigDecimal allocatedAmount;
    private String allocationAccount;
    private Boolean isActive;
    private BigDecimal remainingAmount;
    private BigDecimal allocationAmount;
    private List<PrepaidExpenseAllocation> prepaidExpenseAllocations;

    public PrepaidExpenseConvertDTO() {
    }


    public PrepaidExpenseConvertDTO(UUID prepaidExpenseID, UUID companyID, UUID branchID, Integer typeLedger, Integer typeExpense, String prepaidExpenseCode, String prepaidExpenseName, LocalDate date, BigDecimal amount, Integer allocationTime, Integer allocatedPeriod, BigDecimal allocatedAmount, String allocationAccount, Boolean isActive, BigDecimal remainingAmount, BigDecimal allocationAmount) {
        this.prepaidExpenseID = prepaidExpenseID;
        this.companyID = companyID;
        this.branchID = branchID;
        this.typeLedger = typeLedger;
        this.typeExpense = typeExpense;
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
        this.remainingAmount = remainingAmount;
    }

    public UUID getPrepaidExpenseID() {
        return prepaidExpenseID;
    }

    public void setPrepaidExpenseID(UUID prepaidExpenseID) {
        this.prepaidExpenseID = prepaidExpenseID;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
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

    public List<PrepaidExpenseAllocation> getPrepaidExpenseAllocations() {
        return prepaidExpenseAllocations;
    }

    public void setPrepaidExpenseAllocations(List<PrepaidExpenseAllocation> prepaidExpenseAllocations) {
        this.prepaidExpenseAllocations = prepaidExpenseAllocations;
    }
}
