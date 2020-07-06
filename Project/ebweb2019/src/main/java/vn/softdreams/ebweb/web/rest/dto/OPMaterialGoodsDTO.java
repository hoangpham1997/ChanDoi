package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MaterialGoodsSpecificationsLedger;
import vn.softdreams.ebweb.domain.OPMaterialGoods;
import vn.softdreams.ebweb.domain.Unit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.apache.commons.beanutils.BeanUtils;

public class OPMaterialGoodsDTO {

    private UUID id;

    private UUID companyId;

    private Integer typeId;

    private LocalDate postedDate;

    private Integer typeLedger;

    private String accountNumber;

    private String accountName;

    private UUID materialGoodsId;

    private String materialGoodsName;

    private String materialGoodsCode;

    private List<Unit> units;

    private String currencyId;

    private BigDecimal exchangeRate;

    private UUID unitId;

    private String unitName;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal unitPriceOriginal;

    private UUID mainUnitId;

    private String mainUnitName;

    private BigDecimal mainQuantity;

    private BigDecimal mainUnitPrice;

    private BigDecimal mainConvertRate;

    private String formula;

    private BigDecimal amount;

    private BigDecimal amountOriginal;

    private String lotNo;

    private LocalDate expiryDate;

    private String expiryDateStr;

    private UUID repositoryId;

    private String repositoryCode;

    private UUID bankAccountDetailId;

    private String bankAccount;

    private UUID contractId;

    private String noBookContract;

    private UUID costSetId;

    private String costSetCode;

    private UUID expenseItemId;

    private String expenseItemCode;

    private UUID departmentId;

    private String organizationUnitCode;

    private UUID statisticsCodeId;

    private String statisticsCode;

    private UUID budgetItemId;

    private String budgetItemCode;

    private Integer orderPriority;

    private List<MaterialGoodsSpecificationsLedger> materialGoodsSpecificationsLedgers;


    public OPMaterialGoodsDTO() {
    }

    public OPMaterialGoodsDTO(OPMaterialGoods opMaterialGoods) {
        try {
            BeanUtils.copyProperties(this, opMaterialGoods);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OPMaterialGoodsDTO(UUID id, UUID companyId, Integer typeId, LocalDate postedDate, Integer typeLedger,
                              String accountNumber, UUID materialGoodsId, String materialGoodsName,
                              String materialGoodsCode, String currencyId, BigDecimal exchangeRate,
                              UUID unitId, String unitName, BigDecimal quantity,
                              BigDecimal unitPrice, BigDecimal unitPriceOriginal, UUID mainUnitId,
                              String mainUnitName, BigDecimal mainQuantity, BigDecimal mainUnitPrice,
                              BigDecimal mainConvertRate, String formula, BigDecimal amount,
                              BigDecimal amountOriginal, String lotNo, LocalDate expiryDate, String expiryDateStr,
                              UUID repositoryId, String repositoryCode, UUID bankAccountDetailId,
                              String bankAccount, UUID contractId, String noBookContract, UUID costSetId,
                              String costSetCode, UUID expenseItemId, String expenseItemCode, UUID departmentId,
                              String organizationUnitCode, UUID statisticsCodeId, String statisticsCode,
                              UUID budgetItemId, String budgetItemCode, Integer orderPriority) {
        this.id = id;
        this.companyId = companyId;
        this.typeId = typeId;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.materialGoodsId = materialGoodsId;
        this.materialGoodsName = materialGoodsName;
        this.materialGoodsCode = materialGoodsCode;
        this.currencyId = currencyId;
        this.exchangeRate = exchangeRate;
        this.unitId = unitId;
        this.unitName = unitName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.mainUnitId = mainUnitId;
        this.mainUnitName = mainUnitName;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.lotNo = lotNo;
        this.expiryDate = expiryDate;
        this.expiryDateStr = expiryDateStr;
        this.repositoryId = repositoryId;
        this.repositoryCode = repositoryCode;
        this.bankAccountDetailId = bankAccountDetailId;
        this.bankAccount = bankAccount;
        this.contractId = contractId;
        this.noBookContract = noBookContract;
        this.costSetId = costSetId;
        this.costSetCode = costSetCode;
        this.expenseItemId = expenseItemId;
        this.expenseItemCode = expenseItemCode;
        this.departmentId = departmentId;
        this.organizationUnitCode = organizationUnitCode;
        this.statisticsCodeId = statisticsCodeId;
        this.statisticsCode = statisticsCode;
        this.budgetItemId = budgetItemId;
        this.budgetItemCode = budgetItemCode;
        this.orderPriority = orderPriority;
    }

    public List<MaterialGoodsSpecificationsLedger> getMaterialGoodsSpecificationsLedgers() {
        return materialGoodsSpecificationsLedgers;
    }

    public void setMaterialGoodsSpecificationsLedgers(List<MaterialGoodsSpecificationsLedger> materialGoodsSpecificationsLedgers) {
        this.materialGoodsSpecificationsLedgers = materialGoodsSpecificationsLedgers;
    }

    public String getExpiryDateStr() {
        return expiryDateStr;
    }

    public void setExpiryDateStr(String expiryDateStr) {
        this.expiryDateStr = expiryDateStr;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public UUID getBankAccountDetailId() {
        return bankAccountDetailId;
    }

    public void setBankAccountDetailId(UUID bankAccountDetailId) {
        this.bankAccountDetailId = bankAccountDetailId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public UUID getMaterialGoodsId() {
        return materialGoodsId;
    }

    public void setMaterialGoodsId(UUID materialGoodsId) {
        this.materialGoodsId = materialGoodsId;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getMainUnitName() {
        return mainUnitName;
    }

    public void setMainUnitName(String mainUnitName) {
        this.mainUnitName = mainUnitName;
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

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public UUID getContractId() {
        return contractId;
    }

    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }

    public String getNoBookContract() {
        return noBookContract;
    }

    public void setNoBookContract(String noBookContract) {
        this.noBookContract = noBookContract;
    }

    public UUID getCostSetId() {
        return costSetId;
    }

    public void setCostSetId(UUID costSetId) {
        this.costSetId = costSetId;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public UUID getExpenseItemId() {
        return expenseItemId;
    }

    public void setExpenseItemId(UUID expenseItemId) {
        this.expenseItemId = expenseItemId;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public String getOrganizationUnitCode() {
        return organizationUnitCode;
    }

    public void setOrganizationUnitCode(String organizationUnitCode) {
        this.organizationUnitCode = organizationUnitCode;
    }

    public UUID getStatisticsCodeId() {
        return statisticsCodeId;
    }

    public void setStatisticsCodeId(UUID statisticsCodeId) {
        this.statisticsCodeId = statisticsCodeId;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public UUID getBudgetItemId() {
        return budgetItemId;
    }

    public void setBudgetItemId(UUID budgetItemId) {
        this.budgetItemId = budgetItemId;
    }

    public String getBudgetItemCode() {
        return budgetItemCode;
    }

    public void setBudgetItemCode(String budgetItemCode) {
        this.budgetItemCode = budgetItemCode;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
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

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public UUID getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(UUID repositoryId) {
        this.repositoryId = repositoryId;
    }
}
