package vn.softdreams.ebweb.service.dto;
import java.math.BigDecimal;
import java.util.UUID;

public class MaterialQuantumDetailsDTO {
    private UUID id;
    private String description;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;
    private Integer orderPriority;
    private UUID materialGoodsID;
    private UUID materialQuantumID;
    private UUID unitID;
    private UUID repositoryID;

    public MaterialQuantumDetailsDTO() {
    }

    public MaterialQuantumDetailsDTO(UUID id, String description, BigDecimal quantity, BigDecimal unitPrice, BigDecimal amount, Integer orderPriority, UUID materialGoodsID, UUID materialQuantumID, UUID unitID, UUID repositoryID) {
        this.id = id;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.orderPriority = orderPriority;
        this.materialGoodsID = materialGoodsID;
        this.materialQuantumID = materialQuantumID;
        this.unitID = unitID;
        this.repositoryID = repositoryID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public UUID getMaterialQuantumID() {
        return materialQuantumID;
    }

    public void setMaterialQuantumID(UUID materialQuantumID) {
        this.materialQuantumID = materialQuantumID;
    }

    public UUID getUnitID() {
        return unitID;
    }

    public void setUnitID(UUID unitID) {
        this.unitID = unitID;
    }

    public UUID getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(UUID repositoryID) {
        this.repositoryID = repositoryID;
    }
}
