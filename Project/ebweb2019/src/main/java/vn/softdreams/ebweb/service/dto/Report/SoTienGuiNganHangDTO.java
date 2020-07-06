package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoTienGuiNganHangDTO {
    private Integer rowNum;
    private UUID bankAccountDetailID;
    private String bankAccount;
    private String bankName;
    private LocalDate date;
    private String dateToString;
    private LocalDate postedDate;
    private String postedDateToString;
    private String no;
    private String journalMemo;
    private String accountCorresponding;
    private BigDecimal soThu;
    private String soThuToString;
    private BigDecimal soChi;
    private String soChiToString;
    private BigDecimal soTon;
    private String soTonToString;
    private Integer orderPriority;
    private Integer positionOrder;
    private Integer typeID;
    private UUID refID;
    private String linkRef;

    public SoTienGuiNganHangDTO(Integer rowNum, UUID bankAccountDetailID, String bankAccount, String bankName, LocalDate date, LocalDate postedDate, String no, String journalMemo, String accountCorresponding, BigDecimal soThu, BigDecimal soChi, BigDecimal soTon, Integer orderPriority, Integer positionOrder, Integer typeID, UUID refID) {
        this.rowNum = rowNum;
        this.bankAccountDetailID = bankAccountDetailID;
        this.bankAccount = bankAccount;
        this.bankName = bankName;
        this.date = date;
        this.postedDate = postedDate;
        this.no = no;
        this.journalMemo = journalMemo;
        this.accountCorresponding = accountCorresponding;
        this.soThu = soThu;
        this.soChi = soChi;
        this.soTon = soTon;
        this.orderPriority = orderPriority;
        this.positionOrder = positionOrder;
        this.typeID = typeID;
        this.refID = refID;
    }

    public SoTienGuiNganHangDTO() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public UUID getBankAccountDetailID() {
        return bankAccountDetailID;
    }

    public void setBankAccountDetailID(UUID bankAccountDetailID) {
        this.bankAccountDetailID = bankAccountDetailID;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getJournalMemo() {
        return journalMemo;
    }

    public void setJournalMemo(String journalMemo) {
        this.journalMemo = journalMemo;
    }

    public String getAccountCorresponding() {
        return accountCorresponding;
    }

    public void setAccountCorresponding(String accountCorresponding) {
        this.accountCorresponding = accountCorresponding;
    }

    public BigDecimal getSoThu() {
        return soThu;
    }

    public void setSoThu(BigDecimal soThu) {
        this.soThu = soThu;
    }

    public String getSoThuToString() {
        return soThuToString;
    }

    public void setSoThuToString(String soThuToString) {
        this.soThuToString = soThuToString;
    }

    public BigDecimal getSoChi() {
        return soChi;
    }

    public void setSoChi(BigDecimal soChi) {
        this.soChi = soChi;
    }

    public String getSoChiToString() {
        return soChiToString;
    }

    public void setSoChiToString(String soChiToString) {
        this.soChiToString = soChiToString;
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

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public Integer getPositionOrder() {
        return positionOrder;
    }

    public void setPositionOrder(Integer positionOrder) {
        this.positionOrder = positionOrder;
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

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }
}

