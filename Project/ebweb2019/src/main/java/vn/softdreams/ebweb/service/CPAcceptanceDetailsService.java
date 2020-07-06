package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.CPAcceptanceDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.web.rest.dto.EvaluateDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing CPAcceptanceDetails.
 */
public interface CPAcceptanceDetailsService {

    /**
     * Save a cPAcceptanceDetails.
     *
     * @param cPAcceptanceDetails the entity to save
     * @return the persisted entity
     */
    CPAcceptanceDetails save(CPAcceptanceDetails cPAcceptanceDetails);

    /**
     * Get all the cPAcceptanceDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CPAcceptanceDetails> findAll(Pageable pageable);


    /**
     * Get the "id" cPAcceptanceDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CPAcceptanceDetails> findOne(UUID id);

    /**
     * Delete the "id" cPAcceptanceDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<CPAcceptanceDetails> evaluate(EvaluateDTO evaluateDTO);

}
