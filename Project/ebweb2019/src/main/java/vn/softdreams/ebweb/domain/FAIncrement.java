package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.FAIncrementConvertDTO;
import vn.softdreams.ebweb.service.dto.TIIncrementConvertDTO;

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
 * A FAIncrement.
 */
@Entity
@Table(name = "faincrement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings(value = {
    @SqlResultSetMapping(
        name = "FAIncrementConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = FAIncrementConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
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
public class FAIncrement implements Serializable {

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

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "templateid")
    private String templateID;

    @Column(name = "recorded")
    private Boolean recorded;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "faincrementid")
    private Set<FAIncrementDetailRefVoucher> faIncrementDetailRefVouchers = new HashSet<>();


    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "faincrementid")
    private Set<FAIncrementDetails> faIncrementDetails = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Set<FAIncrementDetailRefVoucher> getFaIncrementDetailRefVouchers() {
        return faIncrementDetailRefVouchers;
    }

    public void setFaIncrementDetailRefVouchers(Set<FAIncrementDetailRefVoucher> faIncrementDetailRefVouchers) {
        if (this.faIncrementDetailRefVouchers == null) {
            this.faIncrementDetailRefVouchers = faIncrementDetailRefVouchers;
        } else if (this.faIncrementDetailRefVouchers != faIncrementDetailRefVouchers) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.faIncrementDetailRefVouchers.clear();
            if (faIncrementDetailRefVouchers != null) {
                this.faIncrementDetailRefVouchers.addAll(faIncrementDetailRefVouchers);
            }
        }
    }

    public Set<FAIncrementDetails> getFaIncrementDetails() {
        return faIncrementDetails;
    }

    public void setFaIncrementDetails(Set<FAIncrementDetails> faIncrementDetails) {
        if (this.faIncrementDetails == null) {
            this.faIncrementDetails = faIncrementDetails;
        } else if (this.faIncrementDetails != faIncrementDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.faIncrementDetails.clear();
            if (faIncrementDetails != null) {
                this.faIncrementDetails.addAll(faIncrementDetails);
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

    public Boolean getRecorded() {
        return recorded;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public FAIncrement typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public FAIncrement date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public FAIncrement typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public FAIncrement noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public FAIncrement noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getReason() {
        return reason;
    }

    public FAIncrement reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public FAIncrement totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTemplateID() {
        return templateID;
    }

    public FAIncrement templateID(String templateID) {
        this.templateID = templateID;
        return this;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public FAIncrement recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
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
        FAIncrement tIIncrement = (FAIncrement) o;
        if (tIIncrement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIIncrement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FAIncrement{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", reason='" + getReason() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", templateID='" + getTemplateID() + "'" +
            ", recorded='" + isRecorded() + "'" +
            "}";
    }
}
