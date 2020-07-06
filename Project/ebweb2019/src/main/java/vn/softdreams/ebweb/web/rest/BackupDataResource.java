package vn.softdreams.ebweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.ebweb.domain.BackupData;
import vn.softdreams.ebweb.domain.RestoreHistory;
import vn.softdreams.ebweb.service.BackupDataService;
import vn.softdreams.ebweb.service.RestoreHistoryService;
import vn.softdreams.ebweb.web.rest.util.PaginationUtil;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/data")
public class BackupDataResource {
    private final BackupDataService backupDataService;
    private final RestoreHistoryService restoreHistoryService;

    public BackupDataResource(BackupDataService backupDataService, RestoreHistoryService restoreHistoryService) {
        this.backupDataService = backupDataService;
        this.restoreHistoryService = restoreHistoryService;
    }

    /**
     * @return
     * @author Hautv
     */
    @GetMapping("/get-all-data-backup")
    @Timed
    public ResponseEntity<List<BackupData>> getAllDataBackup(Pageable pageable) {
        Page<BackupData> page = backupDataService.getAllBackupData(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/get-all-data-backup");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return
     * @author Hautv
     */
    @GetMapping("/get-all-data-restore")
    @Timed
    public ResponseEntity<List<RestoreHistory>> getAllDataRestore(Pageable pageable) {
        Page<RestoreHistory> page = restoreHistoryService.getAllRestoreData(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/get-all-data-restore");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return
     * @author Hautv
     */
    @PostMapping("/backup")
    @Timed
    public ResponseEntity<List<BackupData>> backupData() {
        List<BackupData> dataBackupDTOS = backupDataService.backupData();
        return new ResponseEntity<>(dataBackupDTOS, HttpStatus.OK);
    }

    /**
     * @return
     * @author Hautv
     */
    @PostMapping("/delete")
    @Timed
    public ResponseEntity<List<BackupData>> delete(@RequestBody List<UUID> uuids) {
        backupDataService.delete(uuids);
        List<BackupData> dataBackupDTOS = backupDataService.getAllBackupData();
        return new ResponseEntity<>(dataBackupDTOS, HttpStatus.OK);
    }

    /**
     * @return
     * @author Hautv
     */
    @PostMapping(value = "/restore")
    @Timed
    public ResponseEntity<List<BackupData>> restore(@RequestBody List<UUID> uuids) {
        backupDataService.restoreData(uuids.get(0));
        List<BackupData> dataBackupDTOS = backupDataService.getAllBackupData();
        return new ResponseEntity<>(dataBackupDTOS, HttpStatus.OK);
    }
}
