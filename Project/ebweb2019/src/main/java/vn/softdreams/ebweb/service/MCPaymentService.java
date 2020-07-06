package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCPayment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MCPaymentDetails;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCPaymentDTO;
import vn.softdreams.ebweb.web.rest.dto.MCPaymentSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.MCPaymentViewDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCPayment.
 */
public interface MCPaymentService {

    /**
     * Save a mCPayment.
     *
     * @param mCPayment the entity to save
     * @return the persisted entity
     */
    MCPayment save(MCPayment mCPayment);

    /**
     * Get all the mCPayments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCPayment> findAll(Pageable pageable);


    /**
     * Get the "id" mCPayment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCPayment> findOne(UUID id);

    MCPayment findByAuditID(UUID mcAuditID);

    /**
     * Delete the "id" mCPayment.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * Hautv
     *
     * @param ID
     * @return
     */
    List<MCPaymentDetails> findMCPaymentDetails(UUID ID);

    Page<MCPaymentDTO> findAllDTOByCompanyID(Pageable pageable);

    MCPaymentViewDTO findOneDTO(UUID id);

    MCPaymentSaveDTO saveDTO(MCPayment mCPayment);

    HandlingResultDTO multiDelete(List<MCPayment> mcPayments);

    HandlingResultDTO multiUnrecord(List<MCPayment> mcPayments);

    boolean unrecord(List<UUID> refID, List<UUID> repositoryLedgerID);

}
