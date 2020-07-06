package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCPaymentDetailInsurance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCPaymentDetailInsurance.
 */
public interface MCPaymentDetailInsuranceService {

    /**
     * Save a mCPaymentDetailInsurance.
     *
     * @param mCPaymentDetailInsurance the entity to save
     * @return the persisted entity
     */
    MCPaymentDetailInsurance save(MCPaymentDetailInsurance mCPaymentDetailInsurance);

    /**
     * Get all the mCPaymentDetailInsurances.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCPaymentDetailInsurance> findAll(Pageable pageable);


    /**
     * Get the "id" mCPaymentDetailInsurance.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCPaymentDetailInsurance> findOne(UUID id);

    /**
     * Delete the "id" mCPaymentDetailInsurance.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
