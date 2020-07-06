package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ViewPPInvoiceDTO {
    private UUID id;
    public UUID pPInvoiceDetailID;
    private Integer typeID;
    private UUID companyID;
    private String no;
    private LocalDate date;
    public UUID materialGoodsID;
    public String materialGoodsCode;
    public String reason;
    public String description;
    public BigDecimal quantity;
    public BigDecimal unitPrice;
    public BigDecimal amount;
    private UUID unitID;
    private BigDecimal unitPriceOriginal;
    private BigDecimal mainQuantity;
    private UUID mainUnitID;
    private BigDecimal mainUnitPrice;
    private BigDecimal mainConvertRate;
    private String formula;
    private String vATDescription;
    private BigDecimal amountOriginal;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal discountAmountOriginal;
    private BigDecimal vATRate;
    private BigDecimal vATAmount;
    private BigDecimal vATAmountOriginal;
    private String lotNo;
    private LocalDate expiryDate;
    private UUID rsInwardOutwardID;

    public ViewPPInvoiceDTO(UUID id, UUID pPInvoiceDetailID, Integer typeID, UUID companyID, String no, LocalDate date, UUID materialGoodsID, String materialGoodsCode, String reason, String description, BigDecimal quantity, BigDecimal unitPrice, BigDecimal amount, UUID unitID, BigDecimal unitPriceOriginal, BigDecimal mainQuantity, UUID mainUnitID, BigDecimal mainUnitPrice, BigDecimal mainConvertRate, String formula, String vATDescription, BigDecimal amountOriginal, BigDecimal discountRate, BigDecimal discountAmount, BigDecimal discountAmountOriginal, BigDecimal vATRate, BigDecimal vATAmount, BigDecimal vATAmountOriginal, String lotNo, LocalDate expiryDate) {
        this.id = id;
        this.pPInvoiceDetailID = pPInvoiceDetailID;
        this.typeID = typeID;
        this.companyID = companyID;
        this.no = no;
        this.date = date;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.reason = reason;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.unitID = unitID;
        this.unitPriceOriginal = unitPriceOriginal;
        this.mainQuantity = mainQuantity;
        this.mainUnitID = mainUnitID;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.vATDescription = vATDescription;
        this.amountOriginal = amountOriginal;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.discountAmountOriginal = discountAmountOriginal;
        this.vATRate = vATRate;
        this.vATAmount = vATAmount;
        this.vATAmountOriginal = vATAmountOriginal;
        this.lotNo = lotNo;
        this.expiryDate = expiryDate;
    }

    public ViewPPInvoiceDTO(UUID id, UUID rsInwardOutwardID) {
        this.id = id;
        this.rsInwardOutwardID = rsInwardOutwardID;
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

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public UUID getpPInvoiceDetailID() {
        return pPInvoiceDetailID;
    }

    public void setpPInvoiceDetailID(UUID pPInvoiceDetailID) {
        this.pPInvoiceDetailID = pPInvoiceDetailID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public BigDecimal getUnitPriceOriginal() {
        return unitPriceOriginal;
    }

    public void setUnitPriceOriginal(BigDecimal unitPriceOriginal) {
        this.unitPriceOriginal = unitPriceOriginal;
    }

    public BigDecimal getMainQuantity() {
        return mainQuantity;
    }

    public void setMainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
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

    public String getvATDescription() {
        return vATDescription;
    }

    public void setvATDescription(String vATDescription) {
        this.vATDescription = vATDescription;
    }

    public BigDecimal getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(BigDecimal amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmountOriginal() {
        return discountAmountOriginal;
    }

    public void setDiscountAmountOriginal(BigDecimal discountAmountOriginal) {
        this.discountAmountOriginal = discountAmountOriginal;
    }

    public BigDecimal getvATRate() {
        return vATRate;
    }

    public void setvATRate(BigDecimal vATRate) {
        this.vATRate = vATRate;
    }

    public BigDecimal getvATAmount() {
        return vATAmount;
    }

    public void setvATAmount(BigDecimal vATAmount) {
        this.vATAmount = vATAmount;
    }

    public BigDecimal getvATAmountOriginal() {
        return vATAmountOriginal;
    }

    public void setvATAmountOriginal(BigDecimal vATAmountOriginal) {
        this.vATAmountOriginal = vATAmountOriginal;
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

    public UUID getRsInwardOutwardID() {
        return rsInwardOutwardID;
    }

    public void setRsInwardOutwardID(UUID rsInwardOutwardID) {
        this.rsInwardOutwardID = rsInwardOutwardID;
    }
}
