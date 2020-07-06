package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MBDepositDetailCustomer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MBDepositDetailCustomer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MBDepositDetailCustomerRepository extends JpaRepository<MBDepositDetailCustomer, UUID> {

}
