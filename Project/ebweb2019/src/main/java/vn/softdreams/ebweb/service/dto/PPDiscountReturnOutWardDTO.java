package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PPDiscountReturnOutWardDTO {
    private LocalDate date;
    private LocalDate postedDate;
    private String book;
    private String reason;
    private BigDecimal total;
    private UUID id;
    private Integer typeGroupID;

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
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

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PPDiscountReturnOutWardDTO() {
    }

    public PPDiscountReturnOutWardDTO(LocalDate date, LocalDate postedDate, String book, String reason, BigDecimal total, UUID id, Integer typeGroupID) {
        this.date = date;
        this.postedDate = postedDate;
        this.book = book;
        this.reason = reason;
        this.total = total;
        this.id = id;
        this.typeGroupID = typeGroupID;
    }
}
