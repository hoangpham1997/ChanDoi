package vn.softdreams.ebweb.service.dto;

public class ExampleDTO {
    private String detail1;
    private String detail2;

    public ExampleDTO(String detail1, String detail2) {
        this.detail1 = detail1;
        this.detail2 = detail2;
    }

    public String getDetail1() {
        return detail1;
    }

    public void setDetail1(String detail1) {
        this.detail1 = detail1;
    }

    public String getDetail2() {
        return detail2;
    }

    public void setDetail2(String detail2) {
        this.detail2 = detail2;
    }
}
