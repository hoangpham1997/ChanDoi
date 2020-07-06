package vn.softdreams.ebweb.service.dto.Report;

import java.math.BigDecimal;
import java.util.UUID;

public class TITransferDTO {

    private  String toolCode;

    private String toolName;

    private String  toDepartmentID;

    private String fromDepartmentID;

    private BigDecimal quantity;

    private BigDecimal transferQuantity;

    public TITransferDTO() {
    }

    public TITransferDTO(String toolCode, String toolName, String toDepartmentID, String fromDepartmentID, BigDecimal quantity, BigDecimal transferQuantity) {
        this.toolCode = toolCode;
        this.toolName = toolName;
        this.toDepartmentID = toDepartmentID;
        this.fromDepartmentID = fromDepartmentID;
        this.quantity = quantity;
        this.transferQuantity = transferQuantity;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getToDepartmentID() {
        return toDepartmentID;
    }

    public void setToDepartmentID(String toDepartmentID) {
        this.toDepartmentID = toDepartmentID;
    }

    public String getFromDepartmentID() {
        return fromDepartmentID;
    }

    public void setFromDepartmentID(String fromDepartmentID) {
        this.fromDepartmentID = fromDepartmentID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTransferQuantity() {
        return transferQuantity;
    }

    public void setTransferQuantity(BigDecimal transferQuantity) {
        this.transferQuantity = transferQuantity;
    }
}
