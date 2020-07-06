package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A AutoPrinciple.
 */
@Entity
@Table(name = "autoprinciple")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AutoPrinciple implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "accountingtype")
    private Integer accountingType;

    @NotNull
    @Size(max = 512)
    @Column(name = "autoprinciplename", length = 512, nullable = false)
    private String autoPrincipleName;

    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeId;

    @Column(name = "debitaccount")
    private String debitAccount;

    @Column(name = "creditaccount")
    private String creditAccount;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Size(max = 512)
    @Column(name = "numberattach", length = 512)
    private String numberAttach;

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

    public String getAutoPrincipleName() {
        return autoPrincipleName;
    }

    public AutoPrinciple autoPrincipleName(String autoPrincipleName) {
        this.autoPrincipleName = autoPrincipleName;
        return this;
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

    public Integer getAccountingType() {
        return accountingType;
    }

    public void setAccountingType(Integer accountingType) {
        this.accountingType = accountingType;
    }

    public void setAutoPrincipleName(String autoPrincipleName) {
        this.autoPrincipleName = autoPrincipleName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public AutoPrinciple typeId(Integer typeId) {
        this.typeId = typeId;
        return this;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public AutoPrinciple debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public AutoPrinciple creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getDescription() {
        return description;
    }

    public AutoPrinciple description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public AutoPrinciple isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
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
        AutoPrinciple autoPrinciple = (AutoPrinciple) o;
        if (autoPrinciple.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), autoPrinciple.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AutoPrinciple{" +
            "id=" + getId() +
            ", autoPrincipleName='" + getAutoPrincipleName() + "'" +
            ", typeId=" + getTypeId() +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
