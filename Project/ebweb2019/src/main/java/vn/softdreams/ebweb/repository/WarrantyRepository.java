package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.Warranty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the Warranty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarrantyRepository extends JpaRepository<Warranty, Long> {

    @Query(value = "select * from Warranty b where b.CompanyID = ?1 AND isActive = 1 order by WarrantyTime ", nativeQuery = true)
    List<Warranty> getAllWarrantyByCompanyID(UUID companyID);

}
