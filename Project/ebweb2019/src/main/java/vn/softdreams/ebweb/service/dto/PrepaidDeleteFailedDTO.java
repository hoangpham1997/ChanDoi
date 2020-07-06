package vn.softdreams.ebweb.service.dto;

public class PrepaidDeleteFailedDTO {
    private String prePaidCode;

    private String prePaidName;

    public PrepaidDeleteFailedDTO() {
    }

    public PrepaidDeleteFailedDTO(String prePaidCode, String prePaidName) {
        this.prePaidCode = prePaidCode;
        this.prePaidName = prePaidName;
    }

    public String getPrePaidCode() {
        return prePaidCode;
    }

    public void setPrePaidCode(String prePaidCode) {
        this.prePaidCode = prePaidCode;
    }

    public String getPrePaidName() {
        return prePaidName;
    }

    public void setPrePaidName(String prePaidName) {
        this.prePaidName = prePaidName;
    }

}
