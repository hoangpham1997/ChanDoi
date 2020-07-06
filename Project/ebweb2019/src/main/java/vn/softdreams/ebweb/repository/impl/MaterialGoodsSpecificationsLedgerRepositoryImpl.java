package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.MaterialGoodsSpecificationsLedgerRepositoryCustom;
import vn.softdreams.ebweb.repository.MaterialQuantumRepositoryCustom;
import vn.softdreams.ebweb.service.dto.MaterialGoodsSpecificationsLedgerDTO;
import vn.softdreams.ebweb.service.dto.MaterialQuantumGeneralDTO;
import vn.softdreams.ebweb.service.dto.ObjectsMaterialQuantumDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class MaterialGoodsSpecificationsLedgerRepositoryImpl implements MaterialGoodsSpecificationsLedgerRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<MaterialGoodsSpecificationsLedgerDTO> findByMaterialGoodsID(UUID org, UUID id, UUID repositoryID) {
        Map<String, Object> params = new HashMap<>();
        List<MaterialGoodsSpecificationsLedgerDTO> materialGoodsSpecificationsLedgerDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *, " +
            "       ROW_NUMBER() OVER (order by a.RepositoryCode) rowIndex " +
            "       FROM (SELECT m.MaterialGoodsID, " +
            "       m.Specification1, " +
            "       m.Specification2, " +
            "       m.Specification3, " +
            "       m.Specification4, " +
            "       m.Specification5, " +
            "       m.IWRepositoryID, " +
            "       null OWRepositoryID, " +
            "       r.RepositoryCode, " +
            "       (ISNULL(SUM(ISNULL(IWQuantity, 0)), 0) - ISNULL(SUM(ISNULL(OWQuantity, 0)), 0)) IWQuantity, " +
            "       0    OWQuantity " +
            "FROM MaterialGoodsSpecificationsLedger m " +
            "         LEFT JOIN Repository r ON IWRepositoryID = r.ID " +
            "WHERE m.CompanyID = :comid ");
        if (repositoryID != null) {
            sql.append(" AND m.IWRepositoryID = :repid ");
            params.put("repid", repositoryID);
        }
        sql.append("  AND m.MaterialGoodsID = :mid " +
            "GROUP BY MaterialGoodsID, Specification1, Specification2, Specification3, Specification4, Specification5, " +
            "         IWRepositoryID, r.RepositoryCode) a WHERE a.IWQuantity > 0 ");
        params.put("comid", org);
        params.put("mid", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "MaterialGoodsSpecificationsLedgerDTO");
        Common.setParams(query, params);
        materialGoodsSpecificationsLedgerDTOS = query.getResultList();
        return materialGoodsSpecificationsLedgerDTOS;
    }
}
