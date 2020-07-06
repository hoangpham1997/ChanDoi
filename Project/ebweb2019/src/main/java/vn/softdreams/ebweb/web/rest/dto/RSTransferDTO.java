package vn.softdreams.ebweb.web.rest.dto;

public class RSTransferDTO {
    private String noMBook;
    private String noFBook;
    private String reason;

    public RSTransferDTO(String noMBook, String noFBook, String reason) {
        this.noMBook = noMBook;
        this.noFBook = noFBook;
        this.reason = reason;
    }

    public RSTransferDTO() {
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
