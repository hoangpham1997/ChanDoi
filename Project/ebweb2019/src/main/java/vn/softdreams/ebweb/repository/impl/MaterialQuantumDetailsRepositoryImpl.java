package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.repository.MaterialQuantumDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDTO;
import vn.softdreams.ebweb.service.dto.MaterialQuantumDetailsDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class MaterialQuantumDetailsRepositoryImpl implements MaterialQuantumDetailsRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<MaterialQuantumDTO> findAllMaterialQuantumDTO(Pageable pageable, String fromDate, String toDate, UUID org) {
        List<MaterialQuantumDTO> materialQuantumDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select mq.ToDate, ")
            .append(" mq.ObjectType, " )
            .append(" mq.ObjectID, " )
            .append(" mq.FromDate, " )
            .append(" mq.id, " )
            .append(" mg.MaterialGoodsCode, " )
            .append(" mg.MaterialGoodsName, " )
            .append(" cs.CostSetCode, " )
            .append(" cs.CostSetName ")
            .append(" from MaterialQuantum mq")
            .append(" left join MaterialGoods mg on mq.ObjectID = mg.ID and mq.ObjectType = 0 ")
            .append(" left join CostSet cs on mq.ObjectID = cs.ID and mq.ObjectType = 1 ")
            .append(" Where mq.ObjectType in (0, 1) ");
        Map<String, Object> params = new HashMap<>();
        if (org != null) {
            sql.append("and mq.companyID = :org ");
            params.put("org", org);
        }
        if (!Strings.isNullOrEmpty(fromDate)) {
            sql.append("and CONVERT(varchar, FromDate, 112) >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            sql.append("and CONVERT(varchar, ToDate, 112) <= :toDate ");
            params.put("toDate", toDate);
        }

        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) from (" + sql.toString() + ") a");
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            sql.append(" order by mq.toDate , mq.fromDate, mq.ObjectID ");
            Query query = entityManager.createNativeQuery(sql.toString(), "MaterialQuantumDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            materialQuantumDTOS = query.getResultList();
        }
        return new PageImpl<>(materialQuantumDTOS, pageable, total.longValue());
    }

    @Override
    public List<MaterialQuantumDetailsDTO> findByMaterialQuantumIDOrderByOrderPriority(List<UUID> id) {
        StringBuilder sql = new StringBuilder();
        sql.append("Select MQD.*, MG.RepositoryID ")
            .append(" from MaterialQuantumDetail MQD ")
            .append(" left join MaterialGoods MG on MG.ID = MQD.MaterialGoodsID ")
            .append(" where MaterialQuantumID in :materialQuantumID order by OrderPriority");
        Query query = entityManager.createNativeQuery(sql.toString(), "MaterialQuantumDetailsDTO");
        Map<String, Object> params = new HashMap<>();
        params.put("materialQuantumID", id);
        Common.setParams(query, params);
        return query.getResultList();
    }
}
