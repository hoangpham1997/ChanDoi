package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBTellerPaperDetailVendor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MBTellerPaperDetailVendor.
 */
public interface MBTellerPaperDetailVendorService {

    /**
     * Save a mBTellerPaperDetailVendor.
     *
     * @param mBTellerPaperDetailVendor the entity to save
     * @return the persisted entity
     */
    MBTellerPaperDetailVendor save(MBTellerPaperDetailVendor mBTellerPaperDetailVendor);

    /**
     * Get all the mBTellerPaperDetailVendors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MBTellerPaperDetailVendor> findAll(Pageable pageable);


    /**
     * Get the "id" mBTellerPaperDetailVendor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MBTellerPaperDetailVendor> findOne(UUID id);

    /**
     * Delete the "id" mBTellerPaperDetailVendor.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
