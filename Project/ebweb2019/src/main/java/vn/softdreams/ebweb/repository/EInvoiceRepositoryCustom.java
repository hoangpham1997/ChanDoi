package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.EInvoice;
import vn.softdreams.ebweb.domain.EInvoiceDetails;
import vn.softdreams.ebweb.domain.SaBill;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.InvoicesSDS;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.web.rest.dto.ConnectEInvoiceDTO;
import vn.softdreams.ebweb.web.rest.dto.InformationVoucherDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface EInvoiceRepositoryCustom {
    List<EInvoiceDetails> findAllEInvoiceDetailsByID(UUID id, String refTable);

    List<EInvoice> findAllByListID(List<UUID> uuids);

    List<SaBill> findAllSABillByListID(List<UUID> uuids);

    List<EInvoiceDetails> findAllDetailsByListID(List<UUID> uuids);

    Boolean updateSystemOption(ConnectEInvoiceDTO connectEInvoiceDTO, UUID companyID);

    Boolean updateAfterPublishInvoice(Respone_SDS respone_sds);

    Boolean updateAfterPublishInvoiceReplaced(Respone_SDS respone_sds, EInvoice eInvoice);

    Boolean updateAfterPublishInvoiceAdjusted(Respone_SDS respone_sds, EInvoice eInvoice);

    Boolean updateAfterPublishInvoiceAdjusted(Respone_SDS respone_sds, List<EInvoice> eInvoices);

    Boolean updateAfterCreateKeyInvoiceNoWaitSign(Respone_SDS respone_sds);

    Boolean updateAfterCancelInvoice(Respone_SDS respone_sds);

    Boolean updateAfterConvertedOrigin(Respone_SDS respone_sds);

    Boolean updateSABill(List<InvoicesSDS> invoicesSDSList);

    Boolean updateSendMailAfterPublish(List<EInvoice> eInvoices);

    Boolean updateSendMail(Map<UUID, String> ikeyEmail);

    Page<EInvoice> findAll(Pageable pageable, UUID companyID, SearchVoucher searchVoucher, Integer typeEInvoice);

    InformationVoucherDTO getInformationVoucherByID(UUID id);

}
