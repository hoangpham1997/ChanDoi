package vn.softdreams.ebweb.repository.impl;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.repository.SAInvoiceDetailsRepositoryCustom;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDetailOutWardDTO;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnOutWardDTO;
import vn.softdreams.ebweb.service.dto.SAInvoiceDTO;
import vn.softdreams.ebweb.service.dto.SAInvoiceDetailsOutWardDTO;
import vn.softdreams.ebweb.service.util.Common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class SAInvoiceDetailsRepositoryImpl implements SAInvoiceDetailsRepositoryCustom {
    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public Page<SAInvoiceDTO> findAllSAInvoiceDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate, UUID org, String currentBook) {
        List<SAInvoiceDTO> saInvoiceDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select saI.date, saI.PostedDate, iif(:book = 0, saI.NoFBook, saI.NoMBook) as book, saI.Reason, saI.TotalAmount - saI.TotalDiscountAmount + saI.TotalVATAmount as total, saI.id, a.TypeGroupID ");
        sql.append("from SAInvoice saI ");
        sql.append("left join RSInwardOutwardDetail rsd on rsd.SAInvoiceID = saI.ID ");
        sql.append("left join Type a on a.ID = saI.TypeID ");
        sql.append("where saI.IsDeliveryVoucher = 0 ");
        sql.append("and saI.typeID in (320, 321, 322) ");
        sql.append("and saI.RSInwardOutwardID is null ");
        sql.append("and rsd.SAInvoiceID is null ");
        sql.append("and saI.Recorded = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (accountingObject != null) {
            sql.append("and AccountingObjectID = :accountingObject ");
            params.put("accountingObject", accountingObject);
        }
        if (org != null) {
            sql.append("and companyID = :org ");
            params.put("org", org);
        }
        if (!Strings.isNullOrEmpty(currentBook)) {
            sql.append("and saI.typeLedger in (:book, 2) ");
            params.put("book", currentBook);
        }
        if (!Strings.isNullOrEmpty(fromDate)) {
            sql.append("and CONVERT(varchar, saI.PostedDate, 112) >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (!Strings.isNullOrEmpty(toDate)) {
            sql.append("and CONVERT(varchar, saI.PostedDate, 112) <= :toDate ");
            params.put("toDate", toDate);
        }

        Query countQuerry = entityManager.createNativeQuery("SELECT Count(1) from (" + sql.toString() + ") a");
        Common.setParams(countQuerry, params);
        Number total = (Number) countQuerry.getSingleResult();
        if (total.longValue() > 0) {
            sql.append(" order by saI.date desc, saI.postedDate desc, book desc ");
            Query query = entityManager.createNativeQuery(sql.toString(), "SaReturnRSInwardDTO");
            Common.setParamsWithPageable(query, params, pageable, total);
            saInvoiceDTOS = query.getResultList();
        }
        return new PageImpl<>(saInvoiceDTOS, pageable, total.longValue());
    }

    @Override
    public List<SAInvoiceDetailsOutWardDTO> findBySaInvoiceIDOrderByOrderPriority(List<UUID> id, String currentBook) {
        StringBuilder sqlBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sqlBuilder.append("Select sd.*, case when :typeLedger = 0 then sa.NoFBook else sa.NoMBook end as NoBookSaInvoice " +
            "from SAInvoiceDetail sd " +
            "    left join SAInvoice sa on sa.ID = sd.SAInvoiceID " +
            "where sd.SAInvoiceID in :ids " +
            "and sa.TypeLedger in (2, :typeLedger) order by OrderPriority ");
        params.put("typeLedger", currentBook);
        params.put("ids", id);
        List<SAInvoiceDetailsOutWardDTO> saInvoiceDetailsOutWardDTOS = new ArrayList<>();
        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), "SAInvoiceDetailsOutWardDTO");
        Common.setParams(query, params);
        saInvoiceDetailsOutWardDTOS = query.getResultList();
        return saInvoiceDetailsOutWardDTOS;
    }
}
