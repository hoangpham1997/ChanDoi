package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.ebweb.domain.BackupData;
import vn.softdreams.ebweb.domain.RestoreHistory;
import vn.softdreams.ebweb.service.RestoreHistoryService;

import java.util.UUID;

/**
 * Spring Data  repository for the Bank entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestoreHistoryRepository extends JpaRepository<RestoreHistory, UUID>, RestoreHistoryRepositoryCustom {

    Page<RestoreHistory> findAllByCompanyIDOrderByTimeRestoreDesc(Pageable pageable, UUID copanyID);
}
