package vn.softdreams.ebweb.repository.impl;

import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.repository.BackupDataRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class BackupDataRepositoryImpl implements BackupDataRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "reportEntityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManagerRestore;

    @Override
    public List<String> backupDataString(UUID companyID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("EXEC [sp_generate_insertscripts_backup] @CompanyID = :companyID");
        params.put("companyID", companyID);
        Query query = entityManager.createNativeQuery(sql.toString());
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public void restoreData(StringBuilder sqlRestore) throws SQLGrammarException {
        Query query = entityManagerRestore.createNativeQuery(sqlRestore.toString());
        query.executeUpdate();
    }
}
