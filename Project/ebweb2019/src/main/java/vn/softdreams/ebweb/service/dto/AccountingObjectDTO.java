package vn.softdreams.ebweb.service.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class AccountingObjectDTO {
    private UUID id;

    private UUID branchId;

    private UUID companyId;

    private String accountingObjectCode;

    private String accountingObjectName;

    private LocalDate employeeBirthday;

    private String accountingObjectAddress;

    private String tel;

    private String fax;

    private String email;

    private String website;

    private String bankName;

    private String taxCode;

    private String description;

    private String contactName;

    private String contactTitle;

    private Integer contactSex;

    private String contactMobile;

    private String contactEmail;

    private String contactHomeTel;

    private String contactOfficeTel;

    private String contactAddress;

    private Integer scaleType;

    private Integer objectType;

    private Boolean isEmployee;

    private String identificationNo;

    private LocalDate issueDate;

    private String issueBy;

    private Integer numberOfDependent;

    private BigDecimal agreementSalary;

    private BigDecimal insuranceSalary;

    private BigDecimal salarycoEfficient;

    private Boolean isUnofficialStaff;

    private BigDecimal maximizaDebtAmount;

    private Integer dueTime;

    private Boolean isActive;

    private UUID departmentId;

    public AccountingObjectDTO() {
    }

    public AccountingObjectDTO(UUID id, UUID branchId, UUID companyId, String accountingObjectCode,
                               String accountingObjectName, LocalDate employeeBirthday,
                               String accountingObjectAddress, String tel, String fax, String email,
                               String website, String bankName, String taxCode, Boolean isEmployee,
                               Boolean isActive, Integer objectType,
                               String contactName, String identificationNo,
                               LocalDate issueDate, String issueBy, UUID departmentId,
                               String contactTitle
                               ) {
        this.id = id;
        this.branchId = branchId;
        this.companyId = companyId;
        this.accountingObjectCode = accountingObjectCode;
        this.accountingObjectName = accountingObjectName;
        this.employeeBirthday = employeeBirthday;
        this.accountingObjectAddress = accountingObjectAddress;
        this.tel = tel;
        this.fax = fax;
        this.email = email;
        this.website = website;
        this.bankName = bankName;
        this.taxCode = taxCode;
        this.isEmployee = isEmployee;
        this.isActive = isActive;
        this.objectType = objectType;
        this.contactName = contactName;
        this.identificationNo = identificationNo;
        this.issueDate = issueDate;
        this.issueBy = issueBy;
        this.departmentId = departmentId;
        this.contactTitle = contactTitle;
    }

    public AccountingObjectDTO(UUID id, UUID branchId, UUID companyId, String accountingObjectCode,
                               String accountingObjectName, String contactName, LocalDate employeeBirthday,
                               String accountingObjectAddress, String tel, String fax, String email,
                               String website, String bankName, String taxCode, Boolean isEmployee,
                               Boolean isActive, Integer objectType
    ) {
        this.id = id;
        this.branchId = branchId;
        this.companyId = companyId;
        this.accountingObjectCode = accountingObjectCode;
        this.accountingObjectName = accountingObjectName;
        this.contactName = contactName;
        this.employeeBirthday = employeeBirthday;
        this.accountingObjectAddress = accountingObjectAddress;
        this.tel = tel;
        this.fax = fax;
        this.email = email;
        this.website = website;
        this.bankName = bankName;
        this.taxCode = taxCode;
        this.isEmployee = isEmployee;
        this.isActive = isActive;
        this.objectType = objectType;
    }
    public AccountingObjectDTO(UUID id, String accountingObjectCode, String accountingObjectName) {
        this.id = id;
        this.accountingObjectCode = accountingObjectCode;
        this.accountingObjectName = accountingObjectName;
    }

    public AccountingObjectDTO(UUID id, String accountingObjectCode, String accountingObjectName, String accountingObjectAddress) {
        this.id = id;
        this.accountingObjectCode = accountingObjectCode;
        this.accountingObjectName = accountingObjectName;
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBranchId() {
        return branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public String getAccountingObjectCode() {
        return accountingObjectCode;
    }

    public void setAccountingObjectCode(String accountingObjectCode) {
        this.accountingObjectCode = accountingObjectCode;
    }

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public LocalDate getEmployeeBirthday() {
        return employeeBirthday;
    }

    public void setEmployeeBirthday(LocalDate employeeBirthday) {
        this.employeeBirthday = employeeBirthday;
    }

    public String getAccountingObjectAddress() {
        return accountingObjectAddress;
    }

    public void setAccountingObjectAddress(String accountingObjectAddress) {
        this.accountingObjectAddress = accountingObjectAddress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    public Integer getContactSex() {
        return contactSex;
    }

    public void setContactSex(Integer contactSex) {
        this.contactSex = contactSex;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactHomeTel() {
        return contactHomeTel;
    }

    public void setContactHomeTel(String contactHomeTel) {
        this.contactHomeTel = contactHomeTel;
    }

    public String getContactOfficeTel() {
        return contactOfficeTel;
    }

    public void setContactOfficeTel(String contactOfficeTel) {
        this.contactOfficeTel = contactOfficeTel;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public Integer getScaleType() {
        return scaleType;
    }

    public void setScaleType(Integer scaleType) {
        this.scaleType = scaleType;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public Boolean getisEmployee() {
        return isEmployee;
    }

    public void setisEmployee(Boolean employee) {
        isEmployee = employee;
    }

    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(String issueBy) {
        this.issueBy = issueBy;
    }

    public Integer getNumberOfDependent() {
        return numberOfDependent;
    }

    public void setNumberOfDependent(Integer numberOfDependent) {
        this.numberOfDependent = numberOfDependent;
    }

    public BigDecimal getAgreementSalary() {
        return agreementSalary;
    }

    public void setAgreementSalary(BigDecimal agreementSalary) {
        this.agreementSalary = agreementSalary;
    }

    public BigDecimal getInsuranceSalary() {
        return insuranceSalary;
    }

    public void setInsuranceSalary(BigDecimal insuranceSalary) {
        this.insuranceSalary = insuranceSalary;
    }

    public BigDecimal getSalarycoEfficient() {
        return salarycoEfficient;
    }

    public void setSalarycoEfficient(BigDecimal salarycoEfficient) {
        this.salarycoEfficient = salarycoEfficient;
    }

    public Boolean getUnofficialStaff() {
        return isUnofficialStaff;
    }

    public void setUnofficialStaff(Boolean unofficialStaff) {
        isUnofficialStaff = unofficialStaff;
    }

    public BigDecimal getMaximizaDebtAmount() {
        return maximizaDebtAmount;
    }

    public void setMaximizaDebtAmount(BigDecimal maximizaDebtAmount) {
        this.maximizaDebtAmount = maximizaDebtAmount;
    }

    public Integer getDueTime() {
        return dueTime;
    }

    public void setDueTime(Integer dueTime) {
        this.dueTime = dueTime;
    }

    public Boolean getisActive() {
        return isActive;
    }

    public void setisActive(Boolean active) {
        isActive = active;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }
}
