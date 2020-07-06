package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBInternalTransferTax;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MBInternalTransferTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBInternalTransferTaxRepository extends JpaRepository<MBInternalTransferTax, UUID> {

}
