package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A GenCode.
 */
@Entity
@Table(name = "gencode")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GenCode implements Serializable {

//    private static final UUID serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "displayonbook")
    private Integer displayOnBook;

    @NotNull
    @Column(name = "typegroupid", nullable = false)
    private Integer typeGroupID;

    @Size(max = 50)
    @Column(name = "typegroupname", length = 50)
    private String typeGroupName;

    @Size(max = 50)
    @Column(name = "prefix", length = 50)
    private String prefix;

    @Column(name = "currentvalue")
    private Integer currentValue;

    @Size(max = 50)
    @Column(name = "suffix", length = 50)
    private String suffix;

    @Column(name = "length")
    private Integer length;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public GenCode companyID(UUID companyID) {
        this.companyID = companyID;
        return this;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public GenCode branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getDisplayOnBook() {
        return displayOnBook;
    }

    public GenCode displayOnBook(Integer displayOnBook) {
        this.displayOnBook = displayOnBook;
        return this;
    }

    public void setDisplayOnBook(Integer displayOnBook) {
        this.displayOnBook = displayOnBook;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public GenCode typeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
        return this;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }

    public String getTypeGroupName() {
        return typeGroupName;
    }

    public GenCode typeGroupName(String typeGroupName) {
        this.typeGroupName = typeGroupName;
        return this;
    }

    public void setTypeGroupName(String typeGroupName) {
        this.typeGroupName = typeGroupName;
    }

    public String getPrefix() {
        return prefix;
    }

    public GenCode prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public GenCode currentValue(Integer currentValue) {
        this.currentValue = currentValue;
        return this;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public String getSuffix() {
        return suffix;
    }

    public GenCode suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getLength() {
        return length;
    }

    public GenCode length(Integer length) {
        this.length = length;
        return this;
    }

    public void setLength(Integer length) {
        this.length = length;
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
        GenCode genCode = (GenCode) o;
        if (genCode.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), genCode.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GenCode{" +
            "id=" + getId() +
            ", companyID=" + getCompanyID() +
            ", branchID=" + getBranchID() +
            ", displayOnBook=" + getDisplayOnBook() +
            ", typeGroupID=" + getTypeGroupID() +
            ", typeGroupName='" + getTypeGroupName() + "'" +
            ", prefix='" + getPrefix() + "'" +
            ", currentValue=" + getCurrentValue() +
            ", suffix='" + getSuffix() + "'" +
            ", length=" + getLength() +
            "}";
    }
}
