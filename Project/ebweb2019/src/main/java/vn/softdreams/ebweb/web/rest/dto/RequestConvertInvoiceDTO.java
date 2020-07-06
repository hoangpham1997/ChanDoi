package vn.softdreams.ebweb.web.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class RequestConvertInvoiceDTO {
    private List<UUID> uuidList;
    private String nameUser;

    public List<UUID> getUuidList() {
        return uuidList;
    }

    public void setUuidList(List<UUID> uuidList) {
        this.uuidList = uuidList;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }
}
