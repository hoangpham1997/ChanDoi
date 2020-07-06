package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.SAOrderDTO;
import vn.softdreams.ebweb.service.dto.ViewRSOutwardDTO;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSearchDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A RSInwardOutward.
 */
@Entity
@Table(name = "rsinwardoutward")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "RSInwardOutwardSearchDTO",
        classes = {
            @ConstructorResult(
                targetClass = RSInwardOutwardSearchDTO.class,
                columns = {
                    @ColumnResult(name = "refid", type = UUID.class),
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "typeName", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "RSOutwardSearchDTO",
        classes = {
            @ConstructorResult(
                targetClass = RSInwardOutwardSearchDTO.class,
                columns = {
                    @ColumnResult(name = "refid", type = UUID.class),
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "typeName", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "isDeliveryVoucher", type = Boolean.class),
                    @ColumnResult(name = "refTypeID", type = Integer.class),
                    @ColumnResult(name = "refInvoiceForm", type = Integer.class),
                    @ColumnResult(name = "refInvoiceNo", type = String.class),
                    @ColumnResult(name = "refIsBill", type = Boolean.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "RSOutwardSearchDTOFind",
        classes = {
            @ConstructorResult(
                targetClass = RSInwardOutwardSearchDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "typeName", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "findReferenceTableID",
        classes = {
            @ConstructorResult(
                targetClass = RSInwardOutwardSearchDTO.class,
                columns = {
                    @ColumnResult(name = "refid", type = UUID.class),
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "RSOutwardDTOPopup",
        classes = {
            @ConstructorResult(
                targetClass = ViewRSOutwardDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "rSInwardOutwardDetailID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "debitaccount", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "contactName", type = String.class),
                    @ColumnResult(name = "employeeID", type = UUID.class)
                }
            )
        }
    )})
public class RSInwardOutward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeID;

    @Column(name = "typename")
    private String typeName;

    @NotNull
    @Column(name = "posteddate", nullable = false)
    private LocalDate postedDate;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;

    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Size(max = 512)
    @Column(name = "accountingobjectname", length = 512)
    private String accountingObjectName;

    @Size(max = 512)
    @Column(name = "accountingobjectcode", length = 512)
    private String accountingObjectCode;


    @Size(max = 512)
    @Column(name = "accountingobjectaddress", length = 512)
    private String accountingObjectAddress;


    @Size(max = 512)
    @Column(name = "contactname", length = 512)
    private String contactName;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Size(max = 3)
    @Column(name = "currencyid", length = 3)
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "isimportpurchase")
    private Boolean isImportPurchase;

    @Column(name = "transportmethodid")
    private UUID transportMethodID;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "templateid")
    private UUID templateID;

    @Column(name = "recorded")
    private Boolean recorded;

    @Column(name = "exported")
    private Boolean exported;

    @Size(max = 512)
    @Column(name = "numberattach", length = 512)
    private String numberAttach;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Integer refInvoiceForm;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String refInvoiceNo;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean refIsBill;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "rsinwardoutwardid", nullable = false)
    private Set<RSInwardOutWardDetails> rsInwardOutwardDetails = new HashSet<>();

    public Set<RSInwardOutWardDetails> getRsInwardOutwardDetails() {
        return rsInwardOutwardDetails;
    }

    public void setRsInwardOutwardDetails(Set<RSInwardOutWardDetails> rsInwardOutwardDetails) {
        this.rsInwardOutwardDetails = rsInwardOutwardDetails;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public Boolean getImportPurchase() {
        return isImportPurchase;
    }

    public void setImportPurchase(Boolean importPurchase) {
        isImportPurchase = importPurchase;
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

    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public Integer getRefInvoiceForm() {
        return refInvoiceForm;
    }

    public void setRefInvoiceForm(Integer refInvoiceForm) {
        this.refInvoiceForm = refInvoiceForm;
    }

    public String getRefInvoiceNo() {
        return refInvoiceNo;
    }

    public void setRefInvoiceNo(String refInvoiceNo) {
        this.refInvoiceNo = refInvoiceNo;
    }

    public Boolean getRefIsBill() {
        return refIsBill;
    }

    public void setRefIsBill(Boolean refIsBill) {
        this.refIsBill = refIsBill;
    }

    @Override
    public String toString() {
        return "RSInwardOutward{" +
            "id=" + id +
            ", companyID=" + companyID +
            ", branchID=" + branchID +
            ", typeID=" + typeID +
            ", postedDate=" + postedDate +
            ", date=" + date +
            ", typeLedger=" + typeLedger +
            ", noFBook='" + noFBook + '\'' +
            ", noMBook='" + noMBook + '\'' +
            ", accountingObjectID=" + accountingObjectID +
            ", accountingObjectName='" + accountingObjectName + '\'' +
            ", accountingObjectAddress='" + accountingObjectAddress + '\'' +
            ", contactName='" + contactName + '\'' +
            ", reason='" + reason + '\'' +
            ", currencyID='" + currencyID + '\'' +
            ", exchangeRate=" + exchangeRate +
            ", employeeID=" + employeeID +
            ", isImportPurchase=" + isImportPurchase +
            ", transportMethodID=" + transportMethodID +
            ", totalAmount=" + totalAmount +
            ", totalAmountOriginal=" + totalAmountOriginal +
            ", templateID=" + templateID +
            ", recorded=" + recorded +
            ", exported=" + exported +
            '}';
    }
}
