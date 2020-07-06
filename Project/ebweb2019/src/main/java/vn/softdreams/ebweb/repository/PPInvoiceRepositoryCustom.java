package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.PPInvoice;
import vn.softdreams.ebweb.domain.PPInvoiceDetails;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PPInvoiceRepositoryCustom {
    Page<PPInvoice> findAll(Pageable sort, SearchVoucher searchVoucher, String formality);

    Page<ReceiveBillSearchDTO> findAllReceiveBillSearchDTO(Pageable pageable, SearchVoucher searchVoucher, String formality, Boolean isNoMBook, UUID orgID);

    Page<ReceiveBillSearchDTO> findAllReceiveBillDTO(Pageable pageable, SearchVoucher searchVoucher, Boolean isNoMBook, UUID orgID);

    void updateNhanHoaDon(List<UUID> listIDPPInvoice);

    void updateQuantity(UUID refID, BigDecimal quantity, Boolean statusMinus);

    Page<PPInvoiceSearchDTO> searchPPInvoice(Pageable pageable, UUID accountingObjectID, String currencyID, String fromDate, String toDate, Integer status, String keySearch, boolean isNoMBook, UUID companyID, UUID employeeId, Boolean isRSI);

    PPInvoiceDTO getPPInvoiceById(UUID id);

    List<PPInvoiceDetailDTO> getPPInvoiceDetailByIdPPInvoice(UUID id);

    List<PPInvoiceDetail1DTO> getPPInvoiceDetail1ByPPInvoiceId(UUID id);

    List<PPInvoiceDetailDTO> getPPInvoiceDetailByPaymentVoucherID(UUID paymentVoucherID);

    PPInvoice findIdByRowNum(Pageable pageable, UUID accountingObject, Integer status, String currency, UUID employee, String fromDate, String toDate, String textSearch, Integer rowNum, UUID companyID, boolean isNoMBook, Boolean isRSI);

    Page<ViewPPInvoiceDTO> findAllView(Pageable pageable, UUID org, UUID accountingObjectID, String fromDate, String toDate, String soLamViec, String currencyID);

    int checkReferences(UUID id, List<String> referenceTables, String property);

    void updateUnRecord(List<PPInvoice> ppiList);

    void updateUnRecordMCP(List<PPInvoice> ppiList);

    void updateUnRecordMBT(List<PPInvoice> ppiList);

    void updateUnRecordMBC(List<PPInvoice> ppiList);
    List<ViewPPInvoiceDTO> getAllPPInvoiceHasRSID(UUID org, Integer currentBook);

    void updateUnrecord(List<UUID> uuidList);

    List<UUID> findAllParentID(List<UUID> listIDPPDetail);

    PPServiceCostVoucherDTO getByPPServiceId(UUID ppServiceID);

//    List<PPInvoiceDetails> findDetailsByListIDMBCreditCard(List<UUID> ppInvoiceList);
}
