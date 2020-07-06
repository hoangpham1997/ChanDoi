package vn.softdreams.ebweb.service.dto;
import vn.softdreams.ebweb.domain.PPInvoiceDetails;
import vn.softdreams.ebweb.web.rest.dto.PPInvoiceDetailCostDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class PPInvoiceDTO {
    private UUID id;

    private UUID companyId;

    private Integer typeId;

    private LocalDate date;

    private LocalDate postedDate;

    private Integer typeLedger;

    private String noFBook;

    private String noMBook;

    private UUID rsInwardOutwardId;

    private UUID paymentVoucherId;

    private UUID accountingObjectId;

    private String accountingObjectName;

    private String accountingObjectAddress;

    private String companyTaxCode;

    private String contactName;

    private Boolean billReceived;

    private String currencyId;

    private BigDecimal exchangeRate;

    private UUID paymentClauseId;

    private LocalDate dueDate;

    private UUID transportMethodId;

    private UUID employeeId;

    private Boolean isImportPurchase;

    private Boolean storedInRepository;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountOriginal;

    private BigDecimal totalImportTaxAmount;

    private BigDecimal totalImportTaxAmountOriginal;

    private BigDecimal totalVATAmount;

    private BigDecimal totalVATAmountOriginal;

    private BigDecimal totalDiscountAmount;

    private BigDecimal totalDiscountAmountOriginal;

    private BigDecimal totalInwardAmount;

    private BigDecimal totalInwardAmountOriginal;

    private BigDecimal totalSpecialConsumeTaxAmount;

    private BigDecimal totalSpecialConsumeTaxAmountOriginal;

    private LocalDate matchDate;

    private UUID templateId;

    private Boolean recorded;

    private BigDecimal totalFreightAmount;

    private BigDecimal totalFreightAmountOriginal;

    private BigDecimal totalImportTaxExpenseAmount;

    private BigDecimal totalImportTaxExpenseAmountOriginal;

    private String noFBookRs;

    private String reason;

    private String reasonRs;

    private String numberAttach;

    private String numberAttachRs;

    private String noMBookRs;


    private String receiverUserName;

    private UUID accountPaymentId;

    private String accountPaymentName;

    private UUID accountReceiverId;

    private String accountReceiverName;

    private UUID creditCardId;

    private String creditCardNumber;

    private UUID  bankAccountReceiverId;

    private String bankAccountReceiverName;

    private String otherNoFBook;

    private Integer currentBook;

    private String otherReason;

    private String otherNumberAttach;

    private String otherNoMBook;

    private String accountReceiverFullName;

    private String identificationNo;

    private LocalDate issueDate;

    private String issueBy;

    private List<PPInvoiceDetails> ppInvoiceDetails = new ArrayList<>();

    private List<RefVoucherDTO> refVouchers;

    private List<PPInvoiceDetailCostDTO> ppInvoiceDetailCost;

    private Boolean checkPPOrderQuantity;

    public PPInvoiceDTO() {
    }

    public PPInvoiceDTO(UUID id, LocalDate date, String noFBook, String noMBook, LocalDate dueDate) {
        this.id = id;
        this.date = date;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.dueDate = dueDate;
    }

    public PPInvoiceDTO(UUID id, UUID companyId, Integer typeId, LocalDate date, LocalDate postedDate, Integer typeLedger,
                        String noFBook, String noMBook, UUID rsInwardOutwardId, UUID paymentVoucherId,
                        UUID accountingObjectId, String accountingObjectName, String accountingObjectAddress,
                        String companyTaxCode, String contactName, Boolean billReceived, String currencyId,
                        BigDecimal exchangeRate, UUID paymentClauseId, LocalDate dueDate, UUID transportMethodId,
                        UUID employeeId, Boolean isImportPurchase, Boolean storedInRepository, BigDecimal totalAmount,
                        BigDecimal totalAmountOriginal, BigDecimal totalImportTaxAmount, BigDecimal totalImportTaxAmountOriginal,
                        BigDecimal totalVATAmount, BigDecimal totalVATAmountOriginal, BigDecimal totalDiscountAmount,
                        BigDecimal totalDiscountAmountOriginal, BigDecimal totalInwardAmount, BigDecimal totalInwardAmountOriginal,
                        BigDecimal totalSpecialConsumeTaxAmount, BigDecimal totalSpecialConsumeTaxAmountOriginal,
                        LocalDate matchDate, UUID templateId, Boolean recorded, BigDecimal totalFreightAmount,
                        BigDecimal totalFreightAmountOriginal, BigDecimal totalImportTaxExpenseAmount,
                        BigDecimal totalImportTaxExpenseAmountOriginal, String noFBookRs, String reason, String reasonRs,
                        String numberAttach, String numberAttachRs, String noMBookRs, String receiverUserName,
                        UUID accountPaymentId, String accountPaymentName, UUID accountReceiverId, String accountReceiverName,
                        String creditCardNumber, UUID bankAccountReceiverId, String bankAccountReceiverName,
                        String otherNoFBook, String otherReason, String otherNumberAttach, String otherNoMBook, String accountReceiverFullName, String identificationNo, LocalDate issueDate, String issueBy) {
        this.id = id;
        this.companyId = companyId;
        this.typeId = typeId;
        this.date = date;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.rsInwardOutwardId = rsInwardOutwardId;
        this.paymentVoucherId = paymentVoucherId;
        this.accountingObjectId = accountingObjectId;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.companyTaxCode = companyTaxCode;
        this.contactName = contactName;
        this.billReceived = billReceived;
        this.currencyId = currencyId;
        this.exchangeRate = exchangeRate;
        this.paymentClauseId = paymentClauseId;
        this.dueDate = dueDate;
        this.transportMethodId = transportMethodId;
        this.employeeId = employeeId;
        this.isImportPurchase = isImportPurchase;
        this.storedInRepository = storedInRepository;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.totalImportTaxAmount = totalImportTaxAmount;
        this.totalImportTaxAmountOriginal = totalImportTaxAmountOriginal;
        this.totalVATAmount = totalVATAmount;
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
        this.totalInwardAmount = totalInwardAmount;
        this.totalInwardAmountOriginal = totalInwardAmountOriginal;
        this.totalSpecialConsumeTaxAmount = totalSpecialConsumeTaxAmount;
        this.totalSpecialConsumeTaxAmountOriginal = totalSpecialConsumeTaxAmountOriginal;
        this.matchDate = matchDate;
        this.templateId = templateId;
        this.recorded = recorded;
        this.totalFreightAmount = totalFreightAmount;
        this.totalFreightAmountOriginal = totalFreightAmountOriginal;
        this.totalImportTaxExpenseAmount = totalImportTaxExpenseAmount;
        this.totalImportTaxExpenseAmountOriginal = totalImportTaxExpenseAmountOriginal;
        this.noFBookRs = noFBookRs;
        this.reason = reason;
        this.reasonRs = reasonRs;
        this.numberAttach = numberAttach;
        this.numberAttachRs = numberAttachRs;
        this.noMBookRs = noMBookRs;

        this.receiverUserName = receiverUserName;
        this.accountPaymentId = accountPaymentId;
        this.accountPaymentName = accountPaymentName;
        this.accountReceiverId = accountReceiverId;
        this.accountReceiverName = accountReceiverName;
        this.creditCardNumber = creditCardNumber;
        this.bankAccountReceiverId = bankAccountReceiverId;
        this.bankAccountReceiverName = bankAccountReceiverName;
        this.otherNoFBook = otherNoFBook;
        this.otherReason = otherReason;
        this.otherNumberAttach = otherNumberAttach;
        this.otherNoMBook = otherNoMBook;
        this.accountReceiverFullName = accountReceiverFullName;
        this.identificationNo = identificationNo;
        this.issueDate = issueDate;
        this.issueBy = issueBy;
    }

    public UUID getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(UUID creditCardId) {
        this.creditCardId = creditCardId;
    }

    public Boolean getCheckPPOrderQuantity() {
        return checkPPOrderQuantity;
    }

    public void setCheckPPOrderQuantity(Boolean checkPPOrderQuantity) {
        this.checkPPOrderQuantity = checkPPOrderQuantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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

    public UUID getRsInwardOutwardId() {
        return rsInwardOutwardId;
    }

    public void setRsInwardOutwardId(UUID rsInwardOutwardId) {
        this.rsInwardOutwardId = rsInwardOutwardId;
    }

    public UUID getPaymentVoucherId() {
        return paymentVoucherId;
    }

    public void setPaymentVoucherId(UUID paymentVoucherId) {
        this.paymentVoucherId = paymentVoucherId;
    }

    public UUID getAccountingObjectId() {
        return accountingObjectId;
    }

    public void setAccountingObjectId(UUID accountingObjectId) {
        this.accountingObjectId = accountingObjectId;
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

    public Boolean getBillReceived() {
        return billReceived;
    }

    public void setBillReceived(Boolean billReceived) {
        this.billReceived = billReceived;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public UUID getPaymentClauseId() {
        return paymentClauseId;
    }

    public void setPaymentClauseId(UUID paymentClauseId) {
        this.paymentClauseId = paymentClauseId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public UUID getTransportMethodId() {
        return transportMethodId;
    }

    public void setTransportMethodId(UUID transportMethodId) {
        this.transportMethodId = transportMethodId;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getImportPurchase() {
        return isImportPurchase;
    }

    public void setImportPurchase(Boolean importPurchase) {
        isImportPurchase = importPurchase;
    }

    public Boolean getStoredInRepository() {
        return storedInRepository;
    }

    public void setStoredInRepository(Boolean storedInRepository) {
        this.storedInRepository = storedInRepository;
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

    public BigDecimal getTotalImportTaxAmount() {
        return totalImportTaxAmount;
    }

    public void setTotalImportTaxAmount(BigDecimal totalImportTaxAmount) {
        this.totalImportTaxAmount = totalImportTaxAmount;
    }

    public BigDecimal getTotalImportTaxAmountOriginal() {
        return totalImportTaxAmountOriginal;
    }

    public void setTotalImportTaxAmountOriginal(BigDecimal totalImportTaxAmountOriginal) {
        this.totalImportTaxAmountOriginal = totalImportTaxAmountOriginal;
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

    public BigDecimal getTotalInwardAmount() {
        return totalInwardAmount;
    }

    public void setTotalInwardAmount(BigDecimal totalInwardAmount) {
        this.totalInwardAmount = totalInwardAmount;
    }

    public BigDecimal getTotalInwardAmountOriginal() {
        return totalInwardAmountOriginal;
    }

    public void setTotalInwardAmountOriginal(BigDecimal totalInwardAmountOriginal) {
        this.totalInwardAmountOriginal = totalInwardAmountOriginal;
    }

    public BigDecimal getTotalSpecialConsumeTaxAmount() {
        return totalSpecialConsumeTaxAmount;
    }

    public void setTotalSpecialConsumeTaxAmount(BigDecimal totalSpecialConsumeTaxAmount) {
        this.totalSpecialConsumeTaxAmount = totalSpecialConsumeTaxAmount;
    }

    public BigDecimal getTotalSpecialConsumeTaxAmountOriginal() {
        return totalSpecialConsumeTaxAmountOriginal;
    }

    public void setTotalSpecialConsumeTaxAmountOriginal(BigDecimal totalSpecialConsumeTaxAmountOriginal) {
        this.totalSpecialConsumeTaxAmountOriginal = totalSpecialConsumeTaxAmountOriginal;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public UUID getTemplateID() {
        return templateId;
    }

    public void setTemplateID(UUID templateId) {
        this.templateId = templateId;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public BigDecimal getTotalFreightAmount() {
        return totalFreightAmount;
    }

    public void setTotalFreightAmount(BigDecimal totalFreightAmount) {
        this.totalFreightAmount = totalFreightAmount;
    }

    public BigDecimal getTotalFreightAmountOriginal() {
        return totalFreightAmountOriginal;
    }

    public void setTotalFreightAmountOriginal(BigDecimal totalFreightAmountOriginal) {
        this.totalFreightAmountOriginal = totalFreightAmountOriginal;
    }

    public BigDecimal getTotalImportTaxExpenseAmount() {
        return totalImportTaxExpenseAmount;
    }

    public void setTotalImportTaxExpenseAmount(BigDecimal totalImportTaxExpenseAmount) {
        this.totalImportTaxExpenseAmount = totalImportTaxExpenseAmount;
    }

    public BigDecimal getTotalImportTaxExpenseAmountOriginal() {
        return totalImportTaxExpenseAmountOriginal;
    }

    public void setTotalImportTaxExpenseAmountOriginal(BigDecimal totalImportTaxExpenseAmountOriginal) {
        this.totalImportTaxExpenseAmountOriginal = totalImportTaxExpenseAmountOriginal;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public List<PPInvoiceDetails> getPpInvoiceDetails() {
        return ppInvoiceDetails;
    }

    public void setPpInvoiceDetails(List<PPInvoiceDetails> ppInvoiceDetails) {
        this.ppInvoiceDetails = ppInvoiceDetails;
    }

    public String getReasonRs() {
        return reasonRs;
    }

    public void setReasonRs(String reasonRs) {
        this.reasonRs = reasonRs;
    }

    public String getNoFBookRs() {
        return noFBookRs;
    }

    public void setNoFBookRs(String noFBookRs) {
        this.noFBookRs = noFBookRs;
    }

    public String getNumberAttachRs() {
        return numberAttachRs;
    }

    public void setNumberAttachRs(String numberAttachRs) {
        this.numberAttachRs = numberAttachRs;
    }

    public String getNoMBookRs() {
        return noMBookRs;
    }

    public void setNoMBookRs(String noMBookRs) {
        this.noMBookRs = noMBookRs;
    }

    public String getAccountPaymentName() {
        return accountPaymentName;
    }

    public void setAccountPaymentName(String accountPaymentName) {
        this.accountPaymentName = accountPaymentName;
    }

    public String getAccountReceiverName() {
        return accountReceiverName;
    }

    public void setAccountReceiverName(String accountReceiverName) {
        this.accountReceiverName = accountReceiverName;
    }

    public String getOtherNoFBook() {
        return otherNoFBook;
    }

    public void setOtherNoFBook(String otherNoFBook) {
        this.otherNoFBook = otherNoFBook;
    }

    public Integer getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(Integer currentBook) {
        this.currentBook = currentBook;
    }

    public String getReceiverUserName() {
        return receiverUserName;
    }

    public void setReceiverUserName(String receiverUserName) {
        this.receiverUserName = receiverUserName;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public String getOtherNumberAttach() {
        return otherNumberAttach;
    }

    public void setOtherNumberAttach(String otherNumberAttach) {
        this.otherNumberAttach = otherNumberAttach;
    }

    public UUID getAccountReceiverId() {
        return accountReceiverId;
    }

    public void setAccountReceiverId(UUID accountReceiverId) {
        this.accountReceiverId = accountReceiverId;
    }

    public UUID getAccountPaymentId() {
        return accountPaymentId;
    }

    public void setAccountPaymentId(UUID accountPaymentId) {
        this.accountPaymentId = accountPaymentId;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public List<RefVoucherDTO> getRefVouchers() {
        return refVouchers;
    }

    public void setRefVouchers(List<RefVoucherDTO> refVouchers) {
        this.refVouchers = refVouchers;
    }

    public UUID getBankAccountReceiverId() {
        return bankAccountReceiverId;
    }

    public void setBankAccountReceiverId(UUID bankAccountReceiverId) {
        this.bankAccountReceiverId = bankAccountReceiverId;
    }

    public String getBankAccountReceiverName() {
        return bankAccountReceiverName;
    }

    public void setBankAccountReceiverName(String bankAccountReceiverName) {
        this.bankAccountReceiverName = bankAccountReceiverName;
    }

    public String getOtherNoMBook() {
        return otherNoMBook;
    }

    public void setOtherNoMBook(String otherNoMBook) {
        this.otherNoMBook = otherNoMBook;
    }

    public String getAccountReceiverFullName() {
        return accountReceiverFullName;
    }

    public void setAccountReceiverFullName(String accountReceiverFullName) {
        this.accountReceiverFullName = accountReceiverFullName;
    }

    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(String issueBy) {
        this.issueBy = issueBy;
    }

    public List<PPInvoiceDetailCostDTO> getPpInvoiceDetailCost() {
        return ppInvoiceDetailCost;
    }

    public void setPpInvoiceDetailCost(List<PPInvoiceDetailCostDTO> ppInvoiceDetailCost) {
        this.ppInvoiceDetailCost = ppInvoiceDetailCost;
    }
}
