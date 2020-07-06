package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

/**
 * add by tiepvv
 */
public class SearchVoucherMaterialGoods {
    private String materialGoodsType;
    private UUID unitID;
    private UUID materialGoodsCategoryID;
    private String keySearch;
    private String materialGoodsCode;
    private String materialGoodsName;

    public String getMaterialGoodsType() {
        return materialGoodsType;
    }

    public void setMaterialGoodsType(String materialGoodsType) {
        this.materialGoodsType = materialGoodsType;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public UUID getMaterialGoodsCategoryID() {
        return materialGoodsCategoryID;
    }

    public void setMaterialGoodsCategoryID(UUID materialGoodsCategoryID) {
        this.materialGoodsCategoryID = materialGoodsCategoryID;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
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
}
