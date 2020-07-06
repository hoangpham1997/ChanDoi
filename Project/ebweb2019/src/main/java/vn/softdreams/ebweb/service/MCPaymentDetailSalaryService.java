package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCPaymentDetailSalary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCPaymentDetailSalary.
 */
public interface MCPaymentDetailSalaryService {

    /**
     * Save a mCPaymentDetailSalary.
     *
     * @param mCPaymentDetailSalary the entity to save
     * @return the persisted entity
     */
    MCPaymentDetailSalary save(MCPaymentDetailSalary mCPaymentDetailSalary);

    /**
     * Get all the mCPaymentDetailSalaries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCPaymentDetailSalary> findAll(Pageable pageable);


    /**
     * Get the "id" mCPaymentDetailSalary.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCPaymentDetailSalary> findOne(UUID id);

    /**
     * Delete the "id" mCPaymentDetailSalary.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
