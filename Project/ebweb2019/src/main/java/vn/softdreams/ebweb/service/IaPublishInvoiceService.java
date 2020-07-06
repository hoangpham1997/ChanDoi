package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.IaPublishInvoice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing IaPublishInvoice.
 */
public interface IaPublishInvoiceService {

    /**
     * Save a iaPublishInvoice.
     *
     * @param iaPublishInvoice the entity to save
     * @return the persisted entity
     */
    IaPublishInvoice save(IaPublishInvoice iaPublishInvoice);

    /**
     * Get all the iaPublishInvoices.
     *
     * @return the list of entities
     */
    List<IaPublishInvoice> findAll();


    /**
     * Get the "id" iaPublishInvoice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IaPublishInvoice> findOne(UUID id);

    /**
     * Delete the "id" iaPublishInvoice.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    Page<IaPublishInvoice> findAll(Pageable pageable);

    /**
     * xuất pdf thông báo phát hành hóa đơn
     */
    byte[] exportPdf();
    /**
     * xuất excell thông báo phát hành hóa đơn
     */
    byte[] exportExcel();

    /**
     * @author dungvm
     * lấy đến số lớn nhất dựa vào IAReportID
     */
    Long findCurrentMaxFromNo(UUID id);
}
