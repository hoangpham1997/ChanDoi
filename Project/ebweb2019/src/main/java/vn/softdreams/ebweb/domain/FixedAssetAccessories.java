package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A FixedAssetAccessories.
 */
@Entity
@Table(name = "fixedassetaccessories")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FixedAssetAccessories implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "fixedassetid")
    private UUID fixedassetID;

    @Size(max = 512)
    @Column(name = "fixedassetaccessoriesname", length = 512)
    private String fixedAssetAccessoriesName;

    @Column(name = "fixedassetaccessoriesquantity")
    private Long fixedAssetAccessoriesQuantity;

    @Column(name = "fixedassetaccessoriesamount", precision = 10, scale = 2)
    private BigDecimal fixedAssetAccessoriesAmount;

//    @NotNull
    @Column(name = "orderpriority")
    private Integer orderPriority;



    @ManyToOne    @JsonIgnoreProperties("")
    @JoinColumn(name="fixedassetaccessoriesunitID")
    private Unit fixedAssetAccessoriesUnitID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFixedAssetAccessoriesName() {
        return fixedAssetAccessoriesName;
    }

    public FixedAssetAccessories fixedAssetAccessoriesName(String fixedAssetAccessoriesName) {
        this.fixedAssetAccessoriesName = fixedAssetAccessoriesName;
        return this;
    }

    public void setFixedAssetAccessoriesName(String fixedAssetAccessoriesName) {
        this.fixedAssetAccessoriesName = fixedAssetAccessoriesName;
    }

    public Long getFixedAssetAccessoriesQuantity() {
        return fixedAssetAccessoriesQuantity;
    }

    public FixedAssetAccessories fixedAssetAccessoriesQuantity(Long fixedAssetAccessoriesQuantity) {
        this.fixedAssetAccessoriesQuantity = fixedAssetAccessoriesQuantity;
        return this;
    }

    public void setFixedAssetAccessoriesQuantity(Long fixedAssetAccessoriesQuantity) {
        this.fixedAssetAccessoriesQuantity = fixedAssetAccessoriesQuantity;
    }

    public BigDecimal getFixedAssetAccessoriesAmount() {
        return fixedAssetAccessoriesAmount;
    }

    public FixedAssetAccessories fixedAssetAccessoriesAmount(BigDecimal fixedAssetAccessoriesAmount) {
        this.fixedAssetAccessoriesAmount = fixedAssetAccessoriesAmount;
        return this;
    }

    public void setFixedAssetAccessoriesAmount(BigDecimal fixedAssetAccessoriesAmount) {
        this.fixedAssetAccessoriesAmount = fixedAssetAccessoriesAmount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public FixedAssetAccessories orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getFixedassetID() {
        return fixedassetID;
    }

    public void setFixedassetID(UUID fixedassetID) {
        this.fixedassetID = fixedassetID;
    }

    public Unit getFixedAssetAccessoriesUnitID() {
        return fixedAssetAccessoriesUnitID;
    }

    public FixedAssetAccessories fixedAssetAccessoriesUnitID(Unit unit) {
        this.fixedAssetAccessoriesUnitID = unit;
        return this;
    }

    public void setFixedAssetAccessoriesUnitID(Unit unit) {
        this.fixedAssetAccessoriesUnitID = unit;
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
        FixedAssetAccessories fixedAssetAccessories = (FixedAssetAccessories) o;
        if (fixedAssetAccessories.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fixedAssetAccessories.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FixedAssetAccessories{" +
            "id=" + getId() +
            ", fixedAssetAccessoriesName='" + getFixedAssetAccessoriesName() + "'" +
            ", fixedAssetAccessoriesQuantity=" + getFixedAssetAccessoriesQuantity() +
            ", fixedAssetAccessoriesAmount=" + getFixedAssetAccessoriesAmount() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
