package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CPAllocationQuantum;
import vn.softdreams.ebweb.repository.CPAllocationQuantumRepositoryCustom;
import vn.softdreams.ebweb.repository.CPProductQuantumRepositoryCustom;
import vn.softdreams.ebweb.service.dto.CPAllocationQuantumDTO;
import vn.softdreams.ebweb.service.dto.CPProductQuantumDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class CPAllocationQuantumRepositoryImpl implements CPAllocationQuantumRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<CPAllocationQuantumDTO> findAllByIsActive(Pageable pageable,List<UUID> uuidList, UUID companyID, Boolean isNoMBook) {
        StringBuilder sqlCostSet = new StringBuilder();
        StringBuilder sqlEMContract = new StringBuilder();
        String newSqlCostSet = " SELECT b.ID as ID, a.CostSetCode as objectCode, a.costSetName  as objectName, b.DirectMaterialAmount, b.DirectLaborAmount, " +
            "b.MachineMaterialAmount, b.MachineLaborAmount, b.MachineToolsAmount, b.MachineDepreciationAmount, b.MachineServiceAmount, " +
            "b.MachineGeneralAmount, b.GeneralMaterialAmount, b.GeneralLaborAmount, b.GeneralToolsAmount, b.GeneralDepreciationAmount, b.GeneralServiceAmount, " +
            "b.OtherGeneralAmount, b.TotalCostAmount, a.ID as objectID";
        String newSqlEMContract = "";
        if (Boolean.TRUE.equals(isNoMBook)) {
            newSqlEMContract = " SELECT b.ID  as ID," +
                "       c.NoMBook as objectCode," +
                "       c.Name as objectName," +
                "       b.DirectMaterialAmount," +
                "       b.DirectLaborAmount," +
                "       b.MachineMaterialAmount," +
                "       b.MachineLaborAmount," +
                "       b.MachineToolsAmount," +
                "       b.MachineDepreciationAmount," +
                "       b.MachineServiceAmount," +
                "       b.MachineGeneralAmount," +
                "       b.GeneralMaterialAmount," +
                "       b.GeneralLaborAmount," +
                "       b.GeneralToolsAmount," +
                "       b.GeneralDepreciationAmount," +
                "       b.GeneralServiceAmount," +
                "       b.OtherGeneralAmount," +
                "       b.TotalCostAmount, c.ID as objectID";
        } else {
            newSqlEMContract = " SELECT b.ID  as ID," +
                "       c.NoFBook as objectCode," +
                "       c.Name as objectName," +
                "       b.DirectMaterialAmount," +
                "       b.DirectLaborAmount," +
                "       b.MachineMaterialAmount," +
                "       b.MachineLaborAmount," +
                "       b.MachineToolsAmount," +
                "       b.MachineDepreciationAmount," +
                "       b.MachineServiceAmount," +
                "       b.MachineGeneralAmount," +
                "       b.GeneralMaterialAmount," +
                "       b.GeneralLaborAmount," +
                "       b.GeneralToolsAmount," +
                "       b.GeneralDepreciationAmount," +
                "       b.GeneralServiceAmount," +
                "       b.OtherGeneralAmount," +
                "       b.TotalCostAmount, c.ID as objectID";
        }
        List<CPAllocationQuantumDTO> lstCPAllocationQuantumDTOs = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sqlCostSet.append(" FROM CostSet a LEFT JOIN CPAllocationQuantum b ON a.ID = b.ID where a.CompanyID IN :uuidList AND isActive = 1");
        sqlEMContract.append(" FROM EMContract c LEFT JOIN CPAllocationQuantum b ON c.ID = b.ID where c.CompanyID = :companyID AND TypeID = 730 AND isActive = 1");
        params.put("companyID", companyID);
        params.put("uuidList", uuidList);
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) FROM ( " + newSqlCostSet.toString() + sqlCostSet.toString() + " UNION ALL " + newSqlEMContract.toString() + sqlEMContract.toString() + ") as #temp");
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery(newSqlCostSet.toString() + sqlCostSet.toString() + " UNION ALL " + newSqlEMContract.toString() + sqlEMContract.toString(), "CPAllocationQuantumDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            lstCPAllocationQuantumDTOs = query.getResultList();
        }
        return new PageImpl<>(lstCPAllocationQuantumDTOs, pageable, total.longValue());
    }

    @Override
    public List<CPAllocationQuantum> findByCostSetID(List<UUID> ids) {
        StringBuilder sql = new StringBuilder();
        List<CPAllocationQuantum> lstCPAllocationQuantums = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("Select * from CPAllocationQuantum where ID in :ids");
        params.put("ids", ids);
        Query query = entityManager.createNativeQuery(sql.toString(), CPAllocationQuantum.class);
        Common.setParams(query, params);
        lstCPAllocationQuantums = query.getResultList();
        return lstCPAllocationQuantums;
    }
}
