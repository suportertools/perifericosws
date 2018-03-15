package br.com.rtoolsws.utils;

import javax.ws.rs.core.HttpHeaders;

public class Headers {

    public static String get(HttpHeaders headers, String key) {
        try {
            return headers.getRequestHeader(key).get(0);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static String getClient(HttpHeaders headers) {
        try {
            return headers.getRequestHeader("Client").get(0);
        } catch (Exception e) {
            return null;
        }
    }

}
