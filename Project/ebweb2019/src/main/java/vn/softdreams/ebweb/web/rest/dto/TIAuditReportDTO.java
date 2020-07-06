package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;

public class TIAuditReportDTO {
    private String organizationUnitName;

    private String toolCode;

    private String toolName;

    private String unitName;

    private String organizationUnitToolName;

    private String quantityONBook;

    private String quantityInventory;

    private String diffQuantity;

    private String executeQuantity;

    private String recommendation;

    public TIAuditReportDTO() {
    }

    public TIAuditReportDTO(String organizationUnitName, String toolCode, String toolName, String unitName, String organizationUnitToolName, String quantityONBook, String quantityInventory, String diffQuantity, String executeQuantity, String recommendation) {
        this.organizationUnitName = organizationUnitName;
        this.toolCode = toolCode;
        this.toolName = toolName;
        this.unitName = unitName;
        this.organizationUnitToolName = organizationUnitToolName;
        this.quantityONBook = quantityONBook;
        this.quantityInventory = quantityInventory;
        this.diffQuantity = diffQuantity;
        this.executeQuantity = executeQuantity;
        this.recommendation = recommendation;
    }

    public String getOrganizationUnitName() {
        return organizationUnitName;
    }

    public void setOrganizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
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

    public String getOrganizationUnitToolName() {
        return organizationUnitToolName;
    }

    public void setOrganizationUnitToolName(String organizationUnitToolName) {
        this.organizationUnitToolName = organizationUnitToolName;
    }

    public String getQuantityONBook() {
        return quantityONBook;
    }

    public void setQuantityONBook(String quantityONBook) {
        this.quantityONBook = quantityONBook;
    }

    public String getQuantityInventory() {
        return quantityInventory;
    }

    public void setQuantityInventory(String quantityInventory) {
        this.quantityInventory = quantityInventory;
    }

    public String getDiffQuantity() {
        return diffQuantity;
    }

    public void setDiffQuantity(String diffQuantity) {
        this.diffQuantity = diffQuantity;
    }

    public String getExecuteQuantity() {
        return executeQuantity;
    }

    public void setExecuteQuantity(String executeQuantity) {
        this.executeQuantity = executeQuantity;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
