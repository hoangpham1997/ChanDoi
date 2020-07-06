package vn.softdreams.ebweb.web.rest.dto;

import java.time.LocalDate;

public class UpdateDateClosedBook {
    private LocalDate dateClosedBook;
    private LocalDate dateClosedBookOld;

    public LocalDate getDateClosedBook() {
        return dateClosedBook;
    }

    public void setDateClosedBook(LocalDate dateClosedBook) {
        this.dateClosedBook = dateClosedBook;
    }

    public LocalDate getDateClosedBookOld() {
        return dateClosedBookOld;
    }

    public void setDateClosedBookOld(LocalDate dateClosedBookOld) {
        this.dateClosedBookOld = dateClosedBookOld;
    }
}
