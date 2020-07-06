package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A PPDiscountReturnDetails.
 */
@Entity
@Table(name = "ppdiscountreturndetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "PPDiscountReturnDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPDiscountReturnDetailDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "ppDiscountReturnID", type = UUID.class),
                    @ColumnResult(name = "ppInvoiceID", type = UUID.class),
                    @ColumnResult(name = "ppInvoiceDetailID", type = UUID.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "isPromotion", type = Boolean.class),
                    @ColumnResult(name = "goodsServicePurchaseID", type = UUID.class),
                    @ColumnResult(name = "vatRate", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vatAccount", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "saBillID", type = UUID.class),
                    @ColumnResult(name = "saBillDetailID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "contractID", type = UUID.class),
                    @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "budgetItemID", type = UUID.class),
                    @ColumnResult(name = "isMatch", type = Boolean.class),
                    @ColumnResult(name = "matchDate", type = LocalDate.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "deductionDebitAccount", type = String.class),
                    @ColumnResult(name = "vatDescription", type = String.class),
                    @ColumnResult(name = "ppInVoiceNoBook", type = String.class),
                    @ColumnResult(name = "ppInVoiceDate", type = LocalDate.class),
//                    @ColumnResult(name = "ppInvoiceQuantity", type = BigDecimal.class),
//                    @ColumnResult(name = "refID", type = UUID.class)
                }
            )
        }
    ),

    @SqlResultSetMapping(
        name = "PPDiscountReturnDetailOutWardDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPDiscountReturnDetailOutWardDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "ppDiscountReturnID", type = UUID.class),
                    @ColumnResult(name = "ppInvoiceID", type = UUID.class),
                    @ColumnResult(name = "ppInvoiceDetailID", type = UUID.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "isPromotion", type = Boolean.class),
                    @ColumnResult(name = "goodsServicePurchaseID", type = UUID.class),
                    @ColumnResult(name = "vatRate", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vatAccount", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "saBillID", type = UUID.class),
                    @ColumnResult(name = "saBillDetailID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "contractID", type = UUID.class),
                    @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "budgetItemID", type = UUID.class),
                    @ColumnResult(name = "isMatch", type = Boolean.class),
                    @ColumnResult(name = "matchDate", type = LocalDate.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "deductionDebitAccount", type = String.class),
                    @ColumnResult(name = "vatDescription", type = String.class),
                    @ColumnResult(name = "noBookPPDiscountReturn", type = String.class),
                }
            )
        }
    ),

    @SqlResultSetMapping(
        name = "PPDiscountReturnDetailsReportConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPDiscountReturnDetailsReportConvertDTO.class,
                columns = {
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "expiryDate", type = String.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "vatRate", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vatAccount", type = BigDecimal.class),
                    @ColumnResult(name = "isMatch", type = Boolean.class),
                    @ColumnResult(name = "matchDate", type = String.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "vatDescription", type = String.class),
                    @ColumnResult(name = "deductionDebitAccount", type = String.class),
                    @ColumnResult(name = "isPromotion", type = Boolean.class),
                    @ColumnResult(name = "repositoryCode", type = String.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "unitCode", type = String.class),
                    @ColumnResult(name = "mainUnitCode", type = String.class),
                    @ColumnResult(name = "goodsServicePurchaseCode", type = String.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "contractNoMBook", type = String.class),
                    @ColumnResult(name = "contractNoFBook", type = String.class),
                    @ColumnResult(name = "statisticsCodeCode", type = String.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "BudgetItemCode", type = String.class),
                    @ColumnResult(name = "departmentCode", type = String.class),
                    @ColumnResult(name = "ppInVoiceNoBook", type = String.class),
                    @ColumnResult(name = "ppInVoiceDate", type = String.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "repositoryName", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "PPDiscountReturnDetailConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPDiscountReturnDetailConvertDTO.class,
                columns = {
                    @ColumnResult(name = "pPDiscountReturnID", type = String.class),
                    @ColumnResult(name = "pPInvoiceID", type = String.class),
                    @ColumnResult(name = "pPInvoiceDetailID", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "expiryDate", type = String.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "vatRate", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vatAccount", type = BigDecimal.class),
                    @ColumnResult(name = "isMatch", type = Boolean.class),
                    @ColumnResult(name = "matchDate", type = String.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "vatDescription", type = String.class),
                    @ColumnResult(name = "deductionDebitAccount", type = String.class),
                    @ColumnResult(name = "isPromotion", type = Boolean.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "repositoryCode", type = String.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "mainUnitName", type = String.class),
                    @ColumnResult(name = "goodsServicePurchaseID", type = UUID.class),
                    @ColumnResult(name = "goodsServicePurchaseCode", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "contractNoMBook", type = String.class),
                    @ColumnResult(name = "contractNoFBook", type = String.class),
                    @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                    @ColumnResult(name = "statisticsCode", type = String.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "departmentCode", type = String.class),
                    @ColumnResult(name = "expenseItemID", type = String.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "budgetItemID", type = UUID.class),
                    @ColumnResult(name = "budgetItemCode", type = String.class),
                    @ColumnResult(name = "ppInVoiceNoBook", type = String.class),
                    @ColumnResult(name = "ppInVoiceDate", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "PPDiscountReturnDetailsReportConvertKTDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPDiscountReturnDetailsReportConvertKTDTO.class,
                columns = {
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "orderPriority", type = Long.class),
                    @ColumnResult(name = "exchangeRate", type = Long.class),
                    @ColumnResult(name = "checkVAT", type = Boolean.class),

                }
            )
        }
    )
})
public class PPDiscountReturnDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "ppdiscountreturnid")
    private UUID ppDiscountReturnID;

    @Column(name = "ppinvoiceid")
    private UUID ppInvoiceID;

    @Column(name = "ppinvoicedetailid")
    private UUID ppInvoiceDetailID;

    @Column(name = "repositoryid")
    private UUID repositoryID;

    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;

