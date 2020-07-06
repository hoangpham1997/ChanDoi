package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.EInvoice;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.Utils.RestfullAPI_SDS.Request_SDS;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.web.rest.dto.*;
import vn.softdreams.ebweb.service.dto.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing InvoiceType.
 */
public interface EInvoiceService {

    /**
     * Get all the invoiceTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<EInvoice> findAll(Pageable pageable);

    Page<EInvoice> findAll(Pageable pageable, SearchVoucher searchVoucher, Integer typeEInvoice);

    /**
     * Get the "id" invoiceType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<EInvoice> findOne(UUID id);

    Respone_SDS createEInvoice(List<UUID> uuids, UserDTO userDTO);

    Respone_SDS createEInvoice(UUID uuids);

    Respone_SDS getViewInvoicePDF(UUID uuids, String pattern, Integer option);

    ConnectEInvoiceDTO getConnectEInvoiceDTO();

    Boolean updateEISystemOption(ConnectEInvoiceDTO connectEInvoiceDTO);

    Respone_SDS publishInvoice(List<UUID> uuids);

    Respone_SDS getDigestData(List<UUID> uuids, String certString);

    Respone_SDS publishInvoiceWithCert(Request_SDS request_sds);

    Respone_SDS createInvoiceWaitSign(List<UUID> uuids);

    Respone_SDS cancelInvoice(RequestCancelInvoiceDTO requestCancelInvoiceDTO);

    Respone_SDS convertedOrigin(RequestConvertInvoiceDTO requestConvertInvoiceDTO);

    Respone_SDS connectEInvocie(ConnectEInvoiceDTO connectEInvoiceDTO);

    List<Respone_SDS> deleteEInovice(List<UUID> uuids);

    Page<EInvoice> findAllEInvoiceWaitSign(Pageable pageable);

    Page<EInvoice> findAllEInvoiceCanceled(Pageable pageable);

    Page<EInvoice> findAllEInvoiceForConvert(Pageable pageable);

    Page<EInvoice> findAllEInvoiceAdjusted(Pageable pageable);

    Page<EInvoice> findAllEInvoiceReplaced(Pageable pageable);

    Respone_SDS createEInoviceAdjusted(UUID uuid);

    Respone_SDS createEInoviceReplaced(UUID uuid);

    Respone_SDS sendMail(Map<UUID, String> ikeyEmail);

    InformationVoucherDTO getInformationVoucherByID(UUID id);

    String baoCaoTinhHinhSDHD(RequestReport requestReport);

    String bangKeHDChungTuHHDV(RequestReport requestReport);

    String baoCaoDoanhThuTheoSP(RequestReport requestReport);

    String baoCaoTheoDoanhThuTheoBenMua(RequestReport requestReport);

    Respone_SDS loadAndUpdateDataFromMIV();

    Respone_SDS loadDataToken();
}
