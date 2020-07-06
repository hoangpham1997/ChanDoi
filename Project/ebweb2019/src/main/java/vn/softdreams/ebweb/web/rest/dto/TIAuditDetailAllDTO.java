package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.TIAuditMemberDetails;

import java.util.List;

public class TIAuditDetailAllDTO {
    private List<TIAuditDetailByIDDTO> tiAuditDetailByIDDTOS;
    private List<TIAuditMemberDetailByIDDTO> tiAuditMemberDetailDTOS;
    private List<RefVoucherDTO> refVouchers;

    public TIAuditDetailAllDTO(List<TIAuditDetailByIDDTO> tiAuditDetailByIDDTOS, List<TIAuditMemberDetailByIDDTO> tiAuditMemberDetailDTOS) {
        this.tiAuditDetailByIDDTOS = tiAuditDetailByIDDTOS;
        this.tiAuditMemberDetailDTOS = tiAuditMemberDetailDTOS;
    }

    public TIAuditDetailAllDTO() {
    }

    public List<RefVoucherDTO> getRefVouchers() {
        return refVouchers;
    }

    public void setRefVouchers(List<RefVoucherDTO> refVouchers) {
        this.refVouchers = refVouchers;
    }

    public List<TIAuditDetailByIDDTO> getTiAuditDetailByIDDTOS() {
        return tiAuditDetailByIDDTOS;
    }

    public void setTiAuditDetailByIDDTOS(List<TIAuditDetailByIDDTO> tiAuditDetailByIDDTOS) {
        this.tiAuditDetailByIDDTOS = tiAuditDetailByIDDTOS;
    }

    public List<TIAuditMemberDetailByIDDTO> getTiAuditMemberDetailDTOS() {
        return tiAuditMemberDetailDTOS;
    }

    public void setTiAuditMemberDetailDTOS(List<TIAuditMemberDetailByIDDTO> tiAuditMemberDetailDTOS) {
        this.tiAuditMemberDetailDTOS = tiAuditMemberDetailDTOS;
    }
}
