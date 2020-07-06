package vn.softdreams.ebweb.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A ViewVoucher.
 */
public class ViewSAQuoteDTO implements Serializable {
    public UUID id;
    public UUID sAQuoteDetailID;
    public Integer typeID;
    public UUID companyID;
    public String no;
    public LocalDate date;
    public UUID materialGoodsID;
    public String materialGoodsCode;
    public String description;
    public BigDecimal quantity;
    public BigDecimal unitPrice;
    public BigDecimal amount;
    public Boolean checked;

    private UUID unitID;
    private BigDecimal unitPriceOriginal;
    private BigDecimal mainQuantity;
    private UUID mainUnitID;
    private BigDecimal mainUnitPrice;
    private BigDecimal mainConvertRate;
    private String formula;
    private String vATDescription;
    //    private BigDecimal amount;
    private BigDecimal amountOriginal;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal discountAmountOriginal;
    private BigDecimal vATRate;
    private BigDecimal vATAmount;
    private BigDecimal vATAmountOriginal;
    private UUID accountingObjectID;
    private String accountingObjectName;
    private String accountingObjectAddress;
    private String companyTaxCode;
    private String contactName;
    private String descriptionParent;
    private UUID employeeID;
    private String deliveryTime;

    public ViewSAQuoteDTO(UUID id,
                          UUID sAQuoteDetailID,
                          Integer typeID,
                          UUID companyID,
                          String no,
                          LocalDate date,
                          UUID materialGoodsID,
                          String materialGoodsCode,
                          String description,
                          BigDecimal quantity,
                          BigDecimal unitPrice,
                          BigDecimal amount,

                          UUID unitID,
                          BigDecimal unitPriceOriginal,
                          BigDecimal mainQuantity,
                          UUID mainUnitID,
                          BigDecimal mainUnitPrice,
                          BigDecimal mainConvertRate,
                          String formula,
                          String vATDescription,
                          BigDecimal amountOriginal,
                          BigDecimal discountRate,
                          BigDecimal discountAmount,
                          BigDecimal discountAmountOriginal,
                          BigDecimal vATRate,
                          BigDecimal vATAmount,
                          BigDecimal vATAmountOriginal,
                          UUID accountingObjectID,
                          String accountingObjectName,
                          String accountingObjectAddress,
                          String companyTaxCode,
                          String contactName,
                          String descriptionParent,
                          UUID employeeID,
                          String deliveryTime
    ) {
        this.id = id;
        this.sAQuoteDetailID = sAQuoteDetailID;
        this.typeID = typeID;
        this.companyID = companyID;
        this.no = no;
        this.date = date;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
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

        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.companyTaxCode = companyTaxCode;
        this.contactName = contactName;
        this.descriptionParent = descriptionParent;
        this.employeeID = employeeID;
        this.deliveryTime = deliveryTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public UUID getsAQuoteDetailID() {
        return sAQuoteDetailID;
    }

    public void setsAQuoteDetailID(UUID sAQuoteDetailID) {
        this.sAQuoteDetailID = sAQuoteDetailID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
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

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDescriptionParent() {
        return descriptionParent;
    }

    public void setDescriptionParent(String descriptionParent) {
        this.descriptionParent = descriptionParent;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
