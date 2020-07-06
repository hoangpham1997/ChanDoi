package vn.softdreams.ebweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.BackupData;
import vn.softdreams.ebweb.domain.RestoreHistory;

import java.util.List;

public interface RestoreHistoryService {
    List<BackupData> getAllRestoreData();

    Page<RestoreHistory> getAllRestoreData(Pageable pageable);
}
