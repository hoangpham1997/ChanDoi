package vn.softdreams.ebweb.domain;

import java.util.UUID;

/**
 * add by tiepvv
 */
public class SearchVoucherAccountingObjects {

    private String scaleType;
    private String objectType;
    private UUID accountingObjectGroup;
    private String keySearch;


    public String getScaleType() {
        return scaleType;
    }

    public void setScaleType(String scaleType) {
        this.scaleType = scaleType;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public UUID getAccountingObjectGroup() {
        return accountingObjectGroup;
    }

    public void setAccountingObjectGroup(UUID accountingObjectGroup) {
        this.accountingObjectGroup = accountingObjectGroup;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }
}
