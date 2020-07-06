package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.dto.HandlingResultDTO;
import vn.softdreams.ebweb.service.dto.ViewVoucherNoDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing InvoiceType.
 */
public interface ViewVoucherNoService {

    /**
     * Get all the invoiceTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ViewVoucherNo> findAll(Pageable pageable);

    Page<ViewVoucherNo> getAllVoucherNotRecorded(Pageable pageable, LocalDate postedDate);

    List<ViewVoucherNo> getAllVoucherNotRecorded(LocalDate postedDate);

    List<ViewVoucherNoDetailDTO> findAllViewVoucherNoDetailDTOByListParentID(List<UUID> uuids);

    /**
     * Get the "id" invoiceType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ViewVoucherNo> findOne(UUID id);

    HandlingResultDTO closeBook(CloseBookDTO closeBookDTO);

    Boolean updateDateClosedBook(UpdateDateClosedBook updateDateClosedBook);

    String checkCloseBookForListBranch(List<OrgTreeDTO> orgTreeDTOS, LocalDate postedDate);

    void updateCloseBookDateForBranch(List<OrgTreeDTO> orgTreeDTOS, LocalDate postedDate);

    List<GeneralLedger> handleRecord(List<ViewVoucherNo> lstRecord, List<ViewVoucherNo> lstFail);

    List<RepositoryLedger> handleRepositoryLedger(List<ViewVoucherNo> lstRecord, List<ViewVoucherNo> lstFail);

    List<Toolledger> handleToolRecord(List<ViewVoucherNo> lstRecord, List<ViewVoucherNo> lstFail);

    List<FixedAssetLedger> handleFixedRecord(List<ViewVoucherNo> lstRecord, List<ViewVoucherNo> lstFail);

    Page<ViewVoucherDTO> findAllByTypeGroupID(Pageable pageable, Integer typeGroupID, String fromDate, String toDate);

    Page<ViewVoucherDTO> searchVoucher(Pageable pageable, Integer typeSearch, Integer typeGroupID, String no, String invoiceDate, Boolean recorded, String fromDate, String toDate);

    Respone_SDS resetNo(RequestResetNoDTO requestResetNoDTO);
}
