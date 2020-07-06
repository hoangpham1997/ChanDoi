package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.AccountingObjectDTO;
import vn.softdreams.ebweb.service.dto.VoucherRefCatalogDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * A AccountingObject.
 */
@Entity
@Table(name = "accountingobject")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "AccountingObjectDTO",
        classes = {
            @ConstructorResult(
                targetClass = AccountingObjectDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "branchId", type = UUID.class),
                    @ColumnResult(name = "companyId", type = UUID.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "employeeBirthday", type = LocalDate.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "tel", type = String.class),
                    @ColumnResult(name = "fax", type = String.class),
                    @ColumnResult(name = "email", type = String.class),
                    @ColumnResult(name = "website", type = String.class),
                    @ColumnResult(name = "bankName", type = String.class),
                    @ColumnResult(name = "taxCode", type = String.class),
                    @ColumnResult(name = "isEmployee", type = Boolean.class),
                    @ColumnResult(name = "isActive", type = Boolean.class),
                    @ColumnResult(name = "objectType", type = Integer.class),
                    @ColumnResult(name = "contactName", type = String.class),
                    @ColumnResult(name = "identificationNo", type = String.class),
                    @ColumnResult(name = "issueDate", type = LocalDate.class),
                    @ColumnResult(name = "issueBy", type = String.class),
                    @ColumnResult(name = "departmentId", type = UUID.class),
                    @ColumnResult(name = "contactTitle", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "AccountingObjectPPServiceDTO",
        classes = {
            @ConstructorResult(
                targetClass = AccountingObjectDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "branchId", type = UUID.class),
                    @ColumnResult(name = "companyId", type = UUID.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "contactName", type = String.class),
                    @ColumnResult(name = "employeeBirthday", type = LocalDate.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class),
                    @ColumnResult(name = "tel", type = String.class),
                    @ColumnResult(name = "fax", type = String.class),
                    @ColumnResult(name = "email", type = String.class),
                    @ColumnResult(name = "website", type = String.class),
                    @ColumnResult(name = "bankName", type = String.class),
                    @ColumnResult(name = "taxCode", type = String.class),
                    @ColumnResult(name = "isEmployee", type = Boolean.class),
                    @ColumnResult(name = "isActive", type = Boolean.class),
                    @ColumnResult(name = "objectType", type = Integer.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "AccountingObjectCbbDTO",
        classes = {
            @ConstructorResult(
                targetClass = AccountingObjectDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class)
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "AccountingObjectCustom",
        classes = {
            @ConstructorResult(
                targetClass = AccountingObjectDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "accountingObjectCode", type = String.class),
                    @ColumnResult(name = "accountingObjectName", type = String.class),
                    @ColumnResult(name = "accountingObjectAddress", type = String.class)
                }
            )
        }
    )})

