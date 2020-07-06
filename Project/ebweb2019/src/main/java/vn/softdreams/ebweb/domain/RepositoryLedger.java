package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.models.auth.In;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.ebweb.service.dto.PPPayVendorBillDTO;
import vn.softdreams.ebweb.service.dto.PPPayVendorDTO;
import vn.softdreams.ebweb.service.dto.Report.SoChiTietVatLieuDTO;
import vn.softdreams.ebweb.service.dto.Report.TheKhoDTO;
import vn.softdreams.ebweb.service.dto.Report.TongHopChiTietVatLieuDTO;
import vn.softdreams.ebweb.service.dto.Report.TongHopTonKhoDTO;
import vn.softdreams.ebweb.web.rest.dto.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A RepositoryLedger.
 */
@Entity
@DynamicUpdate
@Table(name = "repositoryledger")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "LotNoDTOSA",
        classes = {
            @ConstructorResult(
                targetClass = LotNoDTO.class,
                columns = {
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                    @ColumnResult(name = "totalIWQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "totalOWQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "totalQuantityBalance", type = BigDecimal.class)
                }
            )
        }
    ), @SqlResultSetMapping(
        name = "CalculateOWPriceDTO",
        classes = {
            @ConstructorResult(
                targetClass = CalculateOWPriceDTO.class,
                columns = {
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class)
                }
            )
        }
    ), @SqlResultSetMapping(
        name = "IWVoucherDTO",
        classes = {
            @ConstructorResult(
                targetClass = IWVoucherDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "detailID", type = UUID.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "repositoryCode", type = String.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "lotNo", type = String.class),
                    @ColumnResult(name = "expiryDate", type = LocalDate.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "mainUnitName", type = String.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "typeID", type = Integer.class)
                }
            )
        }
    ), @SqlResultSetMapping(
    name = "SoChiTietVatLieuDTO",
    classes = {
        @ConstructorResult(
            targetClass = SoChiTietVatLieuDTO.class,
            columns = {
                @ColumnResult(name = "refID", type = UUID.class),
                @ColumnResult(name = "detailID", type = UUID.class),
                @ColumnResult(name = "refType", type = Integer.class),
                @ColumnResult(name = "repositoryID", type = UUID.class),
                @ColumnResult(name = "repositoryCode", type = String.class),
                @ColumnResult(name = "repositoryName", type = String.class),
                @ColumnResult(name = "materialGoodsID", type = UUID.class),
                @ColumnResult(name = "materialGoodsCode", type = String.class),
                @ColumnResult(name = "materialGoodsName", type = String.class),
                @ColumnResult(name = "postedDate", type = LocalDate.class),
                @ColumnResult(name = "refDate", type = LocalDateTime.class),
                @ColumnResult(name = "refNo", type = String.class),
                @ColumnResult(name = "reason", type = String.class),
                @ColumnResult(name = "correspondingAccountNumber", type = String.class),
                @ColumnResult(name = "unitName", type = String.class),
                @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                @ColumnResult(name = "inwardQuantity", type = BigDecimal.class),
                @ColumnResult(name = "inwardAmount", type = BigDecimal.class),
                @ColumnResult(name = "outwardQuantity", type = BigDecimal.class),
                @ColumnResult(name = "outwardAmount", type = BigDecimal.class),
                @ColumnResult(name = "closingQuantity", type = BigDecimal.class),
                @ColumnResult(name = "closingAmount", type = BigDecimal.class),
                @ColumnResult(name = "rowNum", type = Integer.class),
                @ColumnResult(name = "iNRefOrder", type = LocalDateTime.class),
                @ColumnResult(name = "sortOrder", type = Integer.class),
                @ColumnResult(name = "isShowOnReport", type = Boolean.class)
            }
        )
    }
), @SqlResultSetMapping(
    name = "TheKhoDTO",
    classes = {
        @ConstructorResult(
            targetClass = TheKhoDTO.class,
            columns = {
                @ColumnResult(name = "refID", type = UUID.class),
                @ColumnResult(name = "refType", type = Integer.class),
                @ColumnResult(name = "materialGoodsID", type = UUID.class),
                @ColumnResult(name = "materialGoodsCode", type = String.class),
                @ColumnResult(name = "materialGoodsName", type = String.class),
                @ColumnResult(name = "postedDate", type = LocalDate.class),
                @ColumnResult(name = "refDate", type = LocalDateTime.class),
                @ColumnResult(name = "refNo", type = String.class),
                @ColumnResult(name = "reason", type = String.class),
                @ColumnResult(name = "unitName", type = String.class),
                @ColumnResult(name = "inwardQuantity", type = BigDecimal.class),
                @ColumnResult(name = "outwardQuantity", type = BigDecimal.class),
                @ColumnResult(name = "closingQuantity", type = BigDecimal.class),
                @ColumnResult(name = "rowNum", type = Integer.class)
            }
        )
    }
), @SqlResultSetMapping(
    name = "TongHopChiTietVatLieuDTO",
    classes = {
        @ConstructorResult(
            targetClass = TongHopChiTietVatLieuDTO.class,
            columns = {
                @ColumnResult(name = "materialGoodsID", type = UUID.class),
                @ColumnResult(name = "materialGoodsCode", type = String.class),
                @ColumnResult(name = "materialGoodsName", type = String.class),
                @ColumnResult(name = "account", type = String.class),
                @ColumnResult(name = "amountOpening", type = BigDecimal.class),
                @ColumnResult(name = "iWAmount", type = BigDecimal.class),
                @ColumnResult(name = "oWAmount", type = BigDecimal.class),
                @ColumnResult(name = "amountClosing", type = BigDecimal.class)
            }
        )
    }
), @SqlResultSetMapping(
    name = "TongHopTonKhoDTO",
    classes = {
        @ConstructorResult(
            targetClass = TongHopTonKhoDTO.class,
            columns = {
                @ColumnResult(name = "repositoryID", type = UUID.class),
                @ColumnResult(name = "repositoryCode", type = String.class),
                @ColumnResult(name = "repositoryName", type = String.class),
                @ColumnResult(name = "materialGoodsID", type = UUID.class),
                @ColumnResult(name = "unitName", type = String.class),
                @ColumnResult(name = "materialGoodsCode", type = String.class),
                @ColumnResult(name = "materialGoodsName", type = String.class),
                @ColumnResult(name = "openingQuantity", type = BigDecimal.class),
                @ColumnResult(name = "openingAmount", type = BigDecimal.class),
                @ColumnResult(name = "iwQuantity", type = BigDecimal.class),
                @ColumnResult(name = "iwAmount", type = BigDecimal.class),
                @ColumnResult(name = "owQuantity", type = BigDecimal.class),
                @ColumnResult(name = "owAmount", type = BigDecimal.class),
                @ColumnResult(name = "closingQuantity", type = BigDecimal.class),
                @ColumnResult(name = "closingAmount", type = BigDecimal.class)
            }
        )
    }
), @SqlResultSetMapping(
    name = "RepositoryLedgerDTO",
    classes = {
        @ConstructorResult(
            targetClass = RepositoryLedgerDTO.class,
            columns = {
                @ColumnResult(name = "referenceID", type = UUID.class),
                @ColumnResult(name = "noFBook", type = String.class),
                @ColumnResult(name = "noMBook", type = String.class),
                @ColumnResult(name = "postedDate", type = LocalDate.class),
                @ColumnResult(name = "date", type = LocalDate.class),
                @ColumnResult(name = "typeID", type = Integer.class),
                @ColumnResult(name = "reason", type = String.class)
            }
        )
    }
), @SqlResultSetMapping(
    name = "QuantityCompleteDTO",
    classes = {
        @ConstructorResult(
            targetClass = RepositoryLedgerDTO.class,
            columns = {
                @ColumnResult(name = "costSetID", type = UUID.class),
                @ColumnResult(name = "materialGoodsID", type = UUID.class),
                @ColumnResult(name = "quantity", type = BigDecimal.class)
            }
        )
    }
)
})
public class RepositoryLedger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "detailid")
    private UUID detailID;

    @NotNull
    @Column(name = "referenceid", nullable = false)
    private UUID referenceID;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "posteddate", nullable = false)
    private LocalDate postedDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Size(max = 25)
    @Column(name = "nofbook", length = 25)
    private String noFBook;

