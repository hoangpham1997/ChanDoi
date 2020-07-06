package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBCreditCardDetailTax;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MBCreditCardDetailTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBCreditCardDetailTaxRepository extends JpaRepository<MBCreditCardDetailTax, UUID> {
    List<MBCreditCardDetailTax> findByMBCreditCardID(UUID mbCreditCardId);

}
