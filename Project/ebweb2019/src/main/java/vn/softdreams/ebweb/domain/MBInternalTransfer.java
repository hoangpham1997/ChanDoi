package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A MBInternalTransfer.
 */
@Entity
@Table(name = "mbinternaltransfer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MBInternalTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "branchid")
    private String branchID;

    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeID;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "posteddate", nullable = false)
    private LocalDate postedDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @NotNull
    @Size(max = 25)
    @Column(name = "nofbook", length = 25, nullable = false)
    private String noFBook;

    @NotNull
    @Size(max = 25)
    @Column(name = "nombook", length = 25, nullable = false)
    private String noMBook;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Column(name = "employeeid")
    private String employeeID;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "tobranchid")
    private String toBranchID;

    @NotNull
    @Column(name = "recorded", nullable = false)
    private String recorded;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBranchID() {
        return branchID;
    }

    public MBInternalTransfer branchID(String branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public MBInternalTransfer typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public MBInternalTransfer date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public MBInternalTransfer postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public MBInternalTransfer typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public MBInternalTransfer noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public MBInternalTransfer noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getReason() {
        return reason;
    }

    public MBInternalTransfer reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public MBInternalTransfer employeeID(String employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public MBInternalTransfer totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public MBInternalTransfer totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public String getToBranchID() {
        return toBranchID;
    }

    public MBInternalTransfer toBranchID(String toBranchID) {
        this.toBranchID = toBranchID;
        return this;
    }

    public void setToBranchID(String toBranchID) {
        this.toBranchID = toBranchID;
    }

    public String getRecorded() {
        return recorded;
    }

    public MBInternalTransfer recorded(String recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(String recorded) {
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
        MBInternalTransfer mBInternalTransfer = (MBInternalTransfer) o;
        if (mBInternalTransfer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mBInternalTransfer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MBInternalTransfer{" +
            "id=" + getId() +
            ", branchID='" + getBranchID() + "'" +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", reason='" + getReason() + "'" +
            ", employeeID='" + getEmployeeID() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", toBranchID='" + getToBranchID() + "'" +
            ", recorded='" + getRecorded() + "'" +
            "}";
    }
}
