package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class TIDecrementDetailsConvertDTO {
    private UUID id;
    private UUID tiDecrementID;
    private UUID toolsID;
    private String toolsCode;
    private String toolsName;
    private String description;
    private UUID departmentID;
    private String departmentCode;
    private BigDecimal quantity;
    private BigDecimal decrementQuantity;
    private BigDecimal amount;
    private BigDecimal remainingDecrementAmount;
    private UUID tiAuditID;
    private Integer orderPriority;



    public TIDecrementDetailsConvertDTO() {
    }

    public TIDecrementDetailsConvertDTO(UUID id, UUID tiDecrementID, UUID toolsID, String toolsCode, String toolsName, String description, UUID departmentID, String departmentCode, BigDecimal quantity, BigDecimal decrementQuantity, BigDecimal amount, BigDecimal remainingDecrementAmount, UUID tiAuditID, Integer orderPriority) {
        this.id = id;
        this.tiDecrementID = tiDecrementID;
        this.toolsID = toolsID;
        this.toolsCode = toolsCode;
        this.toolsName = toolsName;
        this.description = description;
        this.departmentID = departmentID;
        this.departmentCode = departmentCode;
        this.quantity = quantity;
        this.decrementQuantity = decrementQuantity;
        this.amount = amount;
        this.remainingDecrementAmount = remainingDecrementAmount;
        this.tiAuditID = tiAuditID;
        this.orderPriority = orderPriority;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTiDecrementID() {
        return tiDecrementID;
    }

    public void setTiDecrementID(UUID tiDecrementID) {
        this.tiDecrementID = tiDecrementID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRemainingDecrementAmount() {
        return remainingDecrementAmount;
    }

    public void setRemainingDecrementAmount(BigDecimal remainingDecrementAmount) {
        this.remainingDecrementAmount = remainingDecrementAmount;
    }

    public UUID getTiAuditID() {
        return tiAuditID;
    }

    public void setTiAuditID(UUID tiAuditID) {
        this.tiAuditID = tiAuditID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }
}
