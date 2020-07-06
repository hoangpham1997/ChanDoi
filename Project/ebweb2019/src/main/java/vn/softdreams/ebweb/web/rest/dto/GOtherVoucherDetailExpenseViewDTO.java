package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class GOtherVoucherDetailExpenseViewDTO {
    private UUID id;
    private UUID gOtherVoucherID;
    private UUID prepaidExpenseID;
    private BigDecimal amount;
    private BigDecimal remainingAmount;
    private BigDecimal allocationAmount;
    private Integer orderPriority;
    private String prepaidExpenseCode;
    private String prepaidExpenseName;

    public GOtherVoucherDetailExpenseViewDTO() {
    }

    public GOtherVoucherDetailExpenseViewDTO(UUID id, UUID gotherVoucherID, UUID prepaidExpenseID, BigDecimal amount, BigDecimal remainingAmount, BigDecimal allocationAmount, Integer orderPriority, String prepaidExpenseCode, String prepaidExpenseName) {
        this.id = id;
        this.gOtherVoucherID = gotherVoucherID;
        this.prepaidExpenseID = prepaidExpenseID;
        this.amount = amount;
        this.remainingAmount = remainingAmount;
        this.allocationAmount = allocationAmount;
        this.orderPriority = orderPriority;
        this.prepaidExpenseCode = prepaidExpenseCode;
        this.prepaidExpenseName = prepaidExpenseName;
    }

    public String getPrepaidExpenseName() {
        return prepaidExpenseName;
    }

    public void setPrepaidExpenseName(String prepaidExpenseName) {
        this.prepaidExpenseName = prepaidExpenseName;
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

    public UUID getPrepaidExpenseID() {
        return prepaidExpenseID;
    }

    public void setPrepaidExpenseID(UUID prepaidExpenseID) {
        this.prepaidExpenseID = prepaidExpenseID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getPrepaidExpenseCode() {
        return prepaidExpenseCode;
    }

    public void setPrepaidExpenseCode(String prepaidExpenseCode) {
        this.prepaidExpenseCode = prepaidExpenseCode;
    }
}
