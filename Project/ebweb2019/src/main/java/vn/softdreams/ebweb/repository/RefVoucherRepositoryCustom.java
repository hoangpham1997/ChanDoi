package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherSecondDTO;

import java.util.List;
import java.util.UUID;

public interface RefVoucherRepositoryCustom {

    /**
     * @param pageable
     * @param typeGroup
     * @param no
     * @param invoiceNo
     * @param recorded
     * @param fromDate
     * @param toDate
     * @param isNoMBook
     * @param companyID
     * @param typeSearch
     * @param status
     * @return
     * @author hieugie
     */
    Page<RefVoucherDTO> getAllRefViewVoucher(Pageable pageable, Integer typeGroup, String no, String invoiceNo, Boolean recorded,
                                             String fromDate, String toDate, boolean isNoMBook, UUID companyID, Integer typeSearch, Integer status);

    /**
     * @param id
     * @return
     * @Author hieugie
     */
    List<RefVoucherDTO> getRefViewVoucher(UUID id, boolean isNoMBook);

    /**
     * @param id
     * @param isNoMBook
     * @return
     * @Author Hautv
     */
    List<RefVoucherDTO> getRefViewVoucherByMCReceiptID(UUID id, boolean isNoMBook);

    List<RefVoucherDTO> getRefVouchersByMBDepositID(UUID id, boolean isNoMBook);

    /**
     * @param id
     * @param isNoMBook
     * @return
     * @Author Hautv
     */
    List<RefVoucherDTO> getRefViewVoucherByPaymentVoucherID(Integer typeID, UUID id, boolean isNoMBook);

    Page<RefVoucherSecondDTO> getViewVoucherToModal(Pageable pageable, Integer typeGroup, String fromDate, String toDate, boolean isNoMBook, UUID org);
}
