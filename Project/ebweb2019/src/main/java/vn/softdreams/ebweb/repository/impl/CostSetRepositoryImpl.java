package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.repository.CostSetRepositoryCustom;
import vn.softdreams.ebweb.service.dto.CPAllocationRateDTO;
import vn.softdreams.ebweb.service.dto.CostSetDTO;
import vn.softdreams.ebweb.service.dto.CostSetMaterialGoodsDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

import static vn.softdreams.ebweb.service.Utils.Utils.setParams;

public class CostSetRepositoryImpl implements CostSetRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;


    @Override
    public Page<CostSet> findAll(Pageable pageable, UUID branchID, String costSetCode, String costSetName, Integer costSetType,
                                 String description, UUID parentID, Boolean isParentNode, String orderFixCode, Integer grade, Boolean isActive){
        StringBuilder sql = new StringBuilder();
        List<CostSet> costSets = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM COSTSET WHERE 1 = 1 ");

        if(branchID != null){
            sql.append("AND branchID = :branchID ");
            params.put("branchID", branchID);
        }

        if(!Strings.isNullOrEmpty(costSetCode)){
            sql.append("AND costSetCode LIKE :costSetCode ");
            params.put("costSetCode", "%" +costSetCode+ "%");
        }

        if(!Strings.isNullOrEmpty(costSetName)){
            sql.append("AND costSetName = :costSetName ");
            params.put("costSetName", costSetName);
        }

        if(costSetType != null){
            sql.append("AND costSetType = :costSetType ");
            params.put("costSetType", costSetType);
        }

        if(!Strings.isNullOrEmpty(description)){
            sql.append("AND description = :description ");
            params.put("description", description);
        }

        if(parentID != null){
            sql.append("AND parentID = :parentID ");
            params.put("parentID", parentID);
        }

        if(isParentNode != null){
            sql.append("AND isParentNode = :isParentNode ");
            params.put("isParentNode", isParentNode);
        }

        if(!Strings.isNullOrEmpty(orderFixCode)){
            sql.append("AND orderFixCode = :orderFixCode ");
            params.put("orderFixCode", orderFixCode);
        }

        if(grade != null){
            sql.append("AND grade = :grade ");
            params.put("grade", grade);
        }

        if(isActive != null){
            sql.append("AND isActive = :isActive");
            params.put("isActive", isActive);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString(), CostSet.class);
            setParamsWithPageable(query, params, pageable, total);
            costSets = query.getResultList();
        }
        return new PageImpl<>(costSets, pageable, total.longValue());
    }

    @Override
    public Page<CostSet> getAllByListCompany(Pageable pageable, List<UUID> listCompanyID) {
        StringBuilder sql = new StringBuilder();
        List<CostSet> costSets = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM CostSet WHERE 1 = 1 ");
        if (listCompanyID.size() > 0) {
            sql.append(" AND CompanyID in :org ");
            params.put("org", listCompanyID);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " ORDER BY CostSetCode ASC"
                , CostSet.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            costSets = query.getResultList();
        }
        return new PageImpl<>(costSets, pageable, total.longValue());
    }

    @Override
    public Page<CostSet> pageableAllCostSets(Pageable pageable, UUID org) {
        StringBuilder sql = new StringBuilder();
        List<CostSet> costSets = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM CostSet WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID = :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by CostSetCode ASC"
                , CostSet.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            costSets = query.getResultList();
        }
        return new PageImpl<>(costSets, pageable, total.longValue());
    }

