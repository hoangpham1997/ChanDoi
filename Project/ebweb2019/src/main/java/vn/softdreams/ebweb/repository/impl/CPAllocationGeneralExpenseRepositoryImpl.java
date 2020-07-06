package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.CPAllocationGeneralExpense;
import vn.softdreams.ebweb.repository.CPAllocationGeneralExpenseRepositoryCustom;
import vn.softdreams.ebweb.repository.CPAllocationQuantumRepositoryCustom;
import vn.softdreams.ebweb.service.dto.CPAllocationQuantumDTO;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhAllocationPoPupDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class CPAllocationGeneralExpenseRepositoryImpl implements CPAllocationGeneralExpenseRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<GiaThanhAllocationPoPupDTO> getAllocationPeriod(UUID org, String fromDate, String toDate, List<UUID> costSetID, Integer soLamViec) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT *  " +
            "FROM (Select cpa.*,  " +
            "             gl1.PostedDate as PostedDate,  " +
            "             gl1.Date       as Date,  " +
            "             gl1.NoFBook    as NoFBook,  " +
            "             gl1.NoMBook    as NoMBook,  " +
            "             gl1.Reason     as Reason  " +
            "      from (Select NEWID()                                        id,  " +
            "                   a.AccountNumber                             as AccountNumber,  " +
            "                   a.ExpenseItemID                             as ExpenseItemID,  " +
            "                   e.ExpenseItemCode                           as ExpenseItemCode,  " +
            "                   MAX(a.TotalCost)                            as TotalCost,  " +
            "                   (MAX(a.TotalCost) - SUM(a.AllocatedAmount)) as UnallocatedAmount,  " +
            "                   100                                         as AllocatedRate,  " +
            "                   (MAX(a.TotalCost) - SUM(a.AllocatedAmount)) as AllocatedAmount,  " +
            "                   0                                           as AllocationMethod,  " +
            "                   a.RefID                                     as RefID,  " +
            "                   a.RefDetailID                               as RefDetailID,  " +
            "                   e.ExpenseType                               as ExpenseItemType  " +
            "            from CPAllocationGeneralExpense a  " +
            "                     LEFT JOIN CPPeriod p ON p.ID = a.CPPeriodID  " +
            "                     LEFT JOIN ExpenseItem e on a.ExpenseItemID = e.ID  " +
            "            Where p.CompanyID = :org  " +
            "              AND a.RefDetailID in (select Distinct cpa.RefDetailID  " +
            "                                    from CPPeriod cp  " +
            "                                             LEFT JOIN CPAllocationGeneralExpense cpa on cp.ID = cpa.CPPeriodID  " +
            "                                    WHERE cp.ToDate < :fromDate  " +
            "                                      AND cp.CompanyID = :org)  " +
            "            GROUP BY a.RefID, a.RefDetailID, a.AccountNumber, a.ExpenseItemID, e.ExpenseItemCode, e.ExpenseType)  " +
            "               as cpa  " +
            "               LEFT JOIN GeneralLedger gl1 on cpa.RefDetailID = gl1.ReferenceID  " +
            "      where cpa.AllocatedAmount > 0  " +
            "      union  " +
            "      select NEWID()                                                   id,  " +
            "             gl.Account                                             as accountNumber,  " +
            "             gl.ExpenseItemID                                       as ExpenseItemID,  " +
            "             ei.ExpenseItemCode                                     as ExpenseItemCode,  " +
            "             DebitAmount                                            as totalCost,  " +
            "             DebitAmount - (Select ISNULL(Sum(ISNULL(cpal.AllocatedAmount, 0)), 0)  " +
            "                            from CPAllocationGeneralExpense cpal  " +
            "                            where cpal.RefDetailID = gl.detailID) as unallocatedAmount,  " +
            "             100                                                    as allocatedRate,  " +
            "             DebitAmount - (Select ISNULL(Sum(ISNULL(cpal.AllocatedAmount, 0)), 0)  " +
            "                            from CPAllocationGeneralExpense cpal  " +
            "                            where cpal.RefDetailID = gl.detailID) as allocatedAmount,  " +
            "             0                                                      as allocationMethod,  " +
            "             gl.referenceID                                         as refID,  " +
            "             gl.detailID                                            as refDetailID,  " +
            "             ei.ExpenseType                                         as ExpenseItemType,  " +
            "             gl.PostedDate                                          as PostedDate,  " +
            "             gl.Date                                                as Date,  " +
            "             gl.NoFBook                                             as NoFBook,  " +
            "             gl.NoMBook                                             as NoMBook,  " +
            "             gl.Reason                                              as Reason  " +
            "      from GeneralLedger gl  " +
            "               left join ExpenseItem EI  " +
            "                         on gl.ExpenseItemID = EI.id  " +
            "      where (gl.Account like '627%' or  " +
            "             (gl.Account like '154%'  " +
            "                 and gl.ExpenseItemID is not null  " +
            "                 and ei.ExpenseType in (2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)))  " +
            "        and gl.DebitAmount > 0  " +
            "        and gl.CostSetID is null  " +
            "        and gl.CompanyID = :org  " +
            "        and gl.TypeLedger in (:soLamViec, 2)  " +
            "        and gl.PostedDate >= :fromDate  " +
            "        and gl.PostedDate <= :toDate) cpallo  WHERE cpallo.UnallocatedAmount > 0 " +
            "Order By cpallo.AccountNumber, cpallo.ExpenseItemCode ");
        params.put("org", org);
        params.put("soLamViec", soLamViec);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        Query query = entityManager.createNativeQuery(sql.toString(), "GiaThanhAllocationPoPupDTO");
        Common.setParams(query, params);
        return query.getResultList();
    }

    @Override
    public List<GiaThanhAllocationPoPupDTO> getAllocationPeriodSum(UUID org, String fromDate, String toDate, List<UUID> costSetID, Integer phienSoLamViec) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT NEWID()                          id, " +
            "       cpallo.AccountNumber          as AccountNumber, " +
            "       cpallo.ExpenseItemID          as ExpenseItemID, " +
            "       cpallo.ExpenseItemCode        as ExpenseItemCode, " +
            "       SUM(cpallo.TotalCost)         as TotalCost, " +
            "       SUM(cpallo.UnallocatedAmount) as UnallocatedAmount, " +
            "       100                           as AllocatedRate, " +
            "       SUM(cpallo.AllocatedAmount)   as AllocatedAmount, " +
            "       0                             as AllocationMethod, " +
            "       cpallo.ExpenseItemType        as ExpenseItemType " +
            "FROM ((Select cpa.*, " +
            "              gl1.PostedDate as PostedDate, " +
            "              gl1.Date       as Date, " +
            "              gl1.NoFBook    as NoFBook, " +
            "              gl1.NoMBook    as NoMBook, " +
            "              gl1.Reason     as Reason " +
            "       from (Select NEWID()                                        id, " +
            "                    a.AccountNumber                             as AccountNumber, " +
            "                    a.ExpenseItemID                             as ExpenseItemID, " +
            "                    e.ExpenseItemCode                           as ExpenseItemCode, " +
            "                    MAX(a.TotalCost)                            as TotalCost, " +
            "                    (MAX(a.TotalCost) - SUM(a.AllocatedAmount)) as UnallocatedAmount, " +
            "                    100                                         as AllocatedRate, " +
            "                    (MAX(a.TotalCost) - SUM(a.AllocatedAmount)) as AllocatedAmount, " +
            "                    0                                           as AllocationMethod, " +
            "                    a.RefID                                     as RefID, " +
            "                    a.RefDetailID                               as RefDetailID, " +
            "                    e.ExpenseType                               as ExpenseItemType " +
            "             from CPAllocationGeneralExpense a " +
            "                      LEFT JOIN CPPeriod p ON p.ID = a.CPPeriodID " +
            "                      LEFT JOIN ExpenseItem e on a.ExpenseItemID = e.ID " +
            "             Where p.CompanyID = :org " +
            "               AND a.RefDetailID in (select Distinct cpa.RefDetailID " +
            "                                     from CPPeriod cp " +
            "                                              LEFT JOIN CPAllocationGeneralExpense cpa on cp.ID = cpa.CPPeriodID " +
            "                                     WHERE cp.ToDate < :fromDate " +
            "                                       AND cp.CompanyID = :org) " +
            "             GROUP BY a.RefID, a.RefDetailID, a.AccountNumber, a.ExpenseItemID, e.ExpenseItemCode, e.ExpenseType) " +
            "                as cpa " +
            "                LEFT JOIN GeneralLedger gl1 on cpa.RefDetailID = gl1.ReferenceID " +
            "       where cpa.AllocatedAmount > 0) " +
            "      union " +
            "      select NEWID()                                                 id, " +
            "             gl.Account                                           as accountNumber, " +
            "             gl.ExpenseItemID                                     as ExpenseItemID, " +
            "             ei.ExpenseItemCode                                   as ExpenseItemCode, " +
            "             DebitAmount                                          as totalCost, " +
            "             DebitAmount - (Select ISNULL(Sum(ISNULL(cpal.AllocatedAmount, 0)), 0) " +
            "                            from CPAllocationGeneralExpense cpal " +
            "                            where cpal.RefDetailID = gl.DetailID) as unallocatedAmount, " +
            "             100                                                  as allocatedRate, " +
            "             DebitAmount - (Select ISNULL(Sum(ISNULL(cpal.AllocatedAmount, 0)), 0) " +
            "                            from CPAllocationGeneralExpense cpal " +
            "                            where cpal.RefDetailID = gl.DetailID) as allocatedAmount, " +
            "             0                                                    as allocationMethod, " +
            "             gl.referenceID                                       as refID, " +
            "             gl.DetailID                                          as refDetailID, " +
            "             ei.ExpenseType                                       as ExpenseItemType, " +
            "             gl.PostedDate                                        as PostedDate, " +
            "             gl.Date                                              as Date, " +
            "             gl.NoFBook                                           as NoFBook, " +
            "             gl.NoMBook                                           as NoMBook, " +
            "             gl.Reason                                            as Reason " +
            "      from GeneralLedger gl " +
            "               left join ExpenseItem EI " +
            "                         on gl.ExpenseItemID = EI.id " +
            "      where (gl.Account like '627%' or " +
            "             (gl.Account like '154%' " +
            "                 and gl.ExpenseItemID is not null " +
            "                 and ei.ExpenseType in (2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13))) " +
            "        and gl.DebitAmount > 0 " +
            "        and gl.CostSetID is null " +
            "        and gl.CompanyID = :org " +
            "        and gl.TypeLedger in (:soLamViec, 2) " +
            "        and gl.PostedDate >= :fromDate " +
            "        and gl.PostedDate <= :toDate) cpallo " +
            "GROUP BY cpallo.AccountNumber, cpallo.ExpenseItemID, cpallo.ExpenseItemCode, cpallo.ExpenseItemType " +
            "Order By cpallo.AccountNumber , cpallo.ExpenseItemCode ");
        params.put("org", org);
        params.put("soLamViec", phienSoLamViec);
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        Query query = entityManager.createNativeQuery(sql.toString(), "GiaThanhAllocationPoPupDTOSum");
        Common.setParams(query, params);
        return query.getResultList();
    }
}
