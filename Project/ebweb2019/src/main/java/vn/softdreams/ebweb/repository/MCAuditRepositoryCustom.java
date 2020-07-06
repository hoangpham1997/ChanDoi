package vn.softdreams.ebweb.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.MCAudit;
import vn.softdreams.ebweb.domain.MCAuditDetailMember;
import vn.softdreams.ebweb.domain.MCAuditDetails;
import vn.softdreams.ebweb.service.dto.cashandbank.MCAuditDTO;
import vn.softdreams.ebweb.web.rest.dto.SAInvoiceViewDTO;

import java.util.List;
import java.util.UUID;

public interface MCAuditRepositoryCustom {

    Page<MCAuditDTO> findAllDTOByCompanyID(Pageable pageable, UUID companyID, int displayOnBook);

    Page<MCAuditDTO> searchMCAudit(Pageable pageable, String currencyID, String fromDate, String toDate, String textSearch, UUID org, String currentBook, boolean statusExport);

    MCAudit findByRowNum(String currencyID, String fromDate, String toDate, String keySearch, Integer rowNum, UUID org, String currentBook);

    List<MCAuditDetails> findDetailByMCAuditID(UUID mCAuditID);

    List<MCAuditDetailMember> findDetailMemberByMCAuditID(UUID mCAuditID);

    void multiDeleteMCAudit(UUID org, List<UUID> uuidListMCAudit);

    void multiDeleteMCAuditChild(String mbDepositDetail, List<UUID> uuidListMCAudit);

}
