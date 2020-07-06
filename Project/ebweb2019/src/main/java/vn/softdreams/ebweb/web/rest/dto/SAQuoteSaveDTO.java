package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.SAQuote;

public class SAQuoteSaveDTO {
    private SAQuote sAQuote;
    private int status;

    public SAQuoteSaveDTO() {
    }

    public SAQuoteSaveDTO(SAQuote sAQuote, int status) {
        this.sAQuote = sAQuote;
        this.status = status;
    }

    public SAQuote getsAQuote() {
        return sAQuote;
    }

    public void setsAQuote(SAQuote sAQuote) {
        this.sAQuote = sAQuote;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
