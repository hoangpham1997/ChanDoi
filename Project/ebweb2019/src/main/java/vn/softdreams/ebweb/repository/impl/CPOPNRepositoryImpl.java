package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CPAllocationQuantum;
import vn.softdreams.ebweb.domain.CPOPN;
import vn.softdreams.ebweb.repository.CPOPNRepositoryCustom;
import vn.softdreams.ebweb.service.dto.CPOPNSDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class CPOPNRepositoryImpl implements CPOPNRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<CPOPNSDTO> findAllByIsActive(Pageable pageable, List<UUID> uuidList, UUID companyID, Boolean isNoMBook) {
        List<CPOPNSDTO> lstCPProductQuantumDTOs = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlProduct = new StringBuilder();
        StringBuilder sqlConstructor = new StringBuilder();
        StringBuilder sqlContract = new StringBuilder();
        String newSqlProducts = "  b.ID, 0 as objectType, b.CostSetID, a.CostSetCode, a.CostSetName, b.ContractID, null as contractCode, null as signedDate, null as accountingObjectID, b.DirectMaterialAmount, b.DirectLaborAmount, " +
            "b.MachineMaterialAmount, b.MachineLaborAmount, b.MachineToolsAmount, b.MachineDepreciationAmount, b.MachineServiceAmount, " +
            "b.MachineGeneralAmount, b.GeneralMaterialAmount, b.GeneralLaborAmount, b.GeneralToolsAmount, b.GeneralDepreciationAmount, b.GeneralServiceAmount, " +
            "b.OtherGeneralAmount, b.TotalCostAmount, b.UncompletedAccount,0 as acceptedAmount , 0 as notAcceptedAmount, a.ID as objectID ";
        String newSqlFactoryAndDepartments = "  b.ID, 1 as objectType, b.CostSetID, a.CostSetCode, a.CostSetName, b.ContractID, null as contractCode, null as signedDate, null as accountingObjectID, b.DirectMaterialAmount, b.DirectLaborAmount, " +
            "b.MachineMaterialAmount, b.MachineLaborAmount, b.MachineToolsAmount, b.MachineDepreciationAmount, b.MachineServiceAmount, " +
            "b.MachineGeneralAmount, b.GeneralMaterialAmount, b.GeneralLaborAmount, b.GeneralToolsAmount, b.GeneralDepreciationAmount, b.GeneralServiceAmount, " +
            "b.OtherGeneralAmount, b.TotalCostAmount, b.UncompletedAccount,0 as acceptedAmount , 0 as notAcceptedAmount, a.ID as objectID ";
        String newSqlConstructors = " b.ID, 2 as objectType, b.CostSetID, a.CostSetCode, a.CostSetName, b.ContractID, null as contractCode, null as signedDate, null as accountingObjectID, b.DirectMaterialAmount, b.DirectLaborAmount, " +
            "b.MachineMaterialAmount, b.MachineLaborAmount, b.MachineToolsAmount, b.MachineDepreciationAmount, b.MachineServiceAmount, " +
            "b.MachineGeneralAmount, b.GeneralMaterialAmount, b.GeneralLaborAmount, b.GeneralToolsAmount, b.GeneralDepreciationAmount, b.GeneralServiceAmount, " +
            "b.OtherGeneralAmount, b.TotalCostAmount, b.UncompletedAccount,b.acceptedAmount , b.notAcceptedAmount, a.ID as objectID ";
        String newSqlOrders = " b.ID, 3 as objectType, b.CostSetID, a.CostSetCode, a.CostSetName, b.ContractID, null as contractCode, null as signedDate, null as accountingObjectID, b.DirectMaterialAmount, b.DirectLaborAmount, " +
            "b.MachineMaterialAmount, b.MachineLaborAmount, b.MachineToolsAmount, b.MachineDepreciationAmount, b.MachineServiceAmount, " +
            "b.MachineGeneralAmount, b.GeneralMaterialAmount, b.GeneralLaborAmount, b.GeneralToolsAmount, b.GeneralDepreciationAmount, b.GeneralServiceAmount, " +
            "b.OtherGeneralAmount, b.TotalCostAmount, b.UncompletedAccount,b.acceptedAmount , b.notAcceptedAmount, a.ID as objectID ";
        String newSqlContract = " b.ID, 4 as objectType, b.CostSetID, null as costSetCode, null as costSetName, b.ContractID, a.NoFBook as contractCode, a.SignedDate, a.accountingObjectID as accountingObjectID, b.DirectMaterialAmount, b.DirectLaborAmount, " +
            "b.MachineMaterialAmount, b.MachineLaborAmount, b.MachineToolsAmount, b.MachineDepreciationAmount, b.MachineServiceAmount, " +
            "b.MachineGeneralAmount, b.GeneralMaterialAmount, b.GeneralLaborAmount, b.GeneralToolsAmount, b.GeneralDepreciationAmount, b.GeneralServiceAmount, " +
            "b.OtherGeneralAmount, b.TotalCostAmount, b.UncompletedAccount ,b.acceptedAmount , b.notAcceptedAmount, a.ID as objectID ";
        sqlProduct.append(" FROM CostSet a LEFT JOIN CPOPN b ON a.ID = b.CostSetID where a.CompanyID IN :uuidList AND isActive = 1 ");
        sqlConstructor.append(" FROM CostSet a LEFT JOIN CPOPN b ON a.ID = b.CostSetID where a.CompanyID IN :uuidList AND isActive = 1 ");
        sqlContract.append(" FROM EMContract a LEFT JOIN CPOPN b ON a.ID = b.ContractID where a.CompanyID = :companyID AND isActive = 1 ");
        params.put("companyID", companyID);
        params.put("uuidList", uuidList);
        String selectSQL = "SELECT " + newSqlProducts.toString() + sqlProduct.toString() + "AND CostSetType = 4 UNION ALL SELECT " + newSqlFactoryAndDepartments.toString() + sqlProduct.toString() + " AND CostSetType = 2 UNION ALL SELECT "
            + newSqlConstructors.toString() + sqlConstructor.toString() + " AND CostSetType = 1 UNION ALL SELECT " + newSqlOrders.toString() + sqlConstructor.toString() + "AND CostSetType = 0 UNION ALL SELECT " + newSqlContract.toString() + sqlContract.toString();
        String countSQL = "SELECT Count(1) FROM (" + selectSQL.toString() + " ) as #temp";
        Query countQuerry = entityManager.createNativeQuery(countSQL.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery(selectSQL.toString(), "CPOPNSDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            lstCPProductQuantumDTOs = query.getResultList();
        }
        return new PageImpl<>(lstCPProductQuantumDTOs, pageable, total.longValue());
    }

    @Override
    public List<CPOPN> getByCosetID(List<UUID> collect) {
        StringBuilder sql = new StringBuilder();
        List<CPOPN> cpopns = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("Select * from CPOPN where CostSetID in :ids");
        params.put("ids", collect);
        Query query = entityManager.createNativeQuery(sql.toString(), CPOPN.class);
        Common.setParams(query, params);
        cpopns = query.getResultList();
        return cpopns;
    }
}
