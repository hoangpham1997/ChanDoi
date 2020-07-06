package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCReceiptDetailCustomer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MCReceiptDetailCustomer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCReceiptDetailCustomerRepository extends JpaRepository<MCReceiptDetailCustomer, UUID> {

}
