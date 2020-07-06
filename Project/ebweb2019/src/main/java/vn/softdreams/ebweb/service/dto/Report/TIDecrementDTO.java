package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;


public class TIDecrementDTO {


    private String toolCode;

    private String toolName;

    private String organizationUnitCode;

    private BigDecimal quantity;

    private BigDecimal decrementQuantity;

    private BigDecimal remainingDecrementAmount;


    public TIDecrementDTO() {
    }

    public TIDecrementDTO(String toolCode, String toolName, String organizationUnitCode, BigDecimal quantity, BigDecimal decrementQuantity, BigDecimal remainingDecrementAmount) {
        this.toolCode = toolCode;
        this.toolName = toolName;
        this.organizationUnitCode = organizationUnitCode;
        this.quantity = quantity;
        this.decrementQuantity = decrementQuantity;
        this.remainingDecrementAmount = remainingDecrementAmount;
    }

    public String getOrganizationUnitCode() {
        return organizationUnitCode;
    }

    public void setOrganizationUnitCode(String organizationUnitCode) {
        this.organizationUnitCode = organizationUnitCode;
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

    public BigDecimal getDecrementQuantity() {
        return decrementQuantity;
    }

    public void setDecrementQuantity(BigDecimal decrementQuantity) {
        this.decrementQuantity = decrementQuantity;
    }

    public BigDecimal getRemainingDecrementAmount() {
        return remainingDecrementAmount;
    }

    public void setRemainingDecrementAmount(BigDecimal remainingDecrementAmount) {
        this.remainingDecrementAmount = remainingDecrementAmount;
    }
}
