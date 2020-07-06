package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoKeToanChiTietQuyTienMatDTO {
    private LocalDate date;
    private UUID refID;
    private String dateToString;
    private LocalDate postedDate;
    private String postedDateToString;
    private String receiptRefNo;
    private String paymentRefNo;
    private String journalMemo;
    private String account;
    private String accountCorresponding;
    private BigDecimal phatSinhNo;
    private String phatSinhNoToString;
    private BigDecimal phatSinhCo;
    private String phatSinhCoToString;
    private BigDecimal soTon;
    private String soTonToString;
    private String note;
    private Integer positionOrder;
    private Integer typeID;
    private String linkRef;

    public SoKeToanChiTietQuyTienMatDTO(LocalDate date, UUID refID, LocalDate postedDate, String receiptRefNo, String paymentRefNo, String journalMemo, String account, String accountCorresponding, BigDecimal phatSinhNo, BigDecimal phatSinhCo, BigDecimal soTon, String note, Integer positionOrder, Integer typeID) {
        this.date = date;
        this.postedDate = postedDate;
        this.receiptRefNo = receiptRefNo;
        this.paymentRefNo = paymentRefNo;
        this.journalMemo = journalMemo;
        this.account = account;
        this.accountCorresponding = accountCorresponding;
        this.phatSinhNo = phatSinhNo;
        this.phatSinhCo = phatSinhCo;
        this.soTon = soTon;
        this.note = note;
        this.positionOrder = positionOrder;
        this.typeID = typeID;
        this.refID = refID;
    }

    public Integer getPositionOrder() {
        return positionOrder;
    }

    public void setPositionOrder(Integer positionOrder) {
        this.positionOrder = positionOrder;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDateToString() {
        return dateToString;
    }

    public void setDateToString(String dateToString) {
        this.dateToString = dateToString;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getPostedDateToString() {
        return postedDateToString;
    }

    public void setPostedDateToString(String postedDateToString) {
        this.postedDateToString = postedDateToString;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountCorresponding() {
        return accountCorresponding;
    }

    public void setAccountCorresponding(String accountCorresponding) {
        this.accountCorresponding = accountCorresponding;
    }

    public BigDecimal getPhatSinhNo() {
        return phatSinhNo;
    }

    public void setPhatSinhNo(BigDecimal phatSinhNo) {
        this.phatSinhNo = phatSinhNo;
    }

    public String getPhatSinhNoToString() {
        return phatSinhNoToString;
    }

    public void setPhatSinhNoToString(String phatSinhNoToString) {
        this.phatSinhNoToString = phatSinhNoToString;
    }

    public BigDecimal getPhatSinhCo() {
        return phatSinhCo;
    }

    public void setPhatSinhCo(BigDecimal phatSinhCo) {
        this.phatSinhCo = phatSinhCo;
    }

    public String getPhatSinhCoToString() {
        return phatSinhCoToString;
    }

    public void setPhatSinhCoToString(String phatSinhCoToString) {
        this.phatSinhCoToString = phatSinhCoToString;
    }

    public BigDecimal getSoTon() {
        return soTon;
    }

    public void setSoTon(BigDecimal soTon) {
        this.soTon = soTon;
    }

    public String getSoTonToString() {
        return soTonToString;
    }

    public void setSoTonToString(String soTonToString) {
        this.soTonToString = soTonToString;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }
}

