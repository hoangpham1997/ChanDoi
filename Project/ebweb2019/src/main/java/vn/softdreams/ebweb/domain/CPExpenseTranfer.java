package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.CPExpenseTranferExportDTO;
import vn.softdreams.ebweb.service.dto.KetChuyenChiPhiDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A CPExpenseTranfer.
 */
@Entity
@Table(name = "cpexpensetranfer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "KetChuyenChiPhiDTO",
        classes = {
            @ConstructorResult(
                targetClass = KetChuyenChiPhiDTO.class,
                columns = {
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "contractID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "costSetName", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "CPExpenseTranferExportDTO",
        classes = {
            @ConstructorResult(
                targetClass = CPExpenseTranferExportDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noBook", type = String.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "typeIDInWord", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                }
            )
        }
    )
})
public class CPExpenseTranfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "nofbook")
    private String noFBook;

    @Column(name = "nombook")
    private String noMBook;

    @Column(name = "reason")
    private String reason;

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "cpperiodid")
    private UUID cPPeriodID;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "recorded")
    private Boolean recorded;

    @Column(name = "templateid")
    private UUID templateID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Number total;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucherDTO> viewVouchers;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpexpensetranferid")
    private Set<CPExpenseTranferDetails> cPExpenseTranferDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public CPExpenseTranfer typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public CPExpenseTranfer typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public CPExpenseTranfer noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public CPExpenseTranfer noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getReason() {
        return reason;
    }

    public CPExpenseTranfer reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public CPExpenseTranfer postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public CPExpenseTranfer date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public CPExpenseTranfer totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public CPExpenseTranfer totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public CPExpenseTranfer recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }

    public Set<CPExpenseTranferDetails> getcPExpenseTranferDetails() {
        return cPExpenseTranferDetails;
    }

    public void setcPExpenseTranferDetails(Set<CPExpenseTranferDetails> cPExpenseTranferDetails) {
        this.cPExpenseTranferDetails = cPExpenseTranferDetails;
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

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
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

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
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
        CPExpenseTranfer cPExpenseTranfer = (CPExpenseTranfer) o;
        if (cPExpenseTranfer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cPExpenseTranfer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CPExpenseTranfer{" +
            "id=" + getId() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", typeID=" + getTypeID() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", reason='" + getReason() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", date='" + getDate() + "'" +
            ", cPPeriodID='" + getcPPeriodID() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", recorded='" + isRecorded() + "'" +
            ", templateID='" + getTemplateID() + "'" +
            "}";
    }
}
