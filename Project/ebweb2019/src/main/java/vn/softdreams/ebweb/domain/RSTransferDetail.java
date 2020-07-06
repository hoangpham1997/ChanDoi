package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailReportDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferDetailsDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferSearchDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A RSInwardOutWardDetails.
 */
@Entity
@Table(name = "rstransferdetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "RSTransferDetailsDTO",
        classes = {
            @ConstructorResult(
                targetClass = RSTransferDetailsDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "rsTransferID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "fromRepositoryCode", type = String.class),
                    @ColumnResult(name = "toRepositoryCode", type = String.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "oWPrice", type = BigDecimal.class),
                    @ColumnResult(name = "oWAmount", type = BigDecimal.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                    @ColumnResult(name = "expenseItemCode", type = String.class),
                    @ColumnResult(name = "costSetCode", type = String.class),
                    @ColumnResult(name = "budgetItemCode", type = String.class),
                    @ColumnResult(name = "organizationUnitCode", type = String.class),
                    @ColumnResult(name = "statisticsCode", type = String.class),
                    @ColumnResult(name = "orderPriority", type = Integer.class),
                    @ColumnResult(name = "fromRepositoryID", type = UUID.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "RSTransferDetailsDTO1",
        classes = {
            @ConstructorResult(
                targetClass = RSTransferDetailsDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "rsTransferID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "debitAccount", type = String.class),
                    @ColumnResult(name = "creditAccount", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "oWPrice", type = BigDecimal.class),
                    @ColumnResult(name = "oWAmount", type = BigDecimal.class),
                    @ColumnResult(name = "fromRepositoryID", type = UUID.class),
                    @ColumnResult(name = "toRepositoryID", type = UUID.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                }
            )
        }
    )
})
public class RSTransferDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    //    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    //    @NotNull
    @Column(name = "rstransferid", insertable = false, updatable = false)
    private UUID rsTransferID;

    @ManyToOne
//    @NotNull
    @JoinColumn(name = "materialgoodsid")
    private MaterialGoods materialGood;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    //    @NotNull
    @Column(name = "fromrepositoryid")
    private UUID fromRepositoryID;

    //    @NotNull
    @Column(name = "torepositoryid")
    private UUID toRepositoryID;

    @Size(max = 25)
    @Column(name = "debitaccount", length = 25)
    private String debitAccount;

    @Size(max = 25)
    @Column(name = "creditaccount", length = 25)
    private String creditAccount;

    @ManyToOne
    @JoinColumn(name = "unitid")
    private Unit unit;

    @Column(name = "quantity", precision = 25, scale = 10)
    private BigDecimal quantity;

    //    @NotNull
    @Column(name = "unitprice")
    private BigDecimal unitPrice;

    //    @NotNull
    @Column(name = "unitpriceoriginal", precision = 10, scale = 2)
    private BigDecimal unitPriceOriginal;

    //    @NotNull
    @Column(name = "owprice")
    private BigDecimal oWPrice;

    //    @NotNull
    @Column(name = "owamount")
    private BigDecimal oWAmount;

    @ManyToOne
    @JoinColumn(name = "mainunitid")
    private Unit mainUnit;

    //    @NotNull
    @Column(name = "mainquantity", precision = 25, scale = 10)
    private BigDecimal mainQuantity;

    //    @NotNull
    @Column(name = "mainunitprice", precision = 10, scale = 2)
    private BigDecimal mainUnitPrice;

    @Column(name = "mainconvertrate", precision = 25, scale = 10)
    private BigDecimal mainConvertRate;

    @Size(max = 25)
    @Column(name = "formula", length = 25)
    private String formula;

    //    @NotNull
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    //    @NotNull
    @Column(name = "amountoriginal", precision = 10, scale = 2)
    private BigDecimal amountOriginal;

    @Column(name = "expirydate")
    private LocalDate expiryDate;

    @Size(max = 50)
    @Column(name = "lotno", length = 50)
    private String lotNo;

    @ManyToOne
    @JoinColumn(name = "costsetid")
    private CostSet costSet;

    @ManyToOne
    @JoinColumn(name = "statisticscodeid")
    private StatisticsCode statisticCode;

    @ManyToOne
    @JoinColumn(name = "departmentid")
    private OrganizationUnit department;

    @ManyToOne
    @JoinColumn(name = "expenseitemid")
    private ExpenseItem expenseItem;

    @ManyToOne
    @JoinColumn(name = "budgetitemid")
    private BudgetItem budgetItem;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String fromRepositoryCode;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String toRepositoryCode;


    @Transient
    private String amountOriginalToString;

    @Transient
    private String amountToString;



    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRsTransferID() {
        return rsTransferID;
    }

    public void setRsTransferID(UUID rsTransferID) {
        this.rsTransferID = rsTransferID;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getFromRepositoryID() {
        return fromRepositoryID;
    }

    public void setFromRepositoryID(UUID fromRepositoryID) {
        this.fromRepositoryID = fromRepositoryID;
    }

    public UUID getToRepositoryID() {
        return toRepositoryID;
    }

    public void setToRepositoryID(UUID toRepositoryID) {
        this.toRepositoryID = toRepositoryID;
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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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

    public Unit getMainUnit() {
        return mainUnit;
    }

    public void setMainUnit(Unit mainUnit) {
        this.mainUnit = mainUnit;
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

    public CostSet getCostSet() {
        return costSet;
    }

    public void setCostSet(CostSet costSet) {
        this.costSet = costSet;
    }

    public StatisticsCode getStatisticCode() {
        return statisticCode;
    }

    public void setStatisticCode(StatisticsCode statisticCode) {
        this.statisticCode = statisticCode;
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

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getFromRepositoryCode() {
        return fromRepositoryCode;
    }

    public void setFromRepositoryCode(String fromRepositoryCode) {
        this.fromRepositoryCode = fromRepositoryCode;
    }

    public String getToRepositoryCode() {
        return toRepositoryCode;
    }

    public void setToRepositoryCode(String toRepositoryCode) {
        this.toRepositoryCode = toRepositoryCode;
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
}
