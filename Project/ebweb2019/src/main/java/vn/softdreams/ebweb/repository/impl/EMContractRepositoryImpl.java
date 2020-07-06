package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.CreditCard;
import vn.softdreams.ebweb.domain.EMContract;
import vn.softdreams.ebweb.repository.EMContractRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class EMContractRepositoryImpl implements EMContractRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<EMContract> findAllByIsActive(UUID companyID, Boolean isNoMBook) {
        StringBuilder sql = new StringBuilder();
        List<EMContract> emContractList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM EMContract WHERE 1 = 1");
        if (!Strings.isNullOrEmpty(String.valueOf(companyID))) {
            sql.append(" AND CompanyID = :companyID ");
            params.put("companyID", companyID);
        }
        sql.append("AND IsActive = 1 ");
        if (Boolean.TRUE.equals(isNoMBook)) {
            sql.append(" AND TypeLedger IN (1,2) ");
        } else {
            sql.append(" AND TypeLedger IN (0,2) ");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            String newSql = "SELECT * " + sql.toString();
            if (Boolean.TRUE.equals(isNoMBook)) {
                newSql += " ORDER BY NOMBook ";
            } else {
                newSql += " ORDER BY NOFBook ";
            }
            Query query = entityManager.createNativeQuery(newSql, EMContract.class);
            Common.setParams(query, params);
            emContractList = query.getResultList();
        }
        return emContractList;
    }

    @Override
    public List<EMContract> findAllForReport(List<UUID> listOrgID, boolean currentBook) {
        StringBuilder sql = new StringBuilder();
        List<EMContract> emContractList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM EMContract WHERE 1 = 1");
        sql.append(" AND CompanyID IN :listOrgID ");
        params.put("listOrgID", listOrgID);
        sql.append("AND IsActive = 1 ");
        if (Boolean.TRUE.equals(currentBook)) {
            sql.append(" AND TypeLedger IN (1,2) ");
        } else {
            sql.append(" AND TypeLedger IN (0,2) ");
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            String newSql = "SELECT * " + sql.toString();
            if (Boolean.TRUE.equals(currentBook)) {
                newSql += " ORDER BY NOMBook ";
            } else {
                newSql += " ORDER BY NOFBook ";
            }
            Query query = entityManager.createNativeQuery(newSql, EMContract.class);
            Common.setParams(query, params);
            emContractList = query.getResultList();
        }
        return emContractList;
    }
}
