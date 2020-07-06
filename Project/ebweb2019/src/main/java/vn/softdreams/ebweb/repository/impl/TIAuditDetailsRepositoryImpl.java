package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.TIAuditDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.TIAuditDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditMemberDetailDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TIAuditDetailsRepositoryImpl implements TIAuditDetailsRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;


    @Override
    public List<TIAuditDetailDTO> findTIAuditDetailDTO(UUID id) {
        StringBuilder sql = new StringBuilder();
        List<TIAuditDetailDTO> units;
        Map<String, Object> params = new HashMap<>();
        sql.append("select t.ToolCode, " +
            "       t.ToolName, " +
            "       u.UnitName, " +
            "       eb.OrganizationUnitName, " +
            "       tid.QuantityOnBook, " +
            "       tid.QuantityInventory, " +
            "       tid.DiffQuantity, " +
            "       tid.ExecuteQuantity, " +
            "       tid.Recommendation " +
            " from TIAudit tia " +
            "        left join TIAuditDetail tid on tia.ID = tid.TIAuditID " +
            "         left join Tools t on tid.ToolsID = t.id " +
            "         left join Unit u on u.ID = t.UnitID " +
            "         left join EbOrganizationUnit eb on eb.ID = tid.DepartmentID " +
            "where tid.TIAuditID = :id " +
            "order by tid.OrderPriority ");
        params.put("id", id);
        Query query = entityManager.createNativeQuery( sql.toString(), "TIAuditDetailDTO");
        Common.setParams(query, params);
        units = query.getResultList();
        return units;
    }
}
