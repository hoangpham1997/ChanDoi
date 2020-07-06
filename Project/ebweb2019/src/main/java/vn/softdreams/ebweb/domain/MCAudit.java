package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;
import vn.softdreams.ebweb.service.dto.cashandbank.MCAuditDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A MCAudit.
 */
@Entity
@Table(name = "mcaudit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "MCAuditDTO",
        classes = {
            @ConstructorResult(
                targetClass = MCAuditDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "branchID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "auditDate", type = LocalDateTime.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "summary", type = String.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                    @ColumnResult(name = "totalAuditAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalBalanceAmount", type = BigDecimal.class),
                    @ColumnResult(name = "differAmount", type = BigDecimal.class),
                    @ColumnResult(name = "templateID", type = UUID.class)
                }
            )
        }
    )})
public class MCAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(max = 25)
    @Column(name = "no", length = 25)
    private String no;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "date")
    private LocalDate date;

    @Size(max = 225)
    @Column(name = "description", length = 225)
    private String description;

    @Column(name = "auditdate")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime auditDate;

    @Size(max = 225)
    @Column(name = "summary", length = 225)
    private String summary;

    @Column(name = "currencyid")
    private String currencyID;

    @Column(name = "totalauditamount", precision = 10, scale = 2)
    private BigDecimal totalAuditAmount;

    @Column(name = "totalbalanceamount", precision = 10, scale = 2)
    private BigDecimal totalBalanceAmount;

    @Column(name = "differamount", precision = 10, scale = 2)
    private BigDecimal differAmount;

    @Column(name = "templateid")
    private UUID templateID;

    @JoinColumn(name = "branchid")
    private UUID branchID;

    @JoinColumn(name = "companyid")
    private UUID companyID;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mcauditid")
    private Set<MCAuditDetailMember> mcAuditDetailMembers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mcauditid")
    private Set<MCAuditDetails> mcAuditDetails = new HashSet<>();

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucherDTO> viewVouchers;

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private BigDecimal exchangeRate;

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getNo() {
        return no;
    }

    public MCAudit no(String no) {
        this.no = no;
        return this;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public MCAudit typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public MCAudit typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public LocalDate getDate() {
        return date;
    }

    public MCAudit date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public MCAudit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(LocalDateTime auditDate) {
        this.auditDate = auditDate;
    }

    public String getSummary() {
        return summary;
    }

    public MCAudit summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public MCAudit currencyID(String currencyID) {
        this.currencyID = currencyID;
        return this;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getTotalAuditAmount() {
        return totalAuditAmount;
    }

    public MCAudit totalAuditAmount(BigDecimal totalAuditAmount) {
        this.totalAuditAmount = totalAuditAmount;
        return this;
    }

    public void setTotalAuditAmount(BigDecimal totalAuditAmount) {
        this.totalAuditAmount = totalAuditAmount;
    }

    public BigDecimal getTotalBalanceAmount() {
        return totalBalanceAmount;
    }

    public MCAudit totalBalanceAmount(BigDecimal totalBalanceAmount) {
        this.totalBalanceAmount = totalBalanceAmount;
        return this;
    }

    public void setTotalBalanceAmount(BigDecimal totalBalanceAmount) {
        this.totalBalanceAmount = totalBalanceAmount;
    }

    public BigDecimal getDifferAmount() {
        return differAmount;
    }

    public MCAudit differAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
        return this;
    }

    public void setDifferAmount(BigDecimal differAmount) {
        this.differAmount = differAmount;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public MCAudit templateID(UUID templateID) {
        this.templateID = templateID;
        return this;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public MCAudit branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Set<MCAuditDetailMember> getMcAuditDetailMembers() {
        return mcAuditDetailMembers;
    }

    public void setMcAuditDetailMembers(Set<MCAuditDetailMember> mcAuditDetailMembers) {
        this.mcAuditDetailMembers = mcAuditDetailMembers;
    }

    public Set<MCAuditDetails> getMcAuditDetails() {
        return mcAuditDetails;
    }

    public void setMcAuditDetails(Set<MCAuditDetails> mcAuditDetails) {
        this.mcAuditDetails = mcAuditDetails;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MCAudit mCAudit = (MCAudit) o;
        if (mCAudit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mCAudit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MCAudit{" +
            "id=" + getId() +
            ", no='" + getNo() + "'" +
            ", typeID=" + getTypeID() +
            ", typeLedger=" + getTypeLedger() +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", auditDate='" + getAuditDate() + "'" +
            ", summary='" + getSummary() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", totalAuditAmount=" + getTotalAuditAmount() +
            ", totalBalanceAmount=" + getTotalBalanceAmount() +
            ", differAmount=" + getDifferAmount() +
            ", templateID='" + getTemplateID() + "'" +
            "}";
    }
}
