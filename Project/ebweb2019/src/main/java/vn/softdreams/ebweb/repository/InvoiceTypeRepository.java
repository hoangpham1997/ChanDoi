package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.InvoiceType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the InvoiceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceTypeRepository extends JpaRepository<InvoiceType, UUID> {

    InvoiceType findByInvoiceTypeCode(String loaiHD);

    @Query(value = "SELECT * FROM InvoiceType ORDER BY InvoiceTypeCode", nativeQuery = true)
    List<InvoiceType> findAllOrderByInvoiceTypeName();

    @Query(value = "SELECT InvoiceTypeName FROM InvoiceType WHERE ID = ?1", nativeQuery = true)
    String getInvoiceTypeNameByInvoiceID(UUID id);
}
