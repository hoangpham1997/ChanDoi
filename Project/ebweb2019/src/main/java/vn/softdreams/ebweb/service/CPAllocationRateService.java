package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.Bank;
import vn.softdreams.ebweb.domain.CPAllocationRate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.CPAllocationRateDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPAllocationRate.
 */
public interface CPAllocationRateService {

    /**
     * Save a cPAllocationRate.
     *
     * @param cPAllocationRate the entity to save
     * @return the persisted entity
     */
    CPAllocationRate save(CPAllocationRate cPAllocationRate);

    /**
     * Get all the cPAllocationRates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPAllocationRate> findAll(Pageable pageable);


    /**
     * Get the "id" cPAllocationRate.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPAllocationRate> findOne(UUID id);

    /**
     * Delete the "id" cPAllocationRate.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<CPAllocationRate> findAllByCPPeriodID(UUID cPPeriodID);

    List<CPAllocationRateDTO> findAllByListCostSets(List<UUID> uuids, String fromDate, String toDate);
}
