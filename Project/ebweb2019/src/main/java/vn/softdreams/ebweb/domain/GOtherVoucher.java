package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.GOtherVoucherKcDsDTO;
import vn.softdreams.ebweb.service.dto.GOtherVoucherKcDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.GOtherVoucherExportDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherViewDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A GOtherVoucher.
 */
@Entity
@Table(name = "gothervoucher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "GOtherVoucherExportDTO",
        classes = {
            @ConstructorResult(
                targetClass = GOtherVoucherExportDTO.class,
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
    ),
    @SqlResultSetMapping(
        name = "GOtherVoucherSearchDTO",
        classes = {
            @ConstructorResult(
                targetClass = GOtherVoucherKcDsDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "GOtherVoucherViewDTO",
        classes = {
            @ConstructorResult(
                targetClass = GOtherVoucherViewDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "exchangeRate", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "recorded", type = Boolean.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "GOtherVoucherKcDTO",
        classes = {
            @ConstructorResult(
                targetClass = GOtherVoucherKcDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "exchangeRate", type = BigDecimal.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "recorded", type = Boolean.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "GOtherVoucherDTO",
        classes = {
            @ConstructorResult(
                targetClass = GOtherVoucherDTO.class,
                columns = {
                    @ColumnResult(name = "ID", type = UUID.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "branchID", type = UUID.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "exchangeRate", type = BigDecimal.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "noBook", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "templateID", type = UUID.class),
                }
            )
        }
    )
})
public class GOtherVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "sequenceGenerator")
    private UUID id;

    @Column(name = "companyID")
    private UUID companyID;

    @Column(name = "branchID")
    private UUID branchID;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "currencyid")
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "reason")
    private String reason;

    @Column(name = "nombook")
    private String noMBook;

    @Column(name = "nofbook")
    private String noFBook;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "recorded")
    private Boolean recorded;

    @Column(name = "templateID")
    private UUID templateID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucherDTO> viewVouchers;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "gothervoucherid")
    private List<GOtherVoucherDetails> gOtherVoucherDetails = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "gothervoucherid")
    private Set<GOtherVoucherDetailTax> gOtherVoucherDetailTax = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "gothervoucherid")
    private Set<GOtherVoucherDetailExpenseAllocation> gOtherVoucherDetailExpenseAllocations = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "gothervoucherid")
    private Set<GOtherVoucherDetailExpense> gOtherVoucherDetailExpenses = new HashSet<>();

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public List<GOtherVoucherDetails> getgOtherVoucherDetails() {
        return gOtherVoucherDetails;
    }

    public Set<GOtherVoucherDetailTax> getgOtherVoucherDetailTax() {
        return gOtherVoucherDetailTax;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public void setgOtherVoucherDetails(List<GOtherVoucherDetails> gOtherVoucherDetails) {
        if (this.gOtherVoucherDetails == null) {
            this.gOtherVoucherDetails = gOtherVoucherDetails;
        } else if (this.gOtherVoucherDetails != gOtherVoucherDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.gOtherVoucherDetails.clear();
            if (gOtherVoucherDetails != null) {
                this.gOtherVoucherDetails.addAll(gOtherVoucherDetails);
            }
        }
    }

    public void setgOtherVoucherDetailTax(Set<GOtherVoucherDetailTax> gOtherVoucherDetailTax) {
        this.gOtherVoucherDetailTax = gOtherVoucherDetailTax;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public GOtherVoucher typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public GOtherVoucher exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public GOtherVoucher typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getReason() {
        return reason;
    }

    public GOtherVoucher reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public GOtherVoucher noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public GOtherVoucher noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public LocalDate getDate() {
        return date;
    }

    public GOtherVoucher date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public GOtherVoucher postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public GOtherVoucher totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public GOtherVoucher totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public GOtherVoucher recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
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

    public Set<GOtherVoucherDetailExpenseAllocation> getgOtherVoucherDetailExpenseAllocations() {
        return gOtherVoucherDetailExpenseAllocations;
    }

    public void setgOtherVoucherDetailExpenseAllocations(Set<GOtherVoucherDetailExpenseAllocation> gOtherVoucherDetailExpenseAllocations) {
        this.gOtherVoucherDetailExpenseAllocations.clear();
        this.gOtherVoucherDetailExpenseAllocations.addAll(gOtherVoucherDetailExpenseAllocations);
    }

    public Set<GOtherVoucherDetailExpense> getgOtherVoucherDetailExpenses() {
        return gOtherVoucherDetailExpenses;
    }

    public void setgOtherVoucherDetailExpenses(Set<GOtherVoucherDetailExpense> gOtherVoucherDetailExpenses) {
        this.gOtherVoucherDetailExpenses.clear();
        this.gOtherVoucherDetailExpenses.addAll(gOtherVoucherDetailExpenses);
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
        GOtherVoucher gOtherVoucher = (GOtherVoucher) o;
        if (gOtherVoucher.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gOtherVoucher.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GOtherVoucher{" +
            "id=" + getId() +
            ", typeID=" + getTypeID() +
            ", currencyID='" + getCurrencyID() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            ", typeLedger=" + getTypeLedger() +
            ", reason='" + getReason() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
            ", noFBook='" + getNoFBook() + "'" +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", recorded='" + isRecorded() + "'" +
            "}";
    }
}
