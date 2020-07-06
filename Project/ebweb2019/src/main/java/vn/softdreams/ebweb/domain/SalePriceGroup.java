package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A SalePriceGroup.
 */
@Entity
@Table(name = "salepricegroup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SalePriceGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(max = 25)
    @Column(name = "salepicegroupcode", length = 25, nullable = false)
    private String salePiceGroupCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "salepricegroupname", length = 512, nullable = false)
    private String salePriceGroupName;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSalePiceGroupCode() {
        return salePiceGroupCode;
    }

    public SalePriceGroup salePiceGroupCode(String salePiceGroupCode) {
        this.salePiceGroupCode = salePiceGroupCode;
        return this;
    }

    public void setSalePiceGroupCode(String salePiceGroupCode) {
        this.salePiceGroupCode = salePiceGroupCode;
    }

    public String getSalePriceGroupName() {
        return salePriceGroupName;
    }

    public SalePriceGroup salePriceGroupName(String salePriceGroupName) {
        this.salePriceGroupName = salePriceGroupName;
        return this;
    }

    public void setSalePriceGroupName(String salePriceGroupName) {
        this.salePriceGroupName = salePriceGroupName;
    }

    public String getDescription() {
        return description;
    }

    public SalePriceGroup description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public SalePriceGroup isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
        SalePriceGroup salePriceGroup = (SalePriceGroup) o;
        if (salePriceGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salePriceGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SalePriceGroup{" +
            "id=" + getId() +
            ", salePiceGroupCode='" + getSalePiceGroupCode() + "'" +
            ", salePriceGroupName='" + getSalePriceGroupName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
