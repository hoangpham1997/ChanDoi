package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class MaterialGoodsDTO {

    private UUID id;
    private String materialGoodsCode;
    private String materialGoodsName;
    private UUID unitID;
    private String unitName;
    private BigDecimal unitPrice;
    private UUID repositoryID;
    private BigDecimal materialGoodsInStock;
    private Integer materialGoodsType;
    private UUID materialGoodsCategoryID;
    private Boolean isFollow;

    public MaterialGoodsDTO(UUID id, String materialGoodsCode, String materialGoodsName, UUID unitID, String unitName, BigDecimal unitPrice, Integer materialGoodsType, Boolean isFollow) {
        this.id = id;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.unitName = unitName;
        this.unitID = unitID;
        this.unitPrice = unitPrice;
        this.materialGoodsType = materialGoodsType;
        this.isFollow = isFollow;
    }

    public MaterialGoodsDTO(UUID id, String materialGoodsCode, String materialGoodsName, UUID repositoryID, BigDecimal materialGoodsInStock) {
        this.id = id;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.repositoryID = repositoryID;
        this.materialGoodsInStock = materialGoodsInStock;
    }

    public MaterialGoodsDTO(UUID id, String materialGoodsCode, String materialGoodsName, UUID unitID, UUID repositoryID, Integer materialGoodsType, UUID materialGoodsCategoryID) {
        this.id = id;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.unitID = unitID;
        this.repositoryID = repositoryID;
        this.materialGoodsType = materialGoodsType;
        this.materialGoodsCategoryID = materialGoodsCategoryID;
    }

    public Integer getMaterialGoodsType() {
        return materialGoodsType;
    }

    public void setMaterialGoodsType(Integer materialGoodsType) {
        this.materialGoodsType = materialGoodsType;
    }

    public UUID getMaterialGoodsCategoryID() {
        return materialGoodsCategoryID;
    }

    public void setMaterialGoodsCategoryID(UUID materialGoodsCategoryID) {
        this.materialGoodsCategoryID = materialGoodsCategoryID;
    }

    public BigDecimal getMaterialGoodsInStock() {
        return materialGoodsInStock;
    }

    public void setMaterialGoodsInStock(BigDecimal materialGoodsInStock) {
        this.materialGoodsInStock = materialGoodsInStock;
    }
    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean isIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Boolean isFollow) {
        this.isFollow = isFollow;
    }
}
