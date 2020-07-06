package vn.softdreams.ebweb.service.dto.cashandbank;

import org.springframework.cglib.core.Local;
import vn.softdreams.ebweb.domain.MBDeposit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class MBDepositExportDTO {
    private UUID id;
    private LocalDate date;
    private LocalDate postedDate;
    private String noBook;
    private Integer typeID;
    private String typeIDInWord;
    private String accountingObjectName;
    private String description;
    private BigDecimal totalAmount;

    public MBDepositExportDTO() {
    }

    public MBDepositExportDTO(UUID id, LocalDate date, LocalDate postedDate, String noBook, Integer typeID,
                              String typeIDInWord, String accountingObjectName, String description, BigDecimal totalAmount) {
        this.id = id;
        this.date = date;
        this.postedDate = postedDate;
        this.noBook = noBook;
        this.typeID = typeID;
        this.typeIDInWord = typeIDInWord;
        this.accountingObjectName = accountingObjectName;
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

    public String getAccountingObjectName() {
        return accountingObjectName;
    }

    public void setAccountingObjectName(String accountingObjectName) {
        this.accountingObjectName = accountingObjectName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
