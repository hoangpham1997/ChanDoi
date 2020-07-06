package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBInternalTransfer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBInternalTransfer.
 */
public interface MBInternalTransferService {

    /**
     * Save a mBInternalTransfer.
     *
     * @param mBInternalTransfer the entity to save
     * @return the persisted entity
     */
    MBInternalTransfer save(MBInternalTransfer mBInternalTransfer);

    /**
     * Get all the mBInternalTransfers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBInternalTransfer> findAll(Pageable pageable);


    /**
     * Get the "id" mBInternalTransfer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBInternalTransfer> findOne(UUID id);

    /**
     * Delete the "id" mBInternalTransfer.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
