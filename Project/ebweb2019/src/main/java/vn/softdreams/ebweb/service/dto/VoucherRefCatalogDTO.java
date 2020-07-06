package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class VoucherRefCatalogDTO {
    private UUID id;
    private Integer typeID;
    private Integer typeGroupID;
    private String typeName;
    private LocalDate date;
    private String noFBook;
    private String noMBook;
    private String reason;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountOriginal;

    public VoucherRefCatalogDTO(UUID id,
                                Integer typeID,
                                Integer typeGroupID,
                                String typeName,
                                LocalDate date,
                                String noFBook,
                                String noMBook,
                                String reason,
                                BigDecimal totalAmount,
                                BigDecimal totalAmountOriginal
    ) {
        this.id = id;
        this.typeID = typeID;
        this.typeGroupID = typeGroupID;
        this.typeName = typeName;
        this.date = date;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.reason = reason;
        this.totalAmount = totalAmount;
        this.totalAmountOriginal = totalAmountOriginal;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Integer getTypeGroupID() {
        return typeGroupID;
    }

    public void setTypeGroupID(Integer typeGroupID) {
        this.typeGroupID = typeGroupID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountOriginal() {
        return totalAmountOriginal;
    }

    public void setTotalAmountOriginal(BigDecimal totalAmountOriginal) {
        this.totalAmountOriginal = totalAmountOriginal;
    }
}
