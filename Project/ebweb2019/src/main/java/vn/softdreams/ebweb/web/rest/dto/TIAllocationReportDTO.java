package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;

public class TIAllocationReportDTO {
    private String toolCode;

    private String toolName;

    private String allocationAmount;

    private String remainingAmount;

    private String totalAllocationAmount;

    public TIAllocationReportDTO() {
    }

    public TIAllocationReportDTO(String toolCode, String toolName, String allocationAmount, String remainingAmount, String totalAllocationAmount) {
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

    public String getAllocationAmount() {
        return allocationAmount;
    }

    public void setAllocationAmount(String allocationAmount) {
        this.allocationAmount = allocationAmount;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getTotalAllocationAmount() {
        return totalAllocationAmount;
    }

    public void setTotalAllocationAmount(String totalAllocationAmount) {
        this.totalAllocationAmount = totalAllocationAmount;
    }
}
