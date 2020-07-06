package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.EbGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class OrgTreeTableDTO {
    private UUID value;
    private String text;
    private String workingOnBook;
    private int unitType;
    private boolean check;
    private Set<EbGroup> groups = new HashSet<>();
    private String data;
    private List<OrgTreeTableDTO> children;

    public OrgTreeTableDTO(UUID value, String text, int unitType, List<OrgTreeTableDTO> children
    ) {
        this.value = value;
        this.text = text;
        this.unitType = unitType;
        this.children = children;
    }

    public OrgTreeTableDTO(UUID value, String text, int unitType, String workingOnBook,
                           List<OrgTreeTableDTO> children
    ) {
        this.value = value;
        this.text = text;
        this.unitType = unitType;
        this.children = children;
        this.workingOnBook = workingOnBook;
    }

    public OrgTreeTableDTO(UUID value, String text, int unitType,  Set<EbGroup> groups, List<OrgTreeTableDTO> children, boolean check
    ) {
        this.value = value;
        this.text = text;
        this.unitType = unitType;
        this.children = children;
        this.groups = groups;
        this.check = check;
    }

    public OrgTreeTableDTO(UUID value, String text, int unitType, String workingOnBook,  Set<EbGroup> groups, String data, List<OrgTreeTableDTO> children, boolean check
    ) {
        this.value = value;
        this.text = text;
        this.unitType = unitType;
        this.workingOnBook = workingOnBook;
        this.children = children;
        this.groups = groups;
        this.check = check;
        this.data = data;
    }

    public OrgTreeTableDTO() {
    }

    public UUID getValue() {
        return value;
    }

    public void setValue(UUID value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<OrgTreeTableDTO> getChildren() {
        return children;
    }

    public void setChildren(List<OrgTreeTableDTO> children) {
        this.children = children;
    }

    public int getUnitType() {
        return unitType;
    }

    public void setUnitType(int unitType) {
        this.unitType = unitType;
    }

    public String getWorkingOnBook() {
        return workingOnBook;
    }

    public void setWorkingOnBook(String workingOnBook) {
        this.workingOnBook = workingOnBook;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Set<EbGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<EbGroup> groups) {
        this.groups = groups;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
