package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.SAOrderDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A SAOrder.
 */
@Entity
@Table(name = "saorder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "SAOrderDTO",
        classes = {
            @ConstructorResult(
                targetClass = SAOrderDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "deliverDate", type = LocalDate.class),
                    @ColumnResult(name = "deliveryPlace", type = String.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                    @ColumnResult(name = "totalAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "status", type = Integer.class),
                    @ColumnResult(name = "currencyID", type = String.class),
                }
            )
        }
    ),

    @SqlResultSetMapping(
        name = "SAOrderDTOPopup",
        classes = {
            @ConstructorResult(
                targetClass = SAOrderDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "sAOrderDetailID", type = UUID.class),
                    @ColumnResult(name = "typeID", type = Integer.class),
                    @ColumnResult(name = "companyID", type = UUID.class),
                    @ColumnResult(name = "no", type = String.class),
                    @ColumnResult(name = "date", type = LocalDate.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "contactName", type = String.class),
                    @ColumnResult(name = "companyTaxCode", type = String.class),
                    @ColumnResult(name = "employeeID", type = UUID.class),
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
                    @ColumnResult(name = "repositoryID", type = UUID.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class),
                    @ColumnResult(name = "quantityDelivery", type = BigDecimal.class)
                }
            )
        }
    ),

    @SqlResultSetMapping(
        name = "SAOrderDTOReport",
        classes = {
            @ConstructorResult(
                targetClass = SAOrderDTO.class,
                columns = {
                    @ColumnResult(name = "materialGoodsCode", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "quantity", type = BigDecimal.class),
                    @ColumnResult(name = "unitName", type = String.class),
                    @ColumnResult(name = "unitPrice", type = BigDecimal.class),
                    @ColumnResult(name = "unitPriceOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "amount", type = BigDecimal.class),
                    @ColumnResult(name = "amountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "discountRate", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                    @ColumnResult(name = "discountAmountOriginal", type = BigDecimal.class),
                    @ColumnResult(name = "vATRate", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmount", type = BigDecimal.class),
                    @ColumnResult(name = "vATAmountOriginal", type = BigDecimal.class)
                }
            )
        }
    )})
public class SAOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @NotNull
    @Column(name = "typeid", nullable = false)
    private Integer typeID;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Size(max = 25)
    @Column(name = "no", length = 25, nullable = false)
    private String no;

    @Column(name = "deliverdate")
    private LocalDate deliverDate;

    @Size(max = 512)
    @Column(name = "deliveryplace", length = 512)
    private String deliveryPlace;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectID;

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

    @Size(max = 512)
    @Column(name = "reason", length = 512)
    private String reason;

    @Size(max = 3)
    @Column(name = "currencyid", length = 3)
    private String currencyID;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "transpotmethodid")
    private UUID transpotMethodID;

    @Column(name = "employeeid")
    private UUID employeeID;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "totaldiscountamount", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmount;

    @Column(name = "totaldiscountamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmountOriginal;

    @Column(name = "totalvatamount", precision = 10, scale = 2)
    private BigDecimal totalVATAmount;

    @Column(name = "totalvatamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalVATAmountOriginal;

    @Column(name = "templateid")
    private UUID templateID;

    @Column(name = "status")
    private Integer status;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "saorderid")
    private Set<SAOrderDetails> saOrderDetails = new HashSet<>();

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

    public Set<SAOrderDetails> getSaOrderDetails() {
        return saOrderDetails;
    }

    public void setSaOrderDetails(Set<SAOrderDetails> saOrderDetails) {
        this.saOrderDetails = saOrderDetails;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public SAOrder companyID(UUID companyID) {
        this.companyID = companyID;
        return this;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public SAOrder branchID(UUID branchID) {
        this.branchID = branchID;
        return this;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public SAOrder typeID(Integer typeID) {
        this.typeID = typeID;
        return this;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public LocalDate getDate() {
        return date;
    }

    public SAOrder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public SAOrder no(String no) {
        this.no = no;
        return this;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public LocalDate getDeliverDate() {
        return deliverDate;
    }

    public SAOrder deliverDate(LocalDate deliverDate) {
        this.deliverDate = deliverDate;
        return this;
    }

    public void setDeliverDate(LocalDate deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public SAOrder deliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
        return this;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public SAOrder accountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
        return this;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public SAOrder accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public SAOrder accountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
        return this;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public SAOrder companyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
        return this;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public String getContactName() {
        return contactName;
    }

    public SAOrder contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getReason() {
        return reason;
    }

    public SAOrder reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public SAOrder currencyID(String currencyID) {
        this.currencyID = currencyID;
        return this;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public SAOrder exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public UUID getTranspotMethodID() {
        return transpotMethodID;
    }

    public SAOrder transpotMethodID(UUID transpotMethodID) {
        this.transpotMethodID = transpotMethodID;
        return this;
    }

    public void setTranspotMethodID(UUID transpotMethodID) {
        this.transpotMethodID = transpotMethodID;
    }

    public UUID getEmployeeID() {
        return employeeID;
    }

    public SAOrder employeeID(UUID employeeID) {
        this.employeeID = employeeID;
        return this;
    }

    public void setEmployeeID(UUID employeeID) {
        this.employeeID = employeeID;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public SAOrder totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public SAOrder totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public SAOrder totalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
        return this;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public SAOrder totalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
        return this;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public SAOrder totalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
        return this;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public SAOrder totalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        return this;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public UUID getTemplateID() {
        return templateID;
    }

    public SAOrder templateID(UUID templateID) {
        this.templateID = templateID;
        return this;
    }

    public void setTemplateID(UUID templateID) {
        this.templateID = templateID;
    }

    public Integer getStatus() {
        return status;
    }

    public SAOrder status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        SAOrder sAOrder = (SAOrder) o;
        if (sAOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sAOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SAOrder{" +
            "id=" + getId() +
            ", companyID=" + getCompanyID() +
            ", branchID=" + getBranchID() +
            ", typeID=" + getTypeID() +
            ", date='" + getDate() + "'" +
            ", no='" + getNo() + "'" +
            ", deliverDate='" + getDeliverDate() + "'" +
            ", deliveryPlace='" + getDeliveryPlace() + "'" +
            ", accountingObjectID=" + getAccountingObjectID() +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", accountingObjectAddress='" + getAccountingObjectAddress() + "'" +
            ", companyTaxCode='" + getCompanyTaxCode() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", reason='" + getReason() + "'" +
            ", currencyID='" + getCurrencyID() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            ", transpotMethodID=" + getTranspotMethodID() +
            ", employeeID=" + getEmployeeID() +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", totalDiscountAmount=" + getTotalDiscountAmount() +
            ", totalDiscountAmountOriginal=" + getTotalDiscountAmountOriginal() +
            ", totalVATAmount=" + getTotalVATAmount() +
            ", totalVATAmountOriginal=" + getTotalVATAmountOriginal() +
            ", templateID=" + getTemplateID() +
            ", status=" + getStatus() +
            "}";
    }
}
