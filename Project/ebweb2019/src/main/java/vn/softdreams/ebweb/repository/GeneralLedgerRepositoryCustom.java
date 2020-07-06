package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhAllocationPoPupDTO;
import vn.softdreams.ebweb.service.dto.Report.GiaThanhPoPupDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewGLPayExceedCashDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


/**
 * Spring Data  repository for the GeneralLedger entity.
 */
public interface GeneralLedgerRepositoryCustom {
    List<GeneralLedger> findByRefID(UUID refID);
    boolean unrecord(UUID refID);
    boolean unrecordList(List<UUID> refID);
    List<CollectionVoucher> getCollectionVoucher(String date, String currencyID, UUID bankID);
    List<CollectionVoucher> getSpendingVoucher(String date, String currencyID, UUID bankID);
    List<CollectionVoucher> getListMatch();
    List<GLCPExpenseList> getListForCPExpenseList();

    UpdateDataDTO calculatingLiabilities(UUID accountingObjectId, String postDate);

    List<ViewGLPayExceedCashDTO> getViewGLPayExceedCash(Integer phienSoLamViec, UUID companyID, LocalDate date);

    List<UUID> findAllPaymentVoucherByPPInvoiceId(List<UUID> uuidsPPService);

    Page<GiaThanhPoPupDTO> getByRatioMethod(Pageable pageable, String fromDate, String toDate, List<UUID> costSetID, Integer status, Integer phienSoLamViec, UUID companyID);

    List<GiaThanhPoPupDTO> getExpenseList(String fromDate, String toDate, List<UUID> costSetID, Integer status, Integer phienSoLamViec, UUID companyID);
}
