package vn.softdreams.ebweb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "restorehistory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RestoreHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "backupdataid")
    private UUID backupDataID;

    @Column(name = "timerestore")
    private LocalDateTime timeRestore;

    @Column(name = "status")
    private Integer status;

    @Column(name = "companyid")
    private UUID companyID;

    @Column(name = "name")
    private String name;

    @Column(name = "datebackup")
    private LocalDateTime dateBackup;

    @Column(name = "path")
    private String path;

    @Column(name = "size")
    private BigDecimal size;

    @Column(name = "note")
    private String note;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBackupDataID() {
        return backupDataID;
    }

    public void setBackupDataID(UUID backupDataID) {
        this.backupDataID = backupDataID;
    }

    public LocalDateTime getTimeRestore() {
        return timeRestore;
    }

    public void setTimeRestore(LocalDateTime timeRestore) {
        this.timeRestore = timeRestore;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateBackup() {
        return dateBackup;
    }

    public void setDateBackup(LocalDateTime dateBackup) {
        this.dateBackup = dateBackup;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
