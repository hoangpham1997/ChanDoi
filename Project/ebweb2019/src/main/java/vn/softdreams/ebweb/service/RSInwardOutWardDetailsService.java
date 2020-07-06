package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.RSInwardOutWardDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing RSInwardOutWardDetails.
 */
public interface RSInwardOutWardDetailsService {

    /**
     * Save a rSInwardOutWardDetails.
     *
     * @param rSInwardOutWardDetails the entity to save
     * @return the persisted entity
     */
    RSInwardOutWardDetails save(RSInwardOutWardDetails rSInwardOutWardDetails);

    /**
     * Get all the rSInwardOutWardDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RSInwardOutWardDetails> findAll(Pageable pageable);


    /**
     * Get the "id" rSInwardOutWardDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RSInwardOutWardDetails> findOne(Long id);

    /**
     * Delete the "id" rSInwardOutWardDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
