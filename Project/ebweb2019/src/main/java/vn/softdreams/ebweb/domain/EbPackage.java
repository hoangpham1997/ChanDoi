package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A EbPackage.
 */
@Entity
@Table(name = "ebpackage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EbPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Size(max = 50)
    @Column(name = "packagecode", length = 50)
    private String packageCode;

    @Size(max = 255)
    @Column(name = "packagename", length = 255)
    private String packageName;

    @Column(name = "limitedcompany")
    private Integer limitedCompany;

    @Column(name = "limiteduser")
    private Integer limitedUser;

    @Column(name = "limitedvoucher")
    private Integer limitedVoucher;

    @Column(name = "expiredtime")
    private Integer expiredTime;

    @Column(name = "status")
    private Boolean status;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Boolean isTotalPackage;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Boolean isSave;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "type")
    private Integer type;

    @Column(name = "comtype")
    private Integer comType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public EbPackage packageCode(String packageCode) {
        this.packageCode = packageCode;
        return this;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public EbPackage packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getLimitedCompany() {
        return limitedCompany;
    }

    public EbPackage limitedCompany(Integer limitedCompany) {
        this.limitedCompany = limitedCompany;
        return this;
    }

    public void setLimitedCompany(Integer limitedCompany) {
        this.limitedCompany = limitedCompany;
    }

    public Integer getLimitedUser() {
        return limitedUser;
    }

    public EbPackage limitedUser(Integer limitedUser) {
        this.limitedUser = limitedUser;
        return this;
    }

    public void setLimitedUser(Integer limitedUser) {
        this.limitedUser = limitedUser;
    }

    public Integer getLimitedVoucher() {
        return limitedVoucher;
    }

    public EbPackage limitedVoucher(Integer limitedVoucher) {
        this.limitedVoucher = limitedVoucher;
        return this;
    }

    public void setLimitedVoucher(Integer limitedVoucher) {
        this.limitedVoucher = limitedVoucher;
    }

    public Integer getExpiredTime() {
        return expiredTime;
    }

    public EbPackage expiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
        return this;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Boolean isStatus() {
        return status;
    }

    public EbPackage status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public EbPackage description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public EbPackage type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
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
        EbPackage ebPackage = (EbPackage) o;
        if (ebPackage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ebPackage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EbPackage{" +
            "id=" + getId() +
            ", packageCode='" + getPackageCode() + "'" +
            ", packageName='" + getPackageName() + "'" +
            ", limitedCompany=" + getLimitedCompany() +
            ", limitedUser=" + getLimitedUser() +
            ", limitedVoucher=" + getLimitedVoucher() +
            ", expiredTime=" + getExpiredTime() +
            ", status='" + isStatus() + "'" +
            ", description='" + getDescription() + "'" +
            ", type=" + getType() +
            "}";
    }

    public Boolean getIsTotalPackage() {
        return isTotalPackage;
    }

    public void setIsTotalPackage(Boolean totalPackage) {
        isTotalPackage = totalPackage;
    }

    public Boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(Boolean save) {
        isSave = save;
    }

    public Integer getComType() {
        return comType;
    }

    public void setComType(Integer comType) {
        this.comType = comType;
    }
}
