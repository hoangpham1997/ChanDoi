package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.web.rest.dto.OPMaterialGoodsDTO;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "opmaterialgoods")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "OPMaterialGoodsDTO",
                classes = {
                        @ConstructorResult(
                                targetClass = OPMaterialGoodsDTO.class,
                                columns = {
                                        @ColumnResult(name = "id", type = UUID.class),
                                        @ColumnResult(name = "companyId", type = UUID.class),
                                        @ColumnResult(name = "typeId", type = Integer.class),
                                        @ColumnResult(name = "postedDate", type = LocalDate.class),
                                        @ColumnResult(name = "typeLedger", type = Integer.class),
                                        @ColumnResult(name = "accountNumber", type = String.class),
//                                        @ColumnResult(name = "accountName", type = String.class),
                                        @ColumnResult(name = "materialGoodsId", type = UUID.class),
                                        @ColumnResult(name = "materialGoodsName", type = String.class),
                                        @ColumnResult(name = "materialGoodsCode", type = String.class),
                                        @ColumnResult(name = "currencyId", type = String.class),
                                        @ColumnResult(name = "exchangeRate", type = BigDecimal.class),
                                        @ColumnResult(name = "unitId", type = UUID.class),
                                        @ColumnResult(name = "unitName", type = String.class),
                                        @ColumnResult(name = "quantity", type = BigDecimal.class),
                                        @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                                        @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                                        @ColumnResult(name = "mainUnitId", type = UUID.class),
                                        @ColumnResult(name = "mainUnitName", type = String.class),
                                        @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                                        @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                                        @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                                        @ColumnResult(name = "formula", type = String.class),
                                        @ColumnResult(name = "amount", type = BigDecimal.class),
                                        @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                                        @ColumnResult(name = "lotNo", type = String.class),
                                        @ColumnResult(name = "expiryDate", type = LocalDate.class),
                                        @ColumnResult(name = "expiryDateStr", type = String.class),
                                        @ColumnResult(name = "repositoryId", type = UUID.class),
                                        @ColumnResult(name = "repositoryCode", type = String.class),
                                        @ColumnResult(name = "bankAccountDetailId", type = UUID.class),
                                        @ColumnResult(name = "bankAccount", type = String.class),
                                        @ColumnResult(name = "contractId", type = UUID.class),
                                        @ColumnResult(name = "noBookContract", type = String.class),
                                        @ColumnResult(name = "costSetId", type = UUID.class),
                                        @ColumnResult(name = "costSetCode", type = String.class),
                                        @ColumnResult(name = "expenseItemId", type = UUID.class),
                                        @ColumnResult(name = "expenseItemCode", type = String.class),
                                        @ColumnResult(name = "departmentId", type = UUID.class),
                                        @ColumnResult(name = "organizationUnitCode", type = String.class),
                                        @ColumnResult(name = "statisticsCodeId", type = UUID.class),
                                        @ColumnResult(name = "statisticsCode", type = String.class),
                                        @ColumnResult(name = "budgetItemId", type = UUID.class),
                                        @ColumnResult(name = "budgetItemCode", type = String.class),
                                        @ColumnResult(name = "orderPriority", type = Integer.class),
                                })
                })
})
public class OPMaterialGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyID")
    private UUID companyId;

    @Column(name = "branchID")
    private UUID branchId;

    @Column(name = "typeID")
    private Integer typeId;

    @Column(name = "postedDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate postedDate;

    @Column(name = "typeLedger")
    private Integer typeLedger;

    @Column(name = "accountNumber")
    @Size(max = 25)
    private String accountNumber;

    @Column(name = "materialGoodsID")
    private UUID materialGoodsId;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private MaterialGoods materialGoods;

    @Column(name = "repositoryID")
    private UUID repositoryId;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Repository repository;

    @Column(name = "currencyID")
    @Size(max = 3)
    private String currencyId;

    @Column(name = "exchangeRate")
    @Digits(integer=25, fraction=10)
    private BigDecimal exchangeRate;

    @Column(name = "unitid")
    private UUID unitId;

    @Column(name = "quantity")
    @Digits(integer=25, fraction=10)
    private BigDecimal quantity;

    @Column(name = "unitprice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "unitpriceoriginal", precision = 10, scale = 2)
    private BigDecimal unitPriceOriginal;

    @Column(name = "mainunitid")
    private UUID mainUnitId;

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

    @Column(name = "lotno")
    private String lotNo;

    @Column(name = "expirydate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expiryDate;

    @Column(name = "bankAccountDetailID")
    private UUID bankAccountDetailId;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private BankAccountDetails bankAccountDetails;

    @Column(name = "contractID")
    private UUID contractId;

    @Column(name = "costSetID")
    private UUID costSetId;

    @Column(name = "expenseItemID")
    private UUID expenseItemId;

    @Column(name = "departmentID")
    private UUID departmentId;

    @Column(name = "statisticsCodeID")
    private UUID statisticsCodeId;

    @Column(name = "budgetItemID")
    private UUID budgetItemId;

    @Column(name = "orderPriority")
    private Integer orderPriority;

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

    public OPMaterialGoods(OPMaterialGoodsDTO opMaterialGoodsDTO, UUID companyId) {
        try {
            BeanUtils.copyProperties(this, opMaterialGoodsDTO);
            this.setCompanyId(companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OPMaterialGoods() {
    }

    public BankAccountDetails getBankAccountDetails() {
        return bankAccountDetails;
    }

    public void setBankAccountDetails(BankAccountDetails bankAccountDetails) {
       if (bankAccountDetails != null) {
           this.bankAccountDetails = bankAccountDetails;
           this.bankAccountDetailId = bankAccountDetails.getId();
       }
    }

    public MaterialGoods getMaterialGoods() {
        return materialGoods;
    }

    public void setMaterialGoods(MaterialGoods materialGoods) {
        if (materialGoods != null) {
            this.materialGoods = materialGoods;
            this.materialGoodsId = materialGoods.getId();
        }
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        if (repository != null) {
            this.repository = repository;
            this.repositoryId = repository.getId();
        }
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public UUID getBranchId() {
        return branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public UUID getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(UUID repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public UUID getMaterialGoodsId() {
        return materialGoodsId;
    }

    public void setMaterialGoodsId(UUID materialGoodsId) {
        this.materialGoodsId = materialGoodsId;
    }


    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
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

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public UUID getBankAccountDetailId() {
        return bankAccountDetailId;
    }

    public void setBankAccountDetailId(UUID bankAccountDetailId) {
        this.bankAccountDetailId = bankAccountDetailId;
    }

    public UUID getContractId() {
        return contractId;
    }

    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }

    public UUID getCostSetId() {
        return costSetId;
    }

    public void setCostSetId(UUID costSetId) {
        this.costSetId = costSetId;
    }

    public UUID getExpenseItemId() {
        return expenseItemId;
    }

    public void setExpenseItemId(UUID expenseItemId) {
        this.expenseItemId = expenseItemId;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public UUID getStatisticsCodeId() {
        return statisticsCodeId;
    }

    public void setStatisticsCodeId(UUID statisticsCodeId) {
        this.statisticsCodeId = statisticsCodeId;
    }

    public UUID getBudgetItemId() {
        return budgetItemId;
    }

    public void setBudgetItemId(UUID budgetItemId) {
        this.budgetItemId = budgetItemId;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }
}
