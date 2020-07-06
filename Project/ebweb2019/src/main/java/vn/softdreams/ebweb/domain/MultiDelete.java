package vn.softdreams.ebweb.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Transient;
import java.util.List;
import java.util.UUID;

public class MultiDelete {
    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<UUID> listID;

    @Transient
    @JsonDeserialize
    @JsonSerialize
    private List<Integer> listTypeID;

    public List<UUID> getListID() {
        return listID;
    }

    public void setListID(List<UUID> listID) {
        this.listID = listID;
    }

    public List<Integer> getListTypeID() {
        return listTypeID;
    }

    public void setListTypeID(List<Integer> listTypeID) {
        this.listTypeID = listTypeID;
    }

    public MultiDelete() {
    }

    public MultiDelete(List<UUID> listID, List<Integer> listTypeID) {
        this.listID = listID;
        this.listTypeID = listTypeID;
    }
}
