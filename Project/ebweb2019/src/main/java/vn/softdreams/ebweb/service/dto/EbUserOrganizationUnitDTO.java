package vn.softdreams.ebweb.service.dto;

import java.util.UUID;

/**
 * A DTO representing a user, with his authorities.
 */
public class EbUserOrganizationUnitDTO {

    private Long id;

    private Long userID;

    private UUID orgID;

    private String currentBook;

    public EbUserOrganizationUnitDTO() {
        // Empty constructor needed for Jackson.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public UUID getOrgID() {
        return orgID;
    }

    public void setOrgID(UUID orgID) {
        this.orgID = orgID;
    }

    public String getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(String currentBook) {
        this.currentBook = currentBook;
    }
}
