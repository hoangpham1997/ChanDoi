package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.EbGroup;
import vn.softdreams.ebweb.domain.MBTellerPaper;

public class EbGroupSaveDTO {
    private EbGroup ebGroup;

    private int status;

    public EbGroupSaveDTO() {
    }

    public EbGroupSaveDTO(EbGroup ebGroup, int status) {
        this.ebGroup = ebGroup;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public EbGroup getEbGroup() {
        return ebGroup;
    }

    public void setEbGroup(EbGroup ebGroup) {
        this.ebGroup = ebGroup;
    }
}
