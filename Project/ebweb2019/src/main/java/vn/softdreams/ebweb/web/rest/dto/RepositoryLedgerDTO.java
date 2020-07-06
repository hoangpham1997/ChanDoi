package vn.softdreams.ebweb.web.rest.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RepositoryLedgerDTO {

    private UUID referenceID;
    private String noFBook;
    private String noMBook;
    private LocalDate postedDate;
    private LocalDate date;
    private Integer typeID;
    private String reason;
    private UUID costSetID;
    private UUID materialGoodsID;
    private BigDecimal quantity;

    public RepositoryLedgerDTO(UUID referenceID, String noFBook, String noMBook, LocalDate postedDate, LocalDate date, Integer typeID, String reason) {
        this.referenceID = referenceID;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.postedDate = postedDate;
        this.date = date;
        this.typeID = typeID;
        this.reason = reason;
    }

    public RepositoryLedgerDTO(UUID costSetID, UUID materialGoodsID, BigDecimal quantity) {
        this.costSetID = costSetID;
        this.materialGoodsID = materialGoodsID;
        this.quantity = quantity;
    }

    public UUID getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(UUID referenceID) {
        this.referenceID = referenceID;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getCostSetID() {
        return costSetID;
    }

    public void setCostSetID(UUID costSetID) {
        this.costSetID = costSetID;
    }

    public UUID getMaterialGoodsID() {
        return materialGoodsID;
    }

    public void setMaterialGoodsID(UUID materialGoodsID) {
        this.materialGoodsID = materialGoodsID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
