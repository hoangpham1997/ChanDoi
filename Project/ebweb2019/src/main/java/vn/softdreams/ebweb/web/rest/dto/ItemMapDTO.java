package vn.softdreams.ebweb.web.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemMapDTO {
    private String name;
    private BigDecimal quantity;

    public ItemMapDTO(String name, BigDecimal quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public ItemMapDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
