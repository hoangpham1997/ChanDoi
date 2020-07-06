package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SoNhatKyThuTienDTO {
    private LocalDate postedDate;    // Ngày hạch toán
    private String postedDateString;    // Ngày hạch toán
    private LocalDate date;    // Ngày chứng từ
    private String dateString;    // Ngày chứng từ
    private String no;    // Số chứng từ
    private String description;    // Diễn giải
    private String accountNumber;    // Tài khoản
    private Integer refType;
    private UUID refID;
    private String correspondingAccountNumber;    // Tài khoản đối ứng
    private BigDecimal amount;
    private BigDecimal col2;
    private BigDecimal col3;
    private BigDecimal col4;
    private BigDecimal col5;
    private BigDecimal col6;
    private String amountString;
    private String col2String;
    private String col3String;
    private String col4String;
    private String col5String;
    private String col6String;
    private String colOtherAccount;
    private String accountNumberList;
    private String accountDebit;
    private String account2;
    private String account3;
    private String account4;
    private String account5;
    private boolean breakPage;
    private Integer sortOrder;
    private Integer detailPostOrder;
    private Integer orderPriority;
    private int sizeDetail;
    private boolean headerDetail;
    private boolean boldStyle;
    private String linkRef;

    public SoNhatKyThuTienDTO(Integer refType, UUID refID, LocalDate postedDate, String no, LocalDate date, String description,
                              String accountNumber, BigDecimal amount, BigDecimal col2, BigDecimal col3, BigDecimal col4, BigDecimal col5, BigDecimal col6,
                              String colOtherAccount, String accountNumberList) {
        this.refType = refType;
        this.refID = refID;
        this.postedDate = postedDate;
        this.no = no;
        this.date = date;
        this.description = description;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
        this.col5 = col5;
        this.col6 = col6;
        this.colOtherAccount = colOtherAccount;
        this.accountNumberList = accountNumberList;
        this.boldStyle = false;
    }

    public SoNhatKyThuTienDTO() {
        this.breakPage = false;
    }

    public boolean isBoldStyle() {
        return boldStyle;
    }

    public void setBoldStyle(boolean boldStyle) {
        this.boldStyle = boldStyle;
    }

    public SoNhatKyThuTienDTO(String description, String amountString, String col2String, String col3String,
                              String col4String, String col5String, String col6String) {
        this.description = description;
        this.amountString = amountString;
        this.col2String = col2String;
        this.col3String = col3String;
        this.col4String = col4String;
        this.col5String = col5String;
        this.col6String = col6String;
        this.boldStyle = true;
    }

    public boolean isHeaderDetail() {
        return headerDetail;
    }

    public void setHeaderDetail(boolean headerDetail) {
        this.headerDetail = headerDetail;
    }

    public boolean isBreakPage() {
        return breakPage;
    }

    public void setBreakPage(boolean breakPage) {
        this.breakPage = breakPage;
    }

    public int getSizeDetail() {
        return sizeDetail;
    }

    public void setSizeDetail(int sizeDetail) {
        this.sizeDetail = sizeDetail;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getPostedDateString() {
        return postedDateString;
    }

    public void setPostedDateString(String postedDateString) {
        this.postedDateString = postedDateString;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
    }

    public UUID getRefID() {
        return refID;
    }

    public void setRefID(UUID refID) {
        this.refID = refID;
    }

    public String getCorrespondingAccountNumber() {
        return correspondingAccountNumber;
    }

    public void setCorrespondingAccountNumber(String correspondingAccountNumber) {
        this.correspondingAccountNumber = correspondingAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCol2() {
        return col2 != null ? col2 : BigDecimal.ZERO;
    }

    public void setCol2(BigDecimal col2) {
        this.col2 = col2;
    }

    public BigDecimal getCol3() {
        return col3 != null ? col3 : BigDecimal.ZERO;
    }

    public void setCol3(BigDecimal col3) {
        this.col3 = col3;
    }

    public BigDecimal getCol4() {
        return col4 != null ? col4 : BigDecimal.ZERO;
    }

    public void setCol4(BigDecimal col4) {
        this.col4 = col4;
    }

    public BigDecimal getCol5() {
        return col5 != null ? col5 : BigDecimal.ZERO;
    }

    public void setCol5(BigDecimal col5) {
        this.col5 = col5;
    }

    public BigDecimal getCol6() {
        return col6 != null ? col6 : BigDecimal.ZERO;
    }

    public void setCol6(BigDecimal col6) {
        this.col6 = col6;
    }


    public String getColOtherAccount() {
        return colOtherAccount;
    }

    public void setColOtherAccount(String colOtherAccount) {
        this.colOtherAccount = colOtherAccount;
    }

    public String getAccountNumberList() {
        return accountNumberList;
    }

    public void setAccountNumberList(String accountNumberList) {
        this.accountNumberList = accountNumberList;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getDetailPostOrder() {
        return detailPostOrder;
    }

    public void setDetailPostOrder(Integer detailPostOrder) {
        this.detailPostOrder = detailPostOrder;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getCol2String() {
        return col2String;
    }

    public void setCol2String(String col2String) {
        this.col2String = col2String;
    }

    public String getCol3String() {
        return col3String;
    }

    public void setCol3String(String col3String) {
        this.col3String = col3String;
    }

    public String getCol4String() {
        return col4String;
    }

    public void setCol4String(String col4String) {
        this.col4String = col4String;
    }

    public String getCol5String() {
        return col5String;
    }

    public void setCol5String(String col5String) {
        this.col5String = col5String;
    }

    public String getCol6String() {
        return col6String;
    }

    public void setCol6String(String col6String) {
        this.col6String = col6String;
    }

    public String getAccount2() {
        return account2;
    }

    public void setAccount2(String account2) {
        this.account2 = account2;
    }

    public String getAccount3() {
        return account3;
    }

    public void setAccount3(String account3) {
        this.account3 = account3;
    }

    public String getAccount4() {
        return account4;
    }

    public void setAccount4(String account4) {
        this.account4 = account4;
    }

    public String getAccount5() {
        return account5;
    }

    public void setAccount5(String account5) {
        this.account5 = account5;
    }

    public String getAccountDebit() {
        return accountDebit;
    }

    public void setAccountDebit(String accountDebit) {
        this.accountDebit = accountDebit;
    }

    public String getLinkRef() {
        return linkRef;
    }

    public void setLinkRef(String linkRef) {
        this.linkRef = linkRef;
    }

    @Override
    public String toString() {
        return "SoNhatKyThuTienDTO{" +
            "amountString='" + amountString + '\'' +
            ", col2String='" + col2String + '\'' +
            ", col3String='" + col3String + '\'' +
            ", col4String='" + col4String + '\'' +
            ", col5String='" + col5String + '\'' +
            ", col6String='" + col6String + '\'' +
            '}';
    }
}
