package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class UnitConvertRateDTO {
    private UUID id;
    private String unitName;
    private BigDecimal convertRate;
    private String formula;
    private UUID materialGoodsID;
    private String repositoryId;
    private String materialGoodsCode;

    public UnitConvertRateDTO() {
    }

    public UnitConvertRateDTO(UUID id, UUID materialGoodsID, String materialGoodsCode) {
        this.id = id;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
    }

    public UnitConvertRateDTO(String repositoryId, UUID id, UUID materialGoodsID, BigDecimal convertRate, String formula) {
        this.repositoryId = repositoryId;
        this.id = id;
        this.convertRate = convertRate;
        this.formula = formula;
        this.materialGoodsID = materialGoodsID;
    }

    public UnitConvertRateDTO(UUID id, String unitName, BigDecimal convertRate, String formula) {
        this.id = id;
        this.unitName = unitName;
        this.convertRate = convertRate;
        this.formula = formula;
    }

    public UnitConvertRateDTO(UUID id, String unitName, BigDecimal convertRate, String formula, UUID materialGoodsID) {
        this.id = id;
        this.unitName = unitName;
        this.convertRate = convertRate;
        this.formula = formula;
        this.materialGoodsID = materialGoodsID;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }
}
