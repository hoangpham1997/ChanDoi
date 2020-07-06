package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.GOtherVoucherDetailExpenseRepositoryCustom;
import vn.softdreams.ebweb.repository.GOtherVoucherDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.GOtherVoucherDetailExpenseViewDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class GOtherVoucherDetailExpenseRepositoryImpl implements GOtherVoucherDetailExpenseRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<GOtherVoucherDetailExpenseViewDTO> findAllByGOtherVoucherViewID(UUID id) {
        StringBuilder sql = new StringBuilder();
        List<GOtherVoucherDetailExpenseViewDTO> gOtherVoucherDetailExpenseViewDTOS = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        sql.append("select gode.id,  " +
            "       gode.gothervoucherid,  " +
            "       gode.prepaidexpenseid,  " +
            "       gode.amount,  " +
            "       gode.remainingamount,  " +
            "       gode.allocationamount,  " +
            "       gode.orderpriority,  " +
            "       PE.PrepaidExpenseCode,  " +
            "       PE.PrepaidExpenseName  " +
            " from GotherVoucherDetailExpense gode  " +
            "         left join PrepaidExpense PE on gode.prepaidexpenseid = PE.id where gode.GOtherVoucherID = :gOtherVoucherID " +
            " order by OrderPriority ");
        params.put("gOtherVoucherID", id);
        Query countQuerry = entityManager.createNativeQuery("select count(1) from GotherVoucherDetailExpense where GOtherVoucherID = :gOtherVoucherID" );
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            Query query = entityManager.createNativeQuery(sql.toString(), "GOtherVoucherDetailExpenseViewDTO");
            Common.setParams(query, params);
            gOtherVoucherDetailExpenseViewDTOS = query.getResultList();
        }
        return gOtherVoucherDetailExpenseViewDTOS;
    }
}
