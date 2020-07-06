package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.IARegisterInvoiceDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the IARegisterInvoiceDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IARegisterInvoiceDetailsRepository extends JpaRepository<IARegisterInvoiceDetails, UUID> {

}
