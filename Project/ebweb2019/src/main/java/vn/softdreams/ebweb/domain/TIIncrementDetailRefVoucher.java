package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.TIIncrementConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementDetailRefVoucherConvertDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A TIIncrementDetailRefVoucher.
 */
@Entity
@Table(name = "tiincrementdetailrefvoucher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings(value = {
    @SqlResultSetMapping(
        name = "TIIncrementDetailRefVoucherConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = TIIncrementDetailRefVoucherConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmountOriginal", type = String.class),
                    @ColumnResult(name = "tiIncrementID", type = UUID.class),
                    @ColumnResult(name = "refVoucherID", type = UUID.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                }
            )
        })
})
public class TIIncrementDetailRefVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tiincrementid")
    private UUID tiIncrementID;

    @Column(name = "refvoucherid")
    private UUID refVoucherID;

    @NotNull
    @Column(name = "orderpriority", nullable = false)
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTiIncrementID() {
        return tiIncrementID;
    }

    public void setTiIncrementID(UUID tiIncrementID) {
        this.tiIncrementID = tiIncrementID;
    }

    public UUID getRefVoucherID() {
        return refVoucherID;
    }

    public void setRefVoucherID(UUID refVoucherID) {
        this.refVoucherID = refVoucherID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public TIIncrementDetailRefVoucher orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
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
        TIIncrementDetailRefVoucher tIIncrementDetailRefVoucher = (TIIncrementDetailRefVoucher) o;
        if (tIIncrementDetailRefVoucher.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIIncrementDetailRefVoucher.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TIIncrementDetailRefVoucher{" +
            "id=" + getId() +
            ", tiIncrementID='" + getTiIncrementID() + "'" +
            ", refVoucherID='" + getRefVoucherID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
