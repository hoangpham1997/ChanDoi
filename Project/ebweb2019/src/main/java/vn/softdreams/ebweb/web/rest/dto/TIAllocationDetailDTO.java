package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;

public class TIAllocationDetailDTO {
    private String toolCode;

    private String toolName;

    private BigDecimal allocationAmount;

    private BigDecimal remainingAmount;

    private BigDecimal totalAllocationAmount;

    public TIAllocationDetailDTO() {
    }

    public TIAllocationDetailDTO(String toolCode, String toolName, BigDecimal allocationAmount, BigDecimal remainingAmount, BigDecimal totalAllocationAmount) {
        this.toolCode = toolCode;
        this.toolName = toolName;
        this.allocationAmount = allocationAmount;
        this.remainingAmount = remainingAmount;
        this.totalAllocationAmount = totalAllocationAmount;
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

    public BigDecimal getAllocationAmount() {
        return allocationAmount;
    }

    public void setAllocationAmount(BigDecimal allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getTotalAllocationAmount() {
        return totalAllocationAmount;
    }

    public void setTotalAllocationAmount(BigDecimal totalAllocationAmount) {
        this.totalAllocationAmount = totalAllocationAmount;
    }
}
