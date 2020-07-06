package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.MaterialGoodsCategory;
import vn.softdreams.ebweb.domain.StatisticsCode;
import vn.softdreams.ebweb.repository.StatisticsCodeRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class StatisticsCodeRepositoryCustomImpl implements StatisticsCodeRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<StatisticsCode> getAllStatisticsCodeByCompanyIDAndSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("  select * from StatisticsCode where isActive = 1 ");
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
        params.put("org", allCompanyByCompanyIdAndCode);
        sql.append(" order by statisticsCode ");
        Query query = entityManager.createNativeQuery(sql.toString(), StatisticsCode.class);
        Common.setParams(query, params);
        return query.getResultList();
    }
}
