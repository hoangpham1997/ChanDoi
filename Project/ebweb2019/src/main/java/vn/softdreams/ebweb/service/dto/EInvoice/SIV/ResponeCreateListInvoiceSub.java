package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResponeCreateListInvoiceSub {
    private ResponeCreateListInvoice[] participantList;

    public ResponeCreateListInvoice[] getParticipantList() {
        return participantList;
    }

    public void setParticipantList(ResponeCreateListInvoice[] participantList) {
        this.participantList = participantList;
    }
}
