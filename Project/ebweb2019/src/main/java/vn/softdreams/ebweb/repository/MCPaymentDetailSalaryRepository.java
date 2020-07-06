package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.MCPaymentDetailSalary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the MCPaymentDetailSalary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MCPaymentDetailSalaryRepository extends JpaRepository<MCPaymentDetailSalary, UUID> {

}
