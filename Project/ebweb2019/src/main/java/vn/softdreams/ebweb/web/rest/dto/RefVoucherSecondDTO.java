package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class RefVoucherSecondDTO {
    private UUID id;
    private UUID refID1;
    private UUID refID2;
    private UUID companyID;
    private String no;
    private String date;
    private String postedDate;
    private String reason;
    private Integer typeID;
    private Integer typeGroupID;
    private BigDecimal totalAmount;
    private BigDecimal TotalAmountOriginal;

    public RefVoucherSecondDTO() {
    }

    public RefVoucherSecondDTO(UUID id, UUID refID1, UUID refID2, UUID companyID, String no, String date, String postedDate, String reason, Integer typeID, Integer typeGroupID, BigDecimal totalAmount, BigDecimal totalAmountOriginal) {
        this.id = id;
        this.refID1 = refID1;
        this.refID2 = refID2;
        this.companyID = companyID;
        this.no = no;
        this.date = date;
        this.postedDate = postedDate;
        this.reason = reason;
        this.typeID = typeID;
        this.typeGroupID = typeGroupID;
        this.totalAmount = totalAmount;
        TotalAmountOriginal = totalAmountOriginal;
    }

    public RefVoucherSecondDTO(UUID id, UUID refID1, UUID companyID, String no, String date, String postedDate, String reason, Integer typeID, Integer typeGroupID, BigDecimal totalAmount, BigDecimal totalAmountOriginal) {
        this.id = id;
        this.refID1 = refID1;
        this.companyID = companyID;
        this.no = no;
        this.date = date;
        this.postedDate = postedDate;
        this.reason = reason;
        this.typeID = typeID;
        this.typeGroupID = typeGroupID;
        this.totalAmount = totalAmount;
        TotalAmountOriginal = totalAmountOriginal;
    }

    public UUID getRefID2() {
        return refID2;
    }

    public void setRefID2(UUID refID2) {
        this.refID2 = refID2;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRefID1() {
        return refID1;
    }

    public void setRefID1(UUID refID1) {
        this.refID1 = refID1;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return TotalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        TotalAmountOriginal = totalAmountOriginal;
    }
}
