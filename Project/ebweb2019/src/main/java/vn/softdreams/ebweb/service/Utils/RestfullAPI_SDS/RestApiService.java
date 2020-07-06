package vn.softdreams.ebweb.service.Utils.RestfullAPI_SDS;

import javassist.bytecode.ByteArray;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.EInvoice.SDS.Respone_SDS;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RestApiService {

    public static Respone_SDS post(Request_SDS request_sds, String API, String path, String userName, String password) {
        return post(request_sds, API, null, path, userName, password);
    }

    public static Respone_SDS post(Request_SDS request_sds, String API, UserDTO userDTO) {
        return post(request_sds, API, userDTO, null, null, null);
    }

    private static Respone_SDS post(Request_SDS request_sds, String API, UserDTO userDTO, String path, String userName, String password) {
        String result = "";
        Respone_SDS respone_sds = new Respone_SDS();
        if (userDTO != null) {
            userName = Utils.getEI_TenDangNhap(userDTO);
            password = Utils.getEI_MatKhau(userDTO);
            path = Utils.getEI_Path(userDTO);
        } else {
            if (!isURL(path)) {
                respone_sds.setStatus(1);
                respone_sds.setMessage("Đường dẫn truy cập không hợp lệ");
                return respone_sds;
            }
        }
        if (path.toCharArray()[path.length() - 1] != '/') {
            path += "/";
        }
        final String uri = path + API;

        RestTemplate restTemplate = new RestTemplate();
        setTimeOut(restTemplate);
        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_OCTET_STREAM));
        headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        String Authentication = GenerateToken(HttpMethod.POST, userName, password);
        String requestJson = Utils.ObjectToJSON(request_sds);
        headers.set("Authentication", Authentication);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        // Note the body object as first parameter!
        if (Constants.EInvoice.API_SDS.GET_INVOCIE_PDF.equals(API)) {
            try {
                ResponseEntity<byte[]> rp = restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class);
                respone_sds.setStatus(2);
                String fileName = rp.getHeaders().toSingleValueMap().get("Content-Disposition");
                if (!StringUtils.isEmpty(fileName)) {
                    for (String str : fileName.split(";")
                    ) {
                        if (str.contains("filename=")) {
                            respone_sds.setFileName(str.trim().substring("filename=".length()));
                        }
                    }
                }
                respone_sds.setRawBytes(rp.getBody());
            } catch (HttpClientErrorException e) {
                result = e.getResponseBodyAsString();
                if (Utils.isJSONValid(result)) {
                    respone_sds = (Respone_SDS) Utils.jsonToObject(e.getResponseBodyAsString(), Respone_SDS.class);
                } else {
                    respone_sds.setStatus(1);
                    respone_sds.setMessage(result);
                }
            }
        } else {
            try {
                ResponseEntity<String> rp = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
                result = rp.getBody();
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                    if (!Utils.isJSONValid(e.getResponseBodyAsString())) {
                        respone_sds.setStatus(4);
                        respone_sds.setMessage("Đường dẫn truy cập không hợp lệ");
                    } else {
                        if (userDTO == null) {
                            Respone_SDS respone_sds1 = (Respone_SDS) Utils.jsonToObject(e.getResponseBodyAsString(), Respone_SDS.class);
                            if (respone_sds1.getStatus().equals(4)) {
                                respone_sds.setStatus(2); // không tồn tại key trên hệ thông hddt
                            } else {
                                respone_sds = respone_sds1;
                            }
                        } else {
                            result = e.getResponseBodyAsString();
                        }

                    }
                } else if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                    respone_sds.setStatus(4);
                    respone_sds.setMessage("Tài khoản hoặc mật khẩu không hợp lệ");
                } else if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                    if (userDTO == null) {
                        respone_sds.setStatus(2); // kết nối mk, pass đúng, thiếu ikeys
                    } else {
                        result = e.getResponseBodyAsString();
                    }
                } else {
                    result = e.getResponseBodyAsString();
                }
            } catch (HttpServerErrorException e) {
                if (Utils.isJSONValid(e.getResponseBodyAsString())) {
                    result = e.getResponseBodyAsString();
                }
            }
            if (!StringUtils.isEmpty(result)) {
                if (Utils.isJSONValid(result)) {
                    Object object = Utils.jsonToObject(result, Respone_SDS.class);
                    if (object != null) {
                        respone_sds = (Respone_SDS) object;
                    }
                } else {
                    respone_sds.setStatus(Constants.EInvoice.Respone.Success);
                    respone_sds.setHtml(result);
                }
            }
        }

        return respone_sds;
    }

    /**
     * @param method
     * @param username
     * @param password
     * @return
     * @Author Hautv
     */
    private static String GenerateToken(HttpMethod method, String username, String password) {
        String result = "";
        DateTime epochStart = new DateTime(1970, 01, 01, 0, 0, 0, DateTimeZone.UTC);
        long millis = DateTime.now(DateTimeZone.UTC).getMillis() - epochStart.getMillis();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        String timestamp = String.valueOf(seconds);
        String nonce = UUID.randomUUID().toString().toLowerCase();
        String signatureRawData = method.name().toUpperCase() + timestamp + nonce;
        String hashMD5 = Utils.MD5(signatureRawData);
        result = hashMD5 + ":" + nonce + ":" + timestamp + ":" + username + ":" + password;
        return result;
    }

    private static boolean isURL(String url) {
        try {
            (new java.net.URL(url)).openStream().close();
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

    private static void setTimeOut(RestTemplate restTemplate) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        int tenSeconds = 10000;
        requestFactory.setReadTimeout(tenSeconds);
        requestFactory.setConnectTimeout(tenSeconds);
        restTemplate.setRequestFactory(requestFactory);
    }
}
