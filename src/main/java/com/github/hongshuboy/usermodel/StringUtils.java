package com.github.hongshuboy.usermodel;

/**
 * @author hongshuboy
 */
public class StringUtils {
    public static boolean notEmpty(Object o) {
        return o != null && !o.equals("");
    }
}
