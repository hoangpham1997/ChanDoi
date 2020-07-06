package vn.softdreams.ebweb.web.rest.dto;

import java.util.List;
import java.util.UUID;

public class TreeGetOrganizationUnitDTO {
    private OrganizationUnitCustomDTO parent;
    private List<TreeGetOrganizationUnitDTO> children;
    private UUID value;
    private String text;

    public OrganizationUnitCustomDTO getParent() {
        return parent;
    }

    public void setParent(OrganizationUnitCustomDTO parent) {
        this.parent = parent;
    }

    public List<TreeGetOrganizationUnitDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeGetOrganizationUnitDTO> children) {
        this.children = children;
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

    public TreeGetOrganizationUnitDTO() {
    }

    public TreeGetOrganizationUnitDTO(OrganizationUnitCustomDTO parent, List<TreeGetOrganizationUnitDTO> children, UUID value, String text) {
        this.parent = parent;
        this.children = children;
        this.value = value;
        this.text = text;
    }
}
