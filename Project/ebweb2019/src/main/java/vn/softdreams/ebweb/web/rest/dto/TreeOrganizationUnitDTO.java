package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.OrganizationUnit;

import java.util.List;

public class TreeOrganizationUnitDTO {
    private OrganizationUnit parent;
    private List<TreeOrganizationUnitDTO> children;

    public OrganizationUnit getParent() {
        return parent;
    }

    public void setParent(OrganizationUnit parent) {
        this.parent = parent;
    }

    public List<TreeOrganizationUnitDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeOrganizationUnitDTO> children) {
        this.children = children;
    }

    public TreeOrganizationUnitDTO(OrganizationUnit parent, List<TreeOrganizationUnitDTO> children) {
        this.parent = parent;
        this.children = children;
    }

    public TreeOrganizationUnitDTO() {
    }
}
