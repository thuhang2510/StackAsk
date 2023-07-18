package com.hang.stackask.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class Utility {
    public static String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    public static String getSiteURL(HttpServletRequest request){
        String fullURL = request.getRequestURL().toString();
        return fullURL.substring(0, StringUtils.ordinalIndexOf(fullURL, "/", 5));
    }
}

