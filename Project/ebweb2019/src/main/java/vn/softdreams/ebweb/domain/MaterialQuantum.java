package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDTO;
import vn.softdreams.ebweb.service.dto.MaterialQuantumGeneralDTO;
import vn.softdreams.ebweb.service.dto.ObjectsMaterialQuantumDTO;
import vn.softdreams.ebweb.service.dto.SaReturnRSInwardDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A MaterialQuantum.
 */
@Entity
@Table(name = "materialquantum")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "ObjectsMaterialQuantumDTO",
        classes = {
            @ConstructorResult(
                targetClass = ObjectsMaterialQuantumDTO.class,
                columns = {
                    @ColumnResult(name = "objectID", type = UUID.class),
                    @ColumnResult(name = "objectCode", type = String.class),
                    @ColumnResult(name = "objectName", type = String.class),
                    @ColumnResult(name = "objectTypeString", type = String.class),
                    @ColumnResult(name = "objectType", type = Integer.class),
                    @ColumnResult(name = "isActive", type = Boolean.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "MaterialQuantumGeneralDTO",
        classes = {
            @ConstructorResult(
                targetClass = MaterialQuantumGeneralDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "objectID", type = UUID.class),
                    @ColumnResult(name = "objectCode", type = String.class),
                    @ColumnResult(name = "objectName", type = String.class),
                    @ColumnResult(name = "fromDate", type = LocalDate.class),
                    @ColumnResult(name = "toDate", type = LocalDate.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "MaterialQuantumDTO",
        classes = {
            @ConstructorResult(
                targetClass = MaterialQuantumDTO.class,
                columns = {
                    @ColumnResult(name = "toDate", type = LocalDate.class),
                    @ColumnResult(name = "objectType", type = Integer.class),
                    @ColumnResult(name = "objectID", type = UUID.class),
                    @ColumnResult(name = "fromDate", type = LocalDate.class),
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class)
                }
            )
        }
    )
})
public class MaterialQuantum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "objectid")
    private UUID objectID;

    @Column(name = "objecttype")
    private Integer objectType;

    @Column(name = "fromdate")
    private LocalDate fromDate;

    @Column(name = "todate")
    private LocalDate toDate;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "materialquantumid")
    private Set<MaterialQuantumDetails> materialQuantumDetails = new HashSet<>();

    @Transient
    private Boolean isChange;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getObjectID() {
        return objectID;
    }

    public MaterialQuantum objectID(UUID objectID) {
        this.objectID = objectID;
        return this;
    }

    public void setObjectID(UUID objectID) {
        this.objectID = objectID;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public MaterialQuantum objectType(Integer objectType) {
        this.objectType = objectType;
        return this;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public MaterialQuantum fromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public MaterialQuantum toDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }


    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Set<MaterialQuantumDetails> getMaterialQuantumDetails() {
        return materialQuantumDetails;
    }

    public void setMaterialQuantumDetails(Set<MaterialQuantumDetails> materialQuantumDetails) {
        this.materialQuantumDetails = materialQuantumDetails;
    }

    public Boolean getChange() {
        return isChange;
    }

    public void setChange(Boolean change) {
        isChange = change;
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
        MaterialQuantum materialQuantum = (MaterialQuantum) o;
        if (materialQuantum.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialQuantum.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialQuantum{" +
            "id=" + getId() +
            ", objectID='" + getObjectID() + "'" +
            ", objectType=" + getObjectType() +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            "}";
    }
}
