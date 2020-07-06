package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import vn.softdreams.ebweb.domain.AccountingObjectBankAccount;
import vn.softdreams.ebweb.repository.AccountingObjectBankAccountRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.AccountingObjectBankAccountDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class AccountingObjectBankAccountRepositoryImpl implements AccountingObjectBankAccountRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<AccountingObjectBankAccountDTO> findAllAOBA() {
        Query query = entityManager.createNativeQuery("SELECt * FROM AccountingObjectBankAccount", "AccountingObjectBankAccountDTO");
        return query.getResultList();
    }

    @Override
    public List<AccountingObjectBankAccountDTO> getByAccountingObjectId(UUID accountingObjectID) {
        Map<String, Object> params = new HashMap<>();
        List<AccountingObjectBankAccountDTO> accountingObjectBankAccountDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("from AccountingObjectBankAccount Where 1 = 1 AND AccountingObjectID = :accountingObjectID");
        params.put("accountingObjectID", accountingObjectID);


        StringBuilder selectSql = new StringBuilder();
        selectSql.append("ID as id, BankAccount as bankAccount, bankName as bankName, BankBranchName as bankBranchName,");
        selectSql.append(" AccountHolderName as accountHolderName, OrderPriority as orderPriority, AccountingObjectID as accountingObjectID ");
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        sql.append(" order by bankAccount ");
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT " + selectSql.toString() + sql.toString(), "AccountingObjectBankAccountDTO");
            Common.setParams(query, params);
            accountingObjectBankAccountDTOS = query.getResultList();
        }
        return accountingObjectBankAccountDTOS;
    }
}
