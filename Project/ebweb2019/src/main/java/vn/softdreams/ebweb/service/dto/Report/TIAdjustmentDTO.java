package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;

public class TIAdjustmentDTO {

    private String toolCode;

    private String toolName;

    private BigDecimal quantity;

    private BigDecimal remainingAmount;

    private BigDecimal newRemainingAmount;

    private BigDecimal diffRemainingAmount;

    private Integer remainAllocationTimes;

    private Integer newRemainingAllocationTime;

    private Integer differAllocationTime;

    public TIAdjustmentDTO() {
    }

    public TIAdjustmentDTO(String toolCode, String toolName, BigDecimal quantity, BigDecimal remainingAmount,
                           BigDecimal newRemainingAmount, BigDecimal diffRemainingAmount,
                           Integer remainAllocationTimes, Integer newRemainingAllocationTime, Integer differAllocationTime) {
        this.toolCode = toolCode;
        this.toolName = toolName;
        this.quantity = quantity;
        this.remainingAmount = remainingAmount;
        this.newRemainingAmount = newRemainingAmount;
        this.diffRemainingAmount = diffRemainingAmount;
        this.remainAllocationTimes = remainAllocationTimes;
        this.newRemainingAllocationTime = newRemainingAllocationTime;
        this.differAllocationTime = differAllocationTime;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
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
}
