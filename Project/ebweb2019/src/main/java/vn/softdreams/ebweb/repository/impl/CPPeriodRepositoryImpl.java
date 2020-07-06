package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.CPOPN;
import vn.softdreams.ebweb.repository.CPOPNRepositoryCustom;
import vn.softdreams.ebweb.repository.CPPeriodRepositoryCustom;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.CPPeriodDTO;
import vn.softdreams.ebweb.web.rest.dto.CPUncompleteDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceViewDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class CPPeriodRepositoryImpl implements CPPeriodRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<CPPeriodDTO> findAllByType(Pageable pageable, Integer type, UUID org, Integer typeLedger, Boolean isExport) {
        Map<String, Object> params = new HashMap<>();
        List<CPPeriodDTO> cpPeriodDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        if (type.equals(vn.softdreams.ebweb.service.util.Constants.GiaThanh.GIAN_DON) || type.equals(vn.softdreams.ebweb.service.util.Constants.GiaThanh.HE_SO) || type.equals(vn.softdreams.ebweb.service.util.Constants.GiaThanh.TY_LE)) {
            sqlSelect.append("SELECT cp.ID, " +
                "       cp.ToDate, " +
                "       cp.FromDate, " +
                "       cp.Name, " +
                "       CASE WHEN (SELECT COUNT(*) FROM CPPeriod precp WHERE precp.ToDate < cp.FromDate AND precp.Type = cp.Type AND precp.CompanyID = :org) > 0 " +
                "           THEN (SELECT TOP 1 ISNULL(SUM(ISNULL(precpu.TotalCostAmount, 0)), 0) FROM CPPeriod precp " +
                "           LEFT JOIN CPUncompleteDetail precpu ON precpu.CPPeriodID = precp.ID " +
                "           WHERE precp.ToDate < cp.FromDate AND precp.Type = cp.Type AND precp.CompanyID = cp.CompanyID ORDER BY cp.ToDate DESC ) " +
                "           ELSE (SELECT ISNULL(SUM(ISNULL(cpopn.TotalCostAmount, 0)), 0) FROM CPOPN cpopn " +
                "           LEFT JOIN CPPeriodDetail cpd ON cpopn.CostSetID = cpd.CostSetID " +
                "           WHERE cpd.CPPeriodID = cp.ID) END as TotalIncompleteOpenning, " +
                "       ((SELECT ISNULL(SUM(ISNULL(cpe0.Amount, 0)), 0) FROM CPExpenseList cpe0 WHERE cpe0.CPPeriodID = cp.ID AND cpe0.TypeVoucher = 0) - " +
                "        (SELECT ISNULL(SUM(ISNULL(cpe1.Amount, 0)), 0) FROM CPExpenseList cpe1 WHERE cpe1.CPPeriodID = cp.ID AND cpe1.TypeVoucher = 1) + " +
                "        (SELECT ISNULL(SUM(ISNULL(cpa.AllocatedAmount, 0)), 0) FROM CPAllocationGeneralExpenseDetail cpa WHERE cpa.CPPeriodID = cp.ID))  as TotalAmountInPeriod, " +
                "       (SELECT ISNULL(SUM(ISNULL(cpu.TotalCostAmount, 0)), 0) FROM CPUncompleteDetail cpu WHERE cpu.CPPeriodID = cp.ID)    as TotalIncompleteClosing, " +
                "       (SELECT ISNULL(SUM(ISNULL(cpr.TotalCostAmount, 0)), 0) FROM CPResult cpr WHERE cpr.CPPeriodID = cp.ID)         as TotalCost, " +
                "       cp.Type  as  type " +
                "FROM CPPeriod cp " +
                "WHERE cp.CompanyID = :org " +
                "  AND (cp.TypeLedger = :typeLedger OR cp.TypeLedger = 2) " +
                "  AND Type = :type Order By cp.FromDate DESC , cp.ToDate DESC");
        } else {
            sqlSelect.append("SELECT cp.ID, " +
                "       cp.ToDate, " +
                "       cp.FromDate, " +
                "       cp.Name, " +
                "       CASE " +
                "           WHEN (SELECT COUNT(*) " +
                "                 FROM CPPeriod precp " +
                "                 WHERE precp.ToDate < cp.FromDate " +
                "                   AND precp.Type = cp.Type " +
                "                   AND precp.CompanyID = :org) > 0 " +
                "               THEN (CASE " +
                "                         WHEN (SELECT COUNT(*) " +
                "                               FROM CPAcceptanceDetail precpu " +
                "                               WHERE precpu.CPPeriodID = (SELECT TOP 1 precp.ID " +
                "                                                          FROM CPPeriod precp " +
                "                                                          WHERE precp.ToDate < cp.FromDate " +
                "                                                            AND precp.Type = cp.Type " +
                "                                                            AND precp.CompanyID = :org " +
                "                                                          ORDER BY precp.ToDate DESC) " +
                "                                 AND precpu.CPAcceptanceID IS NOT NULL) = 0 " +
                "                             THEN (SELECT ISNULL(SUM(ISNULL(precpu.Amount, 0)), 0) " +
                "                                   FROM CPAcceptanceDetail precpu " +
                "                                   WHERE precpu.CPPeriodID = (SELECT TOP 1 precp.ID " +
                "                                                              FROM CPPeriod precp " +
                "                                                              WHERE precp.ToDate < cp.FromDate " +
                "                                                                AND precp.Type = cp.Type " +
                "                                                                AND precp.CompanyID = :org " +
                "                                                              ORDER BY precp.ToDate DESC)) " +
                "                         ELSE ((SELECT ISNULL(SUM(ISNULL(precpu.Amount, 0)), 0) " +
                "                                FROM CPAcceptanceDetail precpu " +
                "                                WHERE precpu.CPPeriodID = (SELECT TOP 1 precp.ID " +
                "                                                           FROM CPPeriod precp " +
                "                                                           WHERE precp.ToDate < cp.FromDate " +
                "                                                             AND precp.Type = cp.Type " +
                "                                                             AND precp.CompanyID = :org " +
                "                                                           ORDER BY precp.ToDate DESC) " +
                "                                  AND precpu.CPAcceptanceID IS NULL) - " +
                "                               (SELECT ISNULL(SUM(ISNULL(pre.TotalAcceptedAmount, 0)), 0) " +
                "                                FROM (SELECT * " +
                "                                      FROM CPAcceptanceDetail precpu " +
                "                                      WHERE precpu.CPPeriodID = (SELECT TOP 1 precp.ID " +
                "                                                                 FROM CPPeriod precp " +
                "                                                                 WHERE precp.ToDate < cp.FromDate " +
                "                                                                   AND precp.Type = cp.Type " +
                "                                                                   AND precp.CompanyID = :org " +
                "                                                                 ORDER BY precp.ToDate DESC) " +
                "                                        AND precpu.CPAcceptanceID IS NOT NULL) pre)) END) " +
                "           ELSE (SELECT ISNULL(SUM(ISNULL(cpopn.NotAcceptedAmount, 0)), 0) " +
                "                 FROM CPOPN cpopn " +
                "                          LEFT JOIN CPPeriodDetail cpd ON cpopn.CostSetID = cpd.CostSetID " +
                "                 WHERE cpd.CPPeriodID = cp.ID) END                           as TotalIncompleteOpenning, " +
                "       ((SELECT ISNULL(SUM(ISNULL(cpe0.Amount, 0)), 0) " +
                "         FROM CPExpenseList cpe0 " +
                "         WHERE cpe0.CPPeriodID = cp.ID " +
                "           AND cpe0.TypeVoucher = 0) - " +
                "        (SELECT ISNULL(SUM(ISNULL(cpe1.Amount, 0)), 0) " +
                "         FROM CPExpenseList cpe1 " +
                "         WHERE cpe1.CPPeriodID = cp.ID " +
                "           AND cpe1.TypeVoucher = 1) + " +
                "        (SELECT ISNULL(SUM(ISNULL(cpa.AllocatedAmount, 0)), 0) " +
                "         FROM CPAllocationGeneralExpenseDetail cpa " +
                "         WHERE cpa.CPPeriodID = cp.ID))                                      as TotalAmountInPeriod, " +
                "       ((SELECT SUM(ad.Amount) " +
                "         FROM (Select cad.CostSetID, MAX(cad.Amount) Amount " +
                "               from CPAcceptanceDetail cad " +
                "               where cad.CPPeriodID = cp.ID " +
                "               group by cad.CostSetID) ad) - (SELECT ISNULL(SUM(ISNULL(cpr.TotalAmountOriginal, 0)), 0) " +
                "                                              FROM CPAcceptance cpr " +
                "                                              WHERE cpr.CPPeriodID = cp.ID)) as TotalIncompleteClosing, " +
                "       (SELECT ISNULL(SUM(ISNULL(cpr.TotalAmountOriginal, 0)), 0) " +
                "        FROM CPAcceptance cpr " +
                "        WHERE cpr.CPPeriodID = cp.ID)                                        as TotalCost, " +
                "       cp.Type                                                               as type " +
                "FROM CPPeriod cp " +
                "WHERE cp.CompanyID = :org " +
                "  AND (cp.TypeLedger = :typeLedger OR cp.TypeLedger = 2) " +
                "  AND Type = :type " +
                "Order By cp.FromDate DESC, cp.ToDate DESC");
        }
        params.put("typeLedger", typeLedger);
        params.put("org", org);
        params.put("type", type);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPPeriodDTO");
            if (isExport) {
                Common.setParams(query, params);
            } else {
                Common.setParamsWithPageable(query, params, pageable, total);
            }
            cpPeriodDTOS = query.getResultList();
        }
        return new PageImpl<>(cpPeriodDTOS, pageable, total.longValue());
    }

    @Override
    public Integer findPeriod(String fromDate, String toDate, List<UUID> costSetIDs, Integer type) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("SELECT COUNT(*) FROM CPPeriod cp LEFT JOIN CPPeriodDetail cpd ON cp.ID = cpd.CPPeriodID " +
            "where cp.Type = :type AND cpd.CostSetID in :costSetIDs AND ((cp.FromDate <= :fromDate AND cp.ToDate >= :fromDate) OR (cp.FromDate <= :toDate AND cp.ToDate >= :toDate) " +
            "OR (cp.FromDate >= :fromDate AND cp.ToDate <= :toDate))");
        params.put("costSetIDs", costSetIDs);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("type", type);
        Query countQuery = entityManager.createNativeQuery(sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        return total.intValue();
    }

    @Override
    public List<CPPeriodDetailDTO> getAllCPPeriodDetailByCPPeriodID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<CPPeriodDetailDTO> cpPeriodDetailDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select cpd.id         id, " +
            "       cpd.CPPeriodID CPPeriodID, " +
            "       cpd.CostSetID  CostSetID, " +
            "       c.CostSetCode  CostSetCode, " +
            "       c.CostSetName  CostSetName, " +
            "       c.CostSetType  CostSetType " +
            "from CPPeriodDetail cpd " +
            "         LEFT JOIN CostSet c ON c.ID = cpd.CostSetID " +
            "where cpd.CPPeriodID = :id order by c.CostSetCode");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPPeriodDetailDTO");
            Common.setParams(query, params);
            cpPeriodDetailDTOS = query.getResultList();
        }
        return cpPeriodDetailDTOS;
    }

    @Override
    public List<CPExpenseListDTO> getAllCPExpenseListByCPPeriodID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<CPExpenseListDTO> cpExpenseListDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select cpe.id            id, " +
            "       cpe.CPPeriodID    CPPeriodID, " +
            "       cpe.TypeVoucher   TypeVoucher, " +
            "       cpe.CostSetID     CostSetID, " +
            "       c.CostSetCode     CostSetCode, " +
            "       cpe.TypeID        TypeID, " +
            "       cpe.Date          Date, " +
            "       cpe.PostedDate    PostedDate, " +
            "       cpe.no            no, " +
            "       cpe.Description   Description, " +
            "       cpe.Amount        Amount, " +
            "       cpe.AccountNumber AccountNumber, " +
            "       cpe.ExpenseItemID ExpenseItemID, " +
            "       e.ExpenseItemCode ExpenseItemCode, " +
            "       t.typeGroupID typeGroupID, " +
            "       cpe.refID2 refID2 " +
            "from CPExpenseList cpe " +
            "         LEFT JOIN CostSet c ON c.ID = cpe.CostSetID " +
            "         LEFT JOIN ExpenseItem e ON e.ID = cpe.ExpenseItemID " +
            "         LEFT JOIN Type t ON t.ID = cpe.TypeID " +
            "where cpe.CPPeriodID = :id order by cpe.Date DESC, cpe.PostedDate DESC, cpe.no DESC");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPExpenseListDTO");
            Common.setParams(query, params);
            cpExpenseListDTOS = query.getResultList();
        }
        return cpExpenseListDTOS;
    }

    @Override
    public List<CPAllocationExpenseDTO> getAllCPAllocationExpenseByCPPeriodID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<CPAllocationExpenseDTO> cpAllocationExpenseDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select cpa.id                id, " +
            "       cpa.CPPeriodID        CPPeriodID, " +
            "       cpa.AccountNumber     AccountNumber, " +
            "       cpa.ExpenseItemID     ExpenseItemID, " +
            "       e.ExpenseItemCode     ExpenseItemCode, " +
            "       cpa.TotalCost         TotalCost, " +
            "       cpa.UnallocatedAmount UnallocatedAmount, " +
            "       cpa.AllocatedRate     AllocatedRate, " +
            "       cpa.AllocatedAmount   AllocatedAmount, " +
            "       cpa.AllocationMethod  AllocationMethod, " +
            "       cpa.RefDetailID       RefDetailID, " +
            "       cpa.RefID             RefID " +
            "from CPAllocationGeneralExpense cpa " +
            "         LEFT JOIN ExpenseItem e ON e.ID = cpa.ExpenseItemID " +
            "where cpa.CPPeriodID = :id order by cpa.AccountNumber, e.ExpenseItemCode");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPAllocationExpenseDTO");
            Common.setParams(query, params);
            cpAllocationExpenseDTOS = query.getResultList();
        }
        return cpAllocationExpenseDTOS;
    }

    @Override
    public List<CPAllocationExpenseDetailDTO> getAllCPAllocationExpenseDetailByCPPeriodID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<CPAllocationExpenseDetailDTO> cpPeriodDetailDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select cpa.id                           id, " +
            "       cpa.CPPeriodID                   CPPeriodID, " +
            "       cpa.CPAllocationGeneralExpenseID CPAllocationGeneralExpenseID, " +
            "       cpa.CostSetID                    CostSetID, " +
            "       c.CostSetCode                    CostSetCode, " +
            "       c.CostSetName                    CostSetName, " +
            "       cpa.AccountNumber                AccountNumber, " +
            "       cpa.ExpenseItemID                ExpenseItemID, " +
            "       e.ExpenseItemCode                ExpenseItemCode, " +
            "       cpa.AllocatedRate                AllocatedRate, " +
            "       cpa.AllocatedAmount              AllocatedAmount, " +
            "       e.ExpenseType                    expenseItemType " +
            "from CPAllocationGeneralExpenseDetail cpa " +
            "         LEFT JOIN CostSet c ON c.ID = cpa.CostSetID " +
            "         LEFT JOIN ExpenseItem e ON e.ID = cpa.ExpenseItemID " +
            "where cpa.CPPeriodID = :id order by cpa.OrderPriority");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPAllocationExpenseDetailDTO");
            Common.setParams(query, params);
            cpPeriodDetailDTOS = query.getResultList();
        }
        return cpPeriodDetailDTOS;
    }

    @Override
    public List<CPUncompletesDTO> getAllCPUncompleteByCPPeriodID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<CPUncompletesDTO> cpPeriodDetailDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select cpu.id              id, " +
            "       cpu.CPPeriodID      CPPeriodID, " +
            "       cpu.QuantumID       QuantumID, " +
            "       m.MaterialGoodsCode QuantumCode, " +
            "       m.MaterialGoodsName QuantumName, " +
            "       cpu.CostSetID       CostSetID, " +
            "       c.CostSetCode       CostSetCode, " +
            "       cpu.UncompleteType  UncompleteType, " +
            "       cpu.QuantityClosing QuantityClosing, " +
            "       cpu.Rate            Rate " +
            "from CPUncomplete cpu " +
            "         LEFT JOIN CostSet c ON c.ID = cpu.CostSetID " +
            "         LEFT JOIN MaterialGoods m ON m.ID = cpu.QuantumID " +
            "where CPPeriodID = :id order by c.CostSetCode, m.MaterialGoodsCode");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPUncompleteDTO");
            Common.setParams(query, params);
            cpPeriodDetailDTOS = query.getResultList();
        }
        return cpPeriodDetailDTOS;
    }

    @Override
    public List<CPUncompleteDetailDTO> getAllCPUncompleteDetailByCPPeriodID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<CPUncompleteDetailDTO> cpPeriodDetailDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select cpu.id                        id, " +
            "       cpu.cPUncompleteID            cPUncompleteID, " +
            "       cpu.cPPeriodID                cPPeriodID, " +
            "       cpu.costSetID                 costSetID, " +
            "       c.costSetCode                 costSetCode, " +
            "       c.costSetName                 costSetName, " +
            "       cpu.directMatetialAmount      directMatetialAmount, " +
            "       cpu.directLaborAmount         directLaborAmount, " +
            "       cpu.machineMatetialAmount     machineMatetialAmount, " +
            "       cpu.machineLaborAmount        machineLaborAmount, " +
            "       cpu.machineToolsAmount        machineToolsAmount, " +
            "       cpu.machineDepreciationAmount machineDepreciationAmount, " +
            "       cpu.machineServiceAmount      machineServiceAmount, " +
            "       cpu.machineGeneralAmount      machineGeneralAmount, " +
            "       cpu.generalMatetialAmount     generalMatetialAmount, " +
            "       cpu.generalLaborAmount        generalLaborAmount, " +
            "       cpu.generalToolsAmount        generalToolsAmount, " +
            "       cpu.generalDepreciationAmount generalDepreciationAmount, " +
            "       cpu.generalServiceAmount      generalServiceAmount, " +
            "       cpu.otherGeneralAmount        otherGeneralAmount, " +
            "       cpu.totalCostAmount           totalCostAmount " +
            "from CPUncompleteDetail cpu " +
            "         LEFT JOIN CostSet c ON c.ID = cpu.CostSetID " +
            "where cpu.CPPeriodID = :id " +
            "order by c.CostSetCode");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPUncompleteDetailDTO");
            Common.setParams(query, params);
            cpPeriodDetailDTOS = query.getResultList();
        }
        return cpPeriodDetailDTOS;
    }

    @Override
    public List<CPResultDTO> getAllCPResultByCPPeriodID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<CPResultDTO> cpPeriodDetailDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select cpr.id                        id, " +
            "       cpr.cPPeriodID                cPPeriodID, " +
            "       cpr.costSetID                 costSetID, " +
            "       c.costSetCode                 costSetCode, " +
            "       cpr.MaterialGoodsID           MaterialGoodsID, " +
            "       m.MaterialGoodsCode           MaterialGoodsCode, " +
            "       m.MaterialGoodsName           MaterialGoodsName, " +
            "       cpr.directMatetialAmount      directMatetialAmount, " +
            "       cpr.directLaborAmount         directLaborAmount, " +
            "       cpr.machineMatetialAmount     machineMatetialAmount, " +
            "       cpr.machineLaborAmount        machineLaborAmount, " +
            "       cpr.machineToolsAmount        machineToolsAmount, " +
            "       cpr.machineDepreciationAmount machineDepreciationAmount, " +
            "       cpr.machineServiceAmount      machineServiceAmount, " +
            "       cpr.machineGeneralAmount      machineGeneralAmount, " +
            "       cpr.generalMatetialAmount     generalMatetialAmount, " +
            "       cpr.generalLaborAmount        generalLaborAmount, " +
            "       cpr.generalToolsAmount        generalToolsAmount, " +
            "       cpr.generalDepreciationAmount generalDepreciationAmount, " +
            "       cpr.generalServiceAmount      generalServiceAmount, " +
            "       cpr.otherGeneralAmount        otherGeneralAmount, " +
            "       cpr.totalCostAmount           totalCostAmount, " +
            "       cpr.TotalQuantity             TotalQuantity, " +
            "       cpr.UnitPrice                 UnitPrice, " +
            "       cpr.Coefficien                Coefficien " +
            "from CPResult cpr " +
            "         LEFT JOIN CostSet c ON c.ID = cpr.CostSetID " +
            "         LEFT JOIN MaterialGoods m ON m.ID = cpr.MaterialGoodsID " +
            "where CPPeriodID = :id " +
            "order by c.CostSetCode, m.MaterialGoodsCode");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPResultDTO");
            Common.setParams(query, params);
            cpPeriodDetailDTOS = query.getResultList();
        }
        return cpPeriodDetailDTOS;
    }

    @Override
    public List<CPAllocationRateDTO> getAllCPAllocationRateByCPPeriodID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<CPAllocationRateDTO> cpPeriodDetailDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select cpa.id                 id, " +
            "       cpa.cPPeriodID         cPPeriodID, " +
            "       cpa.allocationMethod   allocationMethod, " +
            "       cpa.costSetID          costSetID, " +
            "       c.costSetCode          costSetCode, " +
            "       cpa.materialGoodsID    materialGoodsID, " +
            "       m.materialGoodsCode    materialGoodsCode, " +
            "       m.materialGoodsName    materialGoodsName, " +
            "       cpa.isStandardItem     isStandardItem, " +
            "       cpa.quantity           quantity, " +
            "       cpa.priceQuantum       priceQuantum, " +
            "       cpa.coefficient        coefficient, " +
            "       cpa.quantityStandard   quantityStandard, " +
            "       cpa.allocationStandard allocationStandard, " +
            "       cpa.allocatedRate      allocatedRate " +
            "from CPAllocationRate cpa " +
            "         LEFT JOIN CostSet c ON c.ID = cpa.CostSetID " +
            "         LEFT JOIN MaterialGoods m ON m.ID = cpa.MaterialGoodsID " +
            "where CPPeriodID = :id " +
            "order by c.CostSetCode, m.MaterialGoodsCode");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPAllocationRateDTO1");
            Common.setParams(query, params);
            cpPeriodDetailDTOS = query.getResultList();
        }
        return cpPeriodDetailDTOS;
    }

    @Override
    public List<CPAcceptanceDTO> getAllPAcceptanceByCPPeriodID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<CPAcceptanceDTO> cpPeriodDetailDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select cpa.ID                  id,   " +
            "       cpa.TypeID              TypeID,   " +
            "       cpa.Date                Date,   " +
            "       cpa.PostedDate          PostedDate,   " +
            "       cpa.No                  No,   " +
            "       cpa.Description         Description,   " +
            "       cpa.cPPeriodID          cPPeriodID,   " +
            "       cpa.totalAmount         totalAmount,   " +
            "       cpa.totalAmountOriginal totalAmountOriginal   " +
            "From CPAcceptance cpa   " +
            "where cpa.CPPeriodID = :id");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPAcceptanceDTO");
            Common.setParams(query, params);
            cpPeriodDetailDTOS = query.getResultList();
        }
        return cpPeriodDetailDTOS;
    }

    @Override
    public List<CPAcceptanceDetailDTO> getAllCPAcceptanceDetailByCPPeriodID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<CPAcceptanceDetailDTO> cpPeriodDetailDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select cpa.id                  id,   " +
            "       cpa.cPAcceptanceID      cPAcceptanceID,   " +
            "       cpa.description         description,   " +
            "       cpa.debitAccount        debitAccount,   " +
            "       cpa.creditAccount       creditAccount,   " +
            "       cpa.revenueAmount       revenueAmount,   " +
            "       cpa.amount              amount,   " +
            "       cpa.acceptedRate        acceptedRate,   " +
            "       cpa.totalAcceptedAmount totalAcceptedAmount,   " +
            "       cpa.expenseItemID       expenseItemID,   " +
            "       cpa.statisticsCodeID    statisticsCodeID,   " +
            "       cpa.cPPeriodID          cPPeriodID,   " +
            "       cpa.costSetID           costSetID,   " +
            "       c.costSetCode           costSetCode,   " +
            "       c.costSetName           costSetName   " +
            "from CPAcceptanceDetail cpa   " +
            "         LEFT JOIN CostSet c ON c.ID = cpa.CostSetID   " +
            "where cpa.CPPeriodID = :id and cpa.CPAcceptanceID is null " +
            "order by c.CostSetCode");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPAcceptanceDetailDTO");
            Common.setParams(query, params);
            cpPeriodDetailDTOS = query.getResultList();
        }
        return cpPeriodDetailDTOS;
    }

    @Override
    public CPPeriodDTO getByID(UUID id) {
        Map<String, Object> params = new HashMap<>();
        CPPeriodDTO cpPeriodDTO = new CPPeriodDTO();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select top (1) cp.id       id,  " +
            "               cp.FromDate FromDate,  " +
            "               cp.ToDate   ToDate,  " +
            "               cp.Name     Name,  " +
            "               cp.Type     Type  " +
            "from CPPeriod cp  " +
            "where ID = :id");
        params.put("id", id);
        Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPPeriodDTO1");
        Common.setParams(query, params);
        cpPeriodDTO = (CPPeriodDTO)query.getSingleResult();
        return cpPeriodDTO;
    }

    @Override
    public void deleteCPPeriod(List<UUID> uuids) {
        String sql1 = "Delete CPPeriod WHERE id = ?;"+
            "Delete CPPeriodDetail WHERE CPPeriodID = ?;"+
            "Delete CPExpenseList WHERE CPPeriodID = ?;"+
            "Delete CPAllocationGeneralExpense WHERE CPPeriodID = ?;"+
            "Delete CPAllocationGeneralExpenseDetail WHERE CPPeriodID = ?;"+
            "Delete CPUncomplete WHERE CPPeriodID = ?;"+
            "Delete CPUncompleteDetail WHERE CPPeriodID = ?;"+
            "Delete CPAllocationRate WHERE CPPeriodID = ?;"+
            "Delete CPAcceptanceDetail WHERE CPPeriodID = ?;"+
            "Delete CPAcceptance WHERE CPPeriodID = ?;"+
            "Delete CPResult WHERE CPPeriodID = ?;";
        jdbcTemplate.batchUpdate(sql1, uuids, Constants.BATCH_SIZE, (ps, detail) -> {
            ps.setString(1, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(2, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(3, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(4, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(5, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(6, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(7, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(8, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(9, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(10, Utils.uuidConvertToGUID(detail).toString());
            ps.setString(11, Utils.uuidConvertToGUID(detail).toString());
        });
    }

    @Override
    public List<CPAcceptanceDetailDTO> getAllCPAcceptanceDetailByCPPeriodIDSecond(UUID id) {
        Map<String, Object> params = new HashMap<>();
        List<CPAcceptanceDetailDTO> cpPeriodDetailDTOS = new ArrayList<>();
        StringBuilder sqlSelect = new StringBuilder();
        sqlSelect.append("Select newid()                id, " +
            "       null                   cPAcceptanceID, " +
            "       null                   description, " +
            "       null                   debitAccount, " +
            "       null                   creditAccount, " +
            "       MAX(cpa.revenueAmount) revenueAmount, " +
            "       MAX(cpa.amount) - SUM(TotalAcceptedAmount)        amount, " +
            "       100                    acceptedRate, " +
            "       MAX(cpa.amount) - SUM(TotalAcceptedAmount)         totalAcceptedAmount, " +
            "       null                   expenseItemID, " +
            "       null                   statisticsCodeID, " +
            "       cpa.cPPeriodID         cPPeriodID, " +
            "       cpa.costSetID          costSetID, " +
            "       c.costSetCode          costSetCode, " +
            "       c.costSetName          costSetName " +
            "from CPAcceptanceDetail cpa " +
            "         LEFT JOIN CostSet c ON c.ID = cpa.CostSetID " +
            "where cpa.CPPeriodID = :id and cpa.CPAcceptanceID is not null " +
            "group by cpa.CostSetID, c.CostSetName, c.CostSetCode, cpa.CPPeriodID " +
            "order by c.CostSetCode");
        params.put("id", id);
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(1) " + sqlSelect.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery( sqlSelect.toString(), "CPAcceptanceDetailDTO");
            Common.setParams(query, params);
            cpPeriodDetailDTOS = query.getResultList();
        }
        return cpPeriodDetailDTOS;
    }
}
