package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.MaterialQuantum;
import vn.softdreams.ebweb.repository.MaterialQuantumRepositoryCustom;
import vn.softdreams.ebweb.service.dto.MaterialQuantumGeneralDTO;
import vn.softdreams.ebweb.service.dto.ObjectsMaterialQuantumDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class MaterialQuantumRepositoryImpl implements MaterialQuantumRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<ObjectsMaterialQuantumDTO> findAllObjectActive(List<UUID> listOrgIDDTTHCP, List<UUID> listOrgIDVTHH) {
        Map<String, Object> params = new HashMap<>();
        List<ObjectsMaterialQuantumDTO> objectsMaterialQuantumDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ID as objectID, CostSetCode as objectCode, CostSetName as objectName, 'ĐTTHCP' as objectTypeString, 1 as objectType, IsActive as isActive from CostSet Where CompanyID IN :listOrgIDDTTHCP ");
        params.put("listOrgIDDTTHCP", listOrgIDDTTHCP);
        sql.append(" UNION ALL ");
        sql.append(" SELECT ID as objectID, MaterialGoodsCode as objectCode, MaterialGoodsName as objectName, 'VTHH' as objectTypeString, 0 as objectType, IsActive as isActive from MaterialGoods Where CompanyID IN :listOrgIDVTHH ");
        params.put("listOrgIDVTHH", listOrgIDVTHH);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) FROM (" + sql.toString() + ") as #temp" );
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery(sql.toString(), "ObjectsMaterialQuantumDTO");
            Common.setParams(query, params);
            objectsMaterialQuantumDTOS = query.getResultList();
        }
        return objectsMaterialQuantumDTOS;
    }

    @Override
    public List<MaterialQuantumGeneralDTO> findAllByCompanyID(List<UUID> companyIDCostSet, List<UUID> companyIDMaterialGoods, UUID currentOrg) {
        Map<String, Object> params = new HashMap<>();
        List<MaterialQuantumGeneralDTO> objectsMaterialQuantumDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.ID as id, a.ObjectID as objectID, b.CostSetCode as objectCode, b.CostSetName as objectName, a.FromDate as fromDate, a.ToDate as toDate FROM MaterialQuantum a LEFT JOIN CostSet b ON a.ObjectID = b.ID WHERE a.CompanyID = :currentOrg AND b.CompanyID IN :companyIDCostSet ");
        sql.append("UNION ALL SELECT a.ID as id, a.ObjectID as objectID, b.MaterialGoodsCode as objectCode, b.MaterialGoodsName as objectName, a.FromDate as fromDate, a.ToDate as toDate FROM MaterialQuantum a LEFT JOIN MaterialGoods b ON a.ObjectID = b.ID WHERE a.CompanyID = :currentOrg AND b.CompanyID IN :companyIDMaterialGoods ");
        params.put("companyIDCostSet", companyIDCostSet);
        params.put("currentOrg",currentOrg);
        params.put("companyIDMaterialGoods", companyIDMaterialGoods);
        Query query = entityManager.createNativeQuery(sql.toString(), "MaterialQuantumGeneralDTO");
        Common.setParams(query, params);
        objectsMaterialQuantumDTOS = query.getResultList();
        return objectsMaterialQuantumDTOS;
    }
}
