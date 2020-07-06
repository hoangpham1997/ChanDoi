package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TITransferAndTIAdjustmentConvertDTO {
    private UUID id;
    private LocalDate date;
    private Integer typeLedger;
    private String nofBook;
    private String noMBook;
    private String noBook;
    private String reason;
    private Boolean recorded;
    private BigDecimal totalAmount;
    private BigDecimal diffRemainingAmount;
    private BigDecimal differAllocationTime;

    public TITransferAndTIAdjustmentConvertDTO() {
    }

    public TITransferAndTIAdjustmentConvertDTO(UUID id, LocalDate date, Integer typeLedger, String nofBook, String noMBook, String noBook, String reason, Boolean recorded, BigDecimal totalAmount) {
        this.id = id;
        this.date = date;
        this.typeLedger = typeLedger;
        this.nofBook = nofBook;
        this.noMBook = noMBook;
        this.noBook = noBook;
        this.reason = reason;
        this.recorded = recorded;
        this.totalAmount = totalAmount;
    }

    public TITransferAndTIAdjustmentConvertDTO(UUID id, LocalDate date, Integer typeLedger, String nofBook, String noMBook, String noBook, String reason, Boolean recorded, BigDecimal totalAmount, BigDecimal diffRemainingAmount, BigDecimal differAllocationTime) {
        this.id = id;
        this.date = date;
        this.typeLedger = typeLedger;
        this.nofBook = nofBook;
        this.noMBook = noMBook;
        this.noBook = noBook;
        this.reason = reason;
        this.recorded = recorded;
        this.totalAmount = totalAmount;
        this.diffRemainingAmount = diffRemainingAmount;
        this.differAllocationTime = differAllocationTime;
    }

    public BigDecimal getDiffRemainingAmount() {
        return diffRemainingAmount;
    }

    public void setDiffRemainingAmount(BigDecimal diffRemainingAmount) {
        this.diffRemainingAmount = diffRemainingAmount;
    }

    public BigDecimal getDifferAllocationTime() {
        return differAllocationTime;
    }

    public void setDifferAllocationTime(BigDecimal differAllocationTime) {
        this.differAllocationTime = differAllocationTime;
    }

    public Boolean getRecorded() {
        return recorded;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
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
}
