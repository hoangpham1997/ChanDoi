package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RSInwardOutwardDetailsDTO {
    private UUID id;
    private UUID rsInwardOutwardID;
    private UUID materialGoodsID;
    private UUID unitID;
    private UUID mainUnitID;
    private MaterialGoods materialGood;
    private String description;
    private Repository repository;
    private String debitAccount;
    private String creditAccount;
    private Unit unit;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal unitPriceOriginal;
    private Unit mainUnit;
    private BigDecimal mainQuantity;
    private BigDecimal mainUnitPrice;
    private BigDecimal mainConvertRate;
    private String formula;
    private BigDecimal amount;
    private BigDecimal amountOriginal;
    private Integer owPurpose;
    private LocalDate expiryDate;
    private String lotNo;
    private UUID ppOrderID;
    private UUID ppOrderDetailId;
    private UUID saReturnID;
    private UUID saReturnDetailsID;
    private String saInvoiceDetailID;
    private String saOrderDetailID;
    private UUID saOrderID;
    private String rsAssemblyDismantlementID;
    private String rsAssemblyDismantlementDetailID;
    private String rsProductionOrderID;
    private String rsProductionOrderDetailID;
    private CostSet costSet;
    private EMContract contract;
    private String employeeID;
    private StatisticsCode statisticsCode;
    private OrganizationUnit department;
    private ExpenseItem expenseItem;
    private BudgetItem budgetItem;
    private String detailID;
    private Integer orderPriority;
    private UUID ppDiscountReturnID;
    private UUID ppDiscountReturnDetailID;

    public RSInwardOutwardDetailsDTO() {
    }

    public RSInwardOutwardDetailsDTO(UUID id, UUID rsInwardOutwardID, MaterialGoods materialGood, String description, Repository repository, String debitAccount, String creditAccount, Unit unit, BigDecimal quantity, BigDecimal unitPrice, BigDecimal unitPriceOriginal, Unit mainUnit, BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal mainConvertRate, String formula, BigDecimal amount, BigDecimal amountOriginal, Integer owPurpose, LocalDate expiryDate, String lotNo, UUID ppOrderID, UUID ppOrderDetailId, UUID saReturnID, UUID saReturnDetailsID, String saInvoiceDetailID, String saOrderDetailID, UUID saOrderID, String rsAssemblyDismantlementID, String rsAssemblyDismantlementDetailID, String rsProductionOrderID, String rsProductionOrderDetailID, CostSet costSet, EMContract contract, String employeeID, StatisticsCode statisticsCode, OrganizationUnit department, ExpenseItem expenseItem, BudgetItem budgetItem, String detailID, Integer orderPriority, UUID ppDiscountReturnID, UUID ppDiscountReturnDetailID) {
        this.id = id;
        this.rsInwardOutwardID = rsInwardOutwardID;
        this.materialGood = materialGood;
        this.description = description;
        this.repository = repository;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.unit = unit;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.mainUnit = mainUnit;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.owPurpose = owPurpose;
        this.expiryDate = expiryDate;
        this.lotNo = lotNo;
        this.ppOrderID = ppOrderID;
        this.ppOrderDetailId = ppOrderDetailId;
        this.saReturnID = saReturnID;
        this.saReturnDetailsID = saReturnDetailsID;
        this.saInvoiceDetailID = saInvoiceDetailID;
        this.saOrderDetailID = saOrderDetailID;
        this.saOrderID = saOrderID;
        this.rsAssemblyDismantlementID = rsAssemblyDismantlementID;
        this.rsAssemblyDismantlementDetailID = rsAssemblyDismantlementDetailID;
        this.rsProductionOrderID = rsProductionOrderID;
        this.rsProductionOrderDetailID = rsProductionOrderDetailID;
        this.costSet = costSet;
        this.contract = contract;
        this.employeeID = employeeID;
        this.statisticsCode = statisticsCode;
        this.department = department;
        this.expenseItem = expenseItem;
        this.budgetItem = budgetItem;
        this.detailID = detailID;
        this.orderPriority = orderPriority;
        this.ppDiscountReturnID = ppDiscountReturnID;
        this.ppDiscountReturnDetailID = ppDiscountReturnDetailID;
    }

    public RSInwardOutwardDetailsDTO(UUID id, UUID rsInwardOutwardID, UUID materialGoodsID, UUID unitID, UUID mainUnitID, String debitAccount, String creditAccount, BigDecimal quantity, BigDecimal unitPrice, BigDecimal unitPriceOriginal, BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal mainConvertRate, String formula, BigDecimal amount, BigDecimal amountOriginal) {
        this.id = id;
        this.rsInwardOutwardID = rsInwardOutwardID;
        this.materialGoodsID = materialGoodsID;
        this.unitID = unitID;
        this.mainUnitID = mainUnitID;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRsInwardOutwardID() {
        return rsInwardOutwardID;
    }

    public void setRsInwardOutwardID(UUID rsInwardOutwardID) {
        this.rsInwardOutwardID = rsInwardOutwardID;
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

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
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

    public Integer getOwPurpose() {
        return owPurpose;
    }

    public void setOwPurpose(Integer owPurpose) {
        this.owPurpose = owPurpose;
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

    public UUID getPpOrderID() {
        return ppOrderID;
    }

    public void setPpOrderID(UUID ppOrderID) {
        this.ppOrderID = ppOrderID;
    }

    public UUID getPpOrderDetailId() {
        return ppOrderDetailId;
    }

    public void setPpOrderDetailId(UUID ppOrderDetailId) {
        this.ppOrderDetailId = ppOrderDetailId;
    }

    public UUID getSaReturnID() {
        return saReturnID;
    }

    public void setSaReturnID(UUID saReturnID) {
        this.saReturnID = saReturnID;
    }

    public UUID getSaReturnDetailsID() {
        return saReturnDetailsID;
    }

    public void setSaReturnDetailsID(UUID saReturnDetailsID) {
        this.saReturnDetailsID = saReturnDetailsID;
    }

    public String getSaInvoiceDetailID() {
        return saInvoiceDetailID;
    }

    public void setSaInvoiceDetailID(String saInvoiceDetailID) {
        this.saInvoiceDetailID = saInvoiceDetailID;
    }

    public String getSaOrderDetailID() {
        return saOrderDetailID;
    }

    public void setSaOrderDetailID(String saOrderDetailID) {
        this.saOrderDetailID = saOrderDetailID;
    }

    public UUID getSaOrderID() {
        return saOrderID;
    }

    public void setSaOrderID(UUID saOrderID) {
        this.saOrderID = saOrderID;
    }

    public String getRsAssemblyDismantlementID() {
        return rsAssemblyDismantlementID;
    }

    public void setRsAssemblyDismantlementID(String rsAssemblyDismantlementID) {
        this.rsAssemblyDismantlementID = rsAssemblyDismantlementID;
    }

    public String getRsAssemblyDismantlementDetailID() {
        return rsAssemblyDismantlementDetailID;
    }

    public void setRsAssemblyDismantlementDetailID(String rsAssemblyDismantlementDetailID) {
        this.rsAssemblyDismantlementDetailID = rsAssemblyDismantlementDetailID;
    }

    public String getRsProductionOrderID() {
        return rsProductionOrderID;
    }

    public void setRsProductionOrderID(String rsProductionOrderID) {
        this.rsProductionOrderID = rsProductionOrderID;
    }

    public String getRsProductionOrderDetailID() {
        return rsProductionOrderDetailID;
    }

    public void setRsProductionOrderDetailID(String rsProductionOrderDetailID) {
        this.rsProductionOrderDetailID = rsProductionOrderDetailID;
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

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
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

    public String getDetailID() {
        return detailID;
    }

    public void setDetailID(String detailID) {
        this.detailID = detailID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getPpDiscountReturnID() {
        return ppDiscountReturnID;
    }

    public void setPpDiscountReturnID(UUID ppDiscountReturnID) {
        this.ppDiscountReturnID = ppDiscountReturnID;
    }

    public UUID getPpDiscountReturnDetailID() {
        return ppDiscountReturnDetailID;
    }

    public void setPpDiscountReturnDetailID(UUID ppDiscountReturnDetailID) {
        this.ppDiscountReturnDetailID = ppDiscountReturnDetailID;
    }


}
