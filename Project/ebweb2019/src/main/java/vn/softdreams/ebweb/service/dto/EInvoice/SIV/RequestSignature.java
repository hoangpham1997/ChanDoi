package vn.softdreams.ebweb.service.dto.EInvoice.SIV;

public class RequestSignature {
    private String supplierTaxCode;
    private String templateCode;
    private String hashString;
    private String signature;

    public String getSupplierTaxCode() {
        return supplierTaxCode;
    }

    public void setSupplierTaxCode(String supplierTaxCode) {
        this.supplierTaxCode = supplierTaxCode;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getHashString() {
        return hashString;
    }

    public void setHashString(String hashString) {
        this.hashString = hashString;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
