package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.GOtherVoucher;
import vn.softdreams.ebweb.service.dto.PrepaidExpenseAllDTO;
import vn.softdreams.ebweb.service.dto.PrepaidExpenseCodeDTO;
import vn.softdreams.ebweb.service.dto.PrepaidExpenseConvertDTO;
import vn.softdreams.ebweb.web.rest.dto.RefVoucherSecondDTO;

import java.util.List;
import java.util.UUID;

public interface PrepaidExpenseRepositoryCustom {
    List<PrepaidExpenseCodeDTO> getPrepaidExpenseCode(List<UUID> listCompanyID, UUID companyID, String currentBook);

    List<AccountList> getCostAccounts(UUID org);

    Page<PrepaidExpenseAllDTO> getAll(Pageable pageable, Integer typeExpense, String fromDate, String toDate, String textSearch, UUID org, String currentBook);

    List<RefVoucherSecondDTO> findPrepaidExpenseByID(UUID id, UUID org, String currentBook);

    List<PrepaidExpenseConvertDTO> getPrepaidExpenseAllocation(Integer month, Integer year, UUID org, String currentBook);

    Long countAllByPrepaidExpenseCode(String prepaidExpenseCode, UUID org, UUID id, String currentBook);

    Long countAllByMonthAndYear(Integer month, Integer year, UUID org, String currentBook);

    GOtherVoucher getPrepaidExpenseAllocationDuplicate(Integer month, Integer year, UUID org, String currentBook);

    void updateAllocatedPeriod(boolean check, List<UUID> pre);

    List<PrepaidExpenseCodeDTO> getPrepaidExpenseCodeCanActive(List<UUID> allCompanyByCompanyIdAndCode, UUID org, String currentBook);
}
