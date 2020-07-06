package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.domain.GenCode;
import vn.softdreams.ebweb.domain.SystemOption;

import java.util.List;

public class SaveSystemOptionsDTO {
    private List<SystemOption> systemOptions;
    private List<GenCode> genCodes;

    public SaveSystemOptionsDTO(List<SystemOption> systemOptions, List<GenCode> genCodes) {
        this.systemOptions = systemOptions;
        this.genCodes = genCodes;
    }

    public SaveSystemOptionsDTO() {
    }

    public List<SystemOption> getSystemOptions() {
        return systemOptions;
    }

    public void setSystemOptions(List<SystemOption> systemOptions) {
        this.systemOptions = systemOptions;
    }

    public List<GenCode> getGenCodes() {
        return genCodes;
    }

    public void setGenCodes(List<GenCode> genCodes) {
        this.genCodes = genCodes;
    }
}
