package vn.softdreams.ebweb.service.dto.EInvoice.SDS;

import javax.xml.bind.annotation.XmlElement;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ResponseData {
    public String Pattern;
    public String Serial;
    public Map<UUID, String> KeyInvoiceNo;
    public Map<UUID, String> KeyInvoiceMsg;
    private List<KeyInvoiceMsg> keyInvoiceMsgDTO;
    private List<KeyInvoiceNo> keyInvoiceNoDTO;
    public String Html;
    public Integer InvoiceStatus;
    public List<String> InvoiceNo;
    public Map<String, String> DigestData;
    public List<DigestData> DigestDataDTO;
    public List<UUID> Ikeys;
    public String XmlReport;
    public List<InvoicesSDS> Invoices;

    @XmlElement(name = "Pattern")
    public String getPattern() {
        return Pattern;
    }

    public void setPattern(String pattern) {
        Pattern = pattern;
    }

    @XmlElement(name = "Serial")
    public String getSerial() {
        return Serial;
    }

    public void setSerial(String serial) {
        Serial = serial;
    }

    @XmlElement(name = "KeyInvoiceNo")
    public Map<UUID, String> getKeyInvoiceNo() {
        return KeyInvoiceNo;
    }

    public void setKeyInvoiceNo(Map<UUID, String> keyInvoiceNo) {
        KeyInvoiceNo = keyInvoiceNo;
    }

    @XmlElement(name = "KeyInvoiceMsg")
    public Map<UUID, String> getKeyInvoiceMsg() {
        return KeyInvoiceMsg;
    }

    public void setKeyInvoiceMsg(Map<UUID, String> keyInvoiceMsg) {
        KeyInvoiceMsg = keyInvoiceMsg;
    }

    @XmlElement(name = "Html")
    public String getHtml() {
        return Html;
    }

    public void setHtml(String html) {
        Html = html;
    }

    @XmlElement(name = "InvoiceStatus")
    public Integer getInvoiceStatus() {
        return InvoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        InvoiceStatus = invoiceStatus;
    }

    @XmlElement(name = "InvoiceNo")
    public List<String> getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(List<String> invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    @XmlElement(name = "DigestData")
    public Map<String, String> getDigestData() {
        return DigestData;
    }

    public void setDigestData(Map<String, String> digestData) {
        DigestData = digestData;
    }

    @XmlElement(name = "XmlReport")
    public String getXmlReport() {
        return XmlReport;
    }

    public void setXmlReport(String xmlReport) {
        XmlReport = xmlReport;
    }

    @XmlElement(name = "Ikeys")
    public List<UUID> getIkeys() {
        return Ikeys;
    }

    public void setIkeys(List<UUID> ikeys) {
        Ikeys = ikeys;
    }

    public List<vn.softdreams.ebweb.service.dto.EInvoice.SDS.KeyInvoiceMsg> getKeyInvoiceMsgDTO() {
        return keyInvoiceMsgDTO;
    }

    public void setKeyInvoiceMsgDTO(List<vn.softdreams.ebweb.service.dto.EInvoice.SDS.KeyInvoiceMsg> keyInvoiceMsgDTO) {
        this.keyInvoiceMsgDTO = keyInvoiceMsgDTO;
    }

    public List<vn.softdreams.ebweb.service.dto.EInvoice.SDS.KeyInvoiceNo> getKeyInvoiceNoDTO() {
        return keyInvoiceNoDTO;
    }

    public void setKeyInvoiceNoDTO(List<vn.softdreams.ebweb.service.dto.EInvoice.SDS.KeyInvoiceNo> keyInvoiceNoDTO) {
        this.keyInvoiceNoDTO = keyInvoiceNoDTO;
    }

    public List<InvoicesSDS> getInvoices() {
        return Invoices;
    }

    public void setInvoices(List<InvoicesSDS> invoices) {
        Invoices = invoices;
    }

    public List<vn.softdreams.ebweb.service.dto.EInvoice.SDS.DigestData> getDigestDataDTO() {
        return DigestDataDTO;
    }

    public void setDigestDataDTO(List<vn.softdreams.ebweb.service.dto.EInvoice.SDS.DigestData> digestDataDTO) {
        DigestDataDTO = digestDataDTO;
    }
}
