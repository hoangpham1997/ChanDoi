package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A PersonalSalaryTax.
 */
@Entity
@Table(name = "personalsalarytax")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersonalSalaryTax implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(max = 512)
    @Column(name = "personalsalarytaxname", length = 512, nullable = false)
    private String personalSalaryTaxName;

    @Column(name = "personalsalarytaxgrade")
    private Integer personalSalaryTaxGrade;

    @NotNull
    @Column(name = "salarytype", nullable = false)
    private Integer salaryType;

    @Column(name = "taxrate", precision = 10, scale = 2)
    private BigDecimal taxRate;

    @NotNull
    @Column(name = "fromamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal fromAmount;

    @NotNull
    @Column(name = "toamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal toAmount;

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

    public String getPersonalSalaryTaxName() {
        return personalSalaryTaxName;
    }

    public PersonalSalaryTax personalSalaryTaxName(String personalSalaryTaxName) {
        this.personalSalaryTaxName = personalSalaryTaxName;
        return this;
    }

    public void setPersonalSalaryTaxName(String personalSalaryTaxName) {
        this.personalSalaryTaxName = personalSalaryTaxName;
    }

    public Integer getPersonalSalaryTaxGrade() {
        return personalSalaryTaxGrade;
    }

    public PersonalSalaryTax personalSalaryTaxGrade(Integer personalSalaryTaxGrade) {
        this.personalSalaryTaxGrade = personalSalaryTaxGrade;
        return this;
    }

    public void setPersonalSalaryTaxGrade(Integer personalSalaryTaxGrade) {
        this.personalSalaryTaxGrade = personalSalaryTaxGrade;
    }

    public Integer getSalaryType() {
        return salaryType;
    }

    public PersonalSalaryTax salaryType(Integer salaryType) {
        this.salaryType = salaryType;
        return this;
    }

    public void setSalaryType(Integer salaryType) {
        this.salaryType = salaryType;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public PersonalSalaryTax taxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
        return this;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public PersonalSalaryTax fromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
        return this;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public PersonalSalaryTax toAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
        return this;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public PersonalSalaryTax isActive(Boolean isActive) {
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
        PersonalSalaryTax personalSalaryTax = (PersonalSalaryTax) o;
        if (personalSalaryTax.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), personalSalaryTax.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PersonalSalaryTax{" +
            "id=" + getId() +
            ", personalSalaryTaxName='" + getPersonalSalaryTaxName() + "'" +
            ", personalSalaryTaxGrade=" + getPersonalSalaryTaxGrade() +
            ", salaryType=" + getSalaryType() +
            ", taxRate=" + getTaxRate() +
            ", fromAmount=" + getFromAmount() +
            ", toAmount=" + getToAmount() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
