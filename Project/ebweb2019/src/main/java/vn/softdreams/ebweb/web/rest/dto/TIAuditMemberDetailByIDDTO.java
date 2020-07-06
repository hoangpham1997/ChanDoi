package vn.softdreams.ebweb.web.rest.dto;

import java.util.UUID;

public class TIAuditMemberDetailByIDDTO {
    private UUID id;
    private UUID tiAuditID;
    private UUID accountingObjectID;
    private String accountingObjectCode;
    private String accountObjectName;
    private String accountingObjectTitle;
    private String role;
    private UUID departmentID;
    private String departmentCode;
    private Integer orderPriority;

    public TIAuditMemberDetailByIDDTO() {
    }

    public TIAuditMemberDetailByIDDTO(UUID id, UUID tiAuditID, UUID accountingObjectID, String accountingObjectCode, String accountObjectName, String accountingObjectTitle, String role, UUID departmentID, String departmentCode, Integer orderPriority) {
        this.id = id;
        this.tiAuditID = tiAuditID;
        this.accountingObjectID = accountingObjectID;
        this.accountingObjectCode = accountingObjectCode;
        this.accountObjectName = accountObjectName;
        this.accountingObjectTitle = accountingObjectTitle;
        this.role = role;
        this.departmentID = departmentID;
        this.departmentCode = departmentCode;
        this.orderPriority = orderPriority;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTiAuditID() {
        return tiAuditID;
    }

    public void setTiAuditID(UUID tiAuditID) {
        this.tiAuditID = tiAuditID;
    }

    public UUID getAccountingObjectID() {
        return accountingObjectID;
    }

    public void setAccountingObjectID(UUID accountingObjectID) {
        this.accountingObjectID = accountingObjectID;
    }

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UUID getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(UUID departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }
}
