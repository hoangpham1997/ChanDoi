package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.RSTransfer;
import vn.softdreams.ebweb.domain.RSTransferDetail;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferDetailsDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RSTransferSearchDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing RSTranfer.
 */
public interface RSTranferService {

    /**
     * Save a rSTranfer.
     *
     * @param rSTransfer the entity to save
     * @return the persisted entity
     */
    RSTransferSaveDTO save(RSTransferSaveDTO rSTransfer);

    /**
     * Get all the rSTranfers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RSTransfer> findAll(Pageable pageable);


    /**
     * Get the "id" rSTranfer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RSTransfer> findOne(UUID id);

    /**
     * Delete the "id" rSTranfer.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<RSTransferSearchDTO> searchAllTransfer(Pageable pageable, UUID accountingObject, UUID accountingObjectEmployee, Integer status, Integer noType, String fromDate, String toDate, String searchValue);

    List<RSTransferDetailsDTO> getDetailsTransferById(UUID id, Integer typeID);

    RSTransferSearchDTO findReferenceTablesID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum);

    Integer findRowNumID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id);

    byte[] exportPdf(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType);

    byte[] exportExcel(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType);

    Page<RSTransferSearchDTO> searchAll(Pageable pageable, UUID accountingObject, UUID repository, UUID accountingObjectEmployee, Integer status, Integer noType, String fromDate, String toDate, String searchValue);

    HandlingResultDTO multiDelete(List<RSTransfer> rsTransfers);

    HandlingResultDTO multiUnrecord(List<RSTransfer> rsTransfers);
}
