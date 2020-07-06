package vn.softdreams.ebweb.service.dto;
import vn.softdreams.ebweb.domain.GOtherVoucherDetails;
import vn.softdreams.ebweb.domain.PPInvoiceDetails;
import vn.softdreams.ebweb.web.rest.dto.PPInvoiceDetailCostDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class GOtherVoucherKcDTO {
    private UUID id;
    private Integer typeID;
    private String currencyID;
    private BigDecimal exchangeRate;
    private Integer typeLedger;
    private String reason;
    private String noMBook;
    private String noFBook;
    private LocalDate date;
    private LocalDate postedDate;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountOriginal;
    private Boolean recorded;

    private List<GOtherVoucherDetails> gOtherVoucherDetails = new ArrayList<>();
    private List<RefVoucherDTO> refVouchers;

    public GOtherVoucherKcDTO() {
    }

    public GOtherVoucherKcDTO(UUID id, Integer typeID, String currencyID, BigDecimal exchangeRate, Integer typeLedger, String reason, String noMBook, String noFBook, LocalDate date, LocalDate postedDate, BigDecimal totalAmount, BigDecimal totalAmountOriginal, Boolean recorded) {
        this.id = id;
        this.typeID = typeID;
        this.currencyID = currencyID;
        this.exchangeRate = exchangeRate;
        this.typeLedger = typeLedger;
        this.reason = reason;
        this.noMBook = noMBook;
        this.noFBook = noFBook;
        this.date = date;
        this.postedDate = postedDate;
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

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
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

    public List<GOtherVoucherDetails> getgOtherVoucherDetails() {
        return gOtherVoucherDetails;
    }

    public void setgOtherVoucherDetails(List<GOtherVoucherDetails> gOtherVoucherDetails) {
        this.gOtherVoucherDetails = gOtherVoucherDetails;
    }

    public List<RefVoucherDTO> getRefVouchers() {
        return refVouchers;
    }

    public void setRefVouchers(List<RefVoucherDTO> refVouchers) {
        this.refVouchers = refVouchers;
    }
}
