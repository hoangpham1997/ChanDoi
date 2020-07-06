package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.BackupData;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BackupDataService {

    List<BackupData> backupData();

    List<BackupData> getAllBackupData();

    Page<BackupData> getAllBackupData(Pageable pageable);

    void delete(List<UUID> uuids);

    void restoreData(UUID id);

    Integer getDaysBackup();
}
