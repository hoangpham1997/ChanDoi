package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBInternalTransferDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBInternalTransferDetails.
 */
public interface MBInternalTransferDetailsService {

    /**
     * Save a mBInternalTransferDetails.
     *
     * @param mBInternalTransferDetails the entity to save
     * @return the persisted entity
     */
    MBInternalTransferDetails save(MBInternalTransferDetails mBInternalTransferDetails);

    /**
     * Get all the mBInternalTransferDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBInternalTransferDetails> findAll(Pageable pageable);


    /**
     * Get the "id" mBInternalTransferDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBInternalTransferDetails> findOne(UUID id);

    /**
     * Delete the "id" mBInternalTransferDetails.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
