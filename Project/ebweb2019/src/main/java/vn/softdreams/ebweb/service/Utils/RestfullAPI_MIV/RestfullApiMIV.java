package vn.softdreams.ebweb.service.Utils.RestfullAPI_MIV;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.EInvoice.MIV.DataStatus_MIV;
import vn.softdreams.ebweb.service.dto.EInvoice.MIV.Login_MIV;
import vn.softdreams.ebweb.service.dto.EInvoice.MIV.ResponeAdjust;
import vn.softdreams.ebweb.service.dto.EInvoice.MIV.Respone_MIV;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.web.rest.errors.BadRequestAlertException;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

public class RestfullApiMIV {

    public static Respone_MIV post(RequestMIV requestMIV) {
        Respone_MIV responeMiv = new Respone_MIV();
        String path = requestMIV.getUrl();
        if (path.toCharArray()[path.length() - 1] != '/') {
            path += "/";
        }
        final String uri = path + requestMIV.getApi();
        if (!requestMIV.getApi().equals(Constants.EInvoice.API_MIV.Login)) {
            getToken(requestMIV);
        }
        RestTemplate restTemplate = new RestTemplate();
        setTimeOut(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        if (requestMIV.getContentType() != null) {
            headers.setAccept(Arrays.asList(MediaType.ALL));
            headers.set("Content-Type", requestMIV.getContentType());
        } else {
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_JSON_UTF8));
            headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
        headers.set("Authorization", requestMIV.getAuth());
//        headers
        HttpEntity<String> entity = new HttpEntity<String>(requestMIV.getData(), headers);
        try {
            ResponseEntity<String> rp = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            if (Utils.isJSONValid(rp.getBody())) {
                responeMiv = (Respone_MIV) Utils.jsonToObject(rp.getBody(), Respone_MIV.class);
//                JSONObject jsonObject = new JSONObject(rp.getBody());
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                throw new BadRequestAlertException("Không có gói nào được tạo", "", "UNAUTHORIZED");
            }
        } catch (Exception e) {
            String mess = e.getMessage();
        }
        return responeMiv;
    }

    public static ResponeAdjust postAdjust(RequestMIV requestMIV) {
        ResponeAdjust responeMiv = new ResponeAdjust();
        String path = requestMIV.getUrl();
        if (path.toCharArray()[path.length() - 1] != '/') {
            path += "/";
        }
        final String uri = path + requestMIV.getApi();
        getToken(requestMIV);
        RestTemplate restTemplate = new RestTemplate();
        setTimeOut(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        if (requestMIV.getContentType() != null) {
            headers.setAccept(Arrays.asList(MediaType.ALL));
            headers.set("Content-Type", requestMIV.getContentType());
        } else {
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_JSON_UTF8));
            headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
        headers.set("Authorization", requestMIV.getAuth());
//        headers
        HttpEntity<String> entity = new HttpEntity<String>(requestMIV.getData(), headers);
        try {
            ResponseEntity<String> rp = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            if (Utils.isJSONValid(rp.getBody())) {
                responeMiv = (ResponeAdjust) Utils.jsonToObject(rp.getBody(), ResponeAdjust.class);
//                JSONObject jsonObject = new JSONObject(rp.getBody());
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                throw new BadRequestAlertException("Không có gói nào được tạo", "", "UNAUTHORIZED");
            }
        } catch (Exception e) {
            String mess = e.getMessage();
        }
        return responeMiv;
    }

    public static DataStatus_MIV postGetStatusMIV(RequestMIV requestMIV) {
        DataStatus_MIV responeMiv = new DataStatus_MIV();
        String path = requestMIV.getUrl();
        if (path.toCharArray()[path.length() - 1] != '/') {
            path += "/";
        }
        final String uri = path + requestMIV.getApi();
        getToken(requestMIV);
        RestTemplate restTemplate = new RestTemplate();
        setTimeOut(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        if (requestMIV.getContentType() != null) {
            headers.setAccept(Arrays.asList(MediaType.ALL));
            headers.set("Content-Type", requestMIV.getContentType());
        } else {
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_JSON_UTF8));
            headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
        headers.set("Authorization", requestMIV.getAuth());
//        headers
        HttpEntity<String> entity = new HttpEntity<String>(requestMIV.getData(), headers);
        try {
            ResponseEntity<String> rp = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            if (Utils.isJSONValid(rp.getBody())) {
                responeMiv = (DataStatus_MIV) Utils.jsonToObject(rp.getBody(), DataStatus_MIV.class);
//                JSONObject jsonObject = new JSONObject(rp.getBody());
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            }
        } catch (Exception e) {
            String mess = e.getMessage();
        }
        return responeMiv;
    }

