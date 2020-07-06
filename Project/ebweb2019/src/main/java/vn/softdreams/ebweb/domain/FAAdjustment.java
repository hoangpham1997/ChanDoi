package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A FAAdjustment.
 */
@Entity
@Table(name = "faadjustment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FAAdjustment implements Serializable {

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

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;

    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

    @Column(name = "reportno")
    private String reportNo;

    @Column(name = "reportdate")
    private LocalDate reportDate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "recorded")
    private Boolean recorded;

    @Column(name = "templateid")
    private UUID templateID;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "faadjustmentid")
    private Set<FAAdjustmentDetails> faAdjustmentDetails = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "faadjustmentid")
    private Set<FAAdjustmentMemberDetail> faAdjustmentMemberDetails = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "faadjustmentid")
    private Set<FAAdjustmentDetailPost> faAdjustmentDetailPosts = new HashSet<>();

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucher> viewVouchers;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public List<RefVoucher> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucher> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public Set<FAAdjustmentDetails> getFaAdjustmentDetails() {
        return faAdjustmentDetails;
    }

    public void setFaAdjustmentDetails(Set<FAAdjustmentDetails> faAdjustmentDetails) {
        if (this.faAdjustmentDetails == null) {
            this.faAdjustmentDetails = faAdjustmentDetails;
        } else if (this.faAdjustmentDetails != faAdjustmentDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.faAdjustmentDetails.clear();
            if (faAdjustmentDetails != null) {
                this.faAdjustmentDetails.addAll(faAdjustmentDetails);
            }
        }
    }

    public Set<FAAdjustmentMemberDetail> getFaAdjustmentMemberDetails() {
        return faAdjustmentMemberDetails;
    }

    public void setFaAdjustmentMemberDetails(Set<FAAdjustmentMemberDetail> faAdjustmentMemberDetails) {
        if (this.faAdjustmentMemberDetails == null) {
            this.faAdjustmentMemberDetails = faAdjustmentMemberDetails;
        } else if (this.faAdjustmentMemberDetails != faAdjustmentMemberDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.faAdjustmentMemberDetails.clear();
            if (faAdjustmentMemberDetails != null) {
                this.faAdjustmentMemberDetails.addAll(faAdjustmentMemberDetails);
            }
        }
    }

    public Set<FAAdjustmentDetailPost> getFaAdjustmentDetailPosts() {
        return faAdjustmentDetailPosts;
    }

    public void setFaAdjustmentDetailPosts(Set<FAAdjustmentDetailPost> faAdjustmentDetailPosts) {
        if (this.faAdjustmentDetailPosts == null) {
            this.faAdjustmentDetailPosts = faAdjustmentDetailPosts;
        } else if (this.faAdjustmentDetailPosts != faAdjustmentDetailPosts) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.faAdjustmentDetailPosts.clear();
            if (faAdjustmentDetailPosts != null) {
                this.faAdjustmentDetailPosts.addAll(faAdjustmentDetailPosts);
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

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
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
        TIAdjustment tIAdjustment = (TIAdjustment) o;
        if (tIAdjustment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAdjustment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
