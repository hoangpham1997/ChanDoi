package vn.softdreams.ebweb.service.dto.Report;

import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.util.UUID;

public class GiaThanhPoPupDTO {
    private UUID costSetID;
    private String costSetCode;
    private String postedDate;
    private String date;
    private String no;
    private String description;
    private BigDecimal amount;
    private String accountNumber;
    private String expenseItemCode;
    private Integer expenseItemType;
    private UUID expenseItemID;
    private Integer typeVoucher;
    private Integer typeID;
    private Integer typeGroupID;
    private UUID refID2;

    public GiaThanhPoPupDTO() {
    }

    public GiaThanhPoPupDTO(UUID costSetID, String costSetCode, String postedDate, String date, String no, String description, BigDecimal amount, String accountNumber, String expenseItemCode, Integer expenseItemType, UUID expenseItemID, Integer typeVoucher, Integer typeID, Integer typeGroupID, UUID refID2) {
        this.costSetID = costSetID;
        this.costSetCode = costSetCode;
        this.postedDate = postedDate;
        this.date = date;
        this.no = no;
        this.description = description;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.expenseItemCode = expenseItemCode;
        this.expenseItemType = expenseItemType;
        this.expenseItemID = expenseItemID;
        this.typeVoucher = typeVoucher;
        this.typeID = typeID;
        this.typeGroupID = typeGroupID;
        this.refID2 = refID2;
    }

    public Integer getExpenseItemType() {
        return expenseItemType;
    }

    public void setExpenseItemType(Integer expenseItemType) {
        this.expenseItemType = expenseItemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setExpenseItemCode(String expenseItemCode) {
        this.expenseItemCode = expenseItemCode;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public String getCostSetCode() {
        return costSetCode;
    }

    public void setCostSetCode(String costSetCode) {
        this.costSetCode = costSetCode;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getExpenseItemCode() {
        return expenseItemCode;
    }

    public void setRxpenseItemCode(String rxpenseItemCode) {
        this.expenseItemCode = rxpenseItemCode;
    }

    public UUID getExpenseItemID() {
        return expenseItemID;
    }

    public void setExpenseItemID(UUID expenseItemID) {
        this.expenseItemID = expenseItemID;
    }

    public Integer getTypeVoucher() {
        return typeVoucher;
    }

    public void setTypeVoucher(Integer typeVoucher) {
        this.typeVoucher = typeVoucher;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public UUID getRefID2() {
        return refID2;
    }

    public void setRefID2(UUID refID2) {
        this.refID2 = refID2;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }
}