//    @NotNull
    @Size(max = 25)
    @Column(name = "nombook", length = 25)
    private String noMBook;

//    @NotNull
    @Size(max = 25)
    @Column(name = "account", length = 25)
    private String account;

//    @NotNull
    @Size(max = 25)
    @Column(name = "accountcorresponding")
    private String accountCorresponding;

//    @NotNull
    @Column(name = "repositoryid", nullable = false)
    private UUID repositoryID;

//    @NotNull
    @Size(max = 25)
    @Column(name = "repositorycode", length = 25, nullable = false)
    private String repositoryCode;

//    @NotNull
    @Size(max = 512)
    @Column(name = "repositoryname", length = 512, nullable = false)
    private String RepositoryName;

    @Column(name = "materialgoodsid", nullable = false)
    private UUID materialGoodsID;

    @Size(max = 50)
    @Column(name = "materialgoodscode", length = 50, nullable = false)
    private String materialGoodsCode;

    @Size(max = 512)
    @Column(name = "materialgoodsname", length = 512, nullable = false)
    private String materialGoodsName;

    @Column(name = "unitid")
    private UUID unitID;

    @Column(name = "unitprice", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "iwquantity", precision = 10, scale = 2)
    private BigDecimal iwQuantity;

    @Column(name = "owquantity", precision = 10, scale = 2)
    private BigDecimal owQuantity;

    @Column(name = "iwamount", precision = 10, scale = 2)
    private BigDecimal iwAmount;

    @Column(name = "owamount", precision = 10, scale = 2)
    private BigDecimal owAmount;

