package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.config.Constants;
import vn.softdreams.ebweb.domain.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class EbUserGroupDTO {

    private Long id;

    private UUID groupID;

    private UUID companyID;

    private UUID branchID;

    private Long userID;

    private Integer workingOnBook;

    public EbUserGroupDTO() {
        // Empty constructor needed for Jackson.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public void setCompanyID(UUID companyID) {
        this.companyID = companyID;
    }

    public UUID getBranchID() {
        return branchID;
    }

    public void setBranchID(UUID branchID) {
        this.branchID = branchID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Integer getWorkingOnBook() {
        return workingOnBook;
    }

    public void setWorkingOnBook(Integer workingOnBook) {
        this.workingOnBook = workingOnBook;
    }
}
