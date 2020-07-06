package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountDefault;
import vn.softdreams.ebweb.domain.Bank;
import vn.softdreams.ebweb.repository.AccountDefaultRepositoryCustom;
import vn.softdreams.ebweb.repository.BankRepositoryCustom;
import vn.softdreams.ebweb.service.dto.AccountDefaultDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class AccountDefaultRepositoryImpl implements AccountDefaultRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<AccountDefaultDTO> findAllForAccountDefaultCategory(UUID companyID) {
        Map<String, Object> params = new HashMap<>();
        List<AccountDefaultDTO> accountDefaultDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" [getDataForAccountDefaultCategory] :companyID ");
        params.put("companyID", companyID);
        Query query = entityManager.createNativeQuery(sql.toString(), "AccountDefaultDTO");
        Common.setParams(query, params);
        accountDefaultDTOS = query.getResultList();
        return accountDefaultDTOS;
    }
}
