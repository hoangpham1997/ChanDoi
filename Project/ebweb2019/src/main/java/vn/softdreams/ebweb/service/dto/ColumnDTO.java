package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

public class ColumnDTO {

    private String column;

    private Boolean ppType;

    public ColumnDTO() {
    }

    public ColumnDTO(String column, Boolean ppType) {
        this.column = column;
        this.ppType = ppType;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Boolean getPpType() {
        return ppType;
    }

    public void setPpType(Boolean ppType) {
        this.ppType = ppType;
    }
}
