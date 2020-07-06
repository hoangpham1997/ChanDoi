package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.RSInwardOutWardDetails;
import vn.softdreams.ebweb.domain.RSInwardOutward;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.SAOrderDTO;
import vn.softdreams.ebweb.service.dto.ViewRSOutwardDTO;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSaveDTO;
import vn.softdreams.ebweb.web.rest.dto.RSInwardOutwardSearchDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing RSInwardOutward.
 */
public interface RSInwardOutwardService {

    /**
     * Save a rSInwardOutward.
     *
     * @param rSInwardOutward the entity to save
     * @return the persisted entity
     */
    RSInwardOutwardSaveDTO save(RSInwardOutwardSaveDTO rSInwardOutward);

    /**
     * Get all the rSInwardOutwards.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<RSInwardOutward> findAll(Pageable pageable);


    /**
     * Get the "id" rSInwardOutward.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RSInwardOutward> findOne(UUID id);

    /**
     * Delete the "id" rSInwardOutward.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<RSInwardOutwardSearchDTO> searchAll(Pageable pageable, UUID accountingObject, Integer status, String fromDate, String toDate, String searchValue, Integer noType);

    RSInwardOutwardSearchDTO findReferenceTablesID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum);

    Integer findRowNumByID(UUID accountingObject, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id);

    RSInwardOutward findByRowNumOutWard(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum);

    Integer findRowNumOutWardByID(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, Integer noType, String searchValue, UUID id);


    Page<RSInwardOutwardSearchDTO> searchAllOutWard(Pageable pageable, UUID accountingObject, UUID accountingObjectEmployee, Integer status, Integer noType, String fromDate, String toDate, String searchValue);

    byte[] exportPdf(UUID accountingObject, Integer status, String fromDate, String toDate, String searchValue, Integer noType);

    byte[] exportExcel(UUID accountingObject, Integer status, String fromDate, String toDate, String searchValue, Integer noType);

    byte[] exportPdfOutWard(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType);

    byte[] exportExcelOutWard(UUID accountingObject, UUID accountingObjectEmployee, Integer status, String fromDate, String toDate, String searchValue, Integer noType);

    List<RSInwardOutWardDetails> getDetailsById(UUID id, Integer typeID);

    List<RSInwardOutWardDetails> getDetailsOutWardById(UUID id, Integer typeID);

    UUID getReferenceIDByRSInwardID(UUID id, Integer typeID);

    Page<ViewRSOutwardDTO> getViewRSOutwardDTOPopup(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, String currencyID);

    RSInwardOutwardSearchDTO findReferenceTableID(UUID accountingObject, Integer status, String fromDate, String toDate, Integer noType, String searchValue, Integer rowNum);

    void unRecordDetails(List<UUID> ids);

    HandlingResultDTO multiDelete(List<RSInwardOutward> rsInwardOutward);

    HandlingResultDTO multiUnrecord(List<RSInwardOutward> rsInwardOutward);
}
