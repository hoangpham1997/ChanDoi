package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.RepositoryLedger;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CalculateOWPriceDTO {

    private UUID materialGoodsID;

    private BigDecimal amount;

    private BigDecimal quantity;

    private UUID repositoryID;

    private List<RepositoryLedger> repositoryLedgers;

    public CalculateOWPriceDTO(UUID materialGoodsID, BigDecimal amount, BigDecimal quantity, UUID repositoryID) {
        this.materialGoodsID = materialGoodsID;
        this.amount = amount;
        this.quantity = quantity;
        this.repositoryID = repositoryID;
    }

    public List<RepositoryLedger> getRepositoryLedgers() {
        return repositoryLedgers;
    }

    public void setRepositoryLedgers(List<RepositoryLedger> repositoryLedgers) {
        this.repositoryLedgers = repositoryLedgers;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
