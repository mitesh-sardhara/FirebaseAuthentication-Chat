package com.bytesnmaterials.zro.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Mahya on 2/4/2016.
 */
public class ZeroEnc {
    public static String encodeString(String inputString) {
        String encodedString = "";
        if (inputString != null && !inputString.equals("")) {
            try {
                encodedString = URLEncoder.encode(inputString, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return encodedString;
    }

    public static String decodeString(String encodedString) {
        String decodedString = "";
        if (encodedString != null && !encodedString.equals("")) {
            try {
                decodedString = URLEncoder.encode(encodedString, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return decodedString;
    }
}
