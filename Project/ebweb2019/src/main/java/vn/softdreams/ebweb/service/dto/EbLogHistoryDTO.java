package vn.softdreams.ebweb.service.dto;

import java.time.LocalDate;
import java.util.UUID;

public class EbLogHistoryDTO {
    private UUID Id;
    private Long userID;
    private String beforeChange;
    private String afterChange;
    private LocalDate changedTime;

    public EbLogHistoryDTO() {
    }

    public EbLogHistoryDTO(UUID Id, Long userID, String beforeChange, String afterChange, LocalDate time) {
        this.Id = Id;
        this.userID = userID;
        this.beforeChange = beforeChange;
        this.afterChange = afterChange;
        this.changedTime = time;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getBeforeChange() {
        return beforeChange;
    }

    public void setBeforeChange(String beforeChange) {
        this.beforeChange = beforeChange;
    }

    public String getAfterChange() {
        return afterChange;
    }

    public void setAfterChange(String afterChange) {
        this.afterChange = afterChange;
    }

    public LocalDate getChangedTime() {
        return changedTime;
    }

    public void setChangedTime(LocalDate changedTime) {
        this.changedTime = changedTime;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }
}
