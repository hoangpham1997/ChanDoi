package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CPAllocationQuantum;
import vn.softdreams.ebweb.domain.CPAllocationRate;
import vn.softdreams.ebweb.repository.CPAllocationQuantumRepositoryCustom;
import vn.softdreams.ebweb.repository.CPAllocationRateRepositoryCustom;
import vn.softdreams.ebweb.service.dto.CPAllocationQuantumDTO;
import vn.softdreams.ebweb.service.dto.CPAllocationRateDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class CPAllocationRateRepositoryImpl implements CPAllocationRateRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<CPAllocationRate> findAllByCPPeriodID(UUID org, UUID cPPeriodID) {
        StringBuilder sql = new StringBuilder();
        List<CPAllocationRate> lstCPAllocationQuantums = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT a.* from CPAllocationRate a LEFT JOIN CPPeriod b ON a.CPPeriodID = b.ID WHERE b.CompanyID = :org AND a.CPPeriodID = :cPPeriodID");
        params.put("org", org);
        params.put("cPPeriodID", cPPeriodID);
        Query query = entityManager.createNativeQuery(sql.toString(), CPAllocationRate.class);
        Common.setParams(query, params);
        lstCPAllocationQuantums = query.getResultList();
        return lstCPAllocationQuantums;
    }

    @Override
    public List<CPAllocationRateDTO> findAllByListCostSets(List<UUID> uuids, UUID org) {
        StringBuilder sql = new StringBuilder();
        List<CPAllocationRateDTO> lstCPAllocationQuantums = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT 0 as allocationMethod, a.ID as costSetID, b.MaterialGoodsID as materialGoodsID, 0 as isStandardItem, 0 as quantity, 0 as priceQuantum, 0 as coefficient, 0 as quantityStandard, 0 as allocationStandard, 0 as allocatedRate from CostSet a LEFT JOIN CostSetMaterialGoods b ON a.ID = b.CostSetID WHERE a.ID IN :uuids AND a.CompanyID = :org");
        params.put("org", org);
        params.put("uuids", uuids);
        Query query = entityManager.createNativeQuery(sql.toString(), "CPAllocationRateDTO");
        Common.setParams(query, params);
        lstCPAllocationQuantums = query.getResultList();
        return lstCPAllocationQuantums;
    }
}
