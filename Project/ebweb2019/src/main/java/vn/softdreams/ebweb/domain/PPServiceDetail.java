package vn.softdreams.ebweb.domain;

import javax.persistence.Column;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.PPServiceDetailDTO;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "ppservicedetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "PPServiceDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPServiceDetailDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "materialGoodsId", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "postedObjectId", type = UUID.class),
                    @ColumnResult(name = "postedObjectCode", type = String.class),
                    @ColumnResult(name = "postedObjectName", type = String.class),
                    @ColumnResult(name = "unitId", type = UUID.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "discountRate", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "discountAccount", type = String.class),
                    @ColumnResult(name = "ppOrderId", type = UUID.class),
                    @ColumnResult(name = "ppOrderDetailId", type = UUID.class),
                    @ColumnResult(name = "ppOrderNo", type = String.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vatDescription", type = String.class),
                    @ColumnResult(name = "vatRate", type = BigDecimal.class),
                    @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vatAccount", type = String.class),
                    @ColumnResult(name = "deductionDebitAccount", type = String.class),
                    @ColumnResult(name = "invoiceTemplate", type = String.class),
                    @ColumnResult(name = "invoiceSeries", type = String.class),
                    @ColumnResult(name = "invoiceNo", type = String.class),
                    @ColumnResult(name = "invoiceDate", type = String.class),
                    @ColumnResult(name = "goodsServicePurchaseId", type = UUID.class),
                    @ColumnResult(name = "goodsServicePurchaseName", type = String.class),
                    @ColumnResult(name = "isForeignCurrency", type = Boolean.class),
                    @ColumnResult(name = "vatAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "expenseItemId", type = UUID.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "costSetId", type = UUID.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "emContractId", type = UUID.class),
                    @ColumnResult(name = "emContractCode", type = String.class),
                    @ColumnResult(name = "budgetItemId", type = UUID.class),
                    @ColumnResult(name = "budgetItemCode", type = String.class),
                    @ColumnResult(name = "departmentId", type = UUID.class),
                    @ColumnResult(name = "departmentCode", type = String.class),
                    @ColumnResult(name = "statisticsId", type = UUID.class),
                    @ColumnResult(name = "statisticsCode", type = String.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class)
                }
            )
        }
    )})
