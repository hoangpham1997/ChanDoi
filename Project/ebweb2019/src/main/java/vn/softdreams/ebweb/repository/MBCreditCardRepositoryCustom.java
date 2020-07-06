package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MBCreditCard;
import vn.softdreams.ebweb.domain.SearchVoucher;
import vn.softdreams.ebweb.service.dto.cashandbank.MBCreditCardExportDTO;
import vn.softdreams.ebweb.web.rest.dto.MBCreditCardViewDTO;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MBCreditCardRepositoryCustom {
    Page<MBCreditCardViewDTO> findAll(Pageable sort, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook);

    MBCreditCard findByRowNum(SearchVoucher searchVoucher, Number rowNum, UUID companyID, Boolean isNoMBook);

    Page<MBCreditCardExportDTO> getAllMBCreditCards(Pageable pageable, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook);

    UpdateDataDTO getNoBookById(UUID paymentVoucherID);

    List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher, UUID companyID, Boolean isNoMBook);

    void multiDeleteMBCreditCard(UUID orgID, List<UUID> uuidList);

    void multiDeleteChildMBCreditCard(String tableChildName, List<UUID> uuidList);

}