/*    @Override
    public Page<CostSet> pageableAllCostSet(Pageable pageable, List<UUID> listCompanyID) {
        StringBuilder sql = new StringBuilder();
        List<CostSet> costSets = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM CostSet WHERE 1 = 1 ");
        if (listCompanyID.size() > 0) {
            sql.append(" AND CompanyID in :org ");
            params.put("org", listCompanyID);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " order by CostSetCode ASC"
                , MaterialGoods.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            costSets = query.getResultList();
        }
        return new PageImpl<>(costSets, pageable, total.longValue());
    }*/

    /*@Override
    public Page<CostSet> pageableAllCostSet(Pageable pageable, List<UUID> org) {
        StringBuilder sql = new StringBuilder();
        List<CostSet> costSets = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM CostSet WHERE 1 = 1 ");
        if (org != null) {
            sql.append("AND CompanyID in :org ");
            params.put("org", org);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " ORDER BY CostSetCode ASC"
                , CostSet.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            costSets = query.getResultList();

        }
        return new PageImpl<>(costSets, pageable, total.longValue());
    }*/

    @Override
    public void multideleteCostSets(UUID orgID, List<UUID> uuidList) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DELETE FROM CostSet WHERE companyID = :orgID ");
        params.put("orgID", orgID);
        sql.append("AND ID IN :uuidList ");
        params.put("uuidList", uuidList);
        Query query = entityManager.createNativeQuery(sql.toString());
        setParams(query, params);
        query.executeUpdate();
    }

    @Override
    public List<UpdateDataDTO> getUsedCostSetID(List<UUID> costSets) {
        String sql = "select CostSetID as uuid from ViewCheckConstraint where CostSetID in :costSets group by CostSetID";
        Map<String, Object> params = new HashMap<>();
        params.put("costSets", costSets);
        Query query = entityManager.createNativeQuery(sql, "UUIDDTO");
        setParams(query, params);
        return query.getResultList();
    }

    @Override
    public Page<CostSet> getCostSetsByTypeRaTio(Pageable pageable, Integer type, List<UUID> allCompanyByCompanyIdAndCode) {
        StringBuilder sql = new StringBuilder();
        List<CostSet> costSets = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM CostSet WHERE 1 = 1 AND CostSetType = :type AND IsActive = 1 ");
        params.put("type", type);
        if (allCompanyByCompanyIdAndCode != null) {
            sql.append("AND CompanyID in :org ");
            params.put("org", allCompanyByCompanyIdAndCode);
        }
        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) " + sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery("SELECT * " + sql.toString() + " ORDER BY CostSetCode ASC"
                , CostSet.class);
            Common.setParamsWithPageable(query, params, pageable, total);
            costSets = query.getResultList();

        }
        return new PageImpl<>(costSets, pageable, total.longValue());
    }

    @Override
    public Boolean checkCostSetRelatedVoucher(UUID id) {
        Boolean result = false;
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("DECLARE @count1 int = (select count(*) from CostSet a left join CPOPN b on a.ID = b.CostSetID where a.ID = :ID ");
        params.put("ID", id);
        sql.append("and (ABS(ISNULL(b.DirectMaterialAmount, 0)) +  ");
        sql.append("ABS(ISNULL(b.DirectLaborAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.MachineDepreciationAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.MachineLaborAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.MachineToolsAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.MachineDepreciationAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.MachineServiceAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.MachineGeneralAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.GeneralMaterialAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.GeneralLaborAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.GeneralDepreciationAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.GeneralServiceAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.GeneralToolsAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.OtherGeneralAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.TotalCostAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.AcceptedAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.NotAcceptedAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.UncompletedAccount, 0))) <> 0) ");
        sql.append("DECLARE @count2 int = (select count(*) from CostSet a left join CPAllocationQuantum b on a.ID = b.ID where a.ID = :ID ");
        sql.append("and (ABS(ISNULL(b.DirectMaterialAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.DirectLaborAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.MachineDepreciationAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.MachineLaborAmount, 0)) +" );
        sql.append("ABS(ISNULL(b.MachineToolsAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.MachineDepreciationAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.MachineServiceAmount, 0)) +" );
        sql.append("ABS(ISNULL(b.MachineGeneralAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.GeneralMaterialAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.GeneralLaborAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.GeneralDepreciationAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.GeneralServiceAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.GeneralToolsAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.OtherGeneralAmount, 0)) + ");
        sql.append("ABS(ISNULL(b.TotalCostAmount, 0))) <> 0)");
        sql.append("select @count1 + @count2");
        Query countQuerry = entityManager.createNativeQuery(sql.toString());
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.intValue() > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public List<CostSetMaterialGoodsDTO> getCostSetByListID(List<UUID> orgCostSet, List<UUID> orgMaterialGoods, List<UUID> uuids) {
        StringBuilder sql = new StringBuilder();
        List<CostSetMaterialGoodsDTO> costSetMaterialGoodsDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT a.MaterialGoodsID as quantumID, c.MaterialGoodsCode as quantumCode,");
        sql.append(" c.MaterialGoodsName as quantumName, 0 as quantityClosing, 0 as rate, a.CostSetID as costSetID, b.costSetCode as costSetCode");
        sql.append(" FROM CostSetMaterialGoods a LEFT JOIN CostSet b ON a.CostSetID = b.ID LEFT JOIN MaterialGoods c ON " +
            "a.MaterialGoodsID = c.ID WHERE b.ID IN :uuids AND b.CompanyID IN :orgCostSet AND c.CompanyID IN :orgMaterialGoods AND a.CostSetID IN :uuids " +
            "order by b.costSetCode, c.MaterialGoodsCode");
        params.put("orgCostSet", orgCostSet);
        params.put("orgMaterialGoods", orgMaterialGoods);
        params.put("uuids", uuids);
        Query query = entityManager.createNativeQuery(sql.toString(), "CostSetMaterialGoodsDTO");
        Common.setParams(query, params);
        costSetMaterialGoodsDTOS = query.getResultList();
        return costSetMaterialGoodsDTOS;
    }

    @Override
    public List<CostSetMaterialGoodsDTO> getMaterialGoodsByCostSetID(List<UUID> collect) {
        StringBuilder sql = new StringBuilder();
        List<CostSetMaterialGoodsDTO> costSetMaterialGood = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("Select c.MaterialGoodsID as quantumID, c.CostSetID CostSetID from CostSetMaterialGoods c where CostSetID in :uuids");
        params.put("uuids", collect);
        Query query = entityManager.createNativeQuery(sql.toString(), "CostSetAndMaterialGoodsDTO");
        Common.setParams(query, params);
        costSetMaterialGood = query.getResultList();
        return costSetMaterialGood;
    }

    @Override
    public List<CostSetDTO> getCostSetAndRevenueAmount(List<UUID> collect, String fromDate, String toDate) {
        StringBuilder sql = new StringBuilder();
        List<CostSetDTO> costSetDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT c.ID                    CostSetID,   " +
            "       c.CostSetCode           CostSetCode,   " +
            "       c.CostSetName           CostSetName,   " +
            "       (SELECT ISNULL(SUM(sad.Amount - sad.DiscountAmount + sad.VATAmount), 0)   " +
            "        FROM SAInvoiceDetail sad   " +
            "                 LEFT JOIN SAInvoice sa ON sa.ID = sad.SAInvoiceID   " +
            "        WHERE sad.CostSetID = c.ID   " +
            "          AND sad.CreditAccount LIKE '511%'   " +
            "          AND sa.PostedDate >= :fromDate  " +
            "          AND sa.PostedDate <= :toDate  " +
            "          AND sa.Recorded = 1) RevenueAmount   " +
            "from CostSet c   " +
            "where ID in :uuids order by c.CostSetCode ");
        params.put("uuids", collect);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        Query query = entityManager.createNativeQuery(sql.toString(), "CostSetDTO");
        Common.setParams(query, params);
        costSetDTOS = query.getResultList();
        return costSetDTOS;
    }

    public static void setParamsWithPageable(@NotNull Query query, Map<String, Object> params, @NotNull Pageable pageable, @NotNull Number total) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
        query.setFirstResult((int)pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }
}
