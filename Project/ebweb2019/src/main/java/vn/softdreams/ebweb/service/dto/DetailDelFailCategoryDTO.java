package vn.softdreams.ebweb.service.dto;

public class DetailDelFailCategoryDTO {
    private String name;
    private String code;
    private String des;

    public DetailDelFailCategoryDTO(String name, String code, String des) {
        this.name = name;
        this.code = code;
        this.des = des;
    }

    public DetailDelFailCategoryDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
