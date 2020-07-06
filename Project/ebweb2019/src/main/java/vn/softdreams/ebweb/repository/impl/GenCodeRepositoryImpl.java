package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.GenCode;
import vn.softdreams.ebweb.domain.GeneralLedger;
import vn.softdreams.ebweb.repository.GenCodeRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.*;

public class GenCodeRepositoryImpl implements GenCodeRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public GenCode findWithTypeID(int typeGroupID, UUID companyID, UUID branchID, int displayOnBook) {
        StringBuilder sql = new StringBuilder();
        GenCode genCode = new GenCode();
        Map<String, Object> params = new HashMap<>();
        sql.append("from GenCode where 1 = 1 ");
        sql.append("And TypeGroupID = :typeGroupID ");
        params.put("typeGroupID", typeGroupID);
        sql.append("And CompanyID = :companyID ");
        params.put("companyID", companyID);
        sql.append("And DisplayOnBook = :displayOnBook ");
        params.put("displayOnBook", displayOnBook);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString(), GenCode.class);
            setParams(query, params);
            genCode = GenCode.class.cast(query.getSingleResult());
        } else {
            return null;
        }
        return genCode;
    }

    @Override
    public List<GenCode> getAllGenCodeForSystemOption(UUID companyID, String currentBook) {
        StringBuilder sql = new StringBuilder();
        List<GenCode> genCode = new ArrayList<GenCode>();
        Map<String, Object> params = new HashMap<>();
        sql.append("from GenCode where 1 = 1 ");
        sql.append(" AND (DisplayOnBook = :displayOnBook OR DisplayOnBook = 2) ");
        params.put("displayOnBook", currentBook);
        sql.append("And CompanyID = :companyID ");
        params.put("companyID", companyID);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " ORDER BY TypeGroupID ", GenCode.class);
            setParams(query, params);
            genCode = query.getResultList();
        } else {
            return null;
        }
        return genCode;
    }

    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }
}
