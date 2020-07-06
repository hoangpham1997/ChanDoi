package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSearchDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferSearchDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A RSTransfer.
 */
@Entity
@Table(name = "rstransfer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "RSTransferSearchDTO",
        classes = {
            @ConstructorResult(
                targetClass = RSTransferSearchDTO.class,
                columns = {
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "typeName", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "employeeName", type = String.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "findReferenceTableTransferID",
        classes = {
            @ConstructorResult(
                targetClass = RSTransferSearchDTO.class,
                columns = {
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "id", type = UUID.class)
                }
            )
        }
    )
})
public class RSTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    //    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeID;

    //    @NotNull
    @Column(name = "posteddate", nullable = false)
    private LocalDate postedDate;

    //    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

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

    @Column(name = "invoicetypeid")
    private UUID invoiceTypeID;

    @Size(max = 25)
    @Column(name = "invoicetemplate", length = 25)
    private String invoiceTemplate;

    @Column(name = "invoiceform")
    private Integer invoiceForm;

    @Size(max = 25)
    @Column(name = "invoiceSeries", length = 25)
    private String invoiceSeries;

    @Size(max = 25)
    @Column(name = "invoiceno", length = 25)
    private String invoiceNo;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Size(max = 512)
    @Column(name = "accountingobjectname", length = 512)
    private String accountingObjectName;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Size(max = 512)
    @Column(name = "employeename", length = 512)
    private String employeeName;

    @Size(max = 512)
    @Column(name = "mobilizationorderno", length = 512)
    private String mobilizationOrderNo;

    //    @NotNull
    @Column(name = "mobilizationorderdate", nullable = false)
    private LocalDate mobilizationOrderDate;

    @Size(max = 512)
    @Column(name = "mobilizationorderof", length = 512)
    private String mobilizationOrderOf;

    @Size(max = 512)
    @Column(name = "mobilizationorderfor", length = 512)
    private String mobilizationOrderFor;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Column(name = "transportmethodid")
    private UUID transportMethodID;

    //    @NotNull
    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    //    @NotNull
    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "templateid")
    private UUID templateID;

    //    @NotNull
    @Column(name = "recorded")
    private Boolean recorded;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "rstransferid", nullable = false)
    private Set<RSTransferDetail> rsTransferDetails = new HashSet<>();

    public RSTransfer() {
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

    public UUID getInvoiceTypeID() {
        return invoiceTypeID;
    }

    public void setInvoiceTypeID(UUID invoiceTypeID) {
        this.invoiceTypeID = invoiceTypeID;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getMobilizationOrderNo() {
        return mobilizationOrderNo;
    }

    public void setMobilizationOrderNo(String mobilizationOrderNo) {
        this.mobilizationOrderNo = mobilizationOrderNo;
    }

    public LocalDate getMobilizationOrderDate() {
        return mobilizationOrderDate;
    }

    public void setMobilizationOrderDate(LocalDate mobilizationOrderDate) {
        this.mobilizationOrderDate = mobilizationOrderDate;
    }

    public String getMobilizationOrderOf() {
        return mobilizationOrderOf;
    }

    public void setMobilizationOrderOf(String mobilizationOrderOf) {
        this.mobilizationOrderOf = mobilizationOrderOf;
    }

    public String getMobilizationOrderFor() {
        return mobilizationOrderFor;
    }

    public void setMobilizationOrderFor(String mobilizationOrderFor) {
        this.mobilizationOrderFor = mobilizationOrderFor;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getTransportMethodID() {
        return transportMethodID;
    }

    public void setTransportMethodID(UUID transportMethodID) {
        this.transportMethodID = transportMethodID;
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

    public Set<RSTransferDetail> getRsTransferDetails() {
        return rsTransferDetails;
    }

    public void setRsTransferDetails(Set<RSTransferDetail> rsTransferDetails) {
        this.rsTransferDetails = rsTransferDetails;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }
}
