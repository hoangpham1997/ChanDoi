package vn.softdreams.ebweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.softdreams.ebweb.domain.BackupData;

import java.util.List;
import java.util.UUID;

public interface BackupDataRepository extends JpaRepository<BackupData, UUID>, BackupDataRepositoryCustom {
    List<BackupData> findAllByCompanyIDOrderByDateBackupDesc(UUID companyID);

    Page<BackupData> findAllByCompanyIDOrderByDateBackupDesc(Pageable pageable, UUID companyID);

    @Modifying
    @Query(value = "delete BackupData where id in ?1", nativeQuery = true)
    void deleteAllByListID(List<UUID> uuids);

    /*@Query(value = "DECLARE " +
        "    @dateLast datetime; " +
        "set @dateLast = (select DISTINCT MAX(DateBackup) " +
        "                 from BackupData " +
        "                 where CompanyID = ?1 ); " +
        "if @dateLast is null " +
        "    begin " +
        "        set @dateLast = " +
        "                (select DISTINCT MAX(date) from ViewVoucherNo where CompanyID = ?1 ); " +
        "        if (@dateLast is not null) " +
        "            begin " +
        "                SELECT DATEDIFF(day, @dateLast, GETDATE()) AS DiffDays " +
        "            end " +
        "        else " +
        "            begin " +
        "                select -1 " +
        "            end " +
        "    end " +
        "else " +
        "    begin " +
        "        SELECT DATEDIFF(day, @dateLast, GETDATE()) AS DiffDays " +
        "    end", nativeQuery = true)
    Integer getDaysBackup(UUID companyID);*/
    @Query(value = "DECLARE " +
        "    @dateLast datetime; " +
        "set @dateLast = (select DISTINCT MAX(DateBackup) " +
        "                 from BackupData " +
        "                 where CompanyID = ?1); " +
        "if @dateLast is null " +
        "    begin " +
        "        select -1 " +
        "    end " +
        "else " +
        "    begin " +
        "        SELECT DATEDIFF(day, @dateLast, GETDATE()) AS DiffDays " +
        "    end", nativeQuery = true)
    Integer getDaysBackup(UUID companyID);
}
