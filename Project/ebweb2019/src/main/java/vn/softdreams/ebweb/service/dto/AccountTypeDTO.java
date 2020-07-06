package vn.softdreams.ebweb.service.dto;

import java.util.List;

public class AccountTypeDTO {

    private Integer typeID;

    private List<ColumnDTO> columnName;

    public AccountTypeDTO() {
    }

    public AccountTypeDTO(Integer typeID, List<ColumnDTO> columnName) {
        this.typeID = typeID;
        this.columnName = columnName;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public List<ColumnDTO> getColumnName() {
        return columnName;
    }

    public void setColumnName(List<ColumnDTO> columnName) {
        this.columnName = columnName;
    }
}
