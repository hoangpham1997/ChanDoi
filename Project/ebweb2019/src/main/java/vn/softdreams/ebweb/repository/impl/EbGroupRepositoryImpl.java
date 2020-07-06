package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.repository.EbGroupRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

public class EbGroupRepositoryImpl implements EbGroupRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<EbGroup> findAllByCompanyID(Pageable pageable, UUID companyID) {
        StringBuilder sql = new StringBuilder();
        List<EbGroup> lstEbGr = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" FROM EbGroup WHERE companyID = :companyID ");
        params.put("companyID", companyID);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT *  " + sql.toString() + " order by code", EbGroup.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            lstEbGr = query.getResultList();
        }
        return new PageImpl<>(lstEbGr, pageable, total.longValue());
    }

}
