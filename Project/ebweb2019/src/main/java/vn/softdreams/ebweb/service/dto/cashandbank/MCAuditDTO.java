package vn.softdreams.ebweb.service.dto.cashandbank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class MCAuditDTO {
    private UUID id;

    private UUID companyID;

    private UUID branchID;

    private Integer typeID;

    private LocalDate date;

    private LocalDateTime auditDate;

    private Integer typeLedger;

    private String no;

    private String description;

    private String summary;

    private String currencyID;

    private BigDecimal totalAuditAmount;

    private BigDecimal totalBalanceAmount;

    public MCAuditDTO(UUID id, UUID companyID, UUID branchID, Integer typeID, LocalDate date, LocalDateTime auditDate, Integer typeLedger, String no, String description, String summary, String currencyID, BigDecimal totalAuditAmount, BigDecimal totalBalanceAmount, BigDecimal differAmount, UUID templateID) {
        this.id = id;
        this.companyID = companyID;
        this.branchID = branchID;
        this.typeID = typeID;
        this.date = date;
        this.auditDate = auditDate;
        this.typeLedger = typeLedger;
        this.no = no;
        this.description = description;
        this.summary = summary;
        this.currencyID = currencyID;
        this.totalAuditAmount = totalAuditAmount;
        this.totalBalanceAmount = totalBalanceAmount;
        this.differAmount = differAmount;
        this.templateID = templateID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
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

    public LocalDateTime getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(LocalDateTime auditDate) {
        this.auditDate = auditDate;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getTotalAuditAmount() {
        return totalAuditAmount;
    }

    public void setTotalAuditAmount(BigDecimal totalAuditAmount) {
        this.totalAuditAmount = totalAuditAmount;
    }

    public BigDecimal getTotalBalanceAmount() {
        return totalBalanceAmount;
    }

    public void setTotalBalanceAmount(BigDecimal totalBalanceAmount) {
        this.totalBalanceAmount = totalBalanceAmount;
    }

    public BigDecimal getDifferAmount() {
        return differAmount;
    }

    public void setDifferAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    private BigDecimal differAmount;

    private UUID templateID;
}
