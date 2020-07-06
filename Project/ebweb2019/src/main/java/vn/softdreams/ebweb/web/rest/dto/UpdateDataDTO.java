package vn.softdreams.ebweb.web.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.*;

public class UpdateDataDTO {
    private UUID uuid;
    private String type;
    private String messages;
    private Object result;
    private String noFBook;
    private String noMBook;
    private Integer totalRows;
    private Integer rowNum;
    private Integer typeLedger;
    private Integer importType;
    private Boolean isUpdateReceiptNo;
    private Integer quantity;
    private Boolean isError;
    private byte[] excelFile;
    private String message;
    private String sheetName;
    private String messageRecord;
    private List<String>  errors = new ArrayList<>();
    Map<String, Integer> headers = new HashMap<>();
    Map<String, String> codeWithExcelField = new HashMap<>();

    public Integer getImportType() {
        return importType;
    }

    public void setImportType(Integer importType) {
        this.importType = importType;
    }

    public Map<String, String> getCodeWithExcelField() {
        return codeWithExcelField;
    }

    public void setCodeWithExcelField(Map<String, String> codeWithExcelField) {
        this.codeWithExcelField = codeWithExcelField;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Map<String, Integer> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Integer> headers) {
        this.headers = headers;
    }

    private List<ExcelSystemFieldDTO> excelSystemFieldDTOS;

    private Map<String, List<String>> excelFields;

    public Map<String, List<String>> getExcelFields() {
        return excelFields;
    }

    public void setExcelFields(Map<String, List<String>> excelFields) {
        this.excelFields = excelFields;
    }

    public List<ExcelSystemFieldDTO> getExcelSystemFieldDTOS() {
        return excelSystemFieldDTOS;
    }

    public void setExcelSystemFieldDTOS(List<ExcelSystemFieldDTO> excelSystemFieldDTOS) {
        this.excelSystemFieldDTOS = excelSystemFieldDTOS;
    }

    public UpdateDataDTO withMessage(String message) {
        this.message = message;
        return this;
    }

    public List<String> getErrors() {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        return errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public UpdateDataDTO() {
    }

    public UpdateDataDTO(UUID uuid) {
        this.uuid = uuid;
    }

    public UpdateDataDTO(String messages) {
        this.messages = messages;
    }

    public UpdateDataDTO(UUID uuid, String type, String messages) {
        this.uuid = uuid;
        this.type = type;
        this.messages = messages;
    }

    public UpdateDataDTO(String noFBook, String noMBook, Integer typeLedger) {
        if (noFBook != null) {
            this.noFBook = noFBook;
        }
        if (noMBook != null) {
            this.noMBook = noMBook;
        }
        if (typeLedger != null) {
            this.typeLedger = typeLedger;
        }

    }

    public UpdateDataDTO(UUID uuid, Integer rowNum, Integer totalRows) {
        this.uuid = uuid;
        this.totalRows = totalRows;
        this.rowNum = rowNum;
    }

    public Boolean getError() {
        return isError == null ? false : isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }

    public byte[] getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(byte[] excelFile) {
        this.excelFile = excelFile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getUpdateReceiptNo() {
        return isUpdateReceiptNo;
    }

    public void setUpdateReceiptNo(Boolean updateReceiptNo) {
        isUpdateReceiptNo = updateReceiptNo;
    }

    public Integer getTypeLedger() {
        return typeLedger;
    }

    public void setTypeLedger(Integer typeLedger) {
        this.typeLedger = typeLedger;
    }

    public Integer getRowNum() {
        return rowNum != null ? rowNum : 0;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getMessageRecord() {
        return messageRecord;
    }

    public void setMessageRecord(String messageRecord) {
        this.messageRecord = messageRecord;
    }
}
