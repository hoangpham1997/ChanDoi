package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class GOtherVoucherViewDTO {
    private UUID id;
    private Integer typeID;
    private LocalDate date;
    private LocalDate postedDate;
    private Integer typeLedger;
    private String noFBook;
    private String noMBook;
    private String reason;
    private String currencyID;
    private BigDecimal exchangeRate;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountOriginal;
    private Boolean recorded;
    private Number total;

    public GOtherVoucherViewDTO() {
    }

    public GOtherVoucherViewDTO(UUID id, Integer typeID, LocalDate date, LocalDate postedDate, Integer typeLedger,
                                String noFBook, String noMBook,
                                String reason, String currencyID, BigDecimal exchangeRate,
                                BigDecimal totalAmount, BigDecimal totalAmountOriginal,
                                Boolean recorded) {
        this.id = id;
        this.typeID = typeID;
        this.date = date;
        this.postedDate = postedDate;
        this.typeLedger = typeLedger;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.reason = reason;
        this.currencyID = currencyID;
        this.exchangeRate = exchangeRate;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
        this.recorded = recorded;
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

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
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

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }
}
