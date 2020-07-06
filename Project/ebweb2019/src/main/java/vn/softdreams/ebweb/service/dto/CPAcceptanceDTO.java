package vn.softdreams.ebweb.service.dto;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CPAcceptanceDTO {
    private UUID id;
    private Integer typeID;
    private LocalDate date;
    private LocalDate postedDate;
    private String no;
    private String description;
    private UUID cPPeriodID;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountOriginal;

    public CPAcceptanceDTO(UUID id, Integer typeID, LocalDate date, LocalDate postedDate, String no, String description, UUID cPPeriodID, BigDecimal totalAmount, BigDecimal totalAmountOriginal) {
        this.id = id;
        this.typeID = typeID;
        this.date = date;
        this.postedDate = postedDate;
        this.no = no;
        this.description = description;
        this.cPPeriodID = cPPeriodID;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
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

    public UUID getcPPeriodID() {
        return cPPeriodID;
    }

    public void setcPPeriodID(UUID cPPeriodID) {
        this.cPPeriodID = cPPeriodID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }
}

