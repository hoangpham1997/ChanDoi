package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.EbPackage;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.domain.SystemOption;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EbUserPackageDTO {
    private UUID id;

    private Long userID;

    private boolean activated = false;

    private UUID orgID;

    private UUID companyID;

    private UUID packageID;

    private OrganizationUnit organizationUnit;

    private EbPackage ebPackage;

    private Integer status;

    private Boolean isTotalPackage;

    public EbUserPackageDTO() {
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public EbUserPackageDTO(UUID id, Long userID, UUID companyID, UUID packageID, Integer status, Boolean isTotalPackage) {
        this.id = id;
        this.userID = userID;
        this.companyID = companyID;
        this.packageID = packageID;
        this.status = status;
        this.isTotalPackage = isTotalPackage;
    }

    public EbUserPackageDTO(UUID id,
                            Long userId,
                            boolean activated,
                            UUID orgID,
                            UUID packageID,
                            OrganizationUnit organizationUnit,
                            EbPackage ebPackage,
                            Integer status,
                            boolean isTotalPackage) {
        this.id = id;
        this.activated = activated;
        this.orgID = orgID;
        this.packageID = packageID;
        this.organizationUnit = organizationUnit;
        this.ebPackage = ebPackage;
        this.status = status;
        this.isTotalPackage = isTotalPackage;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public UUID getOrgID() {
        return orgID;
    }

    public void setOrgID(UUID orgID) {
        this.orgID = orgID;
    }

    public UUID getPackageID() {
        return packageID;
    }

    public void setPackageID(UUID packageID) {
        this.packageID = packageID;
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public EbPackage getEbPackage() {
        return ebPackage;
    }

    public void setEbPackage(EbPackage ebPackage) {
        this.ebPackage = ebPackage;
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
}
