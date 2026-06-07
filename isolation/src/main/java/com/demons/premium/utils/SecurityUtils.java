package com.demons.premium.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utility class untuk security operations
 */
public class SecurityUtils {
    
    /**
     * Generate SHA-256 hash
     */
    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            Logger.e("Failed to generate SHA-256 hash", e);
            return null;
        }
    }
    
    /**
     * Generate MD5 hash
     */
    public static String md5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(input.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            Logger.e("Failed to generate MD5 hash", e);
            return null;
        }
    }
    
    /**
     * Convert bytes to hex string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    /**
     * Encode string to Base64
     */
    public static String encodeBase64(String input) {
        try {
            return Base64.getEncoder().encodeToString(input.getBytes());
        } catch (Exception e) {
            Logger.e("Failed to encode Base64", e);
            return null;
        }
    }
    
    /**
     * Decode string from Base64
     */
    public static String decodeBase64(String input) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(input);
            return new String(decodedBytes);
        } catch (Exception e) {
            Logger.e("Failed to decode Base64", e);
            return null;
        }
    }
}
