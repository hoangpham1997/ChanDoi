package vn.softdreams.ebweb.service.dto.EInvoice.SDS;

import java.util.UUID;

public class DigestData {

    private String key;
    private String hashData;
    private String sigData;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData(String hashData) {
        this.hashData = hashData;
    }

    public String getSigData() {
        return sigData;
    }

    public void setSigData(String sigData) {
        this.sigData = sigData;
    }
}
