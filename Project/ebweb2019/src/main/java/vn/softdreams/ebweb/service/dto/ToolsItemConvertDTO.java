package vn.softdreams.ebweb.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ToolsItemConvertDTO {
    private UUID toolsID;
    private String toolsName;
    private String toolsCode;

    public ToolsItemConvertDTO() {
    }

    public ToolsItemConvertDTO(UUID toolsID, String toolsName, String toolsCode) {
        this.toolsID = toolsID;
        this.toolsName = toolsName;
        this.toolsCode = toolsCode;
    }

    public UUID getToolsID() {
        return toolsID;
    }

    public void setToolsID(UUID toolsID) {
        this.toolsID = toolsID;
    }

    public String getToolsName() {
        return toolsName;
    }

    public void setToolsName(String toolsName) {
        this.toolsName = toolsName;
    }

    public String getToolsCode() {
        return toolsCode;
    }

    public void setToolsCode(String toolsCode) {
        this.toolsCode = toolsCode;
    }
}
