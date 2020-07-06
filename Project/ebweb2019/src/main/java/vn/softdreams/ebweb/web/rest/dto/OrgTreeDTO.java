package vn.softdreams.ebweb.web.rest.dto;

import java.util.List;
import java.util.UUID;

public class OrgTreeDTO {
    private UUID value;
    private String text;
    private List<OrgTreeDTO> children;
    private Boolean isParentCompany;
    private Integer accType;

    public OrgTreeDTO(UUID value, String text, List<OrgTreeDTO> children) {
        this.value = value;
        this.text = text;
        this.children = children;
    }

    public OrgTreeDTO(UUID value, String text, List<OrgTreeDTO> children, Boolean isParentCompany, Integer accType) {
        this.value = value;
        this.text = text;
        this.children = children;
        this.isParentCompany = isParentCompany;
        this.accType = accType;
    }
    public OrgTreeDTO(UUID value, String text, List<OrgTreeDTO> children, Integer accType) {
        this.value = value;
        this.text = text;
        this.children = children;
        this.accType = accType;
    }

    public Integer getAccType() {
        return accType;
    }

    public void setAccType(Integer accType) {
        this.accType = accType;
    }

    public OrgTreeDTO() {
    }

    public Boolean getParentCompany() {
        return isParentCompany;
    }

    public void setParentCompany(Boolean parentCompany) {
        isParentCompany = parentCompany;
    }

    public UUID getValue() {
        return value;
    }

    public void setValue(UUID value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<OrgTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<OrgTreeDTO> children) {
        this.children = children;
    }
}
