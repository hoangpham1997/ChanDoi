package vn.softdreams.ebweb.web.rest.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.http.MediaType;
import vn.softdreams.ebweb.web.rest.dto.UpdateDataDTO;

import java.util.List;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private static final String APPLICATION_NAME = "ebwebApp";

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-alert", message);
        headers.add("X-" + APPLICATION_NAME + "-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-error", "error." + errorKey);
        headers.add("X-" + APPLICATION_NAME + "-params", entityName);
        return headers;
    }
    // add by anmt
    public static HttpHeaders createEntityUnrecordedAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".unrecorded", param);
    }
    public static HttpHeaders createEntityRecordedAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".recorded", param);
    }
    public static HttpHeaders updateData(UpdateDataDTO updateDataDTOS) {
        HttpHeaders headers = new HttpHeaders();
        if (updateDataDTOS != null) {
//            ObjectMapper mapper = new ObjectMapper();
//            try {
//                String secret = Base64Utils.encodeToString((mapper.writeValueAsString(updateDataDTOS).getBytes()));
//                headers.add("X-" + APPLICATION_NAME + "-update-data", secret);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
            headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        }
        return headers;
    }
}
