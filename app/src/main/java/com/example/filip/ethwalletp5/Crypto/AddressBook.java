package com.example.filip.ethwalletp5.Crypto;

import android.util.Log;

import org.spongycastle.jcajce.provider.digest.Keccak;
import org.spongycastle.jce.interfaces.ECPrivateKey;
import org.spongycastle.jce.interfaces.ECPublicKey;
import org.spongycastle.util.encoders.Hex;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;

/**
 * Created by filip on 29/11/2017.
 */

public class AddressBook {


    public static final String TAG_SECURITY = "securt";

    private static KeyPairGenerator keyPairGenerator;
    private static ECGenParameterSpec ecGenSpec;

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
        Log.i(TAG_SECURITY, "Added security provider");

        ecGenSpec = new ECGenParameterSpec("secp256k1");

        try {
            keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "SC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            Log.e(TAG_SECURITY, e.getMessage());
            e.printStackTrace();
        }
    }


    public java.security.KeyPair generateAddressPair() {
        return generateAddresses();

    }

    private static java.security.KeyPair generateAddresses() {
        try {
            keyPairGenerator.initialize(ecGenSpec, new SecureRandom());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        java.security.KeyPair pair = keyPairGenerator.generateKeyPair();
        Log.i(TAG_SECURITY, "KeyPair generated.");

        ECPrivateKey privateKey = (ECPrivateKey) pair.getPrivate();
        ECPublicKey publicKeyExpected = (ECPublicKey) pair.getPublic();

        System.out.printf("\nPrivate Key D\n\n" + privateKey.getD().toString(16));

        System.out.printf("\nPublic Key generated X\n" + Hex.toHexString(publicKeyExpected.getQ().getAffineXCoord().getEncoded()));
        System.out.printf("\nPublic Key generated Y\n" + Hex.toHexString(publicKeyExpected.getQ().getAffineYCoord().getEncoded()));

        byte[] publicraw = concatByteArrays(publicKeyExpected.getQ().getAffineXCoord().getEncoded(), publicKeyExpected.getQ().getAffineYCoord().getEncoded());

        Keccak.Digest256 hashed = new Keccak.Digest256();

        byte[] result = hashed.digest(publicraw);

        System.out.printf("\nHashed\n" + Hex.toHexString(result));
        System.out.println();



        return pair;
    }

    private static byte[] concatByteArrays(byte[] a, byte[] b){
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return c;
    }

}
