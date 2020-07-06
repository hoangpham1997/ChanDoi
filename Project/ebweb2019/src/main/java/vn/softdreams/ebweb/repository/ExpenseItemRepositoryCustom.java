package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountList;
import vn.softdreams.ebweb.domain.ExpenseItem;
import vn.softdreams.ebweb.service.dto.AccountForAccountDefaultDTO;
import vn.softdreams.ebweb.service.dto.AccountListDTO;

import java.util.List;
import java.util.UUID;

public interface ExpenseItemRepositoryCustom {

    List<ExpenseItem> findAllExpenseItemSimilarBranch(Boolean similarBranch, List<UUID> listID);
}
