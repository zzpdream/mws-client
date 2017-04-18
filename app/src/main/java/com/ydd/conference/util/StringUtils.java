package com.ydd.conference.util;

/**
 * Created by ranfi on 4/5/16.
 */
public class StringUtils {


    public static boolean isEmpty(String str) {

        return "".equals(str) || null == str;
    }


    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
