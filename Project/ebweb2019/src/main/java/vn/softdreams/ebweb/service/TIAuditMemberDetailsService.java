package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIAuditMemberDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIAuditMemberDetails.
 */
public interface TIAuditMemberDetailsService {

    /**
     * Save a tIAuditMemberDetails.
     *
     * @param tIAuditMemberDetails the entity to save
     * @return the persisted entity
     */
    TIAuditMemberDetails save(TIAuditMemberDetails tIAuditMemberDetails);

    /**
     * Get all the tIAuditMemberDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIAuditMemberDetails> findAll(Pageable pageable);


    /**
     * Get the "id" tIAuditMemberDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIAuditMemberDetails> findOne(UUID id);

    /**
     * Delete the "id" tIAuditMemberDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
