package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.EInvoice;

import java.util.List;
import java.util.UUID;

@Repository
public interface EInvoiceRepository extends JpaRepository<EInvoice, UUID>, EInvoiceRepositoryCustom {
    // ORDER BY (CASE WHEN ([InvoiceNo] IS NULL or [InvoiceNo] = '') THEN 0 ELSE 1 END), [InvoiceNo] desc , InvoiceDate desc
    @Query(
        value = "select * from viewEinvoice where companyID = ?1 and StatusInvoice != 6 " +
            "ORDER BY case when InvoiceNo is null or InvoiceNo = '' then 0 else 1 end, case when InvoiceNo is not null then InvoiceTemplate end, case when InvoiceNo is not null then InvoiceSeries end ,InvoiceNo DESC, RefDateTime desc", nativeQuery = true
    )
    Page<EInvoice> findAllByCompanyID(Pageable pageable, UUID companyID);

    @Query(
        value = "select SUM(TotalAmount) - SUM(TotalDiscountAmount) + SUM(TotalVATAmount) from viewEinvoice where companyID = ?1 and StatusInvoice != 6", nativeQuery = true
    )
    Number sumFindAllByCompanyID(UUID companyID);

    @Query(
        value = "select * from viewEinvoice where companyID = ?1 and StatusInvoice = 6 " +
            "ORDER BY case when InvoiceNo is null or InvoiceNo = '' then 0 else 1 end, case when InvoiceNo is not null then InvoiceTemplate end, case when InvoiceNo is not null then InvoiceSeries end ,InvoiceNo DESC, RefDateTime desc", nativeQuery = true
    )
    Page<EInvoice> findAllEInvoiceWaitSignByCompanyID(Pageable pageable, UUID companyID);

    @Query(
        value = "select SUM(TotalAmount) - SUM(TotalDiscountAmount) + SUM(TotalVATAmount) from viewEinvoice where companyID = ?1 and StatusInvoice = 6", nativeQuery = true
    )
    Number sumFindAllEInvoiceWaitSignByCompanyID(UUID companyID);

    @Query(
        value = "select * from viewEinvoice where companyID = ?1 and StatusInvoice = 5 " +
            " ORDER BY InvoiceTemplate, InvoiceSeries DESC , InvoiceNo DESC", nativeQuery = true
    )
    Page<EInvoice> findAllEInvoiceCanceledByCompanyID(Pageable pageable, UUID companyID);

    @Query(
        value = "select SUM(TotalAmount) - SUM(TotalDiscountAmount) + SUM(TotalVATAmount) from viewEinvoice where companyID = ?1 and StatusInvoice = 5", nativeQuery = true
    )
    Number sumFindAllEInvoiceCanceledByCompanyID(UUID companyID);

    @Query(
        value = "select * from viewEinvoice where companyID = ?1 and (StatusInvoice = 1 or StatusInvoice = 4) " +
            " ORDER BY InvoiceTemplate, InvoiceSeries DESC , InvoiceNo DESC", nativeQuery = true
    )
    Page<EInvoice> findAllEInvoiceForConvertByCompanyID(Pageable pageable, UUID companyID);

    @Query(
        value = "select SUM(TotalAmount) - SUM(TotalDiscountAmount) + SUM(TotalVATAmount) from viewEinvoice where companyID = ?1 and (StatusInvoice = 1 or StatusInvoice = 4)", nativeQuery = true
    )
    Number sumFindAllEInvoiceForConvertByCompanyID(UUID companyID);

    @Query(
        value = "select * from viewEinvoice where companyID = ?1 and StatusInvoice in (1,3,4,5)  and IDAdjustInv is not null ORDER BY InvoiceTemplate, InvoiceSeries DESC , InvoiceNo DESC", nativeQuery = true
    )
    Page<EInvoice> findAllEInvoiceAdjustedByCompanyID(Pageable pageable, UUID companyID);

    @Query(
        value = "select * from viewEinvoice where companyID = ?1 and StatusInvoice in (1,3,4,5) and IDReplaceInv is not null  ORDER BY InvoiceTemplate, InvoiceSeries DESC , InvoiceNo DESC", nativeQuery = true
    )
    Page<EInvoice> findAllEInvoiceReplacedByCompanyID(Pageable pageable, UUID companyID);

    @Modifying
    @Query(
        value = "UPDATE SABill SET InvoiceNo = ?2, ID_MIV = ?3 WHERE id = ?1 ; " +
            "UPDATE SAInvoice set InvoiceNo = ?2 where id = (select Top(1) SAInvoiceID from SAInvoiceDetail where SABillID = ?1 );" +
            "UPDATE PPDiscountReturn set InvoiceNo = ?2 where id = (select Top(1) PPDiscountReturnID from PPDiscountReturnDetail where SABillID = ?1 );" +
            "UPDATE SAReturn set InvoiceNo = ?2 where id = (select Top(1) SAReturnID from SAReturnDetail where SABillID = ?1 );", nativeQuery = true
    )
    void updateAfterCreateInvoiceMIV(UUID uuid, String invoiNo, String idMIV);

    List<EInvoice> findAllByCompanyIDAndIDMIVNotNull(UUID companyID);

    @Modifying
    @Query(
        value = "UPDATE SystemOption SET DATA = ?2 WHERE companyID = ?1 and Code = 'Token_MIV' ; ", nativeQuery = true
    )
    void updateEISystemOptionTokenMIV(UUID companyID, String token);
}
