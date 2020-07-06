package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MCReceipt;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCReceiptDTO;
import vn.softdreams.ebweb.web.rest.dto.MCReceiptSaveDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCReceipt.
 */
public interface MCReceiptService {

    /**
     * Save a mCReceipt.
     *
     * @param mCReceipt the entity to save
     * @return the persisted entity
     */
    MCReceipt save(MCReceipt mCReceipt);

    /**
     * Get all the mCReceipts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCReceipt> findAll(Pageable pageable);


    /**
     * Get the "id" mCReceipt.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCReceipt> findOne(UUID id);

    /**
     * Get the "mcAuditID" mCReceipt.
     *
     * @param mcAuditID the id of the entity
     * @return the entity
     */
    MCReceipt findByAuditID(UUID mcAuditID);

    /**
     * Delete the "id" mCReceipt.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * Hautv
     *
     * @param pageable
     * @return
     */
    Page<MCReceiptDTO> findAllDTOByCompanyID(Pageable pageable);

    MCReceiptSaveDTO saveDTO(MCReceipt mCReceipt);

    HandlingResultDTO multiUnrecord(List<MCReceipt> mbDeposits);

    HandlingResultDTO multiDelete(List<MCReceipt> mbDeposits);
}
