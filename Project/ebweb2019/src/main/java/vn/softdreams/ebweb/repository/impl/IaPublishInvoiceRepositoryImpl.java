package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.IaPublishInvoiceRepositoryCustom;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IaPublishInvoiceRepositoryImpl implements IaPublishInvoiceRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;


    @Override
    public Long checkExistedNoRange(String fromNo, String toNo, UUID iaReportID, UUID iaPublishInvoiceDetailID, String invoiceSeries) {
        StringBuilder sql = new StringBuilder("  select count(*) ")
            .append(" from ( ")
            .append(" SELECT cast(FromNo as numeric) fromno, cast(ToNo as numeric) tono ")
            .append(" from IAPublishInvoiceDetail ")
            .append(" where IAReportID = :iaReportID ")
            .append(iaPublishInvoiceDetailID != null ? " and id != :iaPublishInvoiceDetailID " : "")
            .append(" ) a ")
            .append(" where (:fromNo >= a.fromno and :fromNo <= a.tono) ")
            .append(" or (:toNo >= a.fromno and :toNo <= a.tono) ");
        Map<String, Object> params = new HashMap<>();
        if (!Strings.isNullOrEmpty(fromNo)) {
            params.put("fromNo", Integer.parseInt(fromNo, 10));
        }
        if (!Strings.isNullOrEmpty(toNo)) {
            params.put("toNo", Integer.parseInt(toNo, 10));
        }
        if (iaReportID != null) {
            params.put("iaReportID", iaReportID);
        }
        if (iaPublishInvoiceDetailID != null) {
            params.put("iaPublishInvoiceDetailID", iaPublishInvoiceDetailID);
        }
        Query countQuery = entityManager.createNativeQuery(sql.toString());
        Common.setParams(countQuery, params);
        Number total = (Number) countQuery.getSingleResult();
        return total.longValue();
    }
}
