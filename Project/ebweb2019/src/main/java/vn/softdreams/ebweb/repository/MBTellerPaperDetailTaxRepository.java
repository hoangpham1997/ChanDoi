package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBTellerPaperDetailTax;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MBTellerPaperDetailTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBTellerPaperDetailTaxRepository extends JpaRepository<MBTellerPaperDetailTax, UUID> {

}
