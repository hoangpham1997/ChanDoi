package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.FAIncrementConvertDTO;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A FADepreciation.
 */
@Entity
@Table(name = "fadepreciation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FADepreciation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "sequenceGenerator")
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

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "templateid")
    private UUID templateID;

    @Column(name = "recorded")
    private Boolean recorded;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "fadepreciationid")
    private Set<FADepreciationDetail> faDepreciationDetails = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "fadepreciationid")
    private Set<FADepreciationAllocation> faDepreciationAllocations = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "fadepreciationid")
    private Set<FADepreciationPost> faDepreciationPosts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Set<FADepreciationDetail> getFaDepreciationDetails() {
        return faDepreciationDetails;
    }

    public void setFaDepreciationDetails(Set<FADepreciationDetail> faDepreciationDetails) {
        if (this.faDepreciationDetails == null) {
            this.faDepreciationDetails = faDepreciationDetails;
        } else if (this.faDepreciationDetails != faDepreciationDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.faDepreciationDetails.clear();
            if (faDepreciationDetails != null) {
                this.faDepreciationDetails.addAll(faDepreciationDetails);
            }
        }
    }

    public Set<FADepreciationAllocation> getFaDepreciationAllocations() {
        return faDepreciationAllocations;
    }

    public void setFaDepreciationAllocations(Set<FADepreciationAllocation> faDepreciationAllocations) {
        if (this.faDepreciationAllocations == null) {
            this.faDepreciationAllocations = faDepreciationAllocations;
        } else if (this.faDepreciationAllocations != faDepreciationAllocations) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.faDepreciationAllocations.clear();
            if (faDepreciationAllocations != null) {
                this.faDepreciationAllocations.addAll(faDepreciationAllocations);
            }
        }
    }

    public Set<FADepreciationPost> getFaDepreciationPosts() {
        return faDepreciationPosts;
    }

    public void setFaDepreciationPosts(Set<FADepreciationPost> faDepreciationPosts) {
        if (this.faDepreciationPosts == null) {
            this.faDepreciationPosts = faDepreciationPosts;
        } else if (this.faDepreciationPosts != faDepreciationPosts) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.faDepreciationPosts.clear();
            if (faDepreciationPosts != null) {
                this.faDepreciationPosts.addAll(faDepreciationPosts);
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

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
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
        FADepreciation tIIncrement = (FADepreciation) o;
        if (tIIncrement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIIncrement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }


}
