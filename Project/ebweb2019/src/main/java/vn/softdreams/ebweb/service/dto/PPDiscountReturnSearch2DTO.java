package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PPDiscountReturnSearch2DTO {
    private UUID id;
    private UUID companyID;
    private UUID branchID;
    private Integer typeID;
    private LocalDate date;
    private LocalDate postedDate;
    private Integer typeLedger;
    private String noFBook;
    private String noMBook;
    private UUID rsInwardOutwardID;
    private UUID accountingObjectID;
    private String accountingObjectName;
    private String accountingObjectAddress;
    private String accountingObjectBankAccount;
    private String accountingObjectBankName;
    private String companyTaxCode;
    private String reason;
    private UUID invoiceTypeID;
    private String invoiceTemplate;
    private String paymentMethod;
    private LocalDate invoiceDate;
    private Integer invoiceForm;
    private String invoiceNo;
    private String numberAttach;
    private String invoiceSeries;
    private String currencyID;
    private BigDecimal exchangeRate;
    private UUID transportMethodID;
    private LocalDate dueDate;
    private UUID paymentClauseID;
    private UUID employeeID;
    private Boolean isAttachList;
    private String listNo;
    private LocalDate listDate;
    private String listCommonNameInventory;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountOriginal;
    private BigDecimal totalVATAmount;
    private BigDecimal totalVATAmountOriginal;
    private BigDecimal totalDiscountAmount;
    private BigDecimal totalDiscountAmounOriginal;
    private Integer statusInvoice;
    private Integer statusSendMail;
    private Integer statusConverted;
    private LocalDate dateSendMail;
    private String email;
    private String idAdjustInv;
    private String idReplaceInv;
    private Boolean isBill;
    private String idMIV;
    private String templateID;
    private Boolean recorded;
    private String contactName;
    private Boolean isDeliveryVoucher;
    private Boolean isExportInvoice;
    private String saBillInvoiceNo;
    private BigDecimal totalSumAmount;
    private Set<PPDiscountReturnDetailDTO> ppDiscountReturnDetails = new HashSet<>();

    public PPDiscountReturnSearch2DTO() {
    }

    public PPDiscountReturnSearch2DTO(UUID id, UUID companyID, UUID branchID, Integer typeID, LocalDate date, LocalDate postedDate, Integer typeLedger, String noFBook, String noMBook, UUID rsInwardOutwardID, UUID accountingObjectID, String accountingObjectName, String accountingObjectAddress, String accountingObjectBankAccount, String accountingObjectBankName, String companyTaxCode, String reason, UUID invoiceTypeID, String invoiceTemplate, String paymentMethod, LocalDate invoiceDate, Integer invoiceForm, String invoiceNo, String numberAttach, String invoiceSeries, String currencyID, BigDecimal exchangeRate, UUID transportMethodID, LocalDate dueDate, UUID paymentClauseID, UUID employeeID, Boolean isAttachList, String listNo, LocalDate listDate, String listCommonNameInventory, BigDecimal totalAmount, BigDecimal totalAmountOriginal, BigDecimal totalVATAmount, BigDecimal totalVATAmountOriginal, BigDecimal totalDiscountAmount, BigDecimal totalDiscountAmounOriginal, Integer statusInvoice, Integer statusSendMail, Integer statusConverted, LocalDate dateSendMail, String email, String idAdjustInv, String idReplaceInv, Boolean isBill, String idMIV, String templateID, Boolean recorded, String contactName, Boolean isDeliveryVoucher, Boolean isExportInvoice, String saBillInvoiceNo) {
        this.id = id;
        this.companyID = companyID;
        this.branchID = branchID;
        this.typeID = typeID;
        this.date = date;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.rsInwardOutwardID = rsInwardOutwardID;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.accountingObjectBankAccount = accountingObjectBankAccount;
        this.accountingObjectBankName = accountingObjectBankName;
        this.companyTaxCode = companyTaxCode;
        this.reason = reason;
        this.invoiceTypeID = invoiceTypeID;
        this.invoiceTemplate = invoiceTemplate;
        this.paymentMethod = paymentMethod;
        this.invoiceDate = invoiceDate;
        this.invoiceForm = invoiceForm;
        this.invoiceNo = invoiceNo;
        this.numberAttach = numberAttach;
        this.invoiceSeries = invoiceSeries;
        this.currencyID = currencyID;
        this.exchangeRate = exchangeRate;
        this.transportMethodID = transportMethodID;
        this.dueDate = dueDate;
        this.paymentClauseID = paymentClauseID;
        this.employeeID = employeeID;
        this.isAttachList = isAttachList;
        this.listNo = listNo;
        this.listDate = listDate;
        this.listCommonNameInventory = listCommonNameInventory;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.totalVATAmount = totalVATAmount;
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalDiscountAmounOriginal = totalDiscountAmounOriginal;
        this.statusInvoice = statusInvoice;
        this.statusSendMail = statusSendMail;
        this.statusConverted = statusConverted;
        this.dateSendMail = dateSendMail;
        this.email = email;
        this.idAdjustInv = idAdjustInv;
        this.idReplaceInv = idReplaceInv;
        this.isBill = isBill;
        this.idMIV = idMIV;
        this.templateID = templateID;
        this.recorded = recorded;
        this.contactName = contactName;
        this.isDeliveryVoucher = isDeliveryVoucher;
        this.isExportInvoice = isExportInvoice;
        this.saBillInvoiceNo = saBillInvoiceNo;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public BigDecimal getTotalSumAmount() {
        return totalSumAmount;
    }

    public void setTotalSumAmount(BigDecimal totalSumAmount) {
        this.totalSumAmount = totalSumAmount;
    }

    public String getSaBillInvoiceNo() {
        return saBillInvoiceNo;
    }

    public void setSaBillInvoiceNo(String saBillInvoiceNo) {
        this.saBillInvoiceNo = saBillInvoiceNo;
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

    public UUID getRsInwardOutwardID() {
        return rsInwardOutwardID;
    }

    public void setRsInwardOutwardID(UUID rsInwardOutwardID) {
        this.rsInwardOutwardID = rsInwardOutwardID;
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

    public String getAccountingObjectBankAccount() {
        return accountingObjectBankAccount;
    }

    public void setAccountingObjectBankAccount(String accountingObjectBankAccount) {
        this.accountingObjectBankAccount = accountingObjectBankAccount;
    }

    public String getAccountingObjectBankName() {
        return accountingObjectBankName;
    }

    public void setAccountingObjectBankName(String accountingObjectBankName) {
        this.accountingObjectBankName = accountingObjectBankName;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getInvoiceTypeID() {
        return invoiceTypeID;
    }

    public void setInvoiceTypeID(UUID invoiceTypeID) {
        this.invoiceTypeID = invoiceTypeID;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
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

    public UUID getTransportMethodID() {
        return transportMethodID;
    }

    public void setTransportMethodID(UUID transportMethodID) {
        this.transportMethodID = transportMethodID;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public UUID getPaymentClauseID() {
        return paymentClauseID;
    }

    public void setPaymentClauseID(UUID paymentClauseID) {
        this.paymentClauseID = paymentClauseID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public Boolean getAttachList() {
        return isAttachList;
    }

    public void setAttachList(Boolean attachList) {
        isAttachList = attachList;
    }

    public String getListNo() {
        return listNo;
    }

    public void setListNo(String listNo) {
        this.listNo = listNo;
    }

    public LocalDate getListDate() {
        return listDate;
    }

    public void setListDate(LocalDate listDate) {
        this.listDate = listDate;
    }

    public String getListCommonNameInventory() {
        return listCommonNameInventory;
    }

    public void setListCommonNameInventory(String listCommonNameInventory) {
        this.listCommonNameInventory = listCommonNameInventory;
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

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmounOriginal() {
        return totalDiscountAmounOriginal;
    }

    public void setTotalDiscountAmounOriginal(BigDecimal totalDiscountAmounOriginal) {
        this.totalDiscountAmounOriginal = totalDiscountAmounOriginal;
    }

    public Integer getStatusInvoice() {
        return statusInvoice;
    }

    public void setStatusInvoice(Integer statusInvoice) {
        this.statusInvoice = statusInvoice;
    }

    public Integer getStatusSendMail() {
        return statusSendMail;
    }

    public void setStatusSendMail(Integer statusSendMail) {
        this.statusSendMail = statusSendMail;
    }

    public Integer getStatusConverted() {
        return statusConverted;
    }

    public void setStatusConverted(Integer statusConverted) {
        this.statusConverted = statusConverted;
    }

    public LocalDate getDateSendMail() {
        return dateSendMail;
    }

    public void setDateSendMail(LocalDate dateSendMail) {
        this.dateSendMail = dateSendMail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdAdjustInv() {
        return idAdjustInv;
    }

    public void setIdAdjustInv(String idAdjustInv) {
        this.idAdjustInv = idAdjustInv;
    }

    public String getIdReplaceInv() {
        return idReplaceInv;
    }

    public void setIdReplaceInv(String idReplaceInv) {
        this.idReplaceInv = idReplaceInv;
    }

    public Boolean getIsBill() {
        return isBill;
    }

    public void setIsBill(Boolean bill) {
        isBill = bill;
    }

    public String getIdMIV() {
        return idMIV;
    }

    public void setIdMIV(String idMIV) {
        this.idMIV = idMIV;
    }

    public String getTemplateID() {
        return templateID;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Boolean getIsDeliveryVoucher() {
        return isDeliveryVoucher;
    }

    public void setIsDeliveryVoucher(Boolean deliveryVoucher) {
        isDeliveryVoucher = deliveryVoucher;
    }

    public Boolean getIsExportInvoice() {
        return isExportInvoice;
    }

    public void setIsExportInvoice(Boolean exportInvoice) {
        isExportInvoice = exportInvoice;
    }

    public Set<PPDiscountReturnDetailDTO> getPpDiscountReturnDetails() {
        return ppDiscountReturnDetails;
    }

    public void setPpDiscountReturnDetails(Set<PPDiscountReturnDetailDTO> ppDiscountReturnDetails) {
        this.ppDiscountReturnDetails = ppDiscountReturnDetails;
    }
}
