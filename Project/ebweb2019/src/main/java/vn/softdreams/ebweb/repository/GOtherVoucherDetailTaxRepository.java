package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.GOtherVoucherDetailTax;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the GOtherVoucherDetailTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GOtherVoucherDetailTaxRepository extends JpaRepository<GOtherVoucherDetailTax, UUID> {

}
