package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.PaymentClause;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PaymentClause.
 */
public interface PaymentClauseService {

    /**
     * Save a paymentClause.
     *
     * @param paymentClause the entity to save
     * @return the persisted entity
     */
    PaymentClause save(PaymentClause paymentClause);

    /**
     * Get all the paymentClauses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PaymentClause> findAll(Pageable pageable);

    Page<PaymentClause> findAllPaymentClauses(Pageable pageable);


    /**
     * Get the "id" paymentClause.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PaymentClause> findOne(UUID id);

    /**
     * Delete the "id" paymentClause.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    List<PaymentClause> findAllByCompanyID();

    HandlingResultDTO deleteMulti(List<PaymentClause> paymentClauses);

//    Integer checkDuplicatePaymentClause(PaymentClause paymentClause);
}
