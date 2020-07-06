package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.MCAudit;
import vn.softdreams.ebweb.domain.MCAuditDetailMember;
import vn.softdreams.ebweb.domain.MCAuditDetails;

import java.util.List;

public class MCAuditDetailDTO {

    private List<MCAuditDetails> mcAuditDetails;
    private List<MCAuditDetailMember> mcAuditDetailMembers;
    private List<RefVoucherDTO> refVouchers;

    public MCAuditDetailDTO() {
    }

    public List<MCAuditDetails> getMcAuditDetails() {
        return mcAuditDetails;
    }

    public void setMcAuditDetails(List<MCAuditDetails> mcAuditDetails) {
        this.mcAuditDetails = mcAuditDetails;
    }

    public List<MCAuditDetailMember> getMcAuditDetailMembers() {
        return mcAuditDetailMembers;
    }

    public void setMcAuditDetailMembers(List<MCAuditDetailMember> mcAuditDetailMembers) {
        this.mcAuditDetailMembers = mcAuditDetailMembers;
    }

    public List<RefVoucherDTO> getRefVouchers() {
        return refVouchers;
    }

    public void setRefVouchers(List<RefVoucherDTO> refVouchers) {
        this.refVouchers = refVouchers;
    }
}
