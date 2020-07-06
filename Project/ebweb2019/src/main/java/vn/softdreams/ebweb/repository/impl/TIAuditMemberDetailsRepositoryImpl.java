package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.TIAuditMemberDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.TIAuditMemberDetailByIDDTO;
import vn.softdreams.ebweb.web.rest.dto.TIAuditMemberDetailDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class TIAuditMemberDetailsRepositoryImpl implements TIAuditMemberDetailsRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<TIAuditMemberDetailDTO> findTIAuditMemberDetailDTO(UUID id) {
        StringBuilder sql = new StringBuilder();
        List<TIAuditMemberDetailDTO> units;
        Map<String, Object> params = new HashMap<>();
        sql.append("select tim.AccountObjectName, tim.AccountingObjectTitle, eb.OrganizationUnitName " +
            "from TIAudit t " +
            "         left join TIAuditMemberDetail tim on tim.TIAuditID = t.ID " +
            "         left join EbOrganizationUnit eb on tim.DepartmentID = eb.ID " +
            "where tim.TIAuditID = :id " +
            "order by tim.OrderPriority ");
        params.put("id", id);
        Query query = entityManager.createNativeQuery( sql.toString(), "TIAuditMemberDetailsDTO");
        Common.setParams(query, params);
        units = query.getResultList();
        return units;
    }

    @Override
    public List<TIAuditMemberDetailByIDDTO> findAllByTiAuditID(UUID id) {

        StringBuilder sql = new StringBuilder();
        List<TIAuditMemberDetailByIDDTO> tiAuditMemberDetailByIDDTOS;
        Map<String, Object> params = new HashMap<>();
        sql.append("select tmdtl.id,  " +
            "       tmdtl.TIAuditID,  " +
            "       tmdtl.AccountingObjectID,  " +
            "       ac.AccountingObjectCode,  " +
            "       tmdtl.AccountObjectName,  " +
            "       tmdtl.AccountingObjectTitle,  " +
            "       tmdtl.Role,  " +
            "       tmdtl.DepartmentID,  " +
            "       eo.OrganizationUnitCode DepartmentCode,  " +
            "       tmdtl.OrderPriority  " +
            "from TIAuditMemberDetail tmdtl  " +
            "         left join EbOrganizationUnit eo on eo.ID = tmdtl.DepartmentID  " +
            "         left join AccountingObject ac on ac.ID = tmdtl.AccountingObjectID  " +
            "where tmdtl.TIAuditID = :id order by tmdtl.OrderPriority");
        params.put("id", id);
        Query query = entityManager.createNativeQuery( sql.toString(), "TIAuditMemberDetailByIDDTO");
        Common.setParams(query, params);
         tiAuditMemberDetailByIDDTOS = query.getResultList();
        return tiAuditMemberDetailByIDDTOS;
    }
}
