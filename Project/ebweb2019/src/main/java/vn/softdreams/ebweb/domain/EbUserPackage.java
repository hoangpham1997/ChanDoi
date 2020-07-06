package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A EbUserPackage.
 */
@Entity
@Table(name = "EbUserPackage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EbUserPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "userid")
    private Long userID;

    @Column(name = "packageid")
    private UUID packageID;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "status")
    private Integer status;

    @Column(name = "istotalpackage")
    private Boolean isTotalPackage;

    @Column(name = "activeddate")
    private LocalDate activedDate;

    @Column(name = "exprireddate")
    private LocalDate expriredDate;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public UUID getPackageID() {
        return packageID;
    }

    public void setPackageID(UUID packageID) {
        this.packageID = packageID;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsTotalPackage() {
        return isTotalPackage;
    }

    public void setIsTotalPackage(Boolean totalPackage) {
        isTotalPackage = totalPackage;
    }

    public LocalDate getActivedDate() {
        return activedDate;
    }

    public void setActivedDate(LocalDate activedDate) {
        this.activedDate = activedDate;
    }

    public LocalDate getExpriredDate() {
        return expriredDate;
    }

    public void setExpriredDate(LocalDate expriredDate) {
        this.expriredDate = expriredDate;
    }
}
