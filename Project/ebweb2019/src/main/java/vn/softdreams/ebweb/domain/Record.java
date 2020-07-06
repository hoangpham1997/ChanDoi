package vn.softdreams.ebweb.domain;

import java.util.UUID;

public class Record {
    private UUID id;
    private Integer typeID;
    private boolean isSuccess;
    private String msg;
    private UUID repositoryLedgerID;
    private Boolean answer;

    public UUID getRepositoryLedgerID() {
        return repositoryLedgerID;
    }

    public void setRepositoryLedgerID(UUID repositoryLedgerID) {
        this.repositoryLedgerID = repositoryLedgerID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }
}
