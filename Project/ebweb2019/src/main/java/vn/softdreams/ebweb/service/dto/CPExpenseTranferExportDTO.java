package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CPExpenseTranferExportDTO {
    private UUID id;
    private LocalDate date;
    private LocalDate postedDate;
    private String noBook;
    private Integer typeID;
    private String typeIDInWord;
    private String description;
    private BigDecimal totalAmount;

    public CPExpenseTranferExportDTO() {
    }

    public CPExpenseTranferExportDTO(UUID id , LocalDate date, LocalDate postedDate, String noBook, Integer typeID,
                                     String typeIDInWord, String description, BigDecimal totalAmount) {
        this.id = id;
        this.date = date;
        this.postedDate = postedDate;
        this.noBook = noBook;
        this.typeID = typeID;
        this.typeIDInWord = typeIDInWord;
        this.description = description;
        this.totalAmount = totalAmount;
    }

    public String getTypeIDInWord() {
        return typeIDInWord;
    }

    public void setTypeIDInWord(String typeIDInWord) {
        this.typeIDInWord = typeIDInWord;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoBook() {
        return noBook;
    }

    public void setNoBook(String noBook) {
        this.noBook = noBook;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
