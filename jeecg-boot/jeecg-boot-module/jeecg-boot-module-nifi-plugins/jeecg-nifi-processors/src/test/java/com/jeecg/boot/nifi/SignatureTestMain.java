package com.jeecg.boot.nifi;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SignatureTestMain {

    private static String nvl(String value) {
        return value == null ? "" : value;
    }

    private static String sha256Hex(String data, Charset charset) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = data.getBytes(charset);
        byte[] hash = digest.digest(bytes);
        StringBuilder sb = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16));
            sb.append(Character.forDigit((b & 0xF), 16));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String appKey = args.length > 0 ? args[0] : "43b396fe9f7542d8a24a96acc8c70b5e";
        String appSecret = args.length > 1 ? args[1] : "e529aabca7c44ae38de38d881e768593";
        String supplier = args.length > 2 ? args[2] : "NEWGRAND";
        String timestamp = args.length > 3 ? args[3] : String.valueOf(System.currentTimeMillis());

        String stringToSign = (appSecret + appKey + timestamp + nvl(supplier) + appSecret).toLowerCase();
        String sign = sha256Hex(stringToSign, StandardCharsets.UTF_8).toLowerCase();

        System.out.println("appKey=" + appKey);
        System.out.println("supplier=" + nvl(supplier));
        System.out.println("timestamp=" + timestamp);
        System.out.println("stringToSign=" + stringToSign);
        System.out.println("sign=" + sign);
    }
}


