package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TIIncrementDetailRefVoucherConvertDTO {
    private UUID id;
    private LocalDate date;
    private LocalDate postedDate;
    private String no;
    private String reason;
    private String totalAmountOriginal;
    private UUID tiIncrementID;
    private UUID refVoucherID;
    private Integer orderPriority;
    private Integer typeGroupID;

    public TIIncrementDetailRefVoucherConvertDTO() {
    }

    public TIIncrementDetailRefVoucherConvertDTO(UUID id, LocalDate date, LocalDate postedDate, String no, String reason, String totalAmountOriginal, UUID tiIncrementID, UUID refVoucherID, Integer orderPriority, Integer typeGroupID) {
        this.id = id;
        this.date = date;
        this.postedDate = postedDate;
        this.no = no;
        this.reason = reason;
        this.totalAmountOriginal = totalAmountOriginal;
        this.tiIncrementID = tiIncrementID;
        this.refVoucherID = refVoucherID;
        this.orderPriority = orderPriority;
        this.typeGroupID = typeGroupID;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(String totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public UUID getTiIncrementID() {
        return tiIncrementID;
    }

    public void setTiIncrementID(UUID tiIncrementID) {
        this.tiIncrementID = tiIncrementID;
    }

    public UUID getRefVoucherID() {
        return refVoucherID;
    }

    public void setRefVoucherID(UUID refVoucherID) {
        this.refVoucherID = refVoucherID;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }
}
