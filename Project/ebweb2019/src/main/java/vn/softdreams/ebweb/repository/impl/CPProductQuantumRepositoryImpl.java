package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CPOPN;
import vn.softdreams.ebweb.domain.CPProductQuantum;
import vn.softdreams.ebweb.repository.CPProductQuantumRepositoryCustom;
import vn.softdreams.ebweb.service.dto.CPProductQuantumDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class CPProductQuantumRepositoryImpl implements CPProductQuantumRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<CPProductQuantumDTO> findAllByIsActive(Pageable pageable, List<UUID> companyID) {
        StringBuilder sql = new StringBuilder();
        String newSql = " b.ID, a.MaterialGoodsCode, a.MaterialGoodsName, a.UnitID, b.DirectMaterialAmount, b.DirectLaborAmount, " +
            "b.MachineMaterialAmount, b.MachineLaborAmount, b.MachineToolsAmount, b.MachineDepreciationAmount, b.MachineServiceAmount, " +
            "b.MachineGeneralAmount, b.GeneralMaterialAmount, b.GeneralLaborAmount, b.GeneralToolsAmount, b.GeneralDepreciationAmount, b.GeneralServiceAmount, " +
            "b.OtherGeneralAmount, b.TotalCostAmount, a.ID as materialGoodsID ";
        List<CPProductQuantumDTO> lstCPProductQuantumDTOs = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append(" FROM MaterialGoods a LEFT JOIN CPProductQuantum b ON a.ID = b.ID where a.CompanyID IN :companyID AND isActive = 1 AND MaterialGoodsType = 3 ");
        params.put("companyID", companyID);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT " + newSql.toString() + sql.toString() + " ORDER BY a.MaterialGoodsCode ", "CPProductQuantumDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            lstCPProductQuantumDTOs = query.getResultList();
        }
        return new PageImpl<>(lstCPProductQuantumDTOs, pageable, total.longValue());
    }

    @Override
    public List<CPProductQuantum> getByCosetID(List<UUID> collect) {
        StringBuilder sql = new StringBuilder();
        List<CPProductQuantum> cpProductQuantums = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("Select * from CPProductQuantum where ID in :ids");
        params.put("ids", collect);
        Query query = entityManager.createNativeQuery(sql.toString(), CPProductQuantum.class);
        Common.setParams(query, params);
        cpProductQuantums = query.getResultList();
        return cpProductQuantums;
    }
}
