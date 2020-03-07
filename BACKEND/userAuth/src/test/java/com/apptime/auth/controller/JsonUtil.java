package com.apptime.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Qi Zhang
 * The Util class for Testing, which would parse JSON data:Wq
 */
public class JsonUtil {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
