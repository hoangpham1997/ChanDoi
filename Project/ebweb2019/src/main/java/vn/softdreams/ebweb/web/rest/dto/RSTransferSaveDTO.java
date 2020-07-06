package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.RSTransfer;
import vn.softdreams.ebweb.domain.RefVoucher;

import java.util.List;

public class RSTransferSaveDTO {
    private RSTransfer rsTransfer;
    private List<RefVoucher> viewVouchers;
    private RSTransferDTO rsTransferDTO;
    private Integer currentBook;
    private Boolean checkRecordSLT;

    public RSTransferSaveDTO() {
    }

    public RSTransferSaveDTO(RSTransfer rsTransfer, List<RefVoucher> viewVouchers, RSTransferDTO rsTransferDTO, Integer currentBook, Boolean checkRecordSLT) {
        this.rsTransfer = rsTransfer;
        this.viewVouchers = viewVouchers;
        this.rsTransferDTO = rsTransferDTO;
        this.currentBook = currentBook;
        this.checkRecordSLT = checkRecordSLT;
    }

    public RSTransfer getRsTransfer() {
        return rsTransfer;
    }

    public void setRsTransfer(RSTransfer rsTransfer) {
        this.rsTransfer = rsTransfer;
    }

    public List<RefVoucher> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucher> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public RSTransferDTO getRsTransferDTO() {
        return rsTransferDTO;
    }

    public void setRsTransferDTO(RSTransferDTO rsTransferDTO) {
        this.rsTransferDTO = rsTransferDTO;
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
