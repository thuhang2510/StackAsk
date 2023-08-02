package com.hang.stackask.utils;

public class CustomFileNameUtil {
    public static String changeFileName(String oldFileName, String newFileName){
        String extension = oldFileName.substring(oldFileName.lastIndexOf('.'));
        return newFileName + extension;
    }
}
