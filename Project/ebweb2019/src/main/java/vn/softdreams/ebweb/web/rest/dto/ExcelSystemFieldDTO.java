package vn.softdreams.ebweb.web.rest.dto;

public class ExcelSystemFieldDTO {
    private boolean required;
    private String systemField;
    private String excelField;
    private String description;
    private String type;
    private String code;
    private Integer index;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public ExcelSystemFieldDTO() {
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getSystemField() {
        return systemField;
    }

    public void setSystemField(String systemField) {
        this.systemField = systemField;
    }

    public String getExcelField() {
        return excelField;
    }

    public void setExcelField(String excelField) {
        this.excelField = excelField;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