    public static Respone_MIV get(RequestMIV requestMIV) {
        Respone_MIV responeMiv = new Respone_MIV();
        String path = requestMIV.getUrl();
        if (path.toCharArray()[path.length() - 1] != '/') {
            path += "/";
        }
        final String uri = path + requestMIV.getApi();
        getToken(requestMIV);
        RestTemplate restTemplate = new RestTemplate();
        setTimeOut(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        if (requestMIV.getContentType() != null) {
            headers.setAccept(Arrays.asList(MediaType.ALL));
            headers.set("Content-Type", requestMIV.getContentType());
        } else {
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_JSON_UTF8));
            headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
        headers.set("Authorization", requestMIV.getAuth());
//        headers
        HttpEntity<String> entity = new HttpEntity<String>(requestMIV.getData(), headers);
        try {
            if (MediaType.APPLICATION_PDF_VALUE.equals(requestMIV.getContentType())) {
                ResponseEntity<byte[]> rp = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
                responeMiv.setOk(true);
                responeMiv.setDataByteArr(rp.getBody());
            } else {
                ResponseEntity<String> rp = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
                if (Utils.isJSONValid(rp.getBody())) {
                    responeMiv = (Respone_MIV) Utils.jsonToObject(rp.getBody(), Respone_MIV.class);
//                JSONObject jsonObject = new JSONObject(rp.getBody());
                }
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            }
        } catch (Exception e) {
            String mess = e.getMessage();
        }
        return responeMiv;
    }

    private static void getToken(RequestMIV requestMIV) {
        Respone_MIV responeMiv = new Respone_MIV();

        Login_MIV login_miv = new Login_MIV();
        login_miv.setUsername(requestMIV.getUserName());
        login_miv.setPassword(requestMIV.getPassword());
        login_miv.setMa_dvcs("VP");
        String jsonData = Utils.ObjectToJSON(login_miv);

        String path = requestMIV.getUrl();
        if (path.toCharArray()[path.length() - 1] != '/') {
            path += "/";
        }
        final String uri = path + Constants.EInvoice.API_MIV.Login;
        RestTemplate restTemplate = new RestTemplate();
        setTimeOut(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
//        headers
        HttpEntity<String> entity = new HttpEntity<String>(jsonData, headers);
        try {
            ResponseEntity<String> rp = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            if (Utils.isJSONValid(rp.getBody())) {
                responeMiv = (Respone_MIV) Utils.jsonToObject(rp.getBody(), Respone_MIV.class);
//                JSONObject jsonObject = new JSONObject(rp.getBody());
                if (!StringUtils.isEmpty(responeMiv.getToken())) {
                    requestMIV.setAuth("Bear " + responeMiv.getToken() + ";VP");
                }
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                throw new BadRequestAlertException("Không có gói nào được tạo", "", "UNAUTHORIZED");
            }
        } catch (Exception e) {
            String mess = e.getMessage();
        }
    }

    private static void setTimeOut(RestTemplate restTemplate) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        int tenSeconds = 10000;
        requestFactory.setReadTimeout(tenSeconds);
        requestFactory.setConnectTimeout(tenSeconds);
        restTemplate.setRequestFactory(requestFactory);
    }


    private static boolean checkUrl(String url) {
        return isURL(url);
    }


    private static String encodeToBase64(String value) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(value.getBytes("UTF-8"));
    }

    private static String decodeToBase64(String value) throws UnsupportedEncodingException {
        return new String(Base64.getDecoder().decode(value), "UTF-8");
    }

    private static boolean isURL(String url) {
        try {
            (new java.net.URL(url)).openStream().close();
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

}
