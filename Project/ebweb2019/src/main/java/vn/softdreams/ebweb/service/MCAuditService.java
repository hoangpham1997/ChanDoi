package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MCAudit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MCPayment;
import vn.softdreams.ebweb.domain.MCReceipt;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MCAuditDTO;
import vn.softdreams.ebweb.web.rest.dto.MCAuditDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.MCAuditSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceViewDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing MCAudit.
 */
public interface MCAuditService {

    /**
     * Save a mCAudit.
     *
     * @param mCAudit the entity to save
     * @return the persisted entity
     */
    MCAudit save(MCAudit mCAudit);

    /**
     * Get all the mCAudits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MCAudit> findAll(Pageable pageable);


    /**
     * Get the "id" mCAudit.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MCAudit> findOne(UUID id);

    /**
     * Delete the "id" mCAudit.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    MCAuditSaveDTO saveDTO(MCAudit mcAudit);

    Page<MCAuditDTO> findAllDTOByCompanyID(Pageable pageable);

    byte[] exportExcel(Pageable pageable, String currencyID, String fromDate, String toDate, String textSearch);

    byte[] exportPdf(Pageable pageable, String currencyID, String fromDate, String toDate, String textSearch);

    Page<MCAuditDTO> searchMCAudit(Pageable pageable, String currencyID, String fromDate, String toDate, String keySearch);

    MCAudit findByRowNum(String currencyID, String fromDate, String toDate, String keySearch, Integer rowNum);

    MCAuditDetailDTO getMCAuditDetailsByID(UUID mCAuditID);

    HandlingResultDTO multiDelete(List<MCAudit> mcAudits);

    void edit(UUID id);

}
