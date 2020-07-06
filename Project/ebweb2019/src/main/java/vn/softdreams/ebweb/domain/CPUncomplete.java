package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPUncompletesDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A CPUncomplete.
 */
@Entity
@Table(name = "cpuncomplete")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "CPUncompleteDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPUncompletesDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "cPPeriodID", type = UUID.class),
                    @ColumnResult(name = "quantumID", type = UUID.class),
                    @ColumnResult(name = "quantumCode", type = String.class),
                    @ColumnResult(name = "quantumName", type = String.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "uncompleteType", type = Integer.class),
                    @ColumnResult(name = "quantityClosing", type = BigDecimal.class),
                    @ColumnResult(name = "rate", type = BigDecimal.class)
                }
            )
        }
    )
})
public class CPUncomplete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "cpperiodid")
    private UUID cPPeriodID;

    @Column(name = "quantumid")
    private UUID quantumID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "uncompletetype")
    private Integer uncompleteType;

    @Column(name = "quantityclosing")
    private BigDecimal quantityClosing;

    @Column(name = "rate", length = 512)
    private BigDecimal rate;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public CPUncomplete() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public UUID getQuantumID() {
        return quantumID;
    }

    public void setQuantumID(UUID quantumID) {
        this.quantumID = quantumID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public Integer getUncompleteType() {
        return uncompleteType;
    }

    public void setUncompleteType(Integer uncompleteType) {
        this.uncompleteType = uncompleteType;
    }

    public BigDecimal getQuantityClosing() {
        return quantityClosing;
    }

    public void setQuantityClosing(BigDecimal quantityClosing) {
        this.quantityClosing = quantityClosing;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
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
        CPUncomplete cPUncomplete = (CPUncomplete) o;
        if (cPUncomplete.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPUncomplete.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPUncomplete{" +
            "id=" + getId() +
            "}";
    }
}
