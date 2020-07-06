package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.Report.BangKeSoDuNganHangDTO;
import vn.softdreams.ebweb.service.dto.Report.SoKeToanChiTietQuyTienMatDTO;
import vn.softdreams.ebweb.service.dto.Report.SoQuyTienMatDTO;
import vn.softdreams.ebweb.service.dto.Report.SoTienGuiNganHangDTO;
import vn.softdreams.ebweb.service.dto.TheoDoiMaThongKeTheoKhoanMucChiPhiDTO;
import vn.softdreams.ebweb.service.dto.TheoDoiMaThongKeTheoTaiKhoanDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBDepositExportDTO;
import vn.softdreams.ebweb.web.rest.dto.MBDepositViewDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A MBDeposit.
 */
@Entity
@Table(name = "mbdeposit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "MBDepositExportDTO",
        classes = {
            @ConstructorResult(
                targetClass = MBDepositExportDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noBook", type = String.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "typeIDInWord", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SoQuyTienMatDTO",
        classes = {
            @ConstructorResult(
                targetClass = SoQuyTienMatDTO.class,
                columns = {
                    @ColumnResult(name = "rowNum", type = Integer.class),
                    @ColumnResult(name = "refID", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "receiptRefNo", type = String.class),
                    @ColumnResult(name = "paymentRefNo", type = String.class),
                    @ColumnResult(name = "journalMemo", type = String.class),
                    @ColumnResult(name = "totalReceiptFBCurrencyID", type = BigDecimal.class),
                    @ColumnResult(name = "totalPaymentFBCurrencyID", type = BigDecimal.class),
                    @ColumnResult(name = "closingFBCurrencyID", type = BigDecimal.class),
                    @ColumnResult(name = "note", type = String.class),
                    @ColumnResult(name = "cAType", type = Integer.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                }
            )
        }
    ), @SqlResultSetMapping(
    name = "BangKeSoDuNganHangDTO",
    classes = {
        @ConstructorResult(
            targetClass = BangKeSoDuNganHangDTO.class,
            columns = {
                @ColumnResult(name = "rowNum", type = Integer.class),
                @ColumnResult(name = "bankAccountDetailID", type = UUID.class),
                @ColumnResult(name = "taiKhoanNganHang", type = String.class),
                @ColumnResult(name = "tenNganHang", type = String.class),
                @ColumnResult(name = "chiNhanh", type = String.class),
                @ColumnResult(name = "soDuDauKy", type = BigDecimal.class),
                @ColumnResult(name = "phatSinhNo", type = BigDecimal.class),
                @ColumnResult(name = "phatSinhCo", type = BigDecimal.class),
                @ColumnResult(name = "soDuCuoiKy", type = BigDecimal.class),
            }
        )
    }
), @SqlResultSetMapping(
    name = "SoKeToanChiTietQuyTienMatDTO",
    classes = {
        @ConstructorResult(
            targetClass = SoKeToanChiTietQuyTienMatDTO.class,
            columns = {
                @ColumnResult(name = "date", type = LocalDate.class),
                @ColumnResult(name = "refID", type = UUID.class),
                @ColumnResult(name = "postedDate", type = LocalDate.class),
                @ColumnResult(name = "receiptRefNo", type = String.class),
                @ColumnResult(name = "paymentRefNo", type = String.class),
                @ColumnResult(name = "journalMemo", type = String.class),
                @ColumnResult(name = "account", type = String.class),
                @ColumnResult(name = "accountCorresponding", type = String.class),
                @ColumnResult(name = "phatSinhNo", type = BigDecimal.class),
                @ColumnResult(name = "phatSinhCo", type = BigDecimal.class),
                @ColumnResult(name = "soTon", type = BigDecimal.class),
                @ColumnResult(name = "note", type = String.class),
                @ColumnResult(name = "positionOrder", type = Integer.class),
                @ColumnResult(name = "typeID", type = Integer.class),
            }
        )
    }
), @SqlResultSetMapping(
    name = "TheoDoiMaThongKeTheoTaiKhoanDTO",
    classes = {
        @ConstructorResult(
            targetClass = TheoDoiMaThongKeTheoTaiKhoanDTO.class,
            columns = {
                @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                @ColumnResult(name = "statisticsCode", type = String.class),
                @ColumnResult(name = "statisticsCodeName", type = String.class),
                @ColumnResult(name = "ngayChungTu", type = LocalDate.class),
                @ColumnResult(name = "ngayHachToan", type = LocalDate.class),
                @ColumnResult(name = "soChungTu", type = String.class),
                @ColumnResult(name = "dienGiai", type = String.class),
                @ColumnResult(name = "tk", type = String.class),
                @ColumnResult(name = "tkDoiUng", type = String.class),
                @ColumnResult(name = "soTienNo", type = BigDecimal.class),
                @ColumnResult(name = "soTienCo", type = BigDecimal.class),
                @ColumnResult(name = "orderPriority", type = Integer.class),
                @ColumnResult(name = "refID", type = UUID.class),
                @ColumnResult(name = "refType", type = Integer.class),
                @ColumnResult(name = "tongNo", type = BigDecimal.class),
                @ColumnResult(name = "tongCo", type = BigDecimal.class),
            }
        )
    }
), @SqlResultSetMapping(
    name = "TheoDoiMaThongKeTheoKhoanMucChiPhiDTO",
    classes = {
        @ConstructorResult(
            targetClass = TheoDoiMaThongKeTheoKhoanMucChiPhiDTO.class,
            columns = {
                @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                @ColumnResult(name = "statisticsCode", type = String.class),
                @ColumnResult(name = "statisticsCodeName", type = String.class),
                @ColumnResult(name = "expenseItemID", type = UUID.class),
                @ColumnResult(name = "expenseItemCode", type = String.class),
                @ColumnResult(name = "expenseItemName", type = String.class),
                @ColumnResult(name = "soDauKy", type = BigDecimal.class),
                @ColumnResult(name = "soPhatSinh", type = BigDecimal.class),
                @ColumnResult(name = "luyKeCuoiKy", type = BigDecimal.class),
                @ColumnResult(name = "tongSoDauKy", type = BigDecimal.class),
                @ColumnResult(name = "tongSoPhatSinh", type = BigDecimal.class),
                @ColumnResult(name = "tongLuyKeCuoiKy", type = BigDecimal.class),
            }
        )
    }
), @SqlResultSetMapping(
    name = "SoTienGuiNganHangDTO",
    classes = {
        @ConstructorResult(
            targetClass = SoTienGuiNganHangDTO.class,
            columns = {
                @ColumnResult(name = "rowNum", type = Integer.class),
                @ColumnResult(name = "bankAccountDetailID", type = UUID.class),
                @ColumnResult(name = "bankAccount", type = String.class),
                @ColumnResult(name = "bankName", type = String.class),
                @ColumnResult(name = "date", type = LocalDate.class),
                @ColumnResult(name = "postedDate", type = LocalDate.class),
                @ColumnResult(name = "no", type = String.class),
                @ColumnResult(name = "journalMemo", type = String.class),
                @ColumnResult(name = "accountCorresponding", type = String.class),
                @ColumnResult(name = "soThu", type = BigDecimal.class),
                @ColumnResult(name = "soChi", type = BigDecimal.class),
                @ColumnResult(name = "soTon", type = BigDecimal.class),
                @ColumnResult(name = "orderPriority", type = Integer.class),
                @ColumnResult(name = "positionOrder", type = Integer.class),
                @ColumnResult(name = "typeID", type = Integer.class),
                @ColumnResult(name = "refID", type = UUID.class),
            }
        )
    }
), @SqlResultSetMapping(
    name = "MBDepositViewDTO",
    classes = {
        @ConstructorResult(
            targetClass = MBDepositViewDTO.class,
            columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "typeID", type = Integer.class),
                @ColumnResult(name = "date", type = LocalDate.class),
                @ColumnResult(name = "postedDate", type = LocalDate.class),
                @ColumnResult(name = "typeLedger", type = Integer.class),
                @ColumnResult(name = "noFBook", type = String.class),
                @ColumnResult(name = "noMBook", type = String.class),
                @ColumnResult(name = "accountingObjectID", type = UUID.class),
                @ColumnResult(name = "accountingObjectName", type = String.class),
                @ColumnResult(name = "accountingObjectAddress", type = String.class),
                @ColumnResult(name = "bankAccountDetailID", type = UUID.class),
                @ColumnResult(name = "bankName", type = String.class),
                @ColumnResult(name = "reason", type = String.class),
                @ColumnResult(name = "currencyID", type = String.class),
                @ColumnResult(name = "exchangeRate", type = BigDecimal.class),
                @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                @ColumnResult(name = "totalVATAmountOriginal", type = BigDecimal.class),
                @ColumnResult(name = "recorded", type = Boolean.class)
            }
        )
    }
)})
public class MBDeposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "companyid")
    private UUID companyID;

    //    @NotNull
    @Column(name = "typeid")
    private Integer typeID;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "posteddate", nullable = false)
    private LocalDate postedDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    //    @NotNull
    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;

    //    @NotNull
    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    //    @NotNull
    @Size(max = 512)
    @Column(name = "accountingobjectname", length = 512)
    private String accountingObjectName;

    @Size(max = 512)
    @Column(name = "accountingobjectaddress", length = 512)
    private String accountingObjectAddress;

    @Column(name = "bankaccountdetailid")
    private UUID bankAccountDetailID;

    @Size(max = 512)
    @Column(name = "bankname", length = 512)
    private String bankName;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @NotNull
    @Size(max = 3)
    @Column(name = "currencyid", length = 3, nullable = false)
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "paymentclauseid")
    private String paymentClauseID;

    @Column(name = "transportmethodid")
    private String transportMethodID;

    @NotNull
    @Column(name = "totalamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "totalamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmountOriginal;

    //    @NotNull
    @Column(name = "totalvatamount", precision = 10, scale = 2)
    private BigDecimal totalVATAmount;

    //    @NotNull
    @Column(name = "totalvatamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalVATAmountOriginal;

    @Column(name = "employeeid")
    private UUID employeeID;


    @Column(name = "accountingobjecttype")
    private Integer accountingObjectType;

    @Column(name = "templateid")
    private String templateID;

    @NotNull
    @Column(name = "recorded", nullable = false)
    private Boolean recorded;

    @NotNull
    @Column(name = "exported")
    private Boolean exported;

    @Column(name = "ismatch")
    private boolean isMatch;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucherDTO> viewVouchers;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID sAInvoiceID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer typeIDSAInvoice;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID rSInwardOutwardID;

    public UUID getsAInvoiceID() {
        return sAInvoiceID;
    }

    public void setsAInvoiceID(UUID sAInvoiceID) {
        this.sAInvoiceID = sAInvoiceID;
    }


    @Column(name = "matchdate")
    private LocalDate matchDate;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mbdepositid")
    private Set<MBDepositDetails> mBDepositDetails = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mbdepositid")
    private Set<MBDepositDetailCustomer> mBDepositDetailCustomers = new HashSet<>();

//    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
//    @JoinColumn(name = "mbdepositid")
//    private Set<MBDepositDetailTax> mBDepositDetailTax = new HashSet<>();
//
//    public Set<MBDepositDetailTax> getmBDepositDetailTax() {
//        return mBDepositDetailTax;
//    }

//    public void setmBDepositDetailTax(Set<MBDepositDetailTax> mBDepositDetailTax) {
//        this.mBDepositDetailTax = mBDepositDetailTax;
//    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public Boolean getExported() {
        return exported;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public void setmBDepositDetails(Set<MBDepositDetails> mBDepositDetails) {
        this.mBDepositDetails = mBDepositDetails;
    }

    public Set<MBDepositDetails> getmBDepositDetails() {
        return mBDepositDetails;
    }

    public MBDeposit() {
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public MBDeposit typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public MBDeposit date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public MBDeposit postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public MBDeposit typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public MBDeposit noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public MBDeposit noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public MBDeposit accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public MBDeposit accountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
        return this;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getBankName() {
        return bankName;
    }

    public MBDeposit bankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getReason() {
        return reason;
    }

    public MBDeposit reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public MBDeposit exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getPaymentClauseID() {
        return paymentClauseID;
    }

    public MBDeposit paymentClauseID(String paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
        return this;
    }

    public void setPaymentClauseID(String paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
    }

    public String getTransportMethodID() {
        return transportMethodID;
    }

    public MBDeposit transportMethodID(String transportMethodID) {
        this.transportMethodID = transportMethodID;
        return this;
    }

    public void setTransportMethodID(String transportMethodID) {
        this.transportMethodID = transportMethodID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public MBDeposit totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public MBDeposit totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public MBDeposit totalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
        return this;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public MBDeposit totalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        return this;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public Integer getAccountingObjectType() {
        return accountingObjectType;
    }

    public MBDeposit accountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
        return this;
    }

    public void setAccountingObjectType(Integer accountingObjectType) {
        this.accountingObjectType = accountingObjectType;
    }

    public String getTemplateID() {
        return templateID;
    }

    public MBDeposit templateID(String templateID) {
        this.templateID = templateID;
        return this;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public MBDeposit recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Boolean isExported() {
        return exported;
    }

    public MBDeposit exported(Boolean exported) {
        this.exported = exported;
        return this;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    public Boolean isIsMatch() {
        return isMatch;
    }

    public MBDeposit isMatch(Boolean isMatch) {
        this.isMatch = isMatch;
        return this;
    }

    public void setIsMatch(Boolean isMatch) {
        this.isMatch = isMatch;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public Set<MBDepositDetailCustomer> getmBDepositDetailCustomers() {
        return mBDepositDetailCustomers;
    }

    public void setmBDepositDetailCustomers(Set<MBDepositDetailCustomer> mBDepositDetailCustomers) {
        this.mBDepositDetailCustomers = mBDepositDetailCustomers;
    }

    public Integer getTypeIDSAInvoice() {
        return typeIDSAInvoice;
    }

    public void setTypeIDSAInvoice(Integer typeIDSAInvoice) {
        this.typeIDSAInvoice = typeIDSAInvoice;
    }

    public UUID getrSInwardOutwardID() {
        return rSInwardOutwardID;
    }

    public void setrSInwardOutwardID(UUID rSInwardOutwardID) {
        this.rSInwardOutwardID = rSInwardOutwardID;
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
        MBDeposit mBDeposit = (MBDeposit) o;
        if (mBDeposit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mBDeposit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MBDeposit{" +
            "id=" + getId() +
            ", branchID='" + getBranchID() + "'" +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noFBook='" + getNoFBook() + "'" +
            ", noMBook='" + getNoMBook() + "'" +
//            ", accountingObjectID='" + getAccountingObjectID() + "'" +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", accountingObjectAddress='" + getAccountingObjectAddress() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", reason='" + getReason() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            /*", sAQuoteID='" + getsAQuoteID() + "'" +
            ", sAOrderID='" + getsAOrderID() + "'" +*/
            ", paymentClauseID='" + getPaymentClauseID() + "'" +
            ", transportMethodID='" + getTransportMethodID() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", totalVATAmount=" + getTotalVATAmount() +
            ", totalVATAmountOriginal=" + getTotalVATAmountOriginal() +
            ", accountingObjectType=" + getAccountingObjectType() +
            ", templateID='" + getTemplateID() + "'" +
            ", recorded='" + isRecorded() + "'" +
            ", exported='" + isExported() + "'" +
            ", ismatch='" + isIsMatch() + "'" +
            "}";
    }
}
