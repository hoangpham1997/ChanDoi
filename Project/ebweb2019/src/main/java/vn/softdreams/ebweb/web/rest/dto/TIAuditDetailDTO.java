package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;

public class TIAuditDetailDTO {
    private String toolCode;

    private String toolName;

    private String unitName;

    private String organizationUnitName;

    private BigDecimal quantityONBook;

    private BigDecimal quantityInventory;

    private BigDecimal diffQuantity;

    private BigDecimal executeQuantity;

    private int recommendation;

    public TIAuditDetailDTO() {
    }

    public TIAuditDetailDTO(String toolCode, String toolName, String unitName, String organizationUnitName, BigDecimal quantityONBook, BigDecimal quantityInventory, BigDecimal diffQuantity, BigDecimal executeQuantity, int recommendation) {
        this.toolCode = toolCode;
        this.toolName = toolName;
        this.unitName = unitName;
        this.organizationUnitName = organizationUnitName;
        this.quantityONBook = quantityONBook;
        this.quantityInventory = quantityInventory;
        this.diffQuantity = diffQuantity;
        this.executeQuantity = executeQuantity;
        this.recommendation = recommendation;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getOrganizationUnitName() {
        return organizationUnitName;
    }

    public void setOrganizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
    }

    public BigDecimal getQuantityONBook() {
        return quantityONBook;
    }

    public void setQuantityONBook(BigDecimal quantityONBook) {
        this.quantityONBook = quantityONBook;
    }

    public BigDecimal getQuantityInventory() {
        return quantityInventory;
    }

    public void setQuantityInventory(BigDecimal quantityInventory) {
        this.quantityInventory = quantityInventory;
    }

    public BigDecimal getDiffQuantity() {
        return diffQuantity;
    }

    public void setDiffQuantity(BigDecimal diffQuantity) {
        this.diffQuantity = diffQuantity;
    }

    public BigDecimal getExecuteQuantity() {
        return executeQuantity;
    }

    public void setExecuteQuantity(BigDecimal executeQuantity) {
        this.executeQuantity = executeQuantity;
    }

    public int getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(int recommendation) {
        this.recommendation = recommendation;
    }
}
