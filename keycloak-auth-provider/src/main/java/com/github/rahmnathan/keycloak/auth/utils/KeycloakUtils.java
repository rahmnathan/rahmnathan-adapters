package com.github.rahmnathan.keycloak.auth.utils;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class KeycloakUtils {
    private static final Logger logger = LoggerFactory.getLogger(KeycloakUtils.class.getName());

    private KeycloakUtils(){
        // No need to instantiate this
    }

    public static String getAccessToken(String username, String password, ProducerTemplate producerTemplate) {
        String requestBody = buildLoginInfo(username, password);

        Map<String, Object> headers = new HashMap<>();
        headers.put(Exchange.CONTENT_LENGTH, requestBody.getBytes().length);

        String response = producerTemplate.requestBodyAndHeaders("direct:accesstoken", requestBody, headers, String.class);

        JSONObject jsonObject = new JSONObject(response);
        if(jsonObject.has("access_token"))
            return jsonObject.getString("access_token");

        throw new RuntimeException("Failed to get access token.");
    }

    private static String buildLoginInfo(String username, String password) {
        Map<String, String> args = new HashMap<>();
        args.put("grant_type", "password");
        args.put("client_id", "movielogin");
        args.put("username", username);
        args.put("password", password);
        StringBuilder sb = new StringBuilder();
        args.forEach((key, value) -> {
            try {
                sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
                        .append(URLEncoder.encode(value, "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                logger.error("Failed building login info. Parameter could not be encoded.", e);
            }
        });

        return sb.toString().substring(0, sb.length() - 1);
    }
}
