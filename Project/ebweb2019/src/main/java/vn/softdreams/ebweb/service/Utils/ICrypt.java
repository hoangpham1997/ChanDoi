package vn.softdreams.ebweb.service.Utils;

public interface ICrypt {
    String encode(String plainText) throws Exception;

    String decode(String encodedText) throws Exception;

    String encode(String plainText, String password) throws Exception;

    String decode(String encodedText, String password) throws Exception;
}
