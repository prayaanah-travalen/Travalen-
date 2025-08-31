package com.app.travelo.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class PhonePeUtil {
    private static final String SEPARATOR_VALUE = "###";

    public static String generateChecksumHeader(String checksumBody, int apiKeyIndex) {
        return String.join("###", checksumBody, Integer.toString(apiKeyIndex));
    }

    public static String generateChecksumBody(String b64RequestBody, String url, String apiKey) {
        String hex = (StringUtils.isNotBlank(b64RequestBody) ? b64RequestBody : "").concat(url).concat(apiKey);
        return sha256Hex(hex);
    }

    public static String decodeBase64(String encodedString) {
        return org.apache.commons.codec.binary.StringUtils.newStringUtf8(Base64.decodeBase64(encodedString));
    }

    public static String encodeBase64(String requestString) {
        return org.apache.commons.codec.binary.StringUtils.newStringUtf8(Base64.encodeBase64(requestString.getBytes()));
    }

    public static String generateCheckSum(String payloadString, String url, String saltKey, Integer saltIndex) {
        String encodedData = encodeBase64(payloadString);
        String hashedString = generateChecksumBody(encodedData, url, saltKey);
        return generateChecksumHeader(hashedString, saltIndex);
    }

    public static String shaHex(String data, String algorithm) {
        switch (algorithm) {
            case "SHA256":
                return DigestUtils.sha256Hex(data);
            default:
                return data;
        }
    }

    public static String sha256Hex(String data) {
        return shaHex(data, "SHA256");
    }

    public static byte[] encrypt(byte[] publicKey, byte[] inputData) {
        try {
            PublicKey key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(1, key);
            byte[] encryptedBytes = cipher.doFinal(inputData);
            return java.util.Base64.getEncoder().encode(encryptedBytes);
        } catch (Throwable var5) {
            return null;
        }
    }

    public static String getEncryptedData(String publicKey, String data) {
        try {
            byte[] decoded = java.util.Base64.getDecoder().decode(publicKey);
            byte[] encryptedData = encrypt(decoded, data.getBytes());
            return new String(encryptedData);
        } catch (Throwable var4) {
            throw var4;
        }
    }
//
//    @Generated
//    private CommonUtils() {
//        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
//    }

    private static enum SHA_ALGORITHM {
        SHA256;

        private SHA_ALGORITHM() {
        }
    }

    public static String getStatusChecksumHeader(String url, String saltKey, Integer saltIndex) {
        String hex = url.concat(saltKey);
        return generateChecksumHeader(sha256Hex(hex), saltIndex);
    }
}


//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
