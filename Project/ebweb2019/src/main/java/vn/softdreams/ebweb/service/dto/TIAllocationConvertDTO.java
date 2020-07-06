package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TIAllocationConvertDTO {
    private UUID id;
    private LocalDate date;
    private LocalDate postedDate;
    private Integer typeLedger;
    private String nofBook;
    private String noMBook;
    private String noBook;
    private String reason;
    private BigDecimal totalAmount;
    private UUID templateID;
    private Boolean recorded;
    private BigDecimal total;

    public TIAllocationConvertDTO() {
    }

    public TIAllocationConvertDTO(UUID id, LocalDate date, LocalDate postedDate, Integer typeLedger, String nofBook, String noMBook, String noBook, String reason, BigDecimal totalAmount, UUID templateID, Boolean recorded) {
        this.id = id;
        this.date = date;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.nofBook = nofBook;
        this.noMBook = noMBook;
        this.noBook = noBook;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.templateID = templateID;
        this.recorded = recorded;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNofBook() {
        return nofBook;
    }

    public void setNofBook(String nofBook) {
        this.nofBook = nofBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getNoBook() {
        return noBook;
    }

    public void setNoBook(String noBook) {
        this.noBook = noBook;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

}
