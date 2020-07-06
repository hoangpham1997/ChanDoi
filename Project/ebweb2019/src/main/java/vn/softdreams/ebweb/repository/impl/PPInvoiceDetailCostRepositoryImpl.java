package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.repository.PPInvoiceDetailCostRepositoryCustom;
import vn.softdreams.ebweb.repository.PPInvoiceDetailsRepositoryCustom;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvertDTO;
import vn.softdreams.ebweb.service.dto.PPInvoiceDetailDTO;
import vn.softdreams.ebweb.service.dto.ResultDTO;
import vn.softdreams.ebweb.service.util.Common;
import vn.softdreams.ebweb.web.rest.dto.PPInvoiceDetailCostDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

public class PPInvoiceDetailCostRepositoryImpl implements PPInvoiceDetailCostRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;


    @Override
    public BigDecimal sumTotalAmount(UUID ppServiceId, UUID ppInvoiceId, Boolean isHaiQuan) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("FROM PPInvoiceDetailCost WHERE 1 = 1 ");

        sql.append("AND ppServiceId = :ppServiceId ");
        params.put("ppServiceId", ppServiceId);

        if (ppInvoiceId != null) {
            sql.append("AND ppInvoiceId != :ppInvoiceId ");
            params.put("ppInvoiceId", ppInvoiceId);
        }
        if (isHaiQuan) {
            sql.append("AND CostType = 1 ");
        } else {
            sql.append("AND CostType = 0 ");
        }

        Query countQuery = entityManager.createNativeQuery("SELECT sum(amount) " + sql.toString());
        Common.setParams(countQuery, params);
        return (BigDecimal) countQuery.getSingleResult();
    }

    @Override
    public List<PPInvoiceDetailCostDTO> getDetailCodeByPPInvoiceId(UUID ppInvoiceId) {
        Map<String, Object> params = new HashMap<>();
        List<PPInvoiceDetailCostDTO> ppInvoiceDetailCostDTOS = new ArrayList<>();
        params.put("refId", ppInvoiceId);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("a.id, ");
        sqlBuilder.append("a.costType, ");
        sqlBuilder.append("a.refID, ");
        sqlBuilder.append("a.typeID, ");
        sqlBuilder.append("a.ppServiceID, ");
        sqlBuilder.append("a.accountObjectID, ");
        sqlBuilder.append("a.totalFreightAmount, ");
        sqlBuilder.append("a.amount, ");
        sqlBuilder.append("a.accumulatedAllocateAmount, ");
        sqlBuilder.append("a.totalFreightAmountOriginal, ");
        sqlBuilder.append("a.amountOriginal, ");
        sqlBuilder.append("a.accumulatedAllocateAmountOriginal, ");
        sqlBuilder.append("a.templateID, ");
        sqlBuilder.append("a.orderPriority, ");
        sqlBuilder.append("b.accountingObjectName, ");
        sqlBuilder.append("b.accountingObjectID, ");
        sqlBuilder.append("b.date, ");
        sqlBuilder.append("b.postedDate, ");
        sqlBuilder.append("b.noMBook, ");
        sqlBuilder.append("b.noFBook ");

        sqlBuilder.append("FROM PPINVOICEDETAILCOST a ");
        sqlBuilder.append("LEFT JOIN PPSERVICE b ON a.PPServiceID = b.id  ");

        sqlBuilder.append("WHERE a.refId = :refId ");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "PPInvoiceDetailCostDTO");
        Common.setParams(query, params);
        ppInvoiceDetailCostDTOS = query.getResultList();
        return ppInvoiceDetailCostDTOS;
    }

    /**
     * @Author Hautv
     * @param paymentVoucherID
     * @return
     */
    @Override
    public List<PPInvoiceDetailCostDTO> getPPInvoiceDetailCostByPaymentVoucherID(UUID paymentVoucherID) {
        Map<String, Object> params = new HashMap<>();
        List<PPInvoiceDetailCostDTO> ppInvoiceDetailCostDTOS = new ArrayList<>();
        params.put("paymentVoucherID", paymentVoucherID);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("a.id, ");
        sqlBuilder.append("a.costType, ");
        sqlBuilder.append("a.refID, ");
        sqlBuilder.append("a.typeID, ");
        sqlBuilder.append("a.ppServiceID, ");
        sqlBuilder.append("a.accountObjectID, ");
        sqlBuilder.append("a.totalFreightAmount, ");
        sqlBuilder.append("a.amount, ");
        sqlBuilder.append("a.accumulatedAllocateAmount, ");
        sqlBuilder.append("a.totalFreightAmountOriginal, ");
        sqlBuilder.append("a.amountOriginal, ");
        sqlBuilder.append("a.accumulatedAllocateAmountOriginal, ");
        sqlBuilder.append("a.templateID, ");
        sqlBuilder.append("a.orderPriority, ");
        sqlBuilder.append("b.accountingObjectName, ");
        sqlBuilder.append("b.accountingObjectID, ");
        sqlBuilder.append("b.date, ");
        sqlBuilder.append("b.postedDate, ");
        sqlBuilder.append("b.noMBook, ");
        sqlBuilder.append("b.noFBook ");

        sqlBuilder.append("FROM PPINVOICEDETAILCOST a ");
        sqlBuilder.append("LEFT JOIN PPSERVICE b ON a.PPServiceID = b.id  ");

        sqlBuilder.append("WHERE a.refId = (select ID from PPInvoice where PaymentVoucherID = :paymentVoucherID) ");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "PPInvoiceDetailCostDTO");
        Common.setParams(query, params);
        ppInvoiceDetailCostDTOS = query.getResultList();
        return ppInvoiceDetailCostDTOS;
    }
}

