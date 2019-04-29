package com.example.codi.crypto;

import android.util.Base64;

/**
 *
 * @author Alejandro Reynoso
 */
public class Utils {

    private static String[][] parseRule = {
            {"+", "%2B"},
            {"/", "%2F"},
            {"=", "%3D"}
    };

    /**
     * Formatea la url para recuperar los carateres especiales de Base64
     * @param url_sp
     * @return
     */
    public static String parseURLtoBase64(String url_sp){
        String url = url_sp;

        for (int i = 0; i < parseRule.length; i++) {
            if(url.contains(parseRule[i][1])){
                url = url.replaceAll(parseRule[i][1], parseRule[i][0]);
            }
        }

        return url;
    }

    /**
     * Codifica el byteArray a un String de base64
     * @param binaryData
     * @return Codificación base64
     */
    public static String encodeByteToString(byte[] binaryData){
        return Base64.encodeToString(binaryData, Base64.DEFAULT);
    }

    /**
     * Decodifica el String de base64 a un byteArray
     * @param base64String
     * @return Decodificación base64
     */
    public static byte[] decodeBase64StringToByte(String base64String){
        return Base64.decode(base64String, Base64.DEFAULT);
    }

    /**
     * Verifica si una cadena viene nula o vacia.
     * @param data
     * @return
     */
    public static boolean isEmpty(String data){
        if (data == null) {
            return true;
        }

        if (data.trim().length() == 0) {
            return true;
        }

        return false;
    }

    public static boolean validateLengthStrKeySrc( String strKeySrc ){
        if (strKeySrc.length() == 128) {
            return true;
        }
        return false;
    }

    public static byte[] getKeyAES(String strKeySrc){
        return hexStringToByteArray(strKeySrc.substring(0, 32));
    }

    public static byte[] getIvAES(String strKeySrc){
        return hexStringToByteArray(strKeySrc.substring(32, 64));
    }

    public static byte[] getKeyHmac(String strKeySrc){
        return hexStringToByteArray(strKeySrc.substring(64, 128));
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String byteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}