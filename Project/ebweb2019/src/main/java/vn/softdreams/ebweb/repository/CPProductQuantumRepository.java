package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPProductQuantum;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.CreditCard;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPProductQuantum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPProductQuantumRepository extends JpaRepository<CPProductQuantum, UUID>, CPProductQuantumRepositoryCustom {

    @Query(value = "select * from CPProductQuantum b where b.CompanyID = ?1", nativeQuery = true)
    List<CPProductQuantum> findAllByCompanyID(UUID companyID);
}