public class PPServiceDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "ppserviceid")
    private UUID ppServiceID ;

    @NotNull
    @Column(name = "materialgoodsid")
    private UUID materialGoodsID;

    @Column(name = "description")
    private String description;

    @Column(name = "debitaccount")
    private String debitAccount;

    @Column(name = "creditaccount")
    private String creditAccount;

    @Column(name = "unitid")
    private UUID unitID;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @NotNull
    @Column(name = "unitprice")
    private BigDecimal unitPrice;

    @NotNull
    @Column(name = "unitpriceoriginal")
    private BigDecimal unitPriceOriginal;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "amountoriginal")
    private BigDecimal amountOriginal;

    @Column(name = "discountrate")
    private BigDecimal discountRate;

    @NotNull
    @Column(name = "discountamount")
    private BigDecimal discountAmount;

    @NotNull
    @Column(name = "discountamountoriginal")
    private BigDecimal discountAmountOriginal;

    @Column(name = "discountaccount")
    private String discountAccount;

    @Column(name = "vatrate")
    private BigDecimal vatRate;

    @Column(name = "vatamount")
    private BigDecimal vatAmount;

    @Column(name = "vatamountoriginal")
    private BigDecimal vatAmountOriginal;

    @Column(name = "vataccount")
    private String vatAccount;

    @Column(name = "invoicedate")
    private LocalDate invoiceDate;

    @Column(name = "invoiceno")
    private String invoiceNo;

    @Column(name = "invoiceseries")
    private String invoiceSeries;

    @Column(name = "goodsservicepurchaseid")
    private UUID goodsServicePurchaseID;

    @Column(name = "budgetitemid")
    private UUID budgetItemID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "contractid")
    private UUID contractID;

    @Column(name = "statisticscodeid")
    private UUID statisticsCodeID;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

    @Column(name = "departmentid")
    private UUID departmentID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "cashoutexchangeratefb")
    private String cashOutExchangeRateFB;

    @Column(name = "cashoutamountfb")
    private BigDecimal cashOutAmountFB;

    @Column(name = "cashoutdifferamountfb")
    private BigDecimal cashOutDifferAmountFB;

    @Column(name = "cashoutdifferaccountfb")
    private String cashOutDifferAccountFB;

    @Column(name = "cashoutexchangeratemb")
    private String cashOutExchangeRateMB;

    @Column(name = "cashoutamountmb")
    private BigDecimal cashOutAmountMB;

    @Column(name = "cashoutdifferamountmb")
    private BigDecimal cashOutDifferAmountMB;

    @Column(name = "cashoutdifferaccountmb")
    private String cashOutDifferAccountMB;

    @Column(name = "cashoutvatamountfb")
    private BigDecimal cashOutVATAmountFB;

    @Column(name = "cashoutdiffervatamountfb")
    private BigDecimal cashOutDifferVATAmountFB;

    @Column(name = "cashoutvatamountmb")
    private BigDecimal cashOutVATAmountMB;

    @Column(name = "cashoutdiffervatamountmb")
    private BigDecimal cashOutDifferVATAmountMB;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @Column(name = "vatdescription")
    private String vatDescription;

    @Column(name = "pporderid")
    private UUID ppOrderID;

    @Column(name = "pporderdetailid")
    private UUID ppOrderDetailID;

    @Column(name = "deductiondebitaccount")
    private String deductionDebitAccount;

    @Column(name = "invoicetemplate")
    private String InvoiceTemplate;

    @Transient
    private String amountOriginalToString;

    @Transient
    private String amountToString;

    @Transient
    private String amountOriginalString;

    @Transient
    private String amountString;

    public String getAmountOriginalString() {
        return amountOriginalString;
    }

    public void setAmountOriginalString(String amountOriginalString) {
        this.amountOriginalString = amountOriginalString;
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

    public String getAmountToString() {
        return amountToString;
    }

    public void setAmountToString(String amountToString) {
        this.amountToString = amountToString;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPpServiceID() {
        return ppServiceID;
    }

    public void setPpServiceID(UUID ppServiceID) {
        this.ppServiceID = ppServiceID;
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

    public UUID getGoodsServicePurchaseID() {
        return goodsServicePurchaseID;
    }

    public void setGoodsServicePurchaseID(UUID goodsServicePurchaseID) {
        this.goodsServicePurchaseID = goodsServicePurchaseID;
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

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
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

    public String getCashOutExchangeRateFB() {
        return cashOutExchangeRateFB;
    }

    public void setCashOutExchangeRateFB(String cashOutExchangeRateFB) {
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

    public String getCashOutExchangeRateMB() {
        return cashOutExchangeRateMB;
    }

    public void setCashOutExchangeRateMB(String cashOutExchangeRateMB) {
        this.cashOutExchangeRateMB = cashOutExchangeRateMB;
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

    public String getVatDescription() {
        return vatDescription;
    }

    public void setVatDescription(String vatDescription) {
        this.vatDescription = vatDescription;
    }

    public UUID getPpOrderID() {
        return ppOrderID;
    }

    public void setPpOrderID(UUID ppOrderID) {
        this.ppOrderID = ppOrderID;
    }

    public UUID getPpOrderDetailID() {
        return ppOrderDetailID;
    }

    public void setPpOrderDetailID(UUID ppOrderDetailID) {
        this.ppOrderDetailID = ppOrderDetailID;
    }

    public String getDeductionDebitAccount() {
        return deductionDebitAccount;
    }

    public void setDeductionDebitAccount(String deductionDebitAccount) {
        this.deductionDebitAccount = deductionDebitAccount;
    }

    public String getInvoiceTemplate() {
        return InvoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        InvoiceTemplate = invoiceTemplate;
    }
}
