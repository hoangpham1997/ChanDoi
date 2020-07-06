package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Warranty.
 */
@Entity
@Table(name = "warranty")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Warranty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warrantytime", nullable = false)
    private Integer warrantyTime;

    @Size(max = 255)
    @Column(name = "warrantyname", length = 255, nullable = false)
    private String warrantyName;

    @Column(name = "description")
    private String description;

    @Column(name = "isactive")
    private Boolean isActive;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWarrantyTime() {
        return warrantyTime;
    }

    public Warranty warrantyTime(Integer warrantyTime) {
        this.warrantyTime = warrantyTime;
        return this;
    }

    public void setWarrantyTime(Integer warrantyTime) {
        this.warrantyTime = warrantyTime;
    }

    public String getWarrantyName() {
        return warrantyName;
    }

    public Warranty warrantyName(String warrantyName) {
        this.warrantyName = warrantyName;
        return this;
    }

    public void setWarrantyName(String warrantyName) {
        this.warrantyName = warrantyName;
    }

    public String getDescription() {
        return description;
    }

    public Warranty description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Warranty isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
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
        Warranty warranty = (Warranty) o;
        if (warranty.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), warranty.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Warranty{" +
            "id=" + getId() +
            ", warrantyTime=" + getWarrantyTime() +
            ", warrantyName='" + getWarrantyName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
