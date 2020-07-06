package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.RSInwardOutward;
import vn.softdreams.ebweb.domain.RefVoucher;

import java.util.List;

public class RSInwardOutwardSaveDTO {
    private RSInwardOutward  rsInwardOutward;
    private List<RefVoucher> viewVouchers;
    private RSInwardOutwardDTO rsInwardOutwardDTO;

    public RSInwardOutwardDTO getRsInwardOutwardDTO() {
        return rsInwardOutwardDTO;
    }

    public void setRsInwardOutwardDTO(RSInwardOutwardDTO rsInwardOutwardDTO) {
        this.rsInwardOutwardDTO = rsInwardOutwardDTO;
    }

    private Integer currentBook;
    private Boolean checkRecordSLT;

    public RSInwardOutwardSaveDTO() {
    }

    public RSInwardOutwardSaveDTO(RSInwardOutward rsInwardOutward, List<RefVoucher> viewVouchers, Integer currentBook, Boolean checkRecordSLT) {
        this.rsInwardOutward = rsInwardOutward;
        this.viewVouchers = viewVouchers;
        this.currentBook = currentBook;
        this.checkRecordSLT = checkRecordSLT;
    }

    public RSInwardOutward getRsInwardOutward() {
        return rsInwardOutward;
    }

    public void setRsInwardOutward(RSInwardOutward rsInwardOutward) {
        this.rsInwardOutward = rsInwardOutward;
    }

    public List<RefVoucher> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucher> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public Integer getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(Integer currentBook) {
        this.currentBook = currentBook;
    }

    public Boolean getCheckRecordSLT() {
        return checkRecordSLT;
    }

    public void setCheckRecordSLT(Boolean checkRecordSLT) {
        this.checkRecordSLT = checkRecordSLT;
    }
}
