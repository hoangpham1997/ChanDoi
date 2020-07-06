package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.TIDecrementConvertDTO;
import vn.softdreams.ebweb.service.dto.TITransferAndTIAdjustmentConvertDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A TITransfer.
 */
@Entity
@Table(name = "titransfer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings(value = {
    @SqlResultSetMapping(
        name = "TITransferAndTIAdjustmentConvertDTO2",
        classes = {
            @ConstructorResult(
                targetClass = TITransferAndTIAdjustmentConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "nofBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "noBook", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "diffRemainingAmount", type = BigDecimal.class),
                    @ColumnResult(name = "differAllocationTime", type = BigDecimal.class),
                }
            )
        }),
    @SqlResultSetMapping(
        name = "TITransferAndTIAdjustmentConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = TITransferAndTIAdjustmentConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "nofBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "noBook", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                }
            )
        }),
})
public class TITransfer implements Serializable {

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

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;

    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

    @Size(max = 128)
    @Column(name = "transferor", length = 128)
    private String transferor;

    @Size(max = 128)
    @Column(name = "receiver", length = 128)
    private String receiver;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Column(name = "totalquantity", precision = 10, scale = 2)
    private BigDecimal totalQuantity;

    @Column(name = "recorded")
    private Boolean recorded;

    @Column(name = "templateid")
    private UUID templateID;


    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "titransferid")
    private Set<TITransferDetails> tiTransferDetails = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucher> viewVouchers;


    public Set<TITransferDetails> getTiTransferDetails() {
        return tiTransferDetails;
    }

    public void setTiTransferDetails(Set<TITransferDetails> tiTransferDetails) {
        if (this.tiTransferDetails == null) {
            this.tiTransferDetails = tiTransferDetails;
        } else if (this.tiTransferDetails != tiTransferDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.tiTransferDetails.clear();
            if (tiTransferDetails != null) {
                this.tiTransferDetails.addAll(tiTransferDetails);
            }
        }
    }

    public List<RefVoucher> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucher> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public TITransfer typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public TITransfer date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public TITransfer typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public TITransfer noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public TITransfer noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getTransferor() {
        return transferor;
    }

    public TITransfer transferor(String transferor) {
        this.transferor = transferor;
        return this;
    }

    public void setTransferor(String transferor) {
        this.transferor = transferor;
    }

    public String getReceiver() {
        return receiver;
    }

    public TITransfer receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReason() {
        return reason;
    }

    public TITransfer reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public TITransfer totalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
        return this;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public TITransfer recorded(Boolean recorded) {
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
        TITransfer tITransfer = (TITransfer) o;
        if (tITransfer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tITransfer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TITransfer{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", transferor='" + getTransferor() + "'" +
            ", receiver='" + getReceiver() + "'" +
            ", reason='" + getReason() + "'" +
            ", totalQuantity=" + getTotalQuantity() +
            ", recorded='" + isRecorded() + "'" +
            ", templateID='" + getTemplateID() + "'" +
            "}";
    }
}
