package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SAInvoice;
import vn.softdreams.ebweb.domain.SAInvoiceDetails;
import vn.softdreams.ebweb.domain.SaBillDetails;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnOutWardDTO;
import vn.softdreams.ebweb.service.dto.RSInwardOutwardDetailReportDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceDetailPopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoicePopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceViewDTO;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;


/**
 * Spring Data  repository for the OrganizationUnit entity.
 */
@SuppressWarnings("unused")
public interface SAInvoiceRepositoryCustom {
    Page<SAInvoiceViewDTO> searchSAInvoice(Pageable pageable, UUID accountingObjectID,
                                           String currencyID, String fromDate, String toDate,
                                           Boolean status, String keySearch, UUID org, Integer typeId, String currentBook, Boolean isExport);

    SAInvoice findByRowNum(UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, String keySearch, Integer rownum, UUID org, String currentBook, Integer typeId);

    /**
     * @Author hieugie
     *
     * Lấy danh sách sa invoice cho popup chứng từ tham chiếu
     *
     * @param ids
     * @param data
     * @return
     */
    List<SAInvoiceDetailPopupDTO> getSaInvoiceDetail(List<UUID> ids, String data);

    List<RSInwardOutwardDetailReportDTO> getSaInvoiceDetails(UUID id);

    /**
     * @Author hieugie
     *
     * Lấy dữ liệu popup chứng từ bán hàng cho xuất hóa đơn
     *
     * @param pageable
     * @param accountingObjectID
     * @param fromInvoiceDate
     * @param toInvoiceDate
     * @param org
     * @param currentBook
     * @param currencyID
     * @return
     */
    Page<SAInvoicePopupDTO> getAllSaInvoiceSaBillPopupDTOs(Pageable pageable, UUID accountingObjectID,
                                                           String fromInvoiceDate, String toInvoiceDate,
                                                           UUID org, String currentBook, String currencyID, List<UUID> ids);

    /**
     * @Author hieugie
     *
     * Lấy dữ liệu popup chứng từ bán hàng cho hàng bán trả lại, hàng bán giảm giá
     *
     * @param pageable
     * @param accountingObjectID
     * @param fromInvoiceDate
     * @param toInvoiceDate
     * @param org
     * @param currentBook
     * @param currencyID
     * @param typeID
     * @return
     */
    Page<SAInvoicePopupDTO> getAllSaInvoiceSaReturnPopupDTOs(Pageable pageable, UUID accountingObjectID,
                                                             String fromInvoiceDate, String toInvoiceDate,
                                                             UUID org, String currentBook, String currencyID, Integer typeID, List<UUID> listSAInvoiceID);

    /**
     * @Author hieugie
     *
     * @param saBillDetails
     */
    void updateSaBill(Set<SaBillDetails> saBillDetails, UUID saBillID, String InvoiceNo);

    List<SAInvoiceDetailPopupDTO> getSaInvoiceDetailBySAInvoiceID(List<UUID> ids, String data);

    void DeleteRelate(List<String> listRelateID);

    void updateUnrecord(List<UUID> uuidList);

    List<SAInvoiceViewDTO> getAllSAInvoiceHasRSID(UUID org, Integer currentBook);

    List<SAInvoiceDetails> findSAOrderIDByListSAInvoice(List<UUID> uuidList);

    SAInvoiceDetails findDetaiBySaBillID(UUID id);

}
