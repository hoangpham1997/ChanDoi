package vn.softdreams.ebweb.web.rest.dto;

import vn.softdreams.ebweb.domain.Supplier;

import java.util.List;

public class ConnectEInvoiceDTO {
    private String path;
    private String supplierCode;
    private String userName;
    private String password;
    private Boolean useInvoceWait;
    private Boolean useToken;
    private List<Supplier> suppliers;
    private String token;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getUseInvoceWait() {
        return useInvoceWait;
    }

    public void setUseInvoceWait(Boolean useInvoceWait) {
        this.useInvoceWait = useInvoceWait;
    }

    public Boolean getUseToken() {
        return useToken;
    }

    public void setUseToken(Boolean useToken) {
        this.useToken = useToken;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
