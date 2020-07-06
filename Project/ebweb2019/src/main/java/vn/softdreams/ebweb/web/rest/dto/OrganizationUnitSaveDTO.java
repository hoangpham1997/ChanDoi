package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.domain.Unit;

public class OrganizationUnitSaveDTO {
    private OrganizationUnit organizationUnit;

    private int status;

    public OrganizationUnitSaveDTO() {
    }

    public OrganizationUnitSaveDTO(OrganizationUnit organizationUnit, int status) {
        this.organizationUnit = organizationUnit;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }
}
