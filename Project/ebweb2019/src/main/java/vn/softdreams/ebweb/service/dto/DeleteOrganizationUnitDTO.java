package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.OrganizationUnit;

import java.util.UUID;

public class DeleteOrganizationUnitDTO {
    public UUID getOrgID() {
        return orgID;
    }

    public void setOrgID(UUID orgID) {
        this.orgID = orgID;
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public DeleteOrganizationUnitDTO(UUID orgID, OrganizationUnit organizationUnit, Boolean skipExistUserOrgID) {
        this.orgID = orgID;
        this.organizationUnit = organizationUnit;
        this.skipExistUserOrgID = skipExistUserOrgID;
    }

    public DeleteOrganizationUnitDTO() {
    }

    private UUID orgID;
    private OrganizationUnit organizationUnit;
    private Boolean skipExistUserOrgID;

    public Boolean getSkipExistUserOrgID() {
        return skipExistUserOrgID;
    }

    public void setSkipExistUserOrgID(Boolean skipExistUserOrgID) {
        this.skipExistUserOrgID = skipExistUserOrgID;
    }
}
