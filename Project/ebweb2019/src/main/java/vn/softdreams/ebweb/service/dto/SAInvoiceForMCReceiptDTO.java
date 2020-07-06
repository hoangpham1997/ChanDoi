package vn.softdreams.ebweb.service.dto;

import java.time.LocalDate;
import java.util.UUID;

public class SAInvoiceForMCReceiptDTO {
    private UUID id;

    private String noFBook;

    private String noMBook;

    private String paymentClauseCode;

    private LocalDate date;

    private LocalDate dueDate;

    public SAInvoiceForMCReceiptDTO(UUID id,
                                    String noFBook,
                                    String noMBook,
                                    String paymentClauseCode,
                                    LocalDate date,
                                    LocalDate dueDate
    ) {
        this.id = id;
        this.noFBook = noFBook;
        this.noMBook = noMBook;
        this.paymentClauseCode = paymentClauseCode;
        this.date = date;
        this.dueDate = dueDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNoFBook() {
        return noFBook;
    }

    public void setNoFBook(String noFBook) {
        this.noFBook = noFBook;
    }

    public String getNoMBook() {
        return noMBook;
    }

    public void setNoMBook(String noMBook) {
        this.noMBook = noMBook;
    }

    public String getPaymentClauseCode() {
        return paymentClauseCode;
    }

    public void setPaymentClauseCode(String paymentClauseCode) {
        this.paymentClauseCode = paymentClauseCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
