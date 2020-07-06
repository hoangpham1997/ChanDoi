package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.BackupData;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data  repository for the Bank entity.
 */
@SuppressWarnings("unused")
public interface RestoreHistoryRepositoryCustom {
    List<BackupData> getAllRestoreData(UUID uuid);
}
