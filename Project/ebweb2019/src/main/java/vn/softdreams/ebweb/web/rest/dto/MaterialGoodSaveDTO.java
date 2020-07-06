package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MaterialGoods;

public class MaterialGoodSaveDTO {
    private MaterialGoods materialGoods;

    private int status;

    public MaterialGoodSaveDTO() {
    }

    public MaterialGoodSaveDTO(MaterialGoods materialGoods, int status) {
        this.materialGoods = materialGoods;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MaterialGoods getMaterialGoods() {
        return materialGoods;
    }

    public void getMaterialGoods(MaterialGoods materialGoods) {
        this.materialGoods = materialGoods;
    }

    public void setMaterialGoods(MaterialGoods materialGoods) {
        this.materialGoods = materialGoods;
    }

}
