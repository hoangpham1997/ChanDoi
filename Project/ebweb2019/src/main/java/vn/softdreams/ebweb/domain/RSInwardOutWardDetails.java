package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailReportDTO;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailsDTO;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSearchDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A RSInwardOutWardDetails.
 */
@Entity
@Table(name = "rsinwardoutwarddetail")
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "RSInwardOutwardDetailReportDTO",
        classes = {
            @ConstructorResult(
                targetClass = RSInwardOutwardDetailReportDTO.class,
                columns = {
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "repositoryCode", type = String.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "repositoryName", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "RSInwardOutwardDetailsDTO",
        classes = {
            @ConstructorResult(
                targetClass = RSInwardOutwardDetailsDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "rsInwardOutwardID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
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
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "RSInwardOutwardDetailReportDTO1",
        classes = {
            @ConstructorResult(
                targetClass = RSInwardOutwardDetailReportDTO.class,
                columns = {
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "repositoryCode", type = String.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "repositoryName", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "owPrice", type = BigDecimal.class),
                    @ColumnResult(name = "owAmount", type = BigDecimal.class),
                }
            )
        }
    )})
public class RSInwardOutWardDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "rsinwardoutwardid", insertable = false, updatable = false)
    private UUID rsInwardOutwardID;

    @ManyToOne
    @JoinColumn(name = "materialgoodsid")
    private MaterialGoods materialGood;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "repositoryid")
    private Repository repository;

    @Column(name = "debitaccount")
    private String debitAccount;

    @Column(name = "creditaccount")
    private String creditAccount;

    @ManyToOne
    @JoinColumn(name = "unitid")
    private Unit unit;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unitpriceoriginal", precision = 10, scale = 2)
    private BigDecimal unitPriceOriginal;

    @ManyToOne
    @JoinColumn(name = "mainunitid")
    private Unit mainUnit;

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

    @Column(name = "owpurpose")
    private Integer owPurpose;

    @Column(name = "expirydate")
    private LocalDate expiryDate;

    @Column(name = "lotno")
    private String lotNo;

    @Column(name = "pporderid")
    private PPOrder ppOrder;

    @Column(name = "pporderdetailsid")
    private PPOrderDetail ppOrderDetail;

    @ManyToOne
    @JoinColumn(name = "sareturnid")
    private SaReturn saReturn;

    @ManyToOne
    @JoinColumn(name = "sareturndetailsid")
    private SaReturnDetails saReturnDetails;

    @ManyToOne
    @JoinColumn(name = "sainvoiceid")
    private SAInvoice saInvoice;

    @ManyToOne
    @JoinColumn(name = "sainvoicedetailid")
    private SAInvoiceDetails saInvoiceDetails;

    @ManyToOne
    @JoinColumn(name = "saorderdetailid")
    private SAOrderDetails saOrderDetail;

    @ManyToOne
    @JoinColumn(name = "saorderid")
    private SAOrder saOrder;

    @Column(name = "rsassemblydismantlementid")
    private String rsAssemblyDismantlementID;

    @Column(name = "rsassemblydismantlementdetailid")
    private String rsAssemblyDismantlementDetailID;

    @Column(name = "rsproductionorderid")
    private String rsProductionOrderID;

    @Column(name = "rsproductionorderdetailid")
    private String rsProductionOrderDetailID;

    @Column(name = "materialquantumid")
    private UUID materialQuantumID;

    @Column(name = "materialquantumdetailid")
    private UUID materialQuantumDetailID;

    @ManyToOne
    @JoinColumn(name = "costsetid")
    private CostSet costSet;

    @ManyToOne
    @JoinColumn(name = "contractid")
    private EMContract contract;

    @Column(name = "employeeid")
    private String employeeID;

    @ManyToOne
    @JoinColumn(name = "statisticscodeid")
    private StatisticsCode statisticsCode;

    @ManyToOne
    @JoinColumn(name = "departmentid")
    private OrganizationUnit department;

    @ManyToOne
    @JoinColumn(name = "expenseitemid")
    private ExpenseItem expenseItem;

    @ManyToOne
    @JoinColumn(name = "budgetitemid")
    private BudgetItem budgetItem;

    @Column(name = "detailid")
    private String detailID;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @Column(name = "confrontid")
    private UUID confrontID;

    @Column(name = "confrontdetailid")
    private UUID confrontDetailID;

    @ManyToOne
    @JoinColumn(name = "ppdiscountreturnid")
    private PPDiscountReturn ppDiscountReturn;

    @ManyToOne
    @JoinColumn(name = "ppdiscountreturndetailid")
    private PPDiscountReturnDetails ppDiscountReturnDetail;

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

    public SAInvoice getSaInvoice() {
        return saInvoice;
    }

    public void setSaInvoice(SAInvoice saInvoice) {
        this.saInvoice = saInvoice;
    }

    public SAInvoiceDetails getSaInvoiceDetails() {
        return saInvoiceDetails;
    }

    public void setSaInvoiceDetails(SAInvoiceDetails saInvoiceDetails) {
        this.saInvoiceDetails = saInvoiceDetails;
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

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public PPDiscountReturn getPpDiscountReturn() {
        return ppDiscountReturn;
    }

    public void setPpDiscountReturn(PPDiscountReturn ppDiscountReturn) {
        this.ppDiscountReturn = ppDiscountReturn;
    }

    public PPDiscountReturnDetails getPpDiscountReturnDetail() {
        return ppDiscountReturnDetail;
    }

    public void setPpDiscountReturnDetail(PPDiscountReturnDetails ppDiscountReturnDetail) {
        this.ppDiscountReturnDetail = ppDiscountReturnDetail;
    }

    public UUID getRsInwardOutwardID() {
        return rsInwardOutwardID;
    }

    public void setRsInwardOutwardID(UUID rsInwardOutwardID) {
        this.rsInwardOutwardID = rsInwardOutwardID;
    }

    public PPOrder getPpOrder() {
        return ppOrder;
    }

    public void setPpOrder(PPOrder ppOrder) {
        this.ppOrder = ppOrder;
    }

    public PPOrderDetail getPpOrderDetail() {
        return ppOrderDetail;
    }

    public void setPpOrderDetail(PPOrderDetail ppOrderDetail) {
        this.ppOrderDetail = ppOrderDetail;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public SaReturn getSaReturn() {
        return saReturn;
    }

    public void setSaReturn(SaReturn saReturn) {
        this.saReturn = saReturn;
    }

    public SaReturnDetails getSaReturnDetails() {
        return saReturnDetails;
    }

    public void setSaReturnDetails(SaReturnDetails saReturnDetails) {
        this.saReturnDetails = saReturnDetails;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MaterialGoods getMaterialGood() {
        return materialGood;
    }

    public void setMaterialGood(MaterialGoods materialGood) {
        this.materialGood = materialGood;
    }

    public String getDescription() {
        return description;
    }

    public RSInwardOutWardDetails description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public RSInwardOutWardDetails debitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
        return this;
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

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public RSInwardOutWardDetails creditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
        return this;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public RSInwardOutWardDetails quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public RSInwardOutWardDetails unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public RSInwardOutWardDetails unitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
        return this;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public BigDecimal getMainQuantity() {
        return mainQuantity;
    }

    public RSInwardOutWardDetails mainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
        return this;
    }

    public void setMainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    public BigDecimal getMainUnitPrice() {
        return mainUnitPrice;
    }

    public RSInwardOutWardDetails mainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
        return this;
    }

    public void setMainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
    }

    public BigDecimal getMainConvertRate() {
        return mainConvertRate;
    }

    public RSInwardOutWardDetails mainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
        return this;
    }

    public SAOrderDetails getSaOrderDetail() {
        return saOrderDetail;
    }

    public void setSaOrderDetail(SAOrderDetails saOrderDetail) {
        this.saOrderDetail = saOrderDetail;
    }

    public SAOrder getSaOrder() {
        return saOrder;
    }

    public void setSaOrder(SAOrder saOrder) {
        this.saOrder = saOrder;
    }

    public void setMainConvertRate(BigDecimal mainConvertRate) {
        this.mainConvertRate = mainConvertRate;
    }

    public String getFormula() {
        return formula;
    }

    public RSInwardOutWardDetails formula(String formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public RSInwardOutWardDetails amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public RSInwardOutWardDetails amountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
        return this;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public Integer getOwPurpose() {
        return owPurpose;
    }

    public RSInwardOutWardDetails owPurpose(Integer owPurpose) {
        this.owPurpose = owPurpose;
        return this;
    }

    public void setOwPurpose(Integer owPurpose) {
        this.owPurpose = owPurpose;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public RSInwardOutWardDetails expiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLotNo() {
        return lotNo;
    }

    public RSInwardOutWardDetails lotNo(String lotNo) {
        this.lotNo = lotNo;
        return this;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getRsAssemblyDismantlementID() {
        return rsAssemblyDismantlementID;
    }

    public RSInwardOutWardDetails rsAssemblyDismantlementID(String rsAssemblyDismantlementID) {
        this.rsAssemblyDismantlementID = rsAssemblyDismantlementID;
        return this;
    }

    public void setRsAssemblyDismantlementID(String rsAssemblyDismantlementID) {
        this.rsAssemblyDismantlementID = rsAssemblyDismantlementID;
    }

    public String getRsAssemblyDismantlementDetailID() {
        return rsAssemblyDismantlementDetailID;
    }

    public RSInwardOutWardDetails rsAssemblyDismantlementDetailID(String rsAssemblyDismantlementDetailID) {
        this.rsAssemblyDismantlementDetailID = rsAssemblyDismantlementDetailID;
        return this;
    }

    public void setRsAssemblyDismantlementDetailID(String rsAssemblyDismantlementDetailID) {
        this.rsAssemblyDismantlementDetailID = rsAssemblyDismantlementDetailID;
    }

    public String getRsProductionOrderID() {
        return rsProductionOrderID;
    }

    public RSInwardOutWardDetails rsProductionOrderID(String rsProductionOrderID) {
        this.rsProductionOrderID = rsProductionOrderID;
        return this;
    }

    public void setRsProductionOrderID(String rsProductionOrderID) {
        this.rsProductionOrderID = rsProductionOrderID;
    }

    public String getRsProductionOrderDetailID() {
        return rsProductionOrderDetailID;
    }

    public RSInwardOutWardDetails rsProductionOrderDetailID(String rsProductionOrderDetailID) {
        this.rsProductionOrderDetailID = rsProductionOrderDetailID;
        return this;
    }

    public void setRsProductionOrderDetailID(String rsProductionOrderDetailID) {
        this.rsProductionOrderDetailID = rsProductionOrderDetailID;
    }


    public String getEmployeeID() {
        return employeeID;
    }

    public RSInwardOutWardDetails employeeID(String employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getDetailID() {
        return detailID;
    }

    public RSInwardOutWardDetails detailID(String detailID) {
        this.detailID = detailID;
        return this;
    }

    public void setDetailID(String detailID) {
        this.detailID = detailID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public RSInwardOutWardDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getMainUnit() {
        return mainUnit;
    }

    public void setMainUnit(Unit mainUnit) {
        this.mainUnit = mainUnit;
    }

    public CostSet getCostSet() {
        return costSet;
    }

    public void setCostSet(CostSet costSet) {
        this.costSet = costSet;
    }

    public EMContract getContract() {
        return contract;
    }

    public void setContract(EMContract contract) {
        this.contract = contract;
    }

    public StatisticsCode getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(StatisticsCode statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public OrganizationUnit getDepartment() {
        return department;
    }

    public void setDepartment(OrganizationUnit department) {
        this.department = department;
    }

    public ExpenseItem getExpenseItem() {
        return expenseItem;
    }

    public void setExpenseItem(ExpenseItem expenseItem) {
        this.expenseItem = expenseItem;
    }

    public BudgetItem getBudgetItem() {
        return budgetItem;
    }

    public void setBudgetItem(BudgetItem budgetItem) {
        this.budgetItem = budgetItem;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getMaterialQuantumDetailID() {
        return materialQuantumDetailID;
    }

    public void setMaterialQuantumDetailID(UUID materialQuantumDetailID) {
        this.materialQuantumDetailID = materialQuantumDetailID;
    }

    public UUID getMaterialQuantumID() {
        return materialQuantumID;
    }

    public void setMaterialQuantumID(UUID materialQuantumID) {
        this.materialQuantumID = materialQuantumID;
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
        RSInwardOutWardDetails rSInwardOutWardDetails = (RSInwardOutWardDetails) o;
        if (rSInwardOutWardDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rSInwardOutWardDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
