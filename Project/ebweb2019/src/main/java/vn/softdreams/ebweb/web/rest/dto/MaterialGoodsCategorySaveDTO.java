package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MaterialGoodsCategory;
import vn.softdreams.ebweb.domain.Unit;

public class MaterialGoodsCategorySaveDTO {
    private MaterialGoodsCategory materialGoodsCategory;

    private int status;

    public MaterialGoodsCategorySaveDTO() {
    }

    public MaterialGoodsCategorySaveDTO(MaterialGoodsCategory materialGoodsCategory, int status) {
        this.materialGoodsCategory = materialGoodsCategory;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MaterialGoodsCategory getMaterialGoodsCategory() {
        return materialGoodsCategory;
    }

    public void setMaterialGoodsCategory(MaterialGoodsCategory materialGoodsCategory) {
        this.materialGoodsCategory = materialGoodsCategory;
    }
}
