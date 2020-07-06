package vn.softdreams.ebweb.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.softdreams.ebweb.repository.IaPublishInviceDetailsRepositoryCustom;
import vn.softdreams.ebweb.web.rest.dto.IAPublishInvoiceDetailDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

public class IaPublishInvoiceDetailsRepositoryImpl implements IaPublishInviceDetailsRepositoryCustom {

    @Autowired
    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    @Override
    public List<IAPublishInvoiceDetailDTO> getAllByCompany(UUID org) {
        StringBuilder sql = new StringBuilder("select a.InvoiceTypeID, a.InvoiceForm, a.InvoiceSeries, a.InvoiceTemplate from IAPublishInvoiceDetail a left join IAPublishInvoice b on a.IAPublishInvoiceID = b.id where CompanyID = :companyID GROUP BY a.InvoiceTypeID, a.InvoiceForm, a.InvoiceSeries, a.InvoiceTemplate");
        Query query = entityManager.createNativeQuery(sql.toString(), "IAPublishInvoiceDetailDTO");
        query.setParameter("companyID", org);
        return query.getResultList();
    }

    @Override
    public List<IAPublishInvoiceDetailDTO> getFollowTransferByCompany(UUID org) {
        StringBuilder sql = new StringBuilder("select a.InvoiceTypeID, a.InvoiceForm, a.InvoiceSeries, a.InvoiceTemplate, a.StartUsing ")
            .append(" from IAPublishInvoiceDetail a ")
            .append(" left join IAPublishInvoice b on a.IAPublishInvoiceID = b.id ")
            .append(" left join InvoiceType c on c.ID = a.InvoiceTypeID ")
            .append(" where CompanyID = :companyID ")
            .append(" and InvoiceTypeCode in ('04HGDL', '03XKNB') ")
            .append(" GROUP BY a.InvoiceTypeID, a.InvoiceForm, a.InvoiceSeries, a.InvoiceTemplate, a.StartUsing ");
        Query query = entityManager.createNativeQuery(sql.toString(), "IAPublishInvoiceDetailDTOs");
        query.setParameter("companyID", org);
        List<IAPublishInvoiceDetailDTO> qq = query.getResultList();
        return query.getResultList();
    }
}
