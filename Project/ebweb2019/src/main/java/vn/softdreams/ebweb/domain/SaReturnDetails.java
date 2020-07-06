package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDetailOutWardDTO;
import vn.softdreams.ebweb.service.dto.SaReturnDetailsRSInwardDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A SaReturnDetails.
 */
@Entity
@Table(name = "sareturndetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "SaReturnDetailsRSInwardDTO",
        classes = {
            @ConstructorResult(
                targetClass = SaReturnDetailsRSInwardDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "saReturnID", type = UUID.class),
                    @ColumnResult(name = "isPromotion", type = Boolean.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "discountRate", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "discountAccount", type = String.class),
                    @ColumnResult(name = "vatRate", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "owPrice", type = BigDecimal.class),
                    @ColumnResult(name = "owAmount", type = BigDecimal.class),
                    @ColumnResult(name = "costAccount", type = String.class),
                    @ColumnResult(name = "repositoryAccount", type = String.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "budgetItemID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "contractID", type = UUID.class),
                    @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                    @ColumnResult(name = "cashOutExchangeRateFB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutAmountFB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutDifferAmountFB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutDifferAccountFB", type = String.class),
                    @ColumnResult(name = "cashOutExchangeRateMB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutAmountMB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutDifferAmountMB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutDifferAccountMB", type = String.class),
                    @ColumnResult(name = "cashOutVATAmountFB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutDifferVATAmountFB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutVATAmountMB", type = BigDecimal.class),
                    @ColumnResult(name = "cashOutDifferVATAmountMB", type = BigDecimal.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "vatDescription", type = String.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "vatAccount", type = String.class),
                    @ColumnResult(name = "deductionDebitAccount", type = String.class),
                    @ColumnResult(name = "saBillID", type = UUID.class),
                    @ColumnResult(name = "saBillDetailID", type = UUID.class),
                    @ColumnResult(name = "saInvoiceID", type = UUID.class),
                    @ColumnResult(name = "saInvoiceDetailID", type = UUID.class),
                    @ColumnResult(name = "careerGroupID", type = UUID.class),
                    @ColumnResult(name = "bookSaReturn", type = String.class)
                }
            )
        }
    )
})
public class SaReturnDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "sareturnid")
    private UUID saReturnID;

    @Column(name = "ispromotion")
    private Boolean isPromotion;

    @Column(name = "description")
    private String description;

    @Column(name = "debitaccount")
    private String debitAccount;

    @Column(name = "creditaccount")
    private String creditAccount;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unitpriceoriginal", precision = 10, scale = 2)
    private BigDecimal unitPriceOriginal;

    @Column(name = "mainquantity", precision = 10, scale = 2)
    private BigDecimal mainQuantity;

    @Column(name = "mainunitprice", precision = 10, scale = 2)
    private BigDecimal mainUnitPrice;

    @Column(name = "mainconvertrate", precision = 10, scale = 2)
    private BigDecimal mainConvertRate;

    @Column(name = "formula")
    private String formula;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "amountoriginal", precision = 10, scale = 2)
    private BigDecimal amountOriginal = BigDecimal.ZERO;

    @Column(name = "discountrate", precision = 10, scale = 2)
    private BigDecimal discountRate;

    @Column(name = "discountamount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "discountamountoriginal", precision = 10, scale = 2)
    private BigDecimal discountAmountOriginal = BigDecimal.ZERO;

    @Column(name = "discountaccount")
    private String discountAccount;

    @Column(name = "vatrate", precision = 10, scale = 2)
    private BigDecimal vatRate;

    @Column(name = "vatamount", precision = 10, scale = 2)
    private BigDecimal vatAmount = BigDecimal.ZERO;

    @Column(name = "vatamountoriginal", precision = 10, scale = 2)
    private BigDecimal vatAmountOriginal = BigDecimal.ZERO;

    @Column(name = "owprice", precision = 10, scale = 2)
    private BigDecimal owPrice = BigDecimal.ZERO;

    @Column(name = "owamount", precision = 10, scale = 2)
    private BigDecimal owAmount = BigDecimal.ZERO;

    @Column(name = "costaccount")
    private String costAccount;

    @Column(name = "repositoryaccount")
    private String repositoryAccount;

    @Column(name = "expirydate")
    private LocalDate expiryDate;

    @Column(name = "lotno")
    private String lotNo;

//    @ManyToOne
//    @JoinColumn(name = "departmentid")
    @Column(name = "departmentid")
//    private OrganizationUnit department;
    private UUID departmentID;

//    @ManyToOne
//    @JoinColumn(name = "expenseitemid")
    @Column(name = "expenseitemid")
//    private ExpenseItem expenseItem;
    private UUID expenseItemID;

//    @ManyToOne
//    @JoinColumn(name = "budgetitemid")
    @Column(name = "budgetitemid")
//    private BudgetItem budgetItem;
    private UUID budgetItemID;

//    @ManyToOne
//    @JoinColumn(name = "costsetid")
    @Column(name = "costsetid")
//    private CostSet costSet;
    private UUID costSetID;

//    @ManyToOne
//    @JoinColumn(name = "contractid")
    @Column(name = "contractid")
//    private EMContract contract;
    private UUID contractID;

//    @ManyToOne
//    @JoinColumn(name = "statisticscodeid")
    @Column(name = "statisticscodeid")
//    private StatisticsCode statisticsCode;
    private UUID statisticsCodeID;

    @Column(name = "cashoutexchangeratefb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateFB;

    @Column(name = "cashoutamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountFB;

    @Column(name = "cashoutdifferamountfb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountFB;

    @Column(name = "cashoutdifferaccountfb")
    private String cashOutDifferAccountFB;

    @Column(name = "cashoutexchangeratemb", precision = 10, scale = 2)
    private BigDecimal cashOutExchangeRateMB;

    @Column(name = "cashoutamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutAmountMB;

    @Column(name = "cashoutdifferamountmb", precision = 10, scale = 2)
    private BigDecimal cashOutDifferAmountMB;

    @Column(name = "cashoutdifferaccountmb")
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

    @Column(name = "vatdescription")
    private String vatDescription;

//    @ManyToOne
//    @JoinColumn(name = "materialgoodsid")
    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;

//    @ManyToOne
//    @JoinColumn(name = "unitid")
    @Column(name = "unitid")
    private UUID unitID;

//    @ManyToOne
//    @JoinColumn(name = "repositoryid")
    @Column(name = "repositoryid")
    private UUID repositoryID;

//    @ManyToOne
//    @JoinColumn(name = "mainunitid")
//    private Unit mainUnit;
    @Column(name = "mainunitid")
    private UUID mainUnitID;

//    @ManyToOne
//    @JoinColumn(name = "accountingobjectid")
//    private AccountingObject accountingObject;
    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "vataccount")
    private String vatAccount;

    @Column(name = "deductiondebitaccount")
    private String deductionDebitAccount;

    @Column(name = "sabillid")
    private UUID saBillID;

    @Column(name = "sabilldetailid")
    private UUID saBillDetailID;

    @Column(name = "sainvoiceid")
    private UUID saInvoiceID;

    @Column(name = "sainvoicedetailid")
    private UUID saInvoiceDetailID;

    @Column(name = "careergroupid")
    private UUID careerGroupID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private  String amountString;

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
    }

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private  String amountToString;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private  String amountOriginalString;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private  String discountAmountString;

    @Transient
    private String amountOriginalToString;

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

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getAmountOriginalToString() {
        return amountOriginalToString;
    }

    public void setAmountOriginalToString(String amountOriginalToString) {
        this.amountOriginalToString = amountOriginalToString;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSaReturnID() {
        return saReturnID;
    }

    public SaReturnDetails saReturnID(UUID saReturnID) {
        this.saReturnID = saReturnID;
        return this;
    }

    public void setSaReturnID(UUID saReturnID) {
        this.saReturnID = saReturnID;
    }


    public Boolean isIsPromotion() {
        return isPromotion;
    }

    public SaReturnDetails isPromotion(Boolean isPromotion) {
        this.isPromotion = isPromotion;
        return this;
    }

    public String getAmountOriginalString() {
        return amountOriginalString;
    }

    public void setAmountOriginalString(String amountOriginalString) {
        this.amountOriginalString = amountOriginalString;
    }



    public void setIsPromotion(Boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    public String getDescription() {
        return description;
    }

    public SaReturnDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public SaReturnDetails debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public SaReturnDetails creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public SaReturnDetails quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public SaReturnDetails unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public SaReturnDetails unitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
        return this;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public BigDecimal getMainQuantity() {
        return mainQuantity;
    }

    public SaReturnDetails mainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
        return this;
    }

    public void setMainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    public BigDecimal getMainUnitPrice() {
        return mainUnitPrice;
    }

    public SaReturnDetails mainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
        return this;
    }

    public void setMainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
    }

    public BigDecimal getMainConvertRate() {
        return mainConvertRate;
    }

    public SaReturnDetails mainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
        return this;
    }

    public void setMainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
    }

    public String getFormula() {
        return formula;
    }

    public SaReturnDetails formula(String formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public SaReturnDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public SaReturnDetails amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public SaReturnDetails discountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public SaReturnDetails discountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public SaReturnDetails discountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
        return this;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public String getDiscountAccount() {
        return discountAccount;
    }

    public SaReturnDetails discountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
        return this;
    }

    public void setDiscountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public SaReturnDetails vatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public SaReturnDetails vatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
        return this;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getVatAmountOriginal() {
        return vatAmountOriginal;
    }

    public SaReturnDetails vatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
        return this;
    }

    public void setVatAmountOriginal(BigDecimal vatAmountOriginal) {
        this.vatAmountOriginal = vatAmountOriginal;
    }

    public BigDecimal getOwPrice() {
        return owPrice;
    }

    public void setOwPrice(BigDecimal owPrice) {
        this.owPrice = owPrice;
    }

    public BigDecimal getOwAmount() {
        return owAmount;
    }

    public void setOwAmount(BigDecimal owAmount) {
        this.owAmount = owAmount;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public SaReturnDetails costAccount(String costAccount) {
        this.costAccount = costAccount;
        return this;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public String getRepositoryAccount() {
        return repositoryAccount;
    }

    public SaReturnDetails repositoryAccount(String repositoryAccount) {
        this.repositoryAccount = repositoryAccount;
        return this;
    }

    public void setRepositoryAccount(String repositoryAccount) {
        this.repositoryAccount = repositoryAccount;
    }


    public String getDiscountAmountString() {
        return discountAmountString;
    }

    public void setDiscountAmountString(String discountAmountString) {
        this.discountAmountString = discountAmountString;
    }
//    public UUID getRepositoryID() {
//        return repositoryID;
//    }
//
//    public void setRepositoryID(UUID repositoryID) {
//        this.repositoryID = repositoryID;
//    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public SaReturnDetails expiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLotNo() {
        return lotNo;
    }

    public SaReturnDetails lotNo(String lotNo) {
        this.lotNo = lotNo;
        return this;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

//    public OrganizationUnit getDepartment() {
//        return department;
//    }
//
//    public void setDepartment(OrganizationUnit department) {
//        this.department = department;
//    }
//
//    public ExpenseItem getExpenseItem() {
//        return expenseItem;
//    }
//
//    public void setExpenseItem(ExpenseItem expenseItem) {
//        this.expenseItem = expenseItem;
//    }
//
//    public BudgetItem getBudgetItem() {
//        return budgetItem;
//    }
//
//    public void setBudgetItem(BudgetItem budgetItem) {
//        this.budgetItem = budgetItem;
//    }
//
//    public CostSet getCostSet() {
//        return costSet;
//    }
//
//    public void setCostSet(CostSet costSet) {
//        this.costSet = costSet;
//    }
//
//    public EMContract getContract() {
//        return contract;
//    }
//
//    public void setContract(EMContract contract) {
//        this.contract = contract;
//    }
//
//    public StatisticsCode getStatisticsCode() {
//        return statisticsCode;
//    }
//
//    public void setStatisticsCode(StatisticsCode statisticsCode) {
//        this.statisticsCode = statisticsCode;
//    }

    public BigDecimal getCashOutExchangeRateFB() {
        return cashOutExchangeRateFB;
    }

    public SaReturnDetails cashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
        return this;
    }

    public void setCashOutExchangeRateFB(BigDecimal cashOutExchangeRateFB) {
        this.cashOutExchangeRateFB = cashOutExchangeRateFB;
    }

    public BigDecimal getCashOutAmountFB() {
        return cashOutAmountFB;
    }

    public SaReturnDetails cashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
        return this;
    }

    public void setCashOutAmountFB(BigDecimal cashOutAmountFB) {
        this.cashOutAmountFB = cashOutAmountFB;
    }

    public BigDecimal getCashOutDifferAmountFB() {
        return cashOutDifferAmountFB;
    }

    public SaReturnDetails cashOutDifferAmountFB(BigDecimal cashOutDifferAmountFB) {
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
        return this;
    }

    public void setCashOutDifferAmountFB(BigDecimal cashOutDifferAmountFB) {
        this.cashOutDifferAmountFB = cashOutDifferAmountFB;
    }

    public String getCashOutDifferAccountFB() {
        return cashOutDifferAccountFB;
    }

    public SaReturnDetails cashOutDifferAccountFB(String cashOutDifferAccountFB) {
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
        return this;
    }

    public void setCashOutDifferAccountFB(String cashOutDifferAccountFB) {
        this.cashOutDifferAccountFB = cashOutDifferAccountFB;
    }

    public BigDecimal getCashOutExchangeRateMB() {
        return cashOutExchangeRateMB;
    }

    public SaReturnDetails cashOutExchangeRateMB(BigDecimal cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
        return this;
    }

    public void setCashOutExchangeRateMB(BigDecimal cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
    }

    public BigDecimal getCashOutAmountMB() {
        return cashOutAmountMB;
    }

    public SaReturnDetails cashOutAmountMB(BigDecimal cashOutAmountMB) {
        this.cashOutAmountMB = cashOutAmountMB;
        return this;
    }

    public void setCashOutAmountMB(BigDecimal cashOutAmountMB) {
        this.cashOutAmountMB = cashOutAmountMB;
    }

    public BigDecimal getCashOutDifferAmountMB() {
        return cashOutDifferAmountMB;
    }

    public SaReturnDetails cashOutDifferAmountMB(BigDecimal cashOutDifferAmountMB) {
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
        return this;
    }

    public void setCashOutDifferAmountMB(BigDecimal cashOutDifferAmountMB) {
        this.cashOutDifferAmountMB = cashOutDifferAmountMB;
    }

    public String getCashOutDifferAccountMB() {
        return cashOutDifferAccountMB;
    }

    public SaReturnDetails cashOutDifferAccountMB(String cashOutDifferAccountMB) {
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
        return this;
    }

    public void setCashOutDifferAccountMB(String cashOutDifferAccountMB) {
        this.cashOutDifferAccountMB = cashOutDifferAccountMB;
    }

    public BigDecimal getCashOutVATAmountFB() {
        return cashOutVATAmountFB;
    }

    public SaReturnDetails cashOutVATAmountFB(BigDecimal cashOutVATAmountFB) {
        this.cashOutVATAmountFB = cashOutVATAmountFB;
        return this;
    }

    public void setCashOutVATAmountFB(BigDecimal cashOutVATAmountFB) {
        this.cashOutVATAmountFB = cashOutVATAmountFB;
    }

    public BigDecimal getCashOutDifferVATAmountFB() {
        return cashOutDifferVATAmountFB;
    }

    public SaReturnDetails cashOutDifferVATAmountFB(BigDecimal cashOutDifferVATAmountFB) {
        this.cashOutDifferVATAmountFB = cashOutDifferVATAmountFB;
        return this;
    }

    public void setCashOutDifferVATAmountFB(BigDecimal cashOutDifferVATAmountFB) {
        this.cashOutDifferVATAmountFB = cashOutDifferVATAmountFB;
    }

    public BigDecimal getCashOutVATAmountMB() {
        return cashOutVATAmountMB;
    }

    public SaReturnDetails cashOutVATAmountMB(BigDecimal cashOutVATAmountMB) {
        this.cashOutVATAmountMB = cashOutVATAmountMB;
        return this;
    }

    public void setCashOutVATAmountMB(BigDecimal cashOutVATAmountMB) {
        this.cashOutVATAmountMB = cashOutVATAmountMB;
    }

    public BigDecimal getCashOutDifferVATAmountMB() {
        return cashOutDifferVATAmountMB;
    }

    public SaReturnDetails cashOutDifferVATAmountMB(BigDecimal cashOutDifferVATAmountMB) {
        this.cashOutDifferVATAmountMB = cashOutDifferVATAmountMB;
        return this;
    }

    public void setCashOutDifferVATAmountMB(BigDecimal cashOutDifferVATAmountMB) {
        this.cashOutDifferVATAmountMB = cashOutDifferVATAmountMB;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public SaReturnDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getVatDescription() {
        return vatDescription;
    }

    public SaReturnDetails vatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
        return this;
    }

    public void setVatDescription(String vATDescription) {
        this.vatDescription = vATDescription;
    }

