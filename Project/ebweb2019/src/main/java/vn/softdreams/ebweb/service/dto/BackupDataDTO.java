package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class BackupDataDTO {
    private UUID id;

    private UUID companyID;

    private String name;

    private LocalDateTime dateBackup;

    private String path;

    private BigDecimal size;

    private String note;

    private LocalDateTime timeRestore;

    private Integer status;

    public BackupDataDTO(UUID id, UUID companyID, String name, LocalDateTime dateBackup, String path, BigDecimal size, String note, LocalDateTime timeRestore, Integer status) {
        this.id = id;
        this.companyID = companyID;
        this.name = name;
        this.dateBackup = dateBackup;
        this.path = path;
        this.size = size;
        this.note = note;
        this.timeRestore = timeRestore;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
