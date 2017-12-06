package com.example.filip.ethwalletp5.Crypto;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.security.KeyPair;
import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by filip on 06/12/2017.
 */

public class web3jWrapper {

    static Web3j web3;

    static {
        web3 = Web3jFactory.build(new HttpService("https://kovan.infura.io/13umMsrT9rOYIlk9UE8j"));

    }


    public static void getClientVersion() {
        Web3ClientVersion clientVersion;
        try {
            clientVersion = web3.web3ClientVersion().sendAsync().get();
            System.out.println(clientVersion.getWeb3ClientVersion());

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public static int createWallet(KeyPair keyPair, Context context) {

        Log.i(AddressBook.TAG_SECURITY, "1");

        String dir = (Environment.getExternalStorageDirectory() + "/myFile1/test");


        ECKeyPair ecKeyPair = ECKeyPair.create(keyPair);
        try {
            WalletUtils.generateWalletFile("aaa", ecKeyPair, new File(dir) , true);
        } catch (Exception e) {

            Log.i(AddressBook.TAG_SECURITY, e.getMessage());
            Log.i(AddressBook.TAG_SECURITY, e.getStackTrace().toString());
            e.printStackTrace();
            return -1;
        }

        Log.i(AddressBook.TAG_SECURITY, "Successfully created wallet.");
        return 0;


    }
}
