package com.bytesnmaterials.zro.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by mitesh on 9/8/16.
 */
public class ZeroUtil {

    static SecureRandom rnd = new SecureRandom();

    public static String random() {
        SecureRandom rnd = new SecureRandom();
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder( 16 );
        for( int i = 0; i < 16; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();

        /*Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(16);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();*/
    }
}
