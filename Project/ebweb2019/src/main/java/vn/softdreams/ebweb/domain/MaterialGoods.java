package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * A MaterialGoods.
 */
@Entity
@Table(name = "materialgoods")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "MaterialGoodsDTO",
        classes = {
            @ConstructorResult(
                targetClass = MaterialGoodsDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "materialGoodsType", type = Integer.class),
                    @ColumnResult(name = "isFollow", type = Boolean.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "MaterialGoodsPPInvoiceDTO",
        classes = {
            @ConstructorResult(
                targetClass = MaterialGoodsDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsInStock", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "MaterialGoodsDTOReport",
        classes = {
            @ConstructorResult(
                targetClass = MaterialGoodsDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsType", type = Integer.class),
                    @ColumnResult(name = "materialGoodsCategoryID", type = UUID.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "MGForPPOrderConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = MGForPPOrderConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "materialGoodsInStock", type = BigDecimal.class),
                    @ColumnResult(name = "materialGoodsType", type = Integer.class),
                    @ColumnResult(name = "minimumStock", type = BigDecimal.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "fixedSalePrice", type = BigDecimal.class),
                    @ColumnResult(name = "salePrice1", type = BigDecimal.class),
                    @ColumnResult(name = "salePrice2", type = BigDecimal.class),
                    @ColumnResult(name = "salePrice3", type = BigDecimal.class),
                    @ColumnResult(name = "expenseAccount", type = String.class),
                    @ColumnResult(name = "vatTaxRate", type = Integer.class),
                    @ColumnResult(name = "reponsitoryAccount", type = String.class),
                    @ColumnResult(name = "revenueAccount", type = String.class),
                    @ColumnResult(name = "purchaseDiscountRate", type = BigDecimal.class),
                    @ColumnResult(name = "importTaxRate", type = Integer.class),
                    @ColumnResult(name = "saleDiscountRate", type = BigDecimal.class),
                    @ColumnResult(name = "exportTaxRate", type = BigDecimal.class),
                    @ColumnResult(name = "careerGroupID", type = UUID.class),
                    @ColumnResult(name = "isFollow", type = Boolean.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "MGForPPOrderConvertQuantityDTO",
        classes = {
            @ConstructorResult(
                targetClass = MGForPPOrderConvertQuantityDTO.class,
                columns = {
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "repositoryCode", type = String.class),
                    @ColumnResult(name = "minimumStock", type = BigDecimal.class),
                    @ColumnResult(name = "materialGoodsType", type = Integer.class),
                    @ColumnResult(name = "materialGoodsInStock", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "ObjectDTO",
        classes = {
            @ConstructorResult(
                targetClass = ObjectDTO.class,
                columns = {
                    @ColumnResult(name = "ID", type = UUID.class),
                    @ColumnResult(name = "name", type = String.class),
                }
            )
        }
    )
})

public class MaterialGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "materialgoodscategoryid")
    private UUID materialGoodsCategoryID;

    @NotNull
    @Size(max = 50)
    @Column(name = "materialgoodscode", length = 50, nullable = false)
    private String materialGoodsCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "materialgoodsname", length = 512, nullable = false)
    private String materialGoodsName;

    @Column(name = "materialgoodstype")
    private Integer materialGoodsType;

    @Column(name = "unitid")
    private UUID unitID;

    @Column(name = "minimumstock", precision = 10, scale = 2)
    private BigDecimal minimumStock;

    @Size(max = 512)
    @Column(name = "itemsource", length = 512)
    private String itemSource;

    @Size(max = 512)
    @Column(name = "warranty", length = 512)
    private String warranty;

    @Column(name = "repositoryid")
    private UUID repositoryID;

    @Size(max = 25)
    @Column(name = "reponsitoryaccount", length = 25)
    private String reponsitoryAccount;

    @Size(max = 25)
    @Column(name = "expenseaccount", length = 25)
    private String expenseAccount;

    @Size(max = 25)
    @Column(name = "revenueaccount", length = 25)
    private String revenueAccount;

    @Column(name = "materialgoodsgstid")
    private UUID materialGoodsGSTID;

    @Column(name = "careergroupid")
    private UUID careerGroupID;

    @Column(name = "description")
    private String description;

    @Column(name = "vattaxrate", precision = 10, scale = 2)
    private BigDecimal vatTaxRate;

    @Column(name = "importtaxrate", precision = 10, scale = 2)
    private BigDecimal importTaxRate;

    @Column(name = "exporttaxrate", precision = 10, scale = 2)
    private BigDecimal exportTaxRate;

    @Column(name = "salediscountrate", precision = 10, scale = 2)
    private BigDecimal saleDiscountRate;

    @Column(name = "purchasediscountrate", precision = 10, scale = 2)
    private BigDecimal purchaseDiscountRate;

    @Column(name = "fixedsaleprice", precision = 10, scale = 2)
    private BigDecimal fixedSalePrice;

    @Column(name = "saleprice1", precision = 10, scale = 2)
    private BigDecimal salePrice1;

    @Column(name = "saleprice2", precision = 10, scale = 2)
    private BigDecimal salePrice2;

    @Column(name = "saleprice3", precision = 10, scale = 2)
    private BigDecimal salePrice3;

    @NotNull
    @Column(name = "isactive", nullable = false)
    private Boolean isActive;

//    @NotNull
    @Column(name = "isfollow", nullable = false)
    private Boolean isFollow;

    @NotNull
    @Column(name = "issecurity", nullable = false)
    private Boolean isSecurity;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "materialgoodsid")
    private Set<MaterialGoodsAssembly> materialGoodsAssembly = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "materialgoodsid")
    private Set<MaterialGoodsPurchasePrice> materialGoodsPurchasePrice = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "materialgoodsid")
    private Set<MaterialGoodsSpecifications> materialGoodsSpecifications = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "materialgoodsid")
    private Set<SaleDiscountPolicy> saleDiscountPolicy = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "materialgoodsid")
    private Set<MaterialGoodsConvertUnit> materialGoodsConvertUnits = new HashSet<>();

    @Transient
    @JsonSerialize
    private List<VoucherRefCatalogDTO> voucherRefCatalogDTOS;

    public Set<MaterialGoodsAssembly> getMaterialGoodsAssembly() {
        return materialGoodsAssembly;
    }

    public void cleanMaterialGoodsAssembly() {
        this.materialGoodsAssembly.clear();
    }

    public void addMaterialGoodsAssembly(MaterialGoodsAssembly materialGoodsAssembly) {
        if (this.materialGoodsAssembly == null) {
            cleanMaterialGoodsAssembly();
        }
        this.materialGoodsAssembly.add(materialGoodsAssembly);
    }

    public void setMaterialGoodsAssembly(Set<MaterialGoodsAssembly> materialGoodsAssemblies) {
        this.materialGoodsAssembly = materialGoodsAssemblies;
    }


    public Set<MaterialGoodsSpecifications> getMaterialGoodsSpecifications() {
        return materialGoodsSpecifications;
    }

    public void setMaterialGoodsSpecifications(Set<MaterialGoodsSpecifications> materialGoodsSpecifications) {
        this.materialGoodsSpecifications = materialGoodsSpecifications;
    }

    public void cleanMaterialGoodsSpecifications() {
        this.materialGoodsSpecifications.clear();
    }

    public void addMaterialGoodsSpecifications(MaterialGoodsSpecifications materialGoodsSpecifications) {
        if (this.materialGoodsSpecifications == null) {
            this.materialGoodsSpecifications = new HashSet<>();
        }
        this.materialGoodsSpecifications.add(materialGoodsSpecifications);
    }

    public Set<SaleDiscountPolicy> getSaleDiscountPolicy() {
        return saleDiscountPolicy;
    }

    public void setSaleDiscountPolicy(Set<SaleDiscountPolicy> saleDiscountPolicis) {
        this.saleDiscountPolicy = saleDiscountPolicis;
    }

    public void addSaleDiscountPolicy(SaleDiscountPolicy saleDiscountPolicy) {
        if (this.saleDiscountPolicy == null) {
            this.saleDiscountPolicy = new HashSet<>();
        }
        this.saleDiscountPolicy.add(saleDiscountPolicy);
    }

    public void cleanSaleDiscountPolicy() {
        saleDiscountPolicy.clear();
    }

    public Set<MaterialGoodsConvertUnit> getMaterialGoodsConvertUnits() {
        return materialGoodsConvertUnits;
    }

    public void setMaterialGoodsConvertUnits(Set<MaterialGoodsConvertUnit> materialGoodsConvertUnits) {
        this.materialGoodsConvertUnits = materialGoodsConvertUnits;
    }

    public void cleanMaterialGoodsConvertUnit() {
        this.materialGoodsConvertUnits.clear();
    }

    public void addMaterialGoodsConvertUnit(MaterialGoodsConvertUnit materialGoodsConvertUnit) {
        if (this.materialGoodsConvertUnits == null) {
            this.materialGoodsConvertUnits = new HashSet<>();
        }
        materialGoodsConvertUnits.add(materialGoodsConvertUnit);
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public MaterialGoods branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public UUID getMaterialGoodsCategoryID() {
        return materialGoodsCategoryID;
    }

    public MaterialGoods materialGoodsCategoryID(UUID materialGoodsCategoryID) {
        this.materialGoodsCategoryID = materialGoodsCategoryID;
        return this;
    }

    public void setMaterialGoodsCategoryID(UUID materialGoodsCategoryID) {
        this.materialGoodsCategoryID = materialGoodsCategoryID;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public MaterialGoods materialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
        return this;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public MaterialGoods materialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
        return this;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public Integer getMaterialGoodsType() {
        return materialGoodsType;
    }

    public MaterialGoods materialGoodsType(Integer materialGoodsType) {
        this.materialGoodsType = materialGoodsType;
        return this;
    }

    public void setMaterialGoodsType(Integer materialGoodsType) {
        this.materialGoodsType = materialGoodsType;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public MaterialGoods unitID(UUID unitID) {
        this.unitID = unitID;
        return this;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public BigDecimal getMinimumStock() {
        return minimumStock;
    }

    public MaterialGoods minimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
        return this;
    }

    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public String getItemSource() {
        return itemSource;
    }

    public MaterialGoods itemSource(String itemSource) {
        this.itemSource = itemSource;
        return this;
    }

    public void setItemSource(String itemSource) {
        this.itemSource = itemSource;
    }

    public Boolean getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Boolean follow) {
        isFollow = follow;
    }

    public String getWarranty() {
        return warranty;
    }

    public MaterialGoods warranty(String warranty) {
        this.warranty = warranty;
        return this;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public MaterialGoods repositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
        return this;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public String getReponsitoryAccount() {
        return reponsitoryAccount;
    }

    public MaterialGoods reponsitoryAccount(String reponsitoryAccount) {
        this.reponsitoryAccount = reponsitoryAccount;
        return this;
    }

    public void setReponsitoryAccount(String reponsitoryAccount) {
        this.reponsitoryAccount = reponsitoryAccount;
    }

    public String getExpenseAccount() {
        return expenseAccount;
    }

    public MaterialGoods expenseAccount(String expenseAccount) {
        this.expenseAccount = expenseAccount;
        return this;
    }

    public void setExpenseAccount(String expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public String getRevenueAccount() {
        return revenueAccount;
    }

    public MaterialGoods revenueAccount(String revenueAccount) {
        this.revenueAccount = revenueAccount;
        return this;
    }

    public void setRevenueAccount(String revenueAccount) {
        this.revenueAccount = revenueAccount;
    }

    public UUID getMaterialGoodsGSTID() {
        return materialGoodsGSTID;
    }

    public MaterialGoods materialGoodsGSTID(UUID materialGoodsGSTID) {
        this.materialGoodsGSTID = materialGoodsGSTID;
        return this;
    }

    public void setMaterialGoodsGSTID(UUID materialGoodsGSTID) {
        this.materialGoodsGSTID = materialGoodsGSTID;
    }

    public UUID getCareerGroupID() {
        return careerGroupID;
    }

    public MaterialGoods careerGroupID(UUID careerGroupID) {
        this.careerGroupID = careerGroupID;
        return this;
    }

    public void setCareerGroupID(UUID careerGroupID) {
        this.careerGroupID = careerGroupID;
    }

    public BigDecimal getVatTaxRate() {
        return vatTaxRate;
    }

    public MaterialGoods vatTaxRate(BigDecimal vatTaxRate) {
        this.vatTaxRate = vatTaxRate;
        return this;
    }

    public void setVatTaxRate(BigDecimal vatTaxRate) {
        this.vatTaxRate = vatTaxRate;
    }

    public BigDecimal getImportTaxRate() {
        return importTaxRate;
    }

    public MaterialGoods importTaxRate(BigDecimal importTaxRate) {
        this.importTaxRate = importTaxRate;
        return this;
    }

    public void setImportTaxRate(BigDecimal importTaxRate) {
        this.importTaxRate = importTaxRate;
    }

    public BigDecimal getExportTaxRate() {
        return exportTaxRate;
    }

    public MaterialGoods exportTaxRate(BigDecimal exportTaxRate) {
        this.exportTaxRate = exportTaxRate;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExportTaxRate(BigDecimal exportTaxRate) {
        this.exportTaxRate = exportTaxRate;
    }

    public BigDecimal getSaleDiscountRate() {
        return saleDiscountRate;
    }

    public MaterialGoods saleDiscountRate(BigDecimal saleDiscountRate) {
        this.saleDiscountRate = saleDiscountRate;
        return this;
    }

    public void setSaleDiscountRate(BigDecimal saleDiscountRate) {
        this.saleDiscountRate = saleDiscountRate;
    }

    public BigDecimal getPurchaseDiscountRate() {
        return purchaseDiscountRate;
    }

    public MaterialGoods purchaseDiscountRate(BigDecimal purchaseDiscountRate) {
        this.purchaseDiscountRate = purchaseDiscountRate;
        return this;
    }

    public void setPurchaseDiscountRate(BigDecimal purchaseDiscountRate) {
        this.purchaseDiscountRate = purchaseDiscountRate;
    }

    public BigDecimal getFixedSalePrice() {
        return fixedSalePrice;
    }

    public MaterialGoods fixedSalePrice(BigDecimal fixedSalePrice) {
        this.fixedSalePrice = fixedSalePrice;
        return this;
    }

    public void setFixedSalePrice(BigDecimal fixedSalePrice) {
        this.fixedSalePrice = fixedSalePrice;
    }

    public BigDecimal getSalePrice1() {
        return salePrice1;
    }

    public MaterialGoods salePrice1(BigDecimal salePrice1) {
        this.salePrice1 = salePrice1;
        return this;
    }

    public void setSalePrice1(BigDecimal salePrice1) {
        this.salePrice1 = salePrice1;
    }

    public BigDecimal getSalePrice2() {
        return salePrice2;
    }

    public MaterialGoods salePrice2(BigDecimal salePrice2) {
        this.salePrice2 = salePrice2;
        return this;
    }

    public void setSalePrice2(BigDecimal salePrice2) {
        this.salePrice2 = salePrice2;
    }

    public BigDecimal getSalePrice3() {
        return salePrice3;
    }

    public MaterialGoods salePrice3(BigDecimal salePrice3) {
        this.salePrice3 = salePrice3;
        return this;
    }

    public void setSalePrice3(BigDecimal salePrice3) {
        this.salePrice3 = salePrice3;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public MaterialGoods isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsSecurity() {
        return isSecurity;
    }

    public MaterialGoods isSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
        return this;
    }

    public void setIsSecurity(Boolean isSecurity) {
        this.isSecurity = isSecurity;
    }


    public Set<MaterialGoodsPurchasePrice> getMaterialGoodsPurchasePrice() {
        return materialGoodsPurchasePrice;
    }

    public void setMaterialGoodsPurchasePrice(Set<MaterialGoodsPurchasePrice> materialGoodsPurchasePrices) {
        this.materialGoodsPurchasePrice = materialGoodsPurchasePrices;
    }

    public void addMaterialGoodsPurchasePrice(MaterialGoodsPurchasePrice materialGoodsPurchasePrice) {
        if (this.materialGoodsPurchasePrice == null) {
            this.materialGoodsPurchasePrice = new HashSet<>();
        }
        this.materialGoodsPurchasePrice.add(materialGoodsPurchasePrice);
    }

    public void cleanMaterialGoodsPurchasePrice() {
        this.materialGoodsPurchasePrice.clear();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaterialGoods materialGoods = (MaterialGoods) o;
        if (materialGoods.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialGoods.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialGoods{" +
            "id=" + getId() +
            ", branchID=" + getBranchID() +
            ", materialGoodsCategoryID=" + getMaterialGoodsCategoryID() +
            ", materialGoodsCode='" + getMaterialGoodsCode() + "'" +
            ", materialGoodsName='" + getMaterialGoodsName() + "'" +
            ", materialGoodsType=" + getMaterialGoodsType() +
            ", unitID=" + getUnitID() +
            ", minimumStock=" + getMinimumStock() +
            ", itemSource='" + getItemSource() + "'" +
            ", warranty='" + getWarranty() + "'" +
            ", repositoryID=" + getRepositoryID() +
            ", reponsitoryAccount='" + getReponsitoryAccount() + "'" +
            ", expenseAccount='" + getExpenseAccount() + "'" +
            ", revenueAccount='" + getRevenueAccount() + "'" +
            ", materialGoodsGSTID=" + getMaterialGoodsGSTID() +
            ", careerGroupID=" + getCareerGroupID() +
            ", vatTaxRate=" + getVatTaxRate() +
            ", importTaxRate=" + getImportTaxRate() +
            ", exportTaxRate=" + getExportTaxRate() +
            ", saleDiscountRate=" + getSaleDiscountRate() +
            ", purchaseDiscountRate=" + getPurchaseDiscountRate() +
            ", fixedSalePrice=" + getFixedSalePrice() +
            ", salePrice1=" + getSalePrice1() +
            ", salePrice2=" + getSalePrice2() +
            ", salePrice3=" + getSalePrice3() +
            ", isActive='" + isIsActive() + "'" +
            ", isSecurity='" + isIsSecurity() + "'" +
            ", isFollow='" + getIsFollow() + "'" +
            "}";
    }

    public MaterialGoods(UUID id) {
        this.id = id;
    }

    public MaterialGoods() {
    }

    public List<VoucherRefCatalogDTO> getVoucherRefCatalogDTOS() {
        return voucherRefCatalogDTOS;
    }

    public void setVoucherRefCatalogDTOS(List<VoucherRefCatalogDTO> voucherRefCatalogDTOS) {
        this.voucherRefCatalogDTOS = voucherRefCatalogDTOS;
    }

}
