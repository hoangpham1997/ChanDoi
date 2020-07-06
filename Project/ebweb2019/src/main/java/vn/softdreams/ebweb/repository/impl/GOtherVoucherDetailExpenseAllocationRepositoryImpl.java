package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.domain.GOtherVoucherDetailExpenseAllocation;
import vn.softdreams.ebweb.repository.GOtherVoucherDetailExpenseAllocationRepositoryCustom;
import vn.softdreams.ebweb.repository.GOtherVoucherDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailExpenseAllocationViewDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class GOtherVoucherDetailExpenseAllocationRepositoryImpl implements GOtherVoucherDetailExpenseAllocationRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<GOtherVoucherDetailExpenseAllocationViewDTO> findAllByGOtherVoucherViewID(UUID id) {
        StringBuilder sql = new StringBuilder();
        List<GOtherVoucherDetailExpenseAllocationViewDTO> gOtherVoucherDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select godea.id, " +
            "       godea.gothervoucherid, " +
            "       godea.allocationamount, " +
            "       godea.objectid, " +
            "       godea.objecttype, " +
            "       godea.allocationrate, " +
            "       godea.amount, " +
            "       godea.costaccount, " +
            "       godea.expenseitemid, " +
            "       ei.ExpenseItemCode, " +
            "       godea.costsetid, " +
            "       godea.orderpriority, " +
            "       godea.PrepaidExpenseID, " +
            "       pe.PrepaidExpenseCode, " +
            "       pe.PrepaidExpenseName, " +
            "       cs.CostSetCode " +
            " from GOtherVoucherDetailExpenseAllocation godea " +
            "         left join PrepaidExpense pe on godea.PrepaidExpenseID = pe.id " +
            "         left join ExpenseItem ei on godea.ExpenseItemID = ei.ID " +
            "         left join CostSet cs on godea.CostSetID = cs.ID " +
            " where godea.GOtherVoucherID = :gOtherVoucherID " +
            " order by godea.OrderPriority ");
        params.put("gOtherVoucherID", id);
        Query countQuerry = entityManager.createNativeQuery("select count(1) from GOtherVoucherDetailExpenseAllocation where GOtherVoucherID = :gOtherVoucherID" );
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery(sql.toString(), "GOtherVoucherDetailExpenseAllocationViewDTO");
            Common.setParams(query, params);
            gOtherVoucherDTOS = query.getResultList();
        }
        return gOtherVoucherDTOS;
    }

    @Override
    public List<GOtherVoucherDetailExpenseAllocation> findAllByGOtherVoucherID(UUID id) {
        return null;
    }
}
