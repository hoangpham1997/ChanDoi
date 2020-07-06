package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.BankAccountDetails;
import vn.softdreams.ebweb.service.dto.BankAccountDetailDTO;
import vn.softdreams.ebweb.service.dto.ComboboxBankAccountDetailDTO;

import java.util.List;
import java.util.UUID;

public interface BankAccountDetailsRepositoryCustom {
    Page<BankAccountDetails> pageableAllBankAccountDetails(Pageable sort, List<UUID> org);

    Page<BankAccountDetails> getAllByListCompany(Pageable pageable, List<UUID> listCompanyID);

    BankAccountDetailDTO getByPaymentVoucherId(UUID id, Integer typeId);

    List<BankAccountDetails> findAllByIsActiveCustom(List<UUID> orgTKNH,List<UUID> orgTTD, UUID parentID);

    List<BankAccountDetails> getAllBankAccountDetailsByOrgID(List<UUID> orgTKNH,List<UUID> orgTTD, UUID parentID);

    List<ComboboxBankAccountDetailDTO> findAllByIsActive(List<UUID> companyIDTKNH, List<UUID> companyIDTTD, UUID orgID);

    List<ComboboxBankAccountDetailDTO> findAllForAccType(UUID org,UUID org2);
}
