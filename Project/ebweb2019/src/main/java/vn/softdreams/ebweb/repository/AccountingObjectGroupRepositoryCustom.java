package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.AccountingObjectGroup;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the AccountingObjectGroup entity.
 */
@SuppressWarnings("unused")

public interface AccountingObjectGroupRepositoryCustom  {
    Page<AccountingObjectGroup> findAll(Pageable pageable, String accountingobjectgroupcode);

    List<AccountingObjectGroup> getAllAccountingObjectGroupSimilarBranch(List<UUID> allCompanyByCompanyIdAndCode, Boolean similarBranch, Boolean checkShared);

    List<AccountingObjectGroup> findAllAccountingObjectGroupSimilar(List<UUID> org, Boolean similarBranch, Boolean checkShared);
}
