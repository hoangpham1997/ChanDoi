package vn.softdreams.ebweb.web.rest.dto;

public class TIAuditMemberDetailDTO {
    private String accountObjectName;

    private String accountingObjectTitle;

    private String organizationUnitName;

    public TIAuditMemberDetailDTO(String accountObjectName, String accountingObjectTitle, String organizationUnitName) {
        this.accountObjectName = accountObjectName;
        this.accountingObjectTitle = accountingObjectTitle;
        this.organizationUnitName = organizationUnitName;
    }

    public TIAuditMemberDetailDTO() {
    }

    public String getAccountObjectName() {
        return accountObjectName;
    }

    public void setAccountObjectName(String accountObjectName) {
        this.accountObjectName = accountObjectName;
    }

    public String getAccountingObjectTitle() {
        return accountingObjectTitle;
    }

    public void setAccountingObjectTitle(String accountingObjectTitle) {
        this.accountingObjectTitle = accountingObjectTitle;
    }

    public String getOrganizationUnitName() {
        return organizationUnitName;
    }

    public void setOrganizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
    }
}
