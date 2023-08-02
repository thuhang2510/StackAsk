package com.hang.stackask.converter;

public class StringToTypePrimitiveConverter {
    public static int toInt(String str){
        Integer integer = Integer.valueOf(str);
        return integer.intValue();
    }
}
