package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.TIAuditConvertDTO;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A TIAudit.
 */
@Entity
@Table(name = "tiaudit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings(value = {
    @SqlResultSetMapping(
        name = "TIAuditConvertDTO",
        classes = {
            @ConstructorResult(
                targetClass = TIAuditConvertDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "typeLedger", type = Integer.class),
                    @ColumnResult(name = "noFBook", type = String.class),
                    @ColumnResult(name = "noMBook", type = String.class),
                    @ColumnResult(name = "noBook", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "inventoryDate", type = LocalDate.class),
                    @ColumnResult(name = "summary", type = String.class),
                    @ColumnResult(name = "templateID", type = UUID.class),
                }
            )
        })
})
public class TIAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "inventorydate")
    private LocalDate inventoryDate;

    @Column(name = "typeledger")
    private Integer typeLedger;

    @Column(name = "nofbook")
    private String noFBook;

    @Column(name = "nombook")
    private String noMBook;

    @Column(name = "summary")
    private String summary;

    @Column(name = "description")
    private String description;

//    @Column(name = "month")
//    private Integer month;
//
//    @Column(name = "year")
//    private Integer Year;
//
//    @Column(name = "totalamount", precision = 10, scale = 2)
//    private BigDecimal totalAmount;
//
//    @Column(name = "recorded")
//    private Boolean recorded;

    @Column(name = "templateid")
    private UUID templateID;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "tiauditid")
    private Set<TIAuditDetails> tiAuditDetails = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "tiauditid")
    private Set<TIAuditMemberDetails> tiAuditMemberDetails = new HashSet<>();


    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID tiDecrementID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private UUID tiIncrementID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String noBookIncrement;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String noBookDecrement;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucher> viewVouchers;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public List<RefVoucher> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucher> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public UUID getTiDecrementID() {
        return tiDecrementID;
    }

    public void setTiDecrementID(UUID tiDecrementID) {
        this.tiDecrementID = tiDecrementID;
    }

    public UUID getTiIncrementID() {
        return tiIncrementID;
    }

    public void setTiIncrementID(UUID tiIncrementID) {
        this.tiIncrementID = tiIncrementID;
    }

    public String getNoBookIncrement() {
        return noBookIncrement;
    }

    public void setNoBookIncrement(String noBookIncrement) {
        this.noBookIncrement = noBookIncrement;
    }

    public String getNoBookDecrement() {
        return noBookDecrement;
    }

    public void setNoBookDecrement(String noBookDecrement) {
        this.noBookDecrement = noBookDecrement;
    }

    public Set<TIAuditDetails> getTiAuditDetails() {
        return tiAuditDetails;
    }

    public void setTiAuditDetails(Set<TIAuditDetails> tiAuditDetails) {
        this.tiAuditDetails = tiAuditDetails;
    }

    public Set<TIAuditMemberDetails> getTiAuditMemberDetails() {
        return tiAuditMemberDetails;
    }

    public void setTiAuditMemberDetails(Set<TIAuditMemberDetails> tiAuditMemberDetails) {
        if (this.tiAuditMemberDetails == null) {
            this.tiAuditMemberDetails = tiAuditMemberDetails;
        } else if (this.tiAuditMemberDetails != tiAuditMemberDetails) { // not the same instance, in other case we can get ConcurrentModificationException from hibernate AbstractPersistentCollection
            this.tiAuditMemberDetails.clear();
            if (tiAuditMemberDetails != null) {
                this.tiAuditMemberDetails.addAll(tiAuditMemberDetails);
            }
        }
    }

    public Integer getTypeID() {
        return typeID;
    }

    public TIAudit typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public TIAudit date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public Integer getTypeLedger() {
        return typeLedger;
    }

    public TIAudit typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public TIAudit noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setInventoryDate(LocalDate inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDate getInventoryDate() {
        return inventoryDate;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TIAudit noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }




    public static long getSerialVersionUID() {
        return serialVersionUID;
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



    public UUID getTemplateID() {
        return templateID;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
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
        TIAudit tIAudit = (TIAudit) o;
        if (tIAudit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tIAudit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TIAudit{" +
            "id=" + getId() +
            "}";
    }
}
