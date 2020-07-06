package vn.softdreams.ebweb.service.Utils.RestfullAPI_SIV;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.EInvoice.SIV.ResponeCreateListInvoice;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class RestfullApiSIV {

    public static ResponeSIV post(RequestSIV requestSIV) {
        ResponeSIV responeSIV = new ResponeSIV();
        String userPass = requestSIV.getUserName() + ":" + requestSIV.getPassword();
        String auth = null;
        try {
            auth = "Basic " + encodeToBase64(userPass);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String path = requestSIV.getUrl();
        if (path.toCharArray()[path.length() - 1] != '/') {
            path += "/";
        }

        final String uri = path + requestSIV.getApi();

        RestTemplate restTemplate = new RestTemplate();
        setTimeOut(restTemplate);
        HttpHeaders headers = new HttpHeaders();
        if (requestSIV.getContentType() != null) {
            headers.setAccept(Arrays.asList(MediaType.ALL));
            headers.set("Content-Type", requestSIV.getContentType());
        } else {
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_JSON_UTF8));
            headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
        headers.set("Authorization", auth);
//        headers
        HttpEntity<String> entity = new HttpEntity<String>(requestSIV.getData(), headers);
        try {
            ResponseEntity<String> rp = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            if (Utils.isJSONValid(rp.getBody())) {
                responeSIV = (ResponeSIV) Utils.jsonToObject(rp.getBody(), ResponeSIV.class);
                JSONObject jsonObject = new JSONObject(rp.getBody());
                if (jsonObject.has("createInvoiceOutputs")) {
                    String newJSONString = jsonObject.get("createInvoiceOutputs").toString();
                    List<ResponeCreateListInvoice> responeCreateListInvoices = Utils.readList(newJSONString, ResponeCreateListInvoice.class);
                    if (responeSIV == null) {
                        responeSIV = new ResponeSIV();
                    }
                    responeSIV.setCreateInvoiceOutputs(responeCreateListInvoices);
                }
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            }
        } catch (Exception e) {
            String mess = e.getMessage();
        }
        return responeSIV;
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
