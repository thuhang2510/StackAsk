package com.hang.stackask.utils;

import java.io.UnsupportedEncodingException;

public class StringUtil {
    public static final String UTF_8 = "UTF-8";

    public static boolean isNoContent(String value) {
        return (value == null || value.isEmpty() || value.trim().isEmpty());
    }

    public static String newUtf(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, UTF_8);
    }
}
