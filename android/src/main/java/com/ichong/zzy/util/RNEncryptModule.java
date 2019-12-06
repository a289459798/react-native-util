
package com.ichong.zzy.util;

import android.util.Base64;
import com.facebook.react.bridge.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class RNEncryptModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNEncryptModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNEncrypt";
    }

    @ReactMethod
    public void string2GBK(final String str,
                           final Callback callback) {

        String encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                callback.invoke(new String(str.getBytes(encode), "GBK"));
                return;
            }
        } catch (Exception exception1) {
        }
        callback.invoke(str);
    }

    @ReactMethod
    public void decode(final String str,
                       String key,
                       final Promise promise) {

        key = string2MD5(key);
        byte[] bstr = Base64.decode(str, Base64.NO_WRAP);
        int keylen = key.length();
        int strlen = bstr.length;

        byte resultByte[] = new byte[bstr.length];
        for (int i = 0; i < strlen; i++) {
            int k = i % keylen;
            int x = bstr[i];
            int y = key.charAt(k);
            byte z = (byte) (x ^ y);
            resultByte[i] = z;
        }

        try {

            String r = new String(resultByte, "utf-8");

            promise.resolve(r);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            promise.reject(e);
        }

    }

    @ReactMethod
    public void encode(final String str,
                       String key,
                       final Promise promise) {

        key = string2MD5(key);
        byte[] bstr = str.getBytes();
        int keylen = key.length();
        int strlen = bstr.length;

        byte resultByte[] = new byte[bstr.length];
        for (int i = 0; i < strlen; i++) {
            int k = i % keylen;
            int x = bstr[i];
            int y = key.charAt(k);
            byte z = (byte) (x ^ y);
            resultByte[i] = z;
        }

        try {
            String r = new String(Base64.encode(resultByte, Base64.NO_WRAP), "utf-8");
            promise.resolve(r);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            promise.reject(e);
        }
    }

    private String string2MD5(String inStr) {
        if (inStr == null || "".equals(inStr)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }
}