package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MCAudit;

public class MCAuditSaveDTO {
    private MCAudit mcAudit;

    private int status;

    public MCAuditSaveDTO() {
    }

    public MCAuditSaveDTO(MCAudit mcAudit, int status) {
        this.mcAudit = mcAudit;
        this.status = status;
    }

    public MCAudit getMcAudit() {
        return mcAudit;
    }

    public void setMcAudit(MCAudit mcAudit) {
        this.mcAudit = mcAudit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
