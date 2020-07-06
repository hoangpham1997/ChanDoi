package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.IaPublishInvoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the IaPublishInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IaPublishInvoiceRepository extends JpaRepository<IaPublishInvoice, UUID>, IaPublishInvoiceRepositoryCustom {

    @Query(value = "SELECT * FROM IAPublishInvoice WHERE CompanyID = ?1 ORDER BY date desc", nativeQuery = true)
    Page<IaPublishInvoice> findAllOrderByDate(Pageable pageable, UUID companyID);

    @Query(value = "SELECT max(cast(ToNo as numeric)) from IAPublishInvoiceDetail where IAReportID = ?1", nativeQuery = true)
    Long findCurrentMaxFromNo(UUID id);
}
