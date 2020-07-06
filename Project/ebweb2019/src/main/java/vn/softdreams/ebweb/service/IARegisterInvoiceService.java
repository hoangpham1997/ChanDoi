package vn.softdreams.ebweb.service;

import org.springframework.http.ResponseEntity;
import vn.softdreams.ebweb.domain.IARegisterInvoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing IARegisterInvoice.
 */
public interface IARegisterInvoiceService {

    /**
     * Save a iARegisterInvoice.
     *
     * @param iARegisterInvoice the entity to save
     * @return the persisted entity
     */
    IARegisterInvoice save(IARegisterInvoice iARegisterInvoice);

    /**
     * Get all the iARegisterInvoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IARegisterInvoice> findAll(Pageable pageable);


    /**
     * Get the "id" iARegisterInvoice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IARegisterInvoice> findOne(UUID id);

    /**
     * Delete the "id" iARegisterInvoice.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);


    /**
     * xuất pdf đăng ký sử dụng hóa đơn
     */
    byte[] exportPdf();

    /**
     * xuất excell đăng ký sử dụng hóa đơn
     */
    byte[] exportExcel();

    /**
     * tải file đính kèm
     */
    ResponseEntity<byte[]> downloadAttachFile(UUID id);

    HandlingResultDTO multiDelete(List<IARegisterInvoice> iaRegisterInvoices);
}