//    //    @NotNull
    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    //    @NotNull
    @Size(max = 25)
    @Column(name = "debitaccount", length = 25)
    private String debitAccount;

    //    @NotNull
    @Size(max = 25)
    @Column(name = "creditaccount", length = 25)
    private String creditAccount;

    @Column(name = "unitid")
    private UUID unitID;

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

    @Column(name = "mainunitid")
    private UUID mainUnitID;

    @Column(name = "mainquantity", precision = 10, scale = 2)
    private BigDecimal mainQuantity;

    @Column(name = "mainunitprice", precision = 10, scale = 2)
    private BigDecimal mainUnitPrice;

    @Column(name = "mainconvertrate", precision = 10, scale = 2)
    private BigDecimal mainConvertRate;

    //    @NotNull
    @Size(max = 25)
    @Column(name = "formula", length = 25)
    private String formula;

    @Column(name = "expirydate")
    private LocalDate expiryDate;

    //    @NotNull
    @Size(max = 50)
    @Column(name = "lotno", length = 50)
    private String lotNo;

    @Column(name = "ispromotion")
    private Boolean isPromotion;

    @Column(name = "goodsservicepurchaseid")
    private UUID goodsServicePurchaseID;

    @Column(name = "vatrate", precision = 10, scale = 2)
    private BigDecimal vatRate;

    @Column(name = "vatamount", precision = 10, scale = 2)
    private BigDecimal vatAmount;

    @Column(name = "vatamountoriginal", precision = 10, scale = 2)
    private BigDecimal vatAmountOriginal;

    //    @NotNull
    @Size(max = 25)
    @Column(name = "vataccount", length = 25)
    private String vatAccount;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

