package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.PPInvoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import vn.softdreams.ebweb.domain.SaReturn;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.*;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service Interface for managing PPInvoice.
 */
public interface PPInvoiceService {

    /**
     * Save a pPInvoice.
     *
     * @param pPInvoice the entity to save
     * @return the persisted entity
     */
    PPInvoice save(PPInvoice pPInvoice);

    /**
     * Get all the pPInvoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PPInvoice> findAll(Pageable pageable);

    Page<PPInvoice> findAll(Pageable pageable, SearchVoucher searchVoucher, String formality);

    /**
     * Get the "id" pPInvoice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PPInvoice> findOne(UUID id);

    /**
     * Delete the "id" pPInvoice.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * @author namnh
     * @param pageable
     * @return
     */
    Page<ReceiveBillSearchDTO> findAllPPInvoiceReceiveBill(Pageable pageable, SearchVoucher searchVoucher, String formality);
    Page<ReceiveBillSearchDTO> findAllReceiveBill(Pageable pageable, SearchVoucher searchVoucher);

    void saveReceiveBill(List<UUID> listIDPPInvoice);

    /**
     * @author congnd
     * @param pPInvoice
     * @param isRSI true: mua hàng qua kho, false: mua hàng không qua kho
     * @return
     */
    UpdateDataDTO savePPInvoice(PPInvoiceDTO pPInvoice, Boolean isRSI);

    /**
     * @author congnd
     * @param pageable
     * @param accountingObjectID
     * @param currencyID
     * @param fromDate
     * @param toDate
     * @param status
     * @param keySearch
     * @param employeeId
     * @param isRSI
     * @return
     */
    Page<PPInvoiceSearchDTO> searchPPInvoice(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Integer status, String keySearch, UUID employeeId, Boolean isRSI);

    PPInvoiceDTO getPPInvoiceById(UUID id);

    PPInvoiceDTO getPPInvoiceNotDetailById(UUID id);

    List<PPInvoiceDetailDTO> getPPInvoiceDetailByIdPPInvoice(UUID id);

    List<PPInvoiceDetailDTO> getPPInvoiceDetailByPaymentVoucherID(UUID paymentVoucherID);

    PPInvoiceDTO findIdByRowNum(Pageable pageable, UUID accountingObject, Integer status, String currency, UUID employee, String fromDate, String toDate, String textSearch, Integer rowNum, Boolean isRSI);

    ResultDTO deleteById(UUID id);

    PPInvoice getPPInvoiceByPaymentVoucherId(UUID id);

    byte[] exportPdf(UUID accountingObject, Integer status, String currency, UUID employee, String fromDate, String toDate, String searchValue, Boolean isRSI);

    byte[] exportExcel(UUID accountingObject, Integer status, String currency, UUID employee, String fromDate, String toDate, String searchValue, Boolean isRSI);

    ResultDTO checkUnRecord(UUID id);

    Page<ViewPPInvoiceDTO> getViewPPInvoiceDTOPopup(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, String currencyID);

    ResultDTO checkViaStock(UUID id);

    ResultDTO checkReferencesCount(UUID id);

    HandlingResultDTO multiDelete(List<PPInvoice> ppInvoices, boolean isKho);

    HandlingResultDTO multiUnRecord(List<PPInvoice> ppInvoices, boolean isKho);
}
