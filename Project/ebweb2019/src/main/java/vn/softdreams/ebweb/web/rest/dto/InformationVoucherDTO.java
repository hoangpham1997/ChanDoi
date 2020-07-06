package vn.softdreams.ebweb.web.rest.dto;

import java.util.UUID;

public class InformationVoucherDTO {
    private UUID id;
    private Integer typeID;

    public InformationVoucherDTO() {
    }

    public InformationVoucherDTO(UUID id, Integer typeID) {
        this.id = id;
        this.typeID = typeID;
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
}
