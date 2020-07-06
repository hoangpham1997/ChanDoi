package vn.softdreams.ebweb.service.dto;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public class CPAllocationExpenseDTO {
    private UUID id;
    private UUID cPPeriodID;
    private String accountNumber;
    private UUID expenseItemID;
    private String expenseItemCode;
    private BigDecimal totalCost;
    private BigDecimal unallocatedAmount;
    private BigDecimal allocatedRate;
    private BigDecimal allocatedAmount;
    private Integer allocationMethod;
    private UUID refDetailID;
    private UUID refID;

    public CPAllocationExpenseDTO(UUID id, UUID cPPeriodID, String accountNumber, UUID expenseItemID, String expenseItemCode, BigDecimal totalCost, BigDecimal unallocatedAmount, BigDecimal allocatedRate, BigDecimal allocatedAmount, Integer allocationMethod, UUID refDetailID, UUID refID) {
        this.id = id;
        this.cPPeriodID = cPPeriodID;
        this.accountNumber = accountNumber;
        this.expenseItemID = expenseItemID;
        this.expenseItemCode = expenseItemCode;
        this.totalCost = totalCost;
        this.unallocatedAmount = unallocatedAmount;
        this.allocatedRate = allocatedRate;
        this.allocatedAmount = allocatedAmount;
        this.allocationMethod = allocationMethod;
        this.refDetailID = refDetailID;
        this.refID = refID;
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

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getUnallocatedAmount() {
        return unallocatedAmount;
    }

    public void setUnallocatedAmount(BigDecimal unallocatedAmount) {
        this.unallocatedAmount = unallocatedAmount;
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

    public Integer getAllocationMethod() {
        return allocationMethod;
    }

    public void setAllocationMethod(Integer allocationMethod) {
        this.allocationMethod = allocationMethod;
    }

    public UUID getRefDetailID() {
        return refDetailID;
    }

    public void setRefDetailID(UUID refDetailID) {
        this.refDetailID = refDetailID;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }
}

