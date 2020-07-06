package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class IWVoucherDTO {
    private UUID id;
    private UUID detailID;
    private String noMBook;
    private String noFBook;
    private LocalDate date;
    private UUID repositoryID;
    private String repositoryCode;
    private UUID unitID;
    private String unitName;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal amount;
    private String lotNo;
    private LocalDate expiryDate;
    private UUID mainUnitID;
    private String mainUnitName;
    private BigDecimal mainQuantity;
    private BigDecimal mainUnitPrice;
    private Integer typeID;

    public IWVoucherDTO(UUID id, UUID detailID, String noMBook, String noFBook, LocalDate date, UUID repositoryID, String repositoryCode, UUID unitID, String unitName, BigDecimal unitPrice, BigDecimal quantity, BigDecimal amount, String lotNo, LocalDate expiryDate, UUID mainUnitID, String mainUnitName, BigDecimal mainQuantity, BigDecimal mainUnitPrice, Integer typeID) {
        this.id = id;
        this.detailID = detailID;
        this.noMBook = noMBook;
        this.noFBook = noFBook;
        this.date = date;
        this.repositoryID = repositoryID;
        this.repositoryCode = repositoryCode;
        this.unitID = unitID;
        this.unitName = unitName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.amount = amount;
        this.lotNo = lotNo;
        this.expiryDate = expiryDate;
        this.mainUnitID = mainUnitID;
        this.mainUnitName = mainUnitName;
        this.mainQuantity = mainQuantity;
        this.mainUnitPrice = mainUnitPrice;
        this.typeID = typeID;
    }

    public String getMainUnitName() {
        return mainUnitName;
    }

    public void setMainUnitName(String mainUnitName) {
        this.mainUnitName = mainUnitName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDetailID() {
        return detailID;
    }

    public void setDetailID(UUID detailID) {
        this.detailID = detailID;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }
}
