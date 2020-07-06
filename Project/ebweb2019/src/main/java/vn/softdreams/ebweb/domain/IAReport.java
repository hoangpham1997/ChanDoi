package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A IAReport.
 */
@Entity
@Table(name = "iareport")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IAReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @NotNull
    @Column(name = "reportname", nullable = false)
    private String reportName;

    @NotNull
    @Column(name = "invoiceform", nullable = false)
    private Integer invoiceForm;

    @NotNull
    @Column(name = "tempsortorder", nullable = false)
    private Integer tempSortOrder;

    @ManyToOne
    @JoinColumn(name = "invoicetypeid")
//    @Column(name = "invoicetypeid")
    private InvoiceType invoiceType;

    @NotNull
    @Column(name = "invoicetemplate")
    private String invoiceTemplate;

    @NotNull
    @Column(name = "invoiceseries")
    private String invoiceSeries;

    @NotNull
    @Column(name = "copynumber", precision = 10, scale = 2, nullable = false)
    private BigDecimal copyNumber;

    @Column(name = "purpose1")
    private String purpose1;

    @Column(name = "codecolor1")
    private String codeColor1;

    @Column(name = "purpose2")
    private String purpose2;

    @Column(name = "codecolor2")
    private String codeColor2;

    @Column(name = "purpose3")
    private String purpose3;

    @Column(name = "codecolor3")
    private String codeColor3;

    @Column(name = "purpose4")
    private String purpose4;

    @Column(name = "codecolor4")
    private String codeColor4;

    @Column(name = "purpose5")
    private String purpose5;

    @Column(name = "codecolor5")
    private String codeColor5;

    @Column(name = "purpose6")
    private String purpose6;

    @Column(name = "codecolor6")
    private String codeColor6;

    @Column(name = "purpose7")
    private String purpose7;

    @Column(name = "codecolor7")
    private String codeColor7;

    @Column(name = "purpose8")
    private String purpose8;

    @Column(name = "codecolor8")
    private String codeColor8;

    @Column(name = "purpose9")
    private String purpose9;

    @Column(name = "codecolor9")
    private String codeColor9;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public Integer getTempSortOrder() {
        return tempSortOrder;
    }

    public void setTempSortOrder(Integer tempSortOrder) {
        this.tempSortOrder = tempSortOrder;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public IAReport reportName(String reportName) {
        this.reportName = reportName;
        return this;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Integer getInvoiceForm() {
        return invoiceForm;
    }

    public IAReport invoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
        return this;
    }

    public void setInvoiceForm(Integer invoiceForm) {
        this.invoiceForm = invoiceForm;
    }

    public String getInvoiceTemplate() {
        return invoiceTemplate;
    }

    public IAReport invoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
        return this;
    }

    public void setInvoiceTemplate(String invoiceTemplate) {
        this.invoiceTemplate = invoiceTemplate;
    }

    public String getInvoiceSeries() {
        return invoiceSeries;
    }

    public IAReport invoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
        return this;
    }

    public void setInvoiceSeries(String invoiceSeries) {
        this.invoiceSeries = invoiceSeries;
    }

    public BigDecimal getCopyNumber() {
        return copyNumber;
    }

    public IAReport copyNumber(BigDecimal copyNumber) {
        this.copyNumber = copyNumber;
        return this;
    }

    public void setCopyNumber(BigDecimal copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getPurpose1() {
        return purpose1;
    }

    public IAReport purpose1(String purpose1) {
        this.purpose1 = purpose1;
        return this;
    }

    public void setPurpose1(String purpose1) {
        this.purpose1 = purpose1;
    }

    public String getCodeColor1() {
        return codeColor1;
    }

    public IAReport codeColor1(String codeColor1) {
        this.codeColor1 = codeColor1;
        return this;
    }

    public void setCodeColor1(String codeColor1) {
        this.codeColor1 = codeColor1;
    }

    public String getPurpose2() {
        return purpose2;
    }

    public IAReport purpose2(String purpose2) {
        this.purpose2 = purpose2;
        return this;
    }

    public void setPurpose2(String purpose2) {
        this.purpose2 = purpose2;
    }

    public String getCodeColor2() {
        return codeColor2;
    }

    public IAReport codeColor2(String codeColor2) {
        this.codeColor2 = codeColor2;
        return this;
    }

    public void setCodeColor2(String codeColor2) {
        this.codeColor2 = codeColor2;
    }

    public String getPurpose3() {
        return purpose3;
    }

    public IAReport purpose3(String purpose3) {
        this.purpose3 = purpose3;
        return this;
    }

    public void setPurpose3(String purpose3) {
        this.purpose3 = purpose3;
    }

    public String getCodeColor3() {
        return codeColor3;
    }

    public IAReport codeColor3(String codeColor3) {
        this.codeColor3 = codeColor3;
        return this;
    }

    public void setCodeColor3(String codeColor3) {
        this.codeColor3 = codeColor3;
    }

    public String getPurpose4() {
        return purpose4;
    }

    public IAReport purpose4(String purpose4) {
        this.purpose4 = purpose4;
        return this;
    }

    public void setPurpose4(String purpose4) {
        this.purpose4 = purpose4;
    }

    public String getCodeColor4() {
        return codeColor4;
    }

    public IAReport codeColor4(String codeColor4) {
        this.codeColor4 = codeColor4;
        return this;
    }

    public void setCodeColor4(String codeColor4) {
        this.codeColor4 = codeColor4;
    }

    public String getPurpose5() {
        return purpose5;
    }

    public IAReport purpose5(String purpose5) {
        this.purpose5 = purpose5;
        return this;
    }

    public void setPurpose5(String purpose5) {
        this.purpose5 = purpose5;
    }

    public String getCodeColor5() {
        return codeColor5;
    }

    public IAReport codeColor5(String codeColor5) {
        this.codeColor5 = codeColor5;
        return this;
    }

    public void setCodeColor5(String codeColor5) {
        this.codeColor5 = codeColor5;
    }

    public String getPurpose6() {
        return purpose6;
    }

    public IAReport purpose6(String purpose6) {
        this.purpose6 = purpose6;
        return this;
    }

    public void setPurpose6(String purpose6) {
        this.purpose6 = purpose6;
    }

    public String getCodeColor6() {
        return codeColor6;
    }

    public IAReport codeColor6(String codeColor6) {
        this.codeColor6 = codeColor6;
        return this;
    }

    public void setCodeColor6(String codeColor6) {
        this.codeColor6 = codeColor6;
    }

    public String getPurpose7() {
        return purpose7;
    }

    public IAReport purpose7(String purpose7) {
        this.purpose7 = purpose7;
        return this;
    }

    public void setPurpose7(String purpose7) {
        this.purpose7 = purpose7;
    }

    public String getCodeColor7() {
        return codeColor7;
    }

    public IAReport codeColor7(String codeColor7) {
        this.codeColor7 = codeColor7;
        return this;
    }

    public void setCodeColor7(String codeColor7) {
        this.codeColor7 = codeColor7;
    }

    public String getPurpose8() {
        return purpose8;
    }

    public IAReport purpose8(String purpose8) {
        this.purpose8 = purpose8;
        return this;
    }

    public void setPurpose8(String purpose8) {
        this.purpose8 = purpose8;
    }

    public String getCodeColor8() {
        return codeColor8;
    }

    public IAReport codeColor8(String codeColor8) {
        this.codeColor8 = codeColor8;
        return this;
    }

    public void setCodeColor8(String codeColor8) {
        this.codeColor8 = codeColor8;
    }

    public String getPurpose9() {
        return purpose9;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public IAReport purpose9(String purpose9) {
        this.purpose9 = purpose9;
        return this;
    }

    public void setPurpose9(String purpose9) {
        this.purpose9 = purpose9;
    }

    public String getCodeColor9() {
        return codeColor9;
    }

    public IAReport codeColor9(String codeColor9) {
        this.codeColor9 = codeColor9;
        return this;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }


    public void setCodeColor9(String codeColor9) {
        this.codeColor9 = codeColor9;
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
        IAReport iAReport = (IAReport) o;
        if (iAReport.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), iAReport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IAReport{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", invoiceForm=" + getInvoiceForm() +
            ", invoiceTemplate='" + getInvoiceTemplate() + "'" +
            ", invoiceSeries='" + getInvoiceSeries() + "'" +
            ", copyNumber=" + getCopyNumber() +
            ", purpose1='" + getPurpose1() + "'" +
            ", codeColor1='" + getCodeColor1() + "'" +
            ", purpose2='" + getPurpose2() + "'" +
            ", codeColor2='" + getCodeColor2() + "'" +
            ", purpose3='" + getPurpose3() + "'" +
            ", codeColor3='" + getCodeColor3() + "'" +
            ", purpose4='" + getPurpose4() + "'" +
            ", codeColor4='" + getCodeColor4() + "'" +
            ", purpose5='" + getPurpose5() + "'" +
            ", codeColor5='" + getCodeColor5() + "'" +
            ", purpose6='" + getPurpose6() + "'" +
            ", codeColor6='" + getCodeColor6() + "'" +
            ", purpose7='" + getPurpose7() + "'" +
            ", codeColor7='" + getCodeColor7() + "'" +
            ", purpose8='" + getPurpose8() + "'" +
            ", codeColor8='" + getCodeColor8() + "'" +
            ", purpose9='" + getPurpose9() + "'" +
            ", codeColor9='" + getCodeColor9() + "'" +
            "}";
    }
}
