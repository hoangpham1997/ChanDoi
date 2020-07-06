package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.PPServiceCostVoucherDTO;
import vn.softdreams.ebweb.service.dto.PPServiceDTO;
import vn.softdreams.ebweb.service.dto.ReceiveBillSearchDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PPServiceRepositoryCustom {

    /***
     * @author jsp
     * @param pageable
     * @param noBookType required = true
     * @return
     */
    Page<PPServiceDTO> findAllPPService(Pageable pageable, Integer receiptType, String toDate, String fromDate,
                                        Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType,
                                        String freeSearch, UUID org);

    /**
     * @author namnh
     * @param pageable
     * @return
     */
    Page<ReceiveBillSearchDTO>  findAllReceiveBillSearchDTO(Pageable pageable, SearchVoucher searchVoucher, Boolean isNoMBook, UUID orgID);

    void updateNhanHoaDon(List<UUID> listIDPPInvoice);

    PPServiceDTO findOneById(UUID ppServiceId, Integer currentBook, UUID org);

    UpdateDataDTO findPPServiceDTOByLocationItem(Integer nextItem, Integer currentBook, UUID ppServiceId, UUID org, Integer receiptType, String toDate, String fromDate,
                                                 Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType,
                                                 String freeSearch);

    Page<PPServiceCostVoucherDTO> findCostVouchers(Pageable pageable, UUID accountingObject, String fromDate, String toDate, boolean isNoMBook, UUID org, UUID ppInvoiceId, Boolean isHaiQuan);

    BigDecimal countTotalResultAmountOriginal(Integer receiptType, String toDate, String fromDate, Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType, String freeSearch, UUID org);

    List<UUID> findAllIDByPaymentVoucherID(List<UUID> uuidsPCMuaDichVu);

    List<UUID> findAllPaymentVoucherIDByID(List<UUID> uuidsPCMuaDichVu);
}
