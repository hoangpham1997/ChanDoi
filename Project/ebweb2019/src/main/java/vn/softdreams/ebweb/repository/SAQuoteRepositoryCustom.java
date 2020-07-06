package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.SAQuote;
import vn.softdreams.ebweb.domain.SAQuoteDetails;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.SAQuoteReportDTO;
import vn.softdreams.ebweb.service.dto.ViewSAQuoteDTO;
import vn.softdreams.ebweb.web.rest.dto.SAQuoteDTO;

import java.util.List;
import java.util.UUID;

public interface SAQuoteRepositoryCustom {
    /**
     * @param sort
     * @return
     */
    Page<SAQuote> findAll(Pageable sort, SearchVoucher searchVoucher, UUID companyID);

    Page<ViewSAQuoteDTO> findAllView(Pageable pageable, UUID companyID, UUID accountingObjectID, String fromDate, String toDate, String currencyID);

    Page<SAQuoteDTO> findAllData(Pageable pageable, UUID companyID);

    SAQuote findOneByRowNum(SearchVoucher searchVoucher, Number rowNum, UUID companyID);

    List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher, UUID companyID);

    List<SAQuote> findAllForExport(SearchVoucher searchVoucher1, UUID org);

    void deleteRefSAInvoiceAndRSInwardOutward(UUID id);

    List<SAQuoteReportDTO> findSAQuoteDetailsReport(UUID sAQuoteDetailID);
}
