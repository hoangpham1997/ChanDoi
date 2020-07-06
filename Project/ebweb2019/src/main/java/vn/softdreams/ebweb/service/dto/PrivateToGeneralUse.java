package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

public class PrivateToGeneralUse {
    private UUID iD;
    private String code;
    private String name;
    private UUID companyID;
    private Integer type;
    private String nameCategory;

    public PrivateToGeneralUse(UUID iD, String code, String name, UUID companyID, Integer type, String nameCategory) {
        this.iD = iD;
        this.code = code;
        this.name = name;
        this.companyID = companyID;
        this.type = type;
        this.nameCategory = nameCategory;
    }

    public PrivateToGeneralUse() {
    }

    public UUID getiD() {
        return iD;
    }

    public void setiD(UUID iD) {
        this.iD = iD;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }
}
