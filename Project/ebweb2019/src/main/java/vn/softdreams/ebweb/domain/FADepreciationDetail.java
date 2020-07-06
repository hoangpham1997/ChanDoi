package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A FADepreciationDetail.
 */
@Entity
@Table(name = "fadepreciationdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FADepreciationDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "fadepreciationid")
    private UUID faDepreciationID;

    @Column(name = "fixedassetid")
    private UUID fixedAssetID;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "orderpriority")
    private BigDecimal OrderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFaDepreciationID() {
        return faDepreciationID;
    }

    public void setFaDepreciationID(UUID faDepreciationID) {
        this.faDepreciationID = faDepreciationID;
    }

    public UUID getFixedAssetID() {
        return fixedAssetID;
    }

    public void setFixedAssetID(UUID fixedAssetID) {
        this.fixedAssetID = fixedAssetID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPriority() {
        return OrderPriority;
    }

    public void setOrderPriority(BigDecimal orderPriority) {
        OrderPriority = orderPriority;
    }

    public static long getSerialVersionUID() {

        return serialVersionUID;
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
        FADepreciationDetail faDecrementDetails = (FADepreciationDetail) o;
        if (faDecrementDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), faDecrementDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
