package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.SAQuote;

import java.util.List;

public class SAQuoteViewDTO {
    private SAQuote sAQuote;
    private List<RefVoucherDTO> viewVouchers;

    public SAQuoteViewDTO() {
    }

    public SAQuoteViewDTO(SAQuote sAQuote, List<RefVoucherDTO> viewVouchers) {
        this.sAQuote = sAQuote;
        this.viewVouchers = viewVouchers;
    }

    public SAQuote getsAQuote() {
        return sAQuote;
    }

    public void setsAQuote(SAQuote sAQuote) {
        this.sAQuote = sAQuote;
    }

    public List<RefVoucherDTO> getViewVouchers() {
        return viewVouchers;
    }

    public void setViewVouchers(List<RefVoucherDTO> viewVouchers) {
        this.viewVouchers = viewVouchers;
    }
}
