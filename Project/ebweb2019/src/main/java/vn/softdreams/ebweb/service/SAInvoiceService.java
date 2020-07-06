package vn.softdreams.ebweb.service;

import vn.softdreams.ebweb.domain.MBDeposit;
import vn.softdreams.ebweb.domain.SAInvoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.SAInvoiceDTO;
import vn.softdreams.ebweb.web.rest.dto.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing SAInvoice.
 */
public interface SAInvoiceService {

    /**
     * Save a sAInvoice.
     *
     * @param sAInvoice the entity to save
     * @return the persisted entity
     */
    SAInvoice save(SAInvoice sAInvoice);

    /**
     * Get all the sAInvoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SAInvoice> findAll(Pageable pageable);


    /**
     * Get the "id" sAInvoice.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SAInvoice> findOne(UUID id);

    /**
     * Delete the "id" sAInvoice.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);

    SaInvoiceDTO saveDTO(SAInvoice saInvoice) throws InvocationTargetException, IllegalAccessException;

    Page<SAInvoiceViewDTO> searchSAInvoice(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, String keySearch, Integer typeId);

    SAInvoice findByRowNum(UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, String keySearch, Integer rownum, Integer typeId);

    List<RefVoucherDTO> refVouchersBySAOrderID(UUID id);

    List<RefVoucherDTO> refVouchersByMCReceiptID(UUID id);
    List<RefVoucherDTO> refVouchersByMBDepositID(UUID id);

    /**
     * @Author hieugie
     *
     * Lấy dữ liệu sau khi chọn từ popup chứng từ bán hàng
     *
     * @param saInvoiceDTO
     * @return
     */
    List<SAInvoiceDetailPopupDTO> getSaInvoiceDetail(List<SAInvoiceDetailPopupDTO> saInvoiceDTO);

    /**
     * @param pageable
     * @param accountingObjectID
     * @param fromDate
     * @param toDate
     * @param typeID 350 : xuất hóa đơn, 330 hàng bán trả lại
     * @param currencyID
     * @return
     */
    Page<SAInvoicePopupDTO> gSaInvoiceSaBillPopupDTOs(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, Integer typeID, String currencyID, UUID objectID, Integer createForm);

    byte[] exportPdf(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, String keySearch, Integer typeId);

    byte[] exportExcel(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, String keySearch, Integer typeId);

    Integer checkRelateVoucher(UUID sAInvoice, Boolean isCheckKPXK);

    Page<SAInvoiceDTO> findAllSAInvoiceDTO(Pageable pageable, UUID accountingObject, String fromDate, String toDate);

    List<UUID> getSAInvoiceBySABillID(UUID saBillID);

    HandlingResultDTO multiDelete(List<SAInvoice> saInvoices);

    HandlingResultDTO multiUnrecord(List<SAInvoice> saInvoices);

    Boolean isBanHangChuaThuTien(UUID refID);

}
