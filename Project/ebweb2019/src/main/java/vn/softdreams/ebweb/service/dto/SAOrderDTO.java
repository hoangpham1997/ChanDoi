package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SAOrderDTO {
    private UUID id;

    private UUID companyID;

    private UUID branchID;

    private Integer typeID;

    private LocalDate date;

    private String no;

    private LocalDate deliverDate;

    private String deliveryPlace;

    private UUID accountingObjectID;

    private String accountingObjectName;

    private String accountingObjectAddress;

    private String companyTaxCode;

    private String contactName;

    private String reason;

    private String currencyID;

    private BigDecimal exchangeRate;

    private UUID transpotMethodID;

    private UUID employeeID;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountOriginal;

    private BigDecimal totalDiscountAmount;

    private BigDecimal totalDiscountAmountOriginal;

    private BigDecimal totalVATAmount;

    private BigDecimal totalVATAmountOriginal;

    private UUID templateID;

    private Integer status;

    private String statusString;

    public UUID sAOrderDetailID;
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
    private BigDecimal amountOriginal;
    private BigDecimal discountRate;
    private BigDecimal discountAmount;
    private BigDecimal discountAmountOriginal;
    private BigDecimal vATRate;
    private BigDecimal vATAmount;
    private BigDecimal vATAmountOriginal;
    private BigDecimal quantityDelivery;
    private Number total;
    private String quantityString;
    private String unitPriceString;
    private String amountString;
    private String amountOriginalString;
    private String discountRateString;
    private String discountAmountString;
    private String discountAmountOriginalString;
    private String vATRateString;
    private String vATAmountString;
    private String vATAmountOriginalString;
    private String unitName;
    private UUID repositoryID;
    private Integer typeGroupID;

    public SAOrderDTO(UUID id,
                      UUID companyID,
                      Integer typeID,
                      LocalDate date,
                      String no,
                      LocalDate deliverDate,
                      String deliveryPlace,
                      UUID accountingObjectID,
                      String accountingObjectName,
                      String accountingObjectAddress,
                      String reason,
                      BigDecimal totalAmount,
                      BigDecimal totalAmountOriginal,
                      Integer status,
                      String currencyID) {
        this.id = id;
        this.companyID = companyID;
        this.typeID = typeID;
        this.date = date;
        this.no = no;
        this.deliverDate = deliverDate;
        this.deliveryPlace = deliveryPlace;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.status = status;
        this.currencyID = currencyID;
    }

    public SAOrderDTO(UUID id,
                      UUID sAOrderDetailID,
                      Integer typeID,
                      UUID companyID,
                      String no,
                      LocalDate date,
                      UUID accountingObjectID,
                      String accountingObjectName,
                      String accountingObjectAddress,
                      String reason,
                      String contactName,
                      String companyTaxCode,
                      UUID employeeID,
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
                      UUID repositoryID,
                      Integer typeGroupID,
                      BigDecimal quantityDelivery
    ) {
        this.id = id;
        this.sAOrderDetailID = sAOrderDetailID;
        this.typeID = typeID;
        this.companyID = companyID;
        this.no = no;
        this.date = date;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.reason = reason;
        this.contactName = contactName;
        this.companyTaxCode = companyTaxCode;
        this.employeeID = employeeID;
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
        this.repositoryID = repositoryID;
        this.typeGroupID = typeGroupID;
        this.quantityDelivery = quantityDelivery;
    }

    public SAOrderDTO(
                      String materialGoodsCode,
                      String description,
                      BigDecimal quantity,
                      String unitName,
                      BigDecimal unitPrice,
                      BigDecimal unitPriceOriginal,
                      BigDecimal amount,
                      BigDecimal amountOriginal,
                      BigDecimal discountRate,
                      BigDecimal discountAmount,
                      BigDecimal discountAmountOriginal,
                      BigDecimal vATRate,
                      BigDecimal vATAmount,
                      BigDecimal vATAmountOriginal) {
        this.materialGoodsCode = materialGoodsCode;
        this.description = description;
        this.quantity = quantity;
        this.unitName = unitName;
        this.unitPrice = unitPrice;
        this.unitPriceOriginal = unitPriceOriginal;
        this.amount = amount;
        this.amountOriginal = amountOriginal;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.discountAmountOriginal = discountAmountOriginal;
        this.vATRate = vATRate;
        this.vATAmount = vATAmount;
        this.vATAmountOriginal = vATAmountOriginal;
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

    public LocalDate getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(LocalDate deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public UUID getTranspotMethodID() {
        return transpotMethodID;
    }

    public void setTranspotMethodID(UUID transpotMethodID) {
        this.transpotMethodID = transpotMethodID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public UUID getsAOrderDetailID() {
        return sAOrderDetailID;
    }

    public void setsAOrderDetailID(UUID sAOrderDetailID) {
        this.sAOrderDetailID = sAOrderDetailID;
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

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
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

    public BigDecimal getQuantityDelivery() {
        return quantityDelivery;
    }

    public void setQuantityDelivery(BigDecimal quantityDelivery) {
        this.quantityDelivery = quantityDelivery;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }

    public String getQuantityString() {
        return quantityString;
    }

    public void setQuantityString(String quantityString) {
        this.quantityString = quantityString;
    }

    public String getUnitPriceString() {
        return unitPriceString;
    }

    public void setUnitPriceString(String unitPriceString) {
        this.unitPriceString = unitPriceString;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getAmountOriginalString() {
        return amountOriginalString;
    }

    public void setAmountOriginalString(String amountOriginalString) {
        this.amountOriginalString = amountOriginalString;
    }

    public String getDiscountRateString() {
        return discountRateString;
    }

    public void setDiscountRateString(String discountRateString) {
        this.discountRateString = discountRateString;
    }

    public String getDiscountAmountString() {
        return discountAmountString;
    }

    public void setDiscountAmountString(String discountAmountString) {
        this.discountAmountString = discountAmountString;
    }

    public String getDiscountAmountOriginalString() {
        return discountAmountOriginalString;
    }

    public void setDiscountAmountOriginalString(String discountAmountOriginalString) {
        this.discountAmountOriginalString = discountAmountOriginalString;
    }

    public String getvATRateString() {
        return vATRateString;
    }

    public void setvATRateString(String vATRateString) {
        this.vATRateString = vATRateString;
    }

    public String getvATAmountString() {
        return vATAmountString;
    }

    public void setvATAmountString(String vATAmountString) {
        this.vATAmountString = vATAmountString;
    }

    public String getvATAmountOriginalString() {
        return vATAmountOriginalString;
    }

    public void setvATAmountOriginalString(String vATAmountOriginalString) {
        this.vATAmountOriginalString = vATAmountOriginalString;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }
}
