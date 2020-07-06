package vn.softdreams.ebweb.service.dto;

import vn.softdreams.ebweb.web.rest.dto.OPAccountDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageDTO {
    private String msgError;

    public MessageDTO(String msgError) {
        this.msgError = msgError;
    }

    public MessageDTO() {

    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }
}
