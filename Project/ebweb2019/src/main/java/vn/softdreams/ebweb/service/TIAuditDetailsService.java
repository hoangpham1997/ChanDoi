package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIAuditDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIAuditDetails.
 */
public interface TIAuditDetailsService {

    /**
     * Save a tIAuditDetails.
     *
     * @param tIAuditDetails the entity to save
     * @return the persisted entity
     */
    TIAuditDetails save(TIAuditDetails tIAuditDetails);

    /**
     * Get all the tIAuditDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIAuditDetails> findAll(Pageable pageable);


    /**
     * Get the "id" tIAuditDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIAuditDetails> findOne(UUID id);

    /**
     * Delete the "id" tIAuditDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
