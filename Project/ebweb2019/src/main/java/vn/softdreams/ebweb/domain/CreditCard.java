package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A CreditCard.
 */
@Entity
@Table(name = "creditcard")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CreditCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @NotNull
    @Size(max = 25)
    @Column(name = "creditcardnumber", length = 25, nullable = false)
    private String creditCardNumber;

    @NotNull
    @Size(max = 50)
    @Column(name = "creditcardtype", length = 50, nullable = false)
    private String creditCardType;

    @Size(max = 512)
    @Column(name = "ownercard", length = 512)
    private String ownerCard;

    @Column(name = "exfrommonth")
    private Integer exFromMonth;

    @Column(name = "exfromyear")
    private Integer exFromYear;

    @Column(name = "extomonth")
    private Integer exToMonth;

    @Column(name = "extoyear")
    private Integer exToYear;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

    @Column(name = "bankidissuecard")
    private UUID bankIDIssueCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("")
    @JoinColumn(name="branchID")
    private OrganizationUnit branchID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public CreditCard creditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
        return this;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public CreditCard creditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
        return this;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getOwnerCard() {
        return ownerCard;
    }

    public CreditCard ownerCard(String ownerCard) {
        this.ownerCard = ownerCard;
        return this;
    }

    public void setOwnerCard(String ownerCard) {
        this.ownerCard = ownerCard;
    }

    public Integer getExFromMonth() {
        return exFromMonth;
    }

    public CreditCard exFromMonth(Integer exFromMonth) {
        this.exFromMonth = exFromMonth;
        return this;
    }

    public void setExFromMonth(Integer exFromMonth) {
        this.exFromMonth = exFromMonth;
    }

    public Integer getExFromYear() {
        return exFromYear;
    }

    public CreditCard exFromYear(Integer exFromYear) {
        this.exFromYear = exFromYear;
        return this;
    }

    public void setExFromYear(Integer exFromYear) {
        this.exFromYear = exFromYear;
    }

    public Integer getExToMonth() {
        return exToMonth;
    }

    public CreditCard exToMonth(Integer exToMonth) {
        this.exToMonth = exToMonth;
        return this;
    }

    public void setExToMonth(Integer exToMonth) {
        this.exToMonth = exToMonth;
    }

    public Integer getExToYear() {
        return exToYear;
    }

    public CreditCard exToYear(Integer exToYear) {
        this.exToYear = exToYear;
        return this;
    }

    public void setExToYear(Integer exToYear) {
        this.exToYear = exToYear;
    }

    public String getDescription() {
        return description;
    }

    public CreditCard description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public CreditCard isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public UUID getBankIDIssueCard() {
        return bankIDIssueCard;
    }

    public void setBankIDIssueCard(UUID bankIDIssueCard) {
        this.bankIDIssueCard = bankIDIssueCard;
    }

    public OrganizationUnit getBranchID() {
        return branchID;
    }

    public CreditCard branchID(OrganizationUnit organizationUnit) {
        this.branchID = organizationUnit;
        return this;
    }

    public void setBranchID(OrganizationUnit organizationUnit) {
        this.branchID = organizationUnit;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
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
        CreditCard creditCard = (CreditCard) o;
        if (creditCard.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), creditCard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CreditCard{" +
            "id=" + getId() +
            ", creditCardNumber='" + getCreditCardNumber() + "'" +
            ", creditCardType='" + getCreditCardType() + "'" +
            ", ownerCard='" + getOwnerCard() + "'" +
            ", exFromMonth=" + getExFromMonth() +
            ", exFromYear=" + getExFromYear() +
            ", exToMonth=" + getExToMonth() +
            ", exToYear=" + getExToYear() +
            ", description='" + getDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
