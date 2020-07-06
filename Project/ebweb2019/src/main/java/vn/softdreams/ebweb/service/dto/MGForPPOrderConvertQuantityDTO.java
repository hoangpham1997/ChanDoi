package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class MGForPPOrderConvertQuantityDTO {

    private String materialGoodsCode;
    private UUID id;
    private UUID repositoryID;
    private String repositoryCode;
    private BigDecimal minimumStock;
    private Integer materialGoodsType;
    private BigDecimal materialGoodsInStock;

    public MGForPPOrderConvertQuantityDTO() {
    }

    public MGForPPOrderConvertQuantityDTO(String materialGoodsCode, UUID id, UUID repositoryID, String repositoryCode, BigDecimal minimumStock, Integer materialGoodsType, BigDecimal materialGoodsInStock) {
        this.materialGoodsCode = materialGoodsCode;
        this.id = id;
        this.repositoryID = repositoryID;
        this.repositoryCode = repositoryCode;
        this.minimumStock = minimumStock;
        this.materialGoodsType = materialGoodsType;
        this.materialGoodsInStock = materialGoodsInStock;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
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

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public BigDecimal getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public Integer getMaterialGoodsType() {
        return materialGoodsType;
    }

    public void setMaterialGoodsType(Integer materialGoodsType) {
        this.materialGoodsType = materialGoodsType;
    }

    public BigDecimal getMaterialGoodsInStock() {
        return materialGoodsInStock;
    }

    public void setMaterialGoodsInStock(BigDecimal materialGoodsInStock) {
        this.materialGoodsInStock = materialGoodsInStock;
    }

}
