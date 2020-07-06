package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.AccountingObject;
import vn.softdreams.ebweb.domain.Currency;
import vn.softdreams.ebweb.service.dto.AccountingObjectDTO;

import java.util.List;

public class SaReturnSearchDTO {
    private List<AccountingObjectDTO> accountingObjects;
    private List<CurrencyCbbDTO> currencies;

    public SaReturnSearchDTO(List<AccountingObjectDTO> accountingObjects, List<CurrencyCbbDTO> currencies) {
        this.accountingObjects = accountingObjects;
        this.currencies = currencies;
    }

    public SaReturnSearchDTO() {
    }

    public List<AccountingObjectDTO> getAccountingObjects() {
        return accountingObjects;
    }

    public void setAccountingObjects(List<AccountingObjectDTO> accountingObjects) {
        this.accountingObjects = accountingObjects;
    }

    public List<CurrencyCbbDTO> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<CurrencyCbbDTO> currencies) {
        this.currencies = currencies;
    }
}
