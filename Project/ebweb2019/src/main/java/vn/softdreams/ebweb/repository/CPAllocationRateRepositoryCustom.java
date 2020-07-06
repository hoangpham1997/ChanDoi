package vn.softdreams.ebweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.CPAllocationRate;
import vn.softdreams.ebweb.service.dto.CPAllocationRateDTO;

import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the CPAllocationRate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CPAllocationRateRepositoryCustom {

    List<CPAllocationRate> findAllByCPPeriodID(UUID org, UUID cPPeriodID);

    List<CPAllocationRateDTO> findAllByListCostSets(List<UUID> uuids, UUID org);
}
