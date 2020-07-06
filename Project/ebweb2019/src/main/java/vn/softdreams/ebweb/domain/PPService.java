package vn.softdreams.ebweb.domain;

import org.apache.poi.hpsf.Decimal;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Cache;
import org.springframework.cglib.core.Local;
import vn.softdreams.ebweb.service.dto.PPServiceCostVoucherDTO;
import vn.softdreams.ebweb.service.dto.PPServiceDTO;
import vn.softdreams.ebweb.service.dto.ReceiveBillSearchDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCPaymentDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "ppservice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "PPServiceDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPServiceDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "receiptDate", type = String.class),
                    @ColumnResult(name = "postedDate", type = String.class),
                    @ColumnResult(name = "noBook", type = String.class),
                    @ColumnResult(name = "otherNoBook", type = String.class),
                    @ColumnResult(name = "receiptType", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "totalDiscountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalDiscountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "resultAmount", type = BigDecimal.class),
                    @ColumnResult(name = "resultAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "billReceived", type = Boolean.class),
                    @ColumnResult(name = "dueDate", type = String.class),
                    @ColumnResult(name = "companyTaxCode", type = String.class),
                    @ColumnResult(name = "contactName", type = String.class),
                    @ColumnResult(name = "typeId", type = Integer.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "purchaseCosts", type = Boolean.class),
                    @ColumnResult(name = "recorded", type = Boolean.class),
                    @ColumnResult(name = "numberAttach", type = String.class),
                    @ColumnResult(name = "employeeID", type = UUID.class),
                    @ColumnResult(name = "employeeName", type = String.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "exchangeRate", type = BigDecimal.class),
                    @ColumnResult(name = "receiverUserName", type = String.class),
                    @ColumnResult(name = "otherNumberAttach", type = String.class),
                    @ColumnResult(name = "otherReason", type = String.class),
                    @ColumnResult(name = "accountPaymentId", type = UUID.class),
                    @ColumnResult(name = "accountPaymentName", type = String.class),
                    @ColumnResult(name = "accountReceiverId", type = UUID.class),
                    @ColumnResult(name = "accountReceiverName", type = String.class),
                    @ColumnResult(name = "creditCardNumber", type = String.class),
                    @ColumnResult(name = "paymentVoucherID", type = UUID.class),
                    @ColumnResult(name = "receiver", type = String.class),
                    @ColumnResult(name = "identificationNo", type = String.class),
                    @ColumnResult(name = "issueDate", type = String.class),
                    @ColumnResult(name = "issueBy", type = String.class),
                }
            )
        }
    ), @SqlResultSetMapping(
    name = "UpdateDataDTO",
    classes = {
        @ConstructorResult(
            targetClass = UpdateDataDTO.class,
            columns = {
                @ColumnResult(name = "uuid", type = UUID.class),
                @ColumnResult(name = "rowNum", type = Integer.class),
                @ColumnResult(name = "totalRows", type = Integer.class),
            })
    }),
    @SqlResultSetMapping(
        name = "UUIDDTO",
        classes = {
            @ConstructorResult(
                targetClass = UpdateDataDTO.class,
                columns = {
                    @ColumnResult(name = "uuid", type = UUID.class),
                })
        }), @SqlResultSetMapping(
    name = "PPServiceCostVoucherDTO",
        classes = {
                @ConstructorResult(
                        targetClass = PPServiceCostVoucherDTO.class,
                        columns = {
                                @ColumnResult(name = "id", type = UUID.class),
                                @ColumnResult(name = "noFBook", type = String.class),
                                @ColumnResult(name = "noMBook", type = String.class),
                                @ColumnResult(name = "postedDate", type = LocalDate.class),
                                @ColumnResult(name = "date", type = LocalDate.class),
                                @ColumnResult(name = "accountingObjectID", type = UUID.class),
                                @ColumnResult(name = "accountingObjectName", type = String.class),
                                @ColumnResult(name = "reason", type = String.class),
                                @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                                @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                                @ColumnResult(name = "exchangeRate", type = BigDecimal.class),
                                @ColumnResult(name = "totalDiscountAmount", type = BigDecimal.class),
                                @ColumnResult(name = "totalDiscountAmountOriginal", type = BigDecimal.class),
                                @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                                @ColumnResult(name = "totalVATAmountOriginal", type = BigDecimal.class),
                                @ColumnResult(name = "sumAmount", type = BigDecimal.class)
                        }
                )
        }
),
})
public class PPService implements Serializable {
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

