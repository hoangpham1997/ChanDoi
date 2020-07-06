package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cglib.core.Local;
import vn.softdreams.ebweb.service.dto.SAInvoiceDetailsOutWardDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceDetailPopupDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A SAInvoiceDetails.
 */
@Entity
@Table(name = "sainvoicedetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "SAInvoiceDetailPopupDTO",
        classes = {
            @ConstructorResult(
                targetClass = SAInvoiceDetailPopupDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "saInvoiceID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "isPromotion", type = Boolean.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
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
                    @ColumnResult(name = "vatAccount", type = String.class),
                    @ColumnResult(name = "repositoryAccount", type = String.class),
                    @ColumnResult(name = "costAccount", type = String.class),
                    @ColumnResult(name = "owPrice", type = BigDecimal.class),
                    @ColumnResult(name = "owAmount", type = BigDecimal.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "budgetItemID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "contractID", type = UUID.class),
                    @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                    @ColumnResult(name = "vatDescription", type = String.class),
                    @ColumnResult(name = "deductionDebitAccount", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "accountingObjectID2", type = UUID.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "typeID", type = Integer.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SAInvoiceDetailsOutWardDTO",
        classes = {
            @ConstructorResult(
                targetClass = SAInvoiceDetailsOutWardDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "sAInvoiceId", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "isPromotion", type = Boolean.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
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
                    @ColumnResult(name = "vatAccount", type = String.class),
                    @ColumnResult(name = "repositoryAccount", type = String.class),
                    @ColumnResult(name = "costAccount", type = String.class),
                    @ColumnResult(name = "owPrice", type = BigDecimal.class),
                    @ColumnResult(name = "owAmount", type = BigDecimal.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "departmentID", type = UUID.class),
                    @ColumnResult(name = "expenseItemID", type = UUID.class),
                    @ColumnResult(name = "budgetItemID", type = UUID.class),
                    @ColumnResult(name = "costSetID", type = UUID.class),
                    @ColumnResult(name = "contractID", type = UUID.class),
                    @ColumnResult(name = "statisticsCodeID", type = UUID.class),
                    @ColumnResult(name = "vatDescription", type = String.class),
                    @ColumnResult(name = "deductionDebitAccount", type = String.class),
                    @ColumnResult(name = "noBookSaInvoice", type = String.class),
                }
            )
        }
    )})
public class SAInvoiceDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "sainvoiceid")
    private UUID sAInvoiceId;

    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;

    @Column(name = "ispromotion")
    private Boolean isPromotion;

    @Column(name = "repositoryid")
    private UUID repositoryID;

    @Column(name = "description")
    private String description;

    @Column(name = "debitaccount")
    private String debitAccount;

    @Column(name = "creditaccount")
    private String creditAccount;

    @Column(name = "unitid")
    private UUID unitID;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unitpriceoriginal", precision = 10, scale = 2)
    private BigDecimal unitPriceOriginal;

    @Column(name = "mainunitid")
    private UUID mainUnitID;

    @Column(name = "mainquantity", precision = 10, scale = 2)
    private BigDecimal mainQuantity;

    @Column(name = "mainunitprice", precision = 10, scale = 2)
    private BigDecimal mainUnitPrice;

    @Column(name = "mainconvertrate", precision = 10, scale = 2)
    private BigDecimal mainConvertRate;

    @Column(name = "formula")
    private String formula;

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

    @Column(name = "discountaccount")
    private String discountAccount;

    @Column(name = "vatrate", precision = 10, scale = 2)
    private BigDecimal vATRate;

    @Column(name = "vatamount", precision = 10, scale = 2)
    private BigDecimal vATAmount;

    @Column(name = "vatamountoriginal", precision = 10, scale = 2)
    private BigDecimal vATAmountOriginal;

    @Column(name = "vataccount")
    private String vATAccount;

    @Column(name = "deductiondebitaccount")
    private String deductionDebitAccount;

    @Column(name = "vatdescription")
    private String vATDescription;

    @Column(name = "repositoryaccount")
    private String repositoryAccount;

    @Column(name = "costaccount")
    private String costAccount;

    @Column(name = "owprice", precision = 10, scale = 2)
    private BigDecimal oWPrice;

    @Column(name = "owamount", precision = 10, scale = 2)
    private BigDecimal oWAmount;

    @Column(name = "expirydate")
    private LocalDate expiryDate;

    @Column(name = "lotno")
    private String lotNo;

    @Column(name = "waranty")
    private String waranty;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "exporttaxprice", precision = 10, scale = 2)
    private BigDecimal exportTaxPrice;

    @Column(name = "exporttaxtaxrate", precision = 10, scale = 2)
    private BigDecimal exportTaxTaxRate;

    @Column(name = "exporttaxamount", precision = 10, scale = 2)
    private BigDecimal exportTaxAmount;

    @Column(name = "exporttaxamountaccount")
    private String exportTaxAmountAccount;

    @Column(name = "exporttaxaccountcorresponding")
    private String exportTaxAccountCorresponding;

    @Column(name = "careergroupid")
    private UUID careerGroupID;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "budgetitemid")
    private UUID budgetItemID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "statisticscodeid")
    private UUID statisticsCodeID;

    @Column(name = "contractid")
    private UUID contractID;

    @Column(name = "contractdetailid")
    private UUID contractDetailID;

    @Column(name = "rsinwardoutwardid")
    private UUID rSInwardOutwardID;

    @Column(name = "rsinwardoutwarddetailid")
    private UUID rSInwardOutwardDetailID;

    @Column(name = "saquoteid")
    private UUID sAQuoteID;

    @Column(name = "saquotedetailid")
    private UUID sAQuoteDetailID;

    @Column(name = "saorderid")
    private UUID sAOrderID;

    @Column(name = "saorderdetailid")
    private UUID sAOrderDetailID;

    @Column(name = "sabillid")
    private UUID sABillID;

    @Column(name = "sabilldetailid")
    private UUID sABillDetailID;

    @Column(name = "ppinvoiceid")
    private UUID pPInvoiceID;

    @Column(name = "ppinvoicedetailid")
    private UUID pPInvoiceDetailID;

    @Column(name = "rstransferid")
    private UUID rSTransferID;

    @Column(name = "rstransferdetailid")
    private UUID rSTransferDetailID;

    @Column(name = "confrontid")
    private UUID confrontID;

    @Column(name = "confrontdetailid")
    private UUID confrontDetailID;

    @Column(name = "orderpriority", insertable = false, updatable = false)
    private Integer orderPriority;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String materialGoodsName;

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String materialGoodsCode;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String unitName;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String quanlityString;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private  String unitPriceString;

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

    public String getAmountOriginalToString() {
        return amountOriginalToString;
    }

    public void setAmountOriginalToString(String amountOriginalToString) {
        this.amountOriginalToString = amountOriginalToString;
    }

    public String getAmountOriginalString() {
        return amountOriginalString;
    }

    public void setAmountOriginalString(String amountOriginalString) {
        this.amountOriginalString = amountOriginalString;
    }

    public String getQuanlityString() {
        return quanlityString;
    }

    public void setQuanlityString(String quanlityString) {
        this.quanlityString = quanlityString;
    }

    public String getUnitPriceString() {
        return unitPriceString;
    }

    public void setUnitPriceString(String unitPriceString) {
        this.unitPriceString = unitPriceString;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
// jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getsAInvoiceId() {
        return sAInvoiceId;
    }

    public void setsAInvoiceId(UUID sAInvoiceId) {
        this.sAInvoiceId = sAInvoiceId;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public Boolean isIsPromotion() {
        return isPromotion;
    }

    public void setIsPromotion(Boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
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

    public String getDiscountAccount() {
        return discountAccount;
    }

    public void setDiscountAccount(String discountAccount) {
        this.discountAccount = discountAccount;
    }

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public BigDecimal getvATAmount() {
        return vATAmount;
    }

    public void setvATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
    }

    public String getvATAccount() {
        return vATAccount;
    }

    public void setvATAccount(String vATAccount) {
        this.vATAccount = vATAccount;
    }

    public String getvATDescription() {
        return vATDescription;
    }

    public void setvATDescription(String vATDescription) {
        this.vATDescription = vATDescription;
    }

    public String getRepositoryAccount() {
        return repositoryAccount;
    }

    public void setRepositoryAccount(String repositoryAccount) {
        this.repositoryAccount = repositoryAccount;
    }

    public String getCostAccount() {
        return costAccount;
    }

    public void setCostAccount(String costAccount) {
        this.costAccount = costAccount;
    }

    public BigDecimal getoWPrice() {
        return oWPrice;
    }

    public void setoWPrice(BigDecimal oWPrice) {
        this.oWPrice = oWPrice;
    }

    public BigDecimal getoWAmount() {
        return oWAmount;
    }

    public void setoWAmount(BigDecimal oWAmount) {
        this.oWAmount = oWAmount;
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

    public String getWaranty() {
        return waranty;
    }

    public void setWaranty(String waranty) {
        this.waranty = waranty;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public BigDecimal getExportTaxPrice() {
        return exportTaxPrice;
    }

    public void setExportTaxPrice(BigDecimal exportTaxPrice) {
        this.exportTaxPrice = exportTaxPrice;
    }

    public BigDecimal getExportTaxTaxRate() {
        return exportTaxTaxRate;
    }

    public void setExportTaxTaxRate(BigDecimal exportTaxTaxRate) {
        this.exportTaxTaxRate = exportTaxTaxRate;
    }

    public BigDecimal getExportTaxAmount() {
        return exportTaxAmount;
    }

    public void setExportTaxAmount(BigDecimal exportTaxAmount) {
        this.exportTaxAmount = exportTaxAmount;
    }

    public String getExportTaxAmountAccount() {
        return exportTaxAmountAccount;
    }

    public void setExportTaxAmountAccount(String exportTaxAmountAccount) {
        this.exportTaxAmountAccount = exportTaxAmountAccount;
    }

    public String getDeductionDebitAccount() {
        return deductionDebitAccount;
    }

    public void setDeductionDebitAccount(String deductionDebitAccount) {
        this.deductionDebitAccount = deductionDebitAccount;
    }

    public String getExportTaxAccountCorresponding() {
        return exportTaxAccountCorresponding;
    }

    public void setExportTaxAccountCorresponding(String exportTaxAccountCorresponding) {
        this.exportTaxAccountCorresponding = exportTaxAccountCorresponding;
    }

    public UUID getCareerGroupID() {
        return careerGroupID;
    }

    public void setCareerGroupID(UUID careerGroupID) {
        this.careerGroupID = careerGroupID;
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

    public UUID getStatisticsCodeID() {
        return statisticsCodeID;
    }

    public void setStatisticsCodeID(UUID statisticsCodeID) {
        this.statisticsCodeID = statisticsCodeID;
    }

    public UUID getContractID() {
        return contractID;
    }

    public void setContractID(UUID contractID) {
        this.contractID = contractID;
    }

    public UUID getContractDetailID() {
        return contractDetailID;
    }

    public void setContractDetailID(UUID contractDetailID) {
        this.contractDetailID = contractDetailID;
    }

    public UUID getrSInwardOutwardID() {
        return rSInwardOutwardID;
    }

    public void setrSInwardOutwardID(UUID rSInwardOutwardID) {
        this.rSInwardOutwardID = rSInwardOutwardID;
    }

    public UUID getrSInwardOutwardDetailID() {
        return rSInwardOutwardDetailID;
    }

    public void setrSInwardOutwardDetailID(UUID rSInwardOutwardDetailID) {
        this.rSInwardOutwardDetailID = rSInwardOutwardDetailID;
    }

    public UUID getsAQuoteID() {
        return sAQuoteID;
    }

    public void setsAQuoteID(UUID sAQuoteID) {
        this.sAQuoteID = sAQuoteID;
    }

    public UUID getsAQuoteDetailID() {
        return sAQuoteDetailID;
    }

    public void setsAQuoteDetailID(UUID sAQuoteDetailID) {
        this.sAQuoteDetailID = sAQuoteDetailID;
    }

    public UUID getsAOrderID() {
        return sAOrderID;
    }

    public void setsAOrderID(UUID sAOrderID) {
        this.sAOrderID = sAOrderID;
    }

    public UUID getsAOrderDetailID() {
        return sAOrderDetailID;
    }

    public void setsAOrderDetailID(UUID sAOrderDetailID) {
        this.sAOrderDetailID = sAOrderDetailID;
    }

    public UUID getsABillID() {
        return sABillID;
    }

    public void setsABillID(UUID sABillID) {
        this.sABillID = sABillID;
    }

    public UUID getsABillDetailID() {
        return sABillDetailID;
    }

    public void setsABillDetailID(UUID sABillDetailID) {
        this.sABillDetailID = sABillDetailID;
    }

    public UUID getpPInvoiceID() {
        return pPInvoiceID;
    }

    public void setpPInvoiceID(UUID pPInvoiceID) {
        this.pPInvoiceID = pPInvoiceID;
    }

    public UUID getpPInvoiceDetailID() {
        return pPInvoiceDetailID;
    }

    public void setpPInvoiceDetailID(UUID pPInvoiceDetailID) {
        this.pPInvoiceDetailID = pPInvoiceDetailID;
    }

    public UUID getrSTransferID() {
        return rSTransferID;
    }

    public void setrSTransferID(UUID rSTransferID) {
        this.rSTransferID = rSTransferID;
    }

    public UUID getrSTransferDetailID() {
        return rSTransferDetailID;
    }

    public void setrSTransferDetailID(UUID rSTransferDetailID) {
        this.rSTransferDetailID = rSTransferDetailID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public UUID getConfrontID() {
        return confrontID;
    }

    public void setConfrontID(UUID confrontID) {
        this.confrontID = confrontID;
    }

    public UUID getConfrontDetailID() {
        return confrontDetailID;
    }

    public void setConfrontDetailID(UUID confrontDetailID) {
        this.confrontDetailID = confrontDetailID;
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
        SAInvoiceDetails sAInvoiceDetails = (SAInvoiceDetails) o;
        if (sAInvoiceDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sAInvoiceDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SAInvoiceDetails{" +
            "id=" + getId() +
            ", sAInvoiceId='" + getsAInvoiceId() + "'" +
            ", materialGoodsID='" + getMaterialGoodsID() + "'" +
            ", repositoryID='" + getRepositoryID() + "'" +
            ", description='" + getDescription() + "'" +
            ", debitAccount='" + getDebitAccount() + "'" +
            ", creditAccount='" + getCreditAccount() + "'" +
            ", unitID='" + getUnitID() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", unitPriceOriginal=" + getUnitPriceOriginal() +
            ", mainUnitID='" + getMainUnitID() + "'" +
            ", mainQuantity=" + getMainQuantity() +
            ", mainQuantity=" + getMainQuantity() +
            ", mainUnitPrice=" + getMainUnitPrice() +
            ", mainConvertRate=" + getMainConvertRate() +
            ", formula='" + getFormula() + "'" +
            ", amount=" + getAmount() +
            ", amountOriginal=" + getAmountOriginal() +
            ", discountRate=" + getDiscountRate() +
            ", discountAmount=" + getDiscountAmount() +
            ", discountAmountOriginal=" + getDiscountAmountOriginal() +
            ", discountAccount='" + getDiscountAccount() + "'" +
            ", vATRate=" + getvATRate() +
            ", vATAmount=" + getvATAmount() +
            ", vATAmountOriginal=" + getvATAmountOriginal() +
            ", vATAccount='" + getvATAccount() + "'" +
            ", vATDescription='" + getvATDescription() + "'" +
            ", repositoryAccount='" + getRepositoryAccount() + "'" +
            ", costAccount='" + getCostAccount() + "'" +
            ", oWPrice=" + getoWPrice() +
            ", oWAmount=" + getoWAmount() +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", lotNo='" + getLotNo() + "'" +
            ", waranty='" + getWaranty() + "'" +
            ", accountingObjectID='" + getAccountingObjectID() + "'" +
            ", exportTaxPrice=" + getExportTaxPrice() +
            ", exportTaxTaxRate=" + getExportTaxTaxRate() +
            ", exportTaxAmount=" + getExportTaxAmount() +
            ", exportTaxAmountAccount='" + getExportTaxAmountAccount() + "'" +
            ", exportTaxAccountCorresponding='" + getExportTaxAccountCorresponding() + "'" +
            ", careerGroupID='" + getCareerGroupID() + "'" +
            ", departmentID='" + getDepartmentID() + "'" +
            ", expenseItemID='" + getExpenseItemID() + "'" +
            ", budgetItemID='" + getBudgetItemID() + "'" +
            ", costSetID='" + getCostSetID() + "'" +
            ", statisticsCodeID='" + getStatisticsCodeID() + "'" +
            ", contractID='" + getContractID() + "'" +
            ", contractDetailID='" + getContractDetailID() + "'" +
            ", rSInwardOutwardID='" + getrSInwardOutwardID() + "'" +
            ", rSInwardOutwardDetailID='" + getrSInwardOutwardDetailID() + "'" +
            ", sAQuoteID='" + getsAQuoteID() + "'" +
            ", sAQuoteDetailID='" + getsAQuoteDetailID() + "'" +
            ", sAOrderID='" + getsAOrderID() + "'" +
            ", sAOrderDetailID='" + getsAOrderDetailID() + "'" +
            ", sABillID='" + getsABillID() + "'" +
            ", sABillDetailID='" + getsABillDetailID() + "'" +
            ", pPInvoiceID='" + getpPInvoiceID() + "'" +
            ", pPInvoiceDetailID='" + getpPInvoiceDetailID() + "'" +
            ", rSTransferID='" + getrSTransferID() + "'" +
            ", rSTransferDetailID='" + getrSTransferDetailID() + "'" +
            ", OrderPriority=" + getOrderPriority() +
            "}";
    }
}
