package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPExpenseTranferDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CPExpenseTranferDetails.
 */
public interface CPExpenseTranferDetailsService {

    /**
     * Save a cPExpenseTranferDetails.
     *
     * @param cPExpenseTranferDetails the entity to save
     * @return the persisted entity
     */
    CPExpenseTranferDetails save(CPExpenseTranferDetails cPExpenseTranferDetails);

    /**
     * Get all the cPExpenseTranferDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPExpenseTranferDetails> findAll(Pageable pageable);


    /**
     * Get the "id" cPExpenseTranferDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPExpenseTranferDetails> findOne(Long id);

    /**
     * Delete the "id" cPExpenseTranferDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
