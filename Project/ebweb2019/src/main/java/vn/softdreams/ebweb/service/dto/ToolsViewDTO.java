package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

public class ToolsViewDTO {
    private UUID id;
    private String toolName;
    private String toolCode;

    public ToolsViewDTO() {
    }

    public ToolsViewDTO(UUID id, String toolName, String toolCode) {
        this.id = id;
        this.toolName = toolName;
        this.toolCode = toolCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }
}
