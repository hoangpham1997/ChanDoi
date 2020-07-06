package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.GeneralLedger;
import vn.softdreams.ebweb.domain.RepositoryLedger;
import vn.softdreams.ebweb.domain.ViewVoucherNo;
import vn.softdreams.ebweb.service.dto.ViewVoucherNoDetailDTO;
import vn.softdreams.ebweb.web.rest.dto.OrgTreeDTO;
import vn.softdreams.ebweb.web.rest.dto.ViewVoucherDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ViewVoucherNoRespositoryCustom {

    List<ViewVoucherNoDetailDTO> findAllViewVoucherNoDetailDTOByListParentID(List<UUID> uuids);

    void saveGeneralLedger(List<GeneralLedger> lsGeneralLedgers);

    void saveRepositoryLedger(List<RepositoryLedger> repositoryLedgers);

    void updateVoucherRefRecorded(List<ViewVoucherNo> lstViewVoucherNos);

    void updateTableRecord(List<ViewVoucherNo> lstViewVoucherNos);

    void updateVoucherPostedDate(List<ViewVoucherNo> viewVoucherNos, List<ViewVoucherNo> listSubChangePostedDate, LocalDate postedDate);

    void deleteVoucher(List<ViewVoucherNo> lstViewVoucherNos);

    void handleChangePostedDate(List<ViewVoucherNo> listSubDateRoot, List<ViewVoucherNo> listDateChangeAfter);

    Boolean updateDateClosedBook(UUID companyID, String code, String codeDateOld, LocalDate dateClose, LocalDate dateCloseOld);

    void updateCloseBookDateForBranch(List<OrgTreeDTO> orgTreeDTOS, LocalDate postedDate);

    void deletePaymentVoucherInID(List<UUID> uuids);

    void saveToolLedger(List<GeneralLedger> ledgers);

    Page<ViewVoucherDTO> findAllByTypeGroupID(Pageable pageable, Integer typeGroupID, UUID companyID, Integer typeLedger, String fromDate, String toDate);

    List<ViewVoucherDTO> findAllByTypeGroupID(Integer typeGroupID, UUID companyID, Integer typeLedger, String fromDate, String toDate);

    Page<ViewVoucherDTO> searchVoucher(Pageable pageable, Integer typeSearch, Integer typeGroupID, String no, String invoiceDate, Boolean recorded, String fromDate, String toDate, Integer phienSoLamViec, UUID companyID);

    void resetNo(List<ViewVoucherDTO> viewVoucherDTOS, Integer phienSoLamViec, Boolean onlyOneBook);

}
