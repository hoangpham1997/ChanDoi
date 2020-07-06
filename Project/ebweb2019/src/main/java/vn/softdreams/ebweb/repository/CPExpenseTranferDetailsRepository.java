package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPExpenseTranferDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CPExpenseTranferDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPExpenseTranferDetailsRepository extends JpaRepository<CPExpenseTranferDetails, Long> {

}
