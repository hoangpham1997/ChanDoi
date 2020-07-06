package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.web.rest.dto.SaBillDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the SaBill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaBillRepository extends JpaRepository<SaBill, UUID>, SaBillRepositoryCustom {

    @Query(value = "select count(*) from SABill where id in ?1", nativeQuery = true)
    Long countByIds(List<UUID> listID);

    @Query(value = "select distinct (b.invoiceseries) from SABill b where b.CompanyID = ?1", nativeQuery = true)
    List<String> getAllSeries(UUID org);

    @Query(value = "select count(1) from SaBill a where a.invoiceNo = ?1 and a.invoiceForm = ?2 and a.invoiceTemplate = ?3 and a.invoiceSeries = ?4 and a.CompanyID = ?5 and id <> ?6", nativeQuery = true)
    int countByInvoiceNoAndCompanyID(String soHd, Integer hinhThucHd, String mauSoHd, String kyHieuHd, UUID org, UUID id);

    @Query(value = "select * from SABill where SABill.ID = ?1", nativeQuery = true)
    Optional<SaBill> findOneById(UUID id);

    @Query(value = "select * from SABill where SABill.ID = (select top 1 sabillid from saReturnDetail where SaReturnID = ?1)", nativeQuery = true)
    Optional<SaBill> findBySaReturnID(UUID id);

    @Query(value = "select * from SABill where SABill.ID = (select top 1 sabillid from saInvoiceDetail where SaInvoiceID = ?1)", nativeQuery = true)
    Optional<SaBill> findBySaInvoiceID(UUID id);

    @Query(value = "select count(1) from SAInvoice sa left join  SAInvoiceDetail sadtl on sa.ID = sadtl.SAInvoiceID where sadtl.SABillID = ?1 and sa.CompanyID = ?2", nativeQuery = true)
    int checkRelateVoucherSaInvoice(UUID sABillID, UUID org);

    @Query(value = "select count(1) from SAReturn sa left join  SAReturnDetail sadtl on sa.ID = sadtl.SAReturnID where sadtl.SABillID = ?1 and sa.CompanyID = ?2", nativeQuery = true)
    int checkRelateVoucherSAReturn(UUID sABillID, UUID org);

    @Query(value = "select count(1) from PPDiscountReturn sa left join  PPDiscountReturnDetail sadtl on sa.ID = sadtl.PPDiscountReturnID where sadtl.SABillID = ?1 and sa.CompanyID = ?2", nativeQuery = true)
    int checkRelateVoucherPPDiscountReturn(UUID sABillID, UUID org);

    @Modifying
    @Query(value = "delete from SABill where id in ?1", nativeQuery = true)
    void deleteByListId(List<UUID> saBillIds);

    @Query(value = "select count(*) from SABill where id = ?1", nativeQuery = true)
    Long countById(UUID saBillId);

    Long countByInvoiceTemplateAndInvoiceSeriesAndCompanyID(String invoiceTemplate, String invoiceSeries, UUID companyID);

    @Query(value = "select sad.SAINvoiceID from SAInvoiceDetail sad where sadtl.SABillID = ?1", nativeQuery = true)
    List<UUID> getSAInvoiceBySABillID(UUID saBillID);
}
