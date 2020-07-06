package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.ReceiveBill;
import vn.softdreams.ebweb.service.dto.PPInvoiceConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.LotNoDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PPInvoiceDetailsRepositoryCustom {
    void saveReceiveBillPPInvoice(ReceiveBill receiveBill, Integer vATRate);

    /**
     * lấy chứng từ tham chiếu
     * @param pageable
     * @param accountingObjectID
     * @param formDate
     * @param toDate
     * @param currencyCode
     * @param listID
     * @param org
     * @param currentBook loại sổ
     * @return
     */
    Page<PPInvoiceConvertDTO> getPPInvoiceDetailsGetLicense(Pageable pageable, UUID accountingObjectID, String formDate, String toDate, String currencyCode, List<UUID> listID, UUID org, String currentBook);

    List<LotNoDTO> getListLotNo(UUID materialGoodsID);

    int checkReferences(UUID id);

    void updateTotalReceiveBill(List<UUID> uuidList, UUID orgID);
}
