package vn.softdreams.ebweb.service.dto.Report;

import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoQuyTienMatDTO {
    private Integer rowNum;
    private UUID refID;
    private LocalDate date;
    private String dateToString;
    private LocalDate postedDate;
    private String postedDateToString;
    private String receiptRefNo;
    private String paymentRefNo;
    private String journalMemo;
    private BigDecimal totalReceiptFBCurrencyID;
    private String totalReceiptFBCurrencyIDToString;
    private BigDecimal totalPaymentFBCurrencyID;
    private String totalPaymentFBCurrencyIDToString;
    private BigDecimal closingFBCurrencyID;
    private String closingFBCurrencyIDToString;
    private String note;
    private Integer cAType;
    private Integer orderPriority;
    private Integer typeID;
    private String linkRef;

    public SoQuyTienMatDTO(Integer rowNum, UUID refID, LocalDate date, LocalDate postedDate, String receiptRefNo, String paymentRefNo, String journalMemo, BigDecimal totalReceiptFBCurrencyID, BigDecimal totalPaymentFBCurrencyID, BigDecimal closingFBCurrencyID, String note, Integer cAType, Integer orderPriority, Integer typeID) {
        this.rowNum = rowNum;
        this.refID = refID;
        this.date = date;
        this.dateToString = "";
        this.postedDate = postedDate;
        this.postedDateToString = "";
        this.receiptRefNo = receiptRefNo;
        this.paymentRefNo = paymentRefNo;
        this.journalMemo = journalMemo;
        this.totalReceiptFBCurrencyID = totalReceiptFBCurrencyID;
        this.totalPaymentFBCurrencyID = totalPaymentFBCurrencyID;
        this.closingFBCurrencyID = closingFBCurrencyID;
        this.note = note;
        this.cAType = cAType;
        this.orderPriority = orderPriority;
        this.typeID = typeID;
    }

    public SoQuyTienMatDTO() {
    }

    public String getTotalReceiptFBCurrencyIDToString() {
        return totalReceiptFBCurrencyIDToString;
    }

    public void setTotalReceiptFBCurrencyIDToString(String totalReceiptFBCurrencyIDToString) {
        this.totalReceiptFBCurrencyIDToString = totalReceiptFBCurrencyIDToString;
    }

    public String getTotalPaymentFBCurrencyIDToString() {
        return totalPaymentFBCurrencyIDToString;
    }

    public void setTotalPaymentFBCurrencyIDToString(String totalPaymentFBCurrencyIDToString) {
        this.totalPaymentFBCurrencyIDToString = totalPaymentFBCurrencyIDToString;
    }

    public String getClosingFBCurrencyIDToString() {
        return closingFBCurrencyIDToString;
    }

    public void setClosingFBCurrencyIDToString(String closingFBCurrencyIDToString) {
        this.closingFBCurrencyIDToString = closingFBCurrencyIDToString;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
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

    public String getReceiptRefNo() {
        return receiptRefNo;
    }

    public void setReceiptRefNo(String receiptRefNo) {
        this.receiptRefNo = receiptRefNo;
    }

    public String getPaymentRefNo() {
        return paymentRefNo;
    }

    public void setPaymentRefNo(String paymentRefNo) {
        this.paymentRefNo = paymentRefNo;
    }

    public String getJournalMemo() {
        return journalMemo;
    }

    public void setJournalMemo(String journalMemo) {
        this.journalMemo = journalMemo;
    }

    public BigDecimal getTotalReceiptFBCurrencyID() {
        return totalReceiptFBCurrencyID;
    }

    public void setTotalReceiptFBCurrencyID(BigDecimal totalReceiptFBCurrencyID) {
        this.totalReceiptFBCurrencyID = totalReceiptFBCurrencyID;
    }

    public BigDecimal getTotalPaymentFBCurrencyID() {
        return totalPaymentFBCurrencyID;
    }

    public void setTotalPaymentFBCurrencyID(BigDecimal totalPaymentFBCurrencyID) {
        this.totalPaymentFBCurrencyID = totalPaymentFBCurrencyID;
    }

    public BigDecimal getClosingFBCurrencyID() {
        return closingFBCurrencyID;
    }

    public void setClosingFBCurrencyID(BigDecimal closingFBCurrencyID) {
        this.closingFBCurrencyID = closingFBCurrencyID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getcAType() {
        return cAType;
    }

    public void setcAType(Integer cAType) {
        this.cAType = cAType;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public String getPostedDateToString() {
        return postedDateToString;
    }

    public void setPostedDateToString(String postedDateToString) {
        this.postedDateToString = postedDateToString;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }
}

