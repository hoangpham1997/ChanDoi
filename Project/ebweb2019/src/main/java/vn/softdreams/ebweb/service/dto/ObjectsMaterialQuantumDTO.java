package vn.softdreams.ebweb.service.dto;

import java.time.LocalDate;
import java.util.UUID;

public class ObjectsMaterialQuantumDTO {
    private UUID objectID;
    private String objectCode;
    private String objectName;
    private String objectTypeString;
    private Integer objectType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Boolean isActive;


    public ObjectsMaterialQuantumDTO() {
    }

    public ObjectsMaterialQuantumDTO(UUID objectID, String objectCode, String objectName, String objectTypeString, Integer objectType, Boolean isActive) {
        this.objectID = objectID;
        this.objectCode = objectCode;
        this.objectName = objectName;
        this.objectTypeString = objectTypeString;
        this.objectType = objectType;
        this.isActive = isActive;
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

    public String getObjectTypeString() {
        return objectTypeString;
    }

    public void setObjectTypeString(String objectTypeString) {
        this.objectTypeString = objectTypeString;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        isActive = isActive;
    }
}
