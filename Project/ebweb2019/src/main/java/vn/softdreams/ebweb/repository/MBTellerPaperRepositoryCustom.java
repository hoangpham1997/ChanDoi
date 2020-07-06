package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MBTellerPaper;
import vn.softdreams.ebweb.domain.SAQuote;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.PPInvoiceDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBTellerPaperDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.util.List;
import java.util.UUID;

public interface MBTellerPaperRepositoryCustom {
    /**
     * @param sort
     * @return
     */
    Page<MBTellerPaper> findAll(Pageable sort, SearchVoucher searchVoucher, UUID companyID, int displayOnBook);

    MBTellerPaper findOneByRowNum(SearchVoucher searchVoucher, Number rowNum, UUID companyID);

    List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher, UUID companyID);

    Page<MBTellerPaperDTO> findAllDTOByCompanyID(Pageable pageable, UUID companyID, int displayOnBook);

    UpdateDataDTO getNoBookById(UUID paymentVoucherID);

    List<MBTellerPaperDTO> findAllForExport(SearchVoucher searchVoucher1, UUID org);

    Object findIDRef(UUID uuid, Integer typeID);

    List<PPInvoiceDTO> findVoucherByListPPInvoiceID(List<UUID> collect, int voucherTypeID);

    void multiDeleteMBTellerPaper(UUID orgID, List<UUID> uuidList);

    void multiDeleteChildMBTellerPaper(String tableChildName, List<UUID> uuidList);
}
