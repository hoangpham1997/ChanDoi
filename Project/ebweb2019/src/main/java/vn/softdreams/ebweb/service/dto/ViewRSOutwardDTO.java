package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ViewRSOutwardDTO {
    private UUID id;
    public UUID rSInwardOutwardDetailID;
    private Integer typeID;
    private UUID companyID;
    private String no;
    private LocalDate date;
    public UUID materialGoodsID;
    public String materialGoodsCode;
    public String description;
    public String reason;
    public BigDecimal quantity;
    private UUID unitID;
    private BigDecimal mainQuantity;
    private UUID mainUnitID;
    private BigDecimal mainUnitPrice;
    private BigDecimal mainConvertRate;
    private String formula;
    private String creditAccount;
    private String debitaccount;
    private UUID accountingObjectID;
    private String accountingObjectName;
    private String accountingObjectAddress;
    private String contactName;
    private UUID employeeID;


    public ViewRSOutwardDTO(UUID id, UUID rSInwardOutwardDetailID, Integer typeID, UUID companyID, String no, LocalDate date, UUID materialGoodsID, String materialGoodsCode, String description, String reason, BigDecimal quantity, UUID unitID, BigDecimal mainQuantity, UUID mainUnitID, BigDecimal mainUnitPrice, BigDecimal mainConvertRate, String formula, String creditAccount, String debitaccount, UUID accountingObjectID, String accountingObjectName, String accountingObjectAddress, String contactName, UUID employeeID) {
        this.id = id;
        this.rSInwardOutwardDetailID = rSInwardOutwardDetailID;
        this.typeID = typeID;
        this.companyID = companyID;
        this.no = no;
        this.date = date;
        this.materialGoodsID = materialGoodsID;
        this.materialGoodsCode = materialGoodsCode;
        this.description = description;
        this.reason = reason;
        this.quantity = quantity;
        this.unitID = unitID;
        this.mainQuantity = mainQuantity;
        this.mainUnitID = mainUnitID;
        this.mainUnitPrice = mainUnitPrice;
        this.mainConvertRate = mainConvertRate;
        this.formula = formula;
        this.creditAccount = creditAccount;
        this.debitaccount = debitaccount;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.contactName = contactName;
        this.employeeID = employeeID;
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

    public UUID getrSInwardOutwardDetailID() {
        return rSInwardOutwardDetailID;
    }

    public void setrSInwardOutwardDetailID(UUID rSInwardOutwardDetailID) {
        this.rSInwardOutwardDetailID = rSInwardOutwardDetailID;
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


    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
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

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getDebitaccount() {
        return debitaccount;
    }

    public void setDebitaccount(String debitaccount) {
        this.debitaccount = debitaccount;
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }
}
