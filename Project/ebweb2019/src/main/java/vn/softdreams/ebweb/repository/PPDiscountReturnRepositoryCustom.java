package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.PPDiscountReturn;
import vn.softdreams.ebweb.domain.PPDiscountReturnDetails;
import vn.softdreams.ebweb.domain.SaBillDetails;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnDTO;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnSearch2DTO;
import vn.softdreams.ebweb.service.dto.PPDiscountReturnSearchDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceDetailPopupDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoicePopupDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


/**
 * Spring Data  repository for the OrganizationUnit entity.
 */
@SuppressWarnings("unused")
public interface PPDiscountReturnRepositoryCustom {
    Page<PPDiscountReturnSearch2DTO> searchPPDiscountReturn(Pageable pageable, UUID accountingObjectID,
                                                            String currencyID, String fromDate, String toDate,
                                                            Boolean status, Boolean statusPurchase, String keySearch, UUID org, String currentBook);

    PPDiscountReturn findByRowNum(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, Boolean statusPurchase, String keySearch, Integer rownum, UUID org, String currentBook);

    Optional<PPDiscountReturnDTO> findOneByID(UUID id);

    Page<PPDiscountReturnSearchDTO> searchPPDiscountReturnPDF(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Boolean status, Boolean statusPurchase, String keySearch, UUID org, String currentBook);

    void updateUnrecord(List<UUID> uuidList);

    List<PPDiscountReturnDTO> getAllPPDiscountReturnHasRSID(UUID org, Integer currentBook);

    Page<SAInvoicePopupDTO> getAllPPDiscountReturnSaBillPopupDTOs(Pageable pageable, UUID accountingObjectID, String fromDate, String toDate, UUID org, String currentBook, String currencyID, List<UUID> listPPDiscountReturnID);

    void updateSaBill(Set<SaBillDetails> saBillDetails, UUID id, String invoiceNo);

    List<SAInvoiceDetailPopupDTO> getPPDiscountDetailByPPDiscountID(List<UUID> ids, String data);

    PPDiscountReturnDetails findDetaiBySaBillID(UUID id);

}
