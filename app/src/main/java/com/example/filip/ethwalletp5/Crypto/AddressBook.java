package com.example.filip.ethwalletp5.Crypto;

import android.util.Log;

import org.spongycastle.jce.interfaces.ECPrivateKey;
import org.spongycastle.jce.interfaces.ECPublicKey;

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


    public int generateAddressPair() {
        return generateAddresses();

    }

    private static int generateAddresses() {
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



        }





        return 0;
    }

}
