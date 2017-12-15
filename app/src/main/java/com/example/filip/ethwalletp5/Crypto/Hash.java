package com.example.filip.ethwalletp5.Crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class Hash {
    public static String stringHash( String text) {
        try {
            MessageDigest md = MessageDigest.getInstance( "SHA-256" );

            // Change this to UTF-16 if needed
            md.update( text.getBytes( StandardCharsets.UTF_8 ) );
            byte[] digest = md.digest();

            String hex = String.format( "%064x", new BigInteger( 1, digest ) );
            System.out.println( hex );

            return hex;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
