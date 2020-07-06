package vn.softdreams.ebweb.service.Utils.RestfullAPI_CRM;

import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import vn.softdreams.ebweb.service.Utils.Utils;
import vn.softdreams.ebweb.service.dto.crm.ResponeCRM;

public class RestApiCRMService {

    public static ResponeCRM postCRM(RequestCRM request_sds, String uri) {
        return postActiveCRM(request_sds, uri);
    }

    private static ResponeCRM postActiveCRM(RequestCRM request_sds, String uri) {
        String result = "";
        ResponeCRM respone_sds = new ResponeCRM();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        String requestJson = Utils.ObjectToJSON(request_sds);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        try {
            ResponseEntity<String> rp = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
            result = rp.getBody();
        } catch (HttpServerErrorException e) {
            if (Utils.isJSONValid(e.getResponseBodyAsString())) {
                result = e.getResponseBodyAsString();
            }
        }
        if (!StringUtils.isEmpty(result)) {
            if (Utils.isJSONValid(result)) {
                Object object = Utils.jsonToObject(result, ResponeCRM.class);
                if (object != null) {
                    respone_sds = (ResponeCRM) object;
                }
            } else {

            }
        }
        return respone_sds;
    }

}
