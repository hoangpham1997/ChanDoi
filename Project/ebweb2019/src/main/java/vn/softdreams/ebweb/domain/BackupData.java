package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.softdreams.ebweb.service.dto.BackupDataDTO;
import vn.softdreams.ebweb.service.dto.cashandbank.MBDepositExportDTO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "backupdata")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SqlResultSetMappings({
    @SqlResultSetMapping(
        name = "BackupDataDTO",
        classes = {
            @ConstructorResult(
                targetClass = BackupDataDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = UUID.class),
                    @ColumnResult(name = "companyid", type = UUID.class),
                    @ColumnResult(name = "name", type = String.class),
                    @ColumnResult(name = "datebackup", type = LocalDateTime.class),
                    @ColumnResult(name = "path", type = String.class),
                    @ColumnResult(name = "size", type = BigDecimal.class),
                    @ColumnResult(name = "note", type = String.class),
                    @ColumnResult(name = "timeRestore", type = LocalDateTime.class),
                    @ColumnResult(name = "status", type = Integer.class),
                }
            )
        }
    )
})
public class BackupData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private LocalDateTime timeRestore;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Integer status;

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
