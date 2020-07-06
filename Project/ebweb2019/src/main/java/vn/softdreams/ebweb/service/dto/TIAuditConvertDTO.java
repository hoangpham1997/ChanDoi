package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.TIAllocationAllocated;
import vn.softdreams.ebweb.domain.TIAllocationPost;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TIAuditConvertDTO {
    private UUID id;
    private Integer TypeID;
    private Integer TypeLedger;
    private String NoFBook;
    private String NoMBook;
    private String noBook;
    private LocalDate date;
    private String Description;
    private LocalDate InventoryDate;
    private String Summary;
    private UUID TemplateID;

    public TIAuditConvertDTO() {
    }

    public TIAuditConvertDTO(UUID id, Integer typeID, Integer typeLedger, String noFBook, String noMBook, String noBook, LocalDate date, String description, LocalDate inventoryDate, String summary, UUID templateID) {
        this.id = id;
        TypeID = typeID;
        TypeLedger = typeLedger;
        NoFBook = noFBook;
        NoMBook = noMBook;
        this.noBook = noBook;
        this.date = date;
        Description = description;
        InventoryDate = inventoryDate;
        Summary = summary;
        TemplateID = templateID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTypeID() {
        return TypeID;
    }

    public void setTypeID(Integer typeID) {
        TypeID = typeID;
    }

    public Integer getTypeLedger() {
        return TypeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        TypeLedger = typeLedger;
    }

    public String getNoFBook() {
        return NoFBook;
    }

    public void setNoFBook(String noFBook) {
        NoFBook = noFBook;
    }

    public String getNoMBook() {
        return NoMBook;
    }

    public void setNoMBook(String noMBook) {
        NoMBook = noMBook;
    }

    public String getNoBook() {
        return noBook;
    }

    public void setNoBook(String noBook) {
        this.noBook = noBook;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public LocalDate getInventoryDate() {
        return InventoryDate;
    }

    public void setInventoryDate(LocalDate inventoryDate) {
        InventoryDate = inventoryDate;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public UUID getTemplateID() {
        return TemplateID;
    }

    public void setTemplateID(UUID templateID) {
        TemplateID = templateID;
    }
}
