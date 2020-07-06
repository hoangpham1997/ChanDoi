package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.cashandbank.MCReceiptDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewVoucherDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A ViewVoucher.
 */
public class ViewVoucher implements Serializable {

    private Long id;

    private String refID;

    private Integer typeID;

    private Integer typeGroupID;

    private String companyID;

    private String branchID;

    private Integer typeLedger;

    private String noMBook;

    private String noFBook;

    private LocalDate date;

    private LocalDate postedDate;

    private String currencyID;

    private String reason;

    private Boolean recorded;

    private BigDecimal totalAmount;

    private BigDecimal totalAmountOriginal;

    private String refTable;

    private AccountingObject accountingObject;

    private AccountingObject employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefID() {
        return refID;
    }

    public ViewVoucher refID(String refID) {
        this.refID = refID;
        return this;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public ViewVoucher typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public ViewVoucher typeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
        return this;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public ViewVoucher companyID(String companyID) {
        this.companyID = companyID;
        return this;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getBranchID() {
        return branchID;
    }

    public ViewVoucher branchID(String branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public ViewVoucher typeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
        return this;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public ViewVoucher noMBook(String noMBook) {
        this.noMBook = noMBook;
        return this;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public ViewVoucher noFBook(String noFBook) {
        this.noFBook = noFBook;
        return this;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public LocalDate getDate() {
        return date;
    }

    public ViewVoucher date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public ViewVoucher postedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public ViewVoucher currencyID(String currencyID) {
        this.currencyID = currencyID;
        return this;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public String getReason() {
        return reason;
    }

    public ViewVoucher reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean isRecorded() {
        return recorded;
    }

    public ViewVoucher recorded(Boolean recorded) {
        this.recorded = recorded;
        return this;
    }

    public void setRecorded(Boolean recorded) {
        this.recorded = recorded;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public ViewVoucher totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public ViewVoucher totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public String getRefTable() {
        return refTable;
    }

    public ViewVoucher refTable(String refTable) {
        this.refTable = refTable;
        return this;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public ViewVoucher accountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
        return this;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
    }

    public AccountingObject getEmployee() {
        return employee;
    }

    public ViewVoucher employee(AccountingObject accountingObject) {
        this.employee = accountingObject;
        return this;
    }

    public void setEmployee(AccountingObject accountingObject) {
        this.employee = accountingObject;
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
        ViewVoucher viewVoucher = (ViewVoucher) o;
        if (viewVoucher.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), viewVoucher.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ViewVoucher{" +
            "id=" + getId() +
            ", refID='" + getRefID() + "'" +
            ", typeID=" + getTypeID() +
            ", typeGroupID=" + getTypeGroupID() +
            ", companyID='" + getCompanyID() + "'" +
            ", branchID='" + getBranchID() + "'" +
            ", typeLedger=" + getTypeLedger() +
            ", noMBook='" + getNoMBook() + "'" +
            ", noFBook='" + getNoFBook() + "'" +
            ", date='" + getDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", reason='" + getReason() + "'" +
            ", recorded='" + isRecorded() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", refTable='" + getRefTable() + "'" +
            "}";
    }
}
