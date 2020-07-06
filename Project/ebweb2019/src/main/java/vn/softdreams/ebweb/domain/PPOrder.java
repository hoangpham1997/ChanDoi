package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.PPOrderDTO;
import vn.softdreams.ebweb.service.dto.SAOrderDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A PPOrder.
 */
@Entity
@Table(name = "pporder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name =
            "PPOrderDTO",
        classes = {
            @ConstructorResult(
                targetClass = PPOrderDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "orderDate", type = String.class),
                    @ColumnResult(name = "orderNumber", type = String.class),
                    @ColumnResult(name = "productCode", type = String.class),
                    @ColumnResult(name = "productName", type = String.class),
                    @ColumnResult(name = "quantityReceipt", type = BigDecimal.class),
                    @ColumnResult(name = "ppOrderId", type = UUID.class),
                    @ColumnResult(name = "priority", type = Integer.class),
                    @ColumnResult(name = "accountingObjectID", type = UUID.class),
                    @ColumnResult(name = "reason", type = String.class),
                    @ColumnResult(name = "typeGroupID", type = Integer.class)
                })
        }),
        @SqlResultSetMapping(
                name = "PPOrderDTOReport",
                classes = {
                        @ConstructorResult(
                                targetClass = PPOrderDTO.class,
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
                                        @ColumnResult(name = "vATAmountOriginal", type = BigDecimal.class),
                                        @ColumnResult(name = "orderPriority", type = Integer.class)
                                }
                        )
                }
        )
})
public class PPOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyId;

    @Column(name = "branchid")
    private UUID branchId;

    @Column(name = "typeid")
    private Integer typeId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "no")
    private String no;

    @Column(name = "accountingobjectid")
    private UUID accountingObjectId;

    @Column(name = "accountingobjectname")
    private String accountingObjectName;

    @Column(name = "accountingobjectaddress")
    private String accountingObjectAddress;

    @Column(name = "companytaxcode")
    private String companyTaxCode;

    @Column(name = "contactname")
    private String contactName;

    @Column(name = "reason")
    private String reason;

    @Column(name = "currencyid")
    private String currencyId;

    @Column(name = "exchangerate", precision = 10, scale = 2)
    private BigDecimal exchangeRate;

    @Column(name = "deliverdate")
    private LocalDate deliverDate;

    @Column(name = "shippingplace")
    private String shippingPlace;

    @Column(name = "transpotmethodid")
    private UUID transportMethodId;

    @Column(name = "employeeid")
    private UUID employeeId;

    @Column(name = "totalamount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "totalamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalAmountOriginal;

    @Column(name = "totalvatamount", precision = 10, scale = 2)
    private BigDecimal totalVATAmount;

    @Column(name = "totalvatamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalVATAmountOriginal;

    @Column(name = "totaldiscountamount", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmount;

    @Column(name = "totaldiscountamountoriginal", precision = 10, scale = 2)
    private BigDecimal totalDiscountAmountOriginal;

    @Column(name = "templateid")
    private UUID templateId;

    @Column(name = "status")
    private Integer status;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "pporderid", nullable = false)
    private Set<PPOrderDetail> ppOrderDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<PPOrderDetail> getPpOrderDetails() {
        return ppOrderDetails;
    }

    public void setPpOrderDetails(Set<PPOrderDetail> ppOrderDetails) {
        this.ppOrderDetails = ppOrderDetails;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public UUID getBranchId() {
        return branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public PPOrder typeId(Integer typeId) {
        this.typeId = typeId;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public PPOrder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public PPOrder no(String no) {
        this.no = no;
        return this;
    }

    public UUID getAccountingObjectId() {
        return accountingObjectId;
    }

    public void setAccountingObjectId(UUID accountingObjectId) {
        this.accountingObjectId = accountingObjectId;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public PPOrder accountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
        return this;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public PPOrder accountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
        return this;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public PPOrder companyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PPOrder reason(String reason) {
        this.reason = reason;
        return this;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public PPOrder currencyId(String currencyId) {
        this.currencyId = currencyId;
        return this;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public PPOrder exchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public LocalDate getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(LocalDate deliverDate) {
        this.deliverDate = deliverDate;
    }

    public PPOrder deliverDate(LocalDate deliverDate) {
        this.deliverDate = deliverDate;
        return this;
    }

    public String getShippingPlace() {
        return shippingPlace;
    }

    public void setShippingPlace(String shippingPlace) {
        this.shippingPlace = shippingPlace;
    }

    public PPOrder shippingPlace(String shippingPlace) {
        this.shippingPlace = shippingPlace;
        return this;
    }

    public UUID getTransportMethodId() {
        return transportMethodId;
    }

    public void setTransportMethodId(UUID transportMethodId) {
        this.transportMethodId = transportMethodId;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PPOrder totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public PPOrder totalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
        return this;
    }

    public BigDecimal getTotalVATAmount() {
        return totalVATAmount;
    }

    public void setTotalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
    }

    public PPOrder totalVATAmount(BigDecimal totalVATAmount) {
        this.totalVATAmount = totalVATAmount;
        return this;
    }

    public BigDecimal getTotalVATAmountOriginal() {
        return totalVATAmountOriginal;
    }

    public void setTotalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
    }

    public PPOrder totalVATAmountOriginal(BigDecimal totalVATAmountOriginal) {
        this.totalVATAmountOriginal = totalVATAmountOriginal;
        return this;
    }

    public BigDecimal getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public PPOrder totalDiscountAmount(BigDecimal totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
        return this;
    }

    public BigDecimal getTotalDiscountAmountOriginal() {
        return totalDiscountAmountOriginal;
    }

    public void setTotalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
    }

    public PPOrder totalDiscountAmountOriginal(BigDecimal totalDiscountAmountOriginal) {
        this.totalDiscountAmountOriginal = totalDiscountAmountOriginal;
        return this;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }

    public Integer isStatus() {
        return status;
    }

    public PPOrder status(Integer status) {
        this.status = status;
        return this;
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
        PPOrder pporder = (PPOrder) o;
        if (pporder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pporder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PPOrder{" +
            "id=" + getId() +
            ", companyId='" + getCompanyId() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", typeId='" + getTypeId() + "'" +
            ", date='" + getDate() + "'" +
            ", no='" + getNo() + "'" +
            ", accountingObjectId='" + getAccountingObjectId() + "'" +
            ", accountingObjectName='" + getAccountingObjectName() + "'" +
            ", accountingObjectAddress='" + getAccountingObjectAddress() + "'" +
            ", companyTaxCode='" + getCompanyTaxCode() + "'" +
            ", reason='" + getReason() + "'" +
            ", currencyId='" + getCurrencyId() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            ", deliverDate='" + getDeliverDate() + "'" +
            ", shippingPlace='" + getShippingPlace() + "'" +
            ", transportMethodId='" + getTransportMethodId() + "'" +
            ", employeeId='" + getEmployeeId() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", totalAmountOriginal=" + getTotalAmountOriginal() +
            ", totalVATAmount=" + getTotalVATAmount() +
            ", totalVATAmountOriginal=" + getTotalVATAmountOriginal() +
            ", totalDiscountAmount=" + getTotalDiscountAmount() +
            ", totalDiscountAmountOriginal=" + getTotalDiscountAmountOriginal() +
            ", templateId='" + getTemplateId() + "'" +
            ", status='" + isStatus() + "'" +
            "}";
    }
}