//    @org.hibernate.annotations.Type(type="uuid-char")
    @Column(name = "sabillid")
    private UUID saBillID;

    @Column(name = "sabilldetailid")
    private UUID saBillDetailID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "contractid")
    private UUID contractID;

    @Column(name = "statisticscodeid")
    private UUID statisticsCodeID;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "budgetitemid")
    private UUID budgetItemID;

    @Column(name = "ismatch")
    private Boolean isMatch;

    @Column(name = "matchdate")
    private LocalDate matchDate;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @Size(max = 25)
    @Column(name = "deductiondebitaccount", length = 25)
    private String deductionDebitAccount;

    @Size(max = 512)
    @Column(name = "vatdescription", length = 512)
    private String vatDescription;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String ppInVoiceNoBook;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String ppInVoiceDate;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private BigDecimal ppInvoiceQuantity;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID refID;

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


    public PPDiscountReturnDetails() {
    }

    public PPDiscountReturnDetails(UUID ppInvoiceID, UUID ppInvoiceDetailID, UUID repositoryID, UUID materialGoodsID,
                                   @Size(max = 512) String description, @Size(max = 25) String debitAccount,
                                   @Size(max = 25) String creditAccount, UUID unitID, BigDecimal quantity,
                                   BigDecimal unitPrice, BigDecimal unitPriceOriginal, BigDecimal amount,
                                   BigDecimal amountOriginal, UUID mainUnitID, BigDecimal mainQuantity,
                                   BigDecimal mainUnitPrice, BigDecimal mainConvertRate, @Size(max = 25) String formula,
                                   LocalDate expiryDate, @Size(max = 50) String lotNo, Boolean isPromotion,
                                   UUID goodsServicePurchaseID, BigDecimal vatRate, BigDecimal vatAmount,
                                   BigDecimal vatAmountOriginal, @Size(max = 25) String vatAccount, UUID accountingObjectID,
                                   UUID saBillID, UUID saBillDetailID, UUID costSetID, UUID contractID, UUID statisticsCodeID,
                                   UUID departmentID, UUID expenseItemID, UUID budgetItemID, Boolean isMatch,
                                   LocalDate matchDate, Integer orderPriority, @Size(max = 25) String deductionDebitAccount,
                                   @Size(max = 512) String vatDescription) {
        this.ppInvoiceID = ppInvoiceID;
        this.ppInvoiceDetailID = ppInvoiceDetailID;
        this.repositoryID = repositoryID;
        this.materialGoodsID = materialGoodsID;
        this.description = description;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.unitID = unitID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.mainUnitID = mainUnitID;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.expiryDate = expiryDate;
        this.lotNo = lotNo;
        this.isPromotion = isPromotion;
        this.goodsServicePurchaseID = goodsServicePurchaseID;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.vatAmountOriginal = vatAmountOriginal;
        this.vatAccount = vatAccount;
        this.accountingObjectID = accountingObjectID;
        this.saBillID = saBillID;
        this.saBillDetailID = saBillDetailID;
        this.costSetID = costSetID;
        this.contractID = contractID;
        this.statisticsCodeID = statisticsCodeID;
        this.departmentID = departmentID;
        this.expenseItemID = expenseItemID;
        this.budgetItemID = budgetItemID;
        this.isMatch = isMatch;
        this.matchDate = matchDate;
        this.orderPriority = orderPriority;
        this.deductionDebitAccount = deductionDebitAccount;
        this.vatDescription = vatDescription;
    }

    public UUID getPpDiscountReturnID() {
        return ppDiscountReturnID;
    }

    public void setPpDiscountReturnID(UUID ppDiscountReturnID) {
        this.ppDiscountReturnID = ppDiscountReturnID;
    }

    public BigDecimal getPpInvoiceQuantity() {
        return ppInvoiceQuantity;
    }

    public void setPpInvoiceQuantity(BigDecimal ppInvoiceQuantity) {
        this.ppInvoiceQuantity = ppInvoiceQuantity;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public String getDeductionDebitAccount() {
        return deductionDebitAccount;
    }

    public void setDeductionDebitAccount(String deductionDebitAccount) {
        this.deductionDebitAccount = deductionDebitAccount;
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

    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
    }

    public UUID getPpInvoiceID() {
        return ppInvoiceID;
    }

    public void setPpInvoiceID(UUID ppInvoiceID) {
        this.ppInvoiceID = ppInvoiceID;
    }

    public UUID getPpInvoiceDetailID() {
        return ppInvoiceDetailID;
    }

    public void setPpInvoiceDetailID(UUID ppInvoiceDetailID) {
        this.ppInvoiceDetailID = ppInvoiceDetailID;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
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

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
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

    public UUID getMainUnitID() {
        return mainUnitID;
    }

    public void setMainUnitID(UUID mainUnitID) {
        this.mainUnitID = mainUnitID;
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

    public Boolean getPromotion() {
        return isPromotion;
    }

    public void setPromotion(Boolean promotion) {
        isPromotion = promotion;
    }

    public UUID getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public void setGoodsServicePurchaseID(UUID goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
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

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getSaBillID() {
        return saBillID;
    }

    public void setSaBillID(UUID saBillID) {
        this.saBillID = saBillID;
    }

    public UUID getSaBillDetailID() {
        return saBillDetailID;
    }

    public void setSaBillDetailID(UUID saBillDetailID) {
        this.saBillDetailID = saBillDetailID;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public UUID getBudgetItemID() {
        return budgetItemID;
    }

    public void setBudgetItemID(UUID budgetItemID) {
        this.budgetItemID = budgetItemID;
    }

    public Boolean getMatch() {
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

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getPpInVoiceNoBook() {
        return ppInVoiceNoBook;
    }

    public void setPpInVoiceNoBook(String ppInVoiceNoBook) {
        this.ppInVoiceNoBook = ppInVoiceNoBook;
    }

    public String getPpInVoiceDate() {
        return ppInVoiceDate;
    }

    public void setPpInVoiceDate(String ppInVoiceDate) {
        this.ppInVoiceDate = ppInVoiceDate;
    }

//        public Set<PPDiscountReturn> getPpDiscountReturn() {
//        return ppDiscountReturn;
//    }
//
//    public void setPpDiscountReturn(Set<PPDiscountReturn> ppDiscountReturn) {
//        this.ppDiscountReturn = ppDiscountReturn;
//    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PPDiscountReturnDetails pPDiscountReturnDetails = (PPDiscountReturnDetails) o;
        if (pPDiscountReturnDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pPDiscountReturnDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PPDiscountReturnDetails{" +
            "id=" + getId() +
//            ", ppDiscountReturnID='" + getPpDiscountReturnID() + "'" +
            ", ppInvoiceID='" + getPpInvoiceID() + "'" +
            ", ppInvoiceDetailID='" + getPpInvoiceDetailID() + "'" +
            ", repositoryID='" + getRepositoryID() + "'" +
            ", materialGoodsID='" + getMaterialGoodsID() + "'" +
            ", description='" + getDescription() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", unitID='" + getUnitID() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", unitPriceOriginal=" + getUnitPriceOriginal() +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
            ", mainUnitID='" + getMainUnitID() + "'" +
            ", mainQuantity=" + getMainQuantity() +
            ", mainUnitPrice=" + getMainUnitPrice() +
            ", mainConvertRate=" + getMainConvertRate() +
            ", formula='" + getFormula() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", lotNo='" + getLotNo() + "'" +
            ", goodsServicePurchaseID='" + getGoodsServicePurchaseID() + "'" +
            ", vatRate=" + getVatRate() +
            ", vatAmount=" + getVatAmount() +
            ", vatAmountOriginal=" + getVatAmountOriginal() +
            ", vatAccount='" + getVatAccount() + "'" +
            ", accountingObjectID='" + getAccountingObjectID() + "'" +
            ", saBillID='" + getSaBillID() + "'" +
            ", saBillDetailID='" + getSaBillDetailID() + "'" +
            ", costSetID='" + getCostSetID() + "'" +
            ", contractID='" + getContractID() + "'" +
            ", statisticsCodeID='" + getStatisticsCodeID() + "'" +
            ", departmentID='" + getDepartmentID() + "'" +
            ", expenseItemID='" + getExpenseItemID() + "'" +
            ", budgetItemID='" + getBudgetItemID() + "'" +
            ", matchDate='" + getMatchDate() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }
}
