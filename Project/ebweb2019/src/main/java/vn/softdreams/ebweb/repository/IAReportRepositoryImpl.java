package vn.softdreams.ebweb.repository;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static vn.softdreams.ebweb.service.Utils.Utils.setParams;

public class IAReportRepositoryImpl implements IAReportRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public void multiDeleteReportInvoice(UUID orgID, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM IAReport WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append("AND ID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }
}
