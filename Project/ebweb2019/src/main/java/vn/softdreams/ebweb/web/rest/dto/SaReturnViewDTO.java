package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.RSInwardOutward;
import vn.softdreams.ebweb.domain.SaBill;
import vn.softdreams.ebweb.domain.SaReturn;
import vn.softdreams.ebweb.domain.SaReturnDetails;

import java.time.LocalDateTime;
import java.util.List;

public class SaReturnViewDTO {
    private SaReturn saReturn;
    private List<SaReturnDetailsViewDTO> saReturnDetails;
    private List<RefVoucherDTO> viewVouchers;
    private RSInwardOutwardDTO rsInwardOutward;
    private Integer totalRow;
    private String noFBook;
    private String noMBook;
    private String reason;
    private LocalDateTime refDateTime;

    public SaReturnViewDTO() {
    }

    public SaReturnViewDTO(SaReturn saReturn, List<RefVoucherDTO> viewVouchers) {
        this.saReturn = saReturn;
        this.viewVouchers = viewVouchers;
    }

    public LocalDateTime getRefDateTime() {
        return refDateTime;
    }

    public void setRefDateTime(LocalDateTime refDateTime) {
        this.refDateTime = refDateTime;
    }

    public SaReturn getSaReturn() {
        return saReturn;
    }

    public void setSaReturn(SaReturn saReturn) {
        this.saReturn = saReturn;
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public Integer getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }

    public RSInwardOutwardDTO getRsInwardOutward() {
        return rsInwardOutward;
    }

    public void setRsInwardOutward(RSInwardOutwardDTO rsInwardOutward) {
        this.rsInwardOutward = rsInwardOutward;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<SaReturnDetailsViewDTO> getSaReturnDetails() {
        return saReturnDetails;
    }

    public void setSaReturnDetails(List<SaReturnDetailsViewDTO> saReturnDetails) {
        this.saReturnDetails = saReturnDetails;
    }
}
