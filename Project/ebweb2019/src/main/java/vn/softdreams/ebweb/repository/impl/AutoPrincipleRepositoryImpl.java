package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.domain.AutoPrinciple;
import vn.softdreams.ebweb.repository.AutoPrincipleRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class AutoPrincipleRepositoryImpl implements AutoPrincipleRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Page<AutoPrinciple> pageableAllAutoPrinciple(Pageable pageable, UUID org) {
        StringBuilder sql = new StringBuilder();
        List<AutoPrinciple> autoPrinciples = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM AutoPrinciple WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString()
                , AutoPrinciple.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            autoPrinciples = query.getResultList();

        }
        return new PageImpl<>(autoPrinciples, pageable, total.longValue());
    }

}
