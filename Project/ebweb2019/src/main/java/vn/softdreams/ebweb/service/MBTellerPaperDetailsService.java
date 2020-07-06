package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBTellerPaperDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBTellerPaperDetails.
 */
public interface MBTellerPaperDetailsService {

    /**
     * Save a mBTellerPaperDetails.
     *
     * @param mBTellerPaperDetails the entity to save
     * @return the persisted entity
     */
    MBTellerPaperDetails save(MBTellerPaperDetails mBTellerPaperDetails);

    /**
     * Get all the mBTellerPaperDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBTellerPaperDetails> findAll(Pageable pageable);


    /**
     * Get the "id" mBTellerPaperDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBTellerPaperDetails> findOne(UUID id);

    /**
     * Delete the "id" mBTellerPaperDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
