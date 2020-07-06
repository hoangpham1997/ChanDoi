package vn.softdreams.ebweb.web.rest.dto;

import java.util.List;
import java.util.UUID;

public class AuthorityTreeDTO {
    private UUID id;
    private String code;
    private String name;
    private String parentCode;
    private List<AuthorityTreeDTO> children;

    public AuthorityTreeDTO(UUID id, String code, String name, String parentCode, List<AuthorityTreeDTO> children
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
        this.children = children;
    }

    public AuthorityTreeDTO() {
    }

    public List<AuthorityTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<AuthorityTreeDTO> children) {
        this.children = children;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}
