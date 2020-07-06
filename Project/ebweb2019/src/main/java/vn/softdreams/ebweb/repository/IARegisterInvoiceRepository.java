package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.IARegisterInvoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the IARegisterInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IARegisterInvoiceRepository extends JpaRepository<IARegisterInvoice, UUID>, IARegisterInvoiceRepositoryCustom {

    Page<IARegisterInvoice> findAllByCompanyIDOrderByDateDesc(Pageable pageable, UUID org);

    @Query(value = "SELECT count(*) from IARegisterInvoice WHERE CompanyID = ?1 and No = ?2", nativeQuery = true)
    Integer checkDuplicateNo(UUID org, String no);
}