public class AccountingObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "branchid")
    private UUID branchId;

    @Column(name = "companyid")
    private UUID companyId;

    @NotNull
    @Size(max = 25)
    @Column(name = "accountingobjectcode", length = 25, nullable = false)
    private String accountingObjectCode;

    @NotNull
    @Size(max = 512)
    @Column(name = "accountingobjectname", length = 512, nullable = false)
    private String accountingObjectName;

    @Column(name = "employeebirthday")
    private LocalDate employeeBirthday;

    @Size(max = 512)
    @Column(name = "accountingobjectaddress", length = 512)
    private String accountingObjectAddress;

    @Size(max = 25)
    @Column(name = "tel", length = 25)
    private String tel;

    @Size(max = 25)
    @Column(name = "fax", length = 25)
    private String fax;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 100)
    @Column(name = "website", length = 100)
    private String website;

    @Size(max = 512)
    @Column(name = "bankname", length = 512)
    private String bankName;

    @Size(max = 50)
    @Column(name = "taxcode", length = 50)
    private String taxCode;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Size(max = 512)
    @Column(name = "contactname", length = 512)
    private String contactName;

    @Size(max = 512)
    @Column(name = "contacttitle", length = 512)
    private String contactTitle;

    @Column(name = "contactsex")
    private Integer contactSex;

    @Size(max = 25)
    @Column(name = "contactmobile", length = 25)
    private String contactMobile;

    @Size(max = 100)
    @Column(name = "contactemail", length = 100)
    private String contactEmail;

    @Size(max = 25)
    @Column(name = "contacthometel", length = 25)
    private String contactHomeTel;

    @Size(max = 25)
    @Column(name = "contactofficetel", length = 25)
    private String contactOfficeTel;

    @Size(max = 512)
    @Column(name = "contactaddress", length = 512)
    private String contactAddress;

    @Column(name = "scaletype")
    private Integer scaleType;

    @Column(name = "objecttype")
    private Integer objectType;

    @NotNull
    @Column(name = "isemployee", nullable = false)
    private Boolean isEmployee;

    @Size(max = 25)
    @Column(name = "identificationno", length = 25)
    private String identificationNo;

    @Column(name = "issuedate")
    private LocalDate issueDate;

    @Size(max = 512)
    @Column(name = "issueby", length = 512)
    private String issueBy;

    @Column(name = "numberofdependent")
    private Integer numberOfDependent;

    @Column(name = "agreementsalary", precision = 10, scale = 2)
    private BigDecimal agreementSalary;

    @Column(name = "insurancesalary", precision = 10, scale = 2)
    private BigDecimal insuranceSalary;

    @Column(name = "salarycoefficient", precision = 10, scale = 2)
    private BigDecimal salarycoEfficient;

    @Column(name = "isUnofficialStaff")
    private Boolean isUnofficialStaff;

    @Column(name = "maximizadebtamount", precision = 10, scale = 2)
    private BigDecimal maximizaDebtAmount;

    @Column(name = "duetime")
    private Integer dueTime;

    @Column(name = "isactive")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "paymentclauseid")
    private PaymentClause paymentClause;

    @ManyToOne
    @JsonIgnoreProperties("")
    @JoinColumn(name = "accountobjectgroupid")
    private AccountingObjectGroup accountingObjectGroup;

    @Column(name = "departmentid")
    private UUID departmentId;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "accountingobjectid")
    private Set<AccountingObjectBankAccount> accountingObjectBankAccounts = new HashSet<>();

    @Transient
    @JsonSerialize
    private List<VoucherRefCatalogDTO> voucherRefCatalogDTOS;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public AccountingObject tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public AccountingObject fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public AccountingObject email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public AccountingObject website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


    public String getDescription() {
        return description;
    }

    public AccountingObject description(String description) {
        this.description = description;
        return this;
    }

    public PaymentClause getPaymentClause() {
        return paymentClause;
    }

    public AccountingObject paymentClause(PaymentClause paymentClause) {
        this.paymentClause = paymentClause;
        return this;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public void setPaymentClause(PaymentClause paymentClause) {
        this.paymentClause = paymentClause;
    }

    public AccountingObjectGroup getAccountingObjectGroup() {
        return accountingObjectGroup;
    }

    public AccountingObject accountingObjectGroup(AccountingObjectGroup accountingObjectGroup) {
        this.accountingObjectGroup = accountingObjectGroup;
        return this;
    }

    public void setAccountingObjectGroup(AccountingObjectGroup accountingObjectGroup) {
        this.accountingObjectGroup = accountingObjectGroup;
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
        AccountingObject accountingObject = (AccountingObject) o;
        if (accountingObject.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountingObject.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountingObject{" +
            "id=" + getId() +
            "}";
    }

    public Set<AccountingObjectBankAccount> getAccountingObjectBankAccounts() {
        return accountingObjectBankAccounts;
    }

    public void setAccountingObjectBankAccounts(Set<AccountingObjectBankAccount> accountingObjectBankAccounts) {
        this.accountingObjectBankAccounts = accountingObjectBankAccounts;
    }

    public void addAccountingObjectBankAccounts(AccountingObjectBankAccount accountingObjectBankAccount) {
        if (accountingObjectBankAccounts == null) {
            accountingObjectBankAccounts = new HashSet<>();
        }
        accountingObjectBankAccounts.add(accountingObjectBankAccount);
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

    public Boolean isIsEmployee() {
        return isEmployee;
    }

    public AccountingObject isEmployee(Boolean isEmployee) {
        this.isEmployee = isEmployee;
        return this;
    }

    public Boolean getisEmployee() {
        return isEmployee;
    }

    public void setisEmployee(Boolean employee) {
        isEmployee = employee;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public AccountingObject isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsUnOfficialStaff() {
        return isUnofficialStaff;
    }

    public void setIsUnOfficialStaff(Boolean unofficialStaff) {
        isUnofficialStaff = unofficialStaff;
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

    public List<VoucherRefCatalogDTO> getVoucherRefCatalogDTOS() {
        return voucherRefCatalogDTOS;
    }

    public void setVoucherRefCatalogDTOS(List<VoucherRefCatalogDTO> voucherRefCatalogDTOS) {
        this.voucherRefCatalogDTOS = voucherRefCatalogDTOS;
    }
}
