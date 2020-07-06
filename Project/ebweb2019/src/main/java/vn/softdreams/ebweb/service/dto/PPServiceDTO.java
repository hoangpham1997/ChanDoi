package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PPServiceDTO {

    private UUID id;

    private String receiptDate;

    private String postedDate;

    private String noBook;

    private String otherNoBook;

    private String receiptType;

    private UUID accountingObjectID;

    private String accountingObjectName;

    private String accountingObjectAddress;

    private String companyTaxCode;

    private String contactName;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountOriginal;

    private BigDecimal totalDiscountAmount;

    private BigDecimal totalDiscountAmountOriginal;

    private BigDecimal totalVATAmount;

    private BigDecimal totalVATAmountOriginal;

    private BigDecimal resultAmount;

    private BigDecimal resultAmountOriginal;

    private Boolean billReceived;

    private String billReceivedStr;

    private String dueDate;

    private String reason;

    private Integer typeId;

    private UUID employeeID;

    private String employeeName;

    private Integer typeLedger;

    private String noFBook;

    private String noMBook;

    private String currencyID;

    private BigDecimal exchangeRate;

    private List<PPServiceDetailDTO> ppServiceDetailDTOS;

    private String receiverUserName;

    private String numberAttach;

    private String otherNumberAttach;

    private String otherReason;

    private UUID accountPaymentId;

    private String accountPaymentName;

    private UUID accountReceiverId;

    private String accountReceiverName;

    private UUID creditCardId;

    private String creditCardNumber;

    private String creditCardType;

    private String ownerCreditCard;

    private Boolean purchaseCosts;

    private Boolean recorded;

    private UUID paymentVoucherID;

    private List<RefVoucherDTO> refVouchers;

    private Integer currentBook;

    private String invoiceNo;

    private Integer rowNum;

    private String receiver;

    private String identificationNo;

    private String issueDate;

    private String issueBy;

    private Boolean checkPPOrderQuantity;

    public PPServiceDTO(UUID id, String receiptDate, String postedDate, String noBook, String otherNoBook, String receiptType,
                        UUID accountingObjectID, String accountingObjectName, String accountingObjectAddress,
                        BigDecimal totalAmount, BigDecimal totalAmountOriginal, BigDecimal totalDiscountAmount, BigDecimal totalDiscountAmountOriginal,
                        BigDecimal totalVATAmount, BigDecimal totalVATAmountOriginal, BigDecimal resultAmount, BigDecimal resultAmountOriginal, Boolean billReceived,
                        String dueDate, String companyTaxCode, String contactName, Integer typeId, String reason,
                        Boolean purchaseCosts, Boolean recorded, String numberAttach,
                        UUID employeeID, String employeeName, Integer typeLedger, String noFBook,
                        String noMBook, String currencyID, BigDecimal exchangeRate,
                        String receiverUserName, String otherNumberAttach, String otherReason, UUID accountPaymentId,
                        String accountPaymentName, UUID accountReceiverId, String accountReceiverName,
                        String creditCardNumber, UUID paymentVoucherID,
                        String receiver, String identificationNo, String issueDate, String issueBy) {
        this.id = id;
        this.receiptDate = receiptDate;
        this.postedDate = postedDate;
        this.noBook = noBook;
        this.otherNoBook = otherNoBook;
        this.receiptType = receiptType;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
        this.totalVATAmount = totalVATAmount;
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        this.resultAmount = resultAmount;
        this.resultAmountOriginal = resultAmountOriginal;
        this.billReceived = billReceived;
        this.billReceivedStr = billReceived ? "Đã lập hóa đơn" : "Chưa lập hóa đơn";
        this.dueDate = dueDate;
        this.companyTaxCode = companyTaxCode;
        this.contactName = contactName;
        this.reason = reason;
        this.typeId = typeId;
        this.purchaseCosts = purchaseCosts;
        this.recorded = recorded;
        this.numberAttach = numberAttach;
        this.employeeName = employeeName;
        this.employeeID = employeeID;
        this.typeLedger = typeLedger;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.currencyID = currencyID;
        this.exchangeRate = exchangeRate;
        this.receiverUserName = receiverUserName;
        this.otherNumberAttach = otherNumberAttach;
        this.otherReason = otherReason;
        this.accountPaymentId = accountPaymentId;
        this.accountPaymentName = accountPaymentName;
        this.accountReceiverId = accountReceiverId;
        this.accountReceiverName = accountReceiverName;
        this.creditCardNumber = creditCardNumber;
        this.paymentVoucherID = paymentVoucherID;
        this.receiver = receiver;
        this.identificationNo = identificationNo;
        this.issueDate = issueDate;
        this.issueBy = issueBy;
    }

    public BigDecimal getResultAmountOriginal() {
        return resultAmountOriginal;
    }

    public void setResultAmountOriginal(BigDecimal resultAmountOriginal) {
        this.resultAmountOriginal = resultAmountOriginal;
    }

    public PPServiceDTO() {
    }

    public Boolean getCheckPPOrderQuantity() {
        return checkPPOrderQuantity;
    }

    public void setCheckPPOrderQuantity(Boolean checkPPOrderQuantity) {
        this.checkPPOrderQuantity = checkPPOrderQuantity;
    }

    public String getBillReceivedStr() {
        return billReceivedStr;
    }

    public void setBillReceivedStr(String billReceivedStr) {
        this.billReceivedStr = billReceivedStr;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(String issueBy) {
        this.issueBy = issueBy;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Boolean getPurchaseCosts() {
        return purchaseCosts;
    }

    public void setPurchaseCosts(Boolean purchaseCosts) {
        this.purchaseCosts = purchaseCosts;
    }

    public Integer getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(Integer currentBook) {
        this.currentBook = currentBook;
    }

    public List<RefVoucherDTO> getRefVouchers() {
        return refVouchers;
    }

    public void addRefVoucherDTO(RefVoucherDTO refVoucherDTO) {
        if (this.refVouchers == null) {
            this.refVouchers = new ArrayList<>();
        }
        this.refVouchers.add(refVoucherDTO);
    }

    public void setRefVouchers(List<RefVoucherDTO> refVouchers) {
        this.refVouchers = refVouchers;
    }

    public void setPaymentVoucherID(UUID paymentVoucherID) {
        this.paymentVoucherID = paymentVoucherID;
    }

    public UUID getPaymentVoucherID() {
        return paymentVoucherID;
    }

    public String getOtherNoBook() {
        return otherNoBook;
    }

    public void setOtherNoBook(String otherNoBook) {
        this.otherNoBook = otherNoBook;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getNumberAttach() {
        return numberAttach;
    }

    public void setNumberAttach(String numberAttach) {
        this.numberAttach = numberAttach;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getNoBook() {
        return noBook;
    }

    public void setNoBook(String noBook) {
        this.noBook = noBook;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getResultAmount() {
        return resultAmount;
    }

    public void setResultAmount(BigDecimal resultAmount) {
        this.resultAmount = resultAmount;
    }

    public Boolean getBillReceived() {
        return billReceived;
    }

    public void setBillReceived(Boolean billReceived) {
        this.billReceived = billReceived;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public List<PPServiceDetailDTO> getPpServiceDetailDTOS() {
        return ppServiceDetailDTOS;
    }

    public void setPpServiceDetailDTOS(List<PPServiceDetailDTO> ppServiceDetailDTOS) {
        this.ppServiceDetailDTOS = ppServiceDetailDTOS;
    }

    public String getReceiverUserName() {
        return receiverUserName;
    }

    public void setReceiverUserName(String receiverUserName) {
        this.receiverUserName = receiverUserName;
    }

    public String getOtherNumberAttach() {
        return otherNumberAttach;
    }

    public void setOtherNumberAttach(String otherNumberAttach) {
        this.otherNumberAttach = otherNumberAttach;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public UUID getAccountPaymentId() {
        return accountPaymentId;
    }

    public void setAccountPaymentId(UUID accountPaymentId) {
        this.accountPaymentId = accountPaymentId;
    }

    public String getAccountPaymentName() {
        return accountPaymentName;
    }

    public void setAccountPaymentName(String accountPaymentName) {
        this.accountPaymentName = accountPaymentName;
    }

    public UUID getAccountReceiverId() {
        return accountReceiverId;
    }

    public void setAccountReceiverId(UUID accountReceiverId) {
        this.accountReceiverId = accountReceiverId;
    }

    public String getAccountReceiverName() {
        return accountReceiverName;
    }

    public void setAccountReceiverName(String accountReceiverName) {
        this.accountReceiverName = accountReceiverName;
    }

    public UUID getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(UUID creditCardId) {
        this.creditCardId = creditCardId;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getOwnerCreditCard() {
        return ownerCreditCard;
    }

    public void setOwnerCreditCard(String ownerCreditCard) {
        this.ownerCreditCard = ownerCreditCard;
    }
}
