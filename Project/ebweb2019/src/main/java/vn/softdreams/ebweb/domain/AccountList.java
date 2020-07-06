package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.AccountForAccountDefaultDTO;
import vn.softdreams.ebweb.service.dto.AccountListDTO;
import vn.softdreams.ebweb.service.dto.AccountingObjectDTO;
import vn.softdreams.ebweb.service.dto.Report.BangCanDoiTaiKhoanDTO;
import vn.softdreams.ebweb.web.rest.dto.BieuDoDoanhThuChiPhiDTO;
import vn.softdreams.ebweb.web.rest.dto.BieuDoTongHopDTO;
import vn.softdreams.ebweb.web.rest.dto.SucKhoeTaiChinhDoanhNghiepDTO;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A AccountList.
 */
@Entity
@Table(name = "accountlist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "AccountListDTO",
        classes = {
            @ConstructorResult(
                targetClass = AccountListDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "companyId", type = UUID.class),
                    @ColumnResult(name = "branchId", type = UUID.class),
                    @ColumnResult(name = "accountingType", type = Integer.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "accountName", type = String.class),
                    @ColumnResult(name = "accountNameGlobal", type = String.class),
                    @ColumnResult(name = "description", type = String.class),
                    @ColumnResult(name = "parentAccountID", type = UUID.class),
                    @ColumnResult(name = "isParentNode", type = Boolean.class),
                    @ColumnResult(name = "grade", type = Integer.class),
                    @ColumnResult(name = "accountGroupKind", type = Integer.class),
                    @ColumnResult(name = "detailType", type = String.class),
                    @ColumnResult(name = "isActive", type = Boolean.class),
                    @ColumnResult(name = "detailByAccountObject", type = Integer.class),
                    @ColumnResult(name = "isForeignCurrency", type = Boolean.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "AccountForAccountDefaultDTO",
        classes = {
            @ConstructorResult(
                targetClass = AccountForAccountDefaultDTO.class,
                columns = {
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "accountName", type = String.class),
                }
            )
        }
    ),
    @SqlResultSetMapping(
        name = "AccountListDTOPPService",
        classes = {
            @ConstructorResult(
                targetClass = AccountListDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "accountNumber", type = String.class),
                    @ColumnResult(name = "accountName", type = String.class),
                    @ColumnResult(name = "accountNameGlobal", type = String.class),
                    @ColumnResult(name = "detailType", type = String.class),
                }
            )
        }
    ), @SqlResultSetMapping(
    name = "AccountListDTOOP",
    classes = {
        @ConstructorResult(
            targetClass = AccountListDTO.class,
            columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "accountNumber", type = String.class),
                @ColumnResult(name = "accountGroupKind", type = Integer.class),
                @ColumnResult(name = "detailType", type = String.class),
                @ColumnResult(name = "parentAccountNumber", type = String.class),
                @ColumnResult(name = "isForeignCurrency", type = Boolean.class),
            }
        )
    }
), @SqlResultSetMapping(
    name = "AccountListDTOFOROPAccount",
    classes = {
        @ConstructorResult(
            targetClass = AccountListDTO.class,
            columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "companyID", type = UUID.class),
                @ColumnResult(name = "branchId", type = UUID.class),
                @ColumnResult(name = "accountingType", type = Integer.class),
                @ColumnResult(name = "accountNumber", type = String.class),
                @ColumnResult(name = "accountName", type = String.class),
                @ColumnResult(name = "accountNameGlobal", type = String.class),
                @ColumnResult(name = "description", type = String.class),
                @ColumnResult(name = "parentAccountID", type = UUID.class),
                @ColumnResult(name = "isParentNode", type = Boolean.class),
                @ColumnResult(name = "grade", type = Integer.class),
                @ColumnResult(name = "accountGroupKind", type = Integer.class),
                @ColumnResult(name = "detailType", type = String.class),
                @ColumnResult(name = "isActive", type = Boolean.class),
                @ColumnResult(name = "detailByAccountObject", type = Integer.class),
                @ColumnResult(name = "isForeignCurrency", type = Boolean.class),
                @ColumnResult(name = "idOPAccount", type = UUID.class),
                @ColumnResult(name = "typeId", type = Integer.class),
                @ColumnResult(name = "postedDate", type = LocalDate.class),
                @ColumnResult(name = "typeLedger", type = Integer.class),
                @ColumnResult(name = "currencyId", type = String.class),
                @ColumnResult(name = "exchangeRate", type = BigDecimal.class),
                @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                @ColumnResult(name = "debitAmountOriginal", type = BigDecimal.class),
                @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                @ColumnResult(name = "creditAmountOriginal", type = BigDecimal.class),
                @ColumnResult(name = "accountingObjectId", type = UUID.class),
                @ColumnResult(name = "accountingObjectName", type = String.class),
                @ColumnResult(name = "accountingObjectCode", type = String.class),
                @ColumnResult(name = "bankAccountDetailId", type = UUID.class),
                @ColumnResult(name = "bankAccount", type = String.class),
                @ColumnResult(name = "contractId", type = UUID.class),
                @ColumnResult(name = "noBookContract", type = String.class),
                @ColumnResult(name = "costSetId", type = UUID.class),
                @ColumnResult(name = "costSetCode", type = String.class),
                @ColumnResult(name = "expenseItemId", type = UUID.class),
                @ColumnResult(name = "expenseItemCode", type = String.class),
                @ColumnResult(name = "departmentId", type = UUID.class),
                @ColumnResult(name = "organizationUnitCode", type = String.class),
                @ColumnResult(name = "statisticsCodeId", type = UUID.class),
                @ColumnResult(name = "statisticsCode", type = String.class),
                @ColumnResult(name = "budgetItemId", type = UUID.class),
                @ColumnResult(name = "budgetItemCode", type = String.class),
                @ColumnResult(name = "orderPriorityOPA", type = Integer.class),
                @ColumnResult(name = "amountOriginal", type = BigDecimal.class)
            }
        )
    }
), @SqlResultSetMapping(
    name = "BangCanDoiTaiKhoanDTO",
    classes = {
        @ConstructorResult(
            targetClass = BangCanDoiTaiKhoanDTO.class,
            columns = {
                @ColumnResult(name = "accountID", type = UUID.class),
                @ColumnResult(name = "accountNumber", type = String.class),
                @ColumnResult(name = "accountName", type = String.class),
                @ColumnResult(name = "accountCategoryKind", type = Integer.class),
                @ColumnResult(name = "openingDebitAmount", type = BigDecimal.class),
                @ColumnResult(name = "openingCreditAmount", type = BigDecimal.class),
                @ColumnResult(name = "debitAmount", type = BigDecimal.class),
                @ColumnResult(name = "creditAmount", type = BigDecimal.class),
                @ColumnResult(name = "debitAmountAccum", type = BigDecimal.class),
                @ColumnResult(name = "creditAmountAccum", type = BigDecimal.class),
                @ColumnResult(name = "closingDebitAmount", type = BigDecimal.class),
                @ColumnResult(name = "closingCreditAmount", type = BigDecimal.class),
                @ColumnResult(name = "grade", type = Integer.class),
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "parentAccountID", type = UUID.class),
                @ColumnResult(name = "isParentNode", type = Boolean.class),
            }
        )
    }
), @SqlResultSetMapping(
    name = "SucKhoeTaiChinhDoanhNghiepDTO",
    classes = {
        @ConstructorResult(
            targetClass = SucKhoeTaiChinhDoanhNghiepDTO.class,
            columns = {
                @ColumnResult(name = "chiSo", type = String.class),
                @ColumnResult(name = "giaTri", type = BigDecimal.class),
            }
        )
    }
), @SqlResultSetMapping(
    name = "BieuDoTongHopDTO",
    classes = {
        @ConstructorResult(
            targetClass = BieuDoTongHopDTO.class,
            columns = {
                @ColumnResult(name = "itemName", type = String.class),
                @ColumnResult(name = "amount", type = BigDecimal.class),
                @ColumnResult(name = "prevAmount", type = BigDecimal.class),
            }
        )
    }
), @SqlResultSetMapping(
    name = "BieuDoDoanhThuChiPhiDTO",
    classes = {
        @ConstructorResult(
            targetClass = BieuDoDoanhThuChiPhiDTO.class,
            columns = {
                @ColumnResult(name = "fromDate", type = LocalDate.class),
                @ColumnResult(name = "toDate", type = LocalDate.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "amount", type = BigDecimal.class),
            }
        )
    }
), @SqlResultSetMapping(
    name = "BieuDoNoPhaiThuPhaiTraDTO",
    classes = {
        @ConstructorResult(
            targetClass = BieuDoDoanhThuChiPhiDTO.class,
            columns = {
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "amount", type = BigDecimal.class),
            }
        )
    }
)

})
public class AccountList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "branchid")
    private UUID branchID;

    @Column(name = "accountingtype")
    private Integer accountingType;

    @Size(max = 25)
    @Column(name = "accountnumber", length = 25)
    private String accountNumber;

    @Size(max = 512)
    @Column(name = "accountname", length = 512)
    private String accountName;

    @Size(max = 512)
    @Column(name = "accountnameglobal", length = 512)
    private String accountNameGlobal;

    @Size(max = 512)
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "isparentnode")
    private Boolean isParentNode;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "accountgroupkind")
    private Integer accountGroupKind;

    @Column(name = "detailtype", length = 50)
    private String detailType;

    @Column(name = "isactive")
    private Boolean isActive;

    @Column(name = "detailbyaccountobject")
    private Integer detailByAccountObject;

    @Column(name = "isforeigncurrency")
    private Boolean isForeignCurrency;

    @Column(name = "parentaccountid")
    private UUID parentAccountID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private String parentAccountNumber;

    public String getParentAccountNumber() {
        return parentAccountNumber;
    }

    public void setParentAccountNumber(String parentAccountNumber) {
        this.parentAccountNumber = parentAccountNumber;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountList accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public AccountList accountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNameGlobal() {
        return accountNameGlobal;
    }

    public AccountList accountNameGlobal(String accountNameGlobal) {
        this.accountNameGlobal = accountNameGlobal;
        return this;
    }

    public void setAccountNameGlobal(String accountNameGlobal) {
        this.accountNameGlobal = accountNameGlobal;
    }

    public String getDescription() {
        return description;
    }

    public AccountList description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsParentNode() {
        return isParentNode;
    }

    public AccountList isParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
        return this;
    }

    public void setIsParentNode(Boolean isParentNode) {
        this.isParentNode = isParentNode;
    }

    public Integer getGrade() {
        return grade;
    }

    public AccountList grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getAccountGroupKind() {
        return accountGroupKind;
    }

    public AccountList accountGroupKind(Integer accountGroupKind) {
        this.accountGroupKind = accountGroupKind;
        return this;
    }

    public void setAccountGroupKind(Integer accountGroupKind) {
        this.accountGroupKind = accountGroupKind;
    }

    public String getDetailType() {
        return detailType;
    }

    public AccountList detailType(String detailType) {
        this.detailType = detailType;
        return this;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public Integer getDetailByAccountObject() {
        return detailByAccountObject;
    }

    public AccountList detailByAccountObject(Integer detailByAccountObject) {
        this.detailByAccountObject = detailByAccountObject;
        return this;
    }

    public void setDetailByAccountObject(Integer detailByAccountObject) {
        this.detailByAccountObject = detailByAccountObject;
    }

    public UUID getParentAccountID() {
        return parentAccountID;
    }

    public AccountList parentAccountID(UUID parentAccountID) {
        this.parentAccountID = parentAccountID;
        return this;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Boolean getParentNode() {
        return isParentNode;
    }

    public void setParentNode(Boolean parentNode) {
        isParentNode = parentNode;
    }

    public Integer getAccountingType() {
        return accountingType;
    }

    public void setAccountingType(Integer accountingType) {
        this.accountingType = accountingType;
    }

    public Boolean getIsForeignCurrency() {
        return isForeignCurrency != null ? isForeignCurrency : false;
    }

    public void setIsForeignCurrency(Boolean foreignCurrency) {
        this.isForeignCurrency = foreignCurrency;
    }

    public void setParentAccountID(UUID parentAccountID) {
        this.parentAccountID = parentAccountID;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
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
        AccountList accountList = (AccountList) o;
        if (accountList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountList{" +
            "id=" + getId() +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", accountName='" + getAccountName() + "'" +
            ", accountNameGlobal='" + getAccountNameGlobal() + "'" +
            ", description='" + getDescription() + "'" +
            ", isParentNode='" + isIsParentNode() + "'" +
            ", grade=" + getGrade() +
            ", accountGroupKind=" + getAccountGroupKind() +
            ", detailType=" + getDetailType() +
            ", detailByAccountObject=" + getDetailByAccountObject() +
            ", parentAccountID='" + getParentAccountID() + "'" +
            "}";
    }
}