    @NotNull
    @Column(name = "date")
    private LocalDate date;

    @NotNull
    @Column(name = "posteddate")
    private LocalDate postedDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Size(max = 25)
    @Column(name = "nofbook")
    private String noFBook;

    @Size(max = 25)
    @Column(name = "nombook")
    private String noMBook;

    @Column(name = "duedate")
    private LocalDate dueDate;

    @Column(name = "paymentvoucherid")
    private UUID paymentVoucherID;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Size(max = 512)
    @Column(name = "accountingobjectname")
    private String accountingObjectName;

    @Size(max = 512)
    @Column(name = "accountingobjectaddress")
    private String accountingObjectAddress;

    @Size(max = 50)
    @Column(name = "companytaxcode")
    private String companyTaxCode;

    @Size(max = 512)
    @Column(name = "accountingobjectcontactname")
    private String contactName;

    @Size(max = 512)
    @Column(name = "reason")
    private String reason;

    @Size(max = 512)
    @Column(name = "numberattach")
    private String numberAttach;

    @Column(name = "isfeightservice")
    private Boolean isFeightService;

    @Column(name = "billreceived")
    private Boolean billReceived;

    @Column(name = "employeeid")
    private UUID employeeID;


    @Column(name = "currencyid")
    private String currencyID;


    @Digits(integer=25, fraction=10)
    @Column(name = "exchangerate")
    private BigDecimal exchangeRate;

    @NotNull
    @Column(name = "totalamount")
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "totalamountoriginal")
    private BigDecimal totalAmountOriginal;

    @NotNull
    @Column(name = "totalvatamount")
    private BigDecimal totalVATAmount;
    @NotNull
    @Column(name = "totalvatamountoriginal")
    private BigDecimal totalVATAmountOriginal;
    @NotNull
    @Column(name = "totaldiscountamount")
    private BigDecimal totalDiscountAmount;
    @NotNull
    @Column(name = "totaldiscountamountoriginal")
    private BigDecimal totalDiscountAmountOriginal;

    @Column(name = "paymentclauseid")
    private UUID paymentClauseID;

    @Column(name = "templateid")
    private UUID templateID;

    @Column(name = "ismatch")
    private Integer IsMatch;

    @Column(name = "matchdate")
    private LocalDate matchDate;

    @Column(name = "recorded")
    private Boolean recorded;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ppserviceid")
    private Set<PPServiceDetail> ppServiceDetails = new HashSet<>();

    public Set<PPServiceDetail> getPpServiceDetails() {
        return ppServiceDetails;
    }

    public void setPpServiceDetails(Set<PPServiceDetail> ppServiceDetails) {
        if (this.ppServiceDetails == null) {
            this.ppServiceDetails = ppServiceDetails;
        } else if(this.ppServiceDetails != ppServiceDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.ppServiceDetails.clear();
            if(ppServiceDetails != null){
                this.ppServiceDetails.addAll(ppServiceDetails);
            }
        }
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public UUID getPaymentVoucherID() {
        return paymentVoucherID;
    }

    public void setPaymentVoucherID(UUID paymentVoucherID) {
        this.paymentVoucherID = paymentVoucherID;
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

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
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

    public String getNumberAttach() {
        return numberAttach;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public Boolean getIsFeightService() {
        return isFeightService;
    }

    public void setIsFeightService(Boolean isFeightService) {
        this.isFeightService = isFeightService;
    }

    public Boolean getBillReceived() {
        return billReceived;
    }

    public void setBillReceived(Boolean billReceived) {
        this.billReceived = billReceived;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public Boolean getFeightService() {
        return isFeightService;
    }

    public void setFeightService(Boolean feightService) {
        isFeightService = feightService;
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

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
    }

    public UUID getPaymentClauseID() {
        return paymentClauseID;
    }

    public void setPaymentClauseID(UUID paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public Integer getIsMatch() {
        return IsMatch;
    }

    public void setIsMatch(Integer isMatch) {
        IsMatch = isMatch;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }
}
