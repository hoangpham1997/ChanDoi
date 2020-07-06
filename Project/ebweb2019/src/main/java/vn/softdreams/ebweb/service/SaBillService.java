package vn.softdreams.ebweb.service;

import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.ebweb.domain.SAInvoice;
import vn.softdreams.ebweb.domain.SaBill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.web.rest.dto.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Service Interface for managing SaBill.
 */
public interface SaBillService {

    /**
     * Save a saBill.
     *
     * @param saBill the entity to save
     * @return the persisted entity
     */
    SaBillSaveDTO save(SaBillSaveDTO saBill);

    /**
     * Get all the saBills.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SaBill> findAll(Pageable pageable);


    /**
     * Get the "id" saBill.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SaBillViewDTO findOne(UUID id);

    /**
     * Delete the "id" saBill.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    /**
     * @Author hieugie
     *
     * @param pageable
     * @param accountingObjectID
     * @param invoiceTemplate
     * @param fromInvoiceDate
     * @param toInvoiceDate
     * @param invoiceSeries
     * @param invoiceNo
     * @param freeText
     * @return
     */
    Page<SaBillDTO> getAllSaBillDTOs(Pageable pageable, UUID accountingObjectID, String invoiceTemplate,
                                     String fromInvoiceDate, String toInvoiceDate, String invoiceSeries,
                                     String invoiceNo, String freeText);

    /**
     * @Author hieugie
     *
     * @param pageable
     * @param accountingObjectID
     * @param invoiceTemplate
     * @param fromInvoiceDate
     * @param toInvoiceDate
     * @param invoiceSeries
     * @param invoiceNo
     * @param freeText
     * @param id
     * @return
     */
    SaBillViewDTO getNextSaBillDTOs(Pageable pageable, UUID accountingObjectID, String invoiceTemplate,
                                    String fromInvoiceDate, String toInvoiceDate, String invoiceSeries,
                                    String invoiceNo, String freeText, Integer index, UUID id);

    /**
     * @Author hieugie
     *
     * @return
     */
    SaBillSearchDTO getAllSearchData();

    /**
     * @Author hieugie
     *
     * @returnss
     */
    Page<SaBill> getAll();

    /**
     * @Author hieugie
     *
     * @param file
     * @return
     */
    UploadInvoiceDTO upload(MultipartFile file) throws IOException;

    /**
     * @Author hieugie
     *
     * @return
     */
    byte[] exportExcel(UUID accountingObjectID, String invoiceTemplate, String fromInvoiceDate, String toInvoiceDate, String invoiceSeries, String invoiceNo, String freeText);

    SaBillViewDTO findOneByID(UUID id);

    Page<SaBillCreatedDTO> saBillCreated(Pageable pageable, UUID accountingObjectID, String formDate, String toDate, String currencyCode);

    List<SaBillCreatedDetailDTO> saBillCreatedDetail(List<UUID> sabillIdList);

    byte[] exportPdf(UUID accountingObjectID, String invoiceTemplate, String fromInvoiceDate, String toInvoiceDate, String invoiceSeries, String invoiceNo, String freeText);

    Boolean checkRelateVoucher(UUID sABillID);

    HandlingResultDTO multiDelete(List<SaBill> saBills);

}
