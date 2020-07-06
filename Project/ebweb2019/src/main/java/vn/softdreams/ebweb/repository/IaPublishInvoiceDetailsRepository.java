package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.IaPublishInvoiceDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.web.rest.dto.IAPublishInvoiceDetailDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the IaPublishInvoiceDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IaPublishInvoiceDetailsRepository extends JpaRepository<IaPublishInvoiceDetails, Long>, IaPublishInviceDetailsRepositoryCustom {

    @Query(value = "select a.* from IAPublishInvoiceDetail a left join IAPublishInvoice b on a.IAPublishInvoiceID = b.id where CompanyID = ?1", nativeQuery = true)
    List<IaPublishInvoiceDetails> findByIaPublishInvoiceCompanyId(UUID companyID);

    @Query(value = "select * from IaPublishInvoiceDetail a  where a.id in(SELECt b.TemplateID from SABill b  where b.CompanyID = ?1)", nativeQuery = true)
    List<IaPublishInvoiceDetails> getAllTemplate(UUID org);

    @Query(value = "select top 1 a.* from IAPublishInvoiceDetail a left join IAPublishInvoice b on a.IAPublishInvoiceID = b.id where a.invoiceTemplate = ?1 and CompanyID = ?2", nativeQuery = true)
    IaPublishInvoiceDetails countByInvoiceTemplateAndCompanyID(String mauSoHd, UUID org);

    @Query(value = "select count(1) from IAPublishInvoiceDetail a left join IAPublishInvoice b on a.IAPublishInvoiceID = b.id where a.invoiceSeries = ?1 and CompanyID = ?2", nativeQuery = true)
    int countByInvoiceSeriesAndCompanyID(String mauSoHd, UUID org);

    @Query(value = "select a.* from IAPublishInvoiceDetail a left join IAPublishInvoice b on a.IAPublishInvoiceID = b.id where a.invoiceForm = ?1 and a.invoiceTypeId = ?2 AND a.invoiceTemplate = ?3 AND a.invoiceSeries = ?4 and CompanyID = ?5", nativeQuery = true)
    List<IaPublishInvoiceDetails> findFirstByInvoiceFormAndInvoiceTypeIdAndInvoiceTemplateAndInvoiceSeriesAndOrg(Integer hinhThucHd, UUID id, String mauSoHd, String kyHieuHd, UUID org);

    @Query(value = "select * from IAPublishInvoiceDetail where IAPublishInvoiceID = ?1 Order by OrderPriority ", nativeQuery = true)
    List<IaPublishInvoiceDetails> findByIaPublishInvoiceID(UUID id);

}
