package com.tea.framework.utils;
import org.apache.commons.codec.digest.DigestUtils;

public class SecurityUtils {

    public SecurityUtils() {
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }


    public static String md5(String plan) {
        return DigestUtils.md5Hex(plan);
    }

    public static String sha512(String plan) {
        return DigestUtils.sha512Hex(plan);
    }

}