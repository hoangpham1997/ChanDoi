package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.PPDiscountReturn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing PPDiscountReturn.
 */
public interface PPDiscountReturnService {

    /**
     * Save a pPDiscountReturn.
     *
     *
     * @return the persisted entity
     */
    DiscountAndBillAndRSIDTO save(DiscountAndBillAndRSIDTO discountAndBillAndRSIDTO);

    PPDiscountReturn save1(PPDiscountReturn pPDiscountReturn);
    /**
     * Get all the pPDiscountReturns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PPDiscountReturn> findAll(Pageable pageable);

    Optional<PPDiscountReturn> findOneByID(UUID id);

    /**
     * Get the "id" pPDiscountReturn.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PPDiscountReturnDTO> findOne(UUID id);

    /**
     * Delete the "id" pPDiscountReturn.
     *
     * @param  id of the entity
     */
    void delete(UUID id);

    Page<PPDiscountReturnSearch2DTO> searchPPDiscountReturn(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, Boolean statusPurchase, String keySearch);

    PPDiscountReturn findByRowNum(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, Boolean statusPurchase, String keySearch, Integer rownum);

    List<RefVoucherDTO> refVouchersByPPOrderID(UUID id);

    Optional<PPDiscountReturn> getPPDiscountReturnByID(UUID id);

    byte[] exportPdf(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, Boolean statusPurchase, String keySearch);

    byte[] exportExcel(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, Boolean statusPurchase, String keySearch);

    Page<PPDiscountReturnOutWardDTO> findAllPPDisCountReturnDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate);

    Long countFromRSI(UUID id);

    HandlingResultDTO multiDelete(List<UUID> listID);

    HandlingResultDTO multiUnrecord(List<PPDiscountReturn> ppDiscountReturns);
}
