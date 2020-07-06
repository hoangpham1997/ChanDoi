package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MaterialQuantumDTO {
    private LocalDate toDate;
    private Integer objectType;
    private UUID objectID;
    private LocalDate fromDate;
    private UUID id;
    private String materialGoodsCode;
    private String materialGoodsName;
    private String costSetCode;
    private String costSetName;

    public MaterialQuantumDTO() {
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public UUID getObjectID() {
        return objectID;
    }

    public void setObjectID(UUID objectID) {
        this.objectID = objectID;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getCostSetName() {
        return costSetName;
    }

    public void setCostSetName(String costSetName) {
        this.costSetName = costSetName;
    }

    public MaterialQuantumDTO(LocalDate toDate, Integer objectType, UUID objectID, LocalDate fromDate, UUID id, String materialGoodsCode, String materialGoodsName, String costSetCode, String costSetName) {
        this.toDate = toDate;
        this.objectType = objectType;
        this.objectID = objectID;
        this.fromDate = fromDate;
        this.id = id;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
    }
}
