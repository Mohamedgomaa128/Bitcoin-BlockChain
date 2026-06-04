package com.blockchain.util;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public final class HexUtil {
    private HexUtil() {
    }

    public static String toHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            result.append(String.format("%02x", b & 0xff));
        }
        return result.toString();
    }

    public static byte[] fromHex(String hex) {
        if (hex == null || hex.isBlank()) {
            return null;
        }
        String normalized = hex.trim();
        if (normalized.length() % 2 != 0) {
            throw new IllegalArgumentException("Hex value must have an even length");
        }
        byte[] bytes = new byte[normalized.length() / 2];
        for (int i = 0; i < normalized.length(); i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(normalized.substring(i, i + 2), 16);
        }
        return bytes;
    }

    public static String publicKeyToHex(PublicKey publicKey) {
        return toHex(publicKey.getEncoded());
    }

    public static PublicKey publicKeyFromHex(String hex) {
        try {
            byte[] encoded = fromHex(hex);
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encoded));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid RSA public key hex", ex);
        }
    }
}
