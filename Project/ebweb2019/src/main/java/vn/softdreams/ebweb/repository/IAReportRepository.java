package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.IAReport;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the IAReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IAReportRepository extends JpaRepository<IAReport, UUID>, IAReportRepositoryCustom {

    Page<IAReport> findAllByCompanyIDOrderByReportName(Pageable pageable, UUID org);

    @Query(value = "SELECT count(*) from IAReport WHERE CompanyID = ?1 and InvoiceTemplate = ?2 AND InvoiceSeries = ?3", nativeQuery = true)
    Integer checkDuplicate(UUID org, String invoiceTemplate, String invoiceSeries);

    @Query(value = "SELECT count(*) from IARegisterInvoiceDetail WHERE IAReportID = ?1", nativeQuery = true)
    Integer checkRegistered(UUID id);

    @Query(value = "SELECT count(*) from IAPublishInvoiceDetail WHERE IAReportID = ?1", nativeQuery = true)
    Integer checkPublished(UUID id);

    @Query(value = "SELECT count(*) from IARegisterInvoiceDetail WHERE IAReportID IN ?1", nativeQuery = true)
    Integer checkRegisteredMultiDelete(List<UUID> ids);

    @Query(value = "SELECT count(*) from IAPublishInvoiceDetail WHERE IAReportID IN ?1", nativeQuery = true)
    Integer checkPublishedMultiDelete(List<UUID> ids);

    @Query(value = "SELECT a.* " +
        "FROM IAReport a " +
        "         LEFT JOIN IARegisterInvoiceDetail b ON a.id = b.IAReportID " +
        "WHERE b.IAReportID IS NULL AND a.CompanyID = ?1"
        , nativeQuery = true)
    Page<IAReport> findAllUnRegistered(Pageable pageable, UUID org);

    @Query(value = "SELECT * FROM IAReport WHERE ID in ?1", nativeQuery = true)
    List<IAReport> findAllByIDs(List<UUID> ids);

    @Query(value = "SELECT COUNT(*) FROM IAPublishInvoiceDetail WHERE IAReportID IN ?1", nativeQuery = true)
    Integer checkIsPublishedReport(List<UUID> ids);

    @Query(value = "SELECT a.* " +
        " FROM IAReport a " +
        " left join IARegisterInvoiceDetail b on b.IAReportID = a.ID " +
        " left join IARegisterInvoice c on c.ID = b.IARegisterInvoiceID " +
        " WHERE a.CompanyID = ?1 " +
        " and c.Status = ?2 " +
        " order by a.ReportName", nativeQuery = true)
    Page<IAReport> findAllByCompanyIDOrderByReportNameIsStatus(Pageable pageable, UUID org, Integer status);
}
