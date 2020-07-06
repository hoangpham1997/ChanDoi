package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.CreditCard;

public class CreditCardSaveDTO {
    private CreditCard creditCard;

    private int status;

    public CreditCardSaveDTO() {
    }

    public CreditCardSaveDTO(CreditCard creditCard, int status) {
        this.creditCard = creditCard;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void getCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

}
