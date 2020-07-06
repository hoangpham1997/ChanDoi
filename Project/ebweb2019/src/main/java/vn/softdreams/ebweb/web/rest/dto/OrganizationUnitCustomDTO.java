package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.OrganizationUnit;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrganizationUnitCustomDTO {
    private UUID id;
    private String organizationUnitCode;
    private String organizationUnitName;
    private Integer unitType;
    private Integer accType;
    private UUID parentID;
    private BigDecimal quantity;
    private BigDecimal quantityRest;
    private String toolsCode;
    private String toolsName;
    private UUID toolsID;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrganizationUnitCode() {
        return organizationUnitCode;
    }

    public void setOrganizationUnitCode(String organizationUnitCode) {
        this.organizationUnitCode = organizationUnitCode;
    }

    public String getOrganizationUnitName() {
        return organizationUnitName;
    }

    public void setOrganizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
    }

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public Integer getAccType() {
        return accType;
    }

    public void setAccType(Integer accType) {
        this.accType = accType;
    }

    public OrganizationUnitCustomDTO() {
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantityRest() {
        return quantityRest;
    }

    public void setQuantityRest(BigDecimal quantityRest) {
        this.quantityRest = quantityRest;
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

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public UUID getParentID() {
        return parentID;
    }

    public void setParentID(UUID parentID) {
        this.parentID = parentID;
    }

    public OrganizationUnitCustomDTO(UUID id, String organizationUnitCode, String organizationUnitName, Integer unitType, Integer accType, BigDecimal quantity, BigDecimal quantityRest, String toolsCode, String toolsName, UUID toolsID) {
        this.id = id;
        this.organizationUnitCode = organizationUnitCode;
        this.organizationUnitName = organizationUnitName;
        this.unitType = unitType;
        this.accType = accType;
        this.quantity = quantity;
        this.quantityRest = quantityRest;
        this.toolsCode = toolsCode;
        this.toolsName = toolsName;
        this.toolsID = toolsID;
    }

    public OrganizationUnitCustomDTO(UUID id, String organizationUnitCode, String organizationUnitName, Integer unitType, Integer accType, BigDecimal quantity, BigDecimal quantityRest) {
        this.id = id;
        this.organizationUnitCode = organizationUnitCode;
        this.organizationUnitName = organizationUnitName;
        this.unitType = unitType;
        this.accType = accType;
        this.quantity = quantity;
        this.quantityRest = quantityRest;
    }

    public OrganizationUnitCustomDTO(UUID id, String organizationUnitCode, String organizationUnitName, Integer unitType, Integer accType, UUID parentID) {
        this.id = id;
        this.organizationUnitCode = organizationUnitCode;
        this.organizationUnitName = organizationUnitName;
        this.unitType = unitType;
        this.accType = accType;
        this.parentID = parentID;
    }
    public OrganizationUnitCustomDTO(UUID id, String organizationUnitCode, String organizationUnitName, Integer unitType, Integer accType, BigDecimal quantityRest) {
        this.id = id;
        this.organizationUnitCode = organizationUnitCode;
        this.organizationUnitName = organizationUnitName;
        this.unitType = unitType;
        this.accType = accType;
        this.quantityRest = quantityRest;
    }
}
