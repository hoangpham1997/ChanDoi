package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPUncompleteDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.web.rest.dto.CPUncompleteDTO;
import vn.softdreams.ebweb.web.rest.dto.EvaluateDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPUncompleteDetails.
 */
public interface CPUncompleteDetailsService {

    /**
     * Save a cPUncompleteDetails.
     *
     * @param cPUncompleteDetails the entity to save
     * @return the persisted entity
     */
    CPUncompleteDetails save(CPUncompleteDetails cPUncompleteDetails);

    /**
     * Get all the cPUncompleteDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPUncompleteDetails> findAll(Pageable pageable);

    List<CPUncompleteDetails> findAllByCPPeriodID(UUID cPPeriodID);


    /**
     * Get the "id" cPUncompleteDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPUncompleteDetails> findOne(UUID id);

    /**
     * Delete the "id" cPUncompleteDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<CPUncompleteDetails> evaluate(EvaluateDTO uncompleteDTO);

}
