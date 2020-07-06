package vn.softdreams.ebweb.web.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class RequestPublishInvoiceDTO {
    private List<UUID> uuidList;
    private String nameDocument;
    private LocalDate dateDocument;

    public List<UUID> getUuidList() {
        return uuidList;
    }

    public void setUuidList(List<UUID> uuidList) {
        this.uuidList = uuidList;
    }

    public String getNameDocument() {
        return nameDocument;
    }

    public void setNameDocument(String nameDocument) {
        this.nameDocument = nameDocument;
    }

    public LocalDate getDateDocument() {
        return dateDocument;
    }

    public void setDateDocument(LocalDate dateDocument) {
        this.dateDocument = dateDocument;
    }
}
