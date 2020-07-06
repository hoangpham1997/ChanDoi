package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.MGForPPOrderConvertDTO;
import vn.softdreams.ebweb.service.dto.MaterialGoodsDTO;
import vn.softdreams.ebweb.service.dto.PPPayVendorBillDTO;
import vn.softdreams.ebweb.service.dto.PPPayVendorDTO;
import vn.softdreams.ebweb.web.rest.dto.IAPublishInvoiceDetailDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A IaPublishInvoiceDetails.
 */
@Entity
@Table(name = "iapublishinvoicedetail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "IAPublishInvoiceDetailDTO",
        classes = {
            @ConstructorResult(
                targetClass = IAPublishInvoiceDetailDTO.class,
                columns = {
                    @ColumnResult(name = "invoiceForm", type = Integer.class),
                    @ColumnResult(name = "invoiceTemplate", type = String.class),
                    @ColumnResult(name = "invoiceTypeID", type = UUID.class),
                    @ColumnResult(name = "invoiceSeries", type = String.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "IAPublishInvoiceDetailDTOs",
        classes = {
            @ConstructorResult(
                targetClass = IAPublishInvoiceDetailDTO.class,
                columns = {
                    @ColumnResult(name = "invoiceTypeID", type = UUID.class),
                    @ColumnResult(name = "invoiceForm", type = Integer.class),
                    @ColumnResult(name = "invoiceSeries", type = String.class),
                    @ColumnResult(name = "invoiceTemplate", type = String.class),
                    @ColumnResult(name = "startUsing", type = LocalDate.class)
                }
            )
        }
    )
})
public class IaPublishInvoiceDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "iapublishinvoiceid", insertable = false, updatable = false)
    private UUID iaPublishInvoiceID;

    @Column(name = "iareportid")
    private UUID iaReportID;

    @Column(name = "invoiceform")
    private Integer invoiceForm;

    @Transient
    private String invoiceFormName;

    @Column(name = "invoicetemplate")
    private String invoiceTemplate;

    @ManyToOne
    @JoinColumn(name = "invoicetypeid")
    private InvoiceType invoiceType;

    @Transient
    private String invoiceTypeName;

    @Column(name = "invoiceseries")
    private String invoiceSeries;

    @Column(name = "fromno")
    private String fromNo;

    @Column(name = "tono")
    private String toNo;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Transient
    private String quantityToString;

    @Column(name = "startusing")
    private LocalDate startUsing;

    @Transient
    private String startUsingToString;

    @Column(name = "companyname")
    private String companyName;

    @Column(name = "companytaxcode")
    private String companyTaxCode;

    @Column(name = "contractno")
    private String contractNo;

    @Column(name = "contractdate")
    private LocalDate contractDate;

    @Transient
    private String contractDateToString;

    @Column(name = "orderpriority")
    private Integer orderPriority;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getIaPublishInvoiceID() {
        return iaPublishInvoiceID;
    }

    public void setIaPublishInvoiceID(UUID iaPublishInvoiceID) {
        this.iaPublishInvoiceID = iaPublishInvoiceID;
    }

    public UUID getIaReportID() {
        return iaReportID;
    }

    public void setIaReportID(UUID iaReportID) {
        this.iaReportID = iaReportID;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public IaPublishInvoiceDetails invoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
        return this;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public IaPublishInvoiceDetails invoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
        return this;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public IaPublishInvoiceDetails invoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
        return this;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public String getFromNo() {
        return fromNo;
    }

    public IaPublishInvoiceDetails fromNo(String fromNo) {
        this.fromNo = fromNo;
        return this;
    }

    public void setFromNo(String fromNo) {
        this.fromNo = fromNo;
    }

    public String getToNo() {
        return toNo;
    }

    public IaPublishInvoiceDetails toNo(String toNo) {
        this.toNo = toNo;
        return this;
    }

    public void setToNo(String toNo) {
        this.toNo = toNo;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public IaPublishInvoiceDetails quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDate getStartUsing() {
        return startUsing;
    }

    public IaPublishInvoiceDetails startUsing(LocalDate startUsing) {
        this.startUsing = startUsing;
        return this;
    }

    public void setStartUsing(LocalDate startUsing) {
        this.startUsing = startUsing;
    }

    public String getCompanyName() {
        return companyName;
    }

    public IaPublishInvoiceDetails companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyTaxCode() {
        return companyTaxCode;
    }

    public IaPublishInvoiceDetails companyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
        return this;
    }

    public void setCompanyTaxCode(String companyTaxCode) {
        this.companyTaxCode = companyTaxCode;
    }

    public String getContractNo() {
        return contractNo;
    }

    public IaPublishInvoiceDetails contractNo(String contractNo) {
        this.contractNo = contractNo;
        return this;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public IaPublishInvoiceDetails contractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
        return this;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public IaPublishInvoiceDetails orderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
        return this;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getInvoiceFormName() {
        return invoiceFormName;
    }

    public void setInvoiceFormName(String invoiceFormName) {
        this.invoiceFormName = invoiceFormName;
    }

    public String getInvoiceTypeName() {
        return invoiceTypeName;
    }

    public void setInvoiceTypeName(String invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
    }

    public String getStartUsingToString() {
        return startUsingToString;
    }

    public void setStartUsingToString(String startUsingToString) {
        this.startUsingToString = startUsingToString;
    }

    public String getQuantityToString() {
        return quantityToString;
    }

    public void setQuantityToString(String quantityToString) {
        this.quantityToString = quantityToString;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IaPublishInvoiceDetails that = (IaPublishInvoiceDetails) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getIaPublishInvoiceID() != null ? !getIaPublishInvoiceID().equals(that.getIaPublishInvoiceID()) : that.getIaPublishInvoiceID() != null)
            return false;
        if (getIaReportID() != null ? !getIaReportID().equals(that.getIaReportID()) : that.getIaReportID() != null)
            return false;
        if (getInvoiceForm() != null ? !getInvoiceForm().equals(that.getInvoiceForm()) : that.getInvoiceForm() != null)
            return false;
        if (getInvoiceTemplate() != null ? !getInvoiceTemplate().equals(that.getInvoiceTemplate()) : that.getInvoiceTemplate() != null)
            return false;
        if (getInvoiceType() != null ? !getInvoiceType().equals(that.getInvoiceType()) : that.getInvoiceType() != null)
            return false;
        if (getInvoiceSeries() != null ? !getInvoiceSeries().equals(that.getInvoiceSeries()) : that.getInvoiceSeries() != null)
            return false;
        if (getFromNo() != null ? !getFromNo().equals(that.getFromNo()) : that.getFromNo() != null) return false;
        if (getToNo() != null ? !getToNo().equals(that.getToNo()) : that.getToNo() != null) return false;
        if (getQuantity() != null ? getQuantity().intValue() != that.getQuantity().intValue() : that.getQuantity() != null)
            return false;
        if (getStartUsing() != null ? !getStartUsing().equals(that.getStartUsing()) : that.getStartUsing() != null)
            return false;
        if (getCompanyName() != null ? !getCompanyName().equals(that.getCompanyName()) : that.getCompanyName() != null)
            return false;
        if (getCompanyTaxCode() != null ? !getCompanyTaxCode().equals(that.getCompanyTaxCode()) : that.getCompanyTaxCode() != null)
            return false;
        if (getContractNo() != null ? !getContractNo().equals(that.getContractNo()) : that.getContractNo() != null)
            return false;
        if (getContractDate() != null ? !getContractDate().equals(that.getContractDate()) : that.getContractDate() != null)
            return false;
        return getOrderPriority() != null ? getOrderPriority().equals(that.getOrderPriority()) : that.getOrderPriority() == null;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getContractDateToString() {
        return contractDateToString;
    }

    public void setContractDateToString(String contractDateToString) {
        this.contractDateToString = contractDateToString;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IaPublishInvoiceDetails{" +
            "id=" + getId() +
            ", iaPublishInvoiceID='" + getIaPublishInvoiceID() + "'" +
            ", iaReportID='" + getIaReportID() + "'" +
            ", invoiceForm=" + getInvoiceForm() +
            ", invoiceTemplate='" + getInvoiceTemplate() + "'" +
            ", invoiceSeries='" + getInvoiceSeries() + "'" +
            ", fromNo='" + getFromNo() + "'" +
            ", toNo='" + getToNo() + "'" +
            ", quantity=" + getQuantity() +
            ", startUsing='" + getStartUsing() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", companyTaxCode='" + getCompanyTaxCode() + "'" +
            ", contractNo='" + getContractNo() + "'" +
            ", contractDate='" + getContractDate() + "'" +
            ", orderPriority=" + getOrderPriority() +
            "}";
    }

    public IaPublishInvoiceDetails() {
    }

    public IaPublishInvoiceDetails(UUID iaPublishInvoiceID, UUID iaReportID, Integer invoiceForm, String invoiceTemplate, InvoiceType invoiceType, String invoiceSeries, String fromNo, String toNo, BigDecimal quantity, LocalDate startUsing, String companyName, String companyTaxCode, String contractNo, LocalDate contractDate, Integer orderPriority) {
        this.iaPublishInvoiceID = iaPublishInvoiceID;
        this.iaReportID = iaReportID;
        this.invoiceForm = invoiceForm;
        this.invoiceTemplate = invoiceTemplate;
        this.invoiceType = invoiceType;
        this.invoiceSeries = invoiceSeries;
        this.fromNo = fromNo;
        this.toNo = toNo;
        this.quantity = quantity;
        this.startUsing = startUsing;
        this.companyName = companyName;
        this.companyTaxCode = companyTaxCode;
        this.contractNo = contractNo;
        this.contractDate = contractDate;
        this.orderPriority = orderPriority;
    }
}
