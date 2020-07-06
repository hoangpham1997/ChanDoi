package vn.softdreams.ebweb.service.dto;

import java.time.LocalDate;
import java.util.UUID;

public class MaterialQuantumGeneralDTO {
    private UUID id;
    private UUID objectID;
    private String objectCode;
    private String objectName;
    private LocalDate fromDate;
    private LocalDate toDate;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getObjectID() {
        return objectID;
    }

    public void setObjectID(UUID objectID) {
        this.objectID = objectID;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public MaterialQuantumGeneralDTO() {
    }

    public MaterialQuantumGeneralDTO(UUID id, UUID objectID, String objectCode, String objectName, LocalDate fromDate, LocalDate toDate) {
        this.id = id;
        this.objectID = objectID;
        this.objectCode = objectCode;
        this.objectName = objectName;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
