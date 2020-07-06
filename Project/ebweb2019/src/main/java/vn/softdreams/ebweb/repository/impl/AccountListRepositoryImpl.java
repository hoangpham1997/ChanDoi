package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.StatisticsCode;
import vn.softdreams.ebweb.domain.User;
import vn.softdreams.ebweb.repository.AccountListRepositoryCustom;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.AccountForAccountDefaultDTO;
import vn.softdreams.ebweb.service.dto.AccountListDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.ebweb.service.Utils.Utils.setParams;

public class AccountListRepositoryImpl implements AccountListRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<AccountList> findAll(Pageable pageable, String accountNumber) {
        StringBuilder sql = new StringBuilder();
        List<AccountList> accountLists = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM ACCOUNTLIST WHERE 1 = 1 ");
        if (!Strings.isNullOrEmpty(accountNumber)) {
            sql.append("AND accountNumber = :accountNumber ");
            params.put("accountNumber", accountNumber);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString(), AccountList.class);
            setParamsWithPageable(query, params, pageable, total);
            accountLists = query.getResultList();
        }
        return new PageImpl<>(accountLists, pageable, total.longValue());
    }

    @Override
    public List<AccountListDTO> getAccountType(UUID currentUserLoginAndOrg, Integer typeID, String columnName, Boolean ppType, Boolean checkParent) {
//      Kiểm tra quyền của tài khoản
        List<AccountListDTO> accountLists = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [GetAccountType] @TypeID = :typeID, @columnName = :columnName");
        params.put("typeID", typeID);
        params.put("columnName", columnName);
        if (ppType == null) {
            sql.append(", @ppType = null");
        } else {
            sql.append(", @ppType = :ppType");
            params.put("ppType", ppType);
        }
        sql.append(", @companyID = :companyID");
        sql.append(", @isDependent = :isDependent");
        params.put("companyID", currentUserLoginAndOrg);
        params.put("isDependent", checkParent);
        Query query = entityManager.createNativeQuery(sql.toString(), "AccountListDTO");
        setParams(query, params);
        accountLists = query.getResultList();
        return accountLists;
    }

    @Override
    public List<AccountListDTO> getAccountListByAccountTypeHasRestrictEdit(Integer ppServiceType, String ColumnName, UUID companyID) {
        // find all FilterAccount
        Set<String> filterAccount = new HashSet<>();
        Map<String, Object> paramsfilterAccount = new HashMap<>();
        paramsfilterAccount.put("typeId", ppServiceType);
        paramsfilterAccount.put("columnName", ColumnName);

        Query queryfilterAccount = entityManager.createNativeQuery("select FilterAccount from AccountDefault where TypeID = :typeId and ColumnName = :columnName ");
        setParams(queryfilterAccount, paramsfilterAccount);
        queryfilterAccount.getResultList().forEach(fa -> filterAccount.addAll((Arrays
            .stream(((String) fa).split(";"))
            .collect(Collectors.toList())
        )));

        Map<String, Object> params = new HashMap<>();
        // find account List by accountNumber
        StringBuilder sql = new StringBuilder();
        sql.append("from AccountList where accountNumber in :accountNumber and companyID = :companyID and isActive = 1 ");
        params.put("accountNumber", filterAccount);
        params.put("companyID", companyID);
        StringBuilder selectSql = new StringBuilder();

        selectSql.append("ID as id, ")
            .append(" AccountNumber as accountNumber, ")
            .append(" AccountName as accountName, ")
            .append(" AccountNameGlobal as accountNameGlobal, ")
            .append(" DetailType as detailType ");

        Query query = entityManager.createNativeQuery("SELECT " + selectSql + sql.toString(), "AccountListDTOPPService");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<AccountListDTO> getAllAccountList() {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("from AccountList ");

        StringBuilder selectSql = new StringBuilder();

        selectSql.append("ID as id, ")
            .append(" AccountNumber as accountNumber, ")
            .append(" AccountName as accountName, ")
            .append(" AccountNameGlobal as accountNameGlobal ");

        Query query = entityManager.createNativeQuery("SELECT " + selectSql + sql.toString(), "AccountListDTO");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<AccountListDTO> findByGOtherVoucher(UUID companyID) {
        Map<String, Object> params = new HashMap<>();
        List<AccountListDTO> accountLists = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("from AccountList Where 1 = 1 AND IsParentNode = 0 AND AccountingType = 0 AND CompanyID = :companyID");
        params.put("companyID", companyID);

        StringBuilder selectSql = new StringBuilder();

        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " ORDER BY AccountNumber ", "AccountListDTO");
            setParams(query, params);
            accountLists = query.getResultList();
        }
        return accountLists;
    }


    @Override
    public List<AccountListDTO> findAccountLike133(UUID companyID) {
        Map<String, Object> params = new HashMap<>();
        List<AccountListDTO> accountLists = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("from AccountList Where 1 = 1 AND isActive = 1 AND (AccountNumber Like '133%' OR AccountNumber = '33312')  AND IsParentNode = 0 AND CompanyID = :companyID");
        params.put("companyID", companyID);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString(), "AccountListDTO");
            setParams(query, params);
            accountLists = query.getResultList();
        }
        return accountLists;
    }

    @Override
    public Page<AccountList> findAllByCompanyID(Pageable pageable, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<AccountList> accountLists = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM ACCOUNTLIST WHERE 1 = 1 AND CompanyID = :companyID ");
        params.put("companyID", companyID);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + "ORDER BY AccountNumber ASC ", AccountList.class);
            setParamsWithPageable(query, params, pageable, total);
            accountLists = query.getResultList();
        }
        return new PageImpl<>(accountLists, pageable, total.longValue());
    }


    @Override
    public List<AccountForAccountDefaultDTO> getAccountForAccountDefault(String listFilterAccount, UUID companyID) {
        Map<String, Object> params = new HashMap<>();
        String[] result = listFilterAccount.split(";");
        List<AccountForAccountDefaultDTO> accountForAccountDefaultDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        String selectSql = "SELECT AccountNumber, AccountName ";
//        and Grade > 1 comment bug 3638
        sql.append("From AccountList WHERE 1 = 1 AND CompanyID = :companyID AND IsActive = 1 ");
        params.put("companyID", companyID);
        for (int i = 0; i < result.length; i++) {
            if (result.length > 1) {
                if (i == 0) {
                    sql.append(" AND ( AccountNumber LIKE :result");
                    sql.append(i);
                    params.put("result" + i, result[i] + "%");
                } else if (i == (result.length - 1)) {
                    sql.append(" OR AccountNumber LIKE :result");
                    sql.append(i);
                    params.put("result" + i, result[i] + "%");
                    sql.append(") ");
                } else {
                    sql.append(" OR AccountNumber LIKE :result");
                    sql.append(i);
                    params.put("result" + i, result[i] + "%");
                }
            } else {
                sql.append(" AND AccountNumber LIKE :result");
                sql.append(i);
                params.put("result" + i, result[i] + "%");
                sql.append(" ");
            }
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            sql.append(" GROUP BY AccountNumber, AccountName ");
            Query query = entityManager.createNativeQuery(selectSql + sql.toString() + "ORDER BY AccountNumber", "AccountForAccountDefaultDTO");
            Common.setParams(query, params);
            accountForAccountDefaultDTOS = query.getResultList();
        }
        return accountForAccountDefaultDTOS;
    }

    @Override
    public List<AccountListDTO> findAllByIsActiveForOP( UUID companyID, String currencySearch, Integer typeLedger, UUID companyIdParent) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT al.id, ");
        sqlBuilder.append("al.companyID, ");
        sqlBuilder.append("al.branchId, ");
        sqlBuilder.append("al.accountingType, ");
        sqlBuilder.append("al.accountNumber, ");
        sqlBuilder.append("al.accountName, ");
        sqlBuilder.append("al.accountNameGlobal, ");
        sqlBuilder.append("al.description, ");
        sqlBuilder.append("al.parentAccountID, ");
        sqlBuilder.append("al.isParentNode, ");
        sqlBuilder.append("al.grade, ");
        sqlBuilder.append("al.accountGroupKind, ");
        sqlBuilder.append("al.detailType, ");
        sqlBuilder.append("al.isActive, ");
        sqlBuilder.append("al.detailByAccountObject, ");
        sqlBuilder.append("al.isForeignCurrency, ");
        sqlBuilder.append("oa.id as idOPAccount, ");
        sqlBuilder.append("oa.typeId, ");
        sqlBuilder.append("oa.typeLedger, ");
        sqlBuilder.append("oa.postedDate, ");
        sqlBuilder.append("oa.currencyId, ");
        sqlBuilder.append("oa.exchangeRate, ");
        sqlBuilder.append("oa.debitAmount, ");
        sqlBuilder.append("oa.debitAmountOriginal, ");
        sqlBuilder.append("oa.creditAmount, ");
        sqlBuilder.append("oa.creditAmountOriginal, ");
        sqlBuilder.append("oa.accountingObjectId, ");
        sqlBuilder.append("ao.accountingObjectName, ");
        sqlBuilder.append("ao.accountingObjectCode, ");
        sqlBuilder.append("oa.bankAccountDetailId, ");
        sqlBuilder.append("IIF(bad.bankAccount is null,cc.CreditCardNumber , bad.bankAccount) as bankAccount, ");
        sqlBuilder.append("oa.contractId, ");
        sqlBuilder.append("IIF(oa.TypeLedger = 0, emc.NoFBook, emc.NoMBook) as noBookContract, ");
        sqlBuilder.append("oa.costSetId, ");
        sqlBuilder.append("cs.costSetCode, ");
        sqlBuilder.append("oa.expenseItemId, ");
        sqlBuilder.append("ei.expenseItemCode, ");
        sqlBuilder.append("oa.departmentId, ");
        sqlBuilder.append("ou.organizationUnitCode, ");
        sqlBuilder.append("oa.statisticsCodeId, ");
        sqlBuilder.append("sc.statisticsCode, ");
        sqlBuilder.append("oa.budgetItemId, ");
        sqlBuilder.append("bi.budgetItemCode, ");
        sqlBuilder.append("oa.orderPriority as orderPriorityOPA, ");
        sqlBuilder.append("opm.amountOriginal ");
        sqlBuilder.append("from AccountList al ");
        sqlBuilder.append("left join OPAccount oa on al.AccountNumber = oa.AccountNumber and oa.CompanyID = :companyID and oa.typeLedger = :typeLedger ");
        if (currencySearch != null) {
            sqlBuilder.append("and oa.CurrencyID = :currencyId ");
            params.put("currencyId", currencySearch);
        }
        sqlBuilder.append("left join AccountingObject ao on oa.AccountingObjectID = ao.ID ");
        sqlBuilder.append("left join BankAccountDetail bad on bad.id = oa.BankAccountDetailID ");
        sqlBuilder.append("left join EMContract emc on emc.id = oa.ContractID ");
        sqlBuilder.append("left join CostSet cs on cs.id = oa.CostSetID ");
        sqlBuilder.append("left join ExpenseItem ei on ei.id = oa.ExpenseItemID ");
        sqlBuilder.append("left join EbOrganizationUnit ou on oa.DepartmentID = ou.ID ");
        sqlBuilder.append("left join StatisticsCode sc on sc.id = oa.StatisticsCodeID ");
        sqlBuilder.append("left join BudgetItem bi on oa.BudgetItemID = bi.ID ");
        sqlBuilder.append("left join CreditCard cc on cc.id = oa.BankAccountDetailID ");
        sqlBuilder.append("left join OPMaterialGoods opm on al.AccountNumber = opm.AccountNumber and opm.CompanyID = :companyID and opm.typeLedger = :typeLedger ");
        sqlBuilder.append("where al.CompanyID = :companyIdParent ");
        sqlBuilder.append("and al.IsActive = 1 ");

        sqlBuilder.append("ORDER BY al.AccountNumber ");
        params.put("companyID", companyID);
        params.put("companyIdParent", companyIdParent);
        params.put("typeLedger", typeLedger);
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "AccountListDTOFOROPAccount");
        setParams(query, params);
        return (List<AccountListDTO>) query.getResultList();
    }

    @Override
    public List<AccountListDTO> findAllByIsActiveCustom(UUID companyId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT al.id, ");
        sqlBuilder.append("al.accountNumber, ");
        sqlBuilder.append("al.accountGroupKind, ");
        sqlBuilder.append("al.detailType, ");
        sqlBuilder.append("pr.accountNumber as parentAccountNumber, ");
        sqlBuilder.append("al.isForeignCurrency ");
        sqlBuilder.append("from AccountList al left join AccountList pr on al.parentaccountid = pr.id ");
        sqlBuilder.append("where al.CompanyID = :companyID ");
        sqlBuilder.append("and al.IsActive = 1 ");

        sqlBuilder.append("ORDER BY al.AccountNumber ");
        params.put("companyID", companyId);
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "AccountListDTOOP");
        setParams(query, params);
        return (List<AccountListDTO>) query.getResultList();
    }

    @Override
    public List<AccountList> findAllAccountListSimilarBranch(Boolean similarBranch, UUID orgID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("  select * from AccountList where isActive = 1 ");
//        if (checkShared != null && checkShared) {
//            if (similarBranch != null && similarBranch) {
//                sql.append("  and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND AccType = 0 AND UnitType = 1)) ");
//            } else {
//                sql.append(" and CompanyID in :org ");
//            }
//        } else {
//            sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND UnitType = 1)) ");
//        }
        sql.append(" and CompanyID = :org ");
        params.put("org", orgID);
        sql.append(" order by accountNumber ");
        Query query = entityManager.createNativeQuery(sql.toString(), AccountList.class);
        Common.setParams(query, params);
        return query.getResultList();
    }

    public static void setParamsWithPageable(@NotNull Query query, Map<String, Object> params, @NotNull Pageable pageable, @NotNull Number total) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }

    @Override
    public List<AccountForAccountDefaultDTO> getAllAccountListActiveAndAccountingType(UUID orgID) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT a.AccountNumber, a.AccountName FROM AccountList a " +
            " WHERE a.CompanyID = :companyID AND a.IsActive = 1 AND AccountingType = 0 AND (a.AccountNumber LIKE '136%' OR  a.AccountNumber LIKE '138%'OR  a.AccountNumber LIKE '141%'OR  a.AccountNumber LIKE '244%' " +
            " OR  a.AccountNumber LIKE '334%'OR  a.AccountNumber LIKE '336%'OR  a.AccountNumber LIKE '338%'OR  a.AccountNumber LIKE '344%') " +
            " ORDER BY a.AccountNumber ");

        params.put("companyID", orgID);
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "AccountForAccountDefaultDTO");
        setParams(query, params);
        return (List<AccountForAccountDefaultDTO>) query.getResultList();
    }
}
