package vn.softdreams.ebweb.web.rest.dto;

import java.util.List;

public class ChangeSessionDTO {
    private List<TreeGetOrganizationUnitDTO> orgTrees;
    private String book;
    private OrgTreeDTO currentOrg;
    private Boolean status;
    private Boolean childDependent;
    private TreeGetOrganizationUnitDTO currentOrgLogin;

    public ChangeSessionDTO() {
    }

    public Boolean getChildDependent() {
        return childDependent;
    }

    public void setChildDependent(Boolean childDependent) {
        this.childDependent = childDependent;
    }

    public List<TreeGetOrganizationUnitDTO> getOrgTrees() {
        return orgTrees;
    }

    public void setOrgTrees(List<TreeGetOrganizationUnitDTO> orgTrees) {
        this.orgTrees = orgTrees;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public OrgTreeDTO getCurrentOrg() {
        return currentOrg;
    }

    public void setCurrentOrg(OrgTreeDTO currentOrg) {
        this.currentOrg = currentOrg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public TreeGetOrganizationUnitDTO getCurrentOrgLogin() {
        return currentOrgLogin;
    }

    public void setCurrentOrgLogin(TreeGetOrganizationUnitDTO currentOrgLogin) {
        this.currentOrgLogin = currentOrgLogin;
    }
}
