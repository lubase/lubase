package com.lubase.starter.util;

import org.springframework.util.DigestUtils;

import java.math.BigInteger;

/**
 *
 * @author A
 */
public class StringEncodeUtil {
    /**
     * 求取字符串的md5摘要
     *
     * @param str
     * @return
     */
    public static String strToMd5Str(String str) {
        byte[] secretBytes = DigestUtils.md5Digest(str.getBytes());
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
}
