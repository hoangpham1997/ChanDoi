package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.PPServiceCostVoucherDTO;
import vn.softdreams.ebweb.service.dto.PPServiceDTO;
import vn.softdreams.ebweb.service.dto.ReceiveBillSearchDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Service Interface for managing MCPayment.
 */
public interface PPServiceService {

    Page<PPServiceDTO> findAllPPService(Pageable pageable, Integer receiptType, String toDate, String fromDate,
                                        Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType,
                                        String freeSearch, UUID currentPPService);

    BigDecimal countTotalResultAmountOriginal(Integer receiptType, String toDate, String fromDate,
                                              Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType,
                                              String freeSearch);
    /***
     * @author phuonghv
     * @param ppServiceDTO
     * @return
     */
    UpdateDataDTO savePPService(PPServiceDTO ppServiceDTO);

    /**
     * @author namnh
     * @param pageable
     * @return
     */
    Page<ReceiveBillSearchDTO> findAllReceiveBillSearchDTO(Pageable pageable, SearchVoucher searchVoucher);

    void saveReceiveBill(List<UUID> listIDPPInvoice);

    UpdateDataDTO deletePPServiceById(UUID id);

    UpdateDataDTO deletePPServiceInId(List<UUID> id);

    UpdateDataDTO checkHadReference(UUID uuid);

    UpdateDataDTO getPPServiceDTOByLocation(UUID ppServiceId, Integer action,  Integer receiptType, String toDate, String fromDate,
                                            Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType,
                                            String freeSearch);

    Page<PPServiceCostVoucherDTO> findCostVouchers(Pageable pageable, UUID accountingObject, String fromDate, String toDate, UUID ppInvoiceId, Boolean isHaiQuan);

    byte[] exportPdf(Integer receiptType, String toDate, String fromDate, Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType, String freeSearch);

    byte[] exportExcel(Integer receiptType, String toDate, String fromDate, Integer hasRecord, String currencyID, UUID accountingObjectID, Integer noBookType, String freeSearch);
}
