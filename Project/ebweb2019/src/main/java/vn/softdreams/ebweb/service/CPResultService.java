package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPResult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.web.rest.dto.CalculateCostDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPResult.
 */
public interface CPResultService {

    /**
     * Save a cPResult.
     *
     * @param cPResult the entity to save
     * @return the persisted entity
     */
    CPResult save(CPResult cPResult);

    /**
     * Get all the cPResults.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPResult> findAll(Pageable pageable);


    /**
     * Get the "id" cPResult.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPResult> findOne(UUID id);

    /**
     * Delete the "id" cPResult.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<CPResult> calculateCost(CalculateCostDTO calculateCostDTO);

}
