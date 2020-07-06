package vn.softdreams.ebweb.service.impl;

import org.apache.commons.io.IOUtils;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.ebweb.domain.BackupData;
import vn.softdreams.ebweb.domain.RestoreHistory;
import vn.softdreams.ebweb.repository.BackupDataRepository;
import vn.softdreams.ebweb.repository.RestoreHistoryRepository;
import vn.softdreams.ebweb.security.SecurityUtils;
import vn.softdreams.ebweb.service.BackupDataService;
import vn.softdreams.ebweb.service.UserService;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Service Implementation for BackupData.
 */
@Service
@Transactional
public class BackupDataServiceImpl implements BackupDataService {

    /*@Autowired
    RedisTemplate redisTemplate;*/

    private final BackupDataRepository backupDataRepository;
    private final RestoreHistoryRepository restoreHistoryRepository;

    public BackupDataServiceImpl(BackupDataRepository backupDataRepository, RestoreHistoryRepository restoreHistoryRepository) {
        this.backupDataRepository = backupDataRepository;
        this.restoreHistoryRepository = restoreHistoryRepository;
    }

    @Autowired
    private UserService userService;

    @Override
    public List<BackupData> backupData() {
        UserDTO userDTO = userService.getAccount();
        List<String> sqlInsert = backupDataRepository.backupDataString(userDTO.getOrganizationUnit().getId());
        File currentDirectory = new File(new File("").getAbsolutePath());
        if (!new File(currentDirectory.getAbsolutePath() + "/Backup").exists()) {
            new File(currentDirectory.getAbsolutePath() + "/Backup").mkdir();
        }
        if (!new File(currentDirectory.getAbsolutePath() + "/Backup/" + Utils.uuidConvertToGUID(userDTO.getOrganizationUnit().getId())).exists()) {
            new File(currentDirectory.getAbsolutePath() + "/Backup/" + Utils.uuidConvertToGUID(userDTO.getOrganizationUnit().getId())).mkdir();
        }
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
//            redisTemplate.opsForList().rightPush("backupData", SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg());
            String nameFile = "Backup-" + Utils.convertDate_C_YYYYMMDD_HHMMSS(localDateTime) + ".zip";
            String path = currentDirectory.getAbsolutePath() + "/Backup/" + Utils.uuidConvertToGUID(userDTO.getOrganizationUnit().getId()) + "/" + nameFile;
//            genFileSQL(sqlInsert, currentDirectory.getAbsolutePath() + "/BackUp/" + Utils.uuidConvertToGUID(userDTO.getOrganizationUnit().getId()) + "/backup.sql");
            genFileZip(sqlInsert, path, nameFile);
            BackupData backupData = new BackupData();
            backupData.setCompanyID(userDTO.getOrganizationUnit().getId());
            backupData.setDateBackup(localDateTime);
            backupData.setName(nameFile);
            File file = new File(path);
            backupData.setSize(BigDecimal.valueOf(file.length()));
            backupData.setPath(path);
            backupDataRepository.save(backupData);
//            Thread.sleep(30000);
//            redisTemplate.opsForList().remove("backupData", 0, SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg());
        } catch (IOException e) {
//            redisTemplate.opsForList().remove("backupData", 0, SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg());
            e.printStackTrace();
        }
        return backupDataRepository.findAllByCompanyIDOrderByDateBackupDesc(userDTO.getOrganizationUnit().getId());
    }

    @Override
    public List<BackupData> getAllBackupData() {
        UUID compayID = SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg();
        return backupDataRepository.findAllByCompanyIDOrderByDateBackupDesc(compayID);
    }

    @Override
    public Page<BackupData> getAllBackupData(Pageable pageable) {
        UUID compayID = SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg();
        return backupDataRepository.findAllByCompanyIDOrderByDateBackupDesc(pageable, compayID);
    }

    @Override
    public void delete(List<UUID> uuids) {
        backupDataRepository.deleteAllByListID(uuids);
    }

    @Override
    public void restoreData(UUID id) {
        StringBuilder sqlRestore = new StringBuilder();
        BackupData backupData = backupDataRepository.findById(id).get();
        if (new File(backupData.getPath()).exists()) {
//            redisTemplate.opsForList().rightPush("restoreData", SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg());
//            redisTemplate.boundSetOps("restore").re
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(backupData.getPath());
                Enumeration<? extends ZipEntry> entries = zipFile.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    InputStream stream = zipFile.getInputStream(entry);
                    String result = IOUtils.toString(stream, "UTF-8");
                    sqlRestore.append(decodeToBase64(result));
                }
                // backup thời điểm hiện tại
                UserDTO userDTO = userService.getAccount();
                List<String> sqlInsert = backupDataRepository.backupDataString(userDTO.getOrganizationUnit().getId());
                LocalDateTime localDateTime = LocalDateTime.now();
                String nameFile = "Backup-" + Utils.convertDate_C_YYYYMMDD_HHMMSS(localDateTime) + ".zip";
                File currentDirectory = new File(new File("").getAbsolutePath());
                String path = currentDirectory.getAbsolutePath() + "/Backup/" + Utils.uuidConvertToGUID(userDTO.getOrganizationUnit().getId()) + "/" + nameFile;
                genFileZip(sqlInsert, path, nameFile);
                BackupData bpDT = new BackupData();
                bpDT.setCompanyID(userDTO.getOrganizationUnit().getId());
                bpDT.setDateBackup(localDateTime);
                bpDT.setName(nameFile);
                File file = new File(path);
                bpDT.setSize(BigDecimal.valueOf(file.length()));
                bpDT.setPath(path);
                bpDT.setNote("Dữ liệu sao lưu trước khi khôi phục dữ liệu");
                backupDataRepository.save(bpDT);
                // bắt đầu restore
                backupDataRepository.restoreData(sqlRestore);

                RestoreHistory restoreHistory = new RestoreHistory();
                BeanUtils.copyProperties(backupData, restoreHistory);
                restoreHistory.setId(null);
                restoreHistory.setTimeRestore(LocalDateTime.now());
                restoreHistory.setStatus(0); // Không thành công
                restoreHistoryRepository.save(restoreHistory);
//                redisTemplate.opsForList().remove("restoreData", 0, SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg());
            } catch (IOException e) {
                RestoreHistory restoreHistory = new RestoreHistory();
                BeanUtils.copyProperties(backupData, restoreHistory);
                restoreHistory.setId(null);
                restoreHistory.setTimeRestore(LocalDateTime.now());
                restoreHistory.setStatus(1); // Không thành công
                restoreHistoryRepository.save(restoreHistory);
//                redisTemplate.opsForList().remove("restoreData", 0, SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg());
                e.printStackTrace();
                throw new BadRequestAlertException("Restore fail", "", "err");
            } catch (SQLGrammarException ex) {
                RestoreHistory restoreHistory = new RestoreHistory();
                BeanUtils.copyProperties(backupData, restoreHistory);
                restoreHistory.setId(null);
                restoreHistory.setTimeRestore(LocalDateTime.now());
                restoreHistory.setStatus(1); // Không thành công
                restoreHistoryRepository.save(restoreHistory);
                throw new BadRequestAlertException("Restore fail", "", "err");
            }
        } else {
            throw new BadRequestAlertException("Không tồn tại file backup", "", "noExists");
        }

    }

    @Override
    public Integer getDaysBackup() {
        return backupDataRepository.getDaysBackup(SecurityUtils.getCurrentUserLoginAndOrg().get().getOrg());
    }

    private void genFileSQL(List<String> data, String path) throws IOException {
        FileWriter writer = new FileWriter(path);
        writer.write("-- Backup : " + LocalDate.now() + System.lineSeparator());
        for (String str : data) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
    }

    private void genFileZip(List<String> data, String path, String nameFile) throws IOException {
        StringBuilder sql = new StringBuilder();
        sql.append("-- Backup : " + LocalDate.now() + System.lineSeparator());
        for (String str : data) {
            sql.append(str + System.lineSeparator());
        }
        File f = new File(path);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f), StandardCharsets.UTF_8);
        ZipEntry e = new ZipEntry(nameFile.substring(0, nameFile.length() - 4) + ".sql");
        out.putNextEntry(e);
        byte[] dataByte = encodeToBase64(sql.toString()).getBytes();
        out.write(dataByte, 0, dataByte.length);
        out.closeEntry();
        out.close();
    }

    String encodeToBase64(String value) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(value.getBytes("UTF-8"));
    }

    String decodeToBase64(String value) throws UnsupportedEncodingException {
        return new String(Base64.getDecoder().decode(value), "UTF-8");
    }
}
