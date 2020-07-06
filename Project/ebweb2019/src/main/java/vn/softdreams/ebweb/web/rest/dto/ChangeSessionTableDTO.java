package vn.softdreams.ebweb.web.rest.dto;

import java.util.List;

public class ChangeSessionTableDTO {
    private List<OrgTreeTableDTO> orgTrees;
    private String book;
    private OrgTreeTableDTO currentOrg;

    public ChangeSessionTableDTO() {
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public List<OrgTreeTableDTO> getOrgTrees() {
        return orgTrees;
    }

    public void setOrgTrees(List<OrgTreeTableDTO> orgTrees) {
        this.orgTrees = orgTrees;
    }

    public OrgTreeTableDTO getCurrentOrg() {
        return currentOrg;
    }

    public void setCurrentOrg(OrgTreeTableDTO currentOrg) {
        this.currentOrg = currentOrg;
    }
}
