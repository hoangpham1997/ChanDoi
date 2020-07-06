package vn.softdreams.ebweb.web.rest.dto;
import vn.softdreams.ebweb.domain.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RSTransferDetailsDTO {
    private UUID id;
    private UUID rsTransferID;
    private UUID materialGoodsID;
    private String materialGoodsCode;
    private String description;
    private String fromRepositoryCode;
    private String toRepositoryCode;
    private String debitAccount;
    private String creditAccount;
    private String unitName;
    private BigDecimal quantity;
    private BigDecimal unitPriceOriginal;
    private BigDecimal amount;
    private BigDecimal oWPrice;
    private BigDecimal oWAmount;
    private String lotNo;
    private LocalDate expiryDate;
    private String expenseItemCode;
    private String costSetCode;
    private String budgetItemCode;
    private String organizationUnitCode;
    private String statisticsCode;
    private Integer orderPriority;
    private UUID fromRepositoryID;
    private UUID toRepositoryID;
    private UUID unitID;
    private UUID mainUnitID;
    private BigDecimal mainQuantity;
    private BigDecimal mainUnitPrice;
    private BigDecimal mainConvertRate;
    private String formula;

    public RSTransferDetailsDTO() {
    }

    public RSTransferDetailsDTO(UUID id, UUID rsTransferID, UUID materialGoodsID, String debitAccount, String creditAccount, BigDecimal quantity, BigDecimal unitPriceOriginal, BigDecimal oWPrice, BigDecimal oWAmount, UUID fromRepositoryID, UUID toRepositoryID, UUID unitID, UUID mainUnitID, BigDecimal mainQuantity, BigDecimal mainUnitPrice, BigDecimal mainConvertRate, String formula) {
        this.id = id;
        this.rsTransferID = rsTransferID;
        this.materialGoodsID = materialGoodsID;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.quantity = quantity;
        this.unitPriceOriginal = unitPriceOriginal;
        this.oWPrice = oWPrice;
        this.oWAmount = oWAmount;
        this.fromRepositoryID = fromRepositoryID;
        this.toRepositoryID = toRepositoryID;
        this.unitID = unitID;
        this.mainUnitID = mainUnitID;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
    }

    public RSTransferDetailsDTO(UUID id, UUID rsTransferID, UUID materialGoodsID, String materialGoodsCode, String description, String fromRepositoryCode, String toRepositoryCode, String debitAccount, String creditAccount, String unitName, BigDecimal quantity, BigDecimal unitPriceOriginal, BigDecimal amount, BigDecimal oWPrice, BigDecimal oWAmount, String lotNo, LocalDate expiryDate, String expenseItemCode, String costSetCode, String budgetItemCode, String organizationUnitCode, String statisticsCode, Integer orderPriority, UUID fromRepositoryID) {
        this.id = id;
        this.rsTransferID = rsTransferID;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.description = description;
        this.fromRepositoryCode = fromRepositoryCode;
        this.toRepositoryCode = toRepositoryCode;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.unitName = unitName;
        this.quantity = quantity;
        this.unitPriceOriginal = unitPriceOriginal;
        this.amount = amount;
        this.oWPrice = oWPrice;
        this.oWAmount = oWAmount;
        this.lotNo = lotNo;
        this.expiryDate = expiryDate;
        this.expenseItemCode = expenseItemCode;
        this.costSetCode = costSetCode;
        this.budgetItemCode = budgetItemCode;
        this.organizationUnitCode = organizationUnitCode;
        this.statisticsCode = statisticsCode;
        this.orderPriority = orderPriority;
        this.fromRepositoryID = fromRepositoryID;
    }

    public UUID getFromRepositoryID() {
        return fromRepositoryID;
    }

    public void setFromRepositoryID(UUID fromRepositoryID) {
        this.fromRepositoryID = fromRepositoryID;
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

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getBudgetItemCode() {
        return budgetItemCode;
    }

    public void setBudgetItemCode(String budgetItemCode) {
        this.budgetItemCode = budgetItemCode;
    }

    public String getOrganizationUnitCode() {
        return organizationUnitCode;
    }

    public void setOrganizationUnitCode(String organizationUnitCode) {
        this.organizationUnitCode = organizationUnitCode;
    }

    public String getStatisticsCode() {
        return statisticsCode;
    }

    public void setStatisticsCode(String statisticsCode) {
        this.statisticsCode = statisticsCode;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public UUID getToRepositoryID() {
        return toRepositoryID;
    }

    public void setToRepositoryID(UUID toRepositoryID) {
        this.toRepositoryID = toRepositoryID;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
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
}
