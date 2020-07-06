package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class GOtherVoucherDetailExpenseAllocationViewDTO {
    private UUID id;
    private UUID gOtherVoucherID;
    private BigDecimal allocationAmount;
    private UUID objectID;
    private Integer objectType;
    private BigDecimal allocationRate;
    private BigDecimal amount;
    private String costAccount;
    private UUID expenseItemID;
    private String expenseItemCode;
    private UUID costSetId;
    private String orderPriority;
    private UUID prepaidExpenseID;
    private String prepaidExpenseCode;
    private String prepaidExpenseName;
    private String costSetCode;

    public GOtherVoucherDetailExpenseAllocationViewDTO() {
    }

    public GOtherVoucherDetailExpenseAllocationViewDTO(UUID id, UUID gOtherVoucherID, BigDecimal allocationAmount, UUID objectID, Integer objectType, BigDecimal allocationRate, BigDecimal amount, String costAccount, UUID expenseItemID, String expenseItemCode, UUID costSetId, String orderPriority, UUID prepaidExpenseID, String prepaidExpenseCode, String prepaidExpenseName, String costSetCode) {
        this.id = id;
        this.gOtherVoucherID = gOtherVoucherID;
        this.allocationAmount = allocationAmount;
        this.objectID = objectID;
        this.objectType = objectType;
        this.allocationRate = allocationRate;
        this.amount = amount;
        this.costAccount = costAccount;
        this.expenseItemID = expenseItemID;
        this.expenseItemCode = expenseItemCode;
        this.costSetId = costSetId;
        this.orderPriority = orderPriority;
        this.prepaidExpenseID = prepaidExpenseID;
        this.prepaidExpenseCode = prepaidExpenseCode;
        this.prepaidExpenseName = prepaidExpenseName;
        this.costSetCode = costSetCode;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getgOtherVoucherID() {
        return gOtherVoucherID;
    }

    public void setgOtherVoucherID(UUID gOtherVoucherID) {
        this.gOtherVoucherID = gOtherVoucherID;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public UUID getObjectID() {
        return objectID;
    }

    public void setObjectID(UUID objectID) {
        this.objectID = objectID;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public BigDecimal getAllocationRate() {
        return allocationRate;
    }

    public void setAllocationRate(BigDecimal allocationRate) {
        this.allocationRate = allocationRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public UUID getCostSetId() {
        return costSetId;
    }

    public void setCostSetId(UUID costSetId) {
        this.costSetId = costSetId;
    }

    public String getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(String orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getPrepaidExpenseID() {
        return prepaidExpenseID;
    }

    public void setPrepaidExpenseID(UUID prepaidExpenseID) {
        this.prepaidExpenseID = prepaidExpenseID;
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

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }
}
