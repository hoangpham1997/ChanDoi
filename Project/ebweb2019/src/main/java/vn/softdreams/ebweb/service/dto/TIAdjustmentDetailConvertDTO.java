package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TIAdjustmentDetailConvertDTO {

    public UUID id;
    public UUID tiAdjustmentID;
    public UUID toolsID;
    public String toolsName;
    public String toolsCode;
    public String description;
    public BigDecimal quantity;
    public BigDecimal remainingAmount;
    public BigDecimal newRemainingAmount;
    public BigDecimal diffRemainingAmount;
    public Integer remainAllocationTimes;
    public Integer newRemainingAllocationTime;
    public Integer differAllocationTime;
    public BigDecimal allocatedAmount;
    public Integer orderPriority;

    public TIAdjustmentDetailConvertDTO() {
    }

    public TIAdjustmentDetailConvertDTO(UUID id, UUID tiAdjustmentID, UUID toolsID, String toolsName, String toolsCode, String description, BigDecimal quantity, BigDecimal remainingAmount, BigDecimal newRemainingAmount, BigDecimal diffRemainingAmount, Integer remainAllocationTimes, Integer newRemainingAllocationTime, Integer differAllocationTime, BigDecimal allocatedAmount, Integer orderPriority) {
        this.id = id;
        this.tiAdjustmentID = tiAdjustmentID;
        this.toolsID = toolsID;
        this.toolsName = toolsName;
        this.toolsCode = toolsCode;
        this.description = description;
        this.quantity = quantity;
        this.remainingAmount = remainingAmount;
        this.newRemainingAmount = newRemainingAmount;
        this.diffRemainingAmount = diffRemainingAmount;
        this.remainAllocationTimes = remainAllocationTimes;
        this.newRemainingAllocationTime = newRemainingAllocationTime;
        this.differAllocationTime = differAllocationTime;
        this.allocatedAmount = allocatedAmount;
        this.orderPriority = orderPriority;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTiAdjustmentID() {
        return tiAdjustmentID;
    }

    public void setTiAdjustmentID(UUID tiAdjustmentID) {
        this.tiAdjustmentID = tiAdjustmentID;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public String getToolsName() {
        return toolsName;
    }

    public void setToolsName(String toolsName) {
        this.toolsName = toolsName;
    }

    public String getToolsCode() {
        return toolsCode;
    }

    public void setToolsCode(String toolsCode) {
        this.toolsCode = toolsCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getNewRemainingAmount() {
        return newRemainingAmount;
    }

    public void setNewRemainingAmount(BigDecimal newRemainingAmount) {
        this.newRemainingAmount = newRemainingAmount;
    }

    public BigDecimal getDiffRemainingAmount() {
        return diffRemainingAmount;
    }

    public void setDiffRemainingAmount(BigDecimal diffRemainingAmount) {
        this.diffRemainingAmount = diffRemainingAmount;
    }

    public Integer getRemainAllocationTimes() {
        return remainAllocationTimes;
    }

    public void setRemainAllocationTimes(Integer remainAllocationTimes) {
        this.remainAllocationTimes = remainAllocationTimes;
    }

    public Integer getNewRemainingAllocationTime() {
        return newRemainingAllocationTime;
    }

    public void setNewRemainingAllocationTime(Integer newRemainingAllocationTime) {
        this.newRemainingAllocationTime = newRemainingAllocationTime;
    }

    public Integer getDifferAllocationTime() {
        return differAllocationTime;
    }

    public void setDifferAllocationTime(Integer differAllocationTime) {
        this.differAllocationTime = differAllocationTime;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }
}
