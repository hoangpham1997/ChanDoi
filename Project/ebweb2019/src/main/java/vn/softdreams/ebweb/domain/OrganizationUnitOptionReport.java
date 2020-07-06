package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A OrganizationUnitOptionReport.
 */
@Entity
@Table(name = "organizationunitoptionreport")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrganizationUnitOptionReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//   , generator = "sequenceGenerator"  @SequenceGenerator(name = "sequenceGenerator")
    private UUID id;

    @Column(name = "organizationunitid")
    private UUID organizationUnitID;

    @Size(max = 50)
    @Column(name = "director", length = 50)
    private String director;

    @Size(max = 50)
    @Column(name = "chiefaccountant", length = 50)
    private String chiefAccountant;

    @Size(max = 50)
    @Column(name = "treasurer", length = 50)
    private String treasurer;

    @Size(max = 50)
    @Column(name = "stocker", length = 50)
    private String stocker;

    @Size(max = 50)
    @Column(name = "reporter", length = 50)
    private String reporter;

    @Column(name = "isdisplaynameinreport")
    private Boolean isDisplayNameInReport;

    @Column(name = "isdisplayaccount")
    private Boolean isDisplayAccount;

    @Column(name = "headersetting")
    private Integer headerSetting;

    @Size(max = 512)
    @Column(name = "taxagentname", length = 512)
    private String taxAgentName;

    @Size(max = 50)
    @Column(name = "taxagenttaxcode", length = 50)
    private String taxAgentTaxCode;

    @Size(max = 50)
    @Column(name = "taxagenttaxemployee", length = 50)
    private String taxAgentTaxEmployee;

    @Size(max = 50)
    @Column(name = "certificatenumber", length = 50)
    private String certificateNumber;

    @Column(name = "governingunitname")
    private String governingUnitName;

    @Column(name = "governingunittaxcode")
    private String governingUnitTaxCode;

    @Column(name = "fax")
    private String fax;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "bankaccountdetailid")
    private UUID bankAccountDetailID;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrganizationUnitID() {
        return organizationUnitID;
    }

    public OrganizationUnitOptionReport organizationUnitID(UUID organizationUnitID) {
        this.organizationUnitID = organizationUnitID;
        return this;
    }

    public void setOrganizationUnitID(UUID organizationUnitID) {
        this.organizationUnitID = organizationUnitID;
    }

    public String getDirector() {
        return director;
    }

    public OrganizationUnitOptionReport director(String director) {
        this.director = director;
        return this;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getChiefAccountant() {
        return chiefAccountant;
    }

    public OrganizationUnitOptionReport chiefAccountant(String chiefAccountant) {
        this.chiefAccountant = chiefAccountant;
        return this;
    }

    public void setChiefAccountant(String chiefAccountant) {
        this.chiefAccountant = chiefAccountant;
    }

    public String getTreasurer() {
        return treasurer;
    }

    public OrganizationUnitOptionReport treasurer(String treasurer) {
        this.treasurer = treasurer;
        return this;
    }

    public void setTreasurer(String treasurer) {
        this.treasurer = treasurer;
    }

    public String getStocker() {
        return stocker;
    }

    public OrganizationUnitOptionReport stocker(String stocker) {
        this.stocker = stocker;
        return this;
    }

    public void setStocker(String stocker) {
        this.stocker = stocker;
    }

    public String getReporter() {
        return reporter;
    }

    public OrganizationUnitOptionReport reporter(String reporter) {
        this.reporter = reporter;
        return this;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public Boolean isIsDisplayNameInReport() {
        return isDisplayNameInReport;
    }

    public OrganizationUnitOptionReport isDisplayNameInReport(Boolean isDisplayNameInReport) {
        this.isDisplayNameInReport = isDisplayNameInReport;
        return this;
    }

    public void setIsDisplayNameInReport(Boolean isDisplayNameInReport) {
        this.isDisplayNameInReport = isDisplayNameInReport;
    }

    public Boolean isIsDisplayAccount() {
        return isDisplayAccount;
    }

    public OrganizationUnitOptionReport isDisplayAccount(Boolean isDisplayAccount) {
        this.isDisplayAccount = isDisplayAccount;
        return this;
    }

    public void setIsDisplayAccount(Boolean isDisplayAccount) {
        this.isDisplayAccount = isDisplayAccount;
    }

    public Integer getHeaderSetting() {
        return headerSetting;
    }

    public OrganizationUnitOptionReport headerSetting(Integer headerSetting) {
        this.headerSetting = headerSetting;
        return this;
    }

    public void setHeaderSetting(Integer headerSetting) {
        this.headerSetting = headerSetting;
    }

    public String getTaxAgentName() {
        return taxAgentName;
    }

    public OrganizationUnitOptionReport taxAgentName(String taxAgentName) {
        this.taxAgentName = taxAgentName;
        return this;
    }

    public void setTaxAgentName(String taxAgentName) {
        this.taxAgentName = taxAgentName;
    }

    public String getTaxAgentTaxCode() {
        return taxAgentTaxCode;
    }

    public OrganizationUnitOptionReport taxAgenttaxCode(String taxAgentTaxCode) {
        this.taxAgentTaxCode = taxAgentTaxCode;
        return this;
    }

    public void setTaxAgenttaxCode(String taxAgentTaxCode) {
        this.taxAgentTaxCode = taxAgentTaxCode;
    }

    public String getTaxAgentTaxEmployee() {
        return taxAgentTaxEmployee;
    }

    public OrganizationUnitOptionReport taxAgentTaxEmployee(String taxAgentTaxEmployee) {
        this.taxAgentTaxEmployee = taxAgentTaxEmployee;
        return this;
    }

    public void setTaxAgentTaxEmployee(String taxAgentTaxEmployee) {
        this.taxAgentTaxEmployee = taxAgentTaxEmployee;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public OrganizationUnitOptionReport certificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
        return this;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public Boolean getDisplayNameInReport() {
        return isDisplayNameInReport;
    }

    public void setDisplayNameInReport(Boolean isDisplayNameInReport) {
        isDisplayNameInReport = isDisplayNameInReport;
    }

    public Boolean getDisplayAccount() {
        return isDisplayAccount;
    }

    public void setDisplayAccount(Boolean isDisplayAccount) {
        isDisplayAccount = isDisplayAccount;
    }

    public void setTaxAgentTaxCode(String taxAgentTaxCode) {
        this.taxAgentTaxCode = taxAgentTaxCode;
    }

    public String getGoverningUnitName() {
        return governingUnitName;
    }

    public void setGoverningUnitName(String governingUnitName) {
        this.governingUnitName = governingUnitName;
    }

    public String getGoverningUnitTaxCode() {
        return governingUnitTaxCode;
    }

    public void setGoverningUnitTaxCode(String governingUnitTaxCode) {
        this.governingUnitTaxCode = governingUnitTaxCode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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
        OrganizationUnitOptionReport organizationUnitOptionReport = (OrganizationUnitOptionReport) o;
        if (organizationUnitOptionReport.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationUnitOptionReport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationUnitOptionReport{" +
            "id=" + getId() +
            ", organizationUnitID='" + getOrganizationUnitID() + "'" +
            ", director='" + getDirector() + "'" +
            ", chiefAccountant='" + getChiefAccountant() + "'" +
            ", treasurer='" + getTreasurer() + "'" +
            ", stocker='" + getStocker() + "'" +
            ", reporter='" + getReporter() + "'" +
            ", isDisplayNameInReport='" + isIsDisplayNameInReport() + "'" +
            ", isDisplayAccount='" + isIsDisplayAccount() + "'" +
            ", headerSetting=" + getHeaderSetting() +
            ", taxAgentName='" + getTaxAgentName() + "'" +
            ", taxAgenttaxCode='" + getTaxAgentTaxCode() + "'" +
            ", taxAgentTaxEmployee='" + getTaxAgentTaxEmployee() + "'" +
            ", certificateNumber='" + getCertificateNumber() + "'" +
            "}";
    }
}
