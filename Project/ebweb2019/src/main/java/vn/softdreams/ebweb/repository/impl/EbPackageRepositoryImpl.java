package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.domain.EbPackage;
import vn.softdreams.ebweb.repository.EbGroupRepositoryCustom;
import vn.softdreams.ebweb.repository.EbPackageRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class EbPackageRepositoryImpl implements EbPackageRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<EbPackage> findAllEbPackage(Pageable pageable) {
        StringBuilder sql = new StringBuilder();
        List<EbPackage> lstResult = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" FROM EbPackage ");
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(*) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT *  " + sql.toString() + " order by packageCode", EbPackage.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            lstResult = query.getResultList();
        }
        return new PageImpl<>(lstResult, pageable, total.longValue());
    }
}
