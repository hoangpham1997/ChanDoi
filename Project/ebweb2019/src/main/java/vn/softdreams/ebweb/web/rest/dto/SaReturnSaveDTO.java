package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.RefVoucher;
import vn.softdreams.ebweb.domain.SaBill;
import vn.softdreams.ebweb.domain.SaReturn;

import java.time.LocalDateTime;
import java.util.List;

public class SaReturnSaveDTO {
    private SaReturn saReturn;
    private List<RefVoucher> viewVouchers;
    private String noMBook;
    private String noFBook;
    private String reason;
    private String recordMsg;
    private LocalDateTime refDateTime;

    public SaReturnSaveDTO(SaReturn saReturn, List<RefVoucher> viewVouchers) {
        this.saReturn = saReturn;
        this.viewVouchers = viewVouchers;
    }

    public SaReturnSaveDTO() {
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

    public List<RefVoucher> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucher> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRecordMsg() {
        return recordMsg;
    }

    public void setRecordMsg(String recordMsg) {
        this.recordMsg = recordMsg;
    }
}
