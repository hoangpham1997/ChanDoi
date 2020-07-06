package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPAllocationGeneralExpenseDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPAllocationGeneralExpenseDetails.
 */
public interface CPAllocationGeneralExpenseDetailsService {

    /**
     * Save a cPAllocationGeneralExpenseDetails.
     *
     * @param cPAllocationGeneralExpenseDetails the entity to save
     * @return the persisted entity
     */
    CPAllocationGeneralExpenseDetails save(CPAllocationGeneralExpenseDetails cPAllocationGeneralExpenseDetails);

    /**
     * Get all the cPAllocationGeneralExpenseDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPAllocationGeneralExpenseDetails> findAll(Pageable pageable);

    /**
     * Get all the cPAllocationGeneralExpenseDetails.
     *
     * @param cPPeriodID the pagination information
     * @return the list of entities
     */
    List<CPAllocationGeneralExpenseDetails> findAllByCPPeriodID(UUID cPPeriodID);


    /**
     * Get the "id" cPAllocationGeneralExpenseDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPAllocationGeneralExpenseDetails> findOne(UUID id);

    /**
     * Delete the "id" cPAllocationGeneralExpenseDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
