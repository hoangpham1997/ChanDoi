package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ToolsConvertDTO {
    private UUID id;
    private Integer declareType;
    private String toolsCode;
    private String toolsName;
    private UUID unitID;
    private BigDecimal quantity;
    private BigDecimal quantityRest;
    private BigDecimal unitPrice;
    private String allocationAwaitAccount;

    public ToolsConvertDTO() {
    }

    public ToolsConvertDTO(UUID id, Integer declareType, String toolsCode, String toolsName, UUID unitID, BigDecimal quantity, BigDecimal quantityRest, BigDecimal unitPrice, String allocationAwaitAccount) {
        this.id = id;
        this.declareType = declareType;
        this.toolsCode = toolsCode;
        this.toolsName = toolsName;
        this.unitID = unitID;
        this.quantity = quantity;
        this.quantityRest = quantityRest;
        this.unitPrice = unitPrice;
        this.allocationAwaitAccount = allocationAwaitAccount;
    }

    public ToolsConvertDTO(UUID id, Integer declareType, String toolsCode, String toolsName, UUID unitID, BigDecimal quantity, BigDecimal unitPrice, String allocationAwaitAccount) {
        this.id = id;
        this.declareType = declareType;
        this.toolsCode = toolsCode;
        this.toolsName = toolsName;
        this.unitID = unitID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.allocationAwaitAccount = allocationAwaitAccount;
    }

    public BigDecimal getQuantityRest() {
        return quantityRest;
    }

    public void setQuantityRest(BigDecimal quantityRest) {
        this.quantityRest = quantityRest;
    }

    public String getAllocationAwaitAccount() {
        return allocationAwaitAccount;
    }

    public void setAllocationAwaitAccount(String allocationAwaitAccount) {
        this.allocationAwaitAccount = allocationAwaitAccount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID ID) {
        this.id = id;
    }

    public Integer getDeclareType() {
        return declareType;
    }

    public void setDeclareType(Integer declareType) {
        this.declareType = declareType;
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
}
