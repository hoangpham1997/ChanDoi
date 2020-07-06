package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.service.util.Constants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PPInvoiceSearchDTO {
    private UUID id;
    private LocalDate date;
    private LocalDate postedDate;
    private Integer typeLedger;
    private String noFBook;
    private String noMBook;
    private String accountingObjectName;
    private BigDecimal totalAmount;
    private BigDecimal totalDiscountAmount;
    private BigDecimal totalVATAmount;
    private Boolean billReceived;
    private Boolean isImportPurchase;
    private Boolean recorded;
    private Integer typeId;
    private String currencyId;
    private String ppOrderNo;

    private String no;
    private String typeIdStr;
    private BigDecimal amountTT;
    private String billReceivedStr;
    private BigDecimal sumTotalAmount;
    private UUID paymentVoucherId;
    private UUID rsInwardOutwardId;

    public PPInvoiceSearchDTO() {
    }

    public PPInvoiceSearchDTO(UUID id, LocalDate date, LocalDate postedDate, Integer typeLedger, String no, String accountingObjectName, BigDecimal totalAmount, BigDecimal totalDiscountAmount, BigDecimal totalVATAmount, Boolean billReceived, Boolean isImportPurchase, Boolean recorded, Integer typeId, String currencyId, String ppOrderNo, String typeIdStr, UUID paymentVoucherId, String noFBook, String noMBook, UUID rsInwardOutwardId) {
        this.id = id;
        this.date = date;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.no = no;
        this.accountingObjectName = accountingObjectName;
        this.totalAmount = totalAmount;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalVATAmount = totalVATAmount;
        this.billReceived = billReceived;
        this.isImportPurchase = isImportPurchase;
        this.recorded = recorded;
        this.typeId = typeId;
        this.currencyId = currencyId;
        this.ppOrderNo = ppOrderNo;
        this.typeIdStr = typeIdStr;
        this.paymentVoucherId = paymentVoucherId;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.rsInwardOutwardId = rsInwardOutwardId;
//        switch (typeId) {
//            case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_CHUA_THANH_TOAN:
//                this.typeIdStr = Constants.PPInvoiceType.MUA_HANG_CHUA_THANH_TOAN;
//                break;
//            case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_TIEN_MAT:
//                this.typeIdStr = Constants.PPInvoiceType.MUA_HANG_TIEN_MAT;
//                break;
//            case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_UY_NHIEM_CHI:
//                this.typeIdStr = Constants.PPInvoiceType.MUA_HANG_UY_NHIEM_CHI;
//                break;
//            case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_CK:
//                this.typeIdStr = Constants.PPInvoiceType.MUA_HANG_SEC_CK;
//                break;
//            case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_SEC_TIEN_MAT:
//                this.typeIdStr = Constants.PPInvoiceType.MUA_HANG_SEC_TIEN_MAT;
//                break;
//            case Constants.PPInvoiceType.TYPE_ID_MUA_HANG_THE_TIN_DUNG:
//                this.typeIdStr = Constants.PPInvoiceType.MUA_HANG_THE_TIN_DUNG;
//                break;
//        }
        if (isImportPurchase) {
            if (totalAmount != null && totalDiscountAmount != null) {
                this.amountTT = totalAmount.subtract(totalDiscountAmount);
            }
        } else {
            if (totalAmount != null && totalDiscountAmount != null) {
                this.amountTT = totalAmount.subtract(totalDiscountAmount);
                if (this.amountTT != null && totalVATAmount != null) {
                    this.amountTT = this.amountTT.add(totalVATAmount);
                }
            }
        }
        if (billReceived) {
            billReceivedStr = Constants.PPInvoiceType.DA_NHAN_HOA_DON;
        } else {
            billReceivedStr = Constants.PPInvoiceType.CHUA_NHAN_HOA_DON;
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
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

    public Boolean getBillReceived() {
        return billReceived;
    }

    public void setBillReceived(Boolean billReceived) {
        this.billReceived = billReceived;
    }

    public Boolean getImportPurchase() {
        return isImportPurchase;
    }

    public void setImportPurchase(Boolean importPurchase) {
        isImportPurchase = importPurchase;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getPpOrderNo() {
        return ppOrderNo;
    }

    public void setPpOrderNo(String ppOrderNo) {
        this.ppOrderNo = ppOrderNo;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTypeIdStr() {
        return typeIdStr;
    }

    public void setTypeIdStr(String typeIdStr) {
        this.typeIdStr = typeIdStr;
    }

    public BigDecimal getAmountTT() {
        return amountTT;
    }

    public void setAmountTT(BigDecimal amountTT) {
        this.amountTT = amountTT;
    }

    public String getBillReceivedStr() {
        return billReceivedStr;
    }

    public void setBillReceivedStr(String billReceivedStr) {
        this.billReceivedStr = billReceivedStr;
    }

    public BigDecimal getSumTotalAmount() {
        return sumTotalAmount;
    }

    public void setSumTotalAmount(BigDecimal sumTotalAmount) {
        this.sumTotalAmount = sumTotalAmount;
    }

    public UUID getPaymentVoucherId() {
        return paymentVoucherId;
    }

    public void setPaymentVoucherId(UUID paymentVoucherId) {
        this.paymentVoucherId = paymentVoucherId;
    }

    public UUID getRsInwardOutwardId() {
        return rsInwardOutwardId;
    }

    public void setRsInwardOutwardId(UUID rsInwardOutwardId) {
        this.rsInwardOutwardId = rsInwardOutwardId;
    }
}
