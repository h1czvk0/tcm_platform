package com.tcm.platform.utils;

import cn.hutool.crypto.digest.MD5;

public class Md5Util {

    private static final String SALT = "tcm_platform_salt_2024";

    /**
     * MD5加密
     */
    public static String encrypt(String password) {
        if (password == null || password.isEmpty()) {
            return null;
        }
        return MD5.create().digestHex(password + SALT);
    }

    /**
     * 验证密码
     */
    public static boolean verify(String password, String encryptedPassword) {
        if (password == null || encryptedPassword == null) {
            return false;
        }
        return encrypt(password).equals(encryptedPassword);
    }
}
