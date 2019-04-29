package com.example.codi.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class CipherData {
    static final String CIPHER_TYPE = "AES/CBC/PKCS5Padding";

    public static String HMACEncode(byte[] keyHmac, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(keyHmac, "HmacSHA256");
        sha256_HMac.init(secret_key);
        String hash = Base64.encodeToString(sha256_HMac.doFinal(message), Base64.DEFAULT);
        return hash;
    }

    public static String AESEncode(byte[] message, byte[] key, byte[] IV) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        IvParameterSpec iv = new IvParameterSpec(IV);
        SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);

        byte[] encrypted = cipher.doFinal(message);

        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public static String AESDecode(byte[] message, byte[] key, byte[] IV) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        IvParameterSpec iv = new IvParameterSpec(IV);
        SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);
        byte[] original = cipher.doFinal(message);

        return new String(original);
    }
}