package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.TIAllocationConvertDTO;
import vn.softdreams.ebweb.service.dto.TIDecrementConvertDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A TIAllocation.
 */
@Entity
@Table(name = "tiallocation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings(value = {
    @SqlResultSetMapping(
        name = "TIAllocationConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = TIAllocationConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "nofBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "noBook", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "templateID", type = UUID.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                }
            )
        })
})
public class TIAllocation implements Serializable {

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

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "nofbook")
    private String noFBook;

    @Column(name = "nombook")
    private String noMBook;

    @Column(name = "reason")
    private String reason;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer Year;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "recorded")
    private Boolean recorded;

    @Column(name = "templateid")
    private UUID templateID;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "tiallocationid")
    private Set<TIAllocationAllocated> tiAllocationAllocateds = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "tiallocationid")
    private Set<TIAllocationDetails> tiAllocationDetails = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "tiallocationid")
    private Set<TIAllocationPost> tiAllocationPosts = new HashSet<>();

    // biến tạo thêm không liên quan đến luồng
    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Integer statusSave;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Integer getStatusSave() {
        return statusSave;
    }

    public void setStatusSave(Integer statusSave) {
        this.statusSave = statusSave;
    }

    public Set<TIAllocationAllocated> getTiAllocationAllocateds() {
        return tiAllocationAllocateds;
    }

    public void setTiAllocationAllocateds(Set<TIAllocationAllocated> tiAllocationAllocateds) {
        if (this.tiAllocationAllocateds == null) {
            this.tiAllocationAllocateds = tiAllocationAllocateds;
        } else if (this.tiAllocationAllocateds != tiAllocationAllocateds) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.tiAllocationAllocateds.clear();
            if (tiAllocationAllocateds != null) {
                this.tiAllocationAllocateds.addAll(tiAllocationAllocateds);
            }
        }
    }

    public Set<TIAllocationDetails> getTiAllocationDetails() {
        return tiAllocationDetails;
    }

    public void setTiAllocationDetails(Set<TIAllocationDetails> tiAllocationDetails) {
        if (this.tiAllocationDetails == null) {
            this.tiAllocationDetails = tiAllocationDetails;
        } else if (this.tiAllocationDetails != tiAllocationDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.tiAllocationDetails.clear();
            if (tiAllocationDetails != null) {
                this.tiAllocationDetails.addAll(tiAllocationDetails);
            }
        }
    }

    public Set<TIAllocationPost> getTiAllocationPosts() {
        return tiAllocationPosts;
    }

    public void setTiAllocationPosts(Set<TIAllocationPost> tiAllocationPosts) {
        if (this.tiAllocationPosts == null) {
            this.tiAllocationPosts = tiAllocationPosts;
        } else if (this.tiAllocationPosts != tiAllocationPosts) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.tiAllocationPosts.clear();
            if (tiAllocationPosts != null) {
                this.tiAllocationPosts.addAll(tiAllocationPosts);
            }
        }
    }

    public Integer getTypeID() {
        return typeID;
    }

    public TIAllocation typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public TIAllocation date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public TIAllocation postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public TIAllocation typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public TIAllocation noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public TIAllocation noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getReason() {
        return reason;
    }

    public TIAllocation reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getMonth() {
        return month;
    }

    public TIAllocation month(Integer month) {
        this.month = month;
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return Year;
    }

    public TIAllocation Year(Integer Year) {
        this.Year = Year;
        return this;
    }

    public void setYear(Integer Year) {
        this.Year = Year;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public TIAllocation totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public TIAllocation recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
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

    public Boolean getRecorded() {
        return recorded;
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
        TIAllocation tIAllocation = (TIAllocation) o;
        if (tIAllocation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAllocation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TIAllocation{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", reason='" + getReason() + "'" +
            ", month=" + getMonth() +
            ", Year=" + getYear() +
            ", totalAmount=" + getTotalAmount() +
            ", recorded='" + isRecorded() + "'" +
            ", templateID='" + getTemplateID() + "'" +
            "}";
    }
}
