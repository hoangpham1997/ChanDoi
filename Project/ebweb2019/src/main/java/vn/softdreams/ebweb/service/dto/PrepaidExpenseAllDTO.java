package vn.softdreams.ebweb.service.dto;

import io.swagger.models.auth.In;
import vn.softdreams.ebweb.domain.PrepaidExpense;
import vn.softdreams.ebweb.domain.PrepaidExpenseVoucher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PrepaidExpenseAllDTO {
    private UUID ID;
    private UUID companyID;
    private UUID branchID;
    private Integer typeLedger;
    private Integer typeExpense;
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
    private Boolean isAllocation;

    public PrepaidExpenseAllDTO() {
    }

    public PrepaidExpenseAllDTO(UUID ID, UUID companyID, UUID branchID, Integer typeLedger, Integer typeExpense, String prepaidExpenseCode, String prepaidExpenseName, LocalDate date, BigDecimal amount, BigDecimal allocationAmount, Integer allocationTime, Integer allocatedPeriod, BigDecimal allocatedAmount, String allocationAccount, Boolean isActive, Boolean isAllocation) {
        this.ID = ID;
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
        this.isAllocation = isAllocation;
    }

    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Boolean getIsAllocation() {
        return isAllocation;
    }

    public void setIsAllocation(Boolean allocation) {
        isAllocation = allocation;
    }
}
