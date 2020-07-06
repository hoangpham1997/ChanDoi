package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.softdreams.ebweb.domain.AccountingObjectGroup;
import vn.softdreams.ebweb.domain.MaterialGoodsCategory;
import vn.softdreams.ebweb.domain.User;
import vn.softdreams.ebweb.repository.AccountingObjectGroupRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class AccountingObjectGroupRepositoryImpl implements AccountingObjectGroupRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<AccountingObjectGroup> findAll(Pageable pageable, String accountingobjectgroupcode) {
        StringBuilder sql = new StringBuilder();
        List<AccountingObjectGroup> units = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AccountingObjectGroup WHERE 1 = 1 ");
        if (!Strings.isNullOrEmpty(accountingobjectgroupcode)) {
            sql.append("AND accountingobjectgroupcode  like :accountingobjectgroupcode ");
            params.put("accountingobjectgroupcode", "%" + accountingobjectgroupcode + "%");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + addSort(pageable.getSort()), AccountingObjectGroup.class);
            setParamsWithPageable(query, params, pageable, total);
            units = query.getResultList();
        }
        return new PageImpl<>(units, pageable, total.longValue());
    }

    @Override
    public List<AccountingObjectGroup> getAllAccountingObjectGroupSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("  select * from AccountingObjectGroup where IsActive = 1 ");
        if (checkShared != null && checkShared) {
            if (similarBranch != null && similarBranch) {
                sql.append("  and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND AccType = 0 AND UnitType = 1)) ");

            } else {
                sql.append(" and CompanyID in :org ");
            }
        } else {
            sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND UnitType = 1)) ");
        }
        params.put("org", allCompanyByCompanyIdAndCode);
        Query query = entityManager.createNativeQuery(sql.toString(), AccountingObjectGroup.class);

        sql.append(" order by AccountingObjectGroupCode ");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<AccountingObjectGroup> findAllAccountingObjectGroupSimilar(List<UUID> org, Boolean similarBranch, Boolean checkShared) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("  from AccountingObjectGroup b where IsActive = 1 ");
        if (checkShared != null && checkShared) {
            if (similarBranch != null && similarBranch) {
                sql.append("  and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND AccType = 0 AND UnitType = 1)) ");

            } else {
                sql.append(" and CompanyID in :org ");
            }
        } else {
            sql.append(" and CompanyID IN (SELECT ID FROM EbOrganizationUnit WHERE (ID in :org) OR (ParentID in :org AND UnitType = 1)) ");
        }
        params.put("org", org);
//        if (id != null) {
//            sql.append(" and AccountObjectGroupID = :id");
//            params.put("id", id);
//        }
        sql.append(" order by AccountingObjectGroupCode ");
        String selectQuery = "SELECT * ";
        Query query = entityManager.createNativeQuery(selectQuery + sql.toString(), AccountingObjectGroup.class);
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

    /**
     * @param sort
     * @return
     */
    public static String addSort(Sort sort) {
        StringBuilder orderSql = new StringBuilder();
        if (sort == null)
            return "";
        for (Sort.Order order : sort) {

            orderSql.append("ORDER BY ");
            orderSql.append(order.getProperty());
            orderSql.append(" ");
            orderSql.append(order.getDirection());
            break;
        }
        return orderSql.toString();
    }

    /**
     * @Author phuonghv
     */
    public static String addMultiSort(Sort sort) {
        StringBuilder orderSql = new StringBuilder();
        if (sort == null) {
            return "";
        }
        orderSql.append("ORDER BY ");
        int i = 0;
        for (Sort.Order order : sort) {
            if (i > 0) {
                orderSql.append(", ");
            }
            orderSql.append(order.getProperty());
            orderSql.append(" ");
            orderSql.append(order.getDirection());
            i++;
        }
        return orderSql.toString();
    }

}
