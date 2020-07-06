package vn.softdreams.ebweb.domain;

import java.util.UUID;

/**
 * add by tiepvv
 */
public class SearchVoucherEmployee {

    private String contactSex;
    private String objectType;
    private UUID organizationUnitName;
    private String keySearch;

    public String getContactSex() {
        return contactSex;
    }

    public void setContactSex(String contactSex) {
        this.contactSex = contactSex;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public UUID getOrganizationUnitName() {
        return organizationUnitName;
    }

    public void setOrganizationUnitName(UUID organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }
}