//    @NotNull
    @Column(name = "mainunitid", nullable = false)
    private UUID mainUnitID;

    @Column(name = "mainunitprice", precision = 10, scale = 2)
    private BigDecimal mainUnitPrice;

    @Column(name = "mainiwquantity", precision = 10, scale = 2)
    private BigDecimal mainIWQuantity;

    @Column(name = "mainowquantity", precision = 10, scale = 2)
    private BigDecimal mainOWQuantity;

    @Column(name = "mainconvertrate", precision = 10, scale = 2)
    private BigDecimal mainConvertRate;

    @Size(max = 25)
    @Column(name = "formula", length = 25)
    private String formula;

//    @NotNull
    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

//    @NotNull
    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "expirydate")
    private LocalDate expiryDate;

//    @NotNull
    @Size(max = 50)
    @Column(name = "lotno", length = 50)
    private String lotNo;

    @Column(name = "budgetitemid")
    private UUID budgetItemID;

    @Column(name = "costsetid")
    private UUID costSetID;

    @Column(name = "statisticscodeid")
    private UUID statisticsCodeID;

    @Column(name = "expenseitemid")
    private UUID expenseItemID;

    @Column(name = "orderpriority", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer orderPriority;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "confrontid")
    private UUID confrontID;

    @Column(name = "confrontdetailid")
    private UUID confrontDetailID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Boolean used;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public RepositoryLedger() {
    }

    public RepositoryLedger(UUID companyID, UUID branchID, UUID detailID, @NotNull UUID referenceID, LocalDate date,
                            LocalDate postedDate, Integer typeLedger, @NotNull @Size(max = 25) String noFBook,
                            @NotNull @Size(max = 25) String noMBook, @NotNull @Size(max = 25) String account,
                            @NotNull @Size(max = 25) String accountCorresponding, @NotNull UUID repositoryID,
                            @NotNull @Size(max = 25) String repositoryCode, @NotNull @Size(max = 512) String repositoryName,
                            @NotNull UUID materialGoodsID, @NotNull @Size(max = 25) String materialGoodsCode,
                            @NotNull @Size(max = 512) String materialGoodsName, UUID unitID, BigDecimal unitPrice,
                            BigDecimal iwQuantity, BigDecimal owQuantity, BigDecimal iwAmount, BigDecimal owAmount,
                            UUID mainUnitID, BigDecimal mainUnitPrice, BigDecimal mainIWQuantity, BigDecimal mainOWQuantity,
                            BigDecimal mainConvertRate, @NotNull @Size(max = 25) String formula, @NotNull @Size(max = 512) String reason,
                            @NotNull @Size(max = 512) String description, LocalDate expiryDate,
                            @NotNull @Size(max = 50) String lotNo, UUID budgetItemID, UUID costSetID,
                            UUID statisticsCodeID, UUID expenseItemID, Integer orderPriority) {
        this.companyID = companyID;
        this.branchID = branchID;
        this.detailID = detailID;
        this.referenceID = referenceID;
        this.date = date;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.account = account;
        this.accountCorresponding = accountCorresponding;
        this.repositoryID = repositoryID;
        this.repositoryCode = repositoryCode;
        RepositoryName = repositoryName;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.materialGoodsName = materialGoodsName;
        this.unitID = unitID;
        this.unitPrice = unitPrice;
        this.iwQuantity = iwQuantity;
        this.owQuantity = owQuantity;
        this.iwAmount = iwAmount;
        this.owAmount = owAmount;
        this.mainUnitID = mainUnitID;
        this.mainUnitPrice = mainUnitPrice;
        this.mainIWQuantity = mainIWQuantity;
        this.mainOWQuantity = mainOWQuantity;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.reason = reason;
        this.description = description;
        this.expiryDate = expiryDate;
        this.lotNo = lotNo;
        this.budgetItemID = budgetItemID;
        this.costSetID = costSetID;
        this.statisticsCodeID = statisticsCodeID;
        this.expenseItemID = expenseItemID;
        this.orderPriority = orderPriority;
    }

    public static Object deepCopy(Object object) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
            outputStrm.writeObject(object);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
            return objInputStream.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public UUID getDetailID() {
        return detailID;
    }

    public void setDetailID(UUID detailID) {
        this.detailID = detailID;
    }

    public UUID getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountCorresponding() {
        return accountCorresponding;
    }

    public void setAccountCorresponding(String accountCorresponding) {
        this.accountCorresponding = accountCorresponding;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getRepositoryName() {
        return RepositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        RepositoryName = repositoryName;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public String getMaterialGoodsCode() {
        return materialGoodsCode;
    }

    public void setMaterialGoodsCode(String materialGoodsCode) {
        this.materialGoodsCode = materialGoodsCode;
    }

    public String getMaterialGoodsName() {
        return materialGoodsName;
    }

    public void setMaterialGoodsName(String materialGoodsName) {
        this.materialGoodsName = materialGoodsName;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getIwQuantity() {
        return iwQuantity;
    }

    public void setIwQuantity(BigDecimal iwQuantity) {
        this.iwQuantity = iwQuantity;
    }

    public BigDecimal getOwQuantity() {
        return owQuantity;
    }

    public void setOwQuantity(BigDecimal owQuantity) {
        this.owQuantity = owQuantity;
    }

    public BigDecimal getIwAmount() {
        return iwAmount;
    }

    public void setIwAmount(BigDecimal iwAmount) {
        this.iwAmount = iwAmount;
    }

    public BigDecimal getOwAmount() {
        return owAmount;
    }

    public void setOwAmount(BigDecimal owAmount) {
        this.owAmount = owAmount;
    }

    public UUID getMainUnitID() {
        return mainUnitID;
    }

    public void setMainUnitID(UUID mainUnitID) {
        this.mainUnitID = mainUnitID;
    }

    public BigDecimal getMainUnitPrice() {
        return mainUnitPrice;
    }

    public void setMainUnitPrice(BigDecimal mainUnitPrice) {
        this.mainUnitPrice = mainUnitPrice;
    }

    public BigDecimal getMainIWQuantity() {
        return mainIWQuantity;
    }

    public void setMainIWQuantity(BigDecimal mainIWQuantity) {
        this.mainIWQuantity = mainIWQuantity;
    }

    public BigDecimal getMainOWQuantity() {
        return mainOWQuantity;
    }

    public void setMainOWQuantity(BigDecimal mainOWQuantity) {
        this.mainOWQuantity = mainOWQuantity;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
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

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
}
