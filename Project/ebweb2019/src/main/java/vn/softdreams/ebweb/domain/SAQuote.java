package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.SAQuoteReportDTO;
import vn.softdreams.ebweb.service.dto.ViewSAQuoteDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A SAQuote.
 */
@Entity
@Table(name = "saquote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "SAQuoteDTO",
        classes = {
            @ConstructorResult(
                targetClass = SAQuoteDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "finalDate", type = LocalDate.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalDiscountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalVATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "currencyID", type = String.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "SAQuoteReportDTO",
        classes = {
            @ConstructorResult(
                targetClass = SAQuoteReportDTO.class,
                columns = {
                    @ColumnResult(name = "materialGoodsName", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "quantityString", type = String.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginalString", type = String.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginalString", type = String.class),
                    @ColumnResult(name = "discountRate", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vATRate", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmountOriginal", type = BigDecimal.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "ViewSAQuoteDTO",
        classes = {
            @ConstructorResult(
                targetClass = ViewSAQuoteDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "sAQuoteDetailID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "materialGoodsID", type = UUID.class),
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "unitID", type = UUID.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "mainQuantity", type = BigDecimal.class),
                    @ColumnResult(name = "mainUnitID", type = UUID.class),
                    @ColumnResult(name = "mainUnitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "mainConvertRate", type = BigDecimal.class),
                    @ColumnResult(name = "formula", type = String.class),
                    @ColumnResult(name = "vATDescription", type = String.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "discountRate", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vATRate", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "companyTaxCode", type = String.class),
                    @ColumnResult(name = "contactName", type = String.class),
                    @ColumnResult(name = "descriptionParent", type = String.class),
                    @ColumnResult(name = "employeeID", type = UUID.class),
                    @ColumnResult(name = "deliveryTime", type = String.class),
                }
            )
        }
    )
})
public class SAQuote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyId;

    @Column(name = "branchid")
    private UUID branchId;

    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeId;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Size(max = 25)
    @Column(name = "no", length = 25, nullable = false)
    private String no;

    @Size(max = 512)
    @Column(name = "accountingobjectname", length = 512)
    private String accountingObjectName;

    @Size(max = 512)
    @Column(name = "accountingobjectaddress", length = 512)
    private String accountingObjectAddress;

    @Size(max = 50)
    @Column(name = "companytaxcode", length = 50)
    private String companyTaxCode;

    @Size(max = 512)
    @Column(name = "contactname", length = 512)
    private String contactName;

    @Size(max = 25)
    @Column(name = "contactmobile", length = 25)
    private String contactMobile;

    @Size(max = 100)
    @Column(name = "contactemail", length = 100)
    private String contactEmail;

    @Size(max = 255)
    @Column(name = "deliverytime", length = 255)
    private String deliveryTime;

    @Size(max = 255)
    @Column(name = "guaranteeduration", length = 255)
    private String guaranteeDuration;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Size(max = 3)
    @Column(name = "currencyid", length = 3)
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "finaldate")
    private LocalDate finalDate;

    @Column(name = "employeeid")
    private UUID employeeID;

    @NotNull
    @Column(name = "totalamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "totalamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmountOriginal;

    @NotNull
    @Column(name = "totalvatamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalVATAmount;

    @NotNull
    @Column(name = "totalvatamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalVATAmountOriginal;

    @NotNull
    @Column(name = "totaldiscountamount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalDiscountAmount;

    @NotNull
    @Column(name = "totaldiscountamountoriginal", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalDiscountAmountOriginal;

    @Column(name = "templateid")
    private UUID templateID;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "accountingobjectid")
    private AccountingObject accountingObject;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "paymentclauseid")
    private PaymentClause paymentClause;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "saquoteid")
    private Set<SAQuoteDetails> sAQuoteDetails = new HashSet<>();

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<RefVoucherDTO> viewVouchers;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private Number totalMoney;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public SAQuote companyId(UUID companyId) {
        this.companyId = companyId;
        return this;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public UUID getBranchId() {
        return branchId;
    }

    public SAQuote branchId(UUID branchId) {
        this.branchId = branchId;
        return this;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public SAQuote typeId(Integer typeId) {
        this.typeId = typeId;
        return this;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public SAQuote date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public SAQuote no(String no) {
        this.no = no;
        return this;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public SAQuote accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public SAQuote accountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
        return this;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public SAQuote companyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
        return this;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public String getContactName() {
        return contactName;
    }

    public SAQuote contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public SAQuote contactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
        return this;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public SAQuote contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public SAQuote deliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
        return this;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getGuaranteeDuration() {
        return guaranteeDuration;
    }

    public SAQuote guaranteeDuration(String guaranteeDuration) {
        this.guaranteeDuration = guaranteeDuration;
        return this;
    }

    public void setGuaranteeDuration(String guaranteeDuration) {
        this.guaranteeDuration = guaranteeDuration;
    }

    public String getDescription() {
        return description;
    }

    public SAQuote description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public SAQuote reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public SAQuote currencyID(String currencyID) {
        this.currencyID = currencyID;
        return this;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public SAQuote exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public SAQuote finalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
        return this;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public SAQuote employeeID(UUID employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public SAQuote totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public SAQuote totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public SAQuote totalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
        return this;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public SAQuote totalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        return this;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public SAQuote totalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
        return this;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public SAQuote totalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
        return this;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public SAQuote templateID(UUID templateID) {
        this.templateID = templateID;
        return this;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public AccountingObject getAccountingObject() {
        return accountingObject;
    }

    public SAQuote accountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
        return this;
    }

    public void setAccountingObject(AccountingObject accountingObject) {
        this.accountingObject = accountingObject;
    }

    public PaymentClause getPaymentClause() {
        return paymentClause;
    }

    public SAQuote paymentClause(PaymentClause paymentClause) {
        this.paymentClause = paymentClause;
        return this;
    }

    public void setPaymentClause(PaymentClause paymentClause) {
        this.paymentClause = paymentClause;
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
        SAQuote sAQuote = (SAQuote) o;
        if (sAQuote.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sAQuote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SAQuote{" +
            "id=" + getId() +
            ", companyId=" + getCompanyId() +
            ", branchId=" + getBranchId() +
            ", typeId=" + getTypeId() +
            ", date='" + getDate() + "'" +
            ", no='" + getNo() + "'" +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", accountingObjectAddress='" + getAccountingObjectAddress() + "'" +
            ", companyTaxCode='" + getCompanyTaxCode() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", contactMobile='" + getContactMobile() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", deliveryTime='" + getDeliveryTime() + "'" +
            ", guaranteeDuration='" + getGuaranteeDuration() + "'" +
            ", description='" + getDescription() + "'" +
            ", reason='" + getReason() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            ", finalDate='" + getFinalDate() + "'" +
            ", employeeID=" + getEmployeeID() +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", totalVATAmount=" + getTotalVATAmount() +
            ", totalVATAmountOriginal=" + getTotalVATAmountOriginal() +
            ", totalDiscountAmount=" + getTotalDiscountAmount() +
            ", totalDiscountAmountOriginal=" + getTotalDiscountAmountOriginal() +
            ", templateID=" + getTemplateID() +
            "}";
    }

    public Set<SAQuoteDetails> getsAQuoteDetails() {
        return sAQuoteDetails;
    }

    public SAQuote sAQuoteDetails(Set<SAQuoteDetails> sAQuoteDetails) {
        this.sAQuoteDetails = sAQuoteDetails;
        return this;
    }

    public void setsAQuoteDetails(Set<SAQuoteDetails> sAQuoteDetails) {
        this.sAQuoteDetails = sAQuoteDetails;
    }


    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public Number getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Number totalMoney) {
        this.totalMoney = totalMoney;
    }
}
