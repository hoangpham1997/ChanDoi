package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.RefVoucher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherSecondDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing ViewVoucher.
 */
public interface RefVoucherService {

    /**
     * Save a viewVoucher.
     *
     * @param viewVoucher the entity to save
     * @return the persisted entity
     */
    RefVoucher save(RefVoucher viewVoucher);

    /**
     * Get all the viewVouchers.
     *
     * @param pageable the pagination information
     * @param typeSearch
     * @param status
     * @return the list of entities
     */
    Page<RefVoucherDTO> findAll(Pageable pageable, Integer typeGroup, String no, String invoiceNo, Boolean recorded,
                                String fromDate, String toDate, Integer typeSearch, Integer status);


    /**
     * Get the "id" viewVoucher.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RefVoucher> findOne(Long id);

    /**
     * Delete the "id" viewVoucher.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<RefVoucherDTO> getRefViewVoucher(UUID id, int currentBook);

    /*
     * @Author Hautv
     * @param id
     * @param currentBook
     * @return
     */
    List<RefVoucherDTO> getRefViewVoucherByPaymentVoucherID(Integer typeID, UUID id, int currentBook);

    List<RefVoucherDTO> getRefViewVoucherPPinvoice(UUID refId);

    Page<RefVoucherSecondDTO> getViewVoucherToModal(Pageable pageable, Integer typeGroup, String fromDate, String toDate);
}
