package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.*;
import vn.softdreams.ebweb.security.SecurityDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBDepositExportDTO;
import vn.softdreams.ebweb.web.rest.dto.MBDepositViewDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MBDepositRepositoryCustom {
    Page<MBDepositViewDTO> findAll(Pageable sort, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook);

    MBDeposit findByRowNum(SearchVoucher searchVoucher, Number rowNum, UUID companyID, Boolean isNoMBook);

    void mutipleRecord(Optional<User> userOptional, Optional<SecurityDTO> currentUserLoginAndOrg, MutipleRecord mutipleRecord);

    void mutipleUnRecord(List<UUID> listUnRecord, UUID orgID);

    void multiDeleteMBDeposit(UUID orgID, List<UUID> uuidList);

    void multiDeleteMBDepositChild(String tableChildName, List<UUID> uuidList);

    void multiDeleteGeneralLedger(UUID orgID, List<UUID> uuidList);

    void multiDeleteSAInvoice(UUID orgID, List<UUID> listIDSAInvoice);

    void multiDeleteSAInvoiceDetails(List<UUID> listIDSAInvoice);

    List<UUID> findSAInvoiceIDByMBDepositID(UUID orgID, List<UUID> listUUID);

    Page<MBDepositExportDTO> getAllMBDeposits(Pageable pageable, SearchVoucher searchVoucher, UUID org, Boolean isNoMBook);

    List<Number> getIndexRow(UUID id, SearchVoucher searchVoucher, UUID companyID, Boolean isNoMBook);

//    String findRefTable(List<Integer> typeID, UUID orgID);

}
