package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.BackupData;
import vn.softdreams.ebweb.repository.RestoreHistoryRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Repository
public class RestoreHistoryRepositoryImpl implements RestoreHistoryRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManagerRestore;


    @Override
    public List<BackupData> getAllRestoreData(UUID uuid) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        queryStr.append("SELECT a.*, b.TimeRestore, b.Status " +
            "from BackupData a " +
            "         inner join RestoreHistory b on a.ID = b.BackupDataID " +
            "where CompanyID = :companyID order by TimeRestore desc");
        params.put("companyID", uuid);
        Query query = entityManagerRestore.createNativeQuery(queryStr.toString(), "BackupDataDTO");
        Common.setParams(query, params);
        List<BackupData> backupData = query.getResultList();
        return backupData;
    }

    public Page<BackupData> getAllRestoreData(Pageable pageable, UUID uuid) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        List<BackupData> lst = new ArrayList<>();
        queryStr.append(
            "from BackupData a " +
                "         inner join RestoreHistory b on a.ID = b.BackupDataID " +
                "where CompanyID = :companyID ");
        params.put("companyID", uuid);
        Query countQuerry = entityManagerRestore.createNativeQuery("SELECT Count(*) " + queryStr.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManagerRestore.createNativeQuery("Select a.*, b.TimeRestore, b.Status " + queryStr.toString() + " order by TimeRestore desc", "BackupDataDTO");
            Common.setParams(query, params);
            lst = query.getResultList();
        }
        return new PageImpl<>(((List<BackupData>) lst), pageable, total.longValue());
    }
}
