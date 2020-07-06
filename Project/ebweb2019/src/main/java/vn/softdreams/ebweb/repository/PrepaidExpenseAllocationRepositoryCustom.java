package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.PrepaidExpense;
import vn.softdreams.ebweb.service.dto.PrepaidExpenseAllocationConvertDTO;

import java.util.List;
import java.util.UUID;

public interface PrepaidExpenseAllocationRepositoryCustom {
    List<PrepaidExpenseAllocationConvertDTO> findAllByExpenseListItemID(Integer month, Integer year, UUID org, String currentBook, List<UUID> listID);

    Page<PrepaidExpense> getPrepaidExpenseByCurrentBookToModal(Pageable pageable, String fromDate, String toDate, Integer typeExpense, UUID org, boolean currentBook);
}
