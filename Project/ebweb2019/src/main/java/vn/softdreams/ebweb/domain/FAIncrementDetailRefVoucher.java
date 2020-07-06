package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.FAIncrementDetailRefVoucherConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementDetailRefVoucherConvertDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A FAIncrementDetailRefVoucher.
 */
@Entity
@Table(name = "faincrementdetailrefvoucher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings(value = {
    @SqlResultSetMapping(
        name = "faIncrementDetailRefVoucherConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = FAIncrementDetailRefVoucherConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmountOriginal", type = String.class),
                    @ColumnResult(name = "faIncrementID", type = UUID.class),
                    @ColumnResult(name = "refVoucherID", type = UUID.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                }
            )
        })
})
public class FAIncrementDetailRefVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "faincrementid")
    private UUID faIncrementID;

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

    public UUID getFaIncrementID() {
        return faIncrementID;
    }

    public void setFaIncrementID(UUID faIncrementID) {
        this.faIncrementID = faIncrementID;
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

    public FAIncrementDetailRefVoucher orderPriority(Integer orderPriority) {
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
        FAIncrementDetailRefVoucher tIIncrementDetailRefVoucher = (FAIncrementDetailRefVoucher) o;
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
        return "FAIncrementDetailRefVoucher{" +
            "id=" + getId() +
            ", faIncrementID='" + getFaIncrementID() + "'" +
            ", refVoucherID='" + getRefVoucherID() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
