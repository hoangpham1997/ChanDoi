package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.domain.OrganizationUnit;
import vn.softdreams.ebweb.web.rest.dto.OrgTreeTableDTO;

import java.util.UUID;

public class OrgGroupDTO {
    private UUID orgId;
    private EbGroup ebGroup;

    public EbGroup getEbGroup() {
        return ebGroup;
    }

    public void setEbGroup(EbGroup ebGroup) {
        this.ebGroup = ebGroup;
    }

    public UUID getOrgId() {
        return orgId;
    }

    public void setOrgId(UUID orgId) {
        this.orgId = orgId;
    }
}
