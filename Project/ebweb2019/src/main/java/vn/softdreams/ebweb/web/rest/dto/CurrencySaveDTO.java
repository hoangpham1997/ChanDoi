package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.Currency;
import vn.softdreams.ebweb.domain.MBTellerPaper;

public class CurrencySaveDTO {
    private Currency currency;

    private int status;

    public CurrencySaveDTO() {
    }

    public CurrencySaveDTO(Currency currency, int status) {
        this.currency = currency;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