//    public MaterialGoods getMaterialGoods() {
//        return materialGoods;
//    }
//
//    public SaReturnDetails materialGoods(MaterialGoods materialGoods) {
//        this.materialGoods = materialGoods;
//        return this;
//    }
//
//    public void setMaterialGoods(MaterialGoods materialGoods) {
//        this.materialGoods = materialGoods;
//    }

//    public UUID getMaterialGoodsID() {
//        return materialGoodsID;
//    }
//
//    public void setMaterialGoodsID(UUID materialGoodsID) {
//        this.materialGoodsID = materialGoodsID;
//    }

//    public Unit getUnit() {
//        return unit;
//    }
//
//    public SaReturnDetails unit(Unit unit) {
//        this.unit = unit;
//        return this;
//    }
//
//    public void setUnit(Unit unit) {
//        this.unit = unit;
//    }
//
//    public Repository getRepository() {
//        return repository;
//    }
//
//    public SaReturnDetails repository(Repository repository) {
//        this.repository = repository;
//        return this;
//    }
//
//    public void setRepository(Repository repository) {
//        this.repository = repository;
//    }
//
//    public Unit getMainUnit() {
//        return mainUnit;
//    }
//
//    public SaReturnDetails mainUnit(Unit mainUnit) {
//        this.mainUnit = mainUnit;
//        return this;
//    }
//
//    public void setMainUnit(Unit mainUnit) {
//        this.mainUnit = mainUnit;
//    }
//
//    public AccountingObject getAccountingObject() {
//        return accountingObject;
//    }
//
//    public SaReturnDetails accountingObject(AccountingObject accountingObject) {
//        this.accountingObject = accountingObject;
//        return this;
//    }
//
//    public void setAccountingObject(AccountingObject accountingObject) {
//        this.accountingObject = accountingObject;
//    }

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

    public UUID getSaInvoiceID() {
        return saInvoiceID;
    }

    public void setSaInvoiceID(UUID saInvoiceID) {
        this.saInvoiceID = saInvoiceID;
    }

    public UUID getSaInvoiceDetailID() {
        return saInvoiceDetailID;
    }

    public void setSaInvoiceDetailID(UUID saInvoiceDetailID) {
        this.saInvoiceDetailID = saInvoiceDetailID;
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

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public UUID getMainUnitID() {
        return mainUnitID;
    }

    public void setMainUnitID(UUID mainUnitID) {
        this.mainUnitID = mainUnitID;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public UUID getCareerGroupID() {
        return careerGroupID;
    }

    public void setCareerGroupID(UUID careerGroupID) {
        this.careerGroupID = careerGroupID;
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
        SaReturnDetails saReturnDetails = (SaReturnDetails) o;
        if (saReturnDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saReturnDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
