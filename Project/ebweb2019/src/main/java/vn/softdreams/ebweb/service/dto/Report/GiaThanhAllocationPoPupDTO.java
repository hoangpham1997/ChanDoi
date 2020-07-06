package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class GiaThanhAllocationPoPupDTO {
    private UUID id;
    private String accountNumber;
    private UUID expenseItemID;
    private String expenseItemCode;
    private BigDecimal totalCost;
    private BigDecimal unallocatedAmount;
    private BigDecimal allocatedRate;
    private BigDecimal allocatedAmount;
    private Integer allocationMethod;
    private UUID refID;
    private UUID refDetailID;
    private Integer expenseItemType;
    private LocalDate postedDate;
    private LocalDate date;
    private String noFBook;
    private String noMBook;
    private String reason;


    public GiaThanhAllocationPoPupDTO() {
    }

    public GiaThanhAllocationPoPupDTO(UUID id, String accountNumber, UUID expenseItemID, String expenseItemCode, BigDecimal totalCost, BigDecimal unallocatedAmount, BigDecimal allocatedRate, BigDecimal allocatedAmount, Integer allocationMethod,
                                      Integer expenseItemType) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.expenseItemID = expenseItemID;
        this.expenseItemCode = expenseItemCode;
        this.totalCost = totalCost;
        this.unallocatedAmount = unallocatedAmount;
        this.allocatedRate = allocatedRate;
        this.allocatedAmount = allocatedAmount;
        this.allocationMethod = allocationMethod;
        this.expenseItemType = expenseItemType;
    }

    public GiaThanhAllocationPoPupDTO(UUID id, String accountNumber, UUID expenseItemID, String expenseItemCode, BigDecimal totalCost, BigDecimal unallocatedAmount, BigDecimal allocatedRate, BigDecimal allocatedAmount, Integer allocationMethod, UUID refID, UUID refDetailID, Integer expenseItemType, LocalDate postedDate, LocalDate date, String noFBook, String noMBook, String reason) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.expenseItemID = expenseItemID;
        this.expenseItemCode = expenseItemCode;
        this.totalCost = totalCost;
        this.unallocatedAmount = unallocatedAmount;
        this.allocatedRate = allocatedRate;
        this.allocatedAmount = allocatedAmount;
        this.allocationMethod = allocationMethod;
        this.refID = refID;
        this.refDetailID = refDetailID;
        this.expenseItemType = expenseItemType;
        this.postedDate = postedDate;
        this.date = date;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.reason = reason;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public void setAllocatedAmount(BigDecimal amountAllocated) {
        this.allocatedAmount = amountAllocated;
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

    public Integer getExpenseItemType() {
        return expenseItemType;
    }

    public void setExpenseItemType(Integer expenseItemType) {
        this.expenseItemType = expenseItemType;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }
}

