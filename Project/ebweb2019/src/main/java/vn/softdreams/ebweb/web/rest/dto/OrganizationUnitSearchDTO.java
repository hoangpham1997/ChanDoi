package vn.softdreams.ebweb.web.rest.dto;

public class OrganizationUnitSearchDTO {
    private String organizationUnitName;
    private String organizationUnitCode;
    private String taxCode;

    public OrganizationUnitSearchDTO () {}

    public OrganizationUnitSearchDTO (String organizationUnitName, String organizationUnitCode, String taxCode) {
        this.organizationUnitCode = organizationUnitCode;
        this.organizationUnitName = organizationUnitName;
        this.taxCode = taxCode;
    }

    public String getOrganizationUnitName() {
        return organizationUnitName;
    }

    public void setOrganizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
    }

    public String getOrganizationUnitCode() {
        return organizationUnitCode;
    }

    public void setOrganizationUnitCode(String organizationUnitCode) {
        this.organizationUnitCode = organizationUnitCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }
}
