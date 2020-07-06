package vn.softdreams.ebweb.repository;

import java.util.UUID;

public interface IaPublishInvoiceRepositoryCustom {
    Long checkExistedNoRange(String fromNo, String toNo, UUID iaReportID, UUID iaPublishInvoiceDetailID, String invoiceSeries);
}
