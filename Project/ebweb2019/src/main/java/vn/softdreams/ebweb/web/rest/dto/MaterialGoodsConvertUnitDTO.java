package vn.softdreams.ebweb.web.rest.dto;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.UUID;

public class MaterialGoodsConvertUnitDTO {

    private UUID id;
    private Integer orderNumber;
    private BigDecimal convertRate;
    private String formula;
    private String description;
    private UUID materialGoodsID;
    private UUID unitID;
    private BigDecimal fixedSalePrice;
    private BigDecimal salePrice1;
    private BigDecimal salePrice2;
    private BigDecimal salePrice3;

    public MaterialGoodsConvertUnitDTO(UUID id, Integer orderNumber, BigDecimal convertRate, String formula, String description, UUID materialGoodsID, UUID unitID) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.convertRate = convertRate;
        this.formula = formula;
        this.description = description;
        this.materialGoodsID = materialGoodsID;
        this.unitID = unitID;
    }

    public MaterialGoodsConvertUnitDTO(UUID id, Integer orderNumber, BigDecimal convertRate, String formula, String description, UUID materialGoodsID, UUID unitID, BigDecimal fixedSalePrice, BigDecimal salePrice1, BigDecimal salePrice2, BigDecimal salePrice3) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.convertRate = convertRate;
        this.formula = formula;
        this.description = description;
        this.materialGoodsID = materialGoodsID;
        this.unitID = unitID;
        this.fixedSalePrice = fixedSalePrice;
        this.salePrice1 = salePrice1;
        this.salePrice2 = salePrice2;
        this.salePrice3 = salePrice3;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public BigDecimal getFixedSalePrice() {
        return fixedSalePrice;
    }

    public void setFixedSalePrice(BigDecimal fixedSalePrice) {
        this.fixedSalePrice = fixedSalePrice;
    }

    public BigDecimal getSalePrice1() {
        return salePrice1;
    }

    public void setSalePrice1(BigDecimal salePrice1) {
        this.salePrice1 = salePrice1;
    }

    public BigDecimal getSalePrice2() {
        return salePrice2;
    }

    public void setSalePrice2(BigDecimal salePrice2) {
        this.salePrice2 = salePrice2;
    }

    public BigDecimal getSalePrice3() {
        return salePrice3;
    }

    public void setSalePrice3(BigDecimal salePrice3) {
        this.salePrice3 = salePrice3;
    }
}
