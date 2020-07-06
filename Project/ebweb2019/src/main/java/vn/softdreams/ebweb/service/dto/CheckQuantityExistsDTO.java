package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.OrganizationUnit;

import java.util.UUID;

public class CheckQuantityExistsDTO {
    private String quantityExists;
    private String minimumStock;

    public CheckQuantityExistsDTO() {
    }

    public CheckQuantityExistsDTO(String quantityExists, String minimumStock) {
        this.quantityExists = quantityExists;
        this.minimumStock = minimumStock;
    }

    public String getQuantityExists() {
        return quantityExists;
    }

    public void setQuantityExists(String quantityExists) {
        this.quantityExists = quantityExists;
    }

    public String getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(String minimumStock) {
        this.minimumStock = minimumStock;
    }
}
