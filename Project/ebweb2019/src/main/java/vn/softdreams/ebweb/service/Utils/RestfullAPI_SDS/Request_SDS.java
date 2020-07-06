package vn.softdreams.ebweb.service.Utils.RestfullAPI_SDS;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.DigestData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Request_SDS {
    private String XmlData;
    private String Pattern;
    private String Serial;
    private UUID Ikey;
    private List<UUID> Ikeys;
    private Map<UUID, String> IkeyDate;
    private Map<UUID, String> IkeyEmail;
    private Map<String, String> Signature;
    private List<DigestData> signatureDTO;
    private Integer Option;
    private String CertString;
    private String fromDate;
    private String toDate;

    public Request_SDS() {
        IkeyDate = new HashMap<>();
    }

    public String getXmlData() {
        return XmlData;
    }

    @JsonProperty("XmlData")
    public void setXmlData(String xmlData) {
        XmlData = xmlData;
    }

    @JsonProperty("Pattern")
    public String getPattern() {
        return Pattern;
    }

    public void setPattern(String pattern) {
        Pattern = pattern;
    }

    @JsonProperty("Serial")
    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }

    @JsonProperty("Ikey")
    public UUID getIkey() {
        return Ikey;
    }

    public void setIkey(UUID ikey) {
        Ikey = ikey;
    }

    @JsonProperty("Ikeys")
    public List<UUID> getIkeys() {
        return Ikeys;
    }

    public void setIkeys(List<UUID> ikeys) {
        Ikeys = ikeys;
    }

    @JsonProperty("Option")
    public Integer getOption() {
        return Option;
    }

    public void setOption(Integer option) {
        Option = option;
    }

    @JsonProperty("IkeyDate")
    public Map<UUID, String> getIkeyDate() {
        return IkeyDate;
    }

    public void setIkeyDate(Map<UUID, String> ikeyDate) {
        IkeyDate = ikeyDate;
    }

    @JsonProperty("IkeyEmail")
    public Map<UUID, String> getIkeyEmail() {
        return IkeyEmail;
    }

    public void setIkeyEmail(Map<UUID, String> ikeyEmail) {
        IkeyEmail = ikeyEmail;
    }

    @JsonProperty("CertString")
    public String getCertString() {
        return CertString;
    }

    public void setCertString(String certString) {
        CertString = certString;
    }

    @JsonProperty("Signature")
    public Map<String, String> getSignature() {
        return Signature;
    }

    public void setSignature(Map<String, String> signature) {
        Signature = signature;
    }

    public List<DigestData> getSignatureDTO() {
        return signatureDTO;
    }

    public void setSignatureDTO(List<DigestData> signatureDTO) {
        this.signatureDTO = signatureDTO;
    }

    @JsonProperty("FromDate")
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    @JsonProperty("ToDate")
    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
