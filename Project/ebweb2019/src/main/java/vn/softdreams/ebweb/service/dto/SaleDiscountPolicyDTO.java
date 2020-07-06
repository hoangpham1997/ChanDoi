package vn.softdreams.ebweb.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.domain.MaterialGoods;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A SaleDiscountPolicy.
 */
public class SaleDiscountPolicyDTO{

    private UUID id;
    private BigDecimal quantityFrom;
    private BigDecimal quantityTo;
    private Integer discountType;
    private BigDecimal discountResult;
    private UUID materialGoodsID;

    public SaleDiscountPolicyDTO(UUID id, BigDecimal quantityFrom, BigDecimal quantityTo, Integer discountType, BigDecimal discountResult, UUID materialGoodsID) {
        this.id = id;
        this.quantityFrom = quantityFrom;
        this.quantityTo = quantityTo;
        this.discountType = discountType;
        this.discountResult = discountResult;
        this.materialGoodsID = materialGoodsID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getQuantityFrom() {
        return quantityFrom;
    }

    public void setQuantityFrom(BigDecimal quantityFrom) {
        this.quantityFrom = quantityFrom;
    }

    public BigDecimal getQuantityTo() {
        return quantityTo;
    }

    public void setQuantityTo(BigDecimal quantityTo) {
        this.quantityTo = quantityTo;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountResult() {
        return discountResult;
    }

    public void setDiscountResult(BigDecimal discountResult) {
        this.discountResult = discountResult;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }
}
