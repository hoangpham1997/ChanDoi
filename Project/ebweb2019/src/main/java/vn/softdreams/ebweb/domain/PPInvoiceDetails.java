package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvert2DTO;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvertDTO;
import vn.softdreams.ebweb.service.dto.PPInvoiceDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.LotNoDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A PPInvoiceDetails.
 */
@Entity
@Table(name = "ppinvoicedetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "PPInvoiceConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPInvoiceConvertDTO.class,
                columns = {
                    @ColumnResult(name = "ppInvoiceDetailID", type = UUID.class),
                    @ColumnResult(name = "Date", type = LocalDate.class),
                    @ColumnResult(name = "postedDate", type = LocalDate.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                    @ColumnResult(name = "NoMBook", type = String.class),
//                    @ColumnResult(name = "NoFBook", type = String.class),
                    @ColumnResult(name = "MaterialGoodsCode", type = String.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "refID2", type = UUID.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "PPInvoiceID", type = UUID.class),
                    @ColumnResult(name = "MaterialGoodsID", type = UUID.class),
                    @ColumnResult(name = "RepositoryID", type = UUID.class),
                    @ColumnResult(name = "Description", type = String.class),
                    @ColumnResult(name = "DebitAccount", type = String.class),
                    @ColumnResult(name = "CreditAccount", type = String.class),
                    @ColumnResult(name = "UnitID", type = UUID.class),
                    @ColumnResult(name = "Quantity", type = BigDecimal.class),
                    @ColumnResult(name = "remainingAmount", type = BigDecimal.class),
                    @ColumnResult(name = "UnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "UnitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "Amount", type = BigDecimal.class),
                    @ColumnResult(name = "AmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "DiscountRate", type = BigDecimal.class),
                    @ColumnResult(name = "DiscountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "DiscountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "InwardAmount", type = BigDecimal.class),
                    @ColumnResult(name = "InwardAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "FreightAmount", type = BigDecimal.class),
                    @ColumnResult(name = "FreightAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "ImportTaxExpenseAmount", type = BigDecimal.class),
                    @ColumnResult(name = "ImportTaxExpenseAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "ExpiryDate", type = LocalDate.class),
                    @ColumnResult(name = "LotNo", type = String.class),
                    @ColumnResult(name = "CustomUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "VATRate", type = BigDecimal.class),
                    @ColumnResult(name = "VATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "VATAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "VATAccount", type = String.class),
                    @ColumnResult(name = "DeductionDebitAccount", type = String.class),
                    @ColumnResult(name = "MainUnitID", type = UUID.class),
                    @ColumnResult(name = "MainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "MainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "MainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "Formula", type = String.class),
                    @ColumnResult(name = "ImportTaxRate", type = BigDecimal.class),
                    @ColumnResult(name = "ImportTaxAmount", type = BigDecimal.class),
                    @ColumnResult(name = "ImportTaxAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "ImportTaxAccount", type = String.class),
                    @ColumnResult(name = "SpecialConsumeTaxRate", type = BigDecimal.class),
                    @ColumnResult(name = "SpecialConsumeTaxAmount", type = BigDecimal.class),
                    @ColumnResult(name = "SpecialConsumeTaxAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "SpecialConsumeTaxAccount", type = String.class),
                    @ColumnResult(name = "InvoiceType", type = String.class),
                    @ColumnResult(name = "InvoiceDate", type = LocalDate.class),
                    @ColumnResult(name = "InvoiceNo", type = String.class),
                    @ColumnResult(name = "InvoiceSeries", type = String.class),
                    @ColumnResult(name = "GoodsServicePurchaseID", type = UUID.class),
                    @ColumnResult(name = "AccountingObjectID", type = UUID.class),
                    @ColumnResult(name = "BudgetItemID", type = UUID.class),
                    @ColumnResult(name = "CostSetID", type = UUID.class),
                    @ColumnResult(name = "ContractID", type = UUID.class),
                    @ColumnResult(name = "StatisticCodeID", type = UUID.class),
                    @ColumnResult(name = "DepartmentID", type = UUID.class),
                    @ColumnResult(name = "ExpenseItemID", type = UUID.class),
                    @ColumnResult(name = "PPOrderID", type = UUID.class),
                    @ColumnResult(name = "PPOrderDetailID", type = UUID.class),
                    @ColumnResult(name = "CashOutExchangeRateFB", type = BigDecimal.class),
                    @ColumnResult(name = "CashOutAmountFB", type = BigDecimal.class),
                    @ColumnResult(name = "CashOutDifferAmountFB", type = BigDecimal.class),
                    @ColumnResult(name = "CashOutDifferAccountFB", type = String.class),
                    @ColumnResult(name = "CashOutExchangeRateMB", type = BigDecimal.class),
                    @ColumnResult(name = "CashOutAmountMB", type = BigDecimal.class),
                    @ColumnResult(name = "CashOutDifferAmountMB", type = BigDecimal.class),
                    @ColumnResult(name = "CashOutDifferAccountMB", type = String.class),
                    @ColumnResult(name = "CashOutVATAmountFB", type = BigDecimal.class),
                    @ColumnResult(name = "CashOutDifferVATAmountFB", type = BigDecimal.class),
                    @ColumnResult(name = "CashOutVATAmountMB", type = BigDecimal.class),
                    @ColumnResult(name = "CashOutDifferVATAmountMB", type = BigDecimal.class),
                    @ColumnResult(name = "OrderPriority", type = Integer.class),
                    @ColumnResult(name = "VATDescription", type = String.class),
                    @ColumnResult(name = "InvoiceTemplate", type = String.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "PPInvoiceConvert2DTO",
        classes = {
            @ConstructorResult(
                targetClass = PPInvoiceConvert2DTO.class,
                columns = {
                    @ColumnResult(name = "Date", type = LocalDate.class),
                    @ColumnResult(name = "NoMBook", type = String.class),
                    @ColumnResult(name = "NoFBook", type = String.class),
                    @ColumnResult(name = "MaterialGoodsCode", type = String.class),
                    @ColumnResult(name = "ppDiscountReturnDetails", type = PPDiscountReturnDetails.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "PPInvoiceDetailListDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPInvoiceDetailDTO.class,
                columns = {
                    @ColumnResult(name = "id                                    ", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode                     ", type = String.class),
                    @ColumnResult(name = "materialGoodsName                     ", type = String.class),
                    @ColumnResult(name = "repositoryCode                        ", type = String.class),
                    @ColumnResult(name = "debitAccount                          ", type = String.class),
                    @ColumnResult(name = "creditAccount                         ", type = String.class),
                    @ColumnResult(name = "accountingObjectCode                  ", type = String.class),
                    @ColumnResult(name = "unitName                              ", type = String.class),
                    @ColumnResult(name = "quantity                              ", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal                     ", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice                             ", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitName                          ", type = String.class),
                    @ColumnResult(name = "mainConvertRate                       ", type = BigDecimal.class),
                    @ColumnResult(name = "formula                               ", type = String.class),
                    @ColumnResult(name = "mainQuantity                          ", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitPrice                         ", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal                        ", type = BigDecimal.class),
                    @ColumnResult(name = "amount                                ", type = BigDecimal.class),
                    @ColumnResult(name = "discountRate                          ", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmountOriginal                ", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmount                        ", type = BigDecimal.class),
                    @ColumnResult(name = "freightAmount                         ", type = BigDecimal.class),
                    @ColumnResult(name = "importTaxExpenseAmount                ", type = BigDecimal.class),
                    @ColumnResult(name = "inwardAmount                          ", type = BigDecimal.class),
                    @ColumnResult(name = "lotNo                                 ", type = String.class),
                    @ColumnResult(name = "expiryDate                            ", type = LocalDate.class),
                    @ColumnResult(name = "ppOrderDetailId                       ", type = UUID.class),
                    @ColumnResult(name = "vatDescription                        ", type = String.class),
                    @ColumnResult(name = "customUnitPrice                       ", type = BigDecimal.class),
                    @ColumnResult(name = "importTaxRate                         ", type = BigDecimal.class),
                    @ColumnResult(name = "importTaxAmount                       ", type = BigDecimal.class),
                    @ColumnResult(name = "importTaxAccount                      ", type = String.class),
                    @ColumnResult(name = "specialConsumeTaxRate                 ", type = BigDecimal.class),
                    @ColumnResult(name = "specialConsumeTaxAmount               ", type = BigDecimal.class),
                    @ColumnResult(name = "specialConsumeTaxAccount              ", type = String.class),
                    @ColumnResult(name = "vatRate                               ", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmountOriginal                     ", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmount                             ", type = BigDecimal.class),
                    @ColumnResult(name = "vatAccount                            ", type = String.class),
                    @ColumnResult(name = "deductionDebitAccount                 ", type = String.class),
                    @ColumnResult(name = "invoiceTemplate                       ", type = String.class),
                    @ColumnResult(name = "invoiceSeries                         ", type = String.class),
                    @ColumnResult(name = "invoiceNo                             ", type = String.class),
                    @ColumnResult(name = "invoiceDate                           ", type = LocalDate.class),
                    @ColumnResult(name = "goodsServicePurchaseCode              ", type = String.class),
                    @ColumnResult(name = "expenseItemCode                       ", type = String.class),
                    @ColumnResult(name = "costSetCode                           ", type = String.class),
                    @ColumnResult(name = "noMBook                               ", type = String.class),
                    @ColumnResult(name = "budgetItemCode                        ", type = String.class),
                    @ColumnResult(name = "organizationUnitCode                  ", type = String.class),
                    @ColumnResult(name = "statisticsCode                        ", type = String.class),
                    @ColumnResult(name = "ppOrderNo                             ", type = String.class),
                    @ColumnResult(name = "repositoryName                        ", type = String.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "LotNoDTO",
        classes = {
            @ConstructorResult(
                targetClass = LotNoDTO.class,
                columns = {
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                }
            )
        }
    )
})
public class PPInvoiceDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(name = "ppinvoiceid")
    private UUID ppInvoiceId;

    @NotNull
    @Column(name = "materialgoodsid", nullable = false)
    private UUID materialGoodsId;

    @Column(name = "repositoryid")
    private UUID repositoryId;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Size(max = 25)
    @Column(name = "debitaccount", length = 25)
    private String debitAccount;

    @Size(max = 25)
    @Column(name = "creditaccount", length = 25)
    private String creditAccount;

    @Column(name = "unitid")
    private UUID unitId;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unitpriceoriginal", precision = 10, scale = 2)
    private BigDecimal unitPriceOriginal;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "amountoriginal", precision = 10, scale = 2)
    private BigDecimal amountOriginal;

    @Column(name = "discountrate", precision = 10, scale = 2)
    private BigDecimal discountRate;

    @Column(name = "discountamount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "discountamountoriginal", precision = 10, scale = 2)
    private BigDecimal discountAmountOriginal;

    @Column(name = "inwardamount", precision = 10, scale = 2)
    private BigDecimal inwardAmount;

    @Column(name = "inwardamountoriginal", precision = 10, scale = 2)
    private BigDecimal inwardAmountOriginal;

    @Column(name = "freightamount", precision = 10, scale = 2)
    private BigDecimal freightAmount;

    @Column(name = "freightamountoriginal", precision = 10, scale = 2)
    private BigDecimal freightAmountOriginal;

    @Column(name = "importtaxexpenseamount", precision = 10, scale = 2)
    private BigDecimal importTaxExpenseAmount;

    @Column(name = "importtaxexpenseamountoriginal", precision = 10, scale = 2)
    private BigDecimal importTaxExpenseAmountOriginal;

    @Column(name = "expirydate")
    private LocalDate expiryDate;

    @Size(max = 50)
    @Column(name = "lotno", length = 50)
    private String lotNo;

    @Column(name = "customunitprice", precision = 10, scale = 2)
    private BigDecimal customUnitPrice;

    @Column(name = "vatrate", precision = 10, scale = 2)
    private BigDecimal vatRate;

    @Column(name = "vatamount", precision = 10, scale = 2)
    private BigDecimal vatAmount;

    @Column(name = "vatamountoriginal", precision = 10, scale = 2)
    private BigDecimal vatAmountOriginal;

    @Size(max = 25)
    @Column(name = "vataccount", length = 25)
    private String vatAccount;

    @Size(max = 25)
    @Column(name = "deductiondebitaccount", length = 25)
    private String deductionDebitAccount;

    @Column(name = "mainunitid")
    private UUID mainUnitId;

    @Column(name = "mainquantity", precision = 10, scale = 2)
    private BigDecimal mainQuantity;

    @Column(name = "mainunitprice", precision = 10, scale = 2)
    private BigDecimal mainUnitPrice;

    @Column(name = "mainconvertrate", precision = 10, scale = 2)
    private BigDecimal mainConvertRate;

    @Size(max = 25)
    @Column(name = "formula", length = 25)
    private String formula;

    @Column(name = "importtaxrate", precision = 10, scale = 2)
    private BigDecimal importTaxRate;

    @Column(name = "importtaxamount", precision = 10, scale = 2)
    private BigDecimal importTaxAmount;

    @Column(name = "importtaxamountoriginal", precision = 10, scale = 2)
    private BigDecimal importTaxAmountOriginal;

    @Size(max = 25)
    @Column(name = "importtaxaccount", length = 25)
    private String importTaxAccount;

    @Column(name = "specialconsumetaxrate", precision = 10, scale = 2)
    private BigDecimal specialConsumeTaxRate;

    @Column(name = "specialconsumetaxamount", precision = 10, scale = 2)
    private BigDecimal specialConsumeTaxAmount;

    @Column(name = "specialconsumetaxamountoriginal", precision = 10, scale = 2)
    private BigDecimal specialConsumeTaxAmountOriginal;

    @Size(max = 25)
    @Column(name = "specialconsumetaxaccount", length = 25)
    private String specialConsumeTaxAccount;

    @Size(max = 25)
    @Column(name = "invoicetype", length = 25)
    private String invoiceType;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Size(max = 25)
    @Column(name = "invoiceno", length = 25)
    private String invoiceNo;

    @Size(max = 25)
    @Column(name = "invoiceseries", length = 25)
    private String invoiceSeries;

    @Column(name = "goodsservicepurchaseid")
    private UUID goodsServicePurchaseId;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectId;

    @Column(name = "budgetitemid")
    private UUID budgetItemId;

    @Column(name = "costsetid")
    private UUID costSetId;

    @Column(name = "contractid")
    private UUID contractId;

    @Column(name = "statisticcodeid")
    private UUID statisticCodeId;

    @Column(name = "departmentid")
    private UUID departmentId;

    @Column(name = "expenseitemid")
    private UUID expenseItemId;

    @Column(name = "pporderid")
    private UUID ppOrderId;

    @Column(name = "pporderdetailid")
    private UUID ppOrderDetailId;

    @Column(name = "cashoutexchangeratefb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateFB;

    @Column(name = "cashoutamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountFB;

    @Column(name = "cashoutdifferamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountFB;

    @Size(max = 25)
    @Column(name = "cashoutdifferaccountfb", length = 25)
    private String cashOutDifferAccountFB;

    @Column(name = "cashoutexchangeratemb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateMB;

    @Column(name = "cashoutamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountMB;

    @Column(name = "cashoutdifferamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountMB;

    @Size(max = 25)
    @Column(name = "cashoutdifferaccountmb", length = 25)
    private String cashOutDifferAccountMB;

    @Column(name = "cashoutvatamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutVATAmountFB;

    @Column(name = "cashoutdiffervatamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferVATAmountFB;

    @Column(name = "cashoutvatamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutVATAmountMB;

    @Column(name = "cashoutdiffervatamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferVATAmountMB;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @Column(name = "invoicetemplate", length = 25)
    private String invoiceTemplate;

    @Column(name = "vatdescription", length = 512)
    private String vatDescription;

    @Transient
    private String amountOriginalToString;

    @Transient
    private String amountToString;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<MaterialGoodsSpecificationsLedger> materialGoodsSpecificationsLedgers;

    public List<MaterialGoodsSpecificationsLedger> getMaterialGoodsSpecificationsLedgers() {
        return materialGoodsSpecificationsLedgers;
    }

    public void setMaterialGoodsSpecificationsLedgers(List<MaterialGoodsSpecificationsLedger> materialGoodsSpecificationsLedgers) {
        this.materialGoodsSpecificationsLedgers = materialGoodsSpecificationsLedgers;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPpInvoiceId() {
        return ppInvoiceId;
    }

    public void setPpInvoiceId(UUID ppInvoiceId) {
        this.ppInvoiceId = ppInvoiceId;
    }

    public UUID getMaterialGoodsId() {
        return materialGoodsId;
    }

    public void setMaterialGoodsId(UUID materialGoodsId) {
        this.materialGoodsId = materialGoodsId;
    }

    public UUID getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(UUID repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public BigDecimal getInwardAmount() {
        return inwardAmount;
    }

    public void setInwardAmount(BigDecimal inwardAmount) {
        this.inwardAmount = inwardAmount;
    }

    public BigDecimal getInwardAmountOriginal() {
        return inwardAmountOriginal;
    }

    public void setInwardAmountOriginal(BigDecimal inwardAmountOriginal) {
        this.inwardAmountOriginal = inwardAmountOriginal;
    }

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public BigDecimal getFreightAmountOriginal() {
        return freightAmountOriginal;
    }

    public void setFreightAmountOriginal(BigDecimal freightAmountOriginal) {
        this.freightAmountOriginal = freightAmountOriginal;
    }

    public BigDecimal getImportTaxExpenseAmount() {
        return importTaxExpenseAmount;
    }

    public void setImportTaxExpenseAmount(BigDecimal importTaxExpenseAmount) {
        this.importTaxExpenseAmount = importTaxExpenseAmount;
    }

    public BigDecimal getImportTaxExpenseAmountOriginal() {
        return importTaxExpenseAmountOriginal;
    }

    public void setImportTaxExpenseAmountOriginal(BigDecimal importTaxExpenseAmountOriginal) {
        this.importTaxExpenseAmountOriginal = importTaxExpenseAmountOriginal;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public BigDecimal getCustomUnitPrice() {
        return customUnitPrice;
    }

    public void setCustomUnitPrice(BigDecimal customUnitPrice) {
        this.customUnitPrice = customUnitPrice;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getVatAmountOriginal() {
        return vatAmountOriginal;
    }

    public void setVatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
    }

    public String getVatAccount() {
        return vatAccount;
    }

    public void setVatAccount(String vatAccount) {
        this.vatAccount = vatAccount;
    }

    public String getDeductionDebitAccount() {
        return deductionDebitAccount;
    }

    public void setDeductionDebitAccount(String deductionDebitAccount) {
        this.deductionDebitAccount = deductionDebitAccount;
    }

    public UUID getMainUnitId() {
        return mainUnitId;
    }

    public void setMainUnitId(UUID mainUnitId) {
        this.mainUnitId = mainUnitId;
    }

    public BigDecimal getMainQuantity() {
        return mainQuantity;
    }

    public void setMainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    public BigDecimal getMainUnitPrice() {
        return mainUnitPrice;
    }

    public void setMainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
    }

    public BigDecimal getMainConvertRate() {
        return mainConvertRate;
    }

    public void setMainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public BigDecimal getImportTaxRate() {
        return importTaxRate;
    }

    public void setImportTaxRate(BigDecimal importTaxRate) {
        this.importTaxRate = importTaxRate;
    }

    public BigDecimal getImportTaxAmount() {
        return importTaxAmount;
    }

    public void setImportTaxAmount(BigDecimal importTaxAmount) {
        this.importTaxAmount = importTaxAmount;
    }

    public BigDecimal getImportTaxAmountOriginal() {
        return importTaxAmountOriginal;
    }

    public void setImportTaxAmountOriginal(BigDecimal importTaxAmountOriginal) {
        this.importTaxAmountOriginal = importTaxAmountOriginal;
    }

    public String getImportTaxAccount() {
        return importTaxAccount;
    }

    public void setImportTaxAccount(String importTaxAccount) {
        this.importTaxAccount = importTaxAccount;
    }

    public BigDecimal getSpecialConsumeTaxRate() {
        return specialConsumeTaxRate;
    }

    public void setSpecialConsumeTaxRate(BigDecimal specialConsumeTaxRate) {
        this.specialConsumeTaxRate = specialConsumeTaxRate;
    }

    public BigDecimal getSpecialConsumeTaxAmount() {
        return specialConsumeTaxAmount;
    }

    public void setSpecialConsumeTaxAmount(BigDecimal specialConsumeTaxAmount) {
        this.specialConsumeTaxAmount = specialConsumeTaxAmount;
    }

    public BigDecimal getSpecialConsumeTaxAmountOriginal() {
        return specialConsumeTaxAmountOriginal;
    }

    public void setSpecialConsumeTaxAmountOriginal(BigDecimal specialConsumeTaxAmountOriginal) {
        this.specialConsumeTaxAmountOriginal = specialConsumeTaxAmountOriginal;
    }

    public String getSpecialConsumeTaxAccount() {
        return specialConsumeTaxAccount;
    }

    public void setSpecialConsumeTaxAccount(String specialConsumeTaxAccount) {
        this.specialConsumeTaxAccount = specialConsumeTaxAccount;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public UUID getGoodsServicePurchaseId() {
        return goodsServicePurchaseId;
    }

    public void setGoodsServicePurchaseId(UUID goodsServicePurchaseId) {
        this.goodsServicePurchaseId = goodsServicePurchaseId;
    }

    public UUID getAccountingObjectId() {
        return accountingObjectId;
    }

    public void setAccountingObjectId(UUID accountingObjectId) {
        this.accountingObjectId = accountingObjectId;
    }

    public UUID getBudgetItemId() {
        return budgetItemId;
    }

    public void setBudgetItemId(UUID budgetItemId) {
        this.budgetItemId = budgetItemId;
    }

    public UUID getCostSetId() {
        return costSetId;
    }

    public void setCostSetId(UUID costSetId) {
        this.costSetId = costSetId;
    }

    public UUID getContractId() {
        return contractId;
    }

    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }

    public UUID getStatisticCodeId() {
        return statisticCodeId;
    }

    public void setStatisticCodeId(UUID statisticCodeId) {
        this.statisticCodeId = statisticCodeId;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public UUID getExpenseItemId() {
        return expenseItemId;
    }

    public void setExpenseItemId(UUID expenseItemId) {
        this.expenseItemId = expenseItemId;
    }

    public UUID getPpOrderId() {
        return ppOrderId;
    }

    public void setPpOrderId(UUID ppOrderId) {
        this.ppOrderId = ppOrderId;
    }

    public UUID getPpOrderDetailId() {
        return ppOrderDetailId;
    }

    public void setPpOrderDetailId(UUID ppOrderDetailId) {
        this.ppOrderDetailId = ppOrderDetailId;
    }

    public BigDecimal getCashOutExchangeRateFB() {
        return cashOutExchangeRateFB;
    }

    public void setCashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
    }

    public BigDecimal getCashOutAmountFB() {
        return cashOutAmountFB;
    }

    public void setCashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
    }

    public BigDecimal getCashOutDifferAmountFB() {
        return cashOutDifferAmountFB;
    }

    public void setCashOutDifferAmountFB(BigDecimal cashOutDifferAmountFB) {
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
    }

    public String getCashOutDifferAccountFB() {
        return cashOutDifferAccountFB;
    }

    public void setCashOutDifferAccountFB(String cashOutDifferAccountFB) {
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
    }

    public BigDecimal getCashOutExchangeRateMB() {
        return cashOutExchangeRateMB;
    }

    public void setCashOutExchangeRateMB(BigDecimal cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
    }

    public String getAmountOriginalToString() {
        return amountOriginalToString;
    }

    public void setAmountOriginalToString(String amountOriginalToString) {
        this.amountOriginalToString = amountOriginalToString;
    }

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
    }

    public BigDecimal getCashOutAmountMB() {
        return cashOutAmountMB;
    }

    public void setCashOutAmountMB(BigDecimal cashOutAmountMB) {
        this.cashOutAmountMB = cashOutAmountMB;
    }

    public BigDecimal getCashOutDifferAmountMB() {
        return cashOutDifferAmountMB;
    }

    public void setCashOutDifferAmountMB(BigDecimal cashOutDifferAmountMB) {
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
    }

    public String getCashOutDifferAccountMB() {
        return cashOutDifferAccountMB;
    }

    public void setCashOutDifferAccountMB(String cashOutDifferAccountMB) {
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
    }

    public BigDecimal getCashOutVATAmountFB() {
        return cashOutVATAmountFB;
    }

    public void setCashOutVATAmountFB(BigDecimal cashOutVATAmountFB) {
        this.cashOutVATAmountFB = cashOutVATAmountFB;
    }

    public BigDecimal getCashOutDifferVATAmountFB() {
        return cashOutDifferVATAmountFB;
    }

    public void setCashOutDifferVATAmountFB(BigDecimal cashOutDifferVATAmountFB) {
        this.cashOutDifferVATAmountFB = cashOutDifferVATAmountFB;
    }

    public BigDecimal getCashOutVATAmountMB() {
        return cashOutVATAmountMB;
    }

    public void setCashOutVATAmountMB(BigDecimal cashOutVATAmountMB) {
        this.cashOutVATAmountMB = cashOutVATAmountMB;
    }

    public BigDecimal getCashOutDifferVATAmountMB() {
        return cashOutDifferVATAmountMB;
    }

    public void setCashOutDifferVATAmountMB(BigDecimal cashOutDifferVATAmountMB) {
        this.cashOutDifferVATAmountMB = cashOutDifferVATAmountMB;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
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
        PPInvoiceDetails pPInvoiceDetails = (PPInvoiceDetails) o;
        if (pPInvoiceDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pPInvoiceDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PPInvoiceDetails{" +
            "id=" + getId() +
            ", ppInvoiceId='" + getPpInvoiceId() + "'" +
            ", materialGoodsId='" + getMaterialGoodsId() + "'" +
            ", repositoryId='" + getRepositoryId() + "'" +
            ", description='" + getDescription() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", unitId='" + getUnitId() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", unitPriceOriginal=" + getUnitPriceOriginal() +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
            ", discountRate=" + getDiscountRate() +
            ", discountAmount=" + getDiscountAmount() +
            ", discountAmountOriginal=" + getDiscountAmountOriginal() +
            ", inwardAmount=" + getInwardAmount() +
            ", inwardAmountOriginal=" + getInwardAmountOriginal() +
            ", freightAmount=" + getFreightAmount() +
            ", freightAmountOriginal=" + getFreightAmountOriginal() +
            ", importTaxExpenseAmount=" + getImportTaxExpenseAmount() +
            ", importTaxExpenseAmountOriginal=" + getImportTaxExpenseAmountOriginal() +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", lotNo='" + getLotNo() + "'" +
            ", customUnitPrice=" + getCustomUnitPrice() +
            ", vatRate=" + getVatRate() +
            ", vatAmount=" + getVatAmount() +
            ", vatAmountOriginal=" + getVatAmountOriginal() +
            ", vatAccount='" + getVatAccount() + "'" +
            ", deductionDebitAccount='" + getDeductionDebitAccount() + "'" +
            ", mainUnitId='" + getMainUnitId() + "'" +
            ", mainQuantity=" + getMainQuantity() +
            ", mainUnitPrice=" + getMainUnitPrice() +
            ", mainConvertRate=" + getMainConvertRate() +
            ", formula='" + getFormula() + "'" +
            ", importTaxRate=" + getImportTaxRate() +
            ", importTaxAmount=" + getImportTaxAmount() +
            ", importTaxAmountOriginal=" + getImportTaxAmountOriginal() +
            ", importTaxAccount='" + getImportTaxAccount() + "'" +
            ", specialConsumeTaxRate=" + getSpecialConsumeTaxRate() +
            ", specialConsumeTaxAmount=" + getSpecialConsumeTaxAmount() +
            ", specialConsumeTaxAmountOriginal=" + getSpecialConsumeTaxAmountOriginal() +
            ", specialConsumeTaxAccount='" + getSpecialConsumeTaxAccount() + "'" +
            ", invoiceType='" + getInvoiceType() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", invoiceNo='" + getInvoiceNo() + "'" +
            ", invoiceSeries='" + getInvoiceSeries() + "'" +
            ", goodsServicePurchaseId='" + getGoodsServicePurchaseId() + "'" +
            ", accountingObjectId='" + getAccountingObjectId() + "'" +
            ", budgetItemId='" + getBudgetItemId() + "'" +
            ", costSetId='" + getCostSetId() + "'" +
            ", contractId='" + getContractId() + "'" +
            ", statisticCodeId='" + getStatisticCodeId() + "'" +
            ", departmentId='" + getDepartmentId() + "'" +
            ", expenseItemId='" + getExpenseItemId() + "'" +
            ", ppOrderId='" + getPpOrderId() + "'" +
            ", ppOrderDetailId='" + getPpOrderDetailId() + "'" +
            ", cashOutExchangeRateFB=" + getCashOutExchangeRateFB() +
            ", cashOutAmountFB=" + getCashOutAmountFB() +
            ", cashOutDifferAmountFB=" + getCashOutDifferAmountFB() +
            ", cashOutDifferAccountFB='" + getCashOutDifferAccountFB() + "'" +
            ", cashOutExchangeRateMB=" + getCashOutExchangeRateMB() +
            ", cashOutAmountMB=" + getCashOutAmountMB() +
            ", cashOutDifferAmountMB=" + getCashOutDifferAmountMB() +
            ", cashOutDifferAccountMB='" + getCashOutDifferAccountMB() + "'" +
            ", cashOutVATAmountFB=" + getCashOutVATAmountFB() +
            ", cashOutDifferVATAmountFB=" + getCashOutDifferVATAmountFB() +
            ", cashOutVATAmountMB=" + getCashOutVATAmountMB() +
            ", cashOutDifferVATAmountMB=" + getCashOutDifferVATAmountMB() +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
