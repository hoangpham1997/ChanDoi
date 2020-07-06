package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CheckQuantityExistsConvertDTO {
   private UUID materialGoodsID;
   private BigDecimal quantity;

    public CheckQuantityExistsConvertDTO(UUID materialGoodsID, BigDecimal quantity) {
        this.materialGoodsID = materialGoodsID;
        this.quantity = quantity;
    }

    public CheckQuantityExistsConvertDTO() {
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
