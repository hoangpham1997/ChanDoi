package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoCaiHTNhatKyChungDTO {
    private LocalDate postedDate;    // Ngày tháng ghi sổ
    private String postedDateString;    // Ngày tháng ghi sổ
    private LocalDate date;    // Ngày tháng số hiệu
    private String dateString;    // Ngày tháng số hiệu
    private String no;    // Số hiệu
    private String journalMemo; //Diễn giải
    private String accountNumber; //Tài khoản
    private String accountName; //Tài khoản
    private Boolean isParent; //Tài khoản
    private String detailAccountNumber; //Tài khoản đối ứng
    private String correspondingAccountNumber;
    private BigDecimal openningDebitAmount; //Số dư đầu kỳ có
    private BigDecimal openningCreditAmount; //Số dư đầu kỳ nợ
    private BigDecimal closingDebitAmount; //Cộng phát sinh có
    private BigDecimal closingCreditAmount; //Cộng phát sinh nợ
    private BigDecimal accumDebitAmount; //Số tiền nợ
    private BigDecimal accumCreditAmount; //Số tiền nợ
    private BigDecimal debitAmount; //Số tiền nợ
    private String debitAmountString; //Số tiền nợ
    private BigDecimal creditAmount; //Số tiền có
    private String creditAmountString; //Số tiền có
    private int accountCategoryKind;
    private UUID referenceID;
    private Integer refType;
    private String linkRef;
    private int orderPriority;
    private int SizeDetail;
    private boolean breakPage;
    private String openningDebitAmountString; //Số dư đầu kỳ có
    private String openningCreditAmountString; //Số dư đầu kỳ nợ
    private String closingDebitAmountString; //Cộng phát sinh có
    private String closingCreditAmountString; //Cộng phát sinh nợ
    private String accumDebitAmountString; //Số tiền nợ
    private String accumCreditAmountString; //Số tiền nợ

    private String nextAccountNumber;
    private String congSoPhatSinhNo;
    private String congSoPhatSinhCo;
    private String soDuCuoiKyNo;
    private String soDuCuoiKyCo;
    private String congLuyKeNo;
    private String congLuyKeCo;
    private BigDecimal congSoPhatSinhNoAm;
    private BigDecimal congSoPhatSinhCoAm;
    private BigDecimal soDuCuoiKyNoAm;
    private BigDecimal soDuCuoiKyCoAm;
    private BigDecimal congLuyKeNoAm;
    private BigDecimal congLuyKeCoAm;
    private boolean headerDetail;
    private boolean isBold;

    public SoCaiHTNhatKyChungDTO() {
    }

    public SoCaiHTNhatKyChungDTO(String accountNumber, String accountName, Boolean isParent, BigDecimal openningDebitAmount, BigDecimal openningCreditAmount, BigDecimal closingDebitAmount, BigDecimal closingCreditAmount, BigDecimal accumDebitAmount, BigDecimal accumCreditAmount, int accountCategoryKind) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.isParent = isParent;
        this.openningDebitAmount = openningDebitAmount;
        this.openningCreditAmount = openningCreditAmount;
        this.closingDebitAmount = closingDebitAmount;
        this.closingCreditAmount = closingCreditAmount;
        this.accumDebitAmount = accumDebitAmount;
        this.accumCreditAmount = accumCreditAmount;
        this.accountCategoryKind = accountCategoryKind;
    }

    public SoCaiHTNhatKyChungDTO(LocalDate postedDate, LocalDate date, String no, String journalMemo, String accountNumber, String detailAccountNumber, String correspondingAccountNumber, BigDecimal debitAmount, BigDecimal creditAmount, Integer refType, UUID referenceID) {
        this.postedDate = postedDate;
        this.date = date;
        this.no = no;
        this.journalMemo = journalMemo;
        this.accountNumber = accountNumber;
        this.detailAccountNumber = detailAccountNumber;
        this.correspondingAccountNumber = correspondingAccountNumber;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.refType = refType;
        this.referenceID = referenceID;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
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

    public String getJournalMemo() {
        return journalMemo;
    }

    public void setJournalMemo(String journalMemo) {
        this.journalMemo = journalMemo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDetailAccountNumber() {
        return detailAccountNumber;
    }

    public void setDetailAccountNumber(String detailAccountNumber) {
        this.detailAccountNumber = detailAccountNumber;
    }

    public String getCorrespondingAccountNumber() {
        return correspondingAccountNumber;
    }

    public void setCorrespondingAccountNumber(String correspondingAccountNumber) {
        this.correspondingAccountNumber = correspondingAccountNumber;
    }

    public BigDecimal getOpenningDebitAmount() {
        return openningDebitAmount;
    }

    public void setOpenningDebitAmount(BigDecimal openningDebitAmount) {
        this.openningDebitAmount = openningDebitAmount;
    }

    public BigDecimal getOpenningCreditAmount() {
        return openningCreditAmount;
    }

    public void setOpenningCreditAmount(BigDecimal openningCreditAmount) {
        this.openningCreditAmount = openningCreditAmount;
    }

    public BigDecimal getClosingDebitAmount() {
        return closingDebitAmount;
    }

    public void setClosingDebitAmount(BigDecimal closingDebitAmount) {
        this.closingDebitAmount = closingDebitAmount;
    }

    public BigDecimal getClosingCreditAmount() {
        return closingCreditAmount;
    }

    public void setClosingCreditAmount(BigDecimal closingCreditAmount) {
        this.closingCreditAmount = closingCreditAmount;
    }

    public BigDecimal getAccumDebitAmount() {
        return accumDebitAmount;
    }

    public void setAccumDebitAmount(BigDecimal accumDebitAmount) {
        this.accumDebitAmount = accumDebitAmount;
    }

    public BigDecimal getAccumCreditAmount() {
        return accumCreditAmount;
    }

    public void setAccumCreditAmount(BigDecimal accumCreditAmount) {
        this.accumCreditAmount = accumCreditAmount;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public int getAccountCategoryKind() {
        return accountCategoryKind;
    }

    public void setAccountCategoryKind(int accountCategoryKind) {
        this.accountCategoryKind = accountCategoryKind;
    }

    public UUID getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public int getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(int orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getDebitAmountString() {
        return debitAmountString;
    }

    public void setDebitAmountString(String debitAmountString) {
        this.debitAmountString = debitAmountString;
    }

    public String getCreditAmountString() {
        return creditAmountString;
    }

    public void setCreditAmountString(String creditAmountString) {
        this.creditAmountString = creditAmountString;
    }

    public String getPostedDateString() {
        return postedDateString;
    }

    public void setPostedDateString(String postedDateString) {
        this.postedDateString = postedDateString;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public int getSizeDetail() {
        return SizeDetail;
    }

    public void setSizeDetail(int sizeDetail) {
        SizeDetail = sizeDetail;
    }

    public boolean isBreakPage() {
        return breakPage;
    }

    public void setBreakPage(boolean breakPage) {
        this.breakPage = breakPage;
    }

    public String getOpenningDebitAmountString() {
        return openningDebitAmountString;
    }

    public void setOpenningDebitAmountString(String openningDebitAmountString) {
        this.openningDebitAmountString = openningDebitAmountString;
    }

    public String getOpenningCreditAmountString() {
        return openningCreditAmountString;
    }

    public void setOpenningCreditAmountString(String openningCreditAmountString) {
        this.openningCreditAmountString = openningCreditAmountString;
    }

    public String getClosingDebitAmountString() {
        return closingDebitAmountString;
    }

    public void setClosingDebitAmountString(String closingDebitAmountString) {
        this.closingDebitAmountString = closingDebitAmountString;
    }

    public String getClosingCreditAmountString() {
        return closingCreditAmountString;
    }

    public void setClosingCreditAmountString(String closingCreditAmountString) {
        this.closingCreditAmountString = closingCreditAmountString;
    }

    public String getAccumDebitAmountString() {
        return accumDebitAmountString;
    }

    public void setAccumDebitAmountString(String accumDebitAmountString) {
        this.accumDebitAmountString = accumDebitAmountString;
    }

    public String getAccumCreditAmountString() {
        return accumCreditAmountString;
    }

    public void setAccumCreditAmountString(String accumCreditAmountString) {
        this.accumCreditAmountString = accumCreditAmountString;
    }

    public String getNextAccountNumber() {
        return nextAccountNumber;
    }

    public void setNextAccountNumber(String nextAccountNumber) {
        this.nextAccountNumber = nextAccountNumber;
    }

    public String getCongSoPhatSinhNo() {
        return congSoPhatSinhNo;
    }

    public void setCongSoPhatSinhNo(String congSoPhatSinhNo) {
        this.congSoPhatSinhNo = congSoPhatSinhNo;
    }

    public String getCongSoPhatSinhCo() {
        return congSoPhatSinhCo;
    }

    public void setCongSoPhatSinhCo(String congSoPhatSinhCo) {
        this.congSoPhatSinhCo = congSoPhatSinhCo;
    }

    public boolean isHeaderDetail() {
        return headerDetail;
    }

    public void setHeaderDetail(boolean headerDetail) {
        this.headerDetail = headerDetail;
    }

    public String getSoDuCuoiKyNo() {
        return soDuCuoiKyNo;
    }

    public void setSoDuCuoiKyNo(String soDuCuoiKyNo) {
        this.soDuCuoiKyNo = soDuCuoiKyNo;
    }

    public String getSoDuCuoiKyCo() {
        return soDuCuoiKyCo;
    }

    public void setSoDuCuoiKyCo(String soDuCuoiKyCo) {
        this.soDuCuoiKyCo = soDuCuoiKyCo;
    }

    public String getCongLuyKeNo() {
        return congLuyKeNo;
    }

    public void setCongLuyKeNo(String congLuyKeNo) {
        this.congLuyKeNo = congLuyKeNo;
    }

    public String getCongLuyKeCo() {
        return congLuyKeCo;
    }

    public void setCongLuyKeCo(String congLuyKeCo) {
        this.congLuyKeCo = congLuyKeCo;
    }

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public BigDecimal getCongSoPhatSinhNoAm() {
        return congSoPhatSinhNoAm;
    }

    public void setCongSoPhatSinhNoAm(BigDecimal congSoPhatSinhNoAm) {
        this.congSoPhatSinhNoAm = congSoPhatSinhNoAm;
    }

    public BigDecimal getCongSoPhatSinhCoAm() {
        return congSoPhatSinhCoAm;
    }

    public void setCongSoPhatSinhCoAm(BigDecimal congSoPhatSinhCoAm) {
        this.congSoPhatSinhCoAm = congSoPhatSinhCoAm;
    }

    public BigDecimal getSoDuCuoiKyNoAm() {
        return soDuCuoiKyNoAm;
    }

    public void setSoDuCuoiKyNoAm(BigDecimal soDuCuoiKyNoAm) {
        this.soDuCuoiKyNoAm = soDuCuoiKyNoAm;
    }

    public BigDecimal getSoDuCuoiKyCoAm() {
        return soDuCuoiKyCoAm;
    }

    public void setSoDuCuoiKyCoAm(BigDecimal soDuCuoiKyCoAm) {
        this.soDuCuoiKyCoAm = soDuCuoiKyCoAm;
    }

    public BigDecimal getCongLuyKeNoAm() {
        return congLuyKeNoAm;
    }

    public void setCongLuyKeNoAm(BigDecimal congLuyKeNoAm) {
        this.congLuyKeNoAm = congLuyKeNoAm;
    }

    public BigDecimal getCongLuyKeCoAm() {
        return congLuyKeCoAm;
    }

    public void setCongLuyKeCoAm(BigDecimal congLuyKeCoAm) {
        this.congLuyKeCoAm = congLuyKeCoAm;
    }
}
