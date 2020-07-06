package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

public class OrgIdAndBookDTO {
    private UUID companyID;
    private Integer workingOnBook;

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public Integer getWorkingOnBook() {
        return workingOnBook;
    }

    public void setWorkingOnBook(Integer workingOnBook) {
        this.workingOnBook = workingOnBook;
    }
}
