package vn.softdreams.ebweb.service.Utils.RestfullAPI_SIV;

public class RequestSIV {

    private String userName;
    private String password;
    private String codeTax;
    private String api;
    private String url;
    private String contentType;
    private String data; //Json, XML,...

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

    public String getCodeTax() {
        return codeTax;
    }

    public void setCodeTax(String codeTax) {
        this.codeTax = codeTax;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
