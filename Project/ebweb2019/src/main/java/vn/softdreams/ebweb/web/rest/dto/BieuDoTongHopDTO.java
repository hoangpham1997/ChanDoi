package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;

public class BieuDoTongHopDTO {
    private String itemName;
    private BigDecimal amount;
    private BigDecimal prevAmount;

    public BieuDoTongHopDTO(String itemName, BigDecimal amount, BigDecimal prevAmount) {
        this.itemName = itemName;
        this.amount = amount;
        this.prevAmount = prevAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrevAmount() {
        return prevAmount;
    }

    public void setPrevAmount(BigDecimal prevAmount) {
        this.prevAmount = prevAmount;
    }
}
