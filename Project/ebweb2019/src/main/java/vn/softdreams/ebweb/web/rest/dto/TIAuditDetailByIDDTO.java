package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class TIAuditDetailByIDDTO {
    private UUID id;

    private UUID tiAuditID;

    private UUID toolsID;

    private String toolsCode;

    private String toolsName;

    private UUID unitID;

    private String unitName;

    private UUID departmentID;

    private String departmentCode;

    private BigDecimal quantityONBook;

    private BigDecimal quantityInventory;

    private BigDecimal diffQuantity;

    private BigDecimal executeQuantity;

    private int recommendation;

    private String note;

    private int orderPriority;

    public TIAuditDetailByIDDTO() {
    }

    public TIAuditDetailByIDDTO(UUID id, UUID tiAuditID, UUID toolsID, String toolsCode, String toolsName, UUID unitID, String unitName, UUID departmentID, String departmentCode, BigDecimal quantityONBook, BigDecimal quantityInventory, BigDecimal diffQuantity, BigDecimal executeQuantity, int recommendation, String note, int orderPriority) {
        this.id = id;
        this.tiAuditID = tiAuditID;
        this.toolsID = toolsID;
        this.toolsCode = toolsCode;
        this.toolsName = toolsName;
        this.unitID = unitID;
        this.unitName = unitName;
        this.departmentID = departmentID;
        this.departmentCode = departmentCode;
        this.quantityONBook = quantityONBook;
        this.quantityInventory = quantityInventory;
        this.diffQuantity = diffQuantity;
        this.executeQuantity = executeQuantity;
        this.recommendation = recommendation;
        this.note = note;
        this.orderPriority = orderPriority;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTiAuditID() {
        return tiAuditID;
    }

    public void setTiAuditID(UUID tiAuditID) {
        this.tiAuditID = tiAuditID;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public String getToolsCode() {
        return toolsCode;
    }

    public void setToolsCode(String toolsCode) {
        this.toolsCode = toolsCode;
    }

    public String getToolsName() {
        return toolsName;
    }

    public void setToolsName(String toolsName) {
        this.toolsName = toolsName;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(int orderPriority) {
        this.orderPriority = orderPriority;
    }
}
