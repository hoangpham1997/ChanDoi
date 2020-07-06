package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RSInwardOutwardSearchDTO {
    private UUID refID;
    private UUID id;
    private LocalDate date;
    private LocalDate postedDate;
    private String noFBook;
    private String noMBook;
    private String accountingObjectName;
    private String typeName;
    private String reason;
    private BigDecimal totalAmount;
    private Integer typeID;
    private Boolean recorded;
    private BigDecimal sumTotalAmount;
    private Integer refTypeID;
    private Integer refInvoiceForm;
    private String refInvoiceNo;
    private Boolean refIsBill;
    private Integer typeLedger;
    private Boolean isDeliveryVoucher;

    public Integer getRefTypeID() {
        return refTypeID;
    }

    public void setRefTypeID(Integer refTypeID) {
        this.refTypeID = refTypeID;
    }

    public RSInwardOutwardSearchDTO(UUID refID, UUID id, Integer typeID) {
        this.refID = refID;
        this.id = id;
        this.typeID = typeID;
    }

    public RSInwardOutwardSearchDTO(UUID refID, UUID id, LocalDate date, LocalDate postedDate, String noFBook, String noMBook,
                                    String accountingObjectName, String typeName, String reason, BigDecimal totalAmount,
                                    Integer typeID, Boolean recorded
    ) {
        this.id = id;
        this.date = date;
        this.postedDate = postedDate;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.accountingObjectName = accountingObjectName;
        this.typeName = typeName;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.typeID = typeID;
        this.refID = refID;
        this.recorded = recorded;
    }

    public RSInwardOutwardSearchDTO(UUID refID, UUID id, LocalDate date, LocalDate postedDate, String noFBook, String noMBook,
                                    String accountingObjectName, String typeName, String reason, BigDecimal totalAmount,
                                    Integer typeID, Boolean recorded, Boolean isDeliveryVoucher, Integer refTypeID, Integer refInvoiceForm, String refInvoiceNo, Boolean refIsBill
    ) {
        this.id = id;
        this.date = date;
        this.postedDate = postedDate;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.accountingObjectName = accountingObjectName;
        this.typeName = typeName;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.typeID = typeID;
        this.refID = refID;
        this.recorded = recorded;
        this.refTypeID = refTypeID;
        this.refInvoiceForm = refInvoiceForm;
        this.refInvoiceNo = refInvoiceNo;
        this.refIsBill = refIsBill;
        this.isDeliveryVoucher = isDeliveryVoucher;
    }

    public RSInwardOutwardSearchDTO(UUID id, LocalDate date, LocalDate postedDate, String noFBook, String noMBook, String accountingObjectName, String typeName, String reason, BigDecimal totalAmount, Integer typeID, Boolean recorded, Integer typeLedger) {
        this.id = id;
        this.date = date;
        this.postedDate = postedDate;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.accountingObjectName = accountingObjectName;
        this.typeName = typeName;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.typeID = typeID;
        this.recorded = recorded;
        this.typeLedger = typeLedger;
    }

    public RSInwardOutwardSearchDTO() {
    }

    public Boolean getDeliveryVoucher() {
        return isDeliveryVoucher;
    }

    public void setDeliveryVoucher(Boolean deliveryVoucher) {
        isDeliveryVoucher = deliveryVoucher;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public BigDecimal getSumTotalAmount() {
        return sumTotalAmount;
    }

    public void setSumTotalAmount(BigDecimal sumTotalAmount) {
        this.sumTotalAmount = sumTotalAmount;
    }

    public String getRefInvoiceNo() {
        return refInvoiceNo;
    }

    public void setRefInvoiceNo(String refInvoiceNo) {
        this.refInvoiceNo = refInvoiceNo;
    }

    public Boolean getRefIsBill() {
        return refIsBill;
    }

    public void setRefIsBill(Boolean refIsBill) {
        this.refIsBill = refIsBill;
    }

    public Integer getRefInvoiceForm() {
        return refInvoiceForm;
    }

    public void setRefInvoiceForm(Integer refInvoiceForm) {
        this.refInvoiceForm = refInvoiceForm;
    }
}
