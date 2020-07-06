package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

public class TheTinhGiaThanhDTO {
    private UUID costSetID;
    private String costSetCode;
    private String costSetName;
    private UUID materialGoodsID;
    private String materialGoodsCode;
    private String materialGoodsName;

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
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

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
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

    public TheTinhGiaThanhDTO() {
    }

    public TheTinhGiaThanhDTO(UUID costSetID, String costSetCode, String costSetName, UUID materialGoodsID, String materialGoodsCode, String materialGoodsName) {
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.costSetName = costSetName;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
    }
}
