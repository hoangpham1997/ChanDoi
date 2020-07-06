package vn.softdreams.ebweb.repository;

import vn.softdreams.ebweb.domain.CPAllocationRate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPAllocationRate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPAllocationRateRepository extends JpaRepository<CPAllocationRate, UUID>, CPAllocationRateRepositoryCustom {

}
