package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.ToolledgerRepositoryCustom;
import vn.softdreams.ebweb.repository.ToolsRepositoryCustom;
import vn.softdreams.ebweb.service.dto.ToolsConvertDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ToolledgerRepositoryImpl implements ToolledgerRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public boolean unrecord(UUID refID) {
        boolean result = true;
        try {
            StringBuilder sql = new StringBuilder();
            Map<String, Object> params = new HashMap<>();
            sql.append("DELETE FROM ToolLedger WHERE ReferenceID = :ReferenceID");
            params.put("ReferenceID", refID);
            Query query = entityManager.createNativeQuery(sql.toString());
            Common.setParams(query, params);
            query.executeUpdate();
        }catch (Exception ex){
            result = false;
        }
        return result;
    }
}
