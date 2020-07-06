package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.ExpenseItem;
import vn.softdreams.ebweb.repository.AccountListRepositoryCustom;
import vn.softdreams.ebweb.repository.ExpenseItemRepositoryCustom;
import vn.softdreams.ebweb.service.dto.AccountForAccountDefaultDTO;
import vn.softdreams.ebweb.service.dto.AccountListDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseItemRepositoryImpl implements ExpenseItemRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;



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
    public List<ExpenseItem> findAllExpenseItemSimilarBranch(Boolean similarBranch, List<UUID> listID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("  select * from ExpenseItem where isActive = 1 ");
//        if (checkShared != null && checkShared) {
//            if (similarBranch != null && similarBranch) {
//                sql.append("  and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND AccType = 0 AND UnitType = 1)) ");
//            } else {
//                sql.append(" and CompanyID in :org ");
//            }
//        } else {
//            sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND UnitType = 1)) ");
//        }
        sql.append(" and CompanyID in :org ");
        params.put("org", listID);
        sql.append(" order by ExpenseItemCode ");
        Query query = entityManager.createNativeQuery(sql.toString(), ExpenseItem.class);
        Common.setParams(query, params);
        return query.getResultList();
    }
}
