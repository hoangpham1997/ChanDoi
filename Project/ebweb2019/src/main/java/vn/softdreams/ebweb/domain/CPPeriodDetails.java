package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPPeriodDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.CPPeriodDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPPeriodDetails.
 */
@Entity
@Table(name = "cpperioddetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPPeriodDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPPeriodDetailDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "cPPeriodID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class),
                    @ColumnResult(name = "costSetType", type = Integer.class)
                }
            )
        }
    )
})
public class CPPeriodDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "cpperiodid")
    private UUID cPPeriodID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "contractid")
    private UUID contractID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public CPPeriodDetails cPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
        return this;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public CPPeriodDetails costSetID(UUID costSetID) {
        this.costSetID = costSetID;
        return this;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getContractID() {
        return contractID;
    }

    public CPPeriodDetails contractID(UUID contractID) {
        this.contractID = contractID;
        return this;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
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
        CPPeriodDetails cPPeriodDetails = (CPPeriodDetails) o;
        if (cPPeriodDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPPeriodDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPPeriodDetails{" +
            "id=" + getId() +
            ", cPPeriodID=" + getcPPeriodID() +
            ", costSetID=" + getCostSetID() +
            ", contractID=" + getContractID() +
            "}";
    }
}
