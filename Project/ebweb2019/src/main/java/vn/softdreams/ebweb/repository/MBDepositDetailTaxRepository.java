package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBDepositDetailTax;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the MBDepositDetailTax entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBDepositDetailTaxRepository extends JpaRepository<MBDepositDetailTax, UUID> {

    List<MBDepositDetailTax> findByMBDepositID(UUID mbDepositId);
}
