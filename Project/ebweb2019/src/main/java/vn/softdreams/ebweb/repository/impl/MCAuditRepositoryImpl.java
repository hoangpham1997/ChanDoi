package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.softdreams.ebweb.domain.*;

import vn.softdreams.ebweb.repository.MCAuditRepositoryCustom;
import vn.softdreams.ebweb.service.dto.cashandbank.MCAuditDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCPaymentDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceViewDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class MCAuditRepositoryImpl implements MCAuditRepositoryCustom {
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

    /**
     * Chuongnv
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<MCAuditDTO> findAllDTOByCompanyID(Pageable pageable, UUID companyID, int displayOnBook) {
        StringBuilder sql = new StringBuilder();
        List<MCAuditDTO> lst = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM MCAudit Where CompanyID = :companyID ");
        params.put("companyID", companyID);
        if (displayOnBook == 0) {
            sql.append("and (TypeLedger = 0 or TypeLedger = 2)");
        } else if (displayOnBook == 1) {
            sql.append("and (TypeLedger = 1 or TypeLedger = 2)");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by Date DESC ,AuditDate DESC ,No DESC ", "MCAuditDTO");
            setParamsWithPageable(query, params, pageable, total);
            lst = query.getResultList();
        }
        return new PageImpl<>(((List<MCAuditDTO>) lst), pageable, total.longValue());
    }

    @Override
    public Page<MCAuditDTO> searchMCAudit(Pageable pageable, String currencyID, String fromDate, String toDate, String textSearch, UUID org, String currentBook, boolean statusExport) {
        Map<String, Object> params = new HashMap<>();
        List<MCAuditDTO> mcAuditDTOS = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" FROM MCAudit pp where pp.CompanyID = :companyID ");
        params.put("companyID", org);
        sqlBuilder.append("  and ( pp.TypeLedger = 2 or pp.TypeLedger = :typeLedger) ");
        params.put("typeLedger", currentBook);

        if (!Strings.isNullOrEmpty(currencyID)) {
            sqlBuilder.append(" and pp.CurrencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        if (fromDate != null && !Strings.isNullOrEmpty(fromDate)) {
            sqlBuilder.append(" and pp.Date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (toDate != null && !Strings.isNullOrEmpty(toDate)) {
            sqlBuilder.append(" and pp.Date <= :toDate ");
            params.put("toDate", toDate);
        }
        if (!Strings.isNullOrEmpty(textSearch)) {
            sqlBuilder.append("AND (No LIKE :keySearch ");
            sqlBuilder.append("OR Description LIKE :keySearch ");
            sqlBuilder.append("OR Summary LIKE :keySearch )");
            params.put("keySearch", "%" + textSearch + "%");
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sqlBuilder.toString() + " order by pp.Date desc, pp.AuditDate desc , pp.No desc ", "MCAuditDTO");
            if (statusExport) {
                Common.setParams(query, params);
            } else {
                Common.setParamsWithPageable(query, params, pageable, total);
            }
            mcAuditDTOS = query.getResultList();
        }
        return new PageImpl<>(mcAuditDTOS, pageable, total.longValue());
    }

    @Override
    public MCAudit findByRowNum(String currencyID, String fromDate, String toDate, String keySearch, Integer rowNum, UUID org, String currentBook) {
        Map<String, Object> params = new HashMap<>();
        MCAudit mcAudit = null;
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" FROM MCAudit pp where pp.CompanyID = :companyID ");
        params.put("companyID", org);
        sqlBuilder.append("  and ( pp.TypeLedger = 2 or pp.TypeLedger = :typeLedger) ");
        params.put("typeLedger", currentBook);

        if (!Strings.isNullOrEmpty(currencyID)) {
            sqlBuilder.append(" and pp.CurrencyID = :currencyID ");
            params.put("currencyID", currencyID);
        }
        if (fromDate != null || toDate != null) {
            Common.addDateSearchCustom(fromDate, toDate, params, sqlBuilder, "pp.Date", "Date");
        }
        if (!Strings.isNullOrEmpty(keySearch)) {
            sqlBuilder.append("AND (No LIKE :keySearch ");
            sqlBuilder.append("OR Description LIKE :keySearch ");
            sqlBuilder.append("OR Summary LIKE :keySearch )");
            params.put("keySearch", "%" + keySearch + "%");
        }

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            String newSql = "SELECT * FROM (SELECT *, ROW_NUMBER() over (order by pp.Date desc, pp.AuditDate desc, pp.No desc) rownum "
                + sqlBuilder.toString() + ") a where a.rownum = :rowNum";
            params.put("rowNum", rowNum);
            Query query = entityManager.createNativeQuery(newSql, MCAudit.class);
            Common.setParams(query, params);
            mcAudit = (MCAudit) query.getSingleResult();
        }
        return mcAudit;
    }

    @Override
    public List<MCAuditDetails> findDetailByMCAuditID(UUID mCAuditID) {
        Map<String, Object> params = new HashMap<>();
        List<MCAuditDetails> mcAuditDetails = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" FROM MCAuditDetail pp where pp.MCAuditID = :mcAuditID ");
        params.put("mcAuditID", mCAuditID);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sqlBuilder.toString() + " order by pp.OrderPriority  ", MCAuditDetails.class);
            Common.setParams(query, params);
            mcAuditDetails = query.getResultList();
        }
        return mcAuditDetails;
    }

    @Override
    public List<MCAuditDetailMember> findDetailMemberByMCAuditID(UUID mCAuditID) {
        Map<String, Object> params = new HashMap<>();
        List<MCAuditDetailMember> mcAuditDetailMembers = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" FROM MCAuditDetailMember pp where pp.MCAuditID = :mcAuditID ");
        params.put("mcAuditID", mCAuditID);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlBuilder.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sqlBuilder.toString() + " order by pp.OrderPriority  ", MCAuditDetailMember.class);
            Common.setParams(query, params);
            mcAuditDetailMembers = query.getResultList();
        }
        return mcAuditDetailMembers;
    }

    @Override
    public void multiDeleteMCAudit(UUID org, List<UUID> uuidListMCAudit) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM MCAudit WHERE companyID = :orgID ");
        params.put("orgID", org);
        sql.append("AND ID IN :uuidList ");
        params.put("uuidList", uuidListMCAudit);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public void multiDeleteMCAuditChild(String mbDepositDetail, List<UUID> uuidListMCAudit) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String deleteSQL = "DELETE FROM " + mbDepositDetail + " ";
        sql.append(" WHERE MCAuditID IN :uuidList ");
        params.put("uuidList", uuidListMCAudit);
        Query query = entityManager.createNativeQuery(deleteSQL + sql.toString());
        setParams(query, params);
        query.executeUpdate();
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

}
