package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.SaleDiscountPolicyDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A SaleDiscountPolicy.
 */
@Entity
@Table(name = "salediscountpolicy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "SaleDiscountPolicyDTO",
        classes = {
            @ConstructorResult(
                targetClass = SaleDiscountPolicyDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "quantityFrom", type = BigDecimal.class),
                    @ColumnResult(name = "quantityTo", type = BigDecimal.class),
                    @ColumnResult(name = "discountType", type = Integer.class),
                    @ColumnResult(name = "discountResult", type = BigDecimal.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                }
            )
        }
    )})

public class SaleDiscountPolicy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "quantityfrom", precision = 10, scale = 2)
    private BigDecimal quantityFrom;

    @Column(name = "quantityto", precision = 10, scale = 2)
    private BigDecimal quantityTo;

    @Column(name = "discounttype")
    private Integer discountType;

    @Column(name = "discountresult", precision = 10, scale = 2)
    private BigDecimal discountResult;

//    @ManyToOne    @JsonIgnoreProperties("")
//    @JoinColumn(name = "materialgoodsid")
//    private MaterialGoods materialGoods;

    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getQuantityFrom() {
        return quantityFrom;
    }

    public SaleDiscountPolicy quantityFrom(BigDecimal quantityFrom) {
        this.quantityFrom = quantityFrom;
        return this;
    }

    public void setQuantityFrom(BigDecimal quantityFrom) {
        this.quantityFrom = quantityFrom;
    }

    public BigDecimal getQuantityTo() {
        return quantityTo;
    }

    public SaleDiscountPolicy quantityTo(BigDecimal quantityTo) {
        this.quantityTo = quantityTo;
        return this;
    }

    public void setQuantityTo(BigDecimal quantityTo) {
        this.quantityTo = quantityTo;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public SaleDiscountPolicy discountType(Integer discountType) {
        this.discountType = discountType;
        return this;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountResult() {
        return discountResult;
    }

    public SaleDiscountPolicy discountResult(BigDecimal discountResult) {
        this.discountResult = discountResult;
        return this;
    }

    public void setDiscountResult(BigDecimal discountResult) {
        this.discountResult = discountResult;
    }
//
//    public MaterialGoods getMaterialGoods() {
//        return materialGoods;
//    }
//
//    public SaleDiscountPolicy materialGoods(MaterialGoods materialGoods) {
//        this.materialGoods = materialGoods;
//        return this;
//    }
//
//    public void setMaterialGoods(MaterialGoods materialGoods) {
//        this.materialGoods = materialGoods;
//    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SaleDiscountPolicy saleDiscountPolicy = (SaleDiscountPolicy) o;
        if (saleDiscountPolicy.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saleDiscountPolicy.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaleDiscountPolicy{" +
            "id=" + getId() +
            ", quantityFrom=" + getQuantityFrom() +
            ", quantityTo=" + getQuantityTo() +
            ", discountType=" + getDiscountType() +
            ", discountResult=" + getDiscountResult() +
            "}";
    }
}
