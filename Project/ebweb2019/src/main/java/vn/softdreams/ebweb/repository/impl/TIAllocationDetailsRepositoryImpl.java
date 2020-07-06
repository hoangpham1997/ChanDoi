package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.TIAllocationDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.TIAllocationDetailDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TIAllocationDetailsRepositoryImpl implements TIAllocationDetailsRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<TIAllocationDetailDTO> findTIAllocationDetails(UUID id) {
        StringBuilder sql = new StringBuilder();
        List<TIAllocationDetailDTO> units;
        Map<String, Object> params = new HashMap<>();
        sql.append("select t.ToolCode, t.ToolName, tia.AllocationAmount, tia.RemainingAmount, tia.TotalAllocationAmount " +
            "from TIAllocation ti " +
            "left join TIAllocationDetail tia on tia.TIAllocationID = ti.ID " +
            "left join Tools t on tia.ToolsID = t.ID " +
            "where ti.ID= :id " +
            "order by tia.OrderPriority ");
        params.put("id", id);
        Query query = entityManager.createNativeQuery(sql.toString(), "TIAllocationDetailDTO");
        Common.setParams(query, params);
        units = query.getResultList();
        return units;
    }
}
