package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.*;

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
 * A PPInvoice.
 */
@Entity
@Table(name = "ppinvoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "ReceiveBillSearchDTO",
        classes = {
            @ConstructorResult(
                targetClass = ReceiveBillSearchDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "pPID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "vATRate", type = BigDecimal.class),
                    @ColumnResult(name = "vATAccount", type = String.class),
                    @ColumnResult(name = "invoiceTemplate", type = String.class),
                    @ColumnResult(name = "invoiceSeries", type = String.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "invoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "goodsServicePurchaseID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                }
            )
        }
    ), @SqlResultSetMapping(
    name = "PPInvoiceSearchDTO",
    classes = {
        @ConstructorResult(
            targetClass = PPInvoiceSearchDTO.class,
            columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "date", type = LocalDate.class),
                @ColumnResult(name = "postedDate", type = LocalDate.class),
                @ColumnResult(name = "typeLedger", type = Integer.class),
                @ColumnResult(name = "no", type = String.class),
                @ColumnResult(name = "accountingObjectName", type = String.class),
                @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                @ColumnResult(name = "totalDiscountAmount", type = BigDecimal.class),
                @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                @ColumnResult(name = "billReceived", type = Boolean.class),
                @ColumnResult(name = "isImportPurchase", type = Boolean.class),
                @ColumnResult(name = "recorded", type = Boolean.class),
                @ColumnResult(name = "typeId", type = Integer.class),
                @ColumnResult(name = "currencyId", type = String.class),
                @ColumnResult(name = "ppOrderNo", type = String.class),
                @ColumnResult(name = "typeIdStr", type = String.class),
                @ColumnResult(name = "paymentVoucherId", type = UUID.class),
                @ColumnResult(name = "noFBook", type = String.class),
                @ColumnResult(name = "noMBook", type = String.class),
                @ColumnResult(name = "rsInwardOutwardId", type = UUID.class)
            }
        )
    }
), @SqlResultSetMapping(
    name = "PPInvoiceDetailDTO",
    classes = {
        @ConstructorResult(
            targetClass = PPInvoiceDTO.class,
            columns = {
                @ColumnResult(name = "id                                       ", type = UUID.class),
                @ColumnResult(name = "companyId                                ", type = UUID.class),
                @ColumnResult(name = "typeId                                   ", type = Integer.class),
                @ColumnResult(name = "date                                     ", type = LocalDate.class),
                @ColumnResult(name = "postedDate                               ", type = LocalDate.class),
                @ColumnResult(name = "typeLedger                               ", type = Integer.class),
                @ColumnResult(name = "noFBook                                  ", type = String.class),
                @ColumnResult(name = "noMBook                                  ", type = String.class),
                @ColumnResult(name = "rsInwardOutwardId                        ", type = UUID.class),
                @ColumnResult(name = "paymentVoucherId                         ", type = UUID.class),
                @ColumnResult(name = "accountingObjectId                       ", type = UUID.class),
                @ColumnResult(name = "accountingObjectName                     ", type = String.class),
                @ColumnResult(name = "accountingObjectAddress                  ", type = String.class),
                @ColumnResult(name = "companyTaxCode                           ", type = String.class),
                @ColumnResult(name = "contactName                              ", type = String.class),
                @ColumnResult(name = "billReceived                             ", type = Boolean.class),
                @ColumnResult(name = "currencyId                               ", type = String.class),
                @ColumnResult(name = "exchangeRate                             ", type = BigDecimal.class),
                @ColumnResult(name = "paymentClauseId                          ", type = UUID.class),
                @ColumnResult(name = "dueDate                                  ", type = LocalDate.class),
                @ColumnResult(name = "transportMethodId                        ", type = UUID.class),
                @ColumnResult(name = "employeeId                               ", type = UUID.class),
                @ColumnResult(name = "isImportPurchase                         ", type = Boolean.class),
                @ColumnResult(name = "storedInRepository                       ", type = Boolean.class),
                @ColumnResult(name = "totalAmount                              ", type = BigDecimal.class),
                @ColumnResult(name = "totalAmountOriginal                      ", type = BigDecimal.class),
                @ColumnResult(name = "totalImportTaxAmount                     ", type = BigDecimal.class),
                @ColumnResult(name = "totalImportTaxAmountOriginal             ", type = BigDecimal.class),
                @ColumnResult(name = "totalVATAmount                           ", type = BigDecimal.class),
                @ColumnResult(name = "totalVATAmountOriginal                   ", type = BigDecimal.class),
                @ColumnResult(name = "totalDiscountAmount                      ", type = BigDecimal.class),
                @ColumnResult(name = "totalDiscountAmountOriginal              ", type = BigDecimal.class),
                @ColumnResult(name = "totalInwardAmount                        ", type = BigDecimal.class),
                @ColumnResult(name = "totalInwardAmountOriginal                ", type = BigDecimal.class),
                @ColumnResult(name = "totalSpecialConsumeTaxAmount             ", type = BigDecimal.class),
                @ColumnResult(name = "totalSpecialConsumeTaxAmountOriginal     ", type = BigDecimal.class),
                @ColumnResult(name = "matchDate                                ", type = LocalDate.class),
                @ColumnResult(name = "templateId                               ", type = UUID.class),
                @ColumnResult(name = "recorded                                 ", type = Boolean.class),
                @ColumnResult(name = "totalFreightAmount                       ", type = BigDecimal.class),
                @ColumnResult(name = "totalFreightAmountOriginal               ", type = BigDecimal.class),
                @ColumnResult(name = "totalImportTaxExpenseAmount              ", type = BigDecimal.class),
                @ColumnResult(name = "totalImportTaxExpenseAmountOriginal      ", type = BigDecimal.class),
                @ColumnResult(name = "noFBookRs                                ", type = String.class),
                @ColumnResult(name = "reason                                   ", type = String.class),
                @ColumnResult(name = "reasonRs                                 ", type = String.class),
                @ColumnResult(name = "numberAttach                             ", type = String.class),
                @ColumnResult(name = "numberAttachRs                           ", type = String.class),
                @ColumnResult(name = "noMBookRs                                ", type = String.class),

                @ColumnResult(name = "receiverUserName                         ", type = String.class),
                @ColumnResult(name = "accountPaymentId                         ", type = UUID.class),
                @ColumnResult(name = "accountPaymentName                       ", type = String.class),
                @ColumnResult(name = "accountReceiverId                        ", type = UUID.class),
                @ColumnResult(name = "accountReceiverName                      ", type = String.class),
                @ColumnResult(name = "creditCardNumber                         ", type = String.class),
                @ColumnResult(name = "bankAccountReceiverId                    ", type = UUID.class),
                @ColumnResult(name = "bankAccountReceiverName                  ", type = String.class),
                @ColumnResult(name = "otherNoFBook                             ", type = String.class),
                @ColumnResult(name = "otherReason                              ", type = String.class),
                @ColumnResult(name = "otherNumberAttach                        ", type = String.class),
                @ColumnResult(name = "otherNoMBook                             ", type = String.class),
                @ColumnResult(name = "accountReceiverFullName                  ", type = String.class),
                @ColumnResult(name = "identificationNo                         ", type = String.class),
                @ColumnResult(name = "issueDate                                ", type = LocalDate.class),
                @ColumnResult(name = "issueBy                                  ", type = String.class)
            }
        )
    }
),
    @SqlResultSetMapping(
        name = "PPInvoiceDetail1DTO",
        classes = {
            @ConstructorResult(
                targetClass = PPInvoiceDetail1DTO.class,
                columns = {
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "PPInvoiceDTOPopup",
        classes = {
            @ConstructorResult(
                targetClass = ViewPPInvoiceDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "pPInvoiceDetailID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "vATDescription", type = String.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "discountRate", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vATRate", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "ViewPPInvoiceDTO",
        classes = {
            @ConstructorResult(
                targetClass = ViewPPInvoiceDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "rsInwardOutwardID", type = UUID.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "ViewFromMCPaymentDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPInvoiceDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "dueDate", type = LocalDate.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "ViewFromMBTellerPaperDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPInvoiceDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "dueDate", type = LocalDate.class),
                }
            )
        }
    )
})
public class PPInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyId;

    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeId;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "posteddate", nullable = false)
    private LocalDate postedDate;

    @NotNull
    @Column(name = "typeledger", nullable = false)
    private Integer typeLedger;

    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;

    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

    @Column(name = "rsinwardoutwardid")
    private UUID rsInwardOutwardId;

    @Column(name = "paymentvoucherid")
    private UUID paymentVoucherId;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectId;

    @Size(max = 512)
    @Column(name = "accountingobjectname", length = 512)
    private String accountingObjectName;

    @Size(max = 512)
    @Column(name = "accountingobjectaddress", length = 512)
    private String accountingObjectAddress;

    @Size(max = 50)
    @Column(name = "companytaxcode", length = 50)
    private String companyTaxCode;

    @Size(max = 512)
    @Column(name = "contactname", length = 512)
    private String contactName;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Size(max = 512)
    @Column(name = "numberattach", length = 512)
    private String numberAttach;

    @Column(name = "billreceived")
    private Boolean billReceived;

    @Size(max = 3)
    @Column(name = "currencyid", length = 3)
    private String currencyId;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "paymentclauseid")
    private UUID paymentClauseId;

    @Column(name = "duedate")
    private LocalDate dueDate;

    @Column(name = "transportmethodid")
    private UUID transportMethodId;

    @Column(name = "employeeid")
    private UUID employeeId;

    @Column(name = "isimportpurchase")
    private Boolean isImportPurchase;

    @Column(name = "storedinrepository")
    private Boolean storedInRepository;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "totalimporttaxamount", precision = 10, scale = 2)
    private BigDecimal totalImportTaxAmount;

    @Column(name = "totalimporttaxamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalImportTaxAmountOriginal;

    @Column(name = "totalvatamount", precision = 10, scale = 2)
    private BigDecimal totalVATAmount;

    @Column(name = "totalvatamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalVATAmountOriginal;

    @Column(name = "totaldiscountamount", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmount;

    @Column(name = "totaldiscountamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmountOriginal;

    @Column(name = "totalinwardamount", precision = 10, scale = 2)
    private BigDecimal totalInwardAmount;

    @Column(name = "totalinwardamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalInwardAmountOriginal;

    @Column(name = "totalspecialconsumetaxamount", precision = 10, scale = 2)
    private BigDecimal totalSpecialConsumeTaxAmount;

    @Column(name = "totalspecialconsumetaxamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalSpecialConsumeTaxAmountOriginal;

    @Column(name = "ismatch")
    private Boolean isMatch;

    @Column(name = "matchdate")
    private LocalDate matchDate;

    @Column(name = "templateid")
    private UUID templateId;

    @Column(name = "recorded")
    private Boolean recorded;

    @Column(name = "totalfreightamount", precision = 10, scale = 2)
    private BigDecimal totalFreightAmount;

    @Column(name = "totalfreightamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalFreightAmountOriginal;

    @Column(name = "totalimporttaxexpenseamount", precision = 10, scale = 2)
    private BigDecimal totalImportTaxExpenseAmount;

    @Column(name = "totalimporttaxexpenseamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalImportTaxExpenseAmountOriginal;

    @OneToMany(cascade={CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ppinvoiceid")
    private Set<PPInvoiceDetails> ppInvoiceDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Set<PPInvoiceDetails> getPpInvoiceDetails() {
        return ppInvoiceDetails;
    }

    public void setPpInvoiceDetails(Set<PPInvoiceDetails> ppInvoiceDetails) {
        if (this.ppInvoiceDetails == null) {
            this.ppInvoiceDetails = ppInvoiceDetails;
        } else if (this.ppInvoiceDetails != ppInvoiceDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.ppInvoiceDetails.clear();
            if (ppInvoiceDetails != null) {
                this.ppInvoiceDetails.addAll(ppInvoiceDetails);
            }
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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

    public UUID getRsInwardOutwardId() {
        return rsInwardOutwardId;
    }

    public void setRsInwardOutwardId(UUID rsInwardOutwardId) {
        this.rsInwardOutwardId = rsInwardOutwardId;
    }

    public UUID getPaymentVoucherId() {
        return paymentVoucherId;
    }

    public void setPaymentVoucherId(UUID paymentVoucherId) {
        this.paymentVoucherId = paymentVoucherId;
    }

    public UUID getAccountingObjectId() {
        return accountingObjectId;
    }

    public void setAccountingObjectId(UUID accountingObjectId) {
        this.accountingObjectId = accountingObjectId;
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

    public Boolean isBillReceived() {
        return billReceived;
    }

    public void setBillReceived(Boolean billReceived) {
        this.billReceived = billReceived;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public UUID getPaymentClauseId() {
        return paymentClauseId;
    }

    public void setPaymentClauseId(UUID paymentClauseId) {
        this.paymentClauseId = paymentClauseId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public UUID getTransportMethodId() {
        return transportMethodId;
    }

    public void setTransportMethodId(UUID transportMethodId) {
        this.transportMethodId = transportMethodId;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean isImportPurchase() {
        return isImportPurchase;
    }

    public void setImportPurchase(Boolean importPurchase) {
        isImportPurchase = importPurchase;
    }

    public Boolean isStoredInRepository() {
        return storedInRepository;
    }

    public void setStoredInRepository(Boolean storedInRepository) {
        this.storedInRepository = storedInRepository;
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

    public BigDecimal getTotalImportTaxAmount() {
        return totalImportTaxAmount;
    }

    public void setTotalImportTaxAmount(BigDecimal totalImportTaxAmount) {
        this.totalImportTaxAmount = totalImportTaxAmount;
    }

    public BigDecimal getTotalImportTaxAmountOriginal() {
        return totalImportTaxAmountOriginal;
    }

    public void setTotalImportTaxAmountOriginal(BigDecimal totalImportTaxAmountOriginal) {
        this.totalImportTaxAmountOriginal = totalImportTaxAmountOriginal;
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

    public BigDecimal getTotalInwardAmount() {
        return totalInwardAmount;
    }

    public void setTotalInwardAmount(BigDecimal totalInwardAmount) {
        this.totalInwardAmount = totalInwardAmount;
    }

    public BigDecimal getTotalInwardAmountOriginal() {
        return totalInwardAmountOriginal;
    }

    public void setTotalInwardAmountOriginal(BigDecimal totalInwardAmountOriginal) {
        this.totalInwardAmountOriginal = totalInwardAmountOriginal;
    }

    public BigDecimal getTotalSpecialConsumeTaxAmount() {
        return totalSpecialConsumeTaxAmount;
    }

    public void setTotalSpecialConsumeTaxAmount(BigDecimal totalSpecialConsumeTaxAmount) {
        this.totalSpecialConsumeTaxAmount = totalSpecialConsumeTaxAmount;
    }

    public BigDecimal getTotalSpecialConsumeTaxAmountOriginal() {
        return totalSpecialConsumeTaxAmountOriginal;
    }

    public void setTotalSpecialConsumeTaxAmountOriginal(BigDecimal totalSpecialConsumeTaxAmountOriginal) {
        this.totalSpecialConsumeTaxAmountOriginal = totalSpecialConsumeTaxAmountOriginal;
    }

    public Boolean isMatch() {
        return isMatch;
    }

    public void setMatch(Boolean match) {
        isMatch = match;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public UUID getTemplateID() {
        return templateId;
    }

    public void setTemplateID(UUID templateId) {
        this.templateId = templateId;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public BigDecimal getTotalFreightAmount() {
        return totalFreightAmount;
    }

    public void setTotalFreightAmount(BigDecimal totalFreightAmount) {
        this.totalFreightAmount = totalFreightAmount;
    }

    public BigDecimal getTotalFreightAmountOriginal() {
        return totalFreightAmountOriginal;
    }

    public void setTotalFreightAmountOriginal(BigDecimal totalFreightAmountOriginal) {
        this.totalFreightAmountOriginal = totalFreightAmountOriginal;
    }

    public BigDecimal getTotalImportTaxExpenseAmount() {
        return totalImportTaxExpenseAmount;
    }

    public void setTotalImportTaxExpenseAmount(BigDecimal totalImportTaxExpenseAmount) {
        this.totalImportTaxExpenseAmount = totalImportTaxExpenseAmount;
    }

    public BigDecimal getTotalImportTaxExpenseAmountOriginal() {
        return totalImportTaxExpenseAmountOriginal;
    }

    public void setTotalImportTaxExpenseAmountOriginal(BigDecimal totalImportTaxExpenseAmountOriginal) {
        this.totalImportTaxExpenseAmountOriginal = totalImportTaxExpenseAmountOriginal;
    }

    public void save() {
        //ada
    }

    private UUID saveMC(UUID companyId) {
        MCPayment mcPayment = new MCPayment();
        mcPayment.setCompanyID(companyId);
        return mcPayment.getId();
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
        PPInvoice pPInvoice = (PPInvoice) o;
        if (pPInvoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pPInvoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
