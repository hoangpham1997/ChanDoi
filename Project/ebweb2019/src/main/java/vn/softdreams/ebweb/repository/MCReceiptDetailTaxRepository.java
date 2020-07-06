package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCReceiptDetailTax;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MCReceiptDetailTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCReceiptDetailTaxRepository extends JpaRepository<MCReceiptDetailTax, UUID> {

}
