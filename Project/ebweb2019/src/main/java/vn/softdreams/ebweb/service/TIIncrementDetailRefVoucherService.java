package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.TIIncrementDetailRefVoucher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing TIIncrementDetailRefVoucher.
 */
public interface TIIncrementDetailRefVoucherService {

    /**
     * Save a tIIncrementDetailRefVoucher.
     *
     * @param tIIncrementDetailRefVoucher the entity to save
     * @return the persisted entity
     */
    TIIncrementDetailRefVoucher save(TIIncrementDetailRefVoucher tIIncrementDetailRefVoucher);

    /**
     * Get all the tIIncrementDetailRefVouchers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TIIncrementDetailRefVoucher> findAll(Pageable pageable);


    /**
     * Get the "id" tIIncrementDetailRefVoucher.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TIIncrementDetailRefVoucher> findOne(UUID id);

    /**
     * Delete the "id" tIIncrementDetailRefVoucher.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
