package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class MGForPPOrderConvertDTO {

    private UUID id;
    private UUID companyID;
    private String materialGoodsCode;
    private String materialGoodsName;
    private BigDecimal materialGoodsInStock;
    private Integer materialGoodsType;
    private BigDecimal minimumStock;
    private UUID repositoryID;
    private UUID unitID;
    private BigDecimal fixedSalePrice;
    private BigDecimal salePrice1;
    private BigDecimal salePrice2;
    private BigDecimal salePrice3;
    private String expenseAccount;
    private Integer vatTaxRate;
    private String reponsitoryAccount;
    private String revenueAccount;
    private BigDecimal purchaseDiscountRate;
    private Integer importTaxRate;
    private BigDecimal saleDiscountRate;
    private BigDecimal exportTaxRate;
    private UUID careerGroupID;
    private Boolean isFollow;

    public MGForPPOrderConvertDTO() {
    }


    public MGForPPOrderConvertDTO(UUID id, UUID companyID, String materialGoodsCode, String materialGoodsName, BigDecimal materialGoodsInStock, Integer materialGoodsType, BigDecimal minimumStock, UUID repositoryID, UUID unitID, BigDecimal fixedSalePrice, BigDecimal salePrice1, BigDecimal salePrice2, BigDecimal salePrice3, String expenseAccount, Integer vatTaxRate, String reponsitoryAccount, String revenueAccount, BigDecimal purchaseDiscountRate, Integer importTaxRate,
                                  BigDecimal saleDiscountRate, BigDecimal exportTaxRate, UUID careerGroupID, Boolean isFollow) {

        this.id = id;
        this.companyID = companyID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.materialGoodsInStock = materialGoodsInStock;
        this.materialGoodsType = materialGoodsType;
        this.minimumStock = minimumStock;
        this.repositoryID = repositoryID;
        this.unitID = unitID;
        this.fixedSalePrice = fixedSalePrice;
        this.salePrice1 = salePrice1;
        this.salePrice2 = salePrice2;
        this.salePrice3 = salePrice3;
        this.expenseAccount = expenseAccount;
        this.vatTaxRate = vatTaxRate;
        this.reponsitoryAccount = reponsitoryAccount;
        this.revenueAccount = revenueAccount;
        this.purchaseDiscountRate = purchaseDiscountRate;
        this.importTaxRate = importTaxRate;
        this.saleDiscountRate = saleDiscountRate;
        this.exportTaxRate = exportTaxRate;
        this.careerGroupID = careerGroupID;
        this.isFollow = isFollow;
    }

    public MGForPPOrderConvertDTO(UUID id, UUID companyID, String materialGoodsCode, String materialGoodsName, BigDecimal materialGoodsInStock, Integer materialGoodsType, UUID repositoryID, UUID unitID) {
        this.id = id;
        this.companyID = companyID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.materialGoodsInStock = materialGoodsInStock;
        this.materialGoodsType = materialGoodsType;
        this.repositoryID = repositoryID;
        this.unitID = unitID;
    }

    public MGForPPOrderConvertDTO(UUID id, UUID companyID, String materialGoodsCode, String materialGoodsName, BigDecimal materialGoodsInStock, Integer materialGoodsType, UUID repositoryID, UUID unitID, BigDecimal fixedSalePrice, BigDecimal salePrice1, BigDecimal salePrice2, BigDecimal salePrice3, String expenseAccount) {
        this.id = id;
        this.companyID = companyID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.materialGoodsInStock = materialGoodsInStock;
        this.materialGoodsType = materialGoodsType;
        this.repositoryID = repositoryID;
        this.unitID = unitID;
        this.fixedSalePrice = fixedSalePrice;
        this.salePrice1 = salePrice1;
        this.salePrice2 = salePrice2;
        this.salePrice3 = salePrice3;
        this.expenseAccount = expenseAccount;
    }

    public MGForPPOrderConvertDTO(UUID id, UUID companyID, String materialGoodsCode, String materialGoodsName, BigDecimal materialGoodsInStock, Integer materialGoodsType, UUID repositoryID, UUID unitID, BigDecimal fixedSalePrice, BigDecimal salePrice1, BigDecimal salePrice2, BigDecimal salePrice3) {
        this.id = id;
        this.companyID = companyID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.materialGoodsInStock = materialGoodsInStock;
        this.materialGoodsType = materialGoodsType;
        this.repositoryID = repositoryID;
        this.unitID = unitID;
        this.fixedSalePrice = fixedSalePrice;
        this.salePrice1 = salePrice1;
        this.salePrice2 = salePrice2;
        this.salePrice3 = salePrice3;
    }


    public String getRevenueAccount() {
        return revenueAccount;
    }

    public void setRevenueAccount(String revenueAccount) {
        this.revenueAccount = revenueAccount;
    }

    public BigDecimal getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public String getExpenseAccount() {
        return expenseAccount;
    }

    public void setExpenseAccount(String expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public Integer getMaterialGoodsType() {
        return materialGoodsType;
    }

    public void setMaterialGoodsType(Integer materialGoodsType) {
        this.materialGoodsType = materialGoodsType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public BigDecimal getMaterialGoodsInStock() {
        return materialGoodsInStock;
    }

    public void setMaterialGoodsInStock(BigDecimal materialGoodsInStock) {
        this.materialGoodsInStock = materialGoodsInStock;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public BigDecimal getFixedSalePrice() {
        return fixedSalePrice;
    }

    public void setFixedSalePrice(BigDecimal fixedSalePrice) {
        this.fixedSalePrice = fixedSalePrice;
    }

    public BigDecimal getSalePrice1() {
        return salePrice1;
    }

    public void setSalePrice1(BigDecimal salePrice1) {
        this.salePrice1 = salePrice1;
    }

    public BigDecimal getSalePrice2() {
        return salePrice2;
    }

    public void setSalePrice2(BigDecimal salePrice2) {
        this.salePrice2 = salePrice2;
    }

    public BigDecimal getSalePrice3() {
        return salePrice3;
    }

    public void setSalePrice3(BigDecimal salePrice3) {
        this.salePrice3 = salePrice3;
    }

    public Integer getVatTaxRate() {
        return vatTaxRate;
    }

    public void setVatTaxRate(Integer vatTaxRate) {
        this.vatTaxRate = vatTaxRate;
    }

    public String getReponsitoryAccount() {
        return reponsitoryAccount;
    }

    public void setReponsitoryAccount(String reponsitoryAccount) {
        this.reponsitoryAccount = reponsitoryAccount;
    }

    public BigDecimal getPurchaseDiscountRate() {
        return purchaseDiscountRate;
    }

    public void setPurchaseDiscountRate(BigDecimal purchaseDiscountRate) {
        this.purchaseDiscountRate = purchaseDiscountRate;
    }

    public Integer getImportTaxRate() {
        return importTaxRate;
    }

    public void setImportTaxRate(Integer importTaxRate) {
        this.importTaxRate = importTaxRate;
    }

    public BigDecimal getSaleDiscountRate() {
        return saleDiscountRate;
    }

    public void setSaleDiscountRate(BigDecimal saleDiscountRate) {
        this.saleDiscountRate = saleDiscountRate;
    }

    public BigDecimal getExportTaxRate() {
        return exportTaxRate;
    }

    public void setExportTaxRate(BigDecimal exportTaxRate) {
        this.exportTaxRate = exportTaxRate;
    }

    public UUID getCareerGroupID() {
        return careerGroupID;
    }

    public void setCareerGroupID(UUID careerGroupID) {
        this.careerGroupID = careerGroupID;
    }

    public Boolean isIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Boolean isFollow) {
        this.isFollow = isFollow;
    }
}
