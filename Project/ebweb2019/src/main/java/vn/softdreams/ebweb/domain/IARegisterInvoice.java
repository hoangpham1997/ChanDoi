package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

/**
 * A IARegisterInvoice.
 */
@Entity
@Table(name = "iaregisterinvoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IARegisterInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "sequenceGenerator")
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "typeid")
    private Integer typeId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "no")
    private String no;

    @Column(name = "description")
    private String description;

    @Column(name = "signer")
    private String signer;

    @Column(name = "status")
    private Integer status;

    @Column(name = "attachfilename")
    private String attachFileName;

    @Column(name = "attachfilecontent")
    private byte[] attachFileContent;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "iaregisterinvoiceid", nullable = false)
    private Set<IARegisterInvoiceDetails> iaRegisterInvoiceDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Set<IARegisterInvoiceDetails> getIaRegisterInvoiceDetails() {
        return iaRegisterInvoiceDetails;
    }

    public void setIaRegisterInvoiceDetails(Set<IARegisterInvoiceDetails> iaRegisterInvoiceDetail) {
        this.iaRegisterInvoiceDetails = iaRegisterInvoiceDetail;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public IARegisterInvoice date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public IARegisterInvoice no(String no) {
        this.no = no;
        return this;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public IARegisterInvoice description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSigner() {
        return signer;
    }

    public IARegisterInvoice signer(String signer) {
        this.signer = signer;
        return this;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public Integer getStatus() {
        return status;
    }

    public IARegisterInvoice status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public IARegisterInvoice attachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
        return this;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public byte[] getAttachFileContent() {
        return attachFileContent;
    }

    public void setAttachFileContent(byte[] attachFileContent) {
        this.attachFileContent = attachFileContent;
    }

    public void resetAttachFileContent() {
        this.attachFileContent = new byte[]{};
    }

    public void setAttachFileContent(String attachFileContent) throws UnsupportedEncodingException {
        this.attachFileContent = Base64.getDecoder().decode(attachFileContent.getBytes(StandardCharsets.UTF_8));
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
        IARegisterInvoice iARegisterInvoice = (IARegisterInvoice) o;
        if (iARegisterInvoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), iARegisterInvoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IARegisterInvoice{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", no='" + getNo() + "'" +
            ", description='" + getDescription() + "'" +
            ", signer='" + getSigner() + "'" +
            ", status=" + getStatus() +
            ", attachFileName='" + getAttachFileName() + "'" +
            ", attachFileContent='" + getAttachFileContent() + "'" +
            "}";
    }
}
