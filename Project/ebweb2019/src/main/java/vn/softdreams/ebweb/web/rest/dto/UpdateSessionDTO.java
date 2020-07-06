package vn.softdreams.ebweb.web.rest.dto;

import java.util.UUID;

public class UpdateSessionDTO {
    private String currentBook;
    private UUID org;

    public UpdateSessionDTO() {
    }

    public UpdateSessionDTO(String currentBook, UUID org) {
        this.currentBook = currentBook;
        this.org = org;
    }

    public String getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(String currentBook) {
        this.currentBook = currentBook;
    }

    public UUID getOrg() {
        return org;
    }

    public void setOrg(UUID org) {
        this.org = org;
    }
}
