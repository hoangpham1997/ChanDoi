package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A GoodsServicePurchase.
 */
@Entity
@Table(name = "goodsservicepurchase")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GoodsServicePurchase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @SequenceGenerator(name = "sequenceGenerator")
    private UUID id;

    @Size(max = 25)
    @Column(name = "goodsservicepurchasecode", length = 25)
    private String goodsServicePurchaseCode;

    @Size(max = 512)
    @Column(name = "goodsservicepurchasename", length = 512)
    private String goodsServicePurchaseName;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "issecurity", nullable = false)
    private Boolean isSecurity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getGoodsServicePurchaseCode() {
        return goodsServicePurchaseCode;
    }

    public GoodsServicePurchase goodsServicePurchaseCode(String goodsServicePurchaseCode) {
        this.goodsServicePurchaseCode = goodsServicePurchaseCode;
        return this;
    }

    public void setGoodsServicePurchaseCode(String goodsServicePurchaseCode) {
        this.goodsServicePurchaseCode = goodsServicePurchaseCode;
    }

    public String getGoodsServicePurchaseName() {
        return goodsServicePurchaseName;
    }

    public GoodsServicePurchase goodsServicePurchaseName(String goodsServicePurchaseName) {
        this.goodsServicePurchaseName = goodsServicePurchaseName;
        return this;
    }

    public void setGoodsServicePurchaseName(String goodsServicePurchaseName) {
        this.goodsServicePurchaseName = goodsServicePurchaseName;
    }

    public String getDescription() {
        return description;
    }

    public GoodsServicePurchase description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public GoodsServicePurchase isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsSecurity() {
        return isSecurity;
    }

    public GoodsServicePurchase isSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
        return this;
    }

    public void setIsSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
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
        GoodsServicePurchase goodsServicePurchase = (GoodsServicePurchase) o;
        if (goodsServicePurchase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), goodsServicePurchase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GoodsServicePurchase{" +
            "id=" + getId() +
            ", goodsServicePurchaseCode='" + getGoodsServicePurchaseCode() + "'" +
            ", goodsServicePurchaseName='" + getGoodsServicePurchaseName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", isSecurity='" + isIsSecurity() + "'" +
            "}";
    }
}
