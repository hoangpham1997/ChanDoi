package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * A FAAudit.
 */
@Entity
@Table(name = "faaudit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FAAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "nofbook")
    private String noFBook;

    @Column(name = "nombook")
    private String noMBook;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "description")
    private String description;

    @Column(name = "inventorydate")
    private LocalDate inventoryDate;

    @Column(name = "summary")
    private String summary;

    @Column(name = "templateid")
    private UUID templateID;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "faauditid")
    private Set<FAAuditDetails> faAuditDetails = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "faauditid")
    private Set<FAAuditMemberDetail> faAuditMemberDetails = new HashSet<>();


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Set<FAAuditDetails> getFaAuditDetails() {
        return faAuditDetails;
    }

    public void setFaAuditDetails(Set<FAAuditDetails> faAuditDetails) {
        if (this.faAuditDetails == null) {
            this.faAuditDetails = faAuditDetails;
        } else if (this.faAuditDetails != faAuditDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.faAuditDetails.clear();
            if (faAuditDetails != null) {
                this.faAuditDetails.addAll(faAuditDetails);
            }
        }
    }

    public Set<FAAuditMemberDetail> getFaAuditMemberDetails() {
        return faAuditMemberDetails;
    }

    public void setFaAuditMemberDetails(Set<FAAuditMemberDetail> faAuditMemberDetails) {
        if (this.faAuditMemberDetails == null) {
            this.faAuditMemberDetails = faAuditMemberDetails;
        } else if (this.faAuditMemberDetails != faAuditMemberDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.faAuditMemberDetails.clear();
            if (faAuditMemberDetails != null) {
                this.faAuditMemberDetails.addAll(faAuditMemberDetails);
            }
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(LocalDate inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
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
        TIAudit tIAudit = (TIAudit) o;
        if (tIAudit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAudit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }


}
