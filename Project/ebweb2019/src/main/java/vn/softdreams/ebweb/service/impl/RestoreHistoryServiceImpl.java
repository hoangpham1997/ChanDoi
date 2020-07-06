package vn.softdreams.ebweb.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.BackupData;
import vn.softdreams.ebweb.domain.RestoreHistory;
import vn.softdreams.ebweb.repository.RestoreHistoryRepository;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.RestoreHistoryService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RestoreHistoryServiceImpl implements RestoreHistoryService {

    private final RestoreHistoryRepository restoreHistoryRepository;

    public RestoreHistoryServiceImpl(RestoreHistoryRepository restoreHistoryRepository) {
        this.restoreHistoryRepository = restoreHistoryRepository;
    }

    @Override
    public List<BackupData> getAllRestoreData() {
        UUID compayID = SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg();
        return restoreHistoryRepository.getAllRestoreData(compayID);
    }

    @Override
    public Page<RestoreHistory> getAllRestoreData(Pageable pageable) {
        UUID compayID = SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg();
        return restoreHistoryRepository.findAllByCompanyIDOrderByTimeRestoreDesc(pageable, compayID);
    }
}
