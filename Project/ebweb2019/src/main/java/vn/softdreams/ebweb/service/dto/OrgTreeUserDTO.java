package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.web.rest.dto.OrgTreeTableDTO;

import java.util.List;

public class OrgTreeUserDTO {
    private List<OrgTreeTableDTO> listOrg;
    private UserDTO user;

    public List<OrgTreeTableDTO> getListOrg() {
        return listOrg;
    }

    public void setListOrg(List<OrgTreeTableDTO> listOrg) {
        this.listOrg = listOrg;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
